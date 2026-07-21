package cn.iocoder.yudao.module.childhealth.job;

import cn.iocoder.yudao.module.childhealth.dal.dataobject.integration.DataPushFailLogDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.integration.DataPushTaskDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.integration.DataPushFailLogMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.integration.DataPushTaskMapper;
import org.springframework.scheduling.annotation.Scheduled;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 数据上报任务执行器
 *
 * 创建日期: 2026-07-20
 * 模块: 2. 数据上报对接
 *
 * 调度策略：
 *   - 每 1 分钟扫描一次 data_push_task 表
 *   - 处理 push_status=0（待推送）和 push_status=3（失败且到达重试时间）的任务
 *   - 优先级小的先处理（priority=1 高优先级）
 *   - 状态机：0待推送 → 1推送中 → 2成功 / 3失败 / 4部分成功
 *   - 失败任务按指数退避策略重试：next_retry_time = now + 2^retryCount 分钟
 *   - 超过 max_retry（默认 5 次）后置为 3 失败，需人工介入
 *
 * 触发方式：
 *   - XXL-JOB 调度：childhealthDataPushJob
 *   - 手动触发：调用 DataPushTaskController.retry(taskId)
 */
@Slf4j
@Component
public class DataPushJob {

    /** 默认最大重试次数 */
    private static final int DEFAULT_MAX_RETRY = 5;

    /** 单次扫描处理的最大任务数 */
    private static final int BATCH_SIZE = 50;

    @Resource
    private DataPushTaskMapper dataPushTaskMapper;
    @Resource
    private DataPushFailLogMapper dataPushFailLogMapper;
    // @Resource
    // private DataPushClient dataPushClient; // 上报客户端（HTTP/Feign）

    /**
     * 数据上报任务执行入口
     */
    @Scheduled(fixedRate = 60000)
    public void execute() {
        log.info("[数据上报] 开始扫描待推送任务...");
        // 1. 处理待推送任务
        List<DataPushTaskDO> pendingList = dataPushTaskMapper.selectPendingList();
        int total = Math.min(pendingList.size(), BATCH_SIZE);
        log.info("[数据上报] 共 {} 个待推送任务，本次处理 {}", pendingList.size(), total);

        int successCount = 0;
        int failCount = 0;
        for (int i = 0; i < total; i++) {
            DataPushTaskDO task = pendingList.get(i);
            try {
                pushTask(task);
                successCount++;
            } catch (Exception e) {
                failCount++;
                log.error("[数据上报] 任务 #{} ({}) 推送失败", task.getId(), task.getTaskNo(), e);
                handlePushFailure(task, e);
            }
        }

        // 2. 处理需重试的失败任务
        List<DataPushTaskDO> retryList = dataPushTaskMapper.selectRetryList();
        int retryTotal = Math.min(retryList.size(), BATCH_SIZE);
        log.info("[数据上报] 共 {} 个重试任务，本次处理 {}", retryList.size(), retryTotal);
        int retrySuccess = 0;
        int retryFail = 0;
        for (int i = 0; i < retryTotal; i++) {
            DataPushTaskDO task = retryList.get(i);
            try {
                pushTask(task);
                retrySuccess++;
            } catch (Exception e) {
                retryFail++;
                log.error("[数据上报] 重试任务 #{} ({}) 失败", task.getId(), task.getTaskNo(), e);
                handlePushFailure(task, e);
            }
        }

        log.info("[数据上报] 本次完成：待推送[成功 {} / 失败 {}]，重试[成功 {} / 失败 {}]",
                successCount, failCount, retrySuccess, retryFail);
    }

    /**
     * 推送单个任务
     */
    private void pushTask(DataPushTaskDO task) {
        long startTime = System.currentTimeMillis();
        // 1. 标记为推送中
        markInProgress(task);
        // 2. 调用上报客户端（此处为模拟实现）
        String responseData;
        String requestId;
        try {
            // 模拟上报调用（实际应替换为 dataPushClient.push(task)）
            Thread.sleep(200);
            // 80% 成功率模拟
            if (Math.random() < 0.2) {
                throw new RuntimeException("模拟上报失败：目标系统返回 500");
            }
            responseData = "{\"code\":0,\"msg\":\"success\",\"data\":{\"id\":\"RPT" + System.currentTimeMillis() + "\"}}";
            requestId = "REQ" + System.currentTimeMillis();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("上报被中断", e);
        }
        int costTime = (int) (System.currentTimeMillis() - startTime);
        // 3. 标记为成功
        markSuccess(task, requestId, responseData, costTime);
        log.info("[数据上报] 任务 #{} ({}) 推送成功，耗时 {}ms", task.getId(), task.getTaskNo(), costTime);
    }

    /**
     * 处理推送失败：记录失败日志 + 设置下次重试时间
     */
    private void handlePushFailure(DataPushTaskDO task, Exception e) {
        // 1. 写入失败日志
        DataPushFailLogDO failLog = new DataPushFailLogDO();
        failLog.setTaskId(task.getId());
        failLog.setAttemptNo(task.getRetryCount() == null ? 1 : task.getRetryCount() + 1);
        failLog.setErrorCode("PUSH_FAIL");
        failLog.setErrorMsg(e.getMessage() != null ? e.getMessage().substring(0, Math.min(e.getMessage().length(), 500)) : "未知错误");
        failLog.setErrorType(classifyError(e));
        failLog.setRequestPayload(task.getDataPayload());
        failLog.setResponsePayload(null);
        failLog.setHttpStatus(500);
        failLog.setCostTimeMs(null);
        failLog.setFailTime(LocalDateTime.now());
        dataPushFailLogMapper.insert(failLog);

        // 2. 更新任务状态：重试或最终失败
        int maxRetry = task.getMaxRetry() != null ? task.getMaxRetry() : DEFAULT_MAX_RETRY;
        int newRetryCount = (task.getRetryCount() == null ? 0 : task.getRetryCount()) + 1;
        DataPushTaskDO update = new DataPushTaskDO();
        update.setId(task.getId());
        update.setRetryCount(newRetryCount);
        update.setErrorMsg(failLog.getErrorMsg());
        if (newRetryCount >= maxRetry) {
            // 超过最大重试次数 → 最终失败
            update.setPushStatus(3);
            update.setFinishTime(LocalDateTime.now());
            log.warn("[数据上报] 任务 #{} 已达最大重试次数 {}，置为最终失败", task.getId(), maxRetry);
        } else {
            // 未达上限 → 仍标记为失败，但等待下次重试
            update.setPushStatus(3);
            // 指数退避：2^retryCount 分钟后重试
            long retryMinutes = (long) Math.pow(2, newRetryCount);
            update.setNextRetryTime(LocalDateTime.now().plusMinutes(retryMinutes));
            log.info("[数据上报] 任务 #{} 将在 {} 分钟后重试（第 {} 次）", task.getId(), retryMinutes, newRetryCount);
        }
        dataPushTaskMapper.updateById(update);
    }

    /**
     * 分类错误类型（用于失败日志统计）
     */
    private String classifyError(Exception e) {
        String msg = e.getMessage() != null ? e.getMessage() : "";
        if (msg.contains("timeout") || msg.contains("Timeout")) {
            return "TIMEOUT";
        }
        if (msg.contains("auth") || msg.contains("Auth") || msg.contains("401") || msg.contains("403")) {
            return "AUTH";
        }
        if (msg.contains("network") || msg.contains("Network") || msg.contains("connect")) {
            return "NETWORK";
        }
        return "BUSINESS";
    }

    private void markInProgress(DataPushTaskDO task) {
        DataPushTaskDO update = new DataPushTaskDO();
        update.setId(task.getId());
        update.setPushStatus(1); // 1推送中
        dataPushTaskMapper.updateById(update);
    }

    private void markSuccess(DataPushTaskDO task, String requestId, String responseData, int costTime) {
        DataPushTaskDO update = new DataPushTaskDO();
        update.setId(task.getId());
        update.setPushStatus(2); // 2成功
        update.setRequestId(requestId);
        update.setResponseData(responseData);
        update.setFinishTime(LocalDateTime.now());
        dataPushTaskMapper.updateById(update);
    }

}

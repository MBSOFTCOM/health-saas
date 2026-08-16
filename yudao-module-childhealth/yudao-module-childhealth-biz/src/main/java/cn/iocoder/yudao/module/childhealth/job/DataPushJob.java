package cn.iocoder.yudao.module.childhealth.job;

import cn.iocoder.yudao.module.childhealth.dal.dataobject.integration.DataPushFailLogDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.integration.DataPushTaskDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.integration.DataPushFailLogMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.integration.DataPushTaskMapper;
import cn.iocoder.yudao.module.childhealth.framework.datapush.DataPushClient;
import cn.iocoder.yudao.module.childhealth.framework.datapush.DataPushClient.PushException;
import cn.iocoder.yudao.module.childhealth.framework.datapush.DataPushClient.PushResponse;
import cn.iocoder.yudao.module.childhealth.framework.datapush.DataPushProperties;
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
 *   - @Scheduled 固定 60s 扫描
 *   - 手动触发：调用 DataPushTaskController.retry(taskId)
 *
 * 推送实现：
 *   - 通过 DataPushClient 调用，支持 Mock 模式（childhealth.data-push.mock-enabled=true）
 *     和真实 HTTP 推送模式（mock-enabled=false）
 *   - 真实模式下根据 targetSystem 映射到 application.yaml 中配置的目标 URL
 *   - 未配置 URL 的目标系统直接标记为失败，errorCode=TARGET_NOT_CONFIGURED
 */
@Slf4j
@Component
public class DataPushJob {

    /** 单次扫描处理的最大任务数 */
    private static final int BATCH_SIZE = 50;

    @Resource
    private DataPushTaskMapper dataPushTaskMapper;
    @Resource
    private DataPushFailLogMapper dataPushFailLogMapper;
    @Resource
    private DataPushClient dataPushClient;
    @Resource
    private DataPushProperties dataPushProperties;

    /**
     * 数据上报任务执行入口
     */
    @Scheduled(fixedRate = 60000)
    public void execute() {
        log.info("[数据上报] 开始扫描待推送任务... mock={}", dataPushProperties.isMockEnabled());
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
        // 2. 调用推送客户端（Mock 或真实 HTTP）
        PushResponse resp = dataPushClient.push(task);
        int costTime = resp.getCostTimeMs() != null ? resp.getCostTimeMs()
                : (int) (System.currentTimeMillis() - startTime);
        // 3. 标记为成功
        markSuccess(task, resp.getRequestId(), resp.getResponseData(), costTime);
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
        String errorCode = (e instanceof PushException) ? ((PushException) e).getErrorCode() : "UNKNOWN";
        failLog.setErrorCode(errorCode);
        String errorMsg = e.getMessage() != null ? e.getMessage() : "未知错误";
        failLog.setErrorMsg(errorMsg.substring(0, Math.min(errorMsg.length(), 500)));
        failLog.setErrorType(classifyError(errorCode, e));
        failLog.setRequestPayload(task.getDataPayload());
        failLog.setResponsePayload(null);
        failLog.setHttpStatus(errorCode.startsWith("HTTP_") ? Integer.parseInt(errorCode.substring(5)) : 500);
        failLog.setCostTimeMs(null);
        failLog.setFailTime(LocalDateTime.now());
        dataPushFailLogMapper.insert(failLog);

        // 2. 更新任务状态：重试或最终失败
        int maxRetry = task.getMaxRetry() != null ? task.getMaxRetry() : dataPushProperties.getDefaultMaxRetry();
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
    private String classifyError(String errorCode, Exception e) {
        if (errorCode == null) {
            return "BUSINESS";
        }
        switch (errorCode) {
            case "TIMEOUT":
                return "TIMEOUT";
            case "AUTH":
            case "HTTP_401":
            case "HTTP_403":
                return "AUTH";
            case "NETWORK":
            case "CALL_ERROR":
            case "HTTP_500":
            case "HTTP_502":
            case "HTTP_503":
            case "HTTP_504":
                return "NETWORK";
            case "TARGET_NOT_CONFIGURED":
                return "CONFIG";
            case "MOCK_FAIL":
                return "MOCK";
            default:
                if (errorCode.startsWith("HTTP_")) {
                    return "BUSINESS";
                }
                return "BUSINESS";
        }
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

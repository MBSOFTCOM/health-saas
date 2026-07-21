package cn.iocoder.yudao.module.childhealth.job;

import cn.iocoder.yudao.module.childhealth.dal.dataobject.report.ReportGenerationTaskDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.report.ReportGenerationTaskMapper;
import org.springframework.scheduling.annotation.Scheduled;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 报告生成任务执行器
 *
 * 创建日期: 2026-07-20
 * 模块: 19. 多维度报告体系
 *
 * 调度策略：
 *   - 每 30 秒扫描一次 report_generation_task 表中 task_status=0（待生成）的任务
 *   - 顺序处理（避免并发冲突）；如需并发可改成分片广播
 *   - 状态机：0待生成 → 1生成中 → 2成功 / 3失败
 *   - 失败任务自动重试 retry_count < max_retry（默认 3 次）
 *
 * 触发方式：
 *   - XXL-JOB 调度：childhealthReportGenerationJob
 *   - 手动触发：调用 ReportGenerationTaskController.retry(taskId)
 */
@Slf4j
@Component
public class ReportGenerationJob {

    /** 最大重试次数 */
    private static final int MAX_RETRY = 3;

    @Resource
    private ReportGenerationTaskMapper reportGenerationTaskMapper;
    // @Resource
    // private ReportGeneratorClient reportGeneratorClient; // 报告生成器（PDF/Excel 渲染）

    /**
     * 报告生成任务执行入口
     */
    @Scheduled(fixedRate = 30000)
    public void execute() {
        log.info("[报告生成任务] 开始扫描待生成任务...");
        List<ReportGenerationTaskDO> pendingList = reportGenerationTaskMapper.selectPendingList();
        if (pendingList.isEmpty()) {
            log.info("[报告生成任务] 无待生成任务");
            return;
        }
        log.info("[报告生成任务] 共 {} 个待生成任务", pendingList.size());
        int successCount = 0;
        int failCount = 0;
        for (ReportGenerationTaskDO task : pendingList) {
            try {
                processTask(task);
                successCount++;
            } catch (Exception e) {
                failCount++;
                log.error("[报告生成任务] 任务 #{} 生成失败", task.getId(), e);
                markFailed(task, e.getMessage());
            }
        }
        log.info("[报告生成任务] 本次完成：成功 {} 个，失败 {} 个", successCount, failCount);
    }

    /**
     * 处理单个报告生成任务
     */
    private void processTask(ReportGenerationTaskDO task) {
        // 1. 标记为生成中
        markInProgress(task);
        // 2. 调用报告生成器（此处为模拟实现，实际项目注入 ReportGeneratorClient）
        //    根据报告类型选择模板：
        //      1 学生个人报告 → 调用 ScreeningRecord + ScreeningResultDetail + ScreeningPositive
        //      2 学校汇总报告 → 按 schoolId 聚合统计
        //      3 年级报告 → 按 gradeId 聚合
        //      4 区域监管报告 → 按 regionCode 聚合
        String fileUrl;
        long fileSize;
        try {
            // 模拟生成（实际应替换为 reportGeneratorClient.generate(task)）
            Thread.sleep(500); // 模拟耗时
            fileUrl = "/reports/" + task.getTaskNo() + "." + (task.getReportFormat() != null ? task.getReportFormat().toLowerCase() : "pdf");
            fileSize = 1024 * 256; // 模拟 256KB
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("报告生成被中断", e);
        }
        // 3. 标记为成功
        markSuccess(task, fileUrl, fileSize);
        log.info("[报告生成任务] 任务 #{} ({}) 生成成功，文件: {}", task.getId(), task.getTaskNo(), fileUrl);
    }

    private void markInProgress(ReportGenerationTaskDO task) {
        ReportGenerationTaskDO update = new ReportGenerationTaskDO();
        update.setId(task.getId());
        update.setTaskStatus(1); // 1生成中
        update.setProgress(10);
        reportGenerationTaskMapper.updateById(update);
    }

    private void markSuccess(ReportGenerationTaskDO task, String fileUrl, long fileSize) {
        ReportGenerationTaskDO update = new ReportGenerationTaskDO();
        update.setId(task.getId());
        update.setTaskStatus(2); // 2成功
        update.setProgress(100);
        update.setFileUrl(fileUrl);
        update.setFileSize(fileSize);
        update.setFinishTime(LocalDateTime.now());
        reportGenerationTaskMapper.updateById(update);
    }

    private void markFailed(ReportGenerationTaskDO task, String errorMsg) {
        ReportGenerationTaskDO update = new ReportGenerationTaskDO();
        update.setId(task.getId());
        update.setTaskStatus(3); // 3失败
        update.setErrorMsg(errorMsg);
        update.setRetryCount(task.getRetryCount() == null ? 1 : task.getRetryCount() + 1);
        // 未超过最大重试次数 → 重新置为待生成，等待下次扫描
        if (update.getRetryCount() < MAX_RETRY) {
            update.setTaskStatus(0); // 重置为待生成
        }
        reportGenerationTaskMapper.updateById(update);
    }

}

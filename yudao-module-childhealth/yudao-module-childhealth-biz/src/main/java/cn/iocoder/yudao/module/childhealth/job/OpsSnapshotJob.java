package cn.iocoder.yudao.module.childhealth.job;

import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.tenant.core.util.TenantUtils;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.management.FollowTaskDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.ops.OpsIndicatorSnapshotDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.workflow.ReferralRecordDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.management.FollowTaskMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.ops.OpsIndicatorSnapshotMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.screening.*;
import cn.iocoder.yudao.module.childhealth.dal.mysql.workflow.ReferralRecordMapper;
import org.springframework.scheduling.annotation.Scheduled;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 运营指标日快照生成 Job
 *
 * 创建日期: 2026-07-20
 * 模块: 1. 数据看板 + 12. 运营工作台
 *
 * 调度策略：
 *   - 每日凌晨 1:00 执行（cron: 0 0 1 * * ?）
 *   - 生成前一日（T-1）的全局运营指标快照
 *   - 写入 ops_indicator_snapshot 表（snapshot_date/批维度）
 *   - 已存在的快照不重复生成（按 snapshot_date + batch_id 唯一）
 *
 * 数据来源：
 *   1. screening_record：筛查总数、阳性数、审核状态
 *   2. screening_positive + recheck_record：复筛数
 *   3. follow_task：随访任务数
 *   4. referral_record：转介数（用于扩展指标）
 *
 * 租户上下文：
 *   - 定时任务不经过 TenantContextWebFilter，租户上下文为空
 *   - 通过 TenantUtils.execute(DEFAULT_TENANT_ID, ...) 显式注入默认租户（id=1）
 *   - ops_indicator_snapshot 已加入 yudao.tenant.ignore-tables，写入时不强制注入 tenant_id
 *     （由 DB 列 DEFAULT 1 兜底）；读取业务表仍走租户过滤，确保仅统计本租户数据。
 *
 * 触发方式：
 *   - @Scheduled 调度
 */
@Slf4j
@Component
public class OpsSnapshotJob {

    /** 默认租户编号（与 system_users.tenant_id=1 一致）；多租户化后需遍历 system_tenant 表逐个生成 */
    private static final Long DEFAULT_TENANT_ID = 1L;

    @Resource
    private ScreeningRecordMapper screeningRecordMapper;
    @Resource
    private ScreeningPositiveMapper screeningPositiveMapper;
    @Resource
    private ScreeningBatchMapper screeningBatchMapper;
    @Resource
    private RecheckRecordMapper recheckRecordMapper;
    @Resource
    private FollowTaskMapper followTaskMapper;
    @Resource
    private ReferralRecordMapper referralRecordMapper;
    @Resource
    private OpsIndicatorSnapshotMapper opsIndicatorSnapshotMapper;

    /**
     * 快照生成入口
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void execute() {
        // 定时任务无 Web 上下文，显式注入默认租户，避免 TenantDatabaseInterceptor 抛 "不存在租户编号"
        TenantUtils.execute(DEFAULT_TENANT_ID, () -> {
            LocalDate snapshotDate = LocalDate.now().minusDays(1); // T-1 快照
            log.info("[运营快照] 开始生成 {} 日快照... tenantId={}", snapshotDate, DEFAULT_TENANT_ID);

            // 1. 查询所有进行中/已完成的批次
            List<ScreeningBatchDO> batches = screeningBatchMapper.selectListByConditions(null, null, null);
            int generated = 0;
            int skipped = 0;
            for (ScreeningBatchDO batch : batches) {
                try {
                    boolean created = generateSnapshotForBatch(batch, snapshotDate);
                    if (created) {
                        generated++;
                    } else {
                        skipped++;
                    }
                } catch (Exception e) {
                    log.error("[运营快照] 批次 #{} 快照生成失败", batch.getId(), e);
                }
            }
            log.info("[运营快照] {} 日快照完成：新增 {} 个，跳过（已存在）{} 个", snapshotDate, generated, skipped);
        });
    }

    /**
     * 为单个批次生成快照
     *
     * @return true=新建；false=已存在跳过
     */
    private boolean generateSnapshotForBatch(ScreeningBatchDO batch, LocalDate snapshotDate) {
        // 1. 检查是否已生成（避免重复）
        OpsIndicatorSnapshotDO existing = opsIndicatorSnapshotMapper.selectByDate(
                snapshotDate, batch.getId(), null, null);
        if (existing != null) {
            log.info("[运营快照] 批次 #{} 在 {} 已有快照，跳过", batch.getId(), snapshotDate);
            return false;
        }

        // 2. 统计该批次截至 snapshotDate 的累计指标
        // 2.1 计划人数 + 实检人数
        Integer targetCount = batch.getTargetCount() != null ? batch.getTargetCount() : 0;
        // 2.2 该批次下筛查记录数（截至 snapshotDate）
        Long actualCount = screeningRecordMapper.selectCount(
                new LambdaQueryWrapperX<ScreeningRecordDO>()
                        .eq(ScreeningRecordDO::getBatchId, batch.getId())
                        .le(ScreeningRecordDO::getScreeningDate, snapshotDate));
        // 2.3 待审核数
        Long pendingAudit = screeningRecordMapper.selectCount(
                new LambdaQueryWrapperX<ScreeningRecordDO>()
                        .eq(ScreeningRecordDO::getBatchId, batch.getId())
                        .le(ScreeningRecordDO::getScreeningDate, snapshotDate)
                        .eq(ScreeningRecordDO::getCheckStatus, 2));
        // 2.4 阳性数（基于 has_positive=1 的筛查记录数）
        Long positiveCount = screeningRecordMapper.selectCount(
                new LambdaQueryWrapperX<ScreeningRecordDO>()
                        .eq(ScreeningRecordDO::getBatchId, batch.getId())
                        .le(ScreeningRecordDO::getScreeningDate, snapshotDate)
                        .eq(ScreeningRecordDO::getHasPositive, 1));
        // 2.5 复筛人数：查询该批次下所有筛查记录ID → 阳性记录ID → 复筛记录数
        List<ScreeningRecordDO> batchRecords = screeningRecordMapper.selectList(
                new LambdaQueryWrapperX<ScreeningRecordDO>()
                        .eq(ScreeningRecordDO::getBatchId, batch.getId())
                        .le(ScreeningRecordDO::getScreeningDate, snapshotDate));
        Long recheckCount = 0L;
        Long pendingRecheck = 0L;
        if (!batchRecords.isEmpty()) {
            List<Long> recordIds = batchRecords.stream().map(ScreeningRecordDO::getId).toList();
            List<ScreeningPositiveDO> positives = screeningPositiveMapper.selectList(
                    new LambdaQueryWrapperX<ScreeningPositiveDO>()
                            .in(ScreeningPositiveDO::getRecordId, recordIds));
            if (!positives.isEmpty()) {
                List<Long> positiveIds = positives.stream().map(ScreeningPositiveDO::getId).toList();
                List<RecheckRecordDO> rechecks = recheckRecordMapper.selectListByPositiveIds(positiveIds);
                recheckCount = (long) rechecks.size();
                // 2.6 待复筛数 = 需复筛且未完成的阳性记录数
                pendingRecheck = positives.stream()
                        .filter(p -> p.getNeedRecheck() != null && p.getNeedRecheck() == 1
                                && (p.getRecheckStatus() == null || p.getRecheckStatus() != 2))
                        .count();
            }
        }
        // 2.7 随访任务数（截至 snapshotDate 当日）
        Long followCount = followTaskMapper.countByPlanDateRange(snapshotDate, snapshotDate);
        // 2.8 待随访数
        Long pendingFollow = followTaskMapper.countByDateRangeAndStatus(snapshotDate, snapshotDate, 0);
        // 2.9 转介总数
        Long referralCount = referralRecordMapper.countAll();

        // 3. 计算各项比率
        BigDecimal participationRate = calcRate(actualCount, targetCount != null ? targetCount.longValue() : 0L);
        BigDecimal positiveRate = calcRate(positiveCount, actualCount);
        BigDecimal recheckRate = calcRate(recheckCount, positiveCount);
        BigDecimal followRate = calcRate(followCount, positiveCount);
        BigDecimal followCompleteRate = calcRate(
                followCount != null ? followCount - (pendingFollow != null ? pendingFollow : 0) : 0,
                followCount);

        // 4. 写入快照
        OpsIndicatorSnapshotDO snapshot = new OpsIndicatorSnapshotDO();
        snapshot.setSnapshotDate(snapshotDate);
        snapshot.setBatchId(batch.getId());
        snapshot.setSchoolId(batch.getSchoolId());
        snapshot.setScreeningCount(actualCount != null ? actualCount.intValue() : 0);
        snapshot.setTargetCount(targetCount);
        snapshot.setActualCount(actualCount != null ? actualCount.intValue() : 0);
        snapshot.setPositiveCount(positiveCount != null ? positiveCount.intValue() : 0);
        snapshot.setRecheckCount(recheckCount != null ? recheckCount.intValue() : 0);
        snapshot.setFollowCount(followCount != null ? followCount.intValue() : 0);
        snapshot.setPendingAuditCount(pendingAudit != null ? pendingAudit.intValue() : 0);
        snapshot.setPendingRecheckCount(pendingRecheck != null ? pendingRecheck.intValue() : 0);
        snapshot.setPendingFollowCount(pendingFollow != null ? pendingFollow.intValue() : 0);
        snapshot.setParticipationRate(participationRate);
        snapshot.setPositiveRate(positiveRate);
        snapshot.setRecheckRate(recheckRate);
        snapshot.setFollowRate(followRate);
        snapshot.setFollowCompleteRate(followCompleteRate);
        snapshot.setExtraIndicators("{\"referralCount\":" + referralCount + "}");
        // BaseDO 字段
        snapshot.setCreateTime(LocalDateTime.now());
        snapshot.setCreator("1"); // 系统自动生成
        opsIndicatorSnapshotMapper.insert(snapshot);
        log.info("[运营快照] 批次 #{} 在 {} 快照生成成功：实检 {} / 阳性 {} / 复筛 {} / 随访 {}",
                batch.getId(), snapshotDate, actualCount, positiveCount, recheckCount, followCount);
        return true;
    }

    /**
     * 计算百分比（保留 2 位小数）
     */
    private BigDecimal calcRate(Long numerator, Long denominator) {
        if (denominator == null || denominator == 0 || numerator == null) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        return BigDecimal.valueOf(numerator)
                .divide(BigDecimal.valueOf(denominator), 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }

}

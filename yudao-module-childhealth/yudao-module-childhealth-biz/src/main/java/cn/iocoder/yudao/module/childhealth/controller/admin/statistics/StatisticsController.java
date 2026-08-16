package cn.iocoder.yudao.module.childhealth.controller.admin.statistics;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.childhealth.controller.admin.statistics.vo.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.management.FollowTaskDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.ops.OpsIndicatorSnapshotDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.ops.StaffWorkloadStatisticsDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.ScreeningBatchDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.management.FollowTaskMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.ops.OpsIndicatorSnapshotMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.ops.StaffWorkloadStatisticsMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.screening.ScreeningBatchMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.screening.RecheckRecordMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.workflow.ReferralRecordMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

/**
 * 管理后台 - 全维度数据统计
 *
 * 创建日期: 2026-07-20
 * 模块: 20. 全维度数据统计
 *
 * 数据源策略:
 *   1. 优先读 ops_indicator_snapshot 最新一条（T+1日级快照）
 *   2. 缺失时回退查最近 30 天快照
 *   3. 实时性要求高的指标直接查业务表（follow_task/staff_workload_statistics/screening_batch 等）
 */
@Tag(name = "管理后台 - 全维度数据统计")
@RestController
@RequestMapping("/childhealth/statistics")
@Validated
public class StatisticsController {

    @Resource
    private OpsIndicatorSnapshotMapper opsIndicatorSnapshotMapper;
    @Resource
    private StaffWorkloadStatisticsMapper staffWorkloadStatisticsMapper;
    @Resource
    private FollowTaskMapper followTaskMapper;
    @Resource
    private ReferralRecordMapper referralRecordMapper;
    @Resource
    private RecheckRecordMapper recheckRecordMapper;
    @Resource
    private ScreeningBatchMapper screeningBatchMapper;

    @GetMapping("/screening-total")
    @Operation(summary = "筛查总表（批次/区域/学校/学生多维）")
    @PreAuthorize("@ss.hasPermission('childhealth:statistics:query')")
    public CommonResult<StatisticsScreeningTotalRespVO> screeningTotal(@Valid StatisticsQueryReqVO reqVO) {
        OpsIndicatorSnapshotDO snapshot = getLatestSnapshot(reqVO);
        StatisticsScreeningTotalRespVO respVO = new StatisticsScreeningTotalRespVO();
        if (snapshot == null) {
            return success(respVO);
        }
        // 增加空值保护，避免 Integer 字段为 null 时自动拆箱触发 NPE
        int actualCount = snapshot.getActualCount() != null ? snapshot.getActualCount() : 0;
        int pendingAuditCount = snapshot.getPendingAuditCount() != null ? snapshot.getPendingAuditCount() : 0;
        respVO.setTotalCount(actualCount);
        respVO.setAuditedCount(actualCount - pendingAuditCount);
        respVO.setPendingAuditCount(pendingAuditCount);
        if (actualCount > 0) {
            respVO.setAuditRate(BigDecimal.valueOf(respVO.getAuditedCount())
                    .divide(BigDecimal.valueOf(actualCount), 2, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100)));
        }
        respVO.setPositiveCount(snapshot.getPositiveCount());
        respVO.setPositiveRate(snapshot.getPositiveRate());
        // 转介数：从 referral_record 实时统计（快照中无此字段）
        Long referralCount = referralRecordMapper.countAll();
        respVO.setReferralCount(referralCount != null ? referralCount.intValue() : 0);
        return success(respVO);
    }

    @GetMapping("/positive")
    @Operation(summary = "阳性数据（区域/年龄/疾病分类）")
    @PreAuthorize("@ss.hasPermission('childhealth:statistics:query')")
    public CommonResult<List<StatisticsPositiveRespVO>> positive(@Valid StatisticsQueryReqVO reqVO) {
        LocalDate queryDate = reqVO.getEndDate() != null ? reqVO.getEndDate() : LocalDate.now();
        List<OpsIndicatorSnapshotDO> list = opsIndicatorSnapshotMapper.selectListByBatch(reqVO.getBatchId(), queryDate);
        if (CollectionUtils.isEmpty(list)) {
            return success(new ArrayList<>());
        }
        List<StatisticsPositiveRespVO> result = new ArrayList<>();
        for (OpsIndicatorSnapshotDO snapshot : list) {
            StatisticsPositiveRespVO resp = new StatisticsPositiveRespVO();
            resp.setDimensionCode(String.valueOf(snapshot.getSchoolId()));
            resp.setDimensionName("学校-" + snapshot.getSchoolId());
            resp.setScreeningCount(snapshot.getActualCount());
            resp.setPositiveCount(snapshot.getPositiveCount());
            resp.setPositiveRate(snapshot.getPositiveRate());
            resp.setRecheckCount(snapshot.getRecheckCount());
            resp.setRecheckRate(snapshot.getRecheckRate());
            result.add(resp);
        }
        return success(result);
    }

    @GetMapping("/positive-trend")
    @Operation(summary = "阳性趋势报表导出")
    @PreAuthorize("@ss.hasPermission('childhealth:statistics:export')")
    public CommonResult<List<StatisticsPositiveRespVO>> positiveTrend(@Valid StatisticsQueryReqVO reqVO) {
        LocalDate startDate = reqVO.getStartDate() != null ? reqVO.getStartDate() : LocalDate.now().minusDays(30);
        LocalDate endDate = reqVO.getEndDate() != null ? reqVO.getEndDate() : LocalDate.now();
        List<OpsIndicatorSnapshotDO> list = opsIndicatorSnapshotMapper.selectListByDateRange(startDate, endDate, reqVO.getBatchId());
        List<StatisticsPositiveRespVO> result = new ArrayList<>();
        for (OpsIndicatorSnapshotDO snapshot : list) {
            StatisticsPositiveRespVO resp = new StatisticsPositiveRespVO();
            resp.setDimensionCode(snapshot.getSnapshotDate().toString());
            resp.setDimensionName(snapshot.getSnapshotDate().toString());
            resp.setScreeningCount(snapshot.getActualCount());
            resp.setPositiveCount(snapshot.getPositiveCount());
            resp.setPositiveRate(snapshot.getPositiveRate());
            resp.setRecheckCount(snapshot.getRecheckCount());
            resp.setRecheckRate(snapshot.getRecheckRate());
            result.add(resp);
        }
        return success(result);
    }

    @GetMapping("/recheck")
    @Operation(summary = "复筛数据全流程跟踪")
    @PreAuthorize("@ss.hasPermission('childhealth:statistics:query')")
    public CommonResult<StatisticsRecheckRespVO> recheck(@Valid StatisticsQueryReqVO reqVO) {
        OpsIndicatorSnapshotDO snapshot = getLatestSnapshot(reqVO);
        StatisticsRecheckRespVO respVO = new StatisticsRecheckRespVO();
        if (snapshot == null) {
            return success(respVO);
        }
        // 增加空值保护
        int pendingRecheckCount = snapshot.getPendingRecheckCount() != null ? snapshot.getPendingRecheckCount() : 0;
        int recheckCount = snapshot.getRecheckCount() != null ? snapshot.getRecheckCount() : 0;
        respVO.setInitialPositiveCount(snapshot.getPositiveCount());
        respVO.setNeedRecheckCount(pendingRecheckCount + recheckCount);
        respVO.setRecheckCompletedCount(recheckCount);
        respVO.setRecheckRate(snapshot.getRecheckRate());
        // 复筛仍阳性数：直接从 recheck_record 表按日期范围实时聚合
        LocalDate startDate = reqVO.getStartDate();
        LocalDate endDate = reqVO.getEndDate() != null ? reqVO.getEndDate() : LocalDate.now();
        Long stillPositive = recheckRecordMapper.countStillPositive(startDate, endDate);
        respVO.setStillPositiveCount(stillPositive != null ? stillPositive.intValue() : 0);
        // 复筛阳性率 = 复筛仍阳性数 / 已复筛人数
        if (recheckCount > 0 && stillPositive != null) {
            respVO.setStillPositiveRate(BigDecimal.valueOf(stillPositive)
                    .divide(BigDecimal.valueOf(recheckCount), 2, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100)));
        } else {
            respVO.setStillPositiveRate(BigDecimal.ZERO);
        }
        // 转介数：从 referral_record 实时统计
        Long referralCount = referralRecordMapper.countAll();
        respVO.setReferralCount(referralCount != null ? referralCount.intValue() : 0);
        return success(respVO);
    }

    @GetMapping("/follow")
    @Operation(summary = "随访数据（累计/当日/完成率）")
    @PreAuthorize("@ss.hasPermission('childhealth:statistics:query')")
    public CommonResult<StatisticsFollowRespVO> follow(@Valid StatisticsQueryReqVO reqVO) {
        OpsIndicatorSnapshotDO snapshot = getLatestSnapshot(reqVO);
        StatisticsFollowRespVO respVO = new StatisticsFollowRespVO();
        if (snapshot == null) {
            return success(respVO);
        }
        // 增加空值保护
        int followCount = snapshot.getFollowCount() != null ? snapshot.getFollowCount() : 0;
        int pendingFollowCount = snapshot.getPendingFollowCount() != null ? snapshot.getPendingFollowCount() : 0;
        respVO.setTotalFollowCount(followCount);
        // 当日随访数：从 follow_task 按 create_time=today 实时统计
        Long todayCount = followTaskMapper.countTodayCreated();
        respVO.setTodayFollowCount(todayCount != null ? todayCount.intValue() : 0);
        respVO.setPendingFollowCount(pendingFollowCount);
        respVO.setCompletedFollowCount(followCount - pendingFollowCount);
        if (followCount > 0) {
            respVO.setFollowCompleteRate(BigDecimal.valueOf(respVO.getCompletedFollowCount())
                    .divide(BigDecimal.valueOf(followCount), 2, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100)));
        }
        return success(respVO);
    }

    @GetMapping("/follow-detail")
    @Operation(summary = "随访内容/健康变化/干预意见明细")
    @PreAuthorize("@ss.hasPermission('childhealth:statistics:query')")
    public CommonResult<List<StatisticsFollowDetailRespVO>> followDetail(@Valid StatisticsQueryReqVO reqVO) {
        // 从 follow_task 查询随访明细列表，按计划日期范围过滤
        LocalDate startDate = reqVO.getStartDate();
        LocalDate endDate = reqVO.getEndDate() != null ? reqVO.getEndDate() : LocalDate.now();
        List<FollowTaskDO> tasks;
        if (startDate != null) {
            // 范围查询：先按条件查 List，再转 VO
            tasks = followTaskMapper.selectList(new cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX<FollowTaskDO>()
                    .geIfPresent(FollowTaskDO::getPlanDate, startDate)
                    .leIfPresent(FollowTaskDO::getPlanDate, endDate)
                    .orderByDesc(FollowTaskDO::getId));
        } else {
            // 默认查最近 100 条
            tasks = followTaskMapper.selectList(new cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX<FollowTaskDO>()
                    .orderByDesc(FollowTaskDO::getId)
                    .last("LIMIT 100"));
        }
        List<StatisticsFollowDetailRespVO> result = new ArrayList<>();
        for (FollowTaskDO task : tasks) {
            StatisticsFollowDetailRespVO vo = new StatisticsFollowDetailRespVO();
            vo.setFollowTaskId(task.getId());
            vo.setStudentId(task.getChildId());
            vo.setFollowDate(task.getPlanDate());
            vo.setFollowContent(task.getTaskContent());
            vo.setFollowStatus(task.getTaskStatus());
            vo.setDoctorId(task.getResponsibleDoctor());
            // TODO: 学生姓名/医生姓名/项目编码/健康变化/干预意见需联表查询，此处留空待前端展示层补充
            result.add(vo);
        }
        return success(result);
    }

    @GetMapping("/workload")
    @Operation(summary = "工作量统计（机构/医生/学校）")
    @PreAuthorize("@ss.hasPermission('childhealth:statistics:query')")
    public CommonResult<List<StatisticsWorkloadRespVO>> workload(@Valid StatisticsQueryReqVO reqVO) {
        LocalDate startDate = reqVO.getStartDate();
        LocalDate endDate = reqVO.getEndDate() != null ? reqVO.getEndDate() : LocalDate.now();
        // 按 schoolId / regionCode 等条件查询工作量统计表
        // regionCode 暂未在 staff_workload_statistics 表中直接对应，按 orgId/schoolId 过滤
        Long orgId = null;       // 若按机构过滤，可由前端在 reqVO 增加 orgId 字段扩展
        List<StaffWorkloadStatisticsDO> list = staffWorkloadStatisticsMapper.selectListByConditions(
                orgId, reqVO.getSchoolId(), startDate, endDate);
        if (CollectionUtils.isEmpty(list)) {
            return success(new ArrayList<>());
        }
        // 按医护ID（staffId）聚合并算各指标之和
        Map<Long, StaffWorkloadStatisticsDO> merged = new LinkedHashMap<>();
        for (StaffWorkloadStatisticsDO item : list) {
            StaffWorkloadStatisticsDO agg = merged.get(item.getStaffId());
            if (agg == null) {
                agg = new StaffWorkloadStatisticsDO();
                agg.setStaffId(item.getStaffId());
                agg.setStaffName(item.getStaffName());
                agg.setOrgId(item.getOrgId());
                agg.setOrgName(item.getOrgName());
                agg.setSchoolId(item.getSchoolId());
                agg.setScreeningCount(0);
                agg.setAuditCount(0);
                agg.setFollowCount(0);
                agg.setRecheckCount(0);
                agg.setReferralCount(0);
                agg.setCaseCreateCount(0);
                agg.setCaseCloseCount(0);
                agg.setTotalWorkload(BigDecimal.ZERO);
                merged.put(item.getStaffId(), agg);
            }
            agg.setScreeningCount(safeSum(agg.getScreeningCount(), item.getScreeningCount()));
            agg.setAuditCount(safeSum(agg.getAuditCount(), item.getAuditCount()));
            agg.setFollowCount(safeSum(agg.getFollowCount(), item.getFollowCount()));
            agg.setRecheckCount(safeSum(agg.getRecheckCount(), item.getRecheckCount()));
            agg.setReferralCount(safeSum(agg.getReferralCount(), item.getReferralCount()));
            agg.setCaseCreateCount(safeSum(agg.getCaseCreateCount(), item.getCaseCreateCount()));
            agg.setCaseCloseCount(safeSum(agg.getCaseCloseCount(), item.getCaseCloseCount()));
            agg.setTotalWorkload(safeSumDecimal(agg.getTotalWorkload(), item.getTotalWorkload()));
        }
        List<StatisticsWorkloadRespVO> result = new ArrayList<>(merged.size());
        for (StaffWorkloadStatisticsDO agg : merged.values()) {
            StatisticsWorkloadRespVO vo = new StatisticsWorkloadRespVO();
            vo.setDimensionCode(String.valueOf(agg.getStaffId()));
            vo.setDimensionName(agg.getStaffName() != null ? agg.getStaffName() : "医护-" + agg.getStaffId());
            vo.setDimensionType("STAFF");
            vo.setStatDate(startDate + " ~ " + endDate);
            vo.setScreeningCount(agg.getScreeningCount());
            vo.setAuditCount(agg.getAuditCount());
            vo.setFollowCount(agg.getFollowCount());
            vo.setRecheckCount(agg.getRecheckCount());
            vo.setReferralCount(agg.getReferralCount());
            vo.setCaseCreateCount(agg.getCaseCreateCount());
            vo.setCaseCloseCount(agg.getCaseCloseCount());
            vo.setTotalWorkload(agg.getTotalWorkload());
            result.add(vo);
        }
        return success(result);
    }

    @GetMapping("/batch-query")
    @Operation(summary = "体检批次查询")
    @PreAuthorize("@ss.hasPermission('childhealth:statistics:query')")
    public CommonResult<List<StatisticsBatchRespVO>> batchQuery(@Valid StatisticsQueryReqVO reqVO) {
        // 按条件查询筛查批次
        List<ScreeningBatchDO> list;
        if (reqVO.getStartDate() != null || reqVO.getEndDate() != null) {
            list = screeningBatchMapper.selectListByDateRange(reqVO.getStartDate(), reqVO.getEndDate());
        } else {
            // 默认按学校/学年查询（如全部为空则返回最近 100 条）
            list = screeningBatchMapper.selectListByConditions(reqVO.getSchoolId(), null, null);
            if (list.size() > 100) {
                list = list.subList(0, 100);
            }
        }
        List<StatisticsBatchRespVO> result = list.stream().map(batch -> {
            StatisticsBatchRespVO vo = new StatisticsBatchRespVO();
            vo.setBatchId(batch.getId());
            vo.setBatchNo(batch.getBatchNo());
            vo.setBatchName(batch.getBatchName());
            vo.setSchoolId(batch.getSchoolId());
            vo.setYearId(batch.getYearId());
            vo.setStartDate(batch.getStartDate());
            vo.setEndDate(batch.getEndDate());
            vo.setTargetCount(batch.getTargetCount());
            vo.setActualCount(batch.getActualCount());
            vo.setBatchStatus(batch.getBatchStatus());
            // 参检率 = 实际筛查 / 计划筛查
            if (batch.getTargetCount() != null && batch.getTargetCount() > 0 && batch.getActualCount() != null) {
                vo.setParticipationRate(BigDecimal.valueOf(batch.getActualCount())
                        .divide(BigDecimal.valueOf(batch.getTargetCount()), 2, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100)));
            } else {
                vo.setParticipationRate(BigDecimal.ZERO);
            }
            return vo;
        }).collect(Collectors.toList());
        return success(result);
    }

    /**
     * 获取最新快照（内部辅助方法）
     */
    private OpsIndicatorSnapshotDO getLatestSnapshot(StatisticsQueryReqVO reqVO) {
        LocalDate today = LocalDate.now();
        OpsIndicatorSnapshotDO snapshot = opsIndicatorSnapshotMapper.selectByDate(today, reqVO.getBatchId(), reqVO.getSchoolId(), reqVO.getGradeId());
        if (snapshot == null) {
            LocalDate endDate = reqVO.getEndDate() != null ? reqVO.getEndDate() : today;
            LocalDate startDate = reqVO.getStartDate() != null ? reqVO.getStartDate() : today.minusDays(30);
            List<OpsIndicatorSnapshotDO> list = opsIndicatorSnapshotMapper.selectListByDateRange(startDate, endDate, reqVO.getBatchId());
            if (!CollectionUtils.isEmpty(list)) {
                snapshot = list.get(list.size() - 1);
            }
        }
        return snapshot;
    }

    /** 安全求和（处理 null） */
    private Integer safeSum(Integer a, Integer b) {
        return (a == null ? 0 : a) + (b == null ? 0 : b);
    }

    /** BigDecimal 安全求和（处理 null） */
    private BigDecimal safeSumDecimal(BigDecimal a, BigDecimal b) {
        return (a == null ? BigDecimal.ZERO : a).add(b == null ? BigDecimal.ZERO : b);
    }

}

package cn.iocoder.yudao.module.childhealth.controller.app.doctor;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.controller.app.doctor.vo.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.management.FollowTaskDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.ops.StaffWorkloadStatisticsDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.workflow.StudentInfoDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.management.FollowTaskMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.ops.StaffWorkloadStatisticsMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.screening.*;
import cn.iocoder.yudao.module.childhealth.dal.mysql.workflow.StudentInfoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.*;

/**
 * 移动端 - 医生 App 接口
 *
 * 创建日期: 2026-07-20
 * 模块: 移动端 - 医生 App
 *
 * 主要功能：
 *   1. 医生工作台（今日待办/统计）
 *   2. 待筛查任务列表（批次维度）
 *   3. 现场筛查录入（学生维度）
 *   4. 待审核筛查列表
 *   5. 筛查审核通过/驳回
 *   6. 复筛结果录入
 *   7. 随访记录录入
 *   8. 待随访任务列表
 *   9. 待复筛阳性列表
 *   10. 医生工作量查询
 */
@Tag(name = "移动端 - 医生 App")
@RestController
@RequestMapping("/childhealth-app/doctor")
@Validated
public class DoctorAppController {

    @Resource
    private ScreeningBatchMapper screeningBatchMapper;
    @Resource
    private ScreeningRecordMapper screeningRecordMapper;
    @Resource
    private ScreeningResultDetailMapper screeningResultDetailMapper;
    @Resource
    private ScreeningPositiveMapper screeningPositiveMapper;
    @Resource
    private RecheckRecordMapper recheckRecordMapper;
    @Resource
    private FollowTaskMapper followTaskMapper;
    @Resource
    private StaffWorkloadStatisticsMapper staffWorkloadStatisticsMapper;
    @Resource
    private StudentInfoMapper studentInfoMapper;

    // ==================== 1. 医生工作台 ====================

    @GetMapping("/workbench")
    @Operation(summary = "医生工作台", description = "今日待筛查/已筛查/待审核/已审核/待随访/待复筛统计")
    @Parameter(name = "doctorId", description = "医生ID", required = true, example = "100")
    public CommonResult<DoctorWorkbenchRespVO> workbench(@RequestParam("doctorId") Long doctorId) {
        DoctorWorkbenchRespVO vo = new DoctorWorkbenchRespVO();
        vo.setDoctorId(doctorId);
        vo.setDoctorName("医生-" + doctorId); // TODO: 联表查询医生姓名
        vo.setStatDate(LocalDate.now());
        vo.setUpdateTime(LocalDateTime.now());

        LocalDate today = LocalDate.now();
        // 今日已筛查数（按 checker_id 过滤）
        Long todayScreened = screeningResultDetailMapper.selectCount(
                new LambdaQueryWrapperX<ScreeningResultDetailDO>()
                        .eq(ScreeningResultDetailDO::getCheckerId, doctorId)
                        .between(ScreeningResultDetailDO::getCheckTime, today.atStartOfDay(), today.atTime(23, 59, 59)));
        vo.setTodayScreened(todayScreened != null ? todayScreened.intValue() : 0);

        // 今日待审核数（按 audit_doctor 过滤，状态=2待审核）
        Long todayPendingAudit = screeningRecordMapper.selectCount(
                new LambdaQueryWrapperX<ScreeningRecordDO>()
                        .eq(ScreeningRecordDO::getAuditDoctor, doctorId)
                        .eq(ScreeningRecordDO::getCheckStatus, 2));
        vo.setTodayPendingAudit(todayPendingAudit != null ? todayPendingAudit.intValue() : 0);

        // 今日已审核数
        Long todayAudited = screeningRecordMapper.selectCount(
                new LambdaQueryWrapperX<ScreeningRecordDO>()
                        .eq(ScreeningRecordDO::getAuditDoctor, doctorId)
                        .eq(ScreeningRecordDO::getCheckStatus, 3)
                        .between(ScreeningRecordDO::getAuditTime, today.atStartOfDay(), today.atTime(23, 59, 59)));
        vo.setTodayAudited(todayAudited != null ? todayAudited.intValue() : 0);

        // 待随访任务数
        Long pendingFollow = followTaskMapper.selectCount(
                new LambdaQueryWrapperX<FollowTaskDO>()
                        .eq(FollowTaskDO::getResponsibleDoctor, doctorId)
                        .eq(FollowTaskDO::getTaskStatus, 0));
        vo.setPendingFollowCount(pendingFollow != null ? pendingFollow.intValue() : 0);

        // 待复筛阳性数（recheck_status=1 表示已通知未完成）
        Long pendingRecheck = screeningPositiveMapper.selectCount(
                new LambdaQueryWrapperX<ScreeningPositiveDO>()
                        .eq(ScreeningPositiveDO::getNeedRecheck, 1)
                        .eq(ScreeningPositiveDO::getRecheckStatus, 1));
        vo.setPendingRecheckCount(pendingRecheck != null ? pendingRecheck.intValue() : 0);

        // 本月工作量
        LocalDate monthStart = LocalDate.now().withDayOfMonth(1);
        StaffWorkloadStatisticsDO workload = staffWorkloadStatisticsMapper.selectByStaffAndDate(doctorId, today);
        BigDecimal monthWorkload = BigDecimal.ZERO;
        if (workload != null && workload.getTotalWorkload() != null) {
            monthWorkload = workload.getTotalWorkload();
        }
        vo.setMonthTotalWorkload(monthWorkload.toPlainString());
        // 今日待筛查：按当日进行中的批次统计计划人数（粗略估计）
        vo.setTodayPendingScreening(0);
        return success(vo);
    }

    // ==================== 2. 待筛查批次任务列表 ====================

    @GetMapping("/screening-tasks")
    @Operation(summary = "待筛查批次任务列表", description = "查询当前进行中的筛查批次")
    public CommonResult<List<DoctorScreeningTaskRespVO>> screeningTasks() {
        List<ScreeningBatchDO> batches = screeningBatchMapper.selectListByConditions(null, null, 2);
        // TODO: 学校名称需联表查询，此处用 schoolId 占位
        return success(batches.stream().map(b -> {
            DoctorScreeningTaskRespVO vo = new DoctorScreeningTaskRespVO();
            vo.setBatchId(b.getId());
            vo.setBatchNo(b.getBatchNo());
            vo.setBatchName(b.getBatchName());
            vo.setSchoolId(b.getSchoolId());
            vo.setSchoolName("学校-" + b.getSchoolId());
            vo.setStartDate(b.getStartDate());
            vo.setEndDate(b.getEndDate());
            vo.setTargetCount(b.getTargetCount());
            vo.setActualCount(b.getActualCount());
            vo.setBatchStatus(b.getBatchStatus());
            return vo;
        }).collect(Collectors.toList()));
    }

    // ==================== 3. 现场筛查录入 ====================

    @PostMapping("/screening-input")
    @Operation(summary = "现场筛查录入", description = "医生在 App 端录入学生筛查结果")
    public CommonResult<Long> screeningInput(@Valid @RequestBody DoctorScreeningInputReqVO reqVO) {
        // 1. 校验学生是否存在
        StudentInfoDO student = studentInfoMapper.selectById(reqVO.getStudentId());
        if (student == null) {
            throw exception(STUDENT_NOT_EXISTS);
        }
        // 2. 校验批次是否存在
        ScreeningBatchDO batch = screeningBatchMapper.selectById(reqVO.getBatchId());
        if (batch == null) {
            throw exception(SCREENING_BATCH_NOT_EXISTS);
        }
        // 3. 检查是否已存在筛查记录（避免重复录入）
        ScreeningRecordDO existing = screeningRecordMapper.selectByBatchIdAndStudentId(
                reqVO.getBatchId(), reqVO.getStudentId());
        if (existing != null) {
            throw exception(SCREENING_RECORD_ALREADY_EXISTS);
        }
        // 4. 创建筛查记录主表
        ScreeningRecordDO record = new ScreeningRecordDO();
        record.setRecordNo("SR" + System.currentTimeMillis());
        record.setBatchId(reqVO.getBatchId());
        record.setStudentId(reqVO.getStudentId());
        record.setScreeningDate(LocalDate.parse(reqVO.getScreeningDate()));
        record.setCheckStatus(2); // 2待审核
        record.setHasPositive(reqVO.getHasPositive() != null ? reqVO.getHasPositive() : 0);
        record.setPositiveItems(reqVO.getPositiveItems());
        record.setAuditDoctor(reqVO.getCheckerId());
        screeningRecordMapper.insert(record);

        // 5. 解析明细JSON并批量插入（detailJson 格式: [{"itemCode":"VISION_1","itemValue":"5.0","isAbnormal":0}, ...]）
        // TODO: 实际项目使用 JSON 工具解析 detailJson，此处简化处理：直接保存原始JSON到首条明细
        if (reqVO.getDetailJson() != null && !reqVO.getDetailJson().isEmpty()) {
            ScreeningResultDetailDO detail = new ScreeningResultDetailDO();
            detail.setRecordId(record.getId());
            detail.setItemCode("BATCH_JSON"); // 占位符
            detail.setItemValue(reqVO.getDetailJson());
            detail.setIsAbnormal(reqVO.getHasPositive() != null && reqVO.getHasPositive() == 1 ? 1 : 0);
            detail.setCheckerId(reqVO.getCheckerId());
            detail.setCheckTime(LocalDateTime.now());
            detail.setDeviceCode(reqVO.getDeviceCode());
            screeningResultDetailMapper.insert(detail);
        }
        return success(record.getId());
    }

    // ==================== 4. 待审核筛查列表 ====================

    @GetMapping("/pending-audit")
    @Operation(summary = "待审核筛查列表")
    @Parameter(name = "doctorId", description = "审核医生ID", example = "100")
    public CommonResult<List<DoctorPendingAuditRespVO>> pendingAudit(@RequestParam(value = "doctorId", required = false) Long doctorId) {
        LambdaQueryWrapperX<ScreeningRecordDO> wrapper = new LambdaQueryWrapperX<ScreeningRecordDO>()
                .eq(ScreeningRecordDO::getCheckStatus, 2)
                .orderByDesc(ScreeningRecordDO::getId);
        if (doctorId != null) {
            wrapper.eq(ScreeningRecordDO::getAuditDoctor, doctorId);
        }
        List<ScreeningRecordDO> records = screeningRecordMapper.selectList(wrapper);
        if (records.isEmpty()) {
            return success(new ArrayList<>());
        }
        // 批量查询学生姓名
        List<Long> studentIds = records.stream().map(ScreeningRecordDO::getStudentId)
                .distinct().collect(Collectors.toList());
        Map<Long, String> studentNameMap = new HashMap<>();
        if (!studentIds.isEmpty()) {
            List<StudentInfoDO> students = studentInfoMapper.selectBatchIds(studentIds);
            for (StudentInfoDO s : students) {
                studentNameMap.put(s.getId(), s.getName());
            }
        }
        // 批量查询批次名称
        List<Long> batchIds = records.stream().map(ScreeningRecordDO::getBatchId)
                .filter(java.util.Objects::nonNull).distinct().collect(Collectors.toList());
        Map<Long, String> batchNameMap = new HashMap<>();
        if (!batchIds.isEmpty()) {
            List<ScreeningBatchDO> batches = screeningBatchMapper.selectBatchIds(batchIds);
            for (ScreeningBatchDO b : batches) {
                batchNameMap.put(b.getId(), b.getBatchName());
            }
        }
        // 转换 VO
        List<DoctorPendingAuditRespVO> result = new ArrayList<>(records.size());
        for (ScreeningRecordDO r : records) {
            DoctorPendingAuditRespVO vo = new DoctorPendingAuditRespVO();
            vo.setRecordId(r.getId());
            vo.setRecordNo(r.getRecordNo());
            vo.setStudentId(r.getStudentId());
            vo.setStudentName(studentNameMap.get(r.getStudentId()));
            vo.setBatchId(r.getBatchId());
            vo.setBatchName(batchNameMap.get(r.getBatchId()));
            vo.setScreeningDate(r.getScreeningDate() != null ? r.getScreeningDate().toString() : null);
            vo.setCheckStatus(r.getCheckStatus());
            vo.setHasPositive(r.getHasPositive());
            vo.setPositiveItems(r.getPositiveItems());
            result.add(vo);
        }
        return success(result);
    }

    // ==================== 5. 筛查审核 ====================

    @PostMapping("/audit")
    @Operation(summary = "筛查审核", description = "审核通过或驳回筛查记录")
    public CommonResult<Boolean> audit(@Valid @RequestBody DoctorAuditReqVO reqVO) {
        ScreeningRecordDO record = screeningRecordMapper.selectById(reqVO.getRecordId());
        if (record == null) {
            throw exception(SCREENING_RECORD_NOT_EXISTS);
        }
        if (record.getCheckStatus() != 2) {
            throw exception(SCREENING_RECORD_NOT_AUDIT);
        }
        ScreeningRecordDO update = new ScreeningRecordDO();
        update.setId(record.getId());
        // 1通过 → 3已审核；2驳回 → 1进行中（重新筛查）
        update.setCheckStatus(reqVO.getAuditResult() == 1 ? 3 : 1);
        update.setAuditDoctor(reqVO.getAuditDoctorId());
        update.setAuditTime(LocalDateTime.now());
        screeningRecordMapper.updateById(update);
        return success(true);
    }

    // ==================== 6. 复筛结果录入 ====================

    @PostMapping("/recheck-input")
    @Operation(summary = "复筛结果录入", description = "医生录入复筛结果，更新阳性记录的复筛状态")
    public CommonResult<Long> recheckInput(@Valid @RequestBody DoctorRecheckInputReqVO reqVO) {
        // 1. 校验阳性记录存在
        ScreeningPositiveDO positive = screeningPositiveMapper.selectById(reqVO.getPositiveId());
        if (positive == null) {
            throw exception(POSITIVE_RECORD_NOT_EXISTS);
        }
        // 2. 检查是否已有复筛记录
        RecheckRecordDO existing = recheckRecordMapper.selectByPositiveId(reqVO.getPositiveId());
        if (existing != null) {
            throw exception(RECHECK_RECORD_ALREADY_EXISTS);
        }
        // 3. 插入复筛记录
        RecheckRecordDO recheck = new RecheckRecordDO();
        recheck.setPositiveId(reqVO.getPositiveId());
        recheck.setStudentId(reqVO.getStudentId());
        recheck.setInitialRecordId(reqVO.getInitialRecordId());
        recheck.setRecheckDate(LocalDate.parse(reqVO.getRecheckDate()));
        recheck.setRecheckItems(reqVO.getRecheckItems());
        recheck.setRecheckResult(reqVO.getRecheckResult());
        recheck.setIsStillPositive(reqVO.getIsStillPositive());
        recheck.setRecheckConclusion(reqVO.getRecheckConclusion());
        recheck.setFollowPlan(reqVO.getFollowPlan());
        recheck.setDoctorId(reqVO.getDoctorId());
        recheckRecordMapper.insert(recheck);
        // 4. 更新阳性记录的复筛状态为已完成
        ScreeningPositiveDO positiveUpdate = new ScreeningPositiveDO();
        positiveUpdate.setId(reqVO.getPositiveId());
        positiveUpdate.setRecheckStatus(2); // 2已完成
        screeningPositiveMapper.updateById(positiveUpdate);
        return success(recheck.getId());
    }

    // ==================== 7. 随访记录录入 ====================

    @PostMapping("/follow-input")
    @Operation(summary = "随访记录录入", description = "医生录入随访结果并完成任务")
    public CommonResult<Boolean> followInput(@Valid @RequestBody DoctorFollowInputReqVO reqVO) {
        FollowTaskDO task = null;
        if (reqVO.getTaskId() != null) {
            task = followTaskMapper.selectById(reqVO.getTaskId());
        }
        if (task == null) {
            // 自动创建一条新任务（兼容主动随访）
            task = new FollowTaskDO();
            task.setTaskNo("FT" + System.currentTimeMillis());
            task.setChildId(reqVO.getChildId());
            task.setTaskContent(reqVO.getFollowContent());
            task.setPlanDate(LocalDate.parse(reqVO.getFollowDate()));
            task.setResponsibleDoctor(reqVO.getDoctorId());
            task.setTaskStatus(1); // 直接完成
            task.setCompleteTime(LocalDateTime.now());
            task.setCreateTime(LocalDateTime.now());
            followTaskMapper.insert(task);
        } else {
            // 更新现有任务为已完成
            FollowTaskDO update = new FollowTaskDO();
            update.setId(task.getId());
            update.setTaskStatus(1);
            update.setCompleteTime(LocalDateTime.now());
            followTaskMapper.updateById(update);
        }
        // TODO: 实际项目应将随访内容/干预意见保存到 case_follow_record 表
        // 此处简化：返回成功
        return success(true);
    }

    // ==================== 8. 待随访任务列表 ====================

    @GetMapping("/pending-follow")
    @Operation(summary = "待随访任务列表")
    @Parameter(name = "doctorId", description = "医生ID", required = true, example = "100")
    public CommonResult<List<FollowTaskDO>> pendingFollow(@RequestParam("doctorId") Long doctorId) {
        List<FollowTaskDO> tasks = followTaskMapper.selectList(new LambdaQueryWrapperX<FollowTaskDO>()
                .eq(FollowTaskDO::getResponsibleDoctor, doctorId)
                .eq(FollowTaskDO::getTaskStatus, 0)
                .orderByAsc(FollowTaskDO::getPlanDate));
        return success(tasks);
    }

    // ==================== 9. 待复筛阳性列表 ====================

    @GetMapping("/pending-recheck")
    @Operation(summary = "待复筛阳性列表", description = "查询需要复筛且尚未完成的阳性记录")
    public CommonResult<List<ScreeningPositiveDO>> pendingRecheck() {
        List<ScreeningPositiveDO> list = screeningPositiveMapper.selectList(
                new LambdaQueryWrapperX<ScreeningPositiveDO>()
                        .eq(ScreeningPositiveDO::getNeedRecheck, 1)
                        .ne(ScreeningPositiveDO::getRecheckStatus, 2)
                        .orderByDesc(ScreeningPositiveDO::getId));
        return success(list);
    }

    // ==================== 10. 医生工作量查询 ====================

    @GetMapping("/workload")
    @Operation(summary = "医生工作量查询")
    @Parameter(name = "doctorId", description = "医生ID", required = true, example = "100")
    @Parameter(name = "startDate", description = "开始日期", example = "2026-07-01")
    @Parameter(name = "endDate", description = "结束日期", example = "2026-07-20")
    public CommonResult<List<StaffWorkloadStatisticsDO>> workload(
            @RequestParam("doctorId") Long doctorId,
            @RequestParam(value = "startDate", required = false) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) LocalDate endDate) {
        if (startDate == null) {
            startDate = LocalDate.now().minusDays(30);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }
        List<StaffWorkloadStatisticsDO> list = staffWorkloadStatisticsMapper.selectListByStaffAndDateRange(
                doctorId, startDate, endDate);
        return success(list);
    }

}

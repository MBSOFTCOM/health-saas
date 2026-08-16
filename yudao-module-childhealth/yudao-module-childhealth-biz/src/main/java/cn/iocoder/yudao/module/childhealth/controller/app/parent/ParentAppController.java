package cn.iocoder.yudao.module.childhealth.controller.app.parent;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.controller.app.parent.vo.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.management.FollowTaskDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.workflow.StudentInfoDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.management.FollowTaskMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.screening.*;
import cn.iocoder.yudao.module.childhealth.dal.mysql.workflow.StudentInfoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
 * 移动端 - 家长 App 接口
 *
 * 创建日期: 2026-07-20
 * 模块: 移动端 - 家长 App
 *
 * 用户身份获取约定（Spring Security 上下文）：
 *   - 当前登录用户ID：SecurityFrameworkUtils.getLoginUserId()
 *   - 通过家长用户ID关联查询孩子列表
 *   - 此处简化：直接以 mobile 作为家长标识，绑定 student_info.guardian_mobile
 *
 * 安全策略：
 *   - 所有接口需要 @PreAuthorize("@ss.hasPermission('childhealth:parent-app:query')")
 *   - 实际项目可扩展为家长 token 校验中间件
 */
@Tag(name = "移动端 - 家长 App")
@RestController
@RequestMapping("/childhealth-app/parent")
@Validated
public class ParentAppController {

    @Resource
    private StudentInfoMapper studentInfoMapper;
    @Resource
    private ScreeningRecordMapper screeningRecordMapper;
    @Resource
    private ScreeningResultDetailMapper screeningResultDetailMapper;
    @Resource
    private ScreeningPositiveMapper screeningPositiveMapper;
    @Resource
    private ScreeningBatchMapper screeningBatchMapper;
    @Resource
    private RecheckNotifyRecordMapper recheckNotifyRecordMapper;
    @Resource
    private DiseaseKnowledgeMapper diseaseKnowledgeMapper;
    @Resource
    private FollowTaskMapper followTaskMapper;

    // ==================== 1. 我的孩子列表 ====================

    @GetMapping("/children")
    @Operation(summary = "我的孩子列表", description = "按当前登录家长的手机号绑定孩子列表")
    @Parameter(name = "mobile", description = "家长手机号", required = true, example = "13800138000")
    public CommonResult<List<ParentChildRespVO>> myChildren(@RequestParam("mobile") String mobile) {
        // 通过 student_info.guardian_mobile 查询家长绑定的孩子列表
        List<StudentInfoDO> students = studentInfoMapper.selectList(new LambdaQueryWrapperX<StudentInfoDO>()
                .eq(StudentInfoDO::getGuardianMobile, mobile)
                .orderByDesc(StudentInfoDO::getId));
        if (students.isEmpty()) {
            return success(new ArrayList<>());
        }
        // 查询每个孩子最近一次筛查记录
        List<ParentChildRespVO> result = new ArrayList<>(students.size());
        for (StudentInfoDO stu : students) {
            ParentChildRespVO vo = new ParentChildRespVO();
            vo.setStudentId(stu.getId());
            vo.setChildId(stu.getChildId());
            vo.setStudentName(stu.getName());
            vo.setGender(stu.getGender());
            vo.setBirthDate(stu.getBirthDate());
            vo.setClassId(stu.getClassId());
            // TODO: 学校名称、班级名称需要联表查询
            // 查询最近一次筛查记录
            ScreeningRecordDO latestRecord = screeningRecordMapper.selectOne(
                    new LambdaQueryWrapperX<ScreeningRecordDO>()
                            .eq(ScreeningRecordDO::getStudentId, stu.getId())
                            .orderByDesc(ScreeningRecordDO::getScreeningDate)
                            .last("LIMIT 1"));
            if (latestRecord != null) {
                vo.setLastScreeningDate(latestRecord.getScreeningDate());
            }
            // 查询是否有未读阳性通知（recheck_notify_record 中 notify_status=1 即已发送未读）
            Long unreadCount = recheckNotifyRecordMapper.selectCount(
                    new LambdaQueryWrapperX<RecheckNotifyRecordDO>()
                            .eq(RecheckNotifyRecordDO::getStudentId, stu.getId())
                            .eq(RecheckNotifyRecordDO::getNotifyStatus, 1));
            vo.setHasUnreadPositive(unreadCount != null && unreadCount > 0 ? 1 : 0);
            result.add(vo);
        }
        return success(result);
    }

    // ==================== 2. 孩子筛查记录列表 ====================

    @GetMapping("/children/{studentId}/screening-records")
    @Operation(summary = "孩子筛查记录列表")
    @Parameter(name = "studentId", description = "学生ID", required = true)
    public CommonResult<List<ParentScreeningReportRespVO>> screeningRecords(@PathVariable("studentId") Long studentId) {
        List<ScreeningRecordDO> records = screeningRecordMapper.selectList(
                new LambdaQueryWrapperX<ScreeningRecordDO>()
                        .eq(ScreeningRecordDO::getStudentId, studentId)
                        .orderByDesc(ScreeningRecordDO::getScreeningDate));
        if (records.isEmpty()) {
            return success(new ArrayList<>());
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
        // 转换 VO（不带明细，列表页用）
        List<ParentScreeningReportRespVO> result = new ArrayList<>(records.size());
        for (ScreeningRecordDO r : records) {
            ParentScreeningReportRespVO vo = new ParentScreeningReportRespVO();
            vo.setRecordId(r.getId());
            vo.setStudentId(r.getStudentId());
            vo.setScreeningDate(r.getScreeningDate());
            vo.setBatchName(batchNameMap.get(r.getBatchId()));
            vo.setCheckStatus(r.getCheckStatus());
            vo.setHasPositive(r.getHasPositive());
            result.add(vo);
        }
        return success(result);
    }

    // ==================== 3. 筛查报告详情（含五健分类明细） ====================

    @GetMapping("/report/{recordId}")
    @Operation(summary = "查看孩子筛查报告详情", description = "按五健专项分类汇总，含阳性项目与健康指导")
    @Parameter(name = "recordId", description = "筛查记录ID", required = true)
    public CommonResult<ParentScreeningReportRespVO> reportDetail(@PathVariable("recordId") Long recordId) {
        ScreeningRecordDO record = screeningRecordMapper.selectById(recordId);
        if (record == null) {
            throw exception(SCREENING_RECORD_NOT_EXISTS);
        }
        ParentScreeningReportRespVO vo = new ParentScreeningReportRespVO();
        vo.setRecordId(record.getId());
        vo.setStudentId(record.getStudentId());
        vo.setScreeningDate(record.getScreeningDate());
        vo.setCheckStatus(record.getCheckStatus());
        vo.setHasPositive(record.getHasPositive());

        // 查询批次名称
        if (record.getBatchId() != null) {
            ScreeningBatchDO batch = screeningBatchMapper.selectById(record.getBatchId());
            if (batch != null) {
                vo.setBatchName(batch.getBatchName());
            }
        }
        // 查询筛查明细，按五健分类汇总
        List<ScreeningResultDetailDO> details = screeningResultDetailMapper.selectByRecordId(recordId);
        List<ParentScreeningReportRespVO.CategoryResult> categoryResults = groupByCategory(details);
        vo.setCategoryResults(categoryResults);
        // 阳性数 = 异常明细数
        int abnormalCount = (int) details.stream().filter(d -> d.getIsAbnormal() != null && d.getIsAbnormal() == 1).count();
        vo.setPositiveCount(abnormalCount);
        vo.setOverallConclusion(abnormalCount == 0 ? "正常" : "存在异常项");

        // 查询阳性记录的健康指导
        List<ScreeningPositiveDO> positives = screeningPositiveMapper.selectByRecordId(recordId);
        StringBuilder guidance = new StringBuilder();
        StringBuilder nextStep = new StringBuilder();
        for (ScreeningPositiveDO p : positives) {
            if (p.getHealthGuidance() != null) {
                guidance.append(p.getDiseaseName()).append("：").append(p.getHealthGuidance()).append("\n");
            }
            if (p.getNeedRecheck() != null && p.getNeedRecheck() == 1) {
                nextStep.append(p.getDiseaseName()).append(" 需要复筛；");
            }
        }
        vo.setHealthGuidance(guidance.toString());
        vo.setNextStepAdvice(nextStep.toString());
        return success(vo);
    }

    // ==================== 4. 阳性结果与健康指导 ====================

    @GetMapping("/positive/{studentId}")
    @Operation(summary = "阳性结果与健康指导列表")
    @Parameter(name = "studentId", description = "学生ID", required = true)
    public CommonResult<List<ParentPositiveRespVO>> positiveList(@PathVariable("studentId") Long studentId) {
        List<ScreeningPositiveDO> positives = screeningPositiveMapper.selectByStudentId(studentId);
        return success(BeanUtils.toBean(positives, ParentPositiveRespVO.class));
    }

    // ==================== 5. 复筛通知列表 ====================

    @GetMapping("/recheck-notify/{studentId}")
    @Operation(summary = "复筛通知列表")
    @Parameter(name = "studentId", description = "学生ID", required = true)
    public CommonResult<List<ParentRecheckNotifyRespVO>> recheckNotifyList(@PathVariable("studentId") Long studentId) {
        List<RecheckNotifyRecordDO> list = recheckNotifyRecordMapper.selectListByStudent(studentId);
        return success(BeanUtils.toBean(list, ParentRecheckNotifyRespVO.class));
    }

    // ==================== 6. 随访任务列表 ====================

    @GetMapping("/follow/{childId}")
    @Operation(summary = "随访任务列表")
    @Parameter(name = "childId", description = "儿童ID", required = true)
    public CommonResult<List<ParentFollowTaskRespVO>> followTasks(@PathVariable("childId") Long childId) {
        List<FollowTaskDO> tasks = followTaskMapper.selectList(new LambdaQueryWrapperX<FollowTaskDO>()
                .eq(FollowTaskDO::getChildId, childId)
                .orderByDesc(FollowTaskDO::getPlanDate));
        return success(BeanUtils.toBean(tasks, ParentFollowTaskRespVO.class));
    }

    // ==================== 7. 疾病知识库详情 ====================

    @GetMapping("/disease-knowledge/{diseaseCode}")
    @Operation(summary = "疾病知识库详情", description = "按疾病编码查询家长可读的健康知识")
    @Parameter(name = "diseaseCode", description = "疾病编码", required = true, example = "H52.0")
    public CommonResult<ParentDiseaseKnowledgeRespVO> diseaseKnowledge(@PathVariable("diseaseCode") String diseaseCode) {
        DiseaseKnowledgeDO knowledge = diseaseKnowledgeMapper.selectOne(
                new LambdaQueryWrapperX<DiseaseKnowledgeDO>()
                        .eq(DiseaseKnowledgeDO::getDiseaseCode, diseaseCode)
                        .eq(DiseaseKnowledgeDO::getStatus, 1));
        if (knowledge == null) {
            throw exception(DISEASE_KNOWLEDGE_NOT_EXISTS);
        }
        return success(BeanUtils.toBean(knowledge, ParentDiseaseKnowledgeRespVO.class));
    }

    // ==================== 8. 复筛通知确认（家长响应） ====================

    @PostMapping("/recheck-notify/{id}/confirm")
    @Operation(summary = "复筛通知确认", description = "家长在 App 上确认是否前往复筛")
    @Parameter(name = "id", description = "通知ID", required = true)
    public CommonResult<Boolean> confirmRecheckNotify(@PathVariable("id") Long id,
                                                       @Valid @RequestBody ParentRecheckConfirmReqVO reqVO) {
        RecheckNotifyRecordDO notify = recheckNotifyRecordMapper.selectById(id);
        if (notify == null) {
            throw exception(RECHECK_NOTIFY_RECORD_NOT_EXISTS);
        }
        RecheckNotifyRecordDO update = new RecheckNotifyRecordDO();
        update.setId(id);
        update.setNotifyStatus(4); // 4已响应
        update.setResponseTime(LocalDateTime.now());
        update.setResponseContent(String.format("是否前往：%s；确认日期：%s；备注：%s",
                reqVO.getWillGo() == 1 ? "是" : "否",
                reqVO.getConfirmDate() != null ? reqVO.getConfirmDate() : "未指定",
                reqVO.getRemark() != null ? reqVO.getRemark() : "无"));
        recheckNotifyRecordMapper.updateById(update);
        return success(true);
    }

    // ==================== 9. 标记通知已读 ====================

    @PostMapping("/recheck-notify/{id}/read")
    @Operation(summary = "标记通知已读")
    @Parameter(name = "id", description = "通知ID", required = true)
    public CommonResult<Boolean> readRecheckNotify(@PathVariable("id") Long id) {
        RecheckNotifyRecordDO notify = recheckNotifyRecordMapper.selectById(id);
        if (notify == null) {
            throw exception(RECHECK_NOTIFY_RECORD_NOT_EXISTS);
        }
        RecheckNotifyRecordDO update = new RecheckNotifyRecordDO();
        update.setId(id);
        update.setNotifyStatus(3); // 3已读
        recheckNotifyRecordMapper.updateById(update);
        return success(true);
    }

    // ==================== 10. 健康指导建议汇总 ====================

    @GetMapping("/health-guidance/{studentId}")
    @Operation(summary = "健康指导建议汇总", description = "汇总该学生所有阳性记录的健康指导")
    @Parameter(name = "studentId", description = "学生ID", required = true)
    public CommonResult<Map<String, String>> healthGuidance(@PathVariable("studentId") Long studentId) {
        List<ScreeningPositiveDO> positives = screeningPositiveMapper.selectByStudentId(studentId);
        Map<String, String> guidanceMap = new HashMap<>();
        for (ScreeningPositiveDO p : positives) {
            String key = p.getDiseaseName() != null ? p.getDiseaseName() : p.getDiseaseCode();
            guidanceMap.put(key, p.getHealthGuidance());
        }
        return success(guidanceMap);
    }

    // ==================== 11. 多子女显式绑定/解绑 ====================

    @PostMapping("/children/bind")
    @Operation(summary = "绑定孩子", description = "家长通过手机号显式绑定学生档案，写入 student_info.guardian_mobile")
    public CommonResult<Boolean> bindChild(
            @RequestParam("mobile") String mobile,
            @RequestParam("studentId") Long studentId) {
        if (mobile == null || mobile.isBlank()) {
            throw exception(PARENT_MOBILE_REQUIRED);
        }
        StudentInfoDO student = studentInfoMapper.selectById(studentId);
        if (student == null) {
            throw exception(STUDENT_NOT_EXISTS);
        }
        // 已绑定其他手机号则拒绝（避免误绑），允许同号重复绑定
        if (student.getGuardianMobile() != null
                && !student.getGuardianMobile().isBlank()
                && !mobile.equals(student.getGuardianMobile())) {
            throw exception(STUDENT_ALREADY_BOUND);
        }
        StudentInfoDO update = new StudentInfoDO();
        update.setId(studentId);
        update.setGuardianMobile(mobile);
        studentInfoMapper.updateById(update);
        return success(true);
    }

    @DeleteMapping("/children/unbind")
    @Operation(summary = "解绑孩子", description = "家长解绑学生档案（校验 mobile 匹配后清空 guardian_mobile）")
    public CommonResult<Boolean> unbindChild(
            @RequestParam("mobile") String mobile,
            @RequestParam("studentId") Long studentId) {
        StudentInfoDO student = studentInfoMapper.selectById(studentId);
        if (student == null) {
            throw exception(STUDENT_NOT_EXISTS);
        }
        if (student.getGuardianMobile() == null
                || !mobile.equals(student.getGuardianMobile())) {
            throw exception(STUDENT_NOT_BOUND_BY_YOU);
        }
        StudentInfoDO update = new StudentInfoDO();
        update.setId(studentId);
        update.setGuardianMobile("");
        studentInfoMapper.updateById(update);
        return success(true);
    }

    // ==================== 内部辅助方法 ====================

    /**
     * 按 itemCode 前缀分组筛查明细，返回五健专项分类汇总
     */
    private List<ParentScreeningReportRespVO.CategoryResult> groupByCategory(List<ScreeningResultDetailDO> details) {
        // 按分类编码分组
        Map<String, List<ScreeningResultDetailDO>> grouped = new HashMap<>();
        for (ScreeningResultDetailDO d : details) {
            String cat = resolveCategoryCode(d.getItemCode());
            grouped.computeIfAbsent(cat, k -> new ArrayList<>()).add(d);
        }
        // 构造 VO（按固定顺序：视力/口腔/骨骼/心理/体形）
        List<ParentScreeningReportRespVO.CategoryResult> result = new ArrayList<>(5);
        result.add(buildCategoryResult("VISION", "视力", grouped.get("VISION")));
        result.add(buildCategoryResult("ORAL", "口腔", grouped.get("ORAL")));
        result.add(buildCategoryResult("BONE", "骨骼", grouped.get("BONE")));
        result.add(buildCategoryResult("PSYCHOLOGICAL", "心理", grouped.get("PSYCHOLOGICAL")));
        result.add(buildCategoryResult("SHAPE", "体形", grouped.get("SHAPE")));
        return result;
    }

    private String resolveCategoryCode(String itemCode) {
        if (itemCode == null) return "OTHER";
        String upper = itemCode.toUpperCase();
        if (upper.startsWith("VISION_") || upper.startsWith("V_")) return "VISION";
        if (upper.startsWith("ORAL_") || upper.startsWith("O_")) return "ORAL";
        if (upper.startsWith("BONE_") || upper.startsWith("B_")) return "BONE";
        if (upper.startsWith("PSYCHOLOGICAL_") || upper.startsWith("PSY_")) return "PSYCHOLOGICAL";
        if (upper.startsWith("SHAPE_") || upper.startsWith("S_")) return "SHAPE";
        return "OTHER";
    }

    private ParentScreeningReportRespVO.CategoryResult buildCategoryResult(String code, String name,
                                                                            List<ScreeningResultDetailDO> details) {
        ParentScreeningReportRespVO.CategoryResult vo = new ParentScreeningReportRespVO.CategoryResult();
        vo.setCategoryCode(code);
        vo.setCategoryName(name);
        if (details == null || details.isEmpty()) {
            vo.setResult("未筛查");
            vo.setIsAbnormal(0);
            return vo;
        }
        boolean hasAbnormal = details.stream().anyMatch(d -> d.getIsAbnormal() != null && d.getIsAbnormal() == 1);
        vo.setIsAbnormal(hasAbnormal ? 1 : 0);
        vo.setResult(hasAbnormal ? "异常" : "正常");
        if (hasAbnormal) {
            // 拼接异常项
            String abnormalDesc = details.stream()
                    .filter(d -> d.getIsAbnormal() != null && d.getIsAbnormal() == 1)
                    .map(ScreeningResultDetailDO::getItemValue)
                    .filter(java.util.Objects::nonNull)
                    .collect(Collectors.joining("; "));
            vo.setAbnormalDesc(abnormalDesc);
            vo.setAdvice("建议进一步检查或咨询医生");
        }
        return vo;
    }

}

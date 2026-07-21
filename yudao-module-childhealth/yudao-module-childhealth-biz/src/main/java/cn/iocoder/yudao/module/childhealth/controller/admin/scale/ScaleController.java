package cn.iocoder.yudao.module.childhealth.controller.admin.scale;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.api.scale.ScaleService;
import cn.iocoder.yudao.module.childhealth.api.scale.dto.ScaleDTO.*;
import cn.iocoder.yudao.module.childhealth.api.scale.dto.ScaleAssessmentDTO.*;
import cn.iocoder.yudao.module.childhealth.api.scale.dto.PsychologicalScaleCreateReqDTO;
import cn.iocoder.yudao.module.childhealth.api.scale.dto.PsychologicalScaleRespDTO;
import cn.iocoder.yudao.module.childhealth.service.scale.PsychologicalScaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 量表评估")
@RestController
@RequestMapping("/childhealth/scale")
public class ScaleController {

    @Resource
    private ScaleService scaleService;

    @Resource
    private PsychologicalScaleService psychologicalScaleService;

    // ==================== 量表配置管理 ====================

    @PostMapping("/config")
    @Operation(summary = "创建量表配置")
    @PreAuthorize("@ss.hasPermission('childhealth:scale:create')")
    public CommonResult<Long> createScaleConfig(@Valid @RequestBody ScaleConfigCreateRequest request) {
        return success(scaleService.createScaleConfig(request));
    }

    @PutMapping("/config")
    @Operation(summary = "更新量表配置")
    @PreAuthorize("@ss.hasPermission('childhealth:scale:update')")
    public CommonResult<Boolean> updateScaleConfig(@Valid @RequestBody ScaleConfigUpdateRequest request) {
        scaleService.updateScaleConfig(request);
        return success(true);
    }

    @DeleteMapping("/config/{id}")
    @Operation(summary = "删除量表配置")
    @Parameter(name = "id", description = "量表ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:scale:delete')")
    public CommonResult<Boolean> deleteScaleConfig(@PathVariable Long id) {
        scaleService.deleteScaleConfig(id);
        return success(true);
    }

    @GetMapping("/config/{id}")
    @Operation(summary = "获取量表配置详情")
    @Parameter(name = "id", description = "量表ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:scale:query')")
    public CommonResult<ScaleConfigResponse> getScaleConfig(@PathVariable Long id) {
        return success(scaleService.getScaleConfig(id));
    }

    @PostMapping("/config/page")
    @Operation(summary = "分页查询量表配置")
    @PreAuthorize("@ss.hasPermission('childhealth:scale:query')")
    public CommonResult<PageResult<ScaleConfigResponse>> getScaleConfigPage(@RequestBody ScaleQueryRequest request) {
        return success(scaleService.getScaleConfigPage(request));
    }

    @GetMapping("/config/active")
    @Operation(summary = "获取所有启用的量表配置")
    @PreAuthorize("@ss.hasPermission('childhealth:scale:query')")
    public CommonResult<List<ScaleConfigResponse>> getActiveScaleConfigs() {
        return success(scaleService.getActiveScaleConfigs());
    }

    @GetMapping("/config/applicable/{ageMonth}")
    @Operation(summary = "根据年龄获取适用的量表列表")
    @Parameter(name = "ageMonth", description = "月龄", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:scale:query')")
    public CommonResult<List<ScaleConfigResponse>> getApplicableScales(@PathVariable Integer ageMonth) {
        return success(scaleService.getApplicableScales(ageMonth));
    }

    // ==================== 心理量表管理（原PsychologicalScaleController） ====================

    @PostMapping("/psychological")
    @Operation(summary = "创建心理量表")
    @PreAuthorize("@ss.hasPermission('childhealth:scale:create')")
    public CommonResult<Long> createPsychologicalScale(@Valid @RequestBody PsychologicalScaleCreateReqDTO request) {
        return success(psychologicalScaleService.createPsychologicalScale(request));
    }

    @GetMapping("/psychological/{id}")
    @Operation(summary = "获取心理量表")
    @PreAuthorize("@ss.hasPermission('childhealth:scale:query')")
    public CommonResult<PsychologicalScaleRespDTO> getPsychologicalScale(@PathVariable Long id) {
        return success(psychologicalScaleService.getPsychologicalScale(id));
    }

    @GetMapping("/psychological/available")
    @Operation(summary = "查询月龄可用心理量表")
    @PreAuthorize("@ss.hasPermission('childhealth:scale:query')")
    public CommonResult<List<PsychologicalScaleRespDTO>> getPsychologicalScalesByAgeAndType(
            @RequestParam Integer ageMonths,
            @RequestParam(required = false) String scaleType) {
        return success(psychologicalScaleService.getPsychologicalScalesByAgeAndType(ageMonths, scaleType));
    }

    @GetMapping("/psychological/list")
    @Operation(summary = "获取所有活跃心理量表")
    @PreAuthorize("@ss.hasPermission('childhealth:scale:query')")
    public CommonResult<List<PsychologicalScaleRespDTO>> getAllActivePsychologicalScales() {
        return success(psychologicalScaleService.getAllActivePsychologicalScales());
    }

    @GetMapping("/psychological/{id}/questions")
    @Operation(summary = "获取心理量表题目")
    @PreAuthorize("@ss.hasPermission('childhealth:scale:query')")
    public CommonResult<List<QuestionResponse>> getPsychologicalQuestions(@PathVariable Long id) {
        return success(psychologicalScaleService.getQuestions(id));
    }

    @GetMapping("/psychological/{id}/scoring")
    @Operation(summary = "获取心理量表计分规则")
    @PreAuthorize("@ss.hasPermission('childhealth:scale:query')")
    public CommonResult<Map<String, String>> getPsychologicalScoring(@PathVariable Long id) {
        return success(psychologicalScaleService.getScoring(id));
    }

    // ==================== 量表题目管理 ====================

    @PostMapping("/question")
    @Operation(summary = "创建量表题目")
    @PreAuthorize("@ss.hasPermission('childhealth:scale:create')")
    public CommonResult<Long> createScaleQuestion(@Valid @RequestBody ScaleQuestionCreateRequest request) {
        return success(scaleService.createScaleQuestion(request));
    }

    @PostMapping("/question/batch")
    @Operation(summary = "批量创建量表题目")
    @PreAuthorize("@ss.hasPermission('childhealth:scale:create')")
    public CommonResult<Boolean> batchCreateScaleQuestions(@Valid @RequestBody ScaleQuestionBatchCreateRequest request) {
        scaleService.batchCreateScaleQuestions(request);
        return success(true);
    }

    @PutMapping("/question/{id}")
    @Operation(summary = "更新量表题目")
    @Parameter(name = "id", description = "题目ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:scale:update')")
    public CommonResult<Boolean> updateScaleQuestion(
            @PathVariable Long id,
            @Valid @RequestBody ScaleQuestionCreateRequest request) {
        scaleService.updateScaleQuestion(id, request);
        return success(true);
    }

    @DeleteMapping("/question/{id}")
    @Operation(summary = "删除量表题目")
    @Parameter(name = "id", description = "题目ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:scale:delete')")
    public CommonResult<Boolean> deleteScaleQuestion(@PathVariable Long id) {
        scaleService.deleteScaleQuestion(id);
        return success(true);
    }

    @GetMapping("/question/scale/{scaleId}")
    @Operation(summary = "获取量表的所有题目")
    @Parameter(name = "scaleId", description = "量表ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:scale:query')")
    public CommonResult<List<ScaleQuestionResponse>> getScaleQuestions(@PathVariable Long scaleId) {
        return success(scaleService.getScaleQuestions(scaleId));
    }

    @DeleteMapping("/question/scale/{scaleId}")
    @Operation(summary = "批量删除量表的题目")
    @Parameter(name = "scaleId", description = "量表ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:scale:delete')")
    public CommonResult<Boolean> deleteScaleQuestionsByScale(@PathVariable Long scaleId) {
        scaleService.deleteScaleQuestionsByScale(scaleId);
        return success(true);
    }

    // ==================== 量表评估管理 ====================

    @PostMapping("/assessment/submit")
    @Operation(summary = "提交量表评估")
    @PreAuthorize("@ss.hasPermission('childhealth:scale:assess')")
    public CommonResult<Long> submitScaleAssessment(@Valid @RequestBody ScaleAssessmentSubmitRequest request) {
        return success(scaleService.submitScaleAssessment(request));
    }

    @PostMapping("/assessment/psychological")
    @Operation(summary = "提交心理量表评估")
    @PreAuthorize("@ss.hasPermission('childhealth:scale:assess')")
    public CommonResult<Long> submitPsychologicalAssessment(@Valid @RequestBody SubmitRequest request) {
        return success(psychologicalScaleService.submitAssessment(request));
    }

    @GetMapping("/assessment/{id}")
    @Operation(summary = "获取评估记录详情")
    @Parameter(name = "id", description = "评估记录ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:scale:query')")
    public CommonResult<ScaleAssessmentRecordResponse> getScaleAssessment(@PathVariable Long id) {
        return success(scaleService.getScaleAssessment(id));
    }

    @GetMapping("/assessment/psychological/{id}")
    @Operation(summary = "获取心理评估记录详情")
    @Parameter(name = "id", description = "心理评估记录ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:scale:query')")
    public CommonResult<AssessmentResponse> getPsychologicalAssessment(@PathVariable Long id) {
        return success(psychologicalScaleService.getAssessment(id));
    }

    @PostMapping("/assessment/page")
    @Operation(summary = "分页查询评估记录")
    @PreAuthorize("@ss.hasPermission('childhealth:scale:query')")
    public CommonResult<PageResult<ScaleAssessmentRecordResponse>> getScaleAssessmentPage(
            @RequestBody ScaleAssessmentQueryRequest request) {
        return success(scaleService.getScaleAssessmentPage(request));
    }

    @GetMapping("/assessment/list")
    @Operation(summary = "查询心理评估记录列表")
    @PreAuthorize("@ss.hasPermission('childhealth:scale:query')")
    public CommonResult<List<AssessmentResponse>> getPsychologicalAssessments(
            @RequestParam(required = false) Long childId,
            @RequestParam(required = false) Long scaleId) {
        return success(psychologicalScaleService.getAssessments(childId, scaleId));
    }

    @GetMapping("/assessment/child/{childId}")
    @Operation(summary = "获取儿童的评估记录列表")
    @Parameter(name = "childId", description = "儿童ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:scale:query')")
    public CommonResult<List<ScaleAssessmentRecordResponse>> getScaleAssessmentsByChild(@PathVariable Long childId) {
        return success(scaleService.getScaleAssessmentsByChild(childId));
    }

    @DeleteMapping("/assessment/{id}")
    @Operation(summary = "删除评估记录")
    @Parameter(name = "id", description = "评估记录ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:scale:delete')")
    public CommonResult<Boolean> deleteScaleAssessment(@PathVariable Long id) {
        scaleService.deleteScaleAssessment(id);
        return success(true);
    }

    @PostMapping("/assessment/{id}/auto-score")
    @Operation(summary = "自动计分评估")
    @PreAuthorize("@ss.hasPermission('childhealth:scale:assess')")
    public CommonResult<AssessmentResponse> autoScore(@PathVariable Long id) {
        return success(psychologicalScaleService.getAssessment(id));
    }

    // ==================== 评估报告管理 ====================

    @PostMapping("/report/generate")
    @Operation(summary = "生成评估报告")
    @PreAuthorize("@ss.hasPermission('childhealth:scale:report')")
    public CommonResult<Long> generateAssessmentReport(@Valid @RequestBody AssessmentReportGenerateRequest request) {
        return success(scaleService.generateAssessmentReport(request));
    }

    @PostMapping("/report/psychological")
    @Operation(summary = "生成心理评估报告")
    @PreAuthorize("@ss.hasPermission('childhealth:scale:report')")
    public CommonResult<Long> generatePsychologicalReport(@Valid @RequestBody ReportRequest request) {
        return success(psychologicalScaleService.createReport(request.getAssessmentId()));
    }

    @GetMapping("/report/{id}")
    @Operation(summary = "获取评估报告详情")
    @Parameter(name = "id", description = "报告ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:scale:query')")
    public CommonResult<AssessmentReportResponse> getAssessmentReport(@PathVariable Long id) {
        return success(scaleService.getAssessmentReport(id));
    }

    @GetMapping("/report/assessment/{assessmentId}")
    @Operation(summary = "根据评估记录获取报告")
    @Parameter(name = "assessmentId", description = "评估记录ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:scale:query')")
    public CommonResult<AssessmentReportResponse> getAssessmentReportByAssessment(@PathVariable Long assessmentId) {
        return success(scaleService.getAssessmentReportByAssessment(assessmentId));
    }

    // ==================== 发育里程碑管理 ====================

    @PostMapping("/milestone")
    @Operation(summary = "创建发育里程碑")
    @PreAuthorize("@ss.hasPermission('childhealth:scale:create')")
    public CommonResult<Long> createDevelopmentMilestone(@Valid @RequestBody DevelopmentMilestoneCreateRequest request) {
        return success(scaleService.createDevelopmentMilestone(request));
    }

    @GetMapping("/milestone/all")
    @Operation(summary = "获取所有发育里程碑")
    @PreAuthorize("@ss.hasPermission('childhealth:scale:query')")
    public CommonResult<List<DevelopmentMilestoneResponse>> getAllDevelopmentMilestones() {
        return success(scaleService.getAllDevelopmentMilestones());
    }

    @GetMapping("/milestone/domain/{domain}")
    @Operation(summary = "根据发育领域获取里程碑")
    @Parameter(name = "domain", description = "发育领域", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:scale:query')")
    public CommonResult<List<DevelopmentMilestoneResponse>> getDevelopmentMilestonesByDomain(@PathVariable String domain) {
        return success(scaleService.getDevelopmentMilestonesByDomain(domain));
    }

    @GetMapping("/milestone/age/{ageMonth}")
    @Operation(summary = "根据月龄获取发育里程碑")
    @Parameter(name = "ageMonth", description = "月龄", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:scale:query')")
    public CommonResult<List<DevelopmentMilestoneResponse>> getDevelopmentMilestonesByAge(@PathVariable Integer ageMonth) {
        return success(scaleService.getDevelopmentMilestonesByAge(ageMonth));
    }

    // ==================== 儿童发育评估管理 ====================

    @PostMapping("/development-assessment")
    @Operation(summary = "创建儿童发育评估")
    @PreAuthorize("@ss.hasPermission('childhealth:scale:assess')")
    public CommonResult<Long> createChildDevelopmentAssessment(
            @Valid @RequestBody ChildDevelopmentAssessmentCreateRequest request) {
        return success(scaleService.createChildDevelopmentAssessment(request));
    }

    @PutMapping("/development-assessment/{id}")
    @Operation(summary = "更新儿童发育评估")
    @Parameter(name = "id", description = "评估ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:scale:update')")
    public CommonResult<Boolean> updateChildDevelopmentAssessment(
            @PathVariable Long id,
            @Valid @RequestBody ChildDevelopmentAssessmentCreateRequest request) {
        scaleService.updateChildDevelopmentAssessment(id, request);
        return success(true);
    }

    @GetMapping("/development-assessment/{id}")
    @Operation(summary = "获取儿童发育评估详情")
    @Parameter(name = "id", description = "评估ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:scale:query')")
    public CommonResult<ChildDevelopmentAssessmentResponse> getChildDevelopmentAssessment(@PathVariable Long id) {
        return success(scaleService.getChildDevelopmentAssessment(id));
    }

    @GetMapping("/development-assessment/child/{childId}")
    @Operation(summary = "获取儿童的发育评估列表")
    @Parameter(name = "childId", description = "儿童ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:scale:query')")
    public CommonResult<List<ChildDevelopmentAssessmentResponse>> getChildDevelopmentAssessmentsByChild(
            @PathVariable Long childId) {
        return success(scaleService.getChildDevelopmentAssessmentsByChild(childId));
    }

    @GetMapping("/development-assessment/check-delay")
    @Operation(summary = "判断儿童是否有发育迟缓")
    @PreAuthorize("@ss.hasPermission('childhealth:scale:query')")
    public CommonResult<Boolean> checkDevelopmentDelay(
            @RequestParam Long childId,
            @RequestParam Integer monthAge) {
        return success(scaleService.checkDevelopmentDelay(childId, monthAge));
    }
}
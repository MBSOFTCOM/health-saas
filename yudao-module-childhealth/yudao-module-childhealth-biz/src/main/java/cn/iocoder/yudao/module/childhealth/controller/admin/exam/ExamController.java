package cn.iocoder.yudao.module.childhealth.controller.admin.exam;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.exam.vo.*;
import cn.iocoder.yudao.module.childhealth.controller.admin.medical.vo.MedicalRecordTemplatePageReqVO;
import cn.iocoder.yudao.module.childhealth.controller.admin.medical.vo.MedicalRecordTemplateRespVO;
import cn.iocoder.yudao.module.childhealth.service.exam.ExamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

/**
 * 体检业务 Controller
 */
@Tag(name = "管理后台 - 体检业务")
@RestController
@RequestMapping("/childhealth/exam")
@Validated
public class ExamController {

    @Resource
    private ExamService examService;

    // ==================== 预约管理 ====================

    @PostMapping("/appointment/create")
    @Operation(summary = "创建体检预约")
    @PreAuthorize("@ss.hasPermission('childhealth:exam:create')")
    public CommonResult<Long> createAppointment(@Valid @RequestBody ExamAppointmentCreateReqVO reqVO) {
        return success(examService.createAppointment(reqVO));
    }

    @PutMapping("/appointment/update-status")
    @Operation(summary = "更新预约状态")
    @PreAuthorize("@ss.hasPermission('childhealth:exam:update')")
    public CommonResult<Boolean> updateAppointmentStatus(
            @RequestParam("id") Long id,
            @RequestParam("status") Integer status) {
        examService.updateAppointmentStatus(id, status);
        return success(true);
    }

    @GetMapping("/appointment/page")
    @Operation(summary = "获取体检预约分页列表")
    @PreAuthorize("@ss.hasPermission('childhealth:exam:query')")
    public CommonResult<PageResult<ExamAppointmentRespVO>> getAppointmentPage(@Valid ExamAppointmentPageReqVO reqVO) {
        return success(examService.getAppointmentPage(reqVO));
    }

    @GetMapping("/appointment/get")
    @Operation(summary = "获取体检预约详情")
    @Parameter(name = "id", description = "预约ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:exam:query')")
    public CommonResult<ExamAppointmentRespVO> getAppointment(@RequestParam("id") Long id) {
        return success(examService.getAppointment(id));
    }

    @PutMapping("/appointment/cancel")
    @Operation(summary = "取消体检预约")
    @Parameter(name = "id", description = "预约ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:exam:update')")
    public CommonResult<Boolean> cancelAppointment(@RequestParam("id") Long id) {
        examService.cancelAppointment(id);
        return success(true);
    }

    // ==================== 体检记录管理 ====================

    @PostMapping("/record/create")
    @Operation(summary = "创建体检记录")
    @PreAuthorize("@ss.hasPermission('childhealth:exam:create')")
    public CommonResult<Long> createExamRecord(@Valid @RequestBody ExamRecordCreateReqVO reqVO) {
        return success(examService.createExamRecord(reqVO));
    }

    @GetMapping("/record/page")
    @Operation(summary = "获取体检记录分页列表")
    @PreAuthorize("@ss.hasPermission('childhealth:exam:query')")
    public CommonResult<PageResult<ExamRecordRespVO>> getExamRecordPage(@Valid ExamRecordPageReqVO reqVO) {
        return success(examService.getExamRecordPage(reqVO));
    }

    @GetMapping("/record/get")
    @Operation(summary = "获取体检记录详情")
    @Parameter(name = "id", description = "体检记录ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:exam:query')")
    public CommonResult<ExamRecordRespVO> getExamRecord(@RequestParam("id") Long id) {
        return success(examService.getExamRecord(id));
    }

    @PutMapping("/record/submit")
    @Operation(summary = "提交体检记录审核")
    @Parameter(name = "id", description = "体检记录ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:exam:update')")
    public CommonResult<Boolean> submitExamRecord(@RequestParam("id") Long id) {
        examService.submitExamRecord(id);
        return success(true);
    }

    @PutMapping("/record/review")
    @Operation(summary = "审核体检记录")
    @PreAuthorize("@ss.hasPermission('childhealth:exam:update')")
    public CommonResult<Boolean> reviewExamRecord(
            @RequestParam("id") Long id,
            @RequestParam("approved") Boolean approved) {
        examService.reviewExamRecord(id, approved);
        return success(true);
    }

    // ==================== 体格检查 ====================

    @PostMapping("/physical/create")
    @Operation(summary = "录入体格检查数据（自动识别异常）")
    @PreAuthorize("@ss.hasPermission('childhealth:exam:create')")
    public CommonResult<Long> createPhysicalExam(@Valid @RequestBody PhysicalExamCreateReqVO reqVO) {
        return success(examService.createPhysicalExam(reqVO));
    }

    @PutMapping("/physical/update")
    @Operation(summary = "更新体格检查数据")
    @PreAuthorize("@ss.hasPermission('childhealth:exam:update')")
    public CommonResult<Boolean> updatePhysicalExam(
            @RequestParam("id") Long id,
            @Valid @RequestBody PhysicalExamCreateReqVO reqVO) {
        examService.updatePhysicalExam(id, reqVO);
        return success(true);
    }

    // ==================== 专科检查 ====================

    @PostMapping("/eye/create")
    @Operation(summary = "录入眼保健检查数据（自动识别异常）")
    @PreAuthorize("@ss.hasPermission('childhealth:exam:create')")
    public CommonResult<Long> createEyeExam(@Valid @RequestBody EyeExamCreateReqVO reqVO) {
        return success(examService.createEyeExam(reqVO));
    }

    @PostMapping("/hearing/create")
    @Operation(summary = "录入听力检查数据（自动识别异常）")
    @PreAuthorize("@ss.hasPermission('childhealth:exam:create')")
    public CommonResult<Long> createHearingExam(@Valid @RequestBody HearingExamCreateReqVO reqVO) {
        return success(examService.createHearingExam(reqVO));
    }

    @PostMapping("/oral/create")
    @Operation(summary = "录入口腔检查数据（自动识别异常）")
    @PreAuthorize("@ss.hasPermission('childhealth:exam:create')")
    public CommonResult<Long> createOralExam(@Valid @RequestBody OralExamCreateReqVO reqVO) {
        return success(examService.createOralExam(reqVO));
    }

    // ==================== 辅助检查报告 ====================

    @PostMapping("/lab-report/create")
    @Operation(summary = "录入辅助检查报告（LIS/PACS/手工）")
    @PreAuthorize("@ss.hasPermission('childhealth:exam:create')")
    public CommonResult<Long> createLabReport(@Valid @RequestBody LabReportCreateReqVO reqVO) {
        return success(examService.createLabReport(reqVO));
    }

    @GetMapping("/lab-report/list")
    @Operation(summary = "获取体检记录的辅助检查报告列表")
    @Parameter(name = "examId", description = "体检记录ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:exam:query')")
    public CommonResult<List<LabReportRespVO>> getLabReportsByExamId(@RequestParam("examId") Long examId) {
        return success(examService.getLabReportsByExamId(examId));
    }

    // ==================== 病历模板 ====================

    @GetMapping("/template/get-applicable")
    @Operation(summary = "根据类型和月龄获取适用的病历模板")
    @PreAuthorize("@ss.hasPermission('childhealth:exam:query')")
    public CommonResult<MedicalRecordTemplateRespVO> getApplicableTemplate(
            @RequestParam("templateType") String templateType,
            @RequestParam("monthAge") Integer monthAge) {
        return success(examService.getApplicableTemplate(templateType, monthAge));
    }

    @GetMapping("/template/page")
    @Operation(summary = "获取病历模板分页列表")
    @PreAuthorize("@ss.hasPermission('childhealth:exam:query')")
    public CommonResult<PageResult<MedicalRecordTemplateRespVO>> getTemplatePage(@Valid MedicalRecordTemplatePageReqVO reqVO) {
        return success(examService.getTemplatePage(reqVO));
    }

    @GetMapping("/template/get")
    @Operation(summary = "获取病历模板详情")
    @Parameter(name = "id", description = "模板ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:exam:query')")
    public CommonResult<MedicalRecordTemplateRespVO> getTemplate(@RequestParam("id") Long id) {
        return success(examService.getTemplate(id));
    }

    // ==================== 公卫保健计划 ====================

    @PostMapping("/care-plan/generate")
    @Operation(summary = "根据儿童出生日期和首次就诊日期自动生成公卫儿童保健计划")
    @Parameter(name = "childId", description = "儿童ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:exam:create')")
    public CommonResult<Long> generateCarePlan(@RequestParam("childId") Long childId) {
        return success(examService.generateCarePlan(childId));
    }

    @PostMapping("/appointment/add-manual")
    @Operation(summary = "手动添加单次预约")
    @PreAuthorize("@ss.hasPermission('childhealth:exam:create')")
    public CommonResult<Long> addManualAppointment(@Valid @RequestBody ExamAppointmentCreateReqVO reqVO) {
        return success(examService.addManualAppointment(reqVO));
    }

    @GetMapping("/care-plan/list-by-child")
    @Operation(summary = "获取儿童的保健计划列表")
    @Parameter(name = "childId", description = "儿童ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:exam:query')")
    public CommonResult<List<HealthCarePlanRespVO>> getCarePlansByChildId(@RequestParam("childId") Long childId) {
        return success(examService.getCarePlansByChildId(childId));
    }

    @GetMapping("/appointment/list-by-plan")
    @Operation(summary = "获取保健计划下的预约列表")
    @Parameter(name = "planId", description = "计划ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:exam:query')")
    public CommonResult<List<ExamAppointmentRespVO>> getAppointmentsByPlanId(@RequestParam("planId") Long planId) {
        return success(examService.getAppointmentsByPlanId(planId));
    }
}
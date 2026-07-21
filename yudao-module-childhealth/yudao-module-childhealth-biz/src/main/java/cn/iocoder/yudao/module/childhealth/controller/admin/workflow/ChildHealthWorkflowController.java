package cn.iocoder.yudao.module.childhealth.controller.admin.workflow;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.childhealth.api.workflow.dto.ChildHealthWorkflowDTO.*;
import java.util.List;
import cn.iocoder.yudao.module.childhealth.service.workflow.ChildHealthWorkflowService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 儿童健康核心流程")
@RestController
@RequestMapping("/childhealth/workflow")
public class ChildHealthWorkflowController {
    @Resource private ChildHealthWorkflowService service;

    @PostMapping("/child") @PreAuthorize("@ss.hasPermission('childhealth:child:create')")
    public CommonResult<Long> createChild(@Valid @RequestBody ChildCreateRequest request) {
        return success(service.createChild(request));
    }
    @GetMapping("/child") @PreAuthorize("@ss.hasPermission('childhealth:child:query')")
    public CommonResult<ChildResponse> getChild(@RequestParam Long id) { return success(service.getChild(id)); }
    @PutMapping("/child/{id}") @PreAuthorize("@ss.hasPermission('childhealth:child:update')")
    public CommonResult<Boolean> updateChild(@PathVariable Long id,
            @Valid @RequestBody ChildUpdateRequest request) {
        service.updateChild(id, request); return success(true);
    }
    @GetMapping("/child/search") @PreAuthorize("@ss.hasPermission('childhealth:child:query')")
    public CommonResult<List<ChildResponse>> searchChildren(
            @RequestParam(required = false) String keyword, @RequestParam(required = false) String status) {
        return success(service.searchChildren(keyword, status));
    }
    @PostMapping("/child/qrcode/{id}") @PreAuthorize("@ss.hasPermission('childhealth:child:update')")
    public CommonResult<String> childQrCode(@PathVariable Long id) { return success(service.generateChildQrCode(id)); }
    @PostMapping("/child/qrcode/batch") @PreAuthorize("@ss.hasPermission('childhealth:child:update')")
    public CommonResult<List<String>> childQrCodes(@RequestBody List<Long> ids) { return success(service.generateChildQrCodes(ids)); }
    @PostMapping("/checkup") @PreAuthorize("@ss.hasPermission('childhealth:checkup:create')")
    public CommonResult<Long> createCheckup(@Valid @RequestBody CheckupCreateRequest request) {
        return success(service.createCheckup(request));
    }
    @GetMapping("/checkup/{id}") @PreAuthorize("@ss.hasPermission('childhealth:checkup:query')")
    public CommonResult<CheckupResponse> getCheckup(@PathVariable Long id) {
        return success(service.getCheckup(id));
    }
    @PutMapping("/checkup/{id}") @PreAuthorize("@ss.hasPermission('childhealth:checkup:update')")
    public CommonResult<Boolean> updateCheckup(@PathVariable Long id, @Valid @RequestBody CheckupCreateRequest request) {
        service.updateCheckup(id, request); return success(true);
    }
    @GetMapping("/checkup/abnormal") @PreAuthorize("@ss.hasPermission('childhealth:checkup:query')")
    public CommonResult<List<CheckupResponse>> abnormalCheckups() { return success(service.getAbnormalCheckups()); }
    @PostMapping("/checkup/{id}/submit") @PreAuthorize("@ss.hasPermission('childhealth:checkup:update')")
    public CommonResult<Boolean> submitCheckup(@PathVariable Long id) { service.submitCheckup(id); return success(true); }
    @PutMapping("/checkup/review") @PreAuthorize("@ss.hasPermission('childhealth:checkup:review')")
    public CommonResult<Boolean> review(@RequestParam Long id, @RequestParam Long reviewerId,
            @RequestParam boolean approved) { service.reviewCheckup(id, reviewerId, approved); return success(true); }
    @PutMapping("/checkup/batch-review") @PreAuthorize("@ss.hasPermission('childhealth:checkup:review')")
    public CommonResult<Boolean> batchReview(@Valid @RequestBody CheckupBatchReviewRequest request) {
        service.batchReviewCheckup(request.getIds(), request.getReviewerId(), request.getApproved());
        return success(true);
    }
    @PutMapping("/checkup/{id}/physical") @PreAuthorize("@ss.hasPermission('childhealth:checkup:update')")
    public CommonResult<Long> physical(@PathVariable Long id, @Valid @RequestBody PhysicalExamRequest request) {
        return success(service.savePhysicalExam(id, request));
    }
    @PutMapping("/checkup/{id}/eye") @PreAuthorize("@ss.hasPermission('childhealth:eye:create')")
    public CommonResult<Long> eye(@PathVariable Long id, @RequestBody EyeExamRequest request) {
        request.setExamId(id); return success(service.recordEyeExam(request));
    }
    @PostMapping("/checkup/hearing") @PreAuthorize("@ss.hasPermission('childhealth:hearing:create')")
    public CommonResult<Long> hearing(@Valid @RequestBody HearingExamRequest request) {
        return success(service.recordHearingExam(request));
    }
    @PutMapping("/checkup/{id}/hearing") @PreAuthorize("@ss.hasPermission('childhealth:hearing:create')")
    public CommonResult<Long> hearingByExam(@PathVariable Long id, @RequestBody HearingExamRequest request) {
        request.setExamId(id); return success(service.recordHearingExam(request));
    }
    @PostMapping("/checkup/oral") @PreAuthorize("@ss.hasPermission('childhealth:oral:create')")
    public CommonResult<Long> oral(@Valid @RequestBody OralExamRequest request) {
        return success(service.recordOralExam(request));
    }
    @PutMapping("/checkup/{id}/oral") @PreAuthorize("@ss.hasPermission('childhealth:oral:create')")
    public CommonResult<Long> oralByExam(@PathVariable Long id, @RequestBody OralExamRequest request) {
        request.setExamId(id); return success(service.recordOralExam(request));
    }
    @PostMapping("/checkup/{id}/lab-report") @PreAuthorize("@ss.hasPermission('childhealth:checkup:update')")
    public CommonResult<Long> labReport(@PathVariable Long id, @Valid @RequestBody LabReportRequest request) {
        return success(service.recordLabReport(id, request));
    }
    @GetMapping("/checkup/{id}/lab-report") @PreAuthorize("@ss.hasPermission('childhealth:checkup:query')")
    public CommonResult<List<LabReportResponse>> labReports(@PathVariable Long id) {
        return success(service.getLabReports(id));
    }
    @PostMapping("/checkup/{id}/medical-record") @PreAuthorize("@ss.hasPermission('childhealth:checkup:update')")
    public CommonResult<Long> generateMedicalRecord(@PathVariable Long id,
            @Valid @RequestBody MedicalRecordGenerateRequest request) {
        return success(service.generateMedicalRecord(id, request));
    }
    @GetMapping("/checkup/{id}/medical-record")
    @PreAuthorize("@ss.hasPermission('childhealth:checkup:query')")
    public CommonResult<MedicalRecordResponse> medicalRecord(@PathVariable Long id) {
        return success(service.getMedicalRecord(id));
    }
    @GetMapping("/checkup/{id}/flow") @PreAuthorize("@ss.hasPermission('childhealth:checkup:query')")
    public CommonResult<CheckupFlowResponse> checkupFlow(@PathVariable Long id) {
        return success(service.getCheckupFlow(id));
    }
    @PostMapping("/checkup/{id}/kindergarten-archive")
    @PreAuthorize("@ss.hasPermission('childhealth:checkup:update')")
    public CommonResult<Long> archiveKindergartenExam(@PathVariable Long id,
            @Valid @RequestBody KindergartenArchiveRequest request) {
        return success(service.archiveKindergartenExam(id, request));
    }
    @GetMapping("/checkup/{id}/kindergarten-archive")
    @PreAuthorize("@ss.hasPermission('childhealth:checkup:query')")
    public CommonResult<KindergartenArchiveResponse> kindergartenExam(@PathVariable Long id) {
        return success(service.getKindergartenExam(id));
    }
    @PostMapping("/child/{id}/external-report")
    @PreAuthorize("@ss.hasPermission('childhealth:checkup:update')")
    public CommonResult<Long> archiveExternalReport(@PathVariable Long id,
            @Valid @RequestBody ExternalReportRequest request) {
        return success(service.archiveExternalReport(id, request));
    }
    @GetMapping("/child/{id}/external-report")
    @PreAuthorize("@ss.hasPermission('childhealth:checkup:query')")
    public CommonResult<List<ExternalReportResponse>> externalReports(@PathVariable Long id) {
        return success(service.getExternalReports(id));
    }
    @PostMapping("/case") @PreAuthorize("@ss.hasPermission('childhealth:case:create')")
    public CommonResult<Long> openCase(@Valid @RequestBody CaseCreateRequest request) {
        return success(service.openCase(request));
    }
    @GetMapping("/case/{id}") @PreAuthorize("@ss.hasPermission('childhealth:case:query')")
    public CommonResult<CaseResponse> getCase(@PathVariable Long id) { return success(service.getCase(id)); }
    @PostMapping("/case/list") @PreAuthorize("@ss.hasPermission('childhealth:case:query')")
    public CommonResult<List<CaseResponse>> cases(@RequestParam(required=false) Long childId,@RequestParam(required=false) Integer status) { return success(service.getCases(childId,status)); }
    @PostMapping("/case/statistics") @PreAuthorize("@ss.hasPermission('childhealth:case:query')")
    public CommonResult<Map<String,Object>> caseStatistics() { return success(service.getCaseStatistics()); }
    @GetMapping("/case/alert") @PreAuthorize("@ss.hasPermission('childhealth:case:query')")
    public CommonResult<List<CaseAlertResponse>> caseAlerts(@RequestParam(required=false) Long childId,
            @RequestParam(required=false) Boolean handled) {
        return success(service.getCaseAlerts(childId, handled));
    }
    @PostMapping("/case/alert/{id}/open") @PreAuthorize("@ss.hasPermission('childhealth:case:create')")
    public CommonResult<Long> openCaseFromAlert(@PathVariable Long id,
            @Valid @RequestBody CaseAlertOpenRequest request) {
        return success(service.openCaseFromAlert(id, request));
    }
    @GetMapping("/case/{id}/card") @PreAuthorize("@ss.hasPermission('childhealth:case:query')")
    public CommonResult<CaseCardResponse> caseCard(@PathVariable Long id) {
        return success(service.getCaseCard(id));
    }
    @PostMapping("/case/{id}/recovery-assessment")
    @PreAuthorize("@ss.hasPermission('childhealth:case:update')")
    public CommonResult<Long> assessCaseRecovery(@PathVariable Long id,
            @Valid @RequestBody CaseRecoveryAssessmentRequest request) {
        return success(service.assessCaseRecovery(id, request));
    }
    @GetMapping("/case/{id}/recovery-assessment")
    @PreAuthorize("@ss.hasPermission('childhealth:case:query')")
    public CommonResult<List<CaseRecoveryAssessmentResponse>> caseRecoveryAssessments(@PathVariable Long id) {
        return success(service.getCaseRecoveryAssessments(id));
    }
    @GetMapping("/case/high-risk/warning") @PreAuthorize("@ss.hasPermission('childhealth:case:query')")
    public CommonResult<List<HighRiskWarningResponse>> highRiskWarnings(
            @RequestParam(required=false) Boolean registered, @RequestParam(required=false) Boolean followed) {
        return success(service.getHighRiskWarnings(registered, followed));
    }
    @PutMapping("/case/{id}") @PreAuthorize("@ss.hasPermission('childhealth:case:update')")
    public CommonResult<Boolean> updateCase(@PathVariable Long id, @RequestBody CaseUpdateRequest request) {
        service.updateCase(id, request); return success(true);
    }
    @PostMapping("/case/{id}/discharge") @PreAuthorize("@ss.hasPermission('childhealth:case:update')")
    public CommonResult<Boolean> dischargeCase(@PathVariable Long id) {
        service.dischargeCase(id); return success(true);
    }
    @PostMapping("/follow-up") @PreAuthorize("@ss.hasPermission('childhealth:followup:create')")
    public CommonResult<Long> followUp(@Valid @RequestBody FollowUpCreateRequest request) {
        return success(service.addFollowUp(request));
    }
    @GetMapping("/follow-up/{id}") @PreAuthorize("@ss.hasPermission('childhealth:followup:query')")
    public CommonResult<FollowUpResponse> getFollowUp(@PathVariable Long id) { return success(service.getFollowUp(id)); }
    @PostMapping("/case/follow/list") @PreAuthorize("@ss.hasPermission('childhealth:followup:query')")
    public CommonResult<List<FollowUpResponse>> followUps(@RequestParam(required=false) Long childId,@RequestParam(required=false) Long caseId) { return success(service.getFollowUps(childId,caseId)); }
    @GetMapping("/case/follow/timeline/{childId}") @PreAuthorize("@ss.hasPermission('childhealth:followup:query')")
    public CommonResult<List<FollowUpResponse>> followTimeline(@PathVariable Long childId) { return success(service.getFollowUps(childId,null)); }
    @GetMapping("/case/follow/statistics") @PreAuthorize("@ss.hasPermission('childhealth:followup:query')")
    public CommonResult<Map<String,Object>> followStatistics() { return success(service.getFollowUpStatistics()); }
    @PutMapping("/follow-up/{id}") @PreAuthorize("@ss.hasPermission('childhealth:followup:update')")
    public CommonResult<Boolean> updateFollowUp(@PathVariable Long id, @Valid @RequestBody FollowUpCreateRequest request) {
        service.updateFollowUp(id, request); return success(true);
    }
    @PostMapping("/follow-up/batch") @PreAuthorize("@ss.hasPermission('childhealth:followup:create')")
    public CommonResult<List<Long>> batchFollowUp(@Valid @RequestBody FollowUpBatchRequest request) {
        return success(service.addFollowUps(request));
    }
    @PostMapping("/case/{id}/follow-record") @PreAuthorize("@ss.hasPermission('childhealth:followup:create')")
    public CommonResult<Long> caseFollowUp(@PathVariable Long id, @Valid @RequestBody FollowUpCreateRequest request) {
        request.setCaseId(id); return success(service.addFollowUp(request));
    }
    @PostMapping("/screening-batch") @PreAuthorize("@ss.hasPermission('childhealth:screening:create')")
    public CommonResult<Long> createBatch(@Valid @RequestBody ScreeningBatchCreateRequest request) {
        return success(service.createScreeningBatch(request));
    }
    @GetMapping("/screening-batch/{id}") @PreAuthorize("@ss.hasPermission('childhealth:screening:query')")
    public CommonResult<ScreeningBatchResponse> batch(@PathVariable Long id) { return success(service.getScreeningBatch(id)); }
    @GetMapping("/screening-batch/list") @PreAuthorize("@ss.hasPermission('childhealth:screening:query')")
    public CommonResult<List<ScreeningBatchResponse>> batches(@RequestParam(required=false) Integer status) { return success(service.getScreeningBatches(status)); }
    @PutMapping("/screening-batch/{id}/status/{status}") @PreAuthorize("@ss.hasPermission('childhealth:screening:update')")
    public CommonResult<Boolean> batchStatus(@PathVariable Long id,@PathVariable Integer status) { service.updateScreeningBatchStatus(id,status);return success(true); }
    @GetMapping("/screening-batch/{id}/statistics") @PreAuthorize("@ss.hasPermission('childhealth:screening:query')")
    public CommonResult<Map<String,Object>> batchStatistics(@PathVariable Long id) { return success(service.getScreeningBatchStatistics(id)); }
    @PostMapping("/screening-record") @PreAuthorize("@ss.hasPermission('childhealth:screening:record')")
    public CommonResult<Long> record(@Valid @RequestBody ScreeningRecordCreateRequest request) {
        return success(service.recordScreening(request));
    }
    @PostMapping("/screening-record/scan/{qrCode}") @PreAuthorize("@ss.hasPermission('childhealth:screening:record')")
    public CommonResult<Long> scanScreening(@PathVariable String qrCode,@RequestParam Long batchId) { return success(service.scanCreateScreening(qrCode,batchId)); }
    @PostMapping("/screening-record/data") @PreAuthorize("@ss.hasPermission('childhealth:screening:record')")
    public CommonResult<Long> screeningData(@Valid @RequestBody ScreeningRecordCreateRequest request) { return success(service.recordScreening(request)); }
    @PostMapping("/screening-record/{id}/submit") @PreAuthorize("@ss.hasPermission('childhealth:screening:record')")
    public CommonResult<Boolean> submitScreeningRecord(@PathVariable Long id) {
        service.submitScreeningRecord(id); return success(true);
    }
    @GetMapping("/screening-record/{id}") @PreAuthorize("@ss.hasPermission('childhealth:screening:query')")
    public CommonResult<ScreeningRecordResponse> screeningRecord(@PathVariable Long id) { return success(service.getScreeningRecord(id)); }
    @PostMapping("/screening/audit") @PreAuthorize("@ss.hasPermission('childhealth:screening:audit')")
    public CommonResult<Boolean> audit(@Valid @RequestBody ScreeningAuditRequest request) {
        service.auditScreening(request); return success(true);
    }
    @PostMapping("/screening/batch-audit") @PreAuthorize("@ss.hasPermission('childhealth:screening:audit')")
    public CommonResult<Boolean> batchAudit(@Valid @RequestBody ScreeningBatchAuditRequest request) {
        service.batchAuditScreening(request); return success(true);
    }
    @GetMapping("/screening/positive") @PreAuthorize("@ss.hasPermission('childhealth:screening:query')")
    public CommonResult<List<ScreeningRecordResponse>> positive(@RequestParam(required = false) Long batchId) {
        return success(service.getPositiveScreenings(batchId));
    }
    @PostMapping("/screening/recheck") @PreAuthorize("@ss.hasPermission('childhealth:screening:recheck')")
    public CommonResult<Boolean> recheck(@Valid @RequestBody RecheckRequest request) {
        service.recheckScreening(request); return success(true);
    }
    @PostMapping("/screening/qrcode/batch") @PreAuthorize("@ss.hasPermission('childhealth:screening:update')")
    public CommonResult<List<ScreeningQrResponse>> screeningQrs(@Valid @RequestBody ScreeningQrBatchRequest request) {
        return success(service.generateScreeningQrs(request));
    }
    @PostMapping("/screening/qrcode/printed") @PreAuthorize("@ss.hasPermission('childhealth:screening:update')")
    public CommonResult<Boolean> markScreeningQrsPrinted(@RequestBody List<Long> ids) {
        service.markScreeningQrsPrinted(ids); return success(true);
    }
    @PostMapping("/screening/recheck/{id}/notify") @PreAuthorize("@ss.hasPermission('childhealth:screening:recheck')")
    public CommonResult<Long> notifyRecheck(@PathVariable Long id, @Valid @RequestBody RecheckNotifyRequest request) {
        return success(service.notifyRecheck(id, request));
    }
    @GetMapping("/screening/recheck/timeline/{studentId}")
    @PreAuthorize("@ss.hasPermission('childhealth:screening:query')")
    public CommonResult<List<RecheckTimelineResponse>> recheckTimeline(@PathVariable Long studentId) {
        return success(service.getRecheckTimeline(studentId));
    }
    @PostMapping("/screening/referral") @PreAuthorize("@ss.hasPermission('childhealth:screening:update')")
    public CommonResult<Long> createReferral(@Valid @RequestBody ReferralCreateRequest request) {
        return success(service.createReferral(request));
    }
    @PutMapping("/screening/referral/{id}/receive") @PreAuthorize("@ss.hasPermission('childhealth:screening:update')")
    public CommonResult<Boolean> receiveReferral(@PathVariable Long id) {
        service.receiveReferral(id); return success(true);
    }
    @PutMapping("/screening/referral/{id}/complete") @PreAuthorize("@ss.hasPermission('childhealth:screening:update')")
    public CommonResult<Boolean> completeReferral(@PathVariable Long id, @RequestParam String feedbackContent) {
        service.completeReferral(id, feedbackContent); return success(true);
    }
    @GetMapping("/screening/referral") @PreAuthorize("@ss.hasPermission('childhealth:screening:query')")
    public CommonResult<List<ReferralResponse>> referrals(@RequestParam(required=false) Long studentId,
            @RequestParam(required=false) Integer status) {
        return success(service.getReferrals(studentId, status));
    }
    @PostMapping("/transfer") @PreAuthorize("@ss.hasPermission('childhealth:child:update')")
    public CommonResult<Long> createTransfer(@Valid @RequestBody TransferCreateRequest request) {
        return success(service.createTransfer(request));
    }
    @PutMapping("/transfer/{id}/complete") @PreAuthorize("@ss.hasPermission('childhealth:child:update')")
    public CommonResult<Boolean> completeTransfer(@PathVariable Long id, @RequestParam String feedbackContent) {
        service.completeTransfer(id, feedbackContent); return success(true);
    }
    @GetMapping("/transfer") @PreAuthorize("@ss.hasPermission('childhealth:child:query')")
    public CommonResult<List<TransferResponse>> transfers(@RequestParam(required=false) Long childId,
            @RequestParam(required=false) Integer type, @RequestParam(required=false) Integer status) {
        return success(service.getTransfers(childId, type, status));
    }

    @PostMapping("/abnormal/detect") @PreAuthorize("@ss.hasPermission('childhealth:checkup:update')")
    public CommonResult<AbnormalDetectResponse> detectAbnormal(@RequestParam Long examId) {
        return success(service.detectAbnormal(examId));
    }

    @PostMapping("/medical-record/auto-generate") @PreAuthorize("@ss.hasPermission('childhealth:checkup:update')")
    public CommonResult<Long> autoGenerateMedicalRecord(@RequestParam Long examId) {
        return success(service.autoGenerateMedicalRecord(examId));
    }

    @PostMapping("/external/lis/receive") @PreAuthorize("@ss.hasPermission('childhealth:checkup:update')")
    public CommonResult<Long> receiveLisData(@Valid @RequestBody List<LisDataRequest> data) {
        return success(service.receiveLisData(data));
    }

    @PostMapping("/external/pacs/receive") @PreAuthorize("@ss.hasPermission('childhealth:checkup:update')")
    public CommonResult<Long> receivePacsData(@Valid @RequestBody PacsDataRequest data) {
        return success(service.receivePacsData(data));
    }
}

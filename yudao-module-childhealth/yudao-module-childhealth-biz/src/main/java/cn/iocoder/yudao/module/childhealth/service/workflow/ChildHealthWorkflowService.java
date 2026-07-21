package cn.iocoder.yudao.module.childhealth.service.workflow;

import cn.iocoder.yudao.module.childhealth.api.workflow.dto.ChildHealthWorkflowDTO.*;

import java.util.List;
import java.util.Map;

public interface ChildHealthWorkflowService {
    AbnormalDetectResponse detectAbnormal(Long examId);
    Long createChild(ChildCreateRequest request);
    ChildResponse getChild(Long id);
    void updateChild(Long id, ChildUpdateRequest request);
    List<ChildResponse> searchChildren(String keyword, String status);
    String generateChildQrCode(Long id);
    List<String> generateChildQrCodes(List<Long> ids);
    Long createCheckup(CheckupCreateRequest request);
    CheckupResponse getCheckup(Long id);
    void updateCheckup(Long id, CheckupCreateRequest request);
    List<CheckupResponse> getAbnormalCheckups();
    void submitCheckup(Long id);
    void reviewCheckup(Long id, Long reviewerId, boolean approved);
    void batchReviewCheckup(List<Long> ids, Long reviewerId, boolean approved);
    Long savePhysicalExam(Long examId, PhysicalExamRequest request);
    Long recordEyeExam(EyeExamRequest request);
    Long recordHearingExam(HearingExamRequest request);
    Long recordOralExam(OralExamRequest request);
    Long recordLabReport(Long examId, LabReportRequest request);
    List<LabReportResponse> getLabReports(Long examId);
    Long generateMedicalRecord(Long examId, MedicalRecordGenerateRequest request);
    MedicalRecordResponse getMedicalRecord(Long checkupId);
    CheckupFlowResponse getCheckupFlow(Long checkupId);
    Long archiveKindergartenExam(Long examId, KindergartenArchiveRequest request);
    KindergartenArchiveResponse getKindergartenExam(Long examId);
    Long archiveExternalReport(Long childId, ExternalReportRequest request);
    List<ExternalReportResponse> getExternalReports(Long childId);
    Long openCase(CaseCreateRequest request);
    /**
     * P2-18 需求36-39：建档时自动建立高危儿专案
     * 用于在 autoDetectHighRisk 完成后自动建 HIGH_RISK 专案
     * @return 专案ID，若已存在同类型进行中专案则返回 null
     */
    Long autoOpenHighRiskCase(Long childId, String initialDiagnosis, Integer caseLevel);
    CaseResponse getCase(Long id);
    List<CaseResponse> getCases(Long childId, Integer status);
    Map<String, Object> getCaseStatistics();
    void updateCase(Long id, CaseUpdateRequest request);
    void dischargeCase(Long id);
    List<CaseAlertResponse> getCaseAlerts(Long childId, Boolean handled);
    Long openCaseFromAlert(Long alertId, CaseAlertOpenRequest request);
    CaseCardResponse getCaseCard(Long caseId);
    Long assessCaseRecovery(Long caseId, CaseRecoveryAssessmentRequest request);
    List<CaseRecoveryAssessmentResponse> getCaseRecoveryAssessments(Long caseId);
    List<HighRiskWarningResponse> getHighRiskWarnings(Boolean registered, Boolean followed);
    Long addFollowUp(FollowUpCreateRequest request);
    FollowUpResponse getFollowUp(Long id);
    List<FollowUpResponse> getFollowUps(Long childId, Long caseId);
    Map<String, Object> getFollowUpStatistics();
    void updateFollowUp(Long id, FollowUpCreateRequest request);
    List<Long> addFollowUps(FollowUpBatchRequest request);
    Long createScreeningBatch(ScreeningBatchCreateRequest request);
    ScreeningBatchResponse getScreeningBatch(Long id);
    List<ScreeningBatchResponse> getScreeningBatches(Integer status);
    void updateScreeningBatchStatus(Long id, Integer status);
    Map<String, Object> getScreeningBatchStatistics(Long id);
    Long recordScreening(ScreeningRecordCreateRequest request);
    Long scanCreateScreening(String qrCode, Long batchId);
    void submitScreeningRecord(Long recordId);
    ScreeningRecordResponse getScreeningRecord(Long id);
    void auditScreening(ScreeningAuditRequest request);
    void batchAuditScreening(ScreeningBatchAuditRequest request);
    List<ScreeningRecordResponse> getPositiveScreenings(Long batchId);
    void recheckScreening(RecheckRequest request);
    List<ScreeningQrResponse> generateScreeningQrs(ScreeningQrBatchRequest request);
    void markScreeningQrsPrinted(List<Long> ids);
    Long notifyRecheck(Long positiveId, RecheckNotifyRequest request);
    List<RecheckTimelineResponse> getRecheckTimeline(Long studentId);
    Long createReferral(ReferralCreateRequest request);
    void receiveReferral(Long id);
    void completeReferral(Long id, String feedbackContent);
    List<ReferralResponse> getReferrals(Long studentId, Integer status);
    Long createTransfer(TransferCreateRequest request);
    void completeTransfer(Long id, String feedbackContent);
    List<TransferResponse> getTransfers(Long childId, Integer type, Integer status);
    Long autoGenerateMedicalRecord(Long examId);
    Long receiveLisData(List<LisDataRequest> data);
    Long receivePacsData(PacsDataRequest data);
}

package cn.iocoder.yudao.module.childhealth.api.workflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public final class ChildHealthWorkflowDTO {
    private ChildHealthWorkflowDTO() {}

    @Data
    public static class ChildCreateRequest {
        @NotBlank private String childCode;
        @NotBlank private String childName;
        @NotBlank private String gender;
        @NotNull private LocalDate birthDate;
        private String idCard;
        private String parentName;
        private String parentPhone;
        private String highRiskType;
    }

    @Data
    public static class ChildResponse extends ChildCreateRequest {
        private Long id;
        private String qrCode;
        private String status;
    }

    @Data
    public static class ChildUpdateRequest {
        @NotBlank private String childName;
        @NotBlank private String gender;
        @NotNull private LocalDate birthDate;
        private String idCard;
        private String parentName;
        private String parentPhone;
        private String highRiskType;
        private String status;
    }

    @Data
    public static class CheckupCreateRequest {
        @NotNull private Long childId;
        @NotNull private LocalDate checkupDate;
        @NotNull private Integer ageMonths;
        @NotBlank private String checkupType;
        private BigDecimal height;
        private BigDecimal weight;
        private String nutritionStatus;
        private String abnormalItems;
        private Long doctorId;
    }

    @Data
    public static class CheckupResponse extends CheckupCreateRequest {
        private Long id;
        private Boolean isAbnormal;
        private String reviewStatus;
        private LocalDateTime reviewedAt;
        private Long reviewedBy;
    }

    @Data public static class CheckupBatchReviewRequest {
        @NotNull private List<Long> ids;
        @NotNull private Long reviewerId;
        @NotNull private Boolean approved;
    }

    @Data
    public static class PhysicalExamRequest {
        private BigDecimal height;
        private BigDecimal weight;
        private BigDecimal headCircumference;
        private BigDecimal chestCircumference;
        private BigDecimal heightSd;
        private BigDecimal weightSd;
        private String growthAssessment;
    }

    @Data
    public static class LabReportRequest {
        @NotNull private Integer reportType;
        private String reportCode;
        @NotBlank private String reportName;
        private LocalDateTime reportDate;
        @NotBlank private String reportContent;
        private String reportUrl;
        @NotNull private Integer source;
        private Boolean isAbnormal;
        private String abnormalItems;
    }

    @Data
    public static class LabReportResponse extends LabReportRequest {
        private Long id;
        private Long examId;
        private LocalDateTime createTime;
    }

    @Data
    public static class KindergartenArchiveRequest {
        @NotBlank private String kindergartenName;
        private String examConclusion;
        @NotNull private Boolean isQualified;
        private Long doctorId;
    }

    @Data
    public static class KindergartenArchiveResponse extends KindergartenArchiveRequest {
        private Long id;
        private Long examId;
        private Long childId;
        private LocalDate examDate;
        private String recordContent;
        private LocalDateTime createTime;
    }

    @Data
    public static class ExternalReportRequest {
        @NotBlank private String reportType;
        private LocalDate reportDate;
        private String reportHospital;
        private String reportContent;
        private String fileUrl;
    }

    @Data
    public static class ExternalReportResponse extends ExternalReportRequest {
        private Long id;
        private Long childId;
        private LocalDateTime uploadTime;
        private LocalDateTime createTime;
    }

    @Data
    public static class MedicalRecordGenerateRequest {
        private String chiefComplaint;
        private String presentIllness;
        private String pastHistory;
        private Long doctorId;
    }

    @Data
    public static class HearingExamRequest {
        @NotNull private Long examId;
        private String leftEarResult;
        private String rightEarResult;
        @NotBlank private String hearingScreening;
        private String earAppearance;
        private String externalAuditory;
        private String tympanicMembrane;
        private String diagnosis;
    }

    @Data
    public static class EyeExamRequest {
        @NotNull private Long examId;
        private String leftVision;
        private String rightVision;
        private String leftDiopter;
        private String rightDiopter;
        private String eyePosition;
        private String eyeAppearance;
        private String fundusExam;
        private String diagnosis;
    }

    @Data
    public static class OralExamRequest {
        @NotNull private Long examId;
        private Integer toothCount;
        private Integer cariesCount;
        private String cariesType;
        private String gumCondition;
        private String oralHygiene;
        private String malocclusion;
        private String diagnosis;
    }

    @Data
    public static class CaseCreateRequest {
        @NotNull private Long childId;
        @NotNull private Long caseTypeId;
        private Integer caseSource;
        private String initialDiagnosis;
        private Integer caseLevel;
        private Long responsibleDoctor;
    }

    @Data
    public static class CaseResponse extends CaseCreateRequest {
        private Long id;
        private String caseNo;
        private LocalDate registrationDate;
        private Integer caseStatus;
    }

    @Data public static class CaseUpdateRequest {
        private Integer caseSource;
        private String initialDiagnosis;
        private Integer caseLevel;
        private Long responsibleDoctor;
    }

    @Data
    public static class CaseAlertResponse {
        private Long id;
        private Long examId;
        private Long childId;
        private String alertType;
        private String alertContent;
        private String suggestCaseType;
        private Boolean isHandled;
        private String handleResult;
        private LocalDateTime createTime;
    }

    @Data
    public static class CaseAlertOpenRequest {
        @NotNull private Long caseTypeId;
        private String initialDiagnosis;
        private Integer caseLevel;
        private Long responsibleDoctor;
    }

    @Data
    public static class CaseCardResponse {
        private Long id;
        private Long caseId;
        private String cardContent;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
    }

    @Data
    public static class CaseRecoveryAssessmentRequest {
        private LocalDate assessmentDate;
        @NotBlank private String assessmentContent;
        @NotNull private Integer recoveryLevel;
        @NotNull private Boolean isQualified;
        private Long doctorId;
        private String dischargeReason;
    }

    @Data
    public static class CaseRecoveryAssessmentResponse extends CaseRecoveryAssessmentRequest {
        private Long id;
        private Long caseId;
        private LocalDateTime createTime;
    }

    @Data
    public static class HighRiskWarningResponse {
        private Long id;
        private Long childId;
        private String riskFactors;
        private Boolean isRegistered;
        private LocalDate registerDate;
        private Boolean isFollowed;
        private Integer alertStatus;
        private LocalDateTime createTime;
    }

    @Data
    public static class FollowUpCreateRequest {
        @NotNull private Long childId;
        @NotNull private Long caseId;
        private Long planId;
        @NotNull private LocalDate followDate;
        @NotNull private Integer followType;
        private String followContent;
        private String healthStatus;
        private String measureData;
        private String guidance;
        private String nextPlan;
        private Long followDoctor;
    }

    @Data public static class FollowUpResponse extends FollowUpCreateRequest {
        private Long id;
        private LocalDateTime createTime;
    }

    @Data public static class FollowUpBatchRequest {
        @NotNull private List<FollowUpCreateRequest> records;
    }

    @Data
    public static class ScreeningBatchCreateRequest {
        @NotBlank private String batchNo;
        @NotBlank private String batchName;
        @NotNull private Long yearId;
        private Long schoolId;
        @NotNull private LocalDate startDate;
        @NotNull private LocalDate endDate;
        private Integer targetCount;
    }

    @Data public static class ScreeningBatchResponse extends ScreeningBatchCreateRequest {
        private Long id;
        private Integer actualCount;
        private Integer batchStatus;
        private LocalDateTime createdAt;
    }

    @Data
    public static class ScreeningRecordCreateRequest {
        @NotNull private Long batchId;
        @NotNull private Long studentId;
        @NotNull private LocalDate screeningDate;
        private String screeningType;
        private Long deptId;
        private Long checkerId;
        private BigDecimal height;
        private BigDecimal weight;
        private String bodyShapeResult;
        private String visionResult;
        private String oralResult;
        private String scoliosisResult;
        private String psychologicalResult;
    }

    @Data
    public static class ScreeningRecordResponse extends ScreeningRecordCreateRequest {
        private Long id;
        private BigDecimal bmi;
        private String overallResult;
        private Integer abnormalCount;
        private String reviewStatus;
        private Boolean rescreenNeeded;
    }

    @Data
    public static class ScreeningAuditRequest {
        @NotNull private Long recordId;
        @NotNull private Boolean approved;
        private Long auditDoctor;
    }

    @Data public static class ScreeningBatchAuditRequest {
        @NotNull private List<Long> recordIds;
        @NotNull private Boolean approved;
        private Long auditDoctor;
    }

    @Data
    public static class RecheckRequest {
        @NotNull private Long positiveId;
        @NotNull private LocalDate recheckDate;
        @NotBlank private String visionResult;
        @NotBlank private String oralResult;
        @NotBlank private String scoliosisResult;
        @NotBlank private String psychologicalResult;
        @NotBlank private String bodyShapeResult;
        private String recheckConclusion;
        private String followPlan;
        private Long doctorId;
    }

    @Data
    public static class ScreeningQrBatchRequest {
        @NotNull private Long batchId;
        @NotEmpty private List<Long> studentIds;
    }

    @Data
    public static class ScreeningQrResponse {
        private Long id;
        private String qrcodeNo;
        private Long batchId;
        private Long studentId;
        private String qrcodeUrl;
        private String qrcodeContent;
        private Integer printStatus;
        private LocalDateTime printTime;
        private Boolean isUsed;
    }

    @Data
    public static class RecheckNotifyRequest {
        @NotBlank private String noticeContent;
    }

    @Data
    public static class RecheckTimelineResponse {
        private Long positiveId;
        private Long studentId;
        private Integer recheckStatus;
        private String positiveItems;
        private Long recheckId;
        private LocalDate recheckDate;
        private String recheckResult;
        private Boolean isStillPositive;
        private String recheckConclusion;
        private String followPlan;
    }

    @Data
    public static class ReferralCreateRequest {
        @NotNull private Long positiveId;
        @NotBlank private String referralReason;
        private String referralItems;
        @NotBlank private String targetHospital;
        private String targetDept;
        private String targetDoctor;
    }

    @Data
    public static class ReferralResponse extends ReferralCreateRequest {
        private Long id;
        private String referralNo;
        private Long studentId;
        private Integer referralStatus;
        private String feedbackContent;
        private LocalDateTime createTime;
    }

    @Data
    public static class TransferCreateRequest {
        @NotNull private Long childId;
        @NotNull private Integer transferType;
        @NotNull private LocalDate transferDate;
        private String sourceHospital;
        private String targetHospital;
        @NotBlank private String transferReason;
        private String healthSummary;
        private String caseList;
        private String contactPerson;
        private String contactMobile;
    }

    @Data
    public static class TransferResponse extends TransferCreateRequest {
        private Long id;
        private String transferNo;
        private String feedbackContent;
        private Integer status;
        private LocalDateTime createTime;
    }

    @Data
    public static class MedicalRecordResponse {
        private CheckupResponse checkup;
        private Long medicalRecordId;
        private Long templateId;
        private Long eyeExamId;
        private Long hearingExamId;
        private Long oralExamId;
        private String chiefComplaint;
        private String presentIllness;
        private String pastHistory;
        private String physicalExam;
        private String specialtyExam;
        private String labExam;
        private String diagnosis;
        private String healthGuidance;
        private String nextVisitPlan;
        private Long doctorId;
        private LocalDateTime recordTime;
    }

    @Data
    public static class CheckupFlowResponse {
        private Long examId;
        private String status;
        private Boolean physicalCompleted;
        private Boolean eyeCompleted;
        private Boolean hearingCompleted;
        private Boolean oralCompleted;
        private Boolean labCompleted;
        private Boolean medicalRecordGenerated;
        private Boolean abnormal;
        private String nextAction;
    }

    @Data
    public static class AbnormalDetectRequest {
        @NotNull private Long examId;
        private Map<String, BigDecimal> values;
    }

    @Data
    public static class AbnormalDetectResponse {
        private Long examId;
        private Boolean hasAbnormal;
        private List<String> abnormalItems;
        private List<String> matchedRules;
    }

    @Data
    public static class LisDataRequest {
        @NotNull private Long examId;
        @NotBlank private String labCode;
        @NotBlank private String labName;
        private String result;
        private String unit;
        private String referenceRange;
        private Boolean isAbnormal;
        private LocalDateTime reportTime;
        private String labDepartment;
    }

    @Data
    public static class PacsDataRequest {
        @NotNull private Long examId;
        @NotBlank private String imagingType;
        @NotBlank private String imageUrl;
        private String reportContent;
        private String diagnosis;
        private LocalDateTime examDate;
        private String imagingDepartment;
    }
}

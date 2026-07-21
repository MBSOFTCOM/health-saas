package cn.iocoder.yudao.module.childhealth.api.scale.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 量表评估完整DTO
 */
public final class ScaleDTO {
    private ScaleDTO() {}

    /**
     * 量表配置创建请求
     */
    @Data
    public static class ScaleConfigCreateRequest {
        @NotBlank private String scaleCode;
        @NotBlank private String scaleName;
        private String scaleType;
        private Integer applicableAgeMin;
        private Integer applicableAgeMax;
        private Integer totalScore;
        private String scoringRule; // JSON配置
        private String riskLevelRule; // JSON配置
        private BigDecimal abnormalThreshold;
        private String description;
    }

    /**
     * 量表配置更新请求
     */
    @Data
    public static class ScaleConfigUpdateRequest {
        @NotNull private Long id;
        private String scaleName;
        private String scaleType;
        private Integer applicableAgeMin;
        private Integer applicableAgeMax;
        private Integer totalScore;
        private String scoringRule;
        private String riskLevelRule;
        private BigDecimal abnormalThreshold;
        private String description;
        private Integer status;
    }

    /**
     * 量表配置响应
     */
    @Data
    public static class ScaleConfigResponse {
        private Long id;
        private String scaleCode;
        private String scaleName;
        private String scaleType;
        private Integer applicableAgeMin;
        private Integer applicableAgeMax;
        private Integer totalScore;
        private String scoringRule;
        private String riskLevelRule;
        private BigDecimal abnormalThreshold;
        private String description;
        private Integer status;
        private LocalDateTime createTime;
    }

    /**
     * 量表题目创建请求
     */
    @Data
    public static class ScaleQuestionCreateRequest {
        @NotNull private Long scaleId;
        @NotNull private Integer questionNo;
        @NotBlank private String questionContent;
        @NotNull private Integer questionType; // 1单选 2多选 3填空
        private String options; // JSON配置
        private String scoreRule; // JSON配置
        private Integer sortOrder;
    }

    /**
     * 量表题目批量创建请求
     */
    @Data
    public static class ScaleQuestionBatchCreateRequest {
        @NotNull private Long scaleId;
        @Valid @NotEmpty private List<ScaleQuestionCreateRequest> questions;
    }

    /**
     * 量表题目响应
     */
    @Data
    public static class ScaleQuestionResponse {
        private Long id;
        private Long scaleId;
        private Integer questionNo;
        private String questionContent;
        private Integer questionType;
        private String options;
        private String scoreRule;
        private Integer sortOrder;
        private LocalDateTime createTime;
    }

    /**
     * 量表评估提交请求(完整版)
     */
    @Data
    public static class ScaleAssessmentSubmitRequest {
        @NotNull private Long childId;
        @NotNull private Long scaleId;
        @NotNull private LocalDate assessmentDate;
        @NotNull private Integer assessorType; // 1医生 2家长
        private Long assessorId;
        @Valid @NotEmpty private List<AnswerItem> answers;
    }

    @Data
    public static class AnswerItem {
        @NotNull private Integer questionNo;
        @NotBlank private String answer;
        private BigDecimal questionScore;
    }

    /**
     * 量表评估记录响应(完整版)
     */
    @Data
    public static class ScaleAssessmentRecordResponse {
        private Long id;
        private String recordNo;
        private Long childId;
        private Long scaleId;
        private String scaleName;
        private LocalDate assessmentDate;
        private Integer assessorType;
        private Long assessorId;
        private String answers;
        private BigDecimal totalScore;
        private String dimensionScores; // JSON格式的各维度得分
        private Integer riskLevel; // 1正常 2可疑 3异常
        private Boolean isAbnormal;
        private String assessmentConclusion;
        private String suggestion;
        private LocalDateTime createTime;
    }

    /**
     * 评估报告生成请求
     */
    @Data
    public static class AssessmentReportGenerateRequest {
        @NotNull private Long assessmentId;
        private String reportType;
    }

    /**
     * 评估报告响应
     */
    @Data
    public static class AssessmentReportResponse {
        private Long id;
        private String reportNo;
        private Long childId;
        private Long assessmentId;
        private String reportType;
        private LocalDate reportDate;
        private String reportContent; // JSON格式
        private String reportUrl;
        private LocalDateTime createTime;
    }

    /**
     * 发育里程碑创建请求
     */
    @Data
    public static class DevelopmentMilestoneCreateRequest {
        @NotBlank private String milestoneCode;
        @NotBlank private String milestoneName;
        @NotBlank private String domain; // 运动/语言/认知/社交
        @NotNull private Integer ageMonth;
        private String description;
        private String assessmentMethod;
    }

    /**
     * 发育里程碑响应
     */
    @Data
    public static class DevelopmentMilestoneResponse {
        private Long id;
        private String milestoneCode;
        private String milestoneName;
        private String domain;
        private Integer ageMonth;
        private String description;
        private String assessmentMethod;
        private LocalDateTime createTime;
    }

    /**
     * 儿童发育评估创建请求
     */
    @Data
    public static class ChildDevelopmentAssessmentCreateRequest {
        @NotNull private Long childId;
        @NotNull private LocalDate assessmentDate;
        private Integer monthAge;
        private BigDecimal motorScore;
        private BigDecimal languageScore;
        private BigDecimal cognitiveScore;
        private BigDecimal socialScore;
        private String overallLevel;
        private Boolean isDelayed;
        private String delayedDomains; // JSON数组
        private Long doctorId;
    }

    /**
     * 儿童发育评估响应
     */
    @Data
    public static class ChildDevelopmentAssessmentResponse {
        private Long id;
        private Long childId;
        private LocalDate assessmentDate;
        private Integer monthAge;
        private BigDecimal motorScore;
        private BigDecimal languageScore;
        private BigDecimal cognitiveScore;
        private BigDecimal socialScore;
        private String overallLevel;
        private Boolean isDelayed;
        private String delayedDomains;
        private Long doctorId;
        private LocalDateTime createTime;
    }

    /**
     * 量表查询请求
     */
    @Data
    public static class ScaleQueryRequest {
        private String scaleType;
        private Integer status;
        private Integer applicableAge;
        private Integer pageNum;
        private Integer pageSize;
    }

    /**
     * 量表评估记录查询请求
     */
    @Data
    public static class ScaleAssessmentQueryRequest {
        private Long childId;
        private Long scaleId;
        private LocalDate assessmentDateStart;
        private LocalDate assessmentDateEnd;
        private Integer riskLevel;
        private Boolean isAbnormal;
        private Integer pageNum;
        private Integer pageSize;
    }
}
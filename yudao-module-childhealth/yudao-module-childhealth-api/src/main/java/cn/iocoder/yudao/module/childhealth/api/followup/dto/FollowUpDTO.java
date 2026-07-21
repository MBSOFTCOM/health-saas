package cn.iocoder.yudao.module.childhealth.api.followup.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 随访管理DTO
 */
public final class FollowUpDTO {
    private FollowUpDTO() {}

    /**
     * 随访记录创建请求
     */
    @Data
    public static class FollowRecordCreateRequest {
        private Long planId;
        @NotNull private Long caseId;
        @NotNull private Long childId;
        @NotNull private LocalDate followDate;
        @NotNull private Integer followType; // 1电话 2短信 3微信 4面诊
        @NotBlank private String followContent;
        private String healthStatus;
        private String measureData; // JSON格式的测量数据
        private String guidance;
        private String nextPlan;
        private Long followDoctor;
    }

    /**
     * 随访记录更新请求
     */
    @Data
    public static class FollowRecordUpdateRequest {
        @NotNull private Long id;
        private LocalDate followDate;
        private Integer followType;
        private String followContent;
        private String healthStatus;
        private String measureData;
        private String guidance;
        private String nextPlan;
    }

    /**
     * 随访记录响应
     */
    @Data
    public static class FollowRecordResponse {
        private Long id;
        private Long planId;
        private Long caseId;
        private Long childId;
        private LocalDate followDate;
        private Integer followType;
        private String followContent;
        private String healthStatus;
        private String measureData;
        private String guidance;
        private String nextPlan;
        private Long followDoctor;
        private LocalDateTime createTime;
    }

    /**
     * 随访任务创建请求
     */
    @Data
    public static class FollowTaskCreateRequest {
        @NotNull private Long caseId;
        @NotNull private Long childId;
        @NotNull private Integer taskType; // 1未按时体检 2心理异常 3高危 4专案
        @NotNull private LocalDate planDate;
        private Integer priority; // 1高 2中 3低
        @NotBlank private String taskContent;
        private Long assignDoctor;
    }

    /**
     * 随访任务响应
     */
    @Data
    public static class FollowTaskResponse {
        private Long id;
        private Long caseId;
        private Long childId;
        private Integer taskType;
        private Integer taskStatus; // 1待执行 2进行中 3已完成 4已取消
        private Integer priority;
        private LocalDate planDate;
        private String taskContent;
        private Long assignDoctor;
        private LocalDate executeDate;
        private LocalDateTime createTime;
    }

    /**
     * 随访任务查询请求
     */
    @Data
    public static class FollowTaskQueryRequest {
        private Long childId;
        private Long doctorId;
        private Integer taskType; // 1未按时体检 2心理异常 3高危 4专案
        private Integer taskStatus; // 1待执行 2进行中 3已完成 4已取消
        private Integer priority; // 1高 2中 3低
        private LocalDate planDateStart;
        private LocalDate planDateEnd;
        private Integer pageNum;
        private Integer pageSize;
    }

    /**
     * 随访任务统计响应
     */
    @Data
    public static class FollowTaskStatisticsResponse {
        private Integer totalTasks;
        private Integer pendingTasks;
        private Integer inProgressTasks;
        private Integer completedTasks;
        private Integer cancelledTasks;
        private Integer highPriorityCount;
        private Integer mediumPriorityCount;
        private Integer lowPriorityCount;
    }

    /**
     * 随访计划创建请求
     */
    @Data
    public static class FollowPlanCreateRequest {
        @NotNull private Long caseId;
        private Integer planType; // 1自动生成 2手工创建
        @NotNull private LocalDate planDate;
        @NotBlank private String followContent;
    }

    /**
     * 随访计划响应
     */
    @Data
    public static class FollowPlanResponse {
        private Long id;
        private Long caseId;
        private Integer planType;
        private LocalDate planDate;
        private String followContent;
        private Integer planStatus; // 1待执行 2已完成 3已取消
        private LocalDate executeDate;
        private LocalDateTime createTime;
    }

    /**
     * 催检规则创建请求
     */
    @Data
    public static class ReminderRuleCreateRequest {
        @NotBlank private String ruleCode;
        @NotBlank private String ruleName;
        private Integer examType;
        @NotBlank private String targetAge; // JSON数组
        private Integer remindAdvanceDays;
        @NotBlank private String remindFrequency;
        @NotBlank private String messageTemplate;
    }

    /**
     * 催检规则响应
     */
    @Data
    public static class ReminderRuleResponse {
        private Long id;
        private String ruleCode;
        private String ruleName;
        private Integer examType;
        private String targetAge;
        private Integer remindAdvanceDays;
        private String remindFrequency;
        private String messageTemplate;
        private Integer status;
        private LocalDateTime createTime;
    }

    /**
     * 催检记录查询请求
     */
    @Data
    public static class ReminderLogQueryRequest {
        private Long childId;
        private Long ruleId;
        private Integer examType;
        private Integer remindStatus; // 0待发送 1已发送 2已响应 3已忽略
        private LocalDate dueDateStart;
        private LocalDate dueDateEnd;
        private Integer pageNum;
        private Integer pageSize;
    }

    /**
     * 催检记录响应
     */
    @Data
    public static class ReminderLogResponse {
        private Long id;
        private Long childId;
        private Long ruleId;
        private Integer examType;
        private LocalDate dueDate;
        private LocalDate remindDate;
        private Integer remindStatus;
        private LocalDateTime responseTime;
        private Boolean isCompleted;
        private LocalDateTime createTime;
    }

    /**
     * 问卷配置创建请求
     */
    @Data
    public static class QuestionnaireConfigCreateRequest {
        @NotBlank private String questionnaireCode;
        @NotBlank private String questionnaireName;
        private String questionnaireType;
        private String applicableScene;
        @NotBlank private String questions; // JSON配置
        private String scoringRule; // JSON配置
    }

    /**
     * 问卷配置响应
     */
    @Data
    public static class QuestionnaireConfigResponse {
        private Long id;
        private String questionnaireCode;
        private String questionnaireName;
        private String questionnaireType;
        private String applicableScene;
        private String questions;
        private String scoringRule;
        private Integer status;
        private LocalDateTime createTime;
    }

    /**
     * 问卷填写提交请求
     */
    @Data
    public static class QuestionnaireAnswerSubmitRequest {
        @NotNull private Long questionnaireId;
        @NotNull private Long childId;
        @NotNull private Integer fillType; // 1医生 2家长
        private Long fillerId;
        @NotBlank private String answers; // JSON格式答案
        private Long associatedRecord; // 关联记录ID
    }

    /**
     * 问卷填写记录响应
     */
    @Data
    public static class QuestionnaireAnswerResponse {
        private Long id;
        private Long questionnaireId;
        private Long childId;
        private Integer fillType;
        private Long fillerId;
        private String answers;
        private LocalDateTime fillTime;
        private Long associatedRecord;
        private LocalDateTime createTime;
    }
}
package cn.iocoder.yudao.module.childhealth.api.management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public final class ChildHealthManagementDTO {
    private ChildHealthManagementDTO() {}

    @Data
    public static class AppointmentCreateRequest {
        @NotNull private Long childId;
        @NotNull private Integer examType;
        private Integer examMonthAge;
        @NotNull private LocalDate appointmentDate;
        private LocalTime appointmentTime;
        private Long deptId;
        private Long doctorId;
        private Integer source;
    }

    @Data
    public static class ScreeningPlanCreateRequest {
        @NotBlank private String planCode;
        @NotBlank private String planName;
        @NotNull private Long batchId;
        @NotBlank private String screeningItems;
        private String questionnaires;
        private String scales;
        private String targetSchools;
        private String targetGrades;
    }

    @Data
    public static class FollowTaskCreateRequest {
        @NotNull private Long childId;
        @NotNull private Integer taskType;
        private String taskSource;
        @NotBlank private String taskContent;
        private Integer priority;
        @NotNull private LocalDate planDate;
        private Long responsibleDoctor;
    }

    @Data
    public static class FollowPlanCreateRequest {
        private Long caseId;
        private Integer planType;
        @NotNull private LocalDate planDate;
        @NotBlank private String followContent;
    }

    @Data
    public static class FollowTaskResponse extends FollowTaskCreateRequest {
        private Long id;
        private String taskNo;
        private Integer taskStatus;
        private LocalDateTime completeTime;
        private LocalDateTime createTime;
    }
}

package cn.iocoder.yudao.module.childhealth.api.scale.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public final class ScaleAssessmentDTO {
    private ScaleAssessmentDTO() {}
    @Data public static class Answer { @NotNull private Integer questionNo; @NotBlank private String answer; }
    @Data public static class SubmitRequest {
        @NotNull private Long childId; @NotNull private Long scaleId;
        @NotNull private Integer assessorType; private Long assessorId;
        @Valid @NotEmpty private List<Answer> answers;
    }
    @Data public static class AssessmentResponse {
        private Long id; private String recordNo; private Long childId; private Long scaleId;
        private LocalDate assessmentDate; private Integer assessorType; private Long assessorId;
        private String answers; private BigDecimal totalScore; private String dimensionScores;
        private Integer riskLevel; private Boolean isAbnormal; private String assessmentConclusion; private String suggestion;
    }
    @Data public static class QuestionResponse {
        private Long id; private Long scaleId; private Integer questionNo; private String questionContent;
        private Integer questionType; private String options; private Integer sortOrder;
    }
    @Data public static class ReportRequest { @NotNull private Long assessmentId; }
}

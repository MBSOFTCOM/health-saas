package cn.iocoder.yudao.module.childhealth.api.ops.dto;
import jakarta.validation.constraints.*; import lombok.Data; import java.time.LocalDate; import java.time.LocalDateTime;
public final class ChildHealthOpsDTO {
 private ChildHealthOpsDTO() {}
 @Data public static class MessagePushRequest { private Long configId; @NotNull private Integer pushType; @NotNull private Integer pushChannel; private Integer targetType; @NotNull private Long targetId; private String mobile; private String openid; @NotBlank private String pushContent; }
 @Data public static class MessagePushResponse extends MessagePushRequest { private Long id; private LocalDateTime pushTime; private Integer pushStatus; private String errorMsg; private Boolean isRead; private LocalDateTime createTime; }
 @Data public static class ReminderRequest { @NotBlank private String ruleCode; @NotBlank private String ruleName; private Integer examType; @NotBlank private String targetAge; private Integer remindAdvanceDays; private String remindFrequency; private String messageTemplate; }
 @Data public static class QuestionnaireRequest { @NotBlank private String questionnaireCode; @NotBlank private String questionnaireName; private String questionnaireType; private String applicableScene; @NotBlank private String questions; private String scoringRule; }
 @Data public static class QuestionnaireAnswerRequest { @NotNull private Long questionnaireId; @NotNull private Long childId; @NotNull private Integer fillType; private Long fillerId; @NotBlank private String answers; private Long associatedRecord; }
 @Data public static class QuestionnaireAnswerResponse extends QuestionnaireAnswerRequest { private Long id; private LocalDateTime fillTime; private LocalDateTime createTime; }
 @Data public static class ReminderGenerateRequest { private LocalDate asOfDate; }
 @Data public static class ArticleRequest { @NotBlank private String articleCode; @NotBlank private String title; private String category; private String tags; private String coverUrl; private String summary; @NotBlank private String content; private String author; private Integer isTop; private Integer publishStatus; }
 @Data public static class ArticleResponse extends ArticleRequest { private Long id; private Integer viewCount; private LocalDateTime publishTime; private LocalDateTime createTime; }
}

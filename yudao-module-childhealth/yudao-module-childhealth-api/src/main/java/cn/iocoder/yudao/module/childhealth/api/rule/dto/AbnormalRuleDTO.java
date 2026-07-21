package cn.iocoder.yudao.module.childhealth.api.rule.dto;
import jakarta.validation.constraints.*; import lombok.Data; import java.math.BigDecimal; import java.time.LocalDateTime; import java.util.Map;
public final class AbnormalRuleDTO { private AbnormalRuleDTO() {}
 @Data public static class RuleSaveRequest { @NotBlank private String ruleCode; @NotBlank private String ruleName; @NotBlank private String checkItem; @NotBlank private String conditionLogic; @NotNull private Integer abnormalLevel; private Boolean autoCreateCase; private String caseType; private Integer status; }
 @Data public static class RuleResponse extends RuleSaveRequest { private Long id; private LocalDateTime createTime; }
 @Data public static class MatchRequest { private Long examId; @NotEmpty private Map<String, BigDecimal> values; }
}

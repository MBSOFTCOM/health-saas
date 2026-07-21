package cn.iocoder.yudao.module.childhealth.controller.admin.casemanagement.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

@Schema(description = "管理后台 - 体检催检规则新增/修改 Request VO")
@Data
public class ExamReminderRuleSaveReqVO {

    @Schema(description = "主键ID", example = "1")
    private Long id;

    @Schema(description = "规则编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "RULE_EXAM_001")
    @NotBlank(message = "规则编码不能为空")
    private String ruleCode;

    @Schema(description = "规则名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "1月龄体检催检")
    @NotBlank(message = "规则名称不能为空")
    private String ruleName;

    @Schema(description = "适用月龄（0-72月）", example = "1")
    private Integer ageMonth;

    @Schema(description = "体检类型 满月/3月/6月/8月/12月/18月/24月/30月/3岁/4岁/5岁/6岁", example = "满月")
    private String examType;

    @Schema(description = "目标人群 0全部 1高危 2专案", example = "0")
    private Integer targetAudience;

    @Schema(description = "推送渠道（短信/微信/APP）")
    private String pushChannel;

    @Schema(description = "推送模板ID", example = "1")
    private Long pushTemplateId;

    @Schema(description = "提前推送天数", example = "7")
    private Integer pushLeadDays;

    @Schema(description = "科室ID（NULL表示全科室）", example = "1")
    private Long deptId;

    @Schema(description = "优先级 1高 2中 3低", example = "2")
    private Integer priority;

    @Schema(description = "生效开始日期")
    private LocalDate effectiveStart;

    @Schema(description = "生效结束日期")
    private LocalDate effectiveEnd;

    @Schema(description = "状态 1启用 0停用", example = "1")
    private Integer status;

}

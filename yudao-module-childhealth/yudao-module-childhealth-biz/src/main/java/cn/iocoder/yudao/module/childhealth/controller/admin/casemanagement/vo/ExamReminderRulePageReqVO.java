package cn.iocoder.yudao.module.childhealth.controller.admin.casemanagement.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 体检催检规则分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ExamReminderRulePageReqVO extends PageParam {

    @Schema(description = "规则编码", example = "RULE_EXAM_001")
    private String ruleCode;

    @Schema(description = "规则名称", example = "1月龄体检催检")
    private String ruleName;

    @Schema(description = "适用月龄", example = "1")
    private Integer ageMonth;

    @Schema(description = "体检类型", example = "满月")
    private String examType;

    @Schema(description = "目标人群 0全部 1高危 2专案", example = "0")
    private Integer targetAudience;

    @Schema(description = "科室ID", example = "1")
    private Long deptId;

    @Schema(description = "状态 1启用 0停用", example = "1")
    private Integer status;

}

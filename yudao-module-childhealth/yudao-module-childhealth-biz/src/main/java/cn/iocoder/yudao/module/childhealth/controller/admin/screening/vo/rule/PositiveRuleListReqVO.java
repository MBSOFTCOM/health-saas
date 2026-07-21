package cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.rule;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 阳性判定规则列表 Request VO")
@Data
public class PositiveRuleListReqVO {

    @Schema(description = "规则编码", example = "RULE001")
    private String ruleCode;

    @Schema(description = "规则名称", example = "视力异常判定规则")
    private String ruleName;

    @Schema(description = "状态 1正常 0停用", example = "1")
    private Integer status;

}
package cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.rule;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 阳性判定规则分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PositiveRulePageReqVO extends PageParam {

    @Schema(description = "规则编码", example = "RULE001")
    private String ruleCode;

    @Schema(description = "规则名称", example = "视力异常判定规则")
    private String ruleName;

    @Schema(description = "阳性等级 1轻度 2中度 3重度", example = "1")
    private Integer positiveLevel;

    @Schema(description = "关联疾病编码", example = "J00.0")
    private String diseaseCode;

    @Schema(description = "状态 1正常 0停用", example = "1")
    private Integer status;

}
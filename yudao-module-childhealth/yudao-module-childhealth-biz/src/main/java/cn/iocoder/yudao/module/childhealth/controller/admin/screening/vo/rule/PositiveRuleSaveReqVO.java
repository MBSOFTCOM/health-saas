package cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.rule;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Schema(description = "管理后台 - 阳性判定规则新增/修改 Request VO")
@Data
public class PositiveRuleSaveReqVO {

    @Schema(description = "主键ID", example = "1")
    private Long id;

    @Schema(description = "规则编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "RULE001")
    @NotBlank(message = "规则编码不能为空")
    private String ruleCode;

    @Schema(description = "规则名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "视力异常判定规则")
    @NotBlank(message = "规则名称不能为空")
    private String ruleName;

    @Schema(description = "关联项目编码JSON")
    private String itemCodes;

    @Schema(description = "判定条件（可视化配置）JSON")
    private String conditionLogic;

    @Schema(description = "阳性等级 1轻度 2中度 3重度", example = "1")
    private Integer positiveLevel;

    @Schema(description = "关联疾病编码", example = "J00.0")
    private String diseaseCode;

    @Schema(description = "是否需要复筛 0否 1是", example = "1")
    private Integer needRecheck;

    @Schema(description = "状态 1正常 0停用", example = "1")
    private Integer status;

}
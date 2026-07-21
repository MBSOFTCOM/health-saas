package cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.rule;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 阳性判定规则 Response VO")
@Data
public class PositiveRuleRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "规则编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "RULE001")
    private String ruleCode;

    @Schema(description = "规则名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "视力异常判定规则")
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

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
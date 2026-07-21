package cn.iocoder.yudao.module.childhealth.controller.admin.medical.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 发育评估量表 Response VO")
@Data
public class DevelopmentScaleRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "量表编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "SCALE001")
    private String scaleCode;

    @Schema(description = "量表名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "丹佛发育筛查测验")
    private String scaleName;

    @Schema(description = "类型 运动/语言/智力/行为/过敏/神经运动", example = "运动")
    private String scaleType;

    @Schema(description = "最小适用月龄", example = "0")
    private Integer applicableAgeMin;

    @Schema(description = "最大适用月龄", example = "72")
    private Integer applicableAgeMax;

    @Schema(description = "适用性别 0全部 1男 2女", example = "0")
    private Integer applicableGender;

    @Schema(description = "题目数量", example = "105")
    private Integer questionCount;

    @Schema(description = "总分上限", example = "100")
    private BigDecimal totalScoreMax;

    @Schema(description = "计分规则JSON")
    private String scoringRuleJson;

    @Schema(description = "风险等级JSON")
    private String riskLevelsJson;

    @Schema(description = "量表说明")
    private String description;

    @Schema(description = "状态 1启用 0停用", example = "1")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}

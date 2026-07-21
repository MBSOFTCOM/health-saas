package cn.iocoder.yudao.module.childhealth.controller.admin.casemanagement.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 专案康复达标标准 Response VO")
@Data
public class CaseRecoveryStandardRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "专案类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer caseType;

    @Schema(description = "专案子类型")
    private String caseSubtype;

    @Schema(description = "指标编码", example = "WEIGHT")
    private String indicatorCode;

    @Schema(description = "指标名称", example = "体重")
    private String indicatorName;

    @Schema(description = "达标值", example = "12.0")
    private String standardValue;

    @Schema(description = "比较运算符", example = ">=")
    private String standardOperator;

    @Schema(description = "单位", example = "kg")
    private String unit;

    @Schema(description = "说明")
    private String description;

    @Schema(description = "状态 1启用 0停用", example = "1")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}

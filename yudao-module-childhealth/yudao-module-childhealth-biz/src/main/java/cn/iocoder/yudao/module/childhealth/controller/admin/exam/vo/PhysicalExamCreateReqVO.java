package cn.iocoder.yudao.module.childhealth.controller.admin.exam.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 体格检查录入请求 VO
 */
@Schema(description = "体格检查录入请求")
@Data
public class PhysicalExamCreateReqVO {

    @Schema(description = "体检记录ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long examId;

    @Schema(description = "身高cm")
    private BigDecimal height;

    @Schema(description = "体重kg")
    private BigDecimal weight;

    @Schema(description = "头围cm")
    private BigDecimal headCircumference;

    @Schema(description = "胸围cm")
    private BigDecimal chestCircumference;

    @Schema(description = "身高SD值")
    private BigDecimal heightSd;

    @Schema(description = "体重SD值")
    private BigDecimal weightSd;

    @Schema(description = "生长评估结果")
    private String growthAssessment;
}
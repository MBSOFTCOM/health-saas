package cn.iocoder.yudao.module.childhealth.controller.admin.exam.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 体检记录创建请求 VO
 */
@Schema(description = "体检记录创建请求")
@Data
public class ExamRecordCreateReqVO {

    @Schema(description = "儿童ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long childId;

    @Schema(description = "体检日期", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate examDate;

    @Schema(description = "体检类型 1常规 2入园 3专项", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer examType;

    @Schema(description = "体检时月龄")
    private Integer monthAge;

    @Schema(description = "科室ID")
    private Long deptId;

    @Schema(description = "医生ID")
    private Long doctorId;

    // ===== 体格检查数据 =====

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
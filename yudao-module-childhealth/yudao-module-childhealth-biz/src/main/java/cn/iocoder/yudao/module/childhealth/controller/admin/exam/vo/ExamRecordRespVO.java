package cn.iocoder.yudao.module.childhealth.controller.admin.exam.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 体检记录响应 VO
 */
@Schema(description = "体检记录响应")
@Data
public class ExamRecordRespVO {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "体检流水号")
    private String examNo;

    @Schema(description = "儿童ID")
    private Long childId;

    @Schema(description = "体检日期")
    private LocalDate examDate;

    @Schema(description = "体检类型")
    private Integer examType;

    @Schema(description = "体检时月龄")
    private Integer monthAge;

    @Schema(description = "科室ID")
    private Long deptId;

    @Schema(description = "医生ID")
    private Long doctorId;

    @Schema(description = "检查状态 1进行中 2待审核 3已完成")
    private Integer checkStatus;

    @Schema(description = "是否有异常")
    private Boolean hasAbnormal;

    @Schema(description = "异常标签")
    private String abnormalTags;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    // ===== 体格检查数据 =====

    @Schema(description = "身高cm")
    private BigDecimal height;

    @Schema(description = "体重kg")
    private BigDecimal weight;

    @Schema(description = "头围cm")
    private BigDecimal headCircumference;

    @Schema(description = "胸围cm")
    private BigDecimal chestCircumference;

    @Schema(description = "BMI")
    private BigDecimal bmi;

    @Schema(description = "身高SD值")
    private BigDecimal heightSd;

    @Schema(description = "体重SD值")
    private BigDecimal weightSd;

    @Schema(description = "生长评估结果")
    private String growthAssessment;
}
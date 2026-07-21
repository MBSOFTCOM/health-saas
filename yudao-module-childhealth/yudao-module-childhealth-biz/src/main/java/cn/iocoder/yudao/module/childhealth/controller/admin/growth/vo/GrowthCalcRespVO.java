package cn.iocoder.yudao.module.childhealth.controller.admin.growth.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 生长标准计算结果 VO
 *
 * 模块: 需求9 ▲ WHO/九城市标准差百分位自动计算
 *
 * @author 系统
 */
@Schema(description = "管理后台 - 生长标准计算 Response VO")
@Data
public class GrowthCalcRespVO {

    @Schema(description = "性别")
    private Integer gender;

    @Schema(description = "月龄")
    private BigDecimal ageMonths;

    @Schema(description = "指标类型")
    private String indicatorType;

    @Schema(description = "实际测量值")
    private BigDecimal measuredValue;

    @Schema(description = "SD 值（标准差分）")
    private BigDecimal sdValue;

    @Schema(description = "百分位（0-100）")
    private BigDecimal percentile;

    @Schema(description = "中位数（P50）")
    private BigDecimal median;

    @Schema(description = "SD-2 值")
    private BigDecimal sdNeg2;

    @Schema(description = "SD+2 值")
    private BigDecimal sdPos2;

    @Schema(description = "营养状态评估")
    private String nutritionStatus;

    @Schema(description = "数据来源")
    private String source;

    @Schema(description = "是否早产儿使用矫正月龄", example = "false")
    private Boolean correctedAgeUsed;

    @Schema(description = "矫正月龄（仅早产儿）")
    private BigDecimal correctedAge;

}

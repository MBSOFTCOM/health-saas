package cn.iocoder.yudao.module.childhealth.controller.admin.growth.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 生长标准计算请求 VO
 *
 * 模块: 需求9 ▲ WHO/九城市标准差百分位自动计算
 *
 * @author 系统
 */
@Schema(description = "管理后台 - 生长标准计算 Request VO")
@Data
public class GrowthCalcReqVO {

    @Schema(description = "性别 1男 2女", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "性别不能为空")
    private Integer gender;

    @Schema(description = "月龄", requiredMode = Schema.RequiredMode.REQUIRED, example = "12")
    @NotNull(message = "月龄不能为空")
    private BigDecimal ageMonths;

    @Schema(description = "指标类型 WEIGHT/HEIGHT/BMI/HEAD_CIRCUMFERENCE", requiredMode = Schema.RequiredMode.REQUIRED, example = "HEIGHT")
    @NotBlank(message = "指标类型不能为空")
    private String indicatorType;

    @Schema(description = "实际测量值", requiredMode = Schema.RequiredMode.REQUIRED, example = "76.5")
    @NotNull(message = "测量值不能为空")
    private BigDecimal measuredValue;

    @Schema(description = "数据来源 WHO/NINE_CITY/FENTON，默认 WHO", example = "WHO")
    private String source;

    @Schema(description = "胎龄（周），仅早产儿使用 Fenton 曲线时需要", example = "34")
    private Integer gestationalAgeWeeks;

}

package cn.iocoder.yudao.module.childhealth.controller.admin.statistics.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 阳性数据统计 Response VO
 *
 * 创建日期: 2026-07-20
 * 模块: 20. 全维度数据统计
 */
@Schema(description = "管理后台 - 阳性数据统计 Response VO")
@Data
public class StatisticsPositiveRespVO {

    @Schema(description = "区域/年龄/疾病分类编码", example = "440100")
    private String dimensionCode;

    @Schema(description = "区域/年龄/疾病分类名称", example = "广州市")
    private String dimensionName;

    @Schema(description = "筛查总数", example = "1000")
    private Integer screeningCount;

    @Schema(description = "阳性数", example = "30")
    private Integer positiveCount;

    @Schema(description = "阳性率%", example = "3.00")
    private BigDecimal positiveRate;

    @Schema(description = "复筛数", example = "20")
    private Integer recheckCount;

    @Schema(description = "复筛率%", example = "66.67")
    private BigDecimal recheckRate;

}

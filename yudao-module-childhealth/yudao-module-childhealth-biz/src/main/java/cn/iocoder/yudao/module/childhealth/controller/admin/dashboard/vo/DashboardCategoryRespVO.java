package cn.iocoder.yudao.module.childhealth.controller.admin.dashboard.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 看板五健专项分类统计 Response VO
 *
 * 创建日期: 2026-07-20
 * 模块: 1. 数据看板
 */
@Schema(description = "管理后台 - 五健专项分类统计 Response VO")
@Data
public class DashboardCategoryRespVO {

    @Schema(description = "专项编码 VISION/ORAL/BONE/PSYCHOLOGICAL/SHAPE", example = "VISION")
    private String categoryCode;

    @Schema(description = "专项名称", example = "视力")
    private String categoryName;

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

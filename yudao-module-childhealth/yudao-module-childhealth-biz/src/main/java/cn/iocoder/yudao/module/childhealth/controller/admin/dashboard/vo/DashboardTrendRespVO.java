package cn.iocoder.yudao.module.childhealth.controller.admin.dashboard.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 看板趋势图 Response VO
 *
 * 创建日期: 2026-07-20
 * 模块: 1. 数据看板
 */
@Schema(description = "管理后台 - 看板趋势图 Response VO")
@Data
public class DashboardTrendRespVO {

    @Schema(description = "日期", example = "2026-07-20")
    private LocalDate snapshotDate;

    @Schema(description = "参检率%", example = "95.24")
    private BigDecimal participationRate;

    @Schema(description = "阳性率%", example = "12.30")
    private BigDecimal positiveRate;

    @Schema(description = "复筛率%", example = "60.16")
    private BigDecimal recheckRate;

    @Schema(description = "随访率%", example = "85.37")
    private BigDecimal followRate;

    @Schema(description = "筛查人数", example = "100")
    private Integer screeningCount;

    @Schema(description = "阳性人数", example = "12")
    private Integer positiveCount;

    @Schema(description = "复筛人数", example = "7")
    private Integer recheckCount;

    @Schema(description = "随访人数", example = "10")
    private Integer followCount;

    /**
     * 将快照DO转为趋势VO
     */
    public static DashboardTrendRespVO fromSnapshot(Object snapshot) {
        // 转换逻辑在Service层实现，此处仅占位
        return new DashboardTrendRespVO();
    }

    /**
     * 简单聚合返回（按日期排序的列表）
     */
    @Schema(description = "趋势列表")
    private List<DashboardTrendRespVO> trendList;

}

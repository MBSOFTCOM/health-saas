package cn.iocoder.yudao.module.childhealth.controller.admin.statistics.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 复筛数据统计 Response VO
 *
 * 创建日期: 2026-07-20
 * 模块: 20. 全维度数据统计
 */
@Schema(description = "管理后台 - 复筛数据统计 Response VO")
@Data
public class StatisticsRecheckRespVO {

    @Schema(description = "初筛阳性人数", example = "123")
    private Integer initialPositiveCount;

    @Schema(description = "需复筛人数", example = "123")
    private Integer needRecheckCount;

    @Schema(description = "已复筛人数", example = "74")
    private Integer recheckCompletedCount;

    @Schema(description = "复筛率%", example = "60.16")
    private BigDecimal recheckRate;

    @Schema(description = "复筛仍阳性数", example = "20")
    private Integer stillPositiveCount;

    @Schema(description = "复筛阳性率%", example = "27.03")
    private BigDecimal stillPositiveRate;

    @Schema(description = "已转介数", example = "15")
    private Integer referralCount;

}

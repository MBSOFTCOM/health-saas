package cn.iocoder.yudao.module.childhealth.controller.admin.statistics.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 筛查总表统计 Response VO
 *
 * 创建日期: 2026-07-20
 * 模块: 20. 全维度数据统计
 */
@Schema(description = "管理后台 - 筛查总表统计 Response VO")
@Data
public class StatisticsScreeningTotalRespVO {

    @Schema(description = "总人数", example = "1000")
    private Integer totalCount;

    @Schema(description = "已审核数", example = "900")
    private Integer auditedCount;

    @Schema(description = "待审核数", example = "100")
    private Integer pendingAuditCount;

    @Schema(description = "审核完成率%", example = "90.00")
    private BigDecimal auditRate;

    @Schema(description = "阳性人数", example = "123")
    private Integer positiveCount;

    @Schema(description = "阳性率%", example = "13.67")
    private BigDecimal positiveRate;

    @Schema(description = "已转介人数", example = "20")
    private Integer referralCount;

}

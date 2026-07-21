package cn.iocoder.yudao.module.childhealth.controller.admin.dashboard.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 看板总览 Response VO
 *
 * 创建日期: 2026-07-20
 * 模块: 1. 数据看板
 */
@Schema(description = "管理后台 - 看板总览 Response VO")
@Data
public class DashboardOverviewRespVO {

    @Schema(description = "应检人数", example = "1050")
    private Integer targetCount;

    @Schema(description = "实检人数", example = "1000")
    private Integer actualCount;

    @Schema(description = "参检率%", example = "95.24")
    private BigDecimal participationRate;

    @Schema(description = "阳性人数", example = "123")
    private Integer positiveCount;

    @Schema(description = "阳性率%", example = "12.30")
    private BigDecimal positiveRate;

    @Schema(description = "需复筛人数", example = "123")
    private Integer needRecheckCount;

    @Schema(description = "已复筛人数", example = "74")
    private Integer recheckCount;

    @Schema(description = "复筛率%", example = "60.16")
    private BigDecimal recheckRate;

    @Schema(description = "需随访人数", example = "123")
    private Integer needFollowCount;

    @Schema(description = "已随访人数", example = "105")
    private Integer followCount;

    @Schema(description = "随访率%", example = "85.37")
    private BigDecimal followRate;

    @Schema(description = "待审核数", example = "20")
    private Integer pendingAuditCount;

    @Schema(description = "待复筛数", example = "49")
    private Integer pendingRecheckCount;

    @Schema(description = "待随访数", example = "18")
    private Integer pendingFollowCount;

}

package cn.iocoder.yudao.module.childhealth.controller.admin.statistics.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 随访数据统计 Response VO
 *
 * 创建日期: 2026-07-20
 * 模块: 20. 全维度数据统计
 */
@Schema(description = "管理后台 - 随访数据统计 Response VO")
@Data
public class StatisticsFollowRespVO {

    @Schema(description = "累计随访数", example = "500")
    private Integer totalFollowCount;

    @Schema(description = "当日随访数", example = "30")
    private Integer todayFollowCount;

    @Schema(description = "待随访数", example = "120")
    private Integer pendingFollowCount;

    @Schema(description = "已完成随访数", example = "380")
    private Integer completedFollowCount;

    @Schema(description = "随访完成率%", example = "76.00")
    private BigDecimal followCompleteRate;

    @Schema(description = "异常干预数", example = "50")
    private Integer abnormalInterventionCount;

    @Schema(description = "健康改善数", example = "180")
    private Integer healthImprovementCount;

}

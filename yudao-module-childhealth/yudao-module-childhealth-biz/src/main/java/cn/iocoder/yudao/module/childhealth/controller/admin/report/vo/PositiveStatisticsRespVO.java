package cn.iocoder.yudao.module.childhealth.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 阳性数据统计响应VO
 */
@Schema(description = "阳性数据统计响应")
@Data
public class PositiveStatisticsRespVO {

    @Schema(description = "总体统计")
    private OverallStatistics overall;

    @Schema(description = "按维度分布统计")
    private List<DistributionItem> distributions;

    @Schema(description = "趋势分析")
    private List<TrendItem> trends;

    @Schema(description = "生成时间")
    private LocalDateTime generatedAt;

    @Data
    public static class OverallStatistics {
        @Schema(description = "筛查总人数")
        private Integer totalScreened;

        @Schema(description = "阳性总人数")
        private Integer totalPositive;

        @Schema(description = "阳性率")
        private BigDecimal positiveRate;

        @Schema(description = "新发阳性人数")
        private Integer newPositive;

        @Schema(description = "复筛确认人数")
        private Integer recheckConfirmed;
    }

    @Data
    public static class DistributionItem {
        @Schema(description = "维度编码")
        private String code;

        @Schema(description = "维度名称")
        private String name;

        @Schema(description = "筛查人数")
        private Integer screenedCount;

        @Schema(description = "阳性人数")
        private Integer positiveCount;

        @Schema(description = "阳性率")
        private BigDecimal positiveRate;

        @Schema(description = "占比")
        private BigDecimal proportion;
    }

    @Data
    public static class TrendItem {
        @Schema(description = "时间段")
        private String period;

        @Schema(description = "筛查人数")
        private Integer screenedCount;

        @Schema(description = "阳性人数")
        private Integer positiveCount;

        @Schema(description = "阳性率")
        private BigDecimal positiveRate;
    }
}
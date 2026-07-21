package cn.iocoder.yudao.module.childhealth.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 随访数据统计响应VO
 */
@Schema(description = "随访数据统计响应")
@Data
public class FollowUpStatisticsRespVO {

    @Schema(description = "总体统计")
    private OverallStatistics overall;

    @Schema(description = "当日统计")
    private DailyStatistics daily;

    @Schema(description = "随访完成率统计")
    private CompletionStatistics completion;

    @Schema(description = "按随访类型分布")
    private List<FollowTypeDistribution> typeDistributions;

    @Schema(description = "按随访结果分布")
    private List<FollowResultDistribution> resultDistributions;

    @Schema(description = "生成时间")
    private LocalDateTime generatedAt;

    @Data
    public static class OverallStatistics {
        @Schema(description = "需随访总人数")
        private Integer totalNeedFollow;

        @Schema(description = "累计随访人次")
        private Integer totalFollowUps;

        @Schema(description = "已随访人数")
        private Integer followedCount;

        @Schema(description = "待随访人数")
        private Integer pendingCount;

        @Schema(description = "失访人数")
        private Integer lostCount;

        @Schema(description = "随访覆盖率")
        private BigDecimal coverageRate;
    }

    @Data
    public static class DailyStatistics {
        @Schema(description = "统计日期")
        private LocalDate date;

        @Schema(description = "当日计划随访人数")
        private Integer plannedCount;

        @Schema(description = "当日实际随访人数")
        private Integer actualCount;

        @Schema(description = "当日完成率")
        private BigDecimal completionRate;

        @Schema(description = "当日新增需随访人数")
        private Integer newNeedCount;
    }

    @Data
    public static class CompletionStatistics {
        @Schema(description = "应随访总人数")
        private Integer shouldFollow;

        @Schema(description = "已完成随访人数")
        private Integer completedFollow;

        @Schema(description = "完成率")
        private BigDecimal completionRate;

        @Schema(description = "规范随访人数")
        private Integer standardFollow;

        @Schema(description = "规范随访率")
        private BigDecimal standardRate;

        @Schema(description = "及时随访人数")
        private Integer timelyFollow;

        @Schema(description = "及时率")
        private BigDecimal timelyRate;
    }

    @Data
    public static class FollowTypeDistribution {
        @Schema(description = "随访类型编码")
        private Integer typeCode;

        @Schema(description = "随访类型名称")
        private String typeName;

        @Schema(description = "随访人次")
        private Integer count;

        @Schema(description = "占比")
        private BigDecimal proportion;
    }

    @Data
    public static class FollowResultDistribution {
        @Schema(description = "结果编码")
        private String resultCode;

        @Schema(description = "结果名称")
        private String resultName;

        @Schema(description = "人次")
        private Integer count;

        @Schema(description = "占比")
        private BigDecimal proportion;
    }
}
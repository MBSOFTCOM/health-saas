package cn.iocoder.yudao.module.childhealth.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 工作量统计响应VO
 */
@Schema(description = "工作量统计响应")
@Data
public class WorkloadStatisticsRespVO {

    @Schema(description = "总体统计")
    private OverallStatistics overall;

    @Schema(description = "按维度明细")
    private List<WorkloadDetail> details;

    @Schema(description = "工作量排名")
    private List<WorkloadRanking> rankings;

    @Schema(description = "生成时间")
    private LocalDateTime generatedAt;

    @Data
    public static class OverallStatistics {
        @Schema(description = "筛查总人次")
        private Integer totalScreening;

        @Schema(description = "审核总人次")
        private Integer totalAudit;

        @Schema(description = "随访总人次")
        private Integer totalFollow;

        @Schema(description = "总工作时长（小时）")
        private BigDecimal totalWorkHours;

        @Schema(description = "平均每日工作量")
        private BigDecimal avgDailyWorkload;
    }

    @Data
    public static class WorkloadDetail {
        @Schema(description = "维度编码")
        private String code;

        @Schema(description = "维度名称")
        private String name;

        @Schema(description = "筛查人次")
        private Integer screeningCount;

        @Schema(description = "审核人次")
        private Integer auditCount;

        @Schema(description = "随访人次")
        private Integer followCount;

        @Schema(description = "总人次")
        private Integer totalCount;

        @Schema(description = "工作时长（小时）")
        private BigDecimal workHours;

        @Schema(description = "占比")
        private BigDecimal proportion;
    }

    @Data
    public static class WorkloadRanking {
        @Schema(description = "排名")
        private Integer rank;

        @Schema(description = "编码")
        private String code;

        @Schema(description = "名称")
        private String name;

        @Schema(description = "工作量")
        private Integer workload;

        @Schema(description = "工作时长")
        private BigDecimal workHours;
    }
}
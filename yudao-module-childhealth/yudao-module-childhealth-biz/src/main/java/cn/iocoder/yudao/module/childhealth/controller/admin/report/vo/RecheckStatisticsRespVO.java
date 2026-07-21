package cn.iocoder.yudao.module.childhealth.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 复筛数据统计响应VO
 */
@Schema(description = "复筛数据统计响应")
@Data
public class RecheckStatisticsRespVO {

    @Schema(description = "总体统计")
    private OverallStatistics overall;

    @Schema(description = "流程跟踪统计")
    private ProcessTracking tracking;

    @Schema(description = "按状态分布")
    private List<StatusDistribution> statusDistributions;

    @Schema(description = "按疾病分布")
    private List<DiseaseRecheck> diseaseRechecks;

    @Schema(description = "生成时间")
    private LocalDateTime generatedAt;

    @Data
    public static class OverallStatistics {
        @Schema(description = "初筛阳性人数")
        private Integer initialPositiveCount;

        @Schema(description = "需复筛人数")
        private Integer needRecheckCount;

        @Schema(description = "已复筛人数")
        private Integer recheckedCount;

        @Schema(description = "复筛完成率")
        private BigDecimal completionRate;

        @Schema(description = "复筛确认阳性人数")
        private Integer confirmedPositiveCount;

        @Schema(description = "复筛排除人数")
        private Integer excludedCount;

        @Schema(description = "转诊人数")
        private Integer referralCount;
    }

    @Data
    public static class ProcessTracking {
        @Schema(description = "待复筛人数")
        private Integer pendingCount;

        @Schema(description = "已预约人数")
        private Integer appointedCount;

        @Schema(description = "已完成人数")
        private Integer completedCount;

        @Schema(description = "超期未完成人数")
        private Integer overdueCount;

        @Schema(description = "平均复筛周期（天）")
        private Integer avgRecheckDays;
    }

    @Data
    public static class StatusDistribution {
        @Schema(description = "状态编码")
        private Integer statusCode;

        @Schema(description = "状态名称")
        private String statusName;

        @Schema(description = "人数")
        private Integer count;

        @Schema(description = "占比")
        private BigDecimal proportion;
    }

    @Data
    public static class DiseaseRecheck {
        @Schema(description = "疾病编码")
        private String diseaseCode;

        @Schema(description = "疾病名称")
        private String diseaseName;

        @Schema(description = "初筛阳性人数")
        private Integer initialCount;

        @Schema(description = "复筛人数")
        private Integer recheckCount;

        @Schema(description = "确认人数")
        private Integer confirmedCount;

        @Schema(description = "排除人数")
        private Integer excludedCount;

        @Schema(description = "确认率")
        private BigDecimal confirmationRate;
    }
}
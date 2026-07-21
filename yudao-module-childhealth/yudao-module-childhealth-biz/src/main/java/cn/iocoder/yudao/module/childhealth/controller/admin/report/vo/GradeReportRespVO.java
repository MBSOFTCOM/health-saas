package cn.iocoder.yudao.module.childhealth.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 年级报告响应VO
 */
@Schema(description = "年级报告响应")
@Data
public class GradeReportRespVO {

    @Schema(description = "年级ID")
    private Long gradeId;

    @Schema(description = "年级名称")
    private String gradeName;

    @Schema(description = "年龄范围描述")
    private String ageRange;

    @Schema(description = "总人数")
    private Integer totalCount;

    @Schema(description = "健康风险趋势")
    private List<HealthRiskTrend> riskTrends;

    @Schema(description = "疾病分布")
    private List<DiseaseDistribution> diseaseDistributions;

    @Schema(description = "年龄段对比分析")
    private List<AgeGroupComparison> ageGroupComparisons;

    @Schema(description = "生成时间")
    private LocalDateTime generatedAt;

    @Data
    public static class HealthRiskTrend {
        @Schema(description = "时间段")
        private String period;

        @Schema(description = "视力不良率")
        private BigDecimal visionAbnormalRate;

        @Schema(description = "龋齿率")
        private BigDecimal cariesRate;

        @Schema(description = "肥胖率")
        private BigDecimal obesityRate;

        @Schema(description = "营养不良率")
        private BigDecimal malnutritionRate;

        @Schema(description = "贫血率")
        private BigDecimal anemiaRate;

        @Schema(description = "其他异常率")
        private BigDecimal otherAbnormalRate;
    }

    @Data
    public static class DiseaseDistribution {
        @Schema(description = "疾病编码")
        private String diseaseCode;

        @Schema(description = "疾病名称")
        private String diseaseName;

        @Schema(description = "患病人数")
        private Integer caseCount;

        @Schema(description = "患病率")
        private BigDecimal caseRate;

        @Schema(description = "男女比例")
        private String genderRatio;
    }

    @Data
    public static class AgeGroupComparison {
        @Schema(description = "年龄段")
        private String ageGroup;

        @Schema(description = "筛查人数")
        private Integer screenedCount;

        @Schema(description = "异常人数")
        private Integer abnormalCount;

        @Schema(description = "异常率")
        private BigDecimal abnormalRate;

        @Schema(description = "主要健康问题")
        private List<String> mainHealthIssues;
    }
}
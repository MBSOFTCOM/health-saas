package cn.iocoder.yudao.module.childhealth.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 区域监管报告响应VO
 */
@Schema(description = "区域监管报告响应")
@Data
public class RegionReportRespVO {

    @Schema(description = "区域编码")
    private String regionCode;

    @Schema(description = "区域名称")
    private String regionName;

    @Schema(description = "总体覆盖情况")
    private CoverageStatistics coverage;

    @Schema(description = "筛查指标汇总")
    private ScreeningIndicators indicators;

    @Schema(description = "学校明细列表")
    private List<SchoolIndicator> schoolIndicators;

    @Schema(description = "区域对比分析")
    private List<RegionComparison> regionComparisons;

    @Schema(description = "生成时间")
    private LocalDateTime generatedAt;

    @Data
    public static class CoverageStatistics {
        @Schema(description = "学校总数")
        private Integer totalSchools;

        @Schema(description = "已筛查学校数")
        private Integer screenedSchools;

        @Schema(description = "学校覆盖率")
        private BigDecimal schoolCoverageRate;

        @Schema(description = "学生总数")
        private Integer totalStudents;

        @Schema(description = "已筛查学生数")
        private Integer screenedStudents;

        @Schema(description = "学生覆盖率")
        private BigDecimal studentCoverageRate;

        @Schema(description = "批次总数")
        private Integer totalBatches;

        @Schema(description = "已完成批次数")
        private Integer completedBatches;
    }

    @Data
    public static class ScreeningIndicators {
        @Schema(description = "筛查总人次")
        private Integer totalScreenings;

        @Schema(description = "阳性总人次")
        private Integer totalPositives;

        @Schema(description = "阳性率")
        private BigDecimal positiveRate;

        @Schema(description = "复筛总人次")
        private Integer totalRechecks;

        @Schema(description = "复筛完成率")
        private BigDecimal recheckCompletionRate;

        @Schema(description = "随访总人次")
        private Integer totalFollowUps;

        @Schema(description = "随访完成率")
        private BigDecimal followUpCompletionRate;

        @Schema(description = "转诊总人次")
        private Integer totalReferrals;
    }

    @Data
    public static class SchoolIndicator {
        @Schema(description = "学校ID")
        private Long schoolId;

        @Schema(description = "学校名称")
        private String schoolName;

        @Schema(description = "学校类型：1-公立，2-私立")
        private Integer schoolType;

        @Schema(description = "应检人数")
        private Integer targetCount;

        @Schema(description = "实检人数")
        private Integer screenedCount;

        @Schema(description = "参检率")
        private BigDecimal participationRate;

        @Schema(description = "阳性人数")
        private Integer positiveCount;

        @Schema(description = "阳性率")
        private BigDecimal positiveRate;

        @Schema(description = "复筛完成率")
        private BigDecimal recheckCompletionRate;

        @Schema(description = "随访完成率")
        private BigDecimal followUpCompletionRate;

        @Schema(description = "状态：1-进行中，2-已完成，3-待审核")
        private Integer status;
    }

    @Data
    public static class RegionComparison {
        @Schema(description = "区域编码")
        private String regionCode;

        @Schema(description = "区域名称")
        private String regionName;

        @Schema(description = "筛查人数")
        private Integer screenedCount;

        @Schema(description = "阳性率")
        private BigDecimal positiveRate;

        @Schema(description = "复筛完成率")
        private BigDecimal recheckCompletionRate;

        @Schema(description = "随访完成率")
        private BigDecimal followUpCompletionRate;

        @Schema(description = "排名")
        private Integer rank;
    }
}
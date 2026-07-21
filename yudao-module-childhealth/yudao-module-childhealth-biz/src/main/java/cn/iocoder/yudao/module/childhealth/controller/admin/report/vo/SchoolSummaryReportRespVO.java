package cn.iocoder.yudao.module.childhealth.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 学校汇总报告响应VO
 */
@Schema(description = "学校汇总报告响应")
@Data
public class SchoolSummaryReportRespVO {

    @Schema(description = "学校ID")
    private Long schoolId;

    @Schema(description = "学校名称")
    private String schoolName;

    @Schema(description = "批次ID")
    private Long batchId;

    @Schema(description = "批次名称")
    private String batchName;

    @Schema(description = "筛查开始日期")
    private LocalDate startDate;

    @Schema(description = "筛查结束日期")
    private LocalDate endDate;

    @Schema(description = "总体统计")
    private OverallStatistics overall;

    @Schema(description = "分年级统计")
    private List<GradeStatistics> gradeStats;

    @Schema(description = "分班级统计")
    private List<ClassStatistics> classStats;

    @Schema(description = "生成时间")
    private LocalDateTime generatedAt;

    @Data
    public static class OverallStatistics {
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

        @Schema(description = "复筛完成人数")
        private Integer recheckCompletedCount;

        @Schema(description = "复筛完成率")
        private BigDecimal recheckCompletionRate;

        @Schema(description = "随访完成人数")
        private Integer followCompletedCount;

        @Schema(description = "随访完成率")
        private BigDecimal followCompletionRate;

        @Schema(description = "转诊人数")
        private Integer referralCount;
    }

    @Data
    public static class GradeStatistics {
        @Schema(description = "年级ID")
        private Long gradeId;

        @Schema(description = "年级名称")
        private String gradeName;

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
    }

    @Data
    public static class ClassStatistics {
        @Schema(description = "班级ID")
        private Long classId;

        @Schema(description = "班级名称")
        private String className;

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
    }
}
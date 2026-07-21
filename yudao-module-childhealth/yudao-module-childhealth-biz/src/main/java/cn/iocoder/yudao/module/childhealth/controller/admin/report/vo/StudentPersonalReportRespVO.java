package cn.iocoder.yudao.module.childhealth.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 学生个人报告响应VO
 */
@Schema(description = "学生个人报告响应")
@Data
public class StudentPersonalReportRespVO {

    @Schema(description = "学生ID")
    private Long studentId;

    @Schema(description = "学生姓名")
    private String studentName;

    @Schema(description = "性别：1-男，2-女")
    private Integer gender;

    @Schema(description = "出生日期")
    private LocalDate birthDate;

    @Schema(description = "班级名称")
    private String className;

    @Schema(description = "学校名称")
    private String schoolName;

    @Schema(description = "筛查日期")
    private LocalDate screeningDate;

    @Schema(description = "筛查结果汇总")
    private ScreeningSummary screeningSummary;

    @Schema(description = "阳性结果列表")
    private List<PositiveResult> positiveResults;

    @Schema(description = "复筛建议")
    private List<RecheckSuggestion> recheckSuggestions;

    @Schema(description = "健康指导建议")
    private String healthGuidance;

    @Schema(description = "生成时间")
    private LocalDateTime generatedAt;

    @Data
    public static class ScreeningSummary {
        @Schema(description = "筛查项目总数")
        private Integer totalItems;

        @Schema(description = "正常项目数")
        private Integer normalCount;

        @Schema(description = "阳性项目数")
        private Integer positiveCount;

        @Schema(description = "需复筛项目数")
        private Integer recheckCount;

        @Schema(description = "筛查状态：1-正常，2-有异常")
        private Integer status;
    }

    @Data
    public static class PositiveResult {
        @Schema(description = "项目编码")
        private String itemCode;

        @Schema(description = "项目名称")
        private String itemName;

        @Schema(description = "检测值")
        private String value;

        @Schema(description = "阳性等级：1-轻度，2-中度，3-重度")
        private Integer positiveLevel;

        @Schema(description = "异常描述")
        private String abnormalDesc;

        @Schema(description = "疾病编码")
        private String diseaseCode;

        @Schema(description = "疾病名称")
        private String diseaseName;

        @Schema(description = "解读说明")
        private String interpretation;
    }

    @Data
    public static class RecheckSuggestion {
        @Schema(description = "项目编码")
        private String itemCode;

        @Schema(description = "项目名称")
        private String itemName;

        @Schema(description = "建议复筛科室")
        private String suggestDepartment;

        @Schema(description = "建议复筛时间")
        private LocalDate suggestDate;

        @Schema(description = "复筛说明")
        private String description;
    }
}
package cn.iocoder.yudao.module.childhealth.controller.app.parent.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 家长 App - 孩子筛查报告 Response VO
 *
 * 创建日期: 2026-07-20
 * 模块: 移动端 - 家长 App
 */
@Schema(description = "家长 App - 孩子筛查报告 VO")
@Data
public class ParentScreeningReportRespVO {

    @Schema(description = "筛查记录ID", example = "5001")
    private Long recordId;

    @Schema(description = "学生ID", example = "1001")
    private Long studentId;

    @Schema(description = "学生姓名", example = "张小明")
    private String studentName;

    @Schema(description = "筛查日期", example = "2026-03-15")
    private LocalDate screeningDate;

    @Schema(description = "批次名称", example = "2026年春季五健筛查")
    private String batchName;

    @Schema(description = "审核状态 1进行中 2待审核 3已审核", example = "3")
    private Integer checkStatus;

    @Schema(description = "是否阳性 0否 1是", example = "0")
    private Integer hasPositive;

    @Schema(description = "五健专项结果列表")
    private List<CategoryResult> categoryResults;

    @Schema(description = "阳性项目数", example = "1")
    private Integer positiveCount;

    @Schema(description = "总体健康结论", example = "正常")
    private String overallConclusion;

    @Schema(description = "健康指导建议")
    private String healthGuidance;

    @Schema(description = "下一步建议")
    private String nextStepAdvice;

    @Data
    public static class CategoryResult {
        @Schema(description = "专项编码 VISION/ORAL/BONE/PSYCHOLOGICAL/SHAPE", example = "VISION")
        private String categoryCode;

        @Schema(description = "专项名称", example = "视力")
        private String categoryName;

        @Schema(description = "检测结果", example = "正常")
        private String result;

        @Schema(description = "是否异常 0正常 1异常", example = "0")
        private Integer isAbnormal;

        @Schema(description = "异常描述")
        private String abnormalDesc;

        @Schema(description = "建议")
        private String advice;
    }

}

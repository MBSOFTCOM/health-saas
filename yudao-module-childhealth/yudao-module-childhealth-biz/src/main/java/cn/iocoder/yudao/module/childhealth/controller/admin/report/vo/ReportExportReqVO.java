package cn.iocoder.yudao.module.childhealth.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 报表导出请求VO
 */
@Schema(description = "报表导出请求")
@Data
public class ReportExportReqVO {

    @Schema(description = "报表类型：student-学生个人报告，school-学校汇总报告，grade-年级报告，region-区域监管报告，screening-筛查总表，positive-阳性数据统计，recheck-复筛数据统计，follow-随访数据统计，workload-工作量统计，batch-体检批次查询")
    private String reportType;

    @Schema(description = "导出格式：xlsx-Excel，pdf-PDF，csv-CSV")
    private String format;

    @Schema(description = "记录ID（学生个人报告用）")
    private Long recordId;

    @Schema(description = "批次ID")
    private Long batchId;

    @Schema(description = "学校ID")
    private Long schoolId;

    @Schema(description = "年级ID")
    private Long gradeId;

    @Schema(description = "区域编码")
    private String regionCode;

    @Schema(description = "开始日期（格式：yyyy-MM-dd）")
    private String startDate;

    @Schema(description = "结束日期（格式：yyyy-MM-dd）")
    private String endDate;
}
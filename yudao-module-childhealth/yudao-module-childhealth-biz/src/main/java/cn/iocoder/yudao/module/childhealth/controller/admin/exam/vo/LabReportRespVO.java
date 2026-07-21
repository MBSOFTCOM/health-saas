package cn.iocoder.yudao.module.childhealth.controller.admin.exam.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 辅助检查报告响应 VO
 */
@Schema(description = "辅助检查报告响应")
@Data
public class LabReportRespVO {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "体检记录ID")
    private Long examId;

    @Schema(description = "报告类型 1检验 2检查")
    private Integer reportType;

    @Schema(description = "报告编号")
    private String reportCode;

    @Schema(description = "报告名称")
    private String reportName;

    @Schema(description = "报告日期")
    private LocalDateTime reportDate;

    @Schema(description = "报告内容JSON")
    private String reportContent;

    @Schema(description = "报告文件URL")
    private String reportUrl;

    @Schema(description = "来源 1LIS 2PACS 3手工录入")
    private Integer source;

    @Schema(description = "是否异常")
    private Boolean isAbnormal;

    @Schema(description = "异常项目JSON")
    private String abnormalItems;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
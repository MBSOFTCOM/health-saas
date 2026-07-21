package cn.iocoder.yudao.module.childhealth.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 报告模板配置 Response VO
 *
 * 创建日期: 2026-07-20
 */
@Schema(description = "管理后台 - 报告模板配置 Response VO")
@Data
public class ReportTemplateConfigRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "模板编码", example = "STUDENT_REPORT_V1")
    private String templateCode;

    @Schema(description = "模板名称", example = "学生个人五健筛查报告")
    private String templateName;

    @Schema(description = "报告类型 1学生个人 2学校汇总 3年级 4区域监管", example = "1")
    private Integer reportType;

    @Schema(description = "模板内容(FTL/HTML/JSON)")
    private String templateContent;

    @Schema(description = "数据来源(SQL路径或接口)")
    private String dataSource;

    @Schema(description = "默认格式 PDF/EXCEL/XML/HTML", example = "PDF")
    private String defaultFormat;

    @Schema(description = "适用最小月龄", example = "36")
    private Integer applicableAgeMin;

    @Schema(description = "适用最大月龄", example = "216")
    private Integer applicableAgeMax;

    @Schema(description = "是否该类型默认模板 0否 1是", example = "1")
    private Integer isDefault;

    @Schema(description = "排序", example = "1")
    private Integer sortOrder;

    @Schema(description = "状态 1启用 0停用", example = "1")
    private Integer status;

    @Schema(description = "模板说明")
    private String description;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

}

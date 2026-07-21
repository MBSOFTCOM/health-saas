package cn.iocoder.yudao.module.childhealth.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 报告生成请求 VO
 *
 * 创建日期: 2026-07-20
 * 模块: 19. 多维度报告体系
 * 用途: 触发异步生成各类型报告（学生/学校/年级/区域）
 */
@Schema(description = "管理后台 - 报告生成请求 VO")
@Data
public class ReportGenerateReqVO {

    @Schema(description = "报告模板ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "报告模板ID不能为空")
    private Long templateId;

    @Schema(description = "报告格式 PDF/EXCEL/XML", example = "PDF")
    private String reportFormat;

    @Schema(description = "关联筛查批次ID", example = "100")
    private Long batchId;

    @Schema(description = "学生ID（生成学生个人报告时必填）", example = "1001")
    private Long studentId;

    @Schema(description = "学校ID（生成学校/年级报告时必填）", example = "10")
    private Long schoolId;

    @Schema(description = "年级ID（生成年级报告时必填）", example = "100")
    private Long gradeId;

    @Schema(description = "区域编码（生成区域监管报告时必填）", example = "330100")
    private String regionCode;

    @Schema(description = "是否同步生成 0否（默认异步） 1是", example = "0")
    private Integer sync;

}

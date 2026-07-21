package cn.iocoder.yudao.module.childhealth.controller.admin.report.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 报告模板配置分页 Request VO
 *
 * 创建日期: 2026-07-20
 */
@Schema(description = "管理后台 - 报告模板配置分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ReportTemplateConfigPageReqVO extends PageParam {

    @Schema(description = "模板编码", example = "STUDENT_REPORT_V1")
    private String templateCode;

    @Schema(description = "模板名称", example = "学生个人五健筛查报告")
    private String templateName;

    @Schema(description = "报告类型 1学生 2学校 3年级 4区域", example = "1")
    private Integer reportType;

    @Schema(description = "状态 1启用 0停用", example = "1")
    private Integer status;

}

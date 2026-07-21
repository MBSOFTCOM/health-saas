package cn.iocoder.yudao.module.childhealth.controller.admin.medical.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

@Schema(description = "管理后台 - 外部报告归档分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ExternalReportArchivePageReqVO extends PageParam {

    @Schema(description = "儿童档案ID", example = "1001")
    private Long childId;

    @Schema(description = "来源机构", example = "市妇幼保健院")
    private String sourceOrg;

    @Schema(description = "报告类型", example = "血常规")
    private String reportType;

    @Schema(description = "报告名称", example = "2026年6月血常规报告")
    private String reportName;

    @Schema(description = "文件格式 PDF/JPG/PNG/DICOM", example = "PDF")
    private String fileFormat;

    @Schema(description = "报告日期", example = "2026-06-15")
    private LocalDate reportDate;

}

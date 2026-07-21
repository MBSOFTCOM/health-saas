package cn.iocoder.yudao.module.childhealth.controller.admin.medical.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 外部报告归档 Response VO")
@Data
public class ExternalReportArchiveRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "儿童档案ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1001")
    private Long childId;

    @Schema(description = "来源机构", example = "市妇幼保健院")
    private String sourceOrg;

    @Schema(description = "报告类型", example = "血常规")
    private String reportType;

    @Schema(description = "报告名称", example = "2026年6月血常规报告")
    private String reportName;

    @Schema(description = "文件URL", example = "https://oss.example.com/report.pdf")
    private String fileUrl;

    @Schema(description = "文件大小(字节)", example = "102400")
    private Long fileSize;

    @Schema(description = "文件格式 PDF/JPG/PNG/DICOM", example = "PDF")
    private String fileFormat;

    @Schema(description = "报告日期", example = "2026-06-15")
    private LocalDate reportDate;

    @Schema(description = "归档日期", example = "2026-07-20")
    private LocalDate archiveDate;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}

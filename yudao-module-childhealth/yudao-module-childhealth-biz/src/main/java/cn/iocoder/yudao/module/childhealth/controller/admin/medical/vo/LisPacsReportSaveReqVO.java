package cn.iocoder.yudao.module.childhealth.controller.admin.medical.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - LIS/PACS检验检查报告新增/修改 Request VO")
@Data
public class LisPacsReportSaveReqVO {

    @Schema(description = "主键ID", example = "1")
    private Long id;

    @Schema(description = "儿童档案ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1001")
    @NotNull(message = "儿童档案ID不能为空")
    private Long childId;

    @Schema(description = "报告单号", requiredMode = Schema.RequiredMode.REQUIRED, example = "LIS20260720001")
    @NotBlank(message = "报告单号不能为空")
    private String reportNo;

    @Schema(description = "报告类型 LIS/PACS", requiredMode = Schema.RequiredMode.REQUIRED, example = "LIS")
    @NotBlank(message = "报告类型不能为空")
    private String reportType;

    @Schema(description = "来源系统（HIS系统名/设备名）", example = "HIS-MAIN")
    private String sourceSystem;

    @Schema(description = "检查项目", example = "血常规")
    private String examItem;

    @Schema(description = "报告数据JSON")
    private String reportDataJson;

    @Schema(description = "报告文件URL")
    private String reportFileUrl;

    @Schema(description = "接收时间")
    private LocalDateTime receivedTime;

    @Schema(description = "报告时间")
    private LocalDateTime reportTime;

    @Schema(description = "状态 0待处理 1已归档到病历 2已忽略", example = "0")
    private Integer status;

    @Schema(description = "关联病历ID", example = "100")
    private Long medicalRecordId;

}

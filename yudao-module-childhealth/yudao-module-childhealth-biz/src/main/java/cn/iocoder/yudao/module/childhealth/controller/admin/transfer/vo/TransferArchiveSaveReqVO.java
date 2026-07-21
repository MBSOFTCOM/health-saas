package cn.iocoder.yudao.module.childhealth.controller.admin.transfer.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Schema(description = "管理后台 - 档案转递新增/修改 Request VO")
@Data
public class TransferArchiveSaveReqVO {

    @Schema(description = "主键ID", example = "1")
    private Long id;

    @Schema(description = "转递编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "ZD202401010001")
    @NotBlank(message = "转递编号不能为空")
    private String transferNo;

    @Schema(description = "儿童ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "儿童ID不能为空")
    private Long childId;

    @Schema(description = "转递类型", example = "1")
    private Integer transferType;

    @Schema(description = "转递日期")
    private LocalDate transferDate;

    @Schema(description = "来源医院")
    private String sourceHospital;

    @Schema(description = "目标医院")
    private String targetHospital;

    @Schema(description = "转递原因")
    private String transferReason;

    @Schema(description = "健康摘要")
    private String healthSummary;

    @Schema(description = "病例列表JSON")
    private String caseList;

    @Schema(description = "联系人")
    private String contactPerson;

    @Schema(description = "联系电话")
    private String contactMobile;

    @Schema(description = "反馈内容")
    private String feedbackContent;

    @Schema(description = "状态", example = "0")
    private Integer status;

}
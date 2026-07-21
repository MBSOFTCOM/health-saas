package cn.iocoder.yudao.module.childhealth.controller.admin.casemanagement.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Schema(description = "管理后台 - 专案随访记录新增/修改 Request VO")
@Data
public class CaseFollowupRecordSaveReqVO {

    @Schema(description = "主键ID", example = "1")
    private Long id;

    @Schema(description = "专案ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "专案ID不能为空")
    private Long caseId;

    @Schema(description = "儿童ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "儿童ID不能为空")
    private Long childId;

    @Schema(description = "随访编号", example = "CF20260720001")
    private String followupNo;

    @Schema(description = "随访日期")
    private LocalDate followupDate;

    @Schema(description = "随访类型 1面诊 2电话 3短信 4微信 5线上问卷", example = "1")
    private Integer followupType;

    @Schema(description = "随访内容")
    private String followupContent;

    @Schema(description = "体征数据JSON（体重/身高/BMI等）")
    private String bodyDataJson;

    @Schema(description = "健康变化 1改善 2稳定 3恶化", example = "1")
    private Integer healthChange;

    @Schema(description = "干预意见")
    private String intervention;

    @Schema(description = "用药情况")
    private String medication;

    @Schema(description = "下次随访日期")
    private LocalDate nextFollowupDate;

    @Schema(description = "随访医生ID", example = "1")
    private Long doctorId;

    @Schema(description = "医生姓名", example = "张医生")
    private String doctorName;

    @Schema(description = "附件URL JSON")
    private String attachmentUrls;

}

package cn.iocoder.yudao.module.childhealth.controller.admin.referral.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 转介管理 Response VO")
@Data
public class ReferralRecordRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "转介编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "ZJ202401010001")
    private String referralNo;

    @Schema(description = "学生ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long studentId;

    @Schema(description = "阳性记录ID", example = "1")
    private Long positiveId;

    @Schema(description = "转介原因")
    private String referralReason;

    @Schema(description = "转介项目JSON")
    private String referralItems;

    @Schema(description = "目标医院")
    private String targetHospital;

    @Schema(description = "目标科室")
    private String targetDept;

    @Schema(description = "目标医生")
    private String targetDoctor;

    @Schema(description = "转介状态", example = "0")
    private Integer referralStatus;

    @Schema(description = "反馈内容")
    private String feedbackContent;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
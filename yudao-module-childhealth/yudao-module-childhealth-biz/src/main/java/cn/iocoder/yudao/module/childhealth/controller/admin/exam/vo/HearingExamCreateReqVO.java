package cn.iocoder.yudao.module.childhealth.controller.admin.exam.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 听力检查录入请求 VO
 */
@Schema(description = "听力检查录入请求")
@Data
public class HearingExamCreateReqVO {

    @Schema(description = "体检记录ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long examId;

    @Schema(description = "左耳检查结果")
    private String leftEarResult;

    @Schema(description = "右耳检查结果")
    private String rightEarResult;

    @Schema(description = "听力筛查 通过/未通过")
    private String hearingScreening;

    @Schema(description = "耳廓外观")
    private String earAppearance;

    @Schema(description = "外耳道")
    private String externalAuditory;

    @Schema(description = "鼓膜")
    private String tympanicMembrane;

    @Schema(description = "诊断")
    private String diagnosis;
}
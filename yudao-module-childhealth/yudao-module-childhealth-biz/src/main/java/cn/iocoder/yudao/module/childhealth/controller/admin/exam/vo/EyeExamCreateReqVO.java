package cn.iocoder.yudao.module.childhealth.controller.admin.exam.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 眼保健检查录入请求 VO
 */
@Schema(description = "眼保健检查录入请求")
@Data
public class EyeExamCreateReqVO {

    @Schema(description = "体检记录ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long examId;

    @Schema(description = "左眼视力")
    private String leftVision;

    @Schema(description = "右眼视力")
    private String rightVision;

    @Schema(description = "左眼屈光度")
    private String leftDiopter;

    @Schema(description = "右眼屈光度")
    private String rightDiopter;

    @Schema(description = "眼位")
    private String eyePosition;

    @Schema(description = "外眼检查")
    private String eyeAppearance;

    @Schema(description = "眼底检查")
    private String fundusExam;

    @Schema(description = "诊断")
    private String diagnosis;
}
package cn.iocoder.yudao.module.ppd.controller.admin.screeninformedconsentform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "管理后台 - 知情同意书新增/修改 Request VO")
@Data
public class ScreenInformedConsentFormSaveReqVO {

    @Schema(description = "自增id", requiredMode = Schema.RequiredMode.REQUIRED, example = "10642")
    private Long id;

    @Schema(description = "受筛查学生的id（待筛查人员id）", example = "20072")
    @NotNull
    private Long studentId;

    @Schema(description = "学校", example = "赵六")
    private String schoolName;

    @Schema(description = "班级")
    private String classroom;

    @Schema(description = "是否签署1：是  2：否")
    private Integer isSign;

    @Schema(description = "拒绝签署原因", example = "不对")
    private String reason;

    @Schema(description = "家长签名图片地址")
    @NotBlank(message = "家长签名未提供")
    private String signature;

}
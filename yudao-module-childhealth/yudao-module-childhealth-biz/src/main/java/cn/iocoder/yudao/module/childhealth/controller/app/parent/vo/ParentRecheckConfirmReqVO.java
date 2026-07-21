package cn.iocoder.yudao.module.childhealth.controller.app.parent.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 家长 App - 复筛通知确认 Request VO
 *
 * 创建日期: 2026-07-20
 * 模块: 移动端 - 家长 App
 */
@Schema(description = "家长 App - 复筛通知确认 Request VO")
@Data
public class ParentRecheckConfirmReqVO {

    @Schema(description = "是否确认前往 0否 1是", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotBlank
    private Integer willGo;

    @Schema(description = "确认的复筛日期")
    private String confirmDate;

    @Schema(description = "备注说明")
    private String remark;

}

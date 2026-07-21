package cn.iocoder.yudao.module.childhealth.controller.app.doctor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 医生 App - 审核通过 Request VO
 *
 * 创建日期: 2026-07-20
 * 模块: 移动端 - 医生 App
 */
@Schema(description = "医生 App - 审核通过 Request VO")
@Data
public class DoctorAuditReqVO {

    @Schema(description = "筛查记录ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "5001")
    @NotNull(message = "筛查记录ID不能为空")
    private Long recordId;

    @Schema(description = "审核结论 1通过 2驳回", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "审核结论不能为空")
    private Integer auditResult;

    @Schema(description = "审核医生ID", example = "100")
    private Long auditDoctorId;

    @Schema(description = "审核意见")
    private String auditOpinion;

    @Schema(description = "是否需要复筛 0否 1是", example = "0")
    private Integer needRecheck;

}

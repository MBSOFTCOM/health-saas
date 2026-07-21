package cn.iocoder.yudao.module.childhealth.controller.app.doctor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 医生 App - 随访记录录入 Request VO
 *
 * 创建日期: 2026-07-20
 * 模块: 移动端 - 医生 App
 */
@Schema(description = "医生 App - 随访记录录入 Request VO")
@Data
public class DoctorFollowInputReqVO {

    @Schema(description = "任务ID", example = "6001")
    private Long taskId;

    @Schema(description = "儿童ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2001")
    @NotNull(message = "儿童ID不能为空")
    private Long childId;

    @Schema(description = "随访日期", requiredMode = Schema.RequiredMode.REQUIRED, example = "2026-04-15")
    @NotNull(message = "随访日期不能为空")
    private String followDate;

    @Schema(description = "随访内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "随访内容不能为空")
    private String followContent;

    @Schema(description = "健康变化 1改善 2稳定 3恶化", example = "1")
    private Integer healthChange;

    @Schema(description = "干预意见")
    private String interventionAdvice;

    @Schema(description = "随访医生ID", example = "100")
    private Long doctorId;

    @Schema(description = "下次随访计划日期", example = "2026-05-15")
    private String nextFollowDate;

}

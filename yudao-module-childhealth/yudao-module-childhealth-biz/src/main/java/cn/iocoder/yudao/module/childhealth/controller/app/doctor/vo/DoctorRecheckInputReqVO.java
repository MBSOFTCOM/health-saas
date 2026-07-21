package cn.iocoder.yudao.module.childhealth.controller.app.doctor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 医生 App - 复筛结果录入 Request VO
 *
 * 创建日期: 2026-07-20
 * 模块: 移动端 - 医生 App
 */
@Schema(description = "医生 App - 复筛结果录入 Request VO")
@Data
public class DoctorRecheckInputReqVO {

    @Schema(description = "阳性记录ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2001")
    @NotNull(message = "阳性记录ID不能为空")
    private Long positiveId;

    @Schema(description = "学生ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1001")
    @NotNull(message = "学生ID不能为空")
    private Long studentId;

    @Schema(description = "初筛记录ID", example = "5001")
    private Long initialRecordId;

    @Schema(description = "复筛日期", requiredMode = Schema.RequiredMode.REQUIRED, example = "2026-04-01")
    @NotNull(message = "复筛日期不能为空")
    private String recheckDate;

    @Schema(description = "复筛项目JSON", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "复筛项目不能为空")
    private String recheckItems;

    @Schema(description = "复筛结果JSON", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "复筛结果不能为空")
    private String recheckResult;

    @Schema(description = "是否仍为阳性 0否 1是", example = "0")
    private Integer isStillPositive;

    @Schema(description = "复筛结论")
    private String recheckConclusion;

    @Schema(description = "后续随访计划")
    private String followPlan;

    @Schema(description = "复筛医生ID", example = "100")
    private Long doctorId;

}

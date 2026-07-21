package cn.iocoder.yudao.module.childhealth.controller.app.doctor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 医生 App - 现场筛查录入 Request VO
 *
 * 创建日期: 2026-07-20
 * 模块: 移动端 - 医生 App
 */
@Schema(description = "医生 App - 现场筛查录入 Request VO")
@Data
public class DoctorScreeningInputReqVO {

    @Schema(description = "批次ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    @NotNull(message = "批次ID不能为空")
    private Long batchId;

    @Schema(description = "学生ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1001")
    @NotNull(message = "学生ID不能为空")
    private Long studentId;

    @Schema(description = "筛查日期", requiredMode = Schema.RequiredMode.REQUIRED, example = "2026-03-15")
    @NotNull(message = "筛查日期不能为空")
    private String screeningDate;

    @Schema(description = "五健明细JSON（itemCode-itemValue 数组）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "筛查明细不能为空")
    private String detailJson;

    @Schema(description = "是否阳性（医生现场判断） 0否 1是", example = "0")
    private Integer hasPositive;

    @Schema(description = "阳性项目JSON")
    private String positiveItems;

    @Schema(description = "检查医生ID", example = "100")
    private Long checkerId;

    @Schema(description = "设备编码", example = "DEVICE001")
    private String deviceCode;

    @Schema(description = "备注")
    private String remark;

}

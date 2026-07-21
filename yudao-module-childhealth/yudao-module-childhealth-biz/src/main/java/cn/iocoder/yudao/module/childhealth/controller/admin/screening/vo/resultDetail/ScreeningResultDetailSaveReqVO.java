package cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.resultDetail;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 筛查结果明细新增/修改 Request VO")
@Data
public class ScreeningResultDetailSaveReqVO {

    @Schema(description = "主键ID", example = "1")
    private Long id;

    @Schema(description = "筛查记录ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "筛查记录ID不能为空")
    private Long recordId;

    @Schema(description = "筛查项目编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "VISION_LEFT")
    @NotBlank(message = "筛查项目编码不能为空")
    private String itemCode;

    @Schema(description = "检查结果值", example = "5.0")
    private String itemValue;

    @Schema(description = "是否异常 0正常 1异常", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @NotNull(message = "是否异常不能为空")
    private Integer isAbnormal;

    @Schema(description = "检查科室ID", example = "1")
    private Long deptId;

    @Schema(description = "检查人员ID", example = "1")
    private Long checkerId;

    @Schema(description = "检查时间")
    private LocalDateTime checkTime;

    @Schema(description = "设备编码", example = "DEVICE001")
    private String deviceCode;

}
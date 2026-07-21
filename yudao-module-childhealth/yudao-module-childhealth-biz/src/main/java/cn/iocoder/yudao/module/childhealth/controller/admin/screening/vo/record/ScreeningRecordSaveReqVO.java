package cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.record;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Schema(description = "管理后台 - 筛查记录新增/修改 Request VO")
@Data
public class ScreeningRecordSaveReqVO {

    @Schema(description = "主键ID", example = "1")
    private Long id;

    @Schema(description = "筛查流水号", example = "SCR2024001")
    private String recordNo;

    @Schema(description = "批次ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "批次ID不能为空")
    private Long batchId;

    @Schema(description = "学生ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "学生ID不能为空")
    private Long studentId;

    @Schema(description = "筛查日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "筛查日期不能为空")
    private LocalDate screeningDate;

    @Schema(description = "审核状态 1进行中 2待审核 3已审核", example = "1")
    private Integer checkStatus;

    @Schema(description = "是否有阳性 0否 1是", example = "0")
    private Integer hasPositive;

    @Schema(description = "阳性项目JSON")
    private String positiveItems;

}
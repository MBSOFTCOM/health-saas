package cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.batch;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Schema(description = "管理后台 - 筛查批次新增/修改 Request VO")
@Data
public class ScreeningBatchSaveReqVO {

    @Schema(description = "主键ID", example = "1")
    private Long id;

    @Schema(description = "批次编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "BATCH2024001")
    @NotBlank(message = "批次编号不能为空")
    private String batchNo;

    @Schema(description = "批次名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024年秋季筛查")
    @NotBlank(message = "批次名称不能为空")
    private String batchName;

    @Schema(description = "学年ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "学年ID不能为空")
    private Long yearId;

    @Schema(description = "学校ID", example = "1")
    private Long schoolId;

    @Schema(description = "开始日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "开始日期不能为空")
    private LocalDate startDate;

    @Schema(description = "结束日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "结束日期不能为空")
    private LocalDate endDate;

    @Schema(description = "计划筛查人数", example = "1000")
    private Integer targetCount;

    @Schema(description = "批次状态 1筹备中 2进行中 3已完成", example = "1")
    private Integer batchStatus;

}
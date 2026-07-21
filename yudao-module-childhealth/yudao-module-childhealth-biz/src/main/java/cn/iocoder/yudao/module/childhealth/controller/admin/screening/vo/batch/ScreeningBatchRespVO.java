package cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.batch;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 筛查批次 Response VO")
@Data
public class ScreeningBatchRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "批次编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "BATCH2024001")
    private String batchNo;

    @Schema(description = "批次名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024年秋季筛查")
    private String batchName;

    @Schema(description = "学年ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long yearId;

    @Schema(description = "学校ID", example = "1")
    private Long schoolId;

    @Schema(description = "开始日期", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate startDate;

    @Schema(description = "结束日期", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate endDate;

    @Schema(description = "计划筛查人数", example = "1000")
    private Integer targetCount;

    @Schema(description = "实际筛查人数", example = "950")
    private Integer actualCount;

    @Schema(description = "批次状态 1筹备中 2进行中 3已完成", example = "1")
    private Integer batchStatus;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
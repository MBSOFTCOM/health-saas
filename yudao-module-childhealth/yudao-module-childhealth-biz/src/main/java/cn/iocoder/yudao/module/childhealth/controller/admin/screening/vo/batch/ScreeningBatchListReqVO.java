package cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.batch;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 筛查批次列表 Request VO")
@Data
public class ScreeningBatchListReqVO {

    @Schema(description = "批次编号", example = "BATCH2024001")
    private String batchNo;

    @Schema(description = "批次名称", example = "2024年秋季筛查")
    private String batchName;

    @Schema(description = "学年ID", example = "1")
    private Long yearId;

    @Schema(description = "学校ID", example = "1")
    private Long schoolId;

    @Schema(description = "批次状态 1筹备中 2进行中 3已完成", example = "1")
    private Integer batchStatus;

}
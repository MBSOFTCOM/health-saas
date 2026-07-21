package cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.batch;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

@Schema(description = "管理后台 - 筛查批次分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ScreeningBatchPageReqVO extends PageParam {

    @Schema(description = "批次编号", example = "BATCH2024001")
    private String batchNo;

    @Schema(description = "批次名称", example = "2024年秋季筛查")
    private String batchName;

    @Schema(description = "学年ID", example = "1")
    private Long yearId;

    @Schema(description = "学校ID", example = "1")
    private Long schoolId;

    @Schema(description = "开始日期-开始")
    private LocalDate[] startDate;

    @Schema(description = "结束日期-开始")
    private LocalDate[] endDate;

    @Schema(description = "批次状态 1筹备中 2进行中 3已完成", example = "1")
    private Integer batchStatus;

}
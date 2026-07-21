package cn.iocoder.yudao.module.childhealth.controller.admin.statistics.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * 体检批次查询 Response VO
 *
 * 创建日期: 2026-07-20
 * 模块: 20. 全维度数据统计
 */
@Schema(description = "管理后台 - 体检批次查询 Response VO")
@Data
public class StatisticsBatchRespVO {

    @Schema(description = "批次ID", example = "100")
    private Long batchId;

    @Schema(description = "批次编号", example = "BATCH20260720001")
    private String batchNo;

    @Schema(description = "批次名称", example = "2026年春季视力筛查")
    private String batchName;

    @Schema(description = "学校ID", example = "10")
    private Long schoolId;

    @Schema(description = "学年ID", example = "2026")
    private Long yearId;

    @Schema(description = "开始日期", example = "2026-03-01")
    private LocalDate startDate;

    @Schema(description = "结束日期", example = "2026-06-30")
    private LocalDate endDate;

    @Schema(description = "计划筛查人数", example = "1000")
    private Integer targetCount;

    @Schema(description = "实际筛查人数", example = "950")
    private Integer actualCount;

    @Schema(description = "参检率%", example = "95.00")
    private java.math.BigDecimal participationRate;

    @Schema(description = "批次状态 1筹备中 2进行中 3已完成", example = "3")
    private Integer batchStatus;

}

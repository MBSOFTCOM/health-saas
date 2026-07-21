package cn.iocoder.yudao.module.childhealth.controller.app.doctor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * 医生 App - 待筛查任务列表项 VO
 *
 * 创建日期: 2026-07-20
 * 模块: 移动端 - 医生 App
 */
@Schema(description = "医生 App - 待筛查任务 VO")
@Data
public class DoctorScreeningTaskRespVO {

    @Schema(description = "筛查批次ID", example = "100")
    private Long batchId;

    @Schema(description = "批次编号", example = "BATCH20260315001")
    private String batchNo;

    @Schema(description = "批次名称", example = "2026年春季视力筛查")
    private String batchName;

    @Schema(description = "学校ID", example = "10")
    private Long schoolId;

    @Schema(description = "学校名称", example = "市第一小学")
    private String schoolName;

    @Schema(description = "计划开始日期", example = "2026-03-15")
    private LocalDate startDate;

    @Schema(description = "计划结束日期", example = "2026-03-20")
    private LocalDate endDate;

    @Schema(description = "计划人数", example = "1000")
    private Integer targetCount;

    @Schema(description = "已完成人数", example = "300")
    private Integer actualCount;

    @Schema(description = "批次状态 1筹备中 2进行中 3已完成", example = "2")
    private Integer batchStatus;

}

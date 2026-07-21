package cn.iocoder.yudao.module.childhealth.controller.admin.screeningconfig.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 体检方案与批次关联 Response VO")
@Data
public class ScreeningPlanBatchRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "方案ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long planId;

    @Schema(description = "批次ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    private Long batchId;

    @Schema(description = "学校ID", example = "10")
    private Long schoolId;

    @Schema(description = "年级ID", example = "20")
    private Long gradeId;

    @Schema(description = "班级ID", example = "30")
    private Long classId;

    @Schema(description = "筛查开始日期")
    private LocalDate screeningStart;

    @Schema(description = "筛查结束日期")
    private LocalDate screeningEnd;

    @Schema(description = "计划人数", example = "200")
    private Integer targetCount;

    @Schema(description = "实际人数", example = "180")
    private Integer actualCount;

    @Schema(description = "完成状态 0未开始 1进行中 2已完成", example = "0")
    private Integer completionStatus;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}

package cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.record;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 筛查记录 Response VO")
@Data
public class ScreeningRecordRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "筛查流水号", requiredMode = Schema.RequiredMode.REQUIRED, example = "SCR2024001")
    private String recordNo;

    @Schema(description = "批次ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long batchId;

    @Schema(description = "学生ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long studentId;

    @Schema(description = "筛查日期", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate screeningDate;

    @Schema(description = "审核状态 1进行中 2待审核 3已审核", example = "1")
    private Integer checkStatus;

    @Schema(description = "是否有阳性 0否 1是", example = "0")
    private Integer hasPositive;

    @Schema(description = "阳性项目JSON")
    private String positiveItems;

    @Schema(description = "审核医生ID", example = "1")
    private Long auditDoctor;

    @Schema(description = "审核时间")
    private LocalDateTime auditTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
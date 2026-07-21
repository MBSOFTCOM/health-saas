package cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.recheck;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 复筛记录 Response VO")
@Data
public class RecheckRecordRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "关联阳性记录ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long positiveId;

    @Schema(description = "学生ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long studentId;

    @Schema(description = "初筛记录ID", example = "1")
    private Long initialRecordId;

    @Schema(description = "复筛日期")
    private LocalDate recheckDate;

    @Schema(description = "复筛项目JSON")
    private String recheckItems;

    @Schema(description = "复筛结果JSON")
    private String recheckResult;

    @Schema(description = "是否仍为阳性 0否 1是", example = "0")
    private Integer isStillPositive;

    @Schema(description = "复筛结论")
    private String recheckConclusion;

    @Schema(description = "后续随访计划")
    private String followPlan;

    @Schema(description = "医生ID", example = "1")
    private Long doctorId;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
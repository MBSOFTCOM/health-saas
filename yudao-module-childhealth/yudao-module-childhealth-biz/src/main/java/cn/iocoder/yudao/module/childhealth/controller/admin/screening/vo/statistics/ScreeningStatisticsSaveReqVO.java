package cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.statistics;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "管理后台 - 筛查统计新增/修改 Request VO")
@Data
public class ScreeningStatisticsSaveReqVO {

    @Schema(description = "主键ID", example = "1")
    private Long id;

    @Schema(description = "统计日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "统计日期不能为空")
    private LocalDate statDate;

    @Schema(description = "批次ID", example = "1")
    private Long batchId;

    @Schema(description = "学校ID", example = "1")
    private Long schoolId;

    @Schema(description = "年级ID", example = "1")
    private Long gradeId;

    @Schema(description = "目标人数", example = "1000")
    private Integer targetCount;

    @Schema(description = "实际人数", example = "950")
    private Integer actualCount;

    @Schema(description = "参与率（百分比）", example = "95.00")
    private BigDecimal participationRate;

    @Schema(description = "阳性人数", example = "50")
    private Integer positiveCount;

    @Schema(description = "阳性率（百分比）", example = "5.26")
    private BigDecimal positiveRate;

    @Schema(description = "复筛人数", example = "30")
    private Integer recheckCount;

    @Schema(description = "复筛率（百分比）", example = "60.00")
    private BigDecimal recheckRate;

    @Schema(description = "随访人数", example = "20")
    private Integer followCount;

    @Schema(description = "随访率（百分比）", example = "40.00")
    private BigDecimal followRate;

}
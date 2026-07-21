package cn.iocoder.yudao.module.childhealth.controller.admin.ops.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 运营指标快照 Response VO
 *
 * 创建日期: 2026-07-20
 */
@Schema(description = "管理后台 - 运营指标快照 Response VO")
@Data
public class OpsIndicatorSnapshotRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "快照日期", example = "2026-07-20")
    private LocalDate snapshotDate;

    @Schema(description = "批次ID", example = "100")
    private Long batchId;

    @Schema(description = "学校ID", example = "10")
    private Long schoolId;

    @Schema(description = "年级ID", example = "100")
    private Long gradeId;

    @Schema(description = "行政区划代码", example = "440100")
    private String regionCode;

    @Schema(description = "参检率%", example = "95.50")
    private BigDecimal participationRate;

    @Schema(description = "阳性率%", example = "12.30")
    private BigDecimal positiveRate;

    @Schema(description = "复筛率%", example = "60.00")
    private BigDecimal recheckRate;

    @Schema(description = "随访率%", example = "85.00")
    private BigDecimal followRate;

    @Schema(description = "随访完成率%", example = "70.00")
    private BigDecimal followCompleteRate;

    @Schema(description = "筛查总人数", example = "1000")
    private Integer screeningCount;

    @Schema(description = "应检人数", example = "1050")
    private Integer targetCount;

    @Schema(description = "实检人数", example = "1000")
    private Integer actualCount;

    @Schema(description = "阳性人数", example = "123")
    private Integer positiveCount;

    @Schema(description = "复筛人数", example = "74")
    private Integer recheckCount;

    @Schema(description = "随访人数", example = "105")
    private Integer followCount;

    @Schema(description = "待审核数", example = "20")
    private Integer pendingAuditCount;

    @Schema(description = "待复筛数", example = "30")
    private Integer pendingRecheckCount;

    @Schema(description = "待随访数", example = "15")
    private Integer pendingFollowCount;

    @Schema(description = "五健专项分类统计JSON")
    private String categoryStats;

    @Schema(description = "扩展指标JSON")
    private String extraIndicators;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}

package cn.iocoder.yudao.module.childhealth.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 体检批次响应VO
 */
@Schema(description = "体检批次响应")
@Data
public class CheckupBatchRespVO {

    @Schema(description = "批次ID")
    private Long batchId;

    @Schema(description = "批次编号")
    private String batchNo;

    @Schema(description = "批次名称")
    private String batchName;

    @Schema(description = "年份ID")
    private Long yearId;

    @Schema(description = "学校ID")
    private Long schoolId;

    @Schema(description = "学校名称")
    private String schoolName;

    @Schema(description = "区域编码")
    private String regionCode;

    @Schema(description = "区域名称")
    private String regionName;

    @Schema(description = "开始日期")
    private LocalDate startDate;

    @Schema(description = "结束日期")
    private LocalDate endDate;

    @Schema(description = "目标人数")
    private Integer targetCount;

    @Schema(description = "实际人数")
    private Integer actualCount;

    @Schema(description = "完成率")
    private BigDecimal completionRate;

    @Schema(description = "阳性人数")
    private Integer positiveCount;

    @Schema(description = "阳性率")
    private BigDecimal positiveRate;

    @Schema(description = "批次状态：1-进行中，2-已完成，3-已归档")
    private Integer batchStatus;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
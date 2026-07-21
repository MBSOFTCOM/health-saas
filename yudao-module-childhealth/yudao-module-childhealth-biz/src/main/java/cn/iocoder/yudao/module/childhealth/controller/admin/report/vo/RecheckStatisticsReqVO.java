package cn.iocoder.yudao.module.childhealth.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * 复筛数据统计请求VO
 */
@Schema(description = "复筛数据统计请求")
@Data
public class RecheckStatisticsReqVO {

    @Schema(description = "批次ID")
    private Long batchId;

    @Schema(description = "区域编码")
    private String regionCode;

    @Schema(description = "学校ID")
    private Long schoolId;

    @Schema(description = "开始日期")
    private LocalDate startDate;

    @Schema(description = "结束日期")
    private LocalDate endDate;

    @Schema(description = "复筛状态：1-待复筛，2-复筛中，3-已完成")
    private Integer recheckStatus;
}
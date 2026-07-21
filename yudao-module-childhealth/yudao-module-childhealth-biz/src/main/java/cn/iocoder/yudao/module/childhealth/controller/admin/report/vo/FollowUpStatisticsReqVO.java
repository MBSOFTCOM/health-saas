package cn.iocoder.yudao.module.childhealth.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * 随访数据统计请求VO
 */
@Schema(description = "随访数据统计请求")
@Data
public class FollowUpStatisticsReqVO {

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

    @Schema(description = "随访状态：1-待随访，2-随访中，3-已完成")
    private Integer followStatus;

    @Schema(description = "随访类型：1-门诊随访，2-电话随访，3-上门随访")
    private Integer followType;
}
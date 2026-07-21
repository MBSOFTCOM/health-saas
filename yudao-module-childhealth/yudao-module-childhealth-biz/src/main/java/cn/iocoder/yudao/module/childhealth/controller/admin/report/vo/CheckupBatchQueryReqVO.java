package cn.iocoder.yudao.module.childhealth.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * 体检批次查询请求VO
 */
@Schema(description = "体检批次查询请求")
@Data
public class CheckupBatchQueryReqVO {

    @Schema(description = "批次编号（模糊查询）")
    private String batchNo;

    @Schema(description = "批次名称（模糊查询）")
    private String batchName;

    @Schema(description = "学校ID")
    private Long schoolId;

    @Schema(description = "区域编码")
    private String regionCode;

    @Schema(description = "批次状态：1-进行中，2-已完成，3-已归档")
    private Integer batchStatus;

    @Schema(description = "开始日期起")
    private LocalDate startDateFrom;

    @Schema(description = "开始日期止")
    private LocalDate startDateTo;

    @Schema(description = "结束日期起")
    private LocalDate endDateFrom;

    @Schema(description = "结束日期止")
    private LocalDate endDateTo;

    @Schema(description = "页码，从1开始")
    private Integer pageNo = 1;

    @Schema(description = "每页条数")
    private Integer pageSize = 20;
}
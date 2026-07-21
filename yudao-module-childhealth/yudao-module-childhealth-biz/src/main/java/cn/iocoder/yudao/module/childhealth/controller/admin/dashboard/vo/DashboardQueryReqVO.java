package cn.iocoder.yudao.module.childhealth.controller.admin.dashboard.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * 看板查询条件 Request VO
 *
 * 创建日期: 2026-07-20
 * 模块: 1. 数据看板
 */
@Schema(description = "管理后台 - 看板查询条件 Request VO")
@Data
public class DashboardQueryReqVO {

    @Schema(description = "批次ID", example = "100")
    private Long batchId;

    @Schema(description = "学校ID", example = "10")
    private Long schoolId;

    @Schema(description = "年级ID", example = "100")
    private Long gradeId;

    @Schema(description = "行政区划代码", example = "440100")
    private String regionCode;

    @Schema(description = "开始日期")
    private LocalDate startDate;

    @Schema(description = "结束日期")
    private LocalDate endDate;

}

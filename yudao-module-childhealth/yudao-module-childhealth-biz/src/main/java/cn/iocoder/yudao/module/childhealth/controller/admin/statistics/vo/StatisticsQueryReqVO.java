package cn.iocoder.yudao.module.childhealth.controller.admin.statistics.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * 统计查询条件 Request VO
 *
 * 创建日期: 2026-07-20
 * 模块: 20. 全维度数据统计
 */
@Schema(description = "管理后台 - 统计查询条件 Request VO")
@Data
public class StatisticsQueryReqVO {

    @Schema(description = "批次ID", example = "100")
    private Long batchId;

    @Schema(description = "学校ID", example = "10")
    private Long schoolId;

    @Schema(description = "年级ID", example = "100")
    private Long gradeId;

    @Schema(description = "行政区划代码", example = "440100")
    private String regionCode;

    @Schema(description = "项目编码 VISION/ORAL/BONE/PSYCHOLOGICAL/SHAPE", example = "VISION")
    private String itemCode;

    @Schema(description = "开始日期")
    private LocalDate startDate;

    @Schema(description = "结束日期")
    private LocalDate endDate;

}

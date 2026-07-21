package cn.iocoder.yudao.module.childhealth.controller.admin.statistics.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 工作量统计 Response VO
 *
 * 创建日期: 2026-07-20
 * 模块: 20. 全维度数据统计
 * 用途: 机构/医生/学校维度工作量聚合
 */
@Schema(description = "管理后台 - 工作量统计 Response VO")
@Data
public class StatisticsWorkloadRespVO {

    @Schema(description = "维度编码（医护ID/机构ID/学校ID）", example = "100")
    private String dimensionCode;

    @Schema(description = "维度名称（医护姓名/机构名称/学校名称）", example = "李医生")
    private String dimensionName;

    @Schema(description = "维度类型 STAFF/ORG/SCHOOL", example = "STAFF")
    private String dimensionType;

    @Schema(description = "统计日期", example = "2026-07-20")
    private String statDate;

    @Schema(description = "筛查数", example = "80")
    private Integer screeningCount;

    @Schema(description = "审核数", example = "70")
    private Integer auditCount;

    @Schema(description = "随访数", example = "30")
    private Integer followCount;

    @Schema(description = "复筛数", example = "10")
    private Integer recheckCount;

    @Schema(description = "转介数", example = "5")
    private Integer referralCount;

    @Schema(description = "专案创建数", example = "2")
    private Integer caseCreateCount;

    @Schema(description = "专案结案数", example = "1")
    private Integer caseCloseCount;

    @Schema(description = "总工作量(加权得分)", example = "256.50")
    private BigDecimal totalWorkload;

}

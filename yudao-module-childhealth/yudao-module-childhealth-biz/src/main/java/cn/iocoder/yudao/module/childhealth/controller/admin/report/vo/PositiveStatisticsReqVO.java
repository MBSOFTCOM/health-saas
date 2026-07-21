package cn.iocoder.yudao.module.childhealth.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * 阳性数据统计请求VO
 */
@Schema(description = "阳性数据统计请求")
@Data
public class PositiveStatisticsReqVO {

    @Schema(description = "批次ID")
    private Long batchId;

    @Schema(description = "区域编码")
    private String regionCode;

    @Schema(description = "学校ID")
    private Long schoolId;

    @Schema(description = "年级ID")
    private Long gradeId;

    @Schema(description = "年龄开始")
    private Integer ageStart;

    @Schema(description = "年龄结束")
    private Integer ageEnd;

    @Schema(description = "性别：1-男，2-女")
    private Integer gender;

    @Schema(description = "疾病编码")
    private String diseaseCode;

    @Schema(description = "开始日期")
    private LocalDate startDate;

    @Schema(description = "结束日期")
    private LocalDate endDate;

    @Schema(description = "统计维度：region-按区域，age-按年龄，disease-按疾病")
    private String dimension;
}
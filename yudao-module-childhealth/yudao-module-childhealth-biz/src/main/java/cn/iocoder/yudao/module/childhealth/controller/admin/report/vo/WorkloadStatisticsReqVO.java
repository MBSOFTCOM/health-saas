package cn.iocoder.yudao.module.childhealth.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * 工作量统计请求VO
 */
@Schema(description = "工作量统计请求")
@Data
public class WorkloadStatisticsReqVO {

    @Schema(description = "机构ID")
    private Long orgId;

    @Schema(description = "医生ID")
    private Long doctorId;

    @Schema(description = "学校ID")
    private Long schoolId;

    @Schema(description = "开始日期")
    private LocalDate startDate;

    @Schema(description = "结束日期")
    private LocalDate endDate;

    @Schema(description = "统计维度：org-按机构，doctor-按医生，school-按学校")
    private String dimension;

    @Schema(description = "工作类型：screening-筛查，audit-审核，follow-随访，all-全部")
    private String workType;
}
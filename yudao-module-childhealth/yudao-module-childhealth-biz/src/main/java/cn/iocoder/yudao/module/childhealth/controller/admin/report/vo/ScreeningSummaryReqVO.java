package cn.iocoder.yudao.module.childhealth.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * 筛查总表请求VO
 */
@Schema(description = "筛查总表请求")
@Data
public class ScreeningSummaryReqVO {

    @Schema(description = "批次ID")
    private Long batchId;

    @Schema(description = "区域编码")
    private String regionCode;

    @Schema(description = "学校ID")
    private Long schoolId;

    @Schema(description = "学生姓名（模糊查询）")
    private String studentName;

    @Schema(description = "年级ID")
    private Long gradeId;

    @Schema(description = "班级ID")
    private Long classId;

    @Schema(description = "筛查状态：1-待审核，2-审核中，3-已审核")
    private Integer checkStatus;

    @Schema(description = "是否有阳性：true-是，false-否")
    private Boolean hasPositive;

    @Schema(description = "开始日期")
    private LocalDate startDate;

    @Schema(description = "结束日期")
    private LocalDate endDate;

    @Schema(description = "页码，从1开始")
    private Integer pageNo = 1;

    @Schema(description = "每页条数")
    private Integer pageSize = 20;
}
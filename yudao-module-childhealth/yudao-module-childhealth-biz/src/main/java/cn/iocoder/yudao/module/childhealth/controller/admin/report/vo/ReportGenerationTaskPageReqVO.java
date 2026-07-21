package cn.iocoder.yudao.module.childhealth.controller.admin.report.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
 * 报告生成任务分页 Request VO
 *
 * 创建日期: 2026-07-20
 */
@Schema(description = "管理后台 - 报告生成任务分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ReportGenerationTaskPageReqVO extends PageParam {

    @Schema(description = "任务编号", example = "RG20260720001")
    private String taskNo;

    @Schema(description = "模板ID", example = "1")
    private Long templateId;

    @Schema(description = "报告类型 1学生 2学校 3年级 4区域", example = "1")
    private Integer reportType;

    @Schema(description = "任务状态 0待生成 1生成中 2成功 3失败 4已取消", example = "0")
    private Integer taskStatus;

    @Schema(description = "批次ID", example = "100")
    private Long batchId;

    @Schema(description = "开始创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime createTimeStart;

    @Schema(description = "结束创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime createTimeEnd;

}

package cn.iocoder.yudao.module.childhealth.controller.app.parent.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * 家长 App - 随访任务 VO
 *
 * 创建日期: 2026-07-20
 * 模块: 移动端 - 家长 App
 */
@Schema(description = "家长 App - 随访任务 VO")
@Data
public class ParentFollowTaskRespVO {

    @Schema(description = "任务ID", example = "6001")
    private Long taskId;

    @Schema(description = "任务编号", example = "FT20260320001")
    private String taskNo;

    @Schema(description = "儿童ID", example = "2001")
    private Long childId;

    @Schema(description = "任务类型", example = "1")
    private Integer taskType;

    @Schema(description = "任务来源", example = "阳性随访")
    private String taskSource;

    @Schema(description = "任务内容")
    private String taskContent;

    @Schema(description = "优先级 1高 2中 3低", example = "2")
    private Integer priority;

    @Schema(description = "计划日期", example = "2026-04-15")
    private LocalDate planDate;

    @Schema(description = "责任医生ID", example = "100")
    private Long doctorId;

    @Schema(description = "任务状态 0待随访 1已完成 2已逾期", example = "0")
    private Integer taskStatus;

}

package cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.recheck;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
 * 复筛随访时间轴分页 Request VO
 *
 * 创建日期: 2026-07-20
 */
@Schema(description = "管理后台 - 复筛随访时间轴分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RecheckFollowTimelinePageReqVO extends PageParam {

    @Schema(description = "阳性记录ID", example = "100")
    private Long positiveId;

    @Schema(description = "学生ID", example = "1001")
    private Long studentId;

    @Schema(description = "事件类型 NOTIFY/RECHECK/FOLLOW/TRANSFER/CLOSE/CANCEL", example = "NOTIFY")
    private String eventType;

    @Schema(description = "开始事件时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime eventTimeStart;

    @Schema(description = "结束事件时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime eventTimeEnd;

}

package cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.rule;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
 * 阳性规则执行日志分页 Request VO
 *
 * 创建日期: 2026-07-20
 */
@Schema(description = "管理后台 - 阳性规则执行日志分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PositiveRuleExecLogPageReqVO extends PageParam {

    @Schema(description = "规则ID", example = "100")
    private Long ruleId;

    @Schema(description = "筛查记录ID", example = "200")
    private Long recordId;

    @Schema(description = "学生ID", example = "1001")
    private Long studentId;

    @Schema(description = "阳性等级 1轻度 2中度 3重度", example = "1")
    private Integer positiveLevel;

    @Schema(description = "是否判定为阳性 0否 1是", example = "1")
    private Integer isPositive;

    @Schema(description = "开始命中时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime matchedTimeStart;

    @Schema(description = "结束命中时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime matchedTimeEnd;

}

package cn.iocoder.yudao.module.childhealth.controller.admin.integration.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
 * 数据上报失败日志分页 Request VO
 *
 * 创建日期: 2026-07-20
 */
@Schema(description = "管理后台 - 数据上报失败日志分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DataPushFailLogPageReqVO extends PageParam {

    @Schema(description = "任务ID", example = "100")
    private Long taskId;

    @Schema(description = "错误类型 NETWORK/AUTH/BUSINESS/TIMEOUT", example = "TIMEOUT")
    private String errorType;

    @Schema(description = "错误码", example = "500")
    private String errorCode;

    @Schema(description = "开始失败时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime failTimeStart;

    @Schema(description = "结束失败时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime failTimeEnd;

}

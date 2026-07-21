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
 * 复筛通知记录分页 Request VO
 *
 * 创建日期: 2026-07-20
 */
@Schema(description = "管理后台 - 复筛通知记录分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RecheckNotifyRecordPageReqVO extends PageParam {

    @Schema(description = "阳性记录ID", example = "100")
    private Long positiveId;

    @Schema(description = "学生ID", example = "1001")
    private Long studentId;

    @Schema(description = "学校ID", example = "10")
    private Long schoolId;

    @Schema(description = "班级ID", example = "100")
    private Long classId;

    @Schema(description = "通知渠道 1短信 2微信 3APP 4电话 5纸质", example = "1")
    private Integer notifyChannel;

    @Schema(description = "通知类型 1首次 2催促 3变更 4取消", example = "1")
    private Integer notifyType;

    @Schema(description = "通知状态 0待发送 1成功 2失败 3已读 4已响应", example = "1")
    private Integer notifyStatus;

    @Schema(description = "开始通知时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime notifyTimeStart;

    @Schema(description = "结束通知时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime notifyTimeEnd;

}

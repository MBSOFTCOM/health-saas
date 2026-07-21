package cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.recheck;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 复筛通知记录 Response VO
 *
 * 创建日期: 2026-07-20
 */
@Schema(description = "管理后台 - 复筛通知记录 Response VO")
@Data
public class RecheckNotifyRecordRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "阳性记录ID", example = "100")
    private Long positiveId;

    @Schema(description = "复筛记录ID", example = "200")
    private Long recheckId;

    @Schema(description = "学生ID", example = "1001")
    private Long studentId;

    @Schema(description = "学校ID", example = "10")
    private Long schoolId;

    @Schema(description = "班级ID", example = "100")
    private Long classId;

    @Schema(description = "通知渠道 1短信 2微信 3APP 4电话 5纸质", example = "1")
    private Integer notifyChannel;

    @Schema(description = "通知类型 1首次通知 2催促 3变更 4取消", example = "1")
    private Integer notifyType;

    @Schema(description = "通知时间")
    private LocalDateTime notifyTime;

    @Schema(description = "通知内容")
    private String notifyContent;

    @Schema(description = "通知状态 0待发送 1成功 2失败 3已读 4已响应", example = "1")
    private Integer notifyStatus;

    @Schema(description = "关联message_push_log.id", example = "500")
    private Long pushLogId;

    @Schema(description = "响应人", example = "张三")
    private String responder;

    @Schema(description = "响应时间")
    private LocalDateTime responseTime;

    @Schema(description = "响应内容")
    private String responseContent;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}

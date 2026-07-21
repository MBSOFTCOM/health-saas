package cn.iocoder.yudao.module.childhealth.controller.app.parent.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 家长 App - 复筛通知列表项 VO
 *
 * 创建日期: 2026-07-20
 * 模块: 移动端 - 家长 App
 */
@Schema(description = "家长 App - 复筛通知 VO")
@Data
public class ParentRecheckNotifyRespVO {

    @Schema(description = "通知ID", example = "3001")
    private Long notifyId;

    @Schema(description = "阳性记录ID", example = "2001")
    private Long positiveId;

    @Schema(description = "复筛记录ID", example = "4001")
    private Long recheckId;

    @Schema(description = "学生ID", example = "1001")
    private Long studentId;

    @Schema(description = "通知渠道 1短信 2微信 3APP 4电话 5纸质通知单", example = "3")
    private Integer notifyChannel;

    @Schema(description = "通知类型 1首次通知 2催促 3变更 4取消", example = "1")
    private Integer notifyType;

    @Schema(description = "通知时间")
    private LocalDateTime notifyTime;

    @Schema(description = "通知内容")
    private String notifyContent;

    @Schema(description = "通知状态 0待发送 1成功 2失败 3已读 4已响应", example = "1")
    private Integer notifyStatus;

    @Schema(description = "建议复筛日期")
    private String suggestDate;

    @Schema(description = "建议复筛医院")
    private String suggestHospital;

    @Schema(description = "建议复筛科室")
    private String suggestDepartment;

}

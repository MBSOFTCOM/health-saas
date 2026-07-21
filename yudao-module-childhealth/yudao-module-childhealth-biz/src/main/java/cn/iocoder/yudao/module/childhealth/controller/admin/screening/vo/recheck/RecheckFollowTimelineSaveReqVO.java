package cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.recheck;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 复筛随访时间轴新增/修改 Request VO
 *
 * 创建日期: 2026-07-20
 */
@Schema(description = "管理后台 - 复筛随访时间轴新增/修改 Request VO")
@Data
public class RecheckFollowTimelineSaveReqVO {

    @Schema(description = "主键ID", example = "1")
    private Long id;

    @Schema(description = "阳性记录ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    private Long positiveId;

    @Schema(description = "学生ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1001")
    private Long studentId;

    @Schema(description = "事件类型 NOTIFY/RECHECK/FOLLOW/TRANSFER/CLOSE/CANCEL", requiredMode = Schema.RequiredMode.REQUIRED, example = "NOTIFY")
    private String eventType;

    @Schema(description = "事件时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime eventTime;

    @Schema(description = "事件标题", example = "已发送复筛通知")
    private String eventTitle;

    @Schema(description = "事件内容")
    private String eventContent;

    @Schema(description = "操作人ID", example = "1")
    private Long operatorId;

    @Schema(description = "操作人姓名", example = "李医生")
    private String operatorName;

    @Schema(description = "关联记录ID（多态）", example = "500")
    private Long relatedId;

    @Schema(description = "关联表名", example = "message_push_log")
    private String relatedTable;

    @Schema(description = "备注")
    private String remark;

}

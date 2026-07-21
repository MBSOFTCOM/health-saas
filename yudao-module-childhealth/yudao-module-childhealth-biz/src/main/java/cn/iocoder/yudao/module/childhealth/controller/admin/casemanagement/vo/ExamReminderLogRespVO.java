package cn.iocoder.yudao.module.childhealth.controller.admin.casemanagement.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 体检催检推送日志 Response VO")
@Data
public class ExamReminderLogRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "规则ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long ruleId;

    @Schema(description = "儿童ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long childId;

    @Schema(description = "推送渠道")
    private String pushChannel;

    @Schema(description = "推送时间")
    private LocalDateTime pushTime;

    @Schema(description = "推送内容")
    private String pushContent;

    @Schema(description = "状态 0待发送 1成功 2失败 3已取消 4已补发", example = "0")
    private Integer pushStatus;

    @Schema(description = "失败原因")
    private String failReason;

    @Schema(description = "重试次数", example = "0")
    private Integer retryCount;

    @Schema(description = "响应数据")
    private String responseData;

    @Schema(description = "阅读时间")
    private LocalDateTime readTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}

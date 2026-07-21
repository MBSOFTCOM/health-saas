package cn.iocoder.yudao.module.childhealth.controller.admin.integration.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 数据上报任务新增/修改 Request VO
 *
 * 创建日期: 2026-07-20
 */
@Schema(description = "管理后台 - 数据上报任务新增/修改 Request VO")
@Data
public class DataPushTaskSaveReqVO {

    @Schema(description = "主键ID", example = "1")
    private Long id;

    @Schema(description = "任务编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "PT20260720001")
    private String taskNo;

    @Schema(description = "目标系统 HIS/LIS/WCHB/WSJW/EDU", requiredMode = Schema.RequiredMode.REQUIRED, example = "HIS")
    private String targetSystem;

    @Schema(description = "目标机构名称", example = "市儿童医院")
    private String targetOrg;

    @Schema(description = "业务类型 SCREENING/REFERRAL/FOLLOW/RECHECK", requiredMode = Schema.RequiredMode.REQUIRED, example = "SCREENING")
    private String businessType;

    @Schema(description = "关联筛查批次ID", example = "100")
    private Long batchId;

    @Schema(description = "业务数据ID列表JSON")
    private String dataIds;

    @Schema(description = "序列化后的数据(XML/JSON)")
    private String dataPayload;

    @Schema(description = "数据格式 JSON/XML/EXCEL", example = "JSON")
    private String dataFormat;

    @Schema(description = "推送模式 1手动 2自动", example = "1")
    private Integer pushMode;

    @Schema(description = "优先级 1高 2中 3低", example = "2")
    private Integer priority;

    @Schema(description = "0待推送 1推送中 2成功 3失败 4部分成功 5已取消", example = "0")
    private Integer pushStatus;

    @Schema(description = "已重试次数", example = "0")
    private Integer retryCount;

    @Schema(description = "最大重试次数", example = "3")
    private Integer maxRetry;

    @Schema(description = "下次重试时间")
    private LocalDateTime nextRetryTime;

    @Schema(description = "错误信息")
    private String errorMsg;

    @Schema(description = "第三方回执ID", example = "EXT20260720001")
    private String requestId;

    @Schema(description = "第三方响应数据")
    private String responseData;

    @Schema(description = "完成时间")
    private LocalDateTime finishTime;

}

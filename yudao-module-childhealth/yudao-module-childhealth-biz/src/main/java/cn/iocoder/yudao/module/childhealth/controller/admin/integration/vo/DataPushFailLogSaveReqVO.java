package cn.iocoder.yudao.module.childhealth.controller.admin.integration.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 数据上报失败日志新增/修改 Request VO
 *
 * 创建日期: 2026-07-20
 */
@Schema(description = "管理后台 - 数据上报失败日志新增/修改 Request VO")
@Data
public class DataPushFailLogSaveReqVO {

    @Schema(description = "主键ID", example = "1")
    private Long id;

    @Schema(description = "任务ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    private Long taskId;

    @Schema(description = "尝试次数", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer attemptNo;

    @Schema(description = "错误码", example = "TIMEOUT")
    private String errorCode;

    @Schema(description = "错误信息")
    private String errorMsg;

    @Schema(description = "错误类型 NETWORK/AUTH/BUSINESS/TIMEOUT", example = "TIMEOUT")
    private String errorType;

    @Schema(description = "请求报文")
    private String requestPayload;

    @Schema(description = "响应报文")
    private String responsePayload;

    @Schema(description = "HTTP状态码", example = "500")
    private Integer httpStatus;

    @Schema(description = "耗时(毫秒)", example = "30000")
    private Integer costTimeMs;

    @Schema(description = "失败时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime failTime;

}

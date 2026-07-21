package cn.iocoder.yudao.module.childhealth.controller.admin.casemanagement.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 体检提醒模板 Response VO")
@Data
public class ExamReminderTemplateRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "模板编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "TPL_001")
    private String templateCode;

    @Schema(description = "模板名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "满月体检提醒模板")
    private String templateName;

    @Schema(description = "模板内容（支持变量占位符）")
    private String templateContent;

    @Schema(description = "模板类型 1满月 2常规 3入园入托 4专案 5科室自定义", example = "1")
    private Integer templateType;

    @Schema(description = "变量定义JSON")
    private String variablesJson;

    @Schema(description = "科室ID", example = "1")
    private Long deptId;

    @Schema(description = "推送渠道 sms/wechat/app", example = "sms")
    private String channel;

    @Schema(description = "状态 1启用 0停用", example = "1")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}

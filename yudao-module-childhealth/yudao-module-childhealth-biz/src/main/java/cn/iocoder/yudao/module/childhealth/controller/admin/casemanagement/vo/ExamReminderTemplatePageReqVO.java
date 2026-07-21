package cn.iocoder.yudao.module.childhealth.controller.admin.casemanagement.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 体检提醒模板分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ExamReminderTemplatePageReqVO extends PageParam {

    @Schema(description = "模板编码", example = "TPL_001")
    private String templateCode;

    @Schema(description = "模板名称", example = "满月体检提醒模板")
    private String templateName;

    @Schema(description = "模板类型 1满月 2常规 3入园入托 4专案 5科室自定义", example = "1")
    private Integer templateType;

    @Schema(description = "科室ID", example = "1")
    private Long deptId;

    @Schema(description = "推送渠道 sms/wechat/app", example = "sms")
    private String channel;

    @Schema(description = "状态 1启用 0停用", example = "1")
    private Integer status;

}

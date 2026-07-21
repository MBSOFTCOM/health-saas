package cn.iocoder.yudao.module.childhealth.controller.admin.medical.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 病历模板分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MedicalRecordTemplatePageReqVO extends PageParam {

    @Schema(description = "模板编码", example = "EYE-01M")
    private String templateCode;

    @Schema(description = "模板名称", example = "满月眼保健病历模板")
    private String templateName;

    @Schema(description = "模板类型 GENERAL_CHECKUP/EYE_HEALTH/HEARING_HEALTH/ORAL_HEALTH/ENTRY_EXAM", example = "EYE_HEALTH")
    private String templateType;

    @Schema(description = "状态 1启用 0停用", example = "1")
    private Integer status;

}

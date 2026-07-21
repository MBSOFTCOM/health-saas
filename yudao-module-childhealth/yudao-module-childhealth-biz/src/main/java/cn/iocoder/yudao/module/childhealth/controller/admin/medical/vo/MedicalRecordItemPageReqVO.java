package cn.iocoder.yudao.module.childhealth.controller.admin.medical.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 病历结构化字段值分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MedicalRecordItemPageReqVO extends PageParam {

    @Schema(description = "病历ID", example = "100")
    private Long recordId;

    @Schema(description = "模板ID", example = "10")
    private Long templateId;

    @Schema(description = "字段编码", example = "HEIGHT")
    private String fieldCode;

    @Schema(description = "字段名称", example = "身高")
    private String fieldName;

    @Schema(description = "字段类型 text/number/select/date/json", example = "number")
    private String fieldType;

    @Schema(description = "是否异常 0正常 1异常", example = "0")
    private Integer isAbnormal;

}

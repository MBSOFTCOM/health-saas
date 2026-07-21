package cn.iocoder.yudao.module.childhealth.controller.admin.screeningconfig.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 问卷定义分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class QuestionnaireDefinitionPageReqVO extends PageParam {

    @Schema(description = "问卷编码", example = "Q001")
    private String code;

    @Schema(description = "问卷名称", example = "学龄前儿童健康问卷")
    private String name;

    @Schema(description = "类型 1筛查问卷 2随访问卷 3健康问卷", example = "1")
    private Integer type;

    @Schema(description = "版本", example = "v1.0")
    private String version;

    @Schema(description = "状态 1启用 0停用", example = "1")
    private Integer status;

}

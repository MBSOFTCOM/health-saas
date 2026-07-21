package cn.iocoder.yudao.module.childhealth.controller.admin.screeningconfig.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Schema(description = "管理后台 - 问卷定义新增/修改 Request VO")
@Data
public class QuestionnaireDefinitionSaveReqVO {

    @Schema(description = "主键ID", example = "1")
    private Long id;

    @Schema(description = "问卷编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "Q001")
    @NotBlank(message = "问卷编码不能为空")
    private String code;

    @Schema(description = "问卷名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "学龄前儿童健康问卷")
    @NotBlank(message = "问卷名称不能为空")
    private String name;

    @Schema(description = "类型 1筛查问卷 2随访问卷 3健康问卷", example = "1")
    private Integer type;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "题目JSON")
    private String questionsJson;

    @Schema(description = "填写规则JSON")
    private String rulesJson;

    @Schema(description = "最小适用月龄", example = "36")
    private Integer applicableAgeMin;

    @Schema(description = "最大适用月龄", example = "144")
    private Integer applicableAgeMax;

    @Schema(description = "版本", example = "v1.0")
    private String version;

    @Schema(description = "状态 1启用 0停用", example = "1")
    private Integer status;

}

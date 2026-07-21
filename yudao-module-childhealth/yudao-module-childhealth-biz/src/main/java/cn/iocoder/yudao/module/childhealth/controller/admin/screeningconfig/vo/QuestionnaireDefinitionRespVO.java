package cn.iocoder.yudao.module.childhealth.controller.admin.screeningconfig.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 问卷定义 Response VO")
@Data
public class QuestionnaireDefinitionRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "问卷编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "Q001")
    private String code;

    @Schema(description = "问卷名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "学龄前儿童健康问卷")
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

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}

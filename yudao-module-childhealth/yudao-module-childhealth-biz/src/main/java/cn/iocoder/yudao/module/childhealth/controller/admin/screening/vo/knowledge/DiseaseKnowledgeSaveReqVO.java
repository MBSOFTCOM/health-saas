package cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.knowledge;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Schema(description = "管理后台 - 疾病知识库新增/修改 Request VO")
@Data
public class DiseaseKnowledgeSaveReqVO {

    @Schema(description = "主键ID", example = "1")
    private Long id;

    @Schema(description = "疾病编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "J00.0")
    @NotBlank(message = "疾病编码不能为空")
    private String diseaseCode;

    @Schema(description = "疾病名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "近视")
    @NotBlank(message = "疾病名称不能为空")
    private String diseaseName;

    @Schema(description = "疾病分类", example = "眼科")
    private String category;

    @Schema(description = "疾病描述")
    private String description;

    @Schema(description = "指标解读")
    private String indicatorExplain;

    @Schema(description = "居家护理建议")
    private String homeCare;

    @Schema(description = "就诊指导")
    private String medicalAdvice;

    @Schema(description = "风险提示")
    private String riskWarning;

    @Schema(description = "阳性分级标签JSON")
    private String positiveTags;

    @Schema(description = "状态 1正常 0停用", example = "1")
    private Integer status;

}
package cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.knowledge;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 疾病知识库 Response VO")
@Data
public class DiseaseKnowledgeRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "疾病编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "J00.0")
    private String diseaseCode;

    @Schema(description = "疾病名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "近视")
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

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
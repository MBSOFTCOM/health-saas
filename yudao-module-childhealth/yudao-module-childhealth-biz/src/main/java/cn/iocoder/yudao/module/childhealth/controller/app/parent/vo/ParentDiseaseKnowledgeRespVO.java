package cn.iocoder.yudao.module.childhealth.controller.app.parent.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 家长 App - 疾病知识库 VO
 *
 * 创建日期: 2026-07-20
 * 模块: 移动端 - 家长 App
 */
@Schema(description = "家长 App - 疾病知识库 VO")
@Data
public class ParentDiseaseKnowledgeRespVO {

    @Schema(description = "知识库ID", example = "1")
    private Long id;

    @Schema(description = "疾病编码", example = "H52.0")
    private String diseaseCode;

    @Schema(description = "疾病名称", example = "近视")
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

}

package cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.knowledge;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 疾病知识库列表 Request VO")
@Data
public class DiseaseKnowledgeListReqVO {

    @Schema(description = "疾病编码", example = "J00.0")
    private String diseaseCode;

    @Schema(description = "疾病名称", example = "近视")
    private String diseaseName;

    @Schema(description = "疾病分类", example = "眼科")
    private String category;

    @Schema(description = "状态 1正常 0停用", example = "1")
    private Integer status;

}
package cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.knowledge;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 疾病知识库分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DiseaseKnowledgePageReqVO extends PageParam {

    @Schema(description = "疾病编码", example = "J00.0")
    private String diseaseCode;

    @Schema(description = "疾病名称", example = "近视")
    private String diseaseName;

    @Schema(description = "疾病分类", example = "眼科")
    private String category;

    @Schema(description = "状态 1正常 0停用", example = "1")
    private Integer status;

}
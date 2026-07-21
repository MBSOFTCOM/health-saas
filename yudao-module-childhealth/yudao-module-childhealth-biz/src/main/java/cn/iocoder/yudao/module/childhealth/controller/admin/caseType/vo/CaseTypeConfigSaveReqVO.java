package cn.iocoder.yudao.module.childhealth.controller.admin.caseType.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - 专案类型配置新增/修改 Request VO")
@Data
public class CaseTypeConfigSaveReqVO {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "类型编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "类型编码不能为空")
    private String typeCode;

    @Schema(description = "类型名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "类型名称不能为空")
    private String typeName;

    @Schema(description = "分类")
    private String category;

    @Schema(description = "随访频率")
    private String followFrequency;

    @Schema(description = "个案卡模板")
    private String caseCardTemplate;

    @Schema(description = "随访模板")
    private String followTemplate;

    @Schema(description = "结案标准")
    private String dischargeCriteria;

    @Schema(description = "状态")
    private Integer status;

}
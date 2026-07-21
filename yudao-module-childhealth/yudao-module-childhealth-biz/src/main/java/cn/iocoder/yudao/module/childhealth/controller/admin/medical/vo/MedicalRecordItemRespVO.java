package cn.iocoder.yudao.module.childhealth.controller.admin.medical.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 病历结构化字段值 Response VO")
@Data
public class MedicalRecordItemRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "病历ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    private Long recordId;

    @Schema(description = "模板ID", example = "10")
    private Long templateId;

    @Schema(description = "字段编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "HEIGHT")
    private String fieldCode;

    @Schema(description = "字段名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "身高")
    private String fieldName;

    @Schema(description = "字段值", example = "75.5")
    private String fieldValue;

    @Schema(description = "字段类型 text/number/select/date/json", example = "number")
    private String fieldType;

    @Schema(description = "单位", example = "cm")
    private String unit;

    @Schema(description = "参考区间", example = "70-80")
    private String referenceRange;

    @Schema(description = "是否异常 0正常 1异常", example = "0")
    private Integer isAbnormal;

    @Schema(description = "异常描述")
    private String abnormalDesc;

    @Schema(description = "排序", example = "1")
    private Integer sort;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}

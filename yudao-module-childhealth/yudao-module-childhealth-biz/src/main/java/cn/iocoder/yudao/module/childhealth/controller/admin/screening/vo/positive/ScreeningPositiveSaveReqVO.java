package cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.positive;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Schema(description = "管理后台 - 筛查阳性记录新增/修改 Request VO")
@Data
public class ScreeningPositiveSaveReqVO {

    @Schema(description = "主键ID", example = "1")
    private Long id;

    @Schema(description = "筛查记录ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "筛查记录ID不能为空")
    private Long recordId;

    @Schema(description = "学生ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "学生ID不能为空")
    private Long studentId;

    @Schema(description = "疾病编码", example = "J00.0")
    private String diseaseCode;

    @Schema(description = "疾病名称", example = "近视")
    private String diseaseName;

    @Schema(description = "阳性等级 1轻度 2中度 3重度", example = "1")
    private Integer positiveLevel;

    @Schema(description = "阳性指标JSON")
    private String positiveItems;

    @Schema(description = "健康指导")
    private String healthGuidance;

    @Schema(description = "是否需要复筛 0否 1是", example = "1")
    private Integer needRecheck;

    @Schema(description = "复筛状态 0未通知 1通知已入队 2已完成", example = "0")
    private Integer recheckStatus;

}
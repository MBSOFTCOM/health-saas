package cn.iocoder.yudao.module.childhealth.controller.admin.screeningconfig.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Schema(description = "管理后台 - 体检方案配置创建/更新 Request VO")
@Data
public class ScreeningPlanSaveReqVO {

    @Schema(description = "主键ID（更新时必传）", example = "1")
    private Long id;

    @Schema(description = "方案编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "PLAN_C_001")
    @NotBlank(message = "方案编码不能为空")
    private String planCode;

    @Schema(description = "方案名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "五健筛查默认方案")
    @NotBlank(message = "方案名称不能为空")
    private String planName;

    @Schema(description = "方案类型 1五健 2基础体检 3入园入托", example = "1")
    private Integer planType;

    @Schema(description = "是否默认套餐 0否 1是", example = "1")
    private Integer defaultPlan;

    @Schema(description = "最小适用月龄", example = "36")
    private Integer applicableAgeMin;

    @Schema(description = "最大适用月龄", example = "144")
    private Integer applicableAgeMax;

    @Schema(description = "适用性别 0全部 1男 2女", example = "0")
    private Integer applicableGender;

    @Schema(description = "方案内容JSON（含项目列表/问卷/量表）")
    private String planContentJson;

    @Schema(description = "关联问卷ID逗号分隔", example = "1,2,3")
    private String questionnaireIds;

    @Schema(description = "关联量表ID逗号分隔", example = "10,11")
    private String scaleIds;

    @Schema(description = "方案说明")
    private String description;

    @Schema(description = "状态 1启用 0停用", example = "1")
    private Integer status;

}

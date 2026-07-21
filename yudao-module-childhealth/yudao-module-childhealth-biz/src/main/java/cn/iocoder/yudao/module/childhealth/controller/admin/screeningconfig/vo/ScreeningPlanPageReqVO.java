package cn.iocoder.yudao.module.childhealth.controller.admin.screeningconfig.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 体检方案配置分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ScreeningPlanPageReqVO extends PageParam {

    @Schema(description = "方案编码", example = "PLAN_C_001")
    private String planCode;

    @Schema(description = "方案名称", example = "五健筛查默认方案")
    private String planName;

    @Schema(description = "方案类型 1五健 2基础体检 3入园入托", example = "1")
    private Integer planType;

    @Schema(description = "是否默认套餐 0否 1是", example = "1")
    private Integer defaultPlan;

    @Schema(description = "适用性别 0全部 1男 2女", example = "0")
    private Integer applicableGender;

    @Schema(description = "状态 1启用 0停用", example = "1")
    private Integer status;

}

package cn.iocoder.yudao.module.childhealth.controller.admin.medical.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 发育评估量表分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DevelopmentScalePageReqVO extends PageParam {

    @Schema(description = "量表编码", example = "SCALE001")
    private String scaleCode;

    @Schema(description = "量表名称", example = "丹佛发育筛查测验")
    private String scaleName;

    @Schema(description = "类型 运动/语言/智力/行为/过敏/神经运动", example = "运动")
    private String scaleType;

    @Schema(description = "适用性别 0全部 1男 2女", example = "0")
    private Integer applicableGender;

    @Schema(description = "状态 1启用 0停用", example = "1")
    private Integer status;

}

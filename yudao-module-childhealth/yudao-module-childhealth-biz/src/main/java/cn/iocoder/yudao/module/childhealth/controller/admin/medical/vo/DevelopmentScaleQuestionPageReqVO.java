package cn.iocoder.yudao.module.childhealth.controller.admin.medical.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 发育评估量表题目分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DevelopmentScaleQuestionPageReqVO extends PageParam {

    @Schema(description = "量表ID", example = "10")
    private Long scaleId;

    @Schema(description = "题号", example = "1")
    private Integer questionNo;

    @Schema(description = "维度（如：大运动/精细动作/语言/社交/适应）", example = "大运动")
    private String dimension;

}

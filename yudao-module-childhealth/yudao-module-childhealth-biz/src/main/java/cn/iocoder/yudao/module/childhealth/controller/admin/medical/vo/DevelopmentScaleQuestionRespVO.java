package cn.iocoder.yudao.module.childhealth.controller.admin.medical.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 发育评估量表题目 Response VO")
@Data
public class DevelopmentScaleQuestionRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "量表ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    private Long scaleId;

    @Schema(description = "题号", example = "1")
    private Integer questionNo;

    @Schema(description = "维度（如：大运动/精细动作/语言/社交/适应）", example = "大运动")
    private String dimension;

    @Schema(description = "题目内容", requiredMode = Schema.RequiredMode.REQUIRED, example = "能抬头")
    private String questionContent;

    @Schema(description = "选项JSON")
    private String optionsJson;

    @Schema(description = "默认分值", example = "1")
    private BigDecimal defaultScore;

    @Schema(description = "是否反向计分 0否 1是", example = "0")
    private Integer reverseScore;

    @Schema(description = "排序", example = "1")
    private Integer sort;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}

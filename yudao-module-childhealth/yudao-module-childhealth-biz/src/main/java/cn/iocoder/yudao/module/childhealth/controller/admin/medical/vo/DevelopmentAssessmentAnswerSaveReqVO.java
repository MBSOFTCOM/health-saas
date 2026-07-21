package cn.iocoder.yudao.module.childhealth.controller.admin.medical.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 发育评估答题新增/修改 Request VO")
@Data
public class DevelopmentAssessmentAnswerSaveReqVO {

    @Schema(description = "主键ID", example = "1")
    private Long id;

    @Schema(description = "评估记录ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    @NotNull(message = "评估记录ID不能为空")
    private Long recordId;

    @Schema(description = "题目ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    @NotNull(message = "题目ID不能为空")
    private Long questionId;

    @Schema(description = "答案", example = "通过")
    private String answer;

    @Schema(description = "得分", example = "1")
    private BigDecimal score;

    @Schema(description = "备注")
    private String remark;

}

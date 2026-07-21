package cn.iocoder.yudao.module.childhealth.controller.admin.medical.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 发育评估答题 Response VO")
@Data
public class DevelopmentAssessmentAnswerRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "评估记录ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    private Long recordId;

    @Schema(description = "题目ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    private Long questionId;

    @Schema(description = "答案", example = "通过")
    private String answer;

    @Schema(description = "得分", example = "1")
    private BigDecimal score;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}

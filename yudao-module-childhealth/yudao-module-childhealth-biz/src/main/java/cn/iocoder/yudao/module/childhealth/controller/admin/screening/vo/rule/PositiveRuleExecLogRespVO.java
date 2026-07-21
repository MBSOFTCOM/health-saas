package cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.rule;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 阳性规则执行日志 Response VO
 *
 * 创建日期: 2026-07-20
 */
@Schema(description = "管理后台 - 阳性规则执行日志 Response VO")
@Data
public class PositiveRuleExecLogRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "规则ID", example = "100")
    private Long ruleId;

    @Schema(description = "筛查记录ID", example = "200")
    private Long recordId;

    @Schema(description = "筛查结果明细ID", example = "300")
    private Long resultDetailId;

    @Schema(description = "学生ID", example = "1001")
    private Long studentId;

    @Schema(description = "命中的项目编码", example = "VISION")
    private String matchedItemCode;

    @Schema(description = "命中的值", example = "0.5")
    private String matchedValue;

    @Schema(description = "命中的条件逻辑快照JSON")
    private String conditionLogic;

    @Schema(description = "阳性等级 1轻度 2中度 3重度", example = "1")
    private Integer positiveLevel;

    @Schema(description = "关联疾病编码", example = "H53.0")
    private String diseaseCode;

    @Schema(description = "是否判定为阳性 0否 1是", example = "1")
    private Integer isPositive;

    @Schema(description = "命中时间")
    private LocalDateTime matchedAt;

    @Schema(description = "规则版本号", example = "v1.0")
    private String execVersion;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}

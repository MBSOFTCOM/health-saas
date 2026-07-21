package cn.iocoder.yudao.module.childhealth.controller.admin.medical.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "管理后台 - 发育评估记录新增/修改 Request VO")
@Data
public class DevelopmentAssessmentRecordSaveReqVO {

    @Schema(description = "主键ID", example = "1")
    private Long id;

    @Schema(description = "量表ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    @NotNull(message = "量表ID不能为空")
    private Long scaleId;

    @Schema(description = "儿童ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1001")
    @NotNull(message = "儿童ID不能为空")
    private Long childId;

    @Schema(description = "关联病历ID", example = "100")
    private Long medicalRecordId;

    @Schema(description = "评估人ID（医生/家长）", example = "500")
    private Long assessorId;

    @Schema(description = "评估人类型 1医生 2家长", example = "1")
    private Integer assessorType;

    @Schema(description = "评估日期", example = "2026-07-20")
    private LocalDate assessDate;

    @Schema(description = "评估时月龄", example = "12")
    private Integer ageMonth;

    @Schema(description = "总分", example = "85")
    private BigDecimal totalScore;

    @Schema(description = "风险等级 1正常 2临界 3异常", example = "1")
    private Integer riskLevel;

    @Schema(description = "各维度得分JSON")
    private String dimensionScoresJson;

    @Schema(description = "结论")
    private String conclusion;

    @Schema(description = "建议")
    private String suggestion;

    @Schema(description = "报告文件URL")
    private String reportUrl;

    @Schema(description = "状态 0进行中 1已完成 2已废弃", example = "0")
    private Integer status;

}

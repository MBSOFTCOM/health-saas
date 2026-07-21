package cn.iocoder.yudao.module.childhealth.api.scale.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PsychologicalScaleCreateReqDTO {

    @NotEmpty(message = "量表编码不能为空")
    private String scaleCode;

    @NotEmpty(message = "量表名称不能为空")
    private String scaleName;

    private String scaleNameEn;

    @NotEmpty(message = "量表类型不能为空")
    private String scaleType;

    @NotNull(message = "适用最小月龄不能为空")
    private Integer applicableAgeMin;

    @NotNull(message = "适用最大月龄不能为空")
    private Integer applicableAgeMax;

    @NotEmpty(message = "填写人类型不能为空")
    private String respondentType;

    private Integer itemCount;
    private String items;
    private String scoringRules;
    private String totalScoreRange;
    private String diagnosticCriteria;
    private String riskLevels;
    private String diagnosticExplanation;
    private String description;
    private String version;
    private Integer publishYear;
    private String references;
    private String status;
}

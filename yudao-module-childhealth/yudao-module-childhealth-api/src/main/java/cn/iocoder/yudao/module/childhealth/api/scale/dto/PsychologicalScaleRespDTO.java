package cn.iocoder.yudao.module.childhealth.api.scale.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PsychologicalScaleRespDTO {

    private Long id;
    private String scaleCode;
    private String scaleName;
    private String scaleNameEn;
    private String scaleType;
    private Integer applicableAgeMin;
    private Integer applicableAgeMax;
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
    private LocalDateTime createdAt;
}

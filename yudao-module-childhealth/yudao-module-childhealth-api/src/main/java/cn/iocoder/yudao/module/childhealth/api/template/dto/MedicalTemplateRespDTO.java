package cn.iocoder.yudao.module.childhealth.api.template.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MedicalTemplateRespDTO {

    private Long id;
    private String templateCode;
    private String templateName;
    private Integer ageMonthMin;
    private Integer ageMonthMax;
    private String templateType;
    private String description;
    private String normVersion;
    private String templateContent;
    private String fieldMapping;
    private String healthGuidance;
    private String followUpRecommendation;
    private String abnormalityThresholds;
    private Integer sortOrder;
    private Integer status;
    private LocalDateTime createTime;
}

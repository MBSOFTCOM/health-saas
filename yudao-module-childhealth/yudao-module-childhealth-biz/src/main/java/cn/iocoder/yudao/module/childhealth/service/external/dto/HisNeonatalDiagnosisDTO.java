package cn.iocoder.yudao.module.childhealth.service.external.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * HIS 新生儿住院诊断 DTO（用于预警标记）
 *
 * 需求 5：自动抓取新生儿住院诊断数据预警
 */
@Data
public class HisNeonatalDiagnosisDTO {

    /**
     * HIS 诊断ID
     */
    private String hisDiagnosisId;

    /**
     * HIS 患者ID
     */
    private String hisPatientId;

    /**
     * 诊断编码（ICD-10）
     */
    private String diagnosisCode;

    /**
     * 诊断名称
     */
    private String diagnosisName;

    /**
     * 诊断类型（入院/出院/主要/次要）
     */
    private String diagnosisType;

    /**
     * 入院日期
     */
    private LocalDate admissionDate;

    /**
     * 出院日期
     */
    private LocalDate dischargeDate;

    /**
     * 诊断医生
     */
    private String diagnosisDoctor;

    /**
     * 严重程度 1轻 2中 3重
     */
    private Integer severityLevel;

    /**
     * 是否高危诊断（HIS 标记）
     */
    private Boolean isHighRisk;

}

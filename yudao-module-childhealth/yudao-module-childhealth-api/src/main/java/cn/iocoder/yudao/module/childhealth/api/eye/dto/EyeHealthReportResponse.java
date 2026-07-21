package cn.iocoder.yudao.module.childhealth.api.eye.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EyeHealthReportResponse {
    private Long checkupId;
    private Long childId;
    private LocalDate checkupDate;
    private Integer ageMonths;

    private BigDecimal leftEyeVisualAcuity;
    private BigDecimal rightEyeVisualAcuity;
    private BigDecimal leftEyeRefraction;
    private BigDecimal rightEyeRefraction;

    private Boolean isAbnormal;
    private String abnormalItems;
    private String abnormalGrade;

    private String recommendations;
    private Boolean followUpNeeded;
    private Integer followUpMonths;
}

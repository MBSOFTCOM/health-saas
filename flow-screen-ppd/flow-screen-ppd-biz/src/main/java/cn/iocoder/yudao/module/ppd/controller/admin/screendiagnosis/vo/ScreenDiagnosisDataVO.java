package cn.iocoder.yudao.module.ppd.controller.admin.screendiagnosis.vo;

import lombok.Data;

@Data
public class ScreenDiagnosisDataVO {

    private Integer todayCount;
    private Integer todayAbnormalCount;

    private Integer monthCount;
    private Integer monthAbnormalCount;

    private Integer yearCount;
    private Integer yearAbnormalCount;

    private Integer outcome1Count;
    private Integer outcome2Count;
    private Integer outcome3Count;
    private Integer outcome4Count;
    private Integer outcome5Count;
    private Integer outcome6Count;
    private Integer outcome7Count;

    private Integer treatmentProgram1Count;
    private Integer treatmentProgram2Count;
    private Integer treatmentProgram3Count;
    private Integer treatmentProgram4Count;
    private Integer treatmentProgram5Count;
    private Integer treatmentProgram6Count;
    private Integer treatmentProgram7Count;
}

package cn.iocoder.yudao.module.ppd.controller.admin.screendiagnosis.vo;

import lombok.Data;

@Data
public class LastTimeDiagnosisResultRespVO {

    /**
     * 诊断结果
     */
    private Integer outcome;
    /**
     * 治疗方案
     */
    private Integer treatmentProgram;
    /**
     * 是否网报 0-否 1-是
     */
    private Integer report;
    /**
     * 符合潜伏治疗条件者是否进行预防性治疗 0-否 1-是
     */
    private Integer preventiveTreatment;
    /**
     * 筛查次序
     */
    private Integer screenOrder;
}

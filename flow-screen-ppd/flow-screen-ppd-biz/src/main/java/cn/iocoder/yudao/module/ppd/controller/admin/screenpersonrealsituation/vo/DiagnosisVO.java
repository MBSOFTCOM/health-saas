package cn.iocoder.yudao.module.ppd.controller.admin.screenpersonrealsituation.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class DiagnosisVO implements Serializable {

    /**
     * 主键
     */
    private Long id;
    /**
     * 筛查编号
     */
    private String screenId;
    /**
     * 年份
     */
    private Integer year;
    /**
     * 筛查类型
     */
    private Integer screenType;
    /**
     * 同步时唯一编码
     */
    private Long syncId;
    /**
     * 患者id
     */
    private Long personId;
    /**
     * 医生签名
     */
    private String doctorSignature;
    /**
     * 筛查时间
     */
    private LocalDateTime screenTime;
    /**
     * 结果。1-利福平耐药 2-病原学阳性 3-病原学阴性 4-无病原学结果
     */
    private Integer outcome;
    /**
     * 治疗方案
     */
    private String treatmentProgram;
    /**
     * 是否网报 0-否 1-是
     */
    private Integer report;
    /**
     * 符合潜伏治疗条件者是否进行预防性治疗 0-否 1-是
     */
    private String preventiveTreatment;
    /**
     * 筛查次序
     */
    private Integer screenOrder;

}

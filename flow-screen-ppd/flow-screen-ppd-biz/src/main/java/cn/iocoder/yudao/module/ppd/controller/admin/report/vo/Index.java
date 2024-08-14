package cn.iocoder.yudao.module.ppd.controller.admin.report.vo;

import lombok.Data;

@Data
public class Index {
    /**
     * 多人群分类类型
     */
    private Integer moreType;
    /**
     * 应查人数
     */
    private Integer expectedCheckCount;
    /**
     * 实查人数
     */
    private Integer actualCheckedCount;
    /**
     * 应拍片人数
     */
    private Integer expectedXRayCount;
    /**
     * 实拍人数 （拍片人数）
     */
    private Integer actualXRayCount;
    /**
     * 应查痰人数
     */
    private Integer expectedSputumTestCount;
    /**
     * 实查痰人数
     */
    private Integer actualSputumCheckCount;
    /**
     * 活动性肺结核人数 （发现活动性肺结核患者数）
     */
    private Integer activeTuberculosisCount;
    /**
     * 病原学阳性人数 （发现初治病原学阳性患者数）
     */
    private Integer pathogenPositiveCount;
    /**
     * 潜伏感染者人数 （发现潜伏感染者人数）
     */
    private Integer latentInfectionCount;
    /**
     * 确诊及治疗中患者人数
     */
    private Integer confirmedAndTreatingCount;
    /**
     * 糖尿病患者人数
     */
    private Integer diabeticPatientCount;
    /**
     * HIV/AIDS患者数
     */
    private Integer HIVPositiveCount;

    /**
     * 应查人数 年度总数
     */
    private Integer expectedCheckCountY;
    /**
     * 实查人数 年度总数
     */
    private Integer actualCheckedCountY;
    /**
     * 应拍片人数 年度总数
     */
    private Integer expectedXRayCountY;
    /**
     * 实拍人数 年度总数
     */
    private Integer actualXRayCountY;
    /**
     * 应查痰人数 年度总数
     */
    private Integer expectedSputumTestCountY;
    /**
     * 实查痰人数 年度总数
     */
    private Integer actualSputumCheckCountY;
    /**
     * 活动性肺结核人数 年度总数
     */
    private Integer activeTuberculosisCountY;
    /**
     * 病原学阳性人数 年度总数
     */
    private Integer pathogenPositiveCountY;
    /**
     * 潜伏感染者人数 年度总数
     */
    private Integer latentInfectionCountY;
    /**
     * 确诊及治疗中患者人数 年度总数
     */
    private Integer confirmedAndTreatingCountY;
    /**
     * 糖尿病患者人数 年度总数
     */
    private Integer diabeticPatientCountY;
    /**
     * HIV/AIDS患者数 年度总数
     */
    private Integer HIVPositiveCountY;
}

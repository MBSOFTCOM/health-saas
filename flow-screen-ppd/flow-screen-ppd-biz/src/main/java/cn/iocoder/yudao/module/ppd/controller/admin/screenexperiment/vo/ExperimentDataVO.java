package cn.iocoder.yudao.module.ppd.controller.admin.screenexperiment.vo;

import lombok.Data;

@Data
public class ExperimentDataVO {
    /**
     * 本日检测人数
     */
    private Integer dayPerson;
    /**
     * 本月检测人数
     */
    private Integer monthPerson;
    /**
     * 本年检测人数
     */
    private Integer yearPerson;
    /**
     * 痰涂片人数
     */
    private Integer smearResultPerson;
    /**
     * 痰培养人数
     */
    private Integer cultureResultPerson;
    /**
     * 分子生物学人数
     */
    private Integer molecularBiologyPerson;
    /**
     * 药敏耐药人数
     */
    private Integer drugResistanceResultPerson;
    /**
     * 潜伏感染人数
     */
    private Integer latentInfectionPerson;

    public ExperimentDataVO(Integer dayPerson, Integer monthPerson, Integer yearPerson,
                            Integer smearResultPerson, Integer cultureResultPerson,
                            Integer molecularBiologyPerson, Integer drugResistanceResultPerson,
                            Integer latentInfectionPerson) {
        this.dayPerson = dayPerson;
        this.monthPerson = monthPerson;
        this.yearPerson = yearPerson;
        this.smearResultPerson = smearResultPerson;
        this.cultureResultPerson = cultureResultPerson;
        this.molecularBiologyPerson = molecularBiologyPerson;
        this.drugResistanceResultPerson = drugResistanceResultPerson;
        this.latentInfectionPerson = latentInfectionPerson;
    }
}

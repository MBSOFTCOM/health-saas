package cn.iocoder.yudao.module.childhealth.service.external.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * 产科系统高危新生儿 DTO
 *
 * 需求 34：产科高危儿/早产儿/低体重儿列表管理
 */
@Data
public class ObstetricsHighRiskNewbornDTO {

    /**
     * 产科系统新生儿ID
     */
    private String obstetricsNewbornId;

    /**
     * 母亲姓名
     */
    private String motherName;

    /**
     * 母亲身份证号
     */
    private String motherIdCard;

    /**
     * 母亲联系电话
     */
    private String motherPhone;

    /**
     * 新生儿姓名
     */
    private String newbornName;

    /**
     * 性别 1男 2女
     */
    private Integer gender;

    /**
     * 出生日期
     */
    private LocalDate birthDate;

    /**
     * 出生体重（克）
     */
    private Integer birthWeight;

    /**
     * 出生身长（cm）
     */
    private Double birthLength;

    /**
     * 胎龄（周）
     */
    private Integer gestationalAge;

    /**
     * Apgar 评分
     */
    private Integer apgarScore;

    /**
     * 分娩方式
     */
    private String deliveryMethod;

    /**
     * 高危类型 PRETERM/LOW_WEIGHT/ASPHYXIA/HYPERBILIRUBINEMIA/HIE/INHERITED_METABOLIC
     */
    private String highRiskType;

    /**
     * 产科高危等级 1一般 / 2重点关注 / 3严密管理
     */
    private Integer riskLevel;

    /**
     * 是否已建册（儿保侧）
     */
    private Boolean hasChildHealthRecord;

    /**
     * 产科出院日期
     */
    private LocalDate dischargeDate;

    /**
     * 产科主管医生
     */
    private String obstetricsDoctor;

}

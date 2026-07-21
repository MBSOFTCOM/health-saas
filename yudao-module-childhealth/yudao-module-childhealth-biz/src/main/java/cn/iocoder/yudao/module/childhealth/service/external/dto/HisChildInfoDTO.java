package cn.iocoder.yudao.module.childhealth.service.external.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * HIS 儿童基本信息 DTO（用于从 HIS 系统拉取建档数据）
 *
 * 需求 2：HIS 自动建册
 */
@Data
public class HisChildInfoDTO {

    /**
     * HIS 系统患者ID
     */
    private String hisPatientId;

    /**
     * 医保卡号
     */
    private String medicareCardNo;

    /**
     * 儿童姓名
     */
    private String childName;

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
     * Apgar 评分（1分钟）
     */
    private Integer apgarScore1Min;

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
     * 父亲姓名
     */
    private String fatherName;

    /**
     * 户籍地址
     */
    private String residenceAddress;

    /**
     * HIS 入院/建档时间
     */
    private LocalDateTime hisRecordTime;

}

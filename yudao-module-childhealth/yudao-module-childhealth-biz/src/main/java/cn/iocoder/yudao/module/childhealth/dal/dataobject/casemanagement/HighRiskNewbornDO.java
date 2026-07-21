package cn.iocoder.yudao.module.childhealth.dal.dataobject.casemanagement;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 高危新生儿台账表 DO
 *
 * 对应表: high_risk_newborn
 * 模块: B.高危儿专案管理
 * 创建日期: 2026-07-20
 *
 * 说明：本 DO 为 high_risk_newborn 表的唯一映射，原 workflow 包下的同名 DO 已删除并合并到此。
 * 字段同时覆盖：1) 母婴基础信息(motherName/pregnancyWeek/birthWeight/apgarScore)
 *              2) 高危评估信息(highRiskTypes/riskFactors/riskLevel/assessmentDate)
 *              3) 建册与随访信息(isRegistered/registerDate/followupStatus/caseId)
 */
@TableName("high_risk_newborn")
@KeySequence("high_risk_newborn_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HighRiskNewbornDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 儿童档案ID
     */
    private Long childId;

    /**
     * 母亲姓名
     */
    private String motherName;

    /**
     * 母亲年龄
     */
    private Integer motherAge;

    /**
     * 孕周
     */
    private Integer pregnancyWeek;

    /**
     * 孕期高危因素
     */
    private String pregnancyRisk;

    /**
     * 出生体重(kg)
     */
    private BigDecimal birthWeight;

    /**
     * 出生身长(cm)
     */
    private BigDecimal birthLength;

    /**
     * Apgar 1分钟评分
     */
    private Integer apgarScore1min;

    /**
     * Apgar 5分钟评分
     */
    private Integer apgarScore5min;

    /**
     * 高危类型JSON（早产/低体重/高胆红素血症/遗传代谢病/HIE）
     */
    private String highRiskTypes;

    /**
     * 是否已建册 0否 1是
     */
    private Integer isRegistered;

    /**
     * 建册日期
     */
    private LocalDate registerDate;

    /**
     * 建册机构
     */
    private String registerOrg;

    /**
     * 随访状态 0未随访 1随访中 2已结案
     */
    private Integer followupStatus;

    /**
     * 关联专案ID
     */
    private Long caseId;

    /**
     * 备注
     */
    private String remark;

    // ========== 合并自原 workflow.HighRiskNewbornDO 的字段 ==========

    /**
     * 出生医院
     */
    private String birthHospital;

    /**
     * 风险因素代码列表（逗号分隔，如 DEL_PRETERM,DEL_LOW_WEIGHT）
     */
    private String riskFactors;

    /**
     * 风险等级 1一般 2重点关注 3严密管理
     */
    private Integer riskLevel;

    /**
     * 评估日期
     */
    private LocalDate assessmentDate;

    /**
     * 评估医生ID
     */
    private Long assessmentDoctor;

    /**
     * 评估类型 1自动识别 2手工评估
     */
    private Integer assessmentType;

    /**
     * 是否已随访
     */
    private Boolean isFollowed;

    /**
     * 预警状态 0未预警 1已预警 2已处理
     */
    private Integer alertStatus;

}

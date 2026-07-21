package cn.iocoder.yudao.module.childhealth.dal.dataobject.medical;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 发育评估记录表 DO
 *
 * 对应表: development_assessment_record
 * 模块: A. 儿童基础健康检查（A8-发育评估记录表）
 * 创建日期: 2026-07-20
 */
@TableName("development_assessment_record")
@KeySequence("development_assessment_record_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DevelopmentAssessmentRecordDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 量表ID
     */
    private Long scaleId;

    /**
     * 儿童ID
     */
    private Long childId;

    /**
     * 关联病历ID
     */
    private Long medicalRecordId;

    /**
     * 评估人ID（医生/家长）
     */
    private Long assessorId;

    /**
     * 评估人类型 1医生 2家长
     */
    private Integer assessorType;

    /**
     * 评估日期
     */
    private LocalDate assessDate;

    /**
     * 评估时月龄
     */
    private Integer ageMonth;

    /**
     * 总分
     */
    private BigDecimal totalScore;

    /**
     * 风险等级 1正常 2临界 3异常
     */
    private Integer riskLevel;

    /**
     * 各维度得分JSON
     */
    private String dimensionScoresJson;

    /**
     * 结论
     */
    private String conclusion;

    /**
     * 建议
     */
    private String suggestion;

    /**
     * 报告文件URL
     */
    private String reportUrl;

    /**
     * 状态 0进行中 1已完成 2已废弃
     */
    private Integer status;

}

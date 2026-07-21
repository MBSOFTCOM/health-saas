package cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 心理量表评估记录表 DO
 *
 * 对应表: psychological_assessment
 * 模块: C.五健筛查配置
 * 创建日期: 2026-07-20
 */
@TableName("psychological_assessment")
@KeySequence("psychological_assessment_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PsychologicalAssessmentDO extends BaseDO {

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
     * 筛查批次ID
     */
    private Long batchId;

    /**
     * 评估人ID
     */
    private Long assessorId;

    /**
     * 评估人类型 1家长 2医生
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
     * 答案JSON
     */
    private String answersJson;

    /**
     * 总分
     */
    private BigDecimal totalScore;

    /**
     * 风险等级 1正常 2临界 3轻度 4中度 5重度
     */
    private Integer riskLevel;

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

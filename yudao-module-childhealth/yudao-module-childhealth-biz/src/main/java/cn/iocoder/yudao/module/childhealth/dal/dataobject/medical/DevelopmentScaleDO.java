package cn.iocoder.yudao.module.childhealth.dal.dataobject.medical;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;

/**
 * 发育评估量表定义表 DO
 *
 * 对应表: development_scale
 * 模块: A. 儿童基础健康检查（A6-发育评估量表定义表，18+套）
 * 创建日期: 2026-07-20
 */
@TableName("development_scale")
@KeySequence("development_scale_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DevelopmentScaleDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 量表编码
     */
    private String scaleCode;

    /**
     * 量表名称
     */
    private String scaleName;

    /**
     * 类型 运动/语言/智力/行为/过敏/神经运动
     */
    private String scaleType;

    /**
     * 最小适用月龄
     */
    private Integer applicableAgeMin;

    /**
     * 最大适用月龄
     */
    private Integer applicableAgeMax;

    /**
     * 适用性别 0全部 1男 2女
     */
    private Integer applicableGender;

    /**
     * 题目数量
     */
    private Integer questionCount;

    /**
     * 总分上限
     */
    private BigDecimal totalScoreMax;

    /**
     * 计分规则JSON
     */
    private String scoringRuleJson;

    /**
     * 风险等级JSON
     */
    private String riskLevelsJson;

    /**
     * 量表说明
     */
    private String description;

    /**
     * 状态 1启用 0停用
     */
    private Integer status;

}

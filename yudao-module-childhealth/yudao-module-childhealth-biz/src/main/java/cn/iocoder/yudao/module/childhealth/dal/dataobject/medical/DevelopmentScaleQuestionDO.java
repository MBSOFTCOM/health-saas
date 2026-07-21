package cn.iocoder.yudao.module.childhealth.dal.dataobject.medical;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;

/**
 * 发育评估量表题目表 DO
 *
 * 对应表: development_scale_question
 * 模块: A. 儿童基础健康检查（A7-发育评估量表题目表）
 * 创建日期: 2026-07-20
 */
@TableName("development_scale_question")
@KeySequence("development_scale_question_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DevelopmentScaleQuestionDO extends BaseDO {

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
     * 题号
     */
    private Integer questionNo;

    /**
     * 维度（如：大运动/精细动作/语言/社交/适应）
     */
    private String dimension;

    /**
     * 题目内容
     */
    private String questionContent;

    /**
     * 选项JSON
     */
    private String optionsJson;

    /**
     * 默认分值
     */
    private BigDecimal defaultScore;

    /**
     * 是否反向计分 0否 1是
     */
    private Integer reverseScore;

    /**
     * 排序
     */
    private Integer sort;

}

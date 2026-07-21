package cn.iocoder.yudao.module.childhealth.dal.dataobject.medical;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;

/**
 * 发育评估答题表 DO
 *
 * 对应表: development_assessment_answer
 * 模块: A. 儿童基础健康检查（A9-发育评估答题表）
 * 创建日期: 2026-07-20
 */
@TableName("development_assessment_answer")
@KeySequence("development_assessment_answer_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DevelopmentAssessmentAnswerDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 评估记录ID
     */
    private Long recordId;

    /**
     * 题目ID
     */
    private Long questionId;

    /**
     * 答案
     */
    private String answer;

    /**
     * 得分
     */
    private BigDecimal score;

    /**
     * 备注
     */
    private String remark;

}

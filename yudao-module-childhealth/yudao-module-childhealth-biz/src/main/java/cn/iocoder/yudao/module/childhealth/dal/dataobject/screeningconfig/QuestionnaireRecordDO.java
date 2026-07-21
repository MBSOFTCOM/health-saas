package cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 问卷答卷表 DO
 *
 * 对应表: questionnaire_record
 * 模块: C.五健筛查配置
 * 创建日期: 2026-07-20
 */
@TableName("questionnaire_record")
@KeySequence("questionnaire_record_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionnaireRecordDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 问卷ID
     */
    private Long questionnaireId;

    /**
     * 儿童ID
     */
    private Long childId;

    /**
     * 批次ID
     */
    private Long batchId;

    /**
     * 提交人ID
     */
    private Long submitterId;

    /**
     * 提交人类型 1家长 2医生
     */
    private Integer submitterType;

    /**
     * 提交时间
     */
    private LocalDateTime submitTime;

    /**
     * 答案JSON
     */
    private String answersJson;

    /**
     * 得分
     */
    private BigDecimal score;

    /**
     * 结果
     */
    private String result;

    /**
     * 状态 0进行中 1已完成 2已废弃
     */
    private Integer status;

}

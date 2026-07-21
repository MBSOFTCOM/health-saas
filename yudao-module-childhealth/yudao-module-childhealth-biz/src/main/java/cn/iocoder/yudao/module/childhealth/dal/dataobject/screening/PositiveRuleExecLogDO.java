package cn.iocoder.yudao.module.childhealth.dal.dataobject.screening;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 阳性规则执行日志表 DO
 *
 * 对应表: positive_rule_exec_log
 * 模块: 7. 阳性自动识别规则引擎
 * 创建日期: 2026-07-20
 */
@TableName("positive_rule_exec_log")
@KeySequence("positive_rule_exec_log_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PositiveRuleExecLogDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 规则ID（关联positive_rule.id）
     */
    private Long ruleId;

    /**
     * 筛查记录ID（关联screening_record.id）
     */
    private Long recordId;

    /**
     * 筛查结果明细ID（关联screening_result_detail.id）
     */
    private Long resultDetailId;

    /**
     * 学生ID（冗余便于查询）
     */
    private Long studentId;

    /**
     * 命中的项目编码
     */
    private String matchedItemCode;

    /**
     * 命中的值
     */
    private String matchedValue;

    /**
     * 命中的条件逻辑快照 JSON
     */
    private String conditionLogic;

    /**
     * 阳性等级 1轻度 2中度 3重度
     */
    private Integer positiveLevel;

    /**
     * 关联疾病编码
     */
    private String diseaseCode;

    /**
     * 是否判定为阳性 0否 1是
     */
    private Integer isPositive;

    /**
     * 命中时间
     */
    private LocalDateTime matchedAt;

    /**
     * 规则版本号
     */
    private String execVersion;

}

package cn.iocoder.yudao.module.childhealth.dal.dataobject.casemanagement;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDate;

/**
 * 体检催检规则表 DO
 *
 * 对应表: exam_reminder_rule
 * 模块: B.体检催检
 * 创建日期: 2026-07-20
 */
@TableName("exam_reminder_rule")
@KeySequence("exam_reminder_rule_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamReminderRuleDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 规则编码
     */
    private String ruleCode;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 适用月龄（0-72月）
     */
    private Integer ageMonth;

    /**
     * 体检类型 满月/3月/6月/8月/12月/18月/24月/30月/3岁/4岁/5岁/6岁
     */
    private String examType;

    /**
     * 目标人群 0全部 1高危 2专案
     */
    private Integer targetAudience;

    /**
     * 推送渠道（短信/微信/APP）
     */
    private String pushChannel;

    /**
     * 推送模板ID
     */
    private Long pushTemplateId;

    /**
     * 提前推送天数
     */
    private Integer pushLeadDays;

    /**
     * 科室ID（NULL表示全科室）
     */
    private Long deptId;

    /**
     * 优先级 1高 2中 3低
     */
    private Integer priority;

    /**
     * 生效开始日期
     */
    private LocalDate effectiveStart;

    /**
     * 生效结束日期
     */
    private LocalDate effectiveEnd;

    /**
     * 状态 1启用 0停用
     */
    private Integer status;

}

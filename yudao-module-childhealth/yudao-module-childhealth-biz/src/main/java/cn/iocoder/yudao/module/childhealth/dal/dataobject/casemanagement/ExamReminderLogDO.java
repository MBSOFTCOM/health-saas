package cn.iocoder.yudao.module.childhealth.dal.dataobject.casemanagement;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 体检催检推送日志表 DO
 *
 * 对应表: exam_reminder_log
 * 模块: B.体检催检
 * 创建日期: 2026-07-20
 *
 * 注：已合并 ops 包下的 ExamReminderLogDO（与 casemanagement 版本同表）
 */
@TableName("exam_reminder_log")
@KeySequence("exam_reminder_log_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamReminderLogDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 规则ID
     */
    private Long ruleId;

    /**
     * 儿童ID
     */
    private Long childId;

    /**
     * 体检类型（1满月 2常规 3入园等，用于催检规则匹配）
     */
    private Integer examType;

    /**
     * 应检日期（用于筛选到期催检）
     */
    private LocalDate dueDate;

    /**
     * 提醒日期（计划发送日期）
     */
    private LocalDate remindDate;

    /**
     * 推送渠道
     */
    private String pushChannel;

    /**
     * 推送时间
     */
    private LocalDateTime pushTime;

    /**
     * 推送内容
     */
    private String pushContent;

    /**
     * 状态 0待发送 1成功 2已完成 3已取消 4已补发 5失败
     */
    private Integer pushStatus;

    /**
     * 失败原因
     */
    private String failReason;

    /**
     * 重试次数
     */
    private Integer retryCount;

    /**
     * 响应数据
     */
    private String responseData;

    /**
     * 响应时间（家长实际响应时间，区别于 readTime 阅读时间）
     */
    private LocalDateTime responseTime;

    /**
     * 阅读时间
     */
    private LocalDateTime readTime;

    /**
     * 是否已完成（用于催检任务完成后标记，区别于 pushStatus 流转状态）
     */
    private Boolean isCompleted;

}


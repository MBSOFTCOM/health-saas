package cn.iocoder.yudao.module.childhealth.dal.dataobject.screening;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 复筛随访时间轴表 DO
 *
 * 对应表: recheck_follow_timeline
 * 模块: 13. 复筛专项管理 + 20. 全维度数据统计
 * 创建日期: 2026-07-20
 */
@TableName("recheck_follow_timeline")
@KeySequence("recheck_follow_timeline_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecheckFollowTimelineDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 阳性记录ID
     */
    private Long positiveId;

    /**
     * 学生ID
     */
    private Long studentId;

    /**
     * 事件类型 NOTIFY/RECHECK/FOLLOW/TRANSFER/CLOSE/CANCEL
     */
    private String eventType;

    /**
     * 事件时间
     */
    private LocalDateTime eventTime;

    /**
     * 事件标题
     */
    private String eventTitle;

    /**
     * 事件内容
     */
    private String eventContent;

    /**
     * 操作人ID
     */
    private Long operatorId;

    /**
     * 操作人姓名
     */
    private String operatorName;

    /**
     * 关联记录ID（多态）
     */
    private Long relatedId;

    /**
     * 关联表名
     */
    private String relatedTable;

    /**
     * 备注
     */
    private String remark;

}

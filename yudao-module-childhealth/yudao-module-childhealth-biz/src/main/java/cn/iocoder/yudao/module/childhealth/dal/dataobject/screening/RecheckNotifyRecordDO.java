package cn.iocoder.yudao.module.childhealth.dal.dataobject.screening;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 复筛通知记录表 DO
 *
 * 对应表: recheck_notify_record
 * 模块: 13. 复筛专项管理
 * 创建日期: 2026-07-20
 */
@TableName("recheck_notify_record")
@KeySequence("recheck_notify_record_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecheckNotifyRecordDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 阳性记录ID（关联screening_positive.id）
     */
    private Long positiveId;

    /**
     * 复筛记录ID（关联recheck_record.id）
     */
    private Long recheckId;

    /**
     * 学生ID
     */
    private Long studentId;

    /**
     * 学校ID（便于按校统计）
     */
    private Long schoolId;

    /**
     * 班级ID
     */
    private Long classId;

    /**
     * 通知渠道 1短信 2微信 3APP 4电话 5纸质通知单
     */
    private Integer notifyChannel;

    /**
     * 通知类型 1首次通知 2催促 3变更 4取消
     */
    private Integer notifyType;

    /**
     * 通知时间
     */
    private LocalDateTime notifyTime;

    /**
     * 通知内容
     */
    private String notifyContent;

    /**
     * 通知状态 0待发送 1成功 2失败 3已读 4已响应
     */
    private Integer notifyStatus;

    /**
     * 关联message_push_log.id
     */
    private Long pushLogId;

    /**
     * 响应人
     */
    private String responder;

    /**
     * 响应时间
     */
    private LocalDateTime responseTime;

    /**
     * 响应内容
     */
    private String responseContent;

}

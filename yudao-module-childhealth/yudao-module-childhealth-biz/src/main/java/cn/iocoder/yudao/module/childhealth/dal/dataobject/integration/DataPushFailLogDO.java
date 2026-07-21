package cn.iocoder.yudao.module.childhealth.dal.dataobject.integration;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 数据上报失败重试日志表 DO
 *
 * 对应表: data_push_fail_log
 * 模块: 2. 数据上报对接
 * 创建日期: 2026-07-20
 */
@TableName("data_push_fail_log")
@KeySequence("data_push_fail_log_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataPushFailLogDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 任务ID（关联data_push_task.id）
     */
    private Long taskId;

    /**
     * 尝试次数
     */
    private Integer attemptNo;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 错误类型 NETWORK/AUTH/BUSINESS/TIMEOUT
     */
    private String errorType;

    /**
     * 请求报文
     */
    private String requestPayload;

    /**
     * 响应报文
     */
    private String responsePayload;

    /**
     * HTTP状态码
     */
    private Integer httpStatus;

    /**
     * 耗时(毫秒)
     */
    private Integer costTimeMs;

    /**
     * 失败时间
     */
    private LocalDateTime failTime;

}

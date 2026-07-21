package cn.iocoder.yudao.module.childhealth.dal.dataobject.integration;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 数据上报任务队列表 DO
 *
 * 对应表: data_push_task
 * 模块: 2. 数据上报对接
 * 创建日期: 2026-07-20
 */
@TableName("data_push_task")
@KeySequence("data_push_task_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataPushTaskDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 任务编号
     */
    private String taskNo;

    /**
     * 目标系统 HIS/LIS/WCHB/WSJW/EDU
     */
    private String targetSystem;

    /**
     * 目标机构名称
     */
    private String targetOrg;

    /**
     * 业务类型 SCREENING/REFERRAL/FOLLOW/RECHECK
     */
    private String businessType;

    /**
     * 关联筛查批次ID
     */
    private Long batchId;

    /**
     * 业务数据ID列表 JSON
     */
    private String dataIds;

    /**
     * 序列化后的数据(XML/JSON)
     */
    private String dataPayload;

    /**
     * 数据格式 JSON/XML/EXCEL
     */
    private String dataFormat;

    /**
     * 推送模式 1手动 2自动
     */
    private Integer pushMode;

    /**
     * 优先级 1高 2中 3低
     */
    private Integer priority;

    /**
     * 0待推送 1推送中 2成功 3失败 4部分成功 5已取消
     */
    private Integer pushStatus;

    /**
     * 已重试次数
     */
    private Integer retryCount;

    /**
     * 最大重试次数
     */
    private Integer maxRetry;

    /**
     * 下次重试时间
     */
    private LocalDateTime nextRetryTime;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 第三方回执ID
     */
    private String requestId;

    /**
     * 第三方响应数据
     */
    private String responseData;

    /**
     * 完成时间
     */
    private LocalDateTime finishTime;

}

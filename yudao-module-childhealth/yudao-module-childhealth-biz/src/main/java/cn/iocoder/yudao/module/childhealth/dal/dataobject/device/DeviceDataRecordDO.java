package cn.iocoder.yudao.module.childhealth.dal.dataobject.device;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 设备数据记录表 DO
 *
 * 对应表: device_data_record
 * 模块: D. 移动端功能补全
 * 创建日期: 2026-07-20
 */
@TableName("device_data_record")
@KeySequence("device_data_record_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceDataRecordDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 设备ID
     */
    private Long deviceId;

    /**
     * 设备编码
     */
    private String deviceCode;

    /**
     * 儿童ID
     */
    private Long childId;

    /**
     * 学生ID
     */
    private Long studentId;

    /**
     * 批次ID
     */
    private Long batchId;

    /**
     * 采集时间
     */
    private LocalDateTime recordTime;

    /**
     * 原始数据
     */
    private String rawData;

    /**
     * 解析后数据JSON
     */
    private String parsedDataJson;

    /**
     * 指标JSON
     */
    private String indicatorsJson;

    /**
     * 是否异常 0正常 1异常
     */
    private Integer isAbnormal;

    /**
     * 异常描述
     */
    private String abnormalDesc;

    /**
     * 同步状态 0待同步 1已同步 2失败
     */
    private Integer syncStatus;

    /**
     * 同步时间
     */
    private LocalDateTime syncTime;

    /**
     * 同步到的筛查记录ID
     */
    private Long targetRecordId;

}

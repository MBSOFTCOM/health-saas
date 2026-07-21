package cn.iocoder.yudao.module.childhealth.dal.dataobject.device;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 设备绑定表 DO
 *
 * 对应表: device_binding
 * 模块: D. 移动端功能补全
 * 创建日期: 2026-07-20
 */
@TableName("device_binding")
@KeySequence("device_binding_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceBindingDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 设备编码
     */
    private String deviceCode;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备类型 WEIGHT_SCALE/VISION_TESTER/SCOLIOSIS_DETECTOR/REFRACTOMETER/BLOOD_PRESSURE/SPIROMETER/PLANTAR_PRESSURE
     */
    private String deviceType;

    /**
     * 设备型号
     */
    private String deviceModel;

    /**
     * 厂家
     */
    private String manufacturer;

    /**
     * 所属科室ID
     */
    private Long deptId;

    /**
     * MAC地址
     */
    private String macAddress;

    /**
     * IP地址
     */
    private String ipAddress;

    /**
     * 蓝牙MAC
     */
    private String bluetoothMac;

    /**
     * 连接方式 BLUETOOTH/WIFI/USB/SERIAL
     */
    private String connectionType;

    /**
     * 绑定状态 0未绑定 1已绑定
     */
    private Integer bindStatus;

    /**
     * 在线状态 0离线 1在线
     */
    private Integer onlineStatus;

    /**
     * 最后同步时间
     */
    private LocalDateTime lastSyncTime;

    /**
     * 设备配置JSON
     */
    private String configJson;

    /**
     * 状态 1启用 0停用
     */
    private Integer status;

}

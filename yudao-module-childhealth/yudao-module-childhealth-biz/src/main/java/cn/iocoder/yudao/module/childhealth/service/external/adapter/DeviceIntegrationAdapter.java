package cn.iocoder.yudao.module.childhealth.service.external.adapter;

import cn.iocoder.yudao.module.childhealth.service.external.dto.DeviceMeasurementDTO;

import java.util.List;

/**
 * 身高体重仪等设备对接适配器
 *
 * 适用需求：
 * - 需求 8：对接身高体重仪设备
 *
 * 真实实现需对接具体设备协议（串口/蓝牙/网络/USB），
 * 当前提供 Mock 实现用于开发期联调。
 */
public interface DeviceIntegrationAdapter {

    /**
     * 需求 8：根据设备序列号+HIS 患者ID 接收测量数据
     *
     * @param deviceSerialNo 设备序列号
     * @param hisPatientId HIS 患者ID
     * @return 测量数据，无数据返回 null
     */
    DeviceMeasurementDTO receiveMeasurement(String deviceSerialNo, String hisPatientId);

    /**
     * 需求 8：根据本地儿童档案ID查询设备测量历史
     *
     * @param childId 儿童档案ID
     * @param limit 最大返回数
     * @return 测量数据列表
     */
    List<DeviceMeasurementDTO> fetchMeasurementHistory(Long childId, Integer limit);

    /**
     * 需求 8：查询设备绑定状态
     *
     * @param deviceSerialNo 设备序列号
     * @return 设备是否在线
     */
    boolean isDeviceOnline(String deviceSerialNo);

    /**
     * 需求 8：注册新设备
     *
     * @param deviceSerialNo 设备序列号
     * @param deviceType 设备类型
     * @return 是否注册成功
     */
    boolean registerDevice(String deviceSerialNo, String deviceType);

    /**
     * 需求 8：模拟一次测量数据（用于测试）
     *
     * @param deviceSerialNo 设备序列号
     * @return 模拟的测量数据
     */
    DeviceMeasurementDTO simulateMeasurement(String deviceSerialNo);

}

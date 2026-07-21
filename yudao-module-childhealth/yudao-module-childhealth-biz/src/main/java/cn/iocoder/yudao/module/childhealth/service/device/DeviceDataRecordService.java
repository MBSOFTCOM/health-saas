package cn.iocoder.yudao.module.childhealth.service.device;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.device.DeviceDataRecordDO;

import java.util.List;

/**
 * 设备数据记录 Service 接口
 *
 * 模块: D. 移动端功能补全
 * 创建日期: 2026-07-20
 *
 * @author 芋道源码
 */
public interface DeviceDataRecordService {

    /**
     * 创建设备数据记录
     *
     * @param saveReqVO 创建信息（后续替换为 DeviceDataRecordSaveReqVO）
     * @return 编号
     */
    Long createDeviceDataRecord(Object saveReqVO);

    /**
     * 更新设备数据记录
     *
     * @param saveReqVO 更新信息（后续替换为 DeviceDataRecordSaveReqVO）
     */
    void updateDeviceDataRecord(Object saveReqVO);

    /**
     * 删除设备数据记录
     *
     * @param id 编号
     */
    void deleteDeviceDataRecord(Long id);

    /**
     * 获得设备数据记录
     *
     * @param id 编号
     * @return 设备数据记录
     */
    DeviceDataRecordDO getDeviceDataRecord(Long id);

    /**
     * 获得设备数据记录分页
     *
     * @param pageParam 分页查询（后续替换为 DeviceDataRecordPageReqVO）
     * @return 设备数据记录分页
     */
    PageResult<DeviceDataRecordDO> getDeviceDataRecordPage(PageParam pageParam);

    /**
     * 按儿童ID查询数据记录列表
     *
     * @param childId 儿童ID
     * @return 设备数据记录列表
     */
    List<DeviceDataRecordDO> selectListByChildId(Long childId);

    /**
     * 解析原始数据
     *
     * @param id 设备数据记录ID
     */
    void parseRawData(Long id);

    /**
     * 自动识别异常
     *
     * @param id 设备数据记录ID
     */
    void autoIdentifyAbnormal(Long id);

    /**
     * 同步到筛查记录
     *
     * @param id 设备数据记录ID
     */
    void syncToScreeningRecord(Long id);

}

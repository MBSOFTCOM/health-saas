package cn.iocoder.yudao.module.childhealth.service.device;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.device.DeviceBindingDO;

import java.util.List;

/**
 * 设备绑定 Service 接口
 *
 * 模块: D. 移动端功能补全
 * 创建日期: 2026-07-20
 *
 * @author 芋道源码
 */
public interface DeviceBindingService {

    /**
     * 创建设备绑定
     *
     * @param saveReqVO 创建信息（后续替换为 DeviceBindingSaveReqVO）
     * @return 编号
     */
    Long createDeviceBinding(Object saveReqVO);

    /**
     * 更新设备绑定
     *
     * @param saveReqVO 更新信息（后续替换为 DeviceBindingSaveReqVO）
     */
    void updateDeviceBinding(Object saveReqVO);

    /**
     * 删除设备绑定
     *
     * @param id 编号
     */
    void deleteDeviceBinding(Long id);

    /**
     * 获得设备绑定
     *
     * @param id 编号
     * @return 设备绑定
     */
    DeviceBindingDO getDeviceBinding(Long id);

    /**
     * 获得设备绑定分页
     *
     * @param pageParam 分页查询（后续替换为 DeviceBindingPageReqVO）
     * @return 设备绑定分页
     */
    PageResult<DeviceBindingDO> getDeviceBindingPage(PageParam pageParam);

    /**
     * 按设备编码查询
     *
     * @param deviceCode 设备编码
     * @return 设备绑定
     */
    DeviceBindingDO selectByCode(String deviceCode);

    /**
     * 查询所有启用且已绑定的设备列表
     *
     * @return 设备绑定列表
     */
    List<DeviceBindingDO> selectActiveBoundList();

    /**
     * 更新设备在线状态（在线状态监测）
     *
     * @param id 设备ID
     * @param onlineStatus 在线状态 0离线 1在线
     */
    void updateOnlineStatus(Long id, Integer onlineStatus);

    /**
     * 同步设备数据（数据同步）
     *
     * @param id 设备ID
     */
    void syncDeviceData(Long id);

}

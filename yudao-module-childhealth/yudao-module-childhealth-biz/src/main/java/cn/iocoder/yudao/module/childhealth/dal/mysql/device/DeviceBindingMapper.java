package cn.iocoder.yudao.module.childhealth.dal.mysql.device;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.device.DeviceBindingDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 设备绑定表 Mapper
 *
 * 模块: D. 移动端功能补全
 * 创建日期: 2026-07-20
 */
@Mapper
public interface DeviceBindingMapper extends BaseMapperX<DeviceBindingDO> {

    /**
     * 按设备编码查询
     */
    default DeviceBindingDO selectByDeviceCode(String deviceCode) {
        return selectOne(DeviceBindingDO::getDeviceCode, deviceCode);
    }

    /**
     * 按MAC地址查询
     */
    default DeviceBindingDO selectByMacAddress(String macAddress) {
        return selectOne(DeviceBindingDO::getMacAddress, macAddress);
    }

    /**
     * 按设备类型查询启用设备列表
     */
    default List<DeviceBindingDO> selectListByDeviceType(String deviceType) {
        return selectList(new LambdaQueryWrapperX<DeviceBindingDO>()
                .eqIfPresent(DeviceBindingDO::getDeviceType, deviceType)
                .eqIfPresent(DeviceBindingDO::getStatus, 1)
                .orderByAsc(DeviceBindingDO::getDeviceCode));
    }

    /**
     * 按科室ID查询已绑定设备列表
     */
    default List<DeviceBindingDO> selectListByDeptId(Long deptId) {
        return selectList(new LambdaQueryWrapperX<DeviceBindingDO>()
                .eqIfPresent(DeviceBindingDO::getDeptId, deptId)
                .eqIfPresent(DeviceBindingDO::getBindStatus, 1)
                .orderByDesc(DeviceBindingDO::getCreateTime));
    }

    /**
     * 按在线状态查询设备列表
     */
    default List<DeviceBindingDO> selectListByOnlineStatus(Integer onlineStatus) {
        return selectList(new LambdaQueryWrapperX<DeviceBindingDO>()
                .eqIfPresent(DeviceBindingDO::getOnlineStatus, onlineStatus)
                .eqIfPresent(DeviceBindingDO::getStatus, 1)
                .orderByAsc(DeviceBindingDO::getDeviceType));
    }

    /**
     * 查询所有启用且已绑定的设备
     */
    default List<DeviceBindingDO> selectActiveBoundList() {
        return selectList(new LambdaQueryWrapperX<DeviceBindingDO>()
                .eqIfPresent(DeviceBindingDO::getStatus, 1)
                .eqIfPresent(DeviceBindingDO::getBindStatus, 1)
                .orderByDesc(DeviceBindingDO::getLastSyncTime));
    }

}

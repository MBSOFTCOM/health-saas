package cn.iocoder.yudao.module.childhealth.dal.mysql.device;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.device.DeviceDataRecordDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 设备数据记录表 Mapper
 *
 * 模块: D. 移动端功能补全
 * 创建日期: 2026-07-20
 */
@Mapper
public interface DeviceDataRecordMapper extends BaseMapperX<DeviceDataRecordDO> {

    /**
     * 按设备ID查询数据记录列表
     */
    default List<DeviceDataRecordDO> selectListByDeviceId(Long deviceId) {
        return selectList(new LambdaQueryWrapperX<DeviceDataRecordDO>()
                .eqIfPresent(DeviceDataRecordDO::getDeviceId, deviceId)
                .orderByDesc(DeviceDataRecordDO::getRecordTime));
    }

    /**
     * 按儿童ID查询数据记录列表
     */
    default List<DeviceDataRecordDO> selectListByChildId(Long childId) {
        return selectList(new LambdaQueryWrapperX<DeviceDataRecordDO>()
                .eqIfPresent(DeviceDataRecordDO::getChildId, childId)
                .orderByDesc(DeviceDataRecordDO::getRecordTime));
    }

    /**
     * 按批次ID查询数据记录列表
     */
    default List<DeviceDataRecordDO> selectListByBatchId(Long batchId) {
        return selectList(new LambdaQueryWrapperX<DeviceDataRecordDO>()
                .eqIfPresent(DeviceDataRecordDO::getBatchId, batchId)
                .orderByDesc(DeviceDataRecordDO::getRecordTime));
    }

    /**
     * 按同步状态查询数据记录列表
     */
    default List<DeviceDataRecordDO> selectListBySyncStatus(Integer syncStatus) {
        return selectList(new LambdaQueryWrapperX<DeviceDataRecordDO>()
                .eqIfPresent(DeviceDataRecordDO::getSyncStatus, syncStatus)
                .orderByAsc(DeviceDataRecordDO::getRecordTime));
    }

    /**
     * 按同步到的筛查记录ID查询
     */
    default DeviceDataRecordDO selectByTargetRecordId(Long targetRecordId) {
        return selectOne(DeviceDataRecordDO::getTargetRecordId, targetRecordId);
    }

    /**
     * 按设备ID查询异常数据记录
     */
    default List<DeviceDataRecordDO> selectAbnormalListByDeviceId(Long deviceId) {
        return selectList(new LambdaQueryWrapperX<DeviceDataRecordDO>()
                .eqIfPresent(DeviceDataRecordDO::getDeviceId, deviceId)
                .eqIfPresent(DeviceDataRecordDO::getIsAbnormal, 1)
                .orderByDesc(DeviceDataRecordDO::getRecordTime));
    }

}

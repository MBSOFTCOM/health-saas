package cn.iocoder.yudao.module.childhealth.service.device.impl;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.device.DeviceBindingDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.device.DeviceBindingMapper;
import cn.iocoder.yudao.module.childhealth.service.device.DeviceBindingService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.DEVICE_BINDING_CODE_DUPLICATE;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.DEVICE_BINDING_NOT_EXISTS;

/**
 * 设备绑定 Service 实现类
 *
 * 模块: D. 移动端功能补全
 * 创建日期: 2026-07-20
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class DeviceBindingServiceImpl implements DeviceBindingService {

    @Resource
    private DeviceBindingMapper deviceBindingMapper;

    @Override
    public Long createDeviceBinding(Object saveReqVO) {
        // TODO 后续替换为 DeviceBindingSaveReqVO
        DeviceBindingDO record = BeanUtils.toBean(saveReqVO, DeviceBindingDO.class);
        // 设备编码唯一性校验
        if (record.getDeviceCode() != null
                && deviceBindingMapper.selectByDeviceCode(record.getDeviceCode()) != null) {
            throw exception(DEVICE_BINDING_CODE_DUPLICATE);
        }
        deviceBindingMapper.insert(record);
        return record.getId();
    }

    @Override
    public void updateDeviceBinding(Object saveReqVO) {
        // TODO 后续替换为 DeviceBindingSaveReqVO
        DeviceBindingDO updateObj = BeanUtils.toBean(saveReqVO, DeviceBindingDO.class);
        validateDeviceBindingExists(updateObj.getId());
        // 设备编码唯一性校验
        if (updateObj.getDeviceCode() != null) {
            DeviceBindingDO existing = deviceBindingMapper.selectByDeviceCode(updateObj.getDeviceCode());
            if (existing != null && !existing.getId().equals(updateObj.getId())) {
                throw exception(DEVICE_BINDING_CODE_DUPLICATE);
            }
        }
        deviceBindingMapper.updateById(updateObj);
    }

    @Override
    public void deleteDeviceBinding(Long id) {
        validateDeviceBindingExists(id);
        deviceBindingMapper.deleteById(id);
    }

    @Override
    public DeviceBindingDO getDeviceBinding(Long id) {
        return deviceBindingMapper.selectById(id);
    }

    @Override
    public PageResult<DeviceBindingDO> getDeviceBindingPage(PageParam pageParam) {
        // TODO 后续替换为 DeviceBindingPageReqVO，并增加查询条件
        return deviceBindingMapper.selectPage(pageParam, null);
    }

    @Override
    public DeviceBindingDO selectByCode(String deviceCode) {
        return deviceBindingMapper.selectByDeviceCode(deviceCode);
    }

    @Override
    public List<DeviceBindingDO> selectActiveBoundList() {
        return deviceBindingMapper.selectActiveBoundList();
    }

    @Override
    public void updateOnlineStatus(Long id, Integer onlineStatus) {
        // TODO 实现在线状态监测：可结合心跳上报/MQTT上下线事件触发，并推送状态变更通知到前端
        log.info("[updateOnlineStatus] 更新设备在线状态 id={} onlineStatus={}", id, onlineStatus);
        validateDeviceBindingExists(id);
        DeviceBindingDO updateObj = new DeviceBindingDO();
        updateObj.setId(id);
        updateObj.setOnlineStatus(onlineStatus);
        deviceBindingMapper.updateById(updateObj);
    }

    @Override
    public void syncDeviceData(Long id) {
        // TODO 实现数据同步：拉取设备最新采集数据 -> 调用 DeviceDataRecordService 落库 -> 回写 lastSyncTime
        log.info("[syncDeviceData] 同步设备数据 id={}", id);
        validateDeviceBindingExists(id);
        DeviceBindingDO updateObj = new DeviceBindingDO();
        updateObj.setId(id);
        updateObj.setLastSyncTime(LocalDateTime.now());
        deviceBindingMapper.updateById(updateObj);
    }

    private void validateDeviceBindingExists(Long id) {
        if (id == null || deviceBindingMapper.selectById(id) == null) {
            throw exception(DEVICE_BINDING_NOT_EXISTS);
        }
    }

}

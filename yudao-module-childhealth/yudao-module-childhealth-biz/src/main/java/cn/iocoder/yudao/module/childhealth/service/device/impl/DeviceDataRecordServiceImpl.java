package cn.iocoder.yudao.module.childhealth.service.device.impl;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.device.DeviceDataRecordDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.device.DeviceDataRecordMapper;
import cn.iocoder.yudao.module.childhealth.service.device.DeviceDataRecordService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.DEVICE_DATA_RECORD_NOT_EXISTS;

/**
 * 设备数据记录 Service 实现类
 *
 * 模块: D. 移动端功能补全
 * 创建日期: 2026-07-20
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class DeviceDataRecordServiceImpl implements DeviceDataRecordService {

    @Resource
    private DeviceDataRecordMapper deviceDataRecordMapper;

    @Override
    public Long createDeviceDataRecord(Object saveReqVO) {
        // TODO 后续替换为 DeviceDataRecordSaveReqVO
        DeviceDataRecordDO record = BeanUtils.toBean(saveReqVO, DeviceDataRecordDO.class);
        deviceDataRecordMapper.insert(record);
        return record.getId();
    }

    @Override
    public void updateDeviceDataRecord(Object saveReqVO) {
        // TODO 后续替换为 DeviceDataRecordSaveReqVO
        DeviceDataRecordDO updateObj = BeanUtils.toBean(saveReqVO, DeviceDataRecordDO.class);
        validateDeviceDataRecordExists(updateObj.getId());
        deviceDataRecordMapper.updateById(updateObj);
    }

    @Override
    public void deleteDeviceDataRecord(Long id) {
        validateDeviceDataRecordExists(id);
        deviceDataRecordMapper.deleteById(id);
    }

    @Override
    public DeviceDataRecordDO getDeviceDataRecord(Long id) {
        return deviceDataRecordMapper.selectById(id);
    }

    @Override
    public PageResult<DeviceDataRecordDO> getDeviceDataRecordPage(PageParam pageParam) {
        // TODO 后续替换为 DeviceDataRecordPageReqVO，并增加查询条件
        return deviceDataRecordMapper.selectPage(pageParam, null);
    }

    @Override
    public List<DeviceDataRecordDO> selectListByChildId(Long childId) {
        return deviceDataRecordMapper.selectListByChildId(childId);
    }

    @Override
    public void parseRawData(Long id) {
        // TODO 实现原始数据解析：根据设备类型解析 rawData -> parsedDataJson + indicatorsJson
        log.info("[parseRawData] 解析原始数据 id={}", id);
        validateDeviceDataRecordExists(id);
        DeviceDataRecordDO record = deviceDataRecordMapper.selectById(id);
        // TODO: 根据 DeviceBinding.deviceType 分发到不同解析器（体重秤/视力筛查仪/脊柱侧弯检测仪等）
        DeviceDataRecordDO updateObj = new DeviceDataRecordDO();
        updateObj.setId(id);
        updateObj.setParsedDataJson(record.getRawData()); // 占位：直接透传
        deviceDataRecordMapper.updateById(updateObj);
    }

    @Override
    public void autoIdentifyAbnormal(Long id) {
        // TODO 实现异常自动识别：基于 AbnormalRuleService 评估 indicatorsJson -> 回写 isAbnormal + abnormalDesc
        log.info("[autoIdentifyAbnormal] 自动识别异常 id={}", id);
        validateDeviceDataRecordExists(id);
        DeviceDataRecordDO updateObj = new DeviceDataRecordDO();
        updateObj.setId(id);
        updateObj.setIsAbnormal(0); // 占位：默认正常
        deviceDataRecordMapper.updateById(updateObj);
    }

    @Override
    public void syncToScreeningRecord(Long id) {
        // TODO 实现同步到筛查记录：根据 targetRecordId 回写 ScreeningResultDetail，并更新 syncStatus/syncTime
        log.info("[syncToScreeningRecord] 同步到筛查记录 id={}", id);
        validateDeviceDataRecordExists(id);
        DeviceDataRecordDO updateObj = new DeviceDataRecordDO();
        updateObj.setId(id);
        updateObj.setSyncStatus(1);
        updateObj.setSyncTime(LocalDateTime.now());
        deviceDataRecordMapper.updateById(updateObj);
    }

    private void validateDeviceDataRecordExists(Long id) {
        if (id == null || deviceDataRecordMapper.selectById(id) == null) {
            throw exception(DEVICE_DATA_RECORD_NOT_EXISTS);
        }
    }

}

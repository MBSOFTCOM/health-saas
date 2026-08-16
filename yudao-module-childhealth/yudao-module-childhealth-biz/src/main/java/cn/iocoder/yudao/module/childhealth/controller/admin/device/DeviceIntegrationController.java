package cn.iocoder.yudao.module.childhealth.controller.admin.device;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.api.device.dto.DeviceCollectRequest;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.device.DeviceBindingDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.device.DeviceDataRecordDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.device.DeviceBindingMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.device.DeviceDataRecordMapper;
import cn.iocoder.yudao.module.childhealth.service.device.DeviceBindingService;
import cn.iocoder.yudao.module.childhealth.service.device.DeviceIntegrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

/**
 * 设备集成 Controller
 *
 * 对应前端 api/screen/device.js
 */
@Tag(name = "管理后台 - 设备集成")
@RestController
@RequestMapping("/childhealth/device")
@Validated
public class DeviceIntegrationController {

    @Resource
    private DeviceIntegrationService service;

    @Resource
    private DeviceBindingService deviceBindingService;

    @Resource
    private DeviceBindingMapper deviceBindingMapper;

    @Resource
    private DeviceDataRecordMapper deviceDataRecordMapper;

    // ==================== 原有接口 ====================

    @PostMapping("/collect")
    @Operation(summary = "设备数据采集")
    @Parameter(name = "request", description = "采集请求", required = true)
    public CommonResult<List<Long>> collect(@Valid @RequestBody DeviceCollectRequest request) {
        return success(service.collect(request));
    }

    // ==================== 设备管理（CRUD） ====================

    @GetMapping("/list")
    @Operation(summary = "设备列表")
    public CommonResult<List<DeviceBindingDO>> getDeviceList(@RequestParam(value = "deviceType", required = false) String deviceType) {
        if (deviceType != null && !deviceType.isEmpty()) {
            return success(deviceBindingMapper.selectList(
                    new LambdaQueryWrapperX<DeviceBindingDO>()
                            .eq(DeviceBindingDO::getDeviceType, deviceType)
                            .orderByDesc(DeviceBindingDO::getId)));
        }
        return success(deviceBindingService.selectActiveBoundList());
    }

    @GetMapping("/page")
    @Operation(summary = "设备分页查询")
    public CommonResult<PageResult<DeviceBindingDO>> getDevicePage(@Valid PageParam pageParam) {
        return success(deviceBindingService.getDeviceBindingPage(pageParam));
    }

    @GetMapping("/get")
    @Operation(summary = "设备详情")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<DeviceBindingDO> getDeviceDetail(@RequestParam("id") Long id) {
        return success(deviceBindingService.getDeviceBinding(id));
    }

    @PostMapping("/create")
    @Operation(summary = "新增设备")
    public CommonResult<Long> createDevice(@RequestBody Map<String, Object> data) {
        return success(deviceBindingService.createDeviceBinding(data));
    }

    @PutMapping("/update")
    @Operation(summary = "修改设备")
    public CommonResult<Boolean> updateDevice(@RequestBody Map<String, Object> data) {
        deviceBindingService.updateDeviceBinding(data);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除设备")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteDevice(@RequestParam("id") Long id) {
        deviceBindingService.deleteDeviceBinding(id);
        return success(true);
    }

    // ==================== 设备配对与状态 ====================

    @PostMapping("/pair")
    @Operation(summary = "设备配对", description = "按品牌、型号配对设备，更新绑定状态为已绑定")
    public CommonResult<Boolean> pairDevice(@RequestBody Map<String, Object> data) {
        String deviceCode = (String) data.get("deviceCode");
        String macAddress = (String) data.get("macAddress");
        DeviceBindingDO device = null;
        if (deviceCode != null && !deviceCode.isEmpty()) {
            device = deviceBindingService.selectByCode(deviceCode);
        }
        if (device == null && macAddress != null && !macAddress.isEmpty()) {
            device = deviceBindingMapper.selectOne(
                    new LambdaQueryWrapperX<DeviceBindingDO>()
                            .eq(DeviceBindingDO::getMacAddress, macAddress));
        }
        if (device == null) {
            // 设备不存在则按提交信息新建一条绑定记录
            DeviceBindingDO newDevice = new DeviceBindingDO();
            newDevice.setDeviceCode(deviceCode);
            newDevice.setDeviceName((String) data.get("deviceName"));
            newDevice.setDeviceType((String) data.get("deviceType"));
            newDevice.setDeviceModel((String) data.get("deviceModel"));
            newDevice.setManufacturer((String) data.get("brand"));
            newDevice.setMacAddress(macAddress);
            newDevice.setBindStatus(1);
            newDevice.setOnlineStatus(1);
            newDevice.setStatus(1);
            newDevice.setLastSyncTime(LocalDateTime.now());
            deviceBindingMapper.insert(newDevice);
            return success(true);
        }
        // 已存在则更新为已绑定+在线
        DeviceBindingDO update = new DeviceBindingDO();
        update.setId(device.getId());
        update.setBindStatus(1);
        update.setOnlineStatus(1);
        update.setLastSyncTime(LocalDateTime.now());
        deviceBindingMapper.updateById(update);
        return success(true);
    }

    @GetMapping("/status")
    @Operation(summary = "设备状态检测")
    @Parameter(name = "deviceId", description = "设备ID或设备编码", required = true)
    public CommonResult<DeviceBindingDO> checkDeviceStatus(@RequestParam("deviceId") String deviceId) {
        // deviceId 可能是数字 ID，也可能是设备编码
        DeviceBindingDO device = null;
        try {
            Long id = Long.parseLong(deviceId);
            device = deviceBindingService.getDeviceBinding(id);
        } catch (NumberFormatException ignored) {
            device = deviceBindingService.selectByCode(deviceId);
        }
        return success(device);
    }

    @PostMapping("/sync")
    @Operation(summary = "设备数据同步", description = "触发设备数据同步")
    @Parameter(name = "id", description = "设备ID", required = true)
    public CommonResult<Boolean> syncDeviceData(@RequestParam("id") Long id) {
        deviceBindingService.syncDeviceData(id);
        return success(true);
    }

    @PostMapping("/sync-verify")
    @Operation(summary = "数据同步校验", description = "校验同步数据完整性，当前为占位实现")
    public CommonResult<Boolean> verifySyncData(@RequestBody Map<String, Object> data) {
        // 实际项目应校验 DeviceDataRecordDO.syncStatus 与设备原始数据一致性
        return success(true);
    }

    @GetMapping("/alerts")
    @Operation(summary = "设备异常提醒列表", description = "查询设备相关的异常数据记录")
    @Parameter(name = "deviceId", description = "设备ID或设备编码")
    public CommonResult<List<DeviceDataRecordDO>> getDeviceAlerts(
            @RequestParam(value = "deviceId", required = false) String deviceId) {
        LambdaQueryWrapperX<DeviceDataRecordDO> wrapper = new LambdaQueryWrapperX<DeviceDataRecordDO>()
                .eq(DeviceDataRecordDO::getIsAbnormal, 1)
                .orderByDesc(DeviceDataRecordDO::getId);
        if (deviceId != null && !deviceId.isEmpty()) {
            wrapper.eq(DeviceDataRecordDO::getDeviceCode, deviceId);
        }
        return success(deviceDataRecordMapper.selectList(wrapper));
    }

}

package cn.iocoder.yudao.module.childhealth.service.external.mock;

import cn.iocoder.yudao.module.childhealth.service.external.adapter.DeviceIntegrationAdapter;
import cn.iocoder.yudao.module.childhealth.service.external.dto.DeviceMeasurementDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 身高体重仪等设备对接 Mock 实现
 *
 * 模拟设备数据接收，覆盖：
 * - 体重测量（精度 0.01kg）
 * - 身高测量（精度 0.1cm）
 * - 头围测量
 * - BMI 自动计算
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "childhealth.external.device", name = "mode", havingValue = "mock", matchIfMissing = true)
public class MockDeviceIntegrationAdapter implements DeviceIntegrationAdapter {

    @Override
    public DeviceMeasurementDTO receiveMeasurement(String deviceSerialNo, String hisPatientId) {
        log.info("[MockDevice] receiveMeasurement device={} patient={}", deviceSerialNo, hisPatientId);
        return simulateMeasurement(deviceSerialNo);
    }

    @Override
    public List<DeviceMeasurementDTO> fetchMeasurementHistory(Long childId, Integer limit) {
        log.info("[MockDevice] fetchMeasurementHistory childId={} limit={}", childId, limit);
        List<DeviceMeasurementDTO> list = new ArrayList<>();
        int count = limit != null ? Math.min(limit, 5) : 5;
        for (int i = 0; i < count; i++) {
            DeviceMeasurementDTO dto = new DeviceMeasurementDTO();
            dto.setDeviceSerialNo("MOCK_DEVICE_001");
            dto.setDeviceType("INTEGRATED");
            dto.setChildId(childId);
            dto.setWeight(8.5 + i * 0.3 + ThreadLocalRandom.current().nextDouble(-0.1, 0.1));
            dto.setHeight(70.0 + i * 1.5 + ThreadLocalRandom.current().nextDouble(-0.5, 0.5));
            dto.setHeadCircumference(43.0 + i * 0.3);
            dto.setBmi(calculateBmi(dto.getWeight(), dto.getHeight()));
            dto.setMeasureTime(LocalDateTime.now().minusMonths(i));
            dto.setTransportType("NETWORK");
            dto.setRawData(String.format("{\"weight\":%.2f,\"height\":%.1f,\"head\":%.1f}",
                    dto.getWeight(), dto.getHeight(), dto.getHeadCircumference()));
            list.add(dto);
        }
        return list;
    }

    @Override
    public boolean isDeviceOnline(String deviceSerialNo) {
        log.info("[MockDevice] isDeviceOnline device={}", deviceSerialNo);
        return true;
    }

    @Override
    public boolean registerDevice(String deviceSerialNo, String deviceType) {
        log.info("[MockDevice] registerDevice device={} type={}", deviceSerialNo, deviceType);
        return true;
    }

    @Override
    public DeviceMeasurementDTO simulateMeasurement(String deviceSerialNo) {
        log.info("[MockDevice] simulateMeasurement device={}", deviceSerialNo);
        DeviceMeasurementDTO dto = new DeviceMeasurementDTO();
        dto.setDeviceSerialNo(deviceSerialNo);
        dto.setDeviceType("INTEGRATED");
        dto.setHisPatientId("MOCK_HIS_001");
        dto.setWeight(9.2 + ThreadLocalRandom.current().nextDouble(-0.2, 0.2));
        dto.setHeight(73.5 + ThreadLocalRandom.current().nextDouble(-0.5, 0.5));
        dto.setHeadCircumference(44.8 + ThreadLocalRandom.current().nextDouble(-0.2, 0.2));
        dto.setBmi(calculateBmi(dto.getWeight(), dto.getHeight()));
        dto.setTemperature(36.5 + ThreadLocalRandom.current().nextDouble(-0.3, 0.3));
        dto.setMeasureTime(LocalDateTime.now());
        dto.setTransportType("NETWORK");
        dto.setRawData(String.format("{\"weight\":%.2f,\"height\":%.1f,\"head\":%.1f,\"temp\":%.1f}",
                dto.getWeight(), dto.getHeight(), dto.getHeadCircumference(), dto.getTemperature()));
        return dto;
    }

    /**
     * BMI = 体重(kg) / 身高(m)^2
     */
    private Double calculateBmi(Double weight, Double height) {
        if (weight == null || height == null || height <= 0) return null;
        double h = height / 100.0;
        return Math.round(weight / (h * h) * 100.0) / 100.0;
    }

}

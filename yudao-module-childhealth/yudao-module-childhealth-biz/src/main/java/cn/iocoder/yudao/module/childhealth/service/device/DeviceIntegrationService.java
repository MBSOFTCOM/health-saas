package cn.iocoder.yudao.module.childhealth.service.device;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.module.childhealth.api.device.dto.DeviceCollectRequest;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.device.DeviceIntegrationLogDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.exam.PhysicalExamRecordDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.ScreeningResultDetailDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.device.DeviceIntegrationLogMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.exam.PhysicalExamRecordMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.workflow.HealthCheckupMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.screening.ScreeningRecordMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.screening.ScreeningResultDetailMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 设备数据集成 Service
 *
 * 模块: 需求8 对接身高体重仪自动采集
 *
 * 当 examId 不为空时，将身高体重仪、体温仪等数据写入 physical_exam_record 表；
 * 当 screeningRecordId 不为空时，将筛查设备数据写入 screening_result_detail 表。
 *
 * @author 系统
 */
@Service
public class DeviceIntegrationService {

    @Resource private DeviceIntegrationLogMapper deviceIntegrationLogMapper;
    @Resource private HealthCheckupMapper healthCheckupMapper;
    @Resource private ScreeningRecordMapper screeningRecordMapper;
    @Resource private ScreeningResultDetailMapper screeningResultDetailMapper;
    @Resource private PhysicalExamRecordMapper physicalExamRecordMapper;

    /**
     * 已知设备类型 → 字段名映射
     * 设备采集字段名 → PhysicalExamRecordDO 字段名
     */
    private static final Map<String, String> PHYSICAL_FIELD_MAP = new LinkedHashMap<>();
    static {
        PHYSICAL_FIELD_MAP.put("HEIGHT", "height");
        PHYSICAL_FIELD_MAP.put("WEIGHT", "weight");
        PHYSICAL_FIELD_MAP.put("HEAD_CIRCUMFERENCE", "headCircumference");
        PHYSICAL_FIELD_MAP.put("CHEST_CIRCUMFERENCE", "chestCircumference");
        PHYSICAL_FIELD_MAP.put("BODY_TEMP", "bodyTemp");
        PHYSICAL_FIELD_MAP.put("HEART_RATE", "heartRate");
        PHYSICAL_FIELD_MAP.put("RESPIRATORY_RATE", "respiratoryRate");
        PHYSICAL_FIELD_MAP.put("BMI", "bmi");
    }

    /**
     * 支持自动计算的设备类型
     */
    private static final Set<String> AUTO_CALC_DEVICE_TYPES = new HashSet<>(
            Arrays.asList("HEIGHT_WEIGHT_SCALE", "BABY_SCALE", "INFANT_MEASUREMENT"));

    @Transactional
    public List<Long> collect(DeviceCollectRequest request) {
        if (!JsonUtils.isJson(request.getDataContent())) {
            throw error("设备数据必须是JSON");
        }
        if (request.getExamId() != null && healthCheckupMapper.selectById(request.getExamId()) == null) {
            throw error("体检记录不存在");
        }
        if (request.getScreeningRecordId() != null && screeningRecordMapper.selectById(request.getScreeningRecordId()) == null) {
            throw error("筛查记录不存在");
        }
        JsonNode data = JsonUtils.parseTree(request.getDataContent());
        List<Long> ids = new ArrayList<>();

        // 筛查数据：写入 screening_result_detail
        if (request.getScreeningRecordId() != null) {
            JsonNode items = data.path("items");
            if (!items.isArray() || items.isEmpty()) {
                throw error("筛查设备数据缺少items");
            }
            for (JsonNode item : items) {
                ids.add(saveDetail(request, item));
            }
        }

        // 体检数据：写入 physical_exam_record（需求8）
        if (request.getExamId() != null) {
            Long recordId = savePhysicalExamRecord(request.getExamId(), request, data);
            if (recordId != null) {
                ids.add(recordId);
            }
        }

        // 记录集成日志
        DeviceIntegrationLogDO log = new DeviceIntegrationLogDO();
        log.setDeviceType(request.getDeviceType());
        log.setDeviceCode(request.getDeviceCode());
        log.setExamId(request.getExamId());
        log.setDataContent(request.getDataContent());
        log.setIntegrationTime(LocalDateTime.now());
        log.setStatus(1);
        log.setCreateTime(LocalDateTime.now());
        deviceIntegrationLogMapper.insert(log);

        return ids;
    }

    /**
     * 保存体检数据到 PhysicalExamRecordDO（需求8）
     *
     * 支持 dataContent 两种格式：
     * 1. 扁平格式：{"height": "76.5", "weight": "9.5", "headCircumference": "45.2"}
     * 2. items 数组格式：{"items": [{"itemCode": "HEIGHT", "value": "76.5"}, ...]}
     */
    private Long savePhysicalExamRecord(Long examId, DeviceCollectRequest request, JsonNode data) {
        // 查找已有记录（同一体检记录只保留一份 PhysicalExamRecord）
        PhysicalExamRecordDO record = physicalExamRecordMapper.selectOne(
                Wrappers.<PhysicalExamRecordDO>lambdaQuery()
                        .eq(PhysicalExamRecordDO::getExamId, examId)
                        .last("LIMIT 1"));
        boolean isNew = record == null;
        if (isNew) {
            record = new PhysicalExamRecordDO();
            record.setExamId(examId);
            record.setCreateTime(LocalDateTime.now());
        }

        // 收集所有写入字段
        Map<String, BigDecimal> collected = new LinkedHashMap<>();
        // 1. 扁平字段
        for (Map.Entry<String, String> entry : PHYSICAL_FIELD_MAP.entrySet()) {
            String fieldName = entry.getKey();
            String dbField = entry.getValue();
            JsonNode node = data.path(fieldName);
            if (!node.isMissingNode() && !node.asText().isBlank()) {
                BigDecimal v = parseDecimal(node.asText());
                if (v != null) {
                    collected.put(dbField, v);
                }
            }
        }
        // 2. items 数组
        JsonNode items = data.path("items");
        if (items.isArray()) {
            for (JsonNode item : items) {
                String code = item.path("itemCode").asText();
                String value = item.path("value").asText();
                if (code.isBlank() || value.isBlank()) continue;
                String dbField = PHYSICAL_FIELD_MAP.get(code.toUpperCase());
                if (dbField == null) {
                    // 兼容小写命名
                    dbField = PHYSICAL_FIELD_MAP.get(code);
                }
                if (dbField == null) continue;
                BigDecimal v = parseDecimal(value);
                if (v != null) {
                    collected.put(dbField, v);
                }
            }
        }

        if (collected.isEmpty()) {
            return null;
        }

        // 逐一写入字段
        if (collected.containsKey("height")) record.setHeight(collected.get("height"));
        if (collected.containsKey("weight")) record.setWeight(collected.get("weight"));
        if (collected.containsKey("headCircumference")) record.setHeadCircumference(collected.get("headCircumference"));
        if (collected.containsKey("chestCircumference")) record.setChestCircumference(collected.get("chestCircumference"));
        if (collected.containsKey("bodyTemp")) record.setBodyTemp(collected.get("bodyTemp"));
        if (collected.containsKey("heartRate")) {
            BigDecimal hr = collected.get("heartRate");
            record.setHeartRate(hr.intValue());
        }
        if (collected.containsKey("respiratoryRate")) {
            BigDecimal rr = collected.get("respiratoryRate");
            record.setRespiratoryRate(rr.intValue());
        }
        if (collected.containsKey("bmi")) {
            record.setBmi(collected.get("bmi"));
        } else if (record.getHeight() != null && record.getWeight() != null
                && record.getHeight().signum() > 0) {
            // 自动计算 BMI = weight / (height/100)^2
            BigDecimal heightM = record.getHeight().divide(new BigDecimal("100"), 6, java.math.RoundingMode.HALF_UP);
            BigDecimal bmi = record.getWeight().divide(
                    heightM.multiply(heightM), 2, java.math.RoundingMode.HALF_UP);
            record.setBmi(bmi);
        }

        // 标记异常
        record.setIsAbnormal(checkAbnormal(record));

        if (isNew) {
            physicalExamRecordMapper.insert(record);
        } else {
            physicalExamRecordMapper.updateById(record);
        }
        return record.getId();
    }

    /**
     * 简单的异常判定（与异常规则引擎互补）
     */
    private Boolean checkAbnormal(PhysicalExamRecordDO record) {
        if (record.getBodyTemp() != null && record.getBodyTemp().compareTo(new BigDecimal("37.5")) >= 0) {
            return true;
        }
        if (record.getHeartRate() != null) {
            int hr = record.getHeartRate();
            if (hr > 160 || hr < 80) return true;
        }
        if (record.getRespiratoryRate() != null) {
            int rr = record.getRespiratoryRate();
            if (rr > 60 || rr < 20) return true;
        }
        if (record.getBmi() != null) {
            // 简单的 BMI 异常判定：> 25 或 < 12
            if (record.getBmi().compareTo(new BigDecimal("25")) > 0
                    || record.getBmi().compareTo(new BigDecimal("12")) < 0) {
                return true;
            }
        }
        return false;
    }

    private BigDecimal parseDecimal(String value) {
        try {
            return new BigDecimal(value);
        } catch (Exception e) {
            return null;
        }
    }

    private Long saveDetail(DeviceCollectRequest request, JsonNode item) {
        String code = item.path("itemCode").asText();
        String value = item.path("value").asText();
        if (code.isBlank() || value.isBlank()) {
            throw error("设备项目缺少itemCode或value");
        }
        ScreeningResultDetailDO detail = screeningResultDetailMapper.selectOne(
                Wrappers.<ScreeningResultDetailDO>lambdaQuery()
                        .eq(ScreeningResultDetailDO::getRecordId, request.getScreeningRecordId())
                        .eq(ScreeningResultDetailDO::getItemCode, code)
                        .last("LIMIT 1"));
        if (detail == null) {
            detail = new ScreeningResultDetailDO();
            detail.setRecordId(request.getScreeningRecordId());
            detail.setItemCode(code);
            detail.setCreateTime(LocalDateTime.now());
        }
        detail.setItemValue(value);
        detail.setIsAbnormal(item.path("isAbnormal").asBoolean(false) ? 1 : 0);
        detail.setDeviceCode(request.getDeviceCode());
        detail.setCheckTime(LocalDateTime.now());
        if (detail.getId() == null) {
            screeningResultDetailMapper.insert(detail);
        } else {
            screeningResultDetailMapper.updateById(detail);
        }
        return detail.getId();
    }

    private ServiceException error(String message) {
        return new ServiceException(1_010_006_001, message);
    }
}

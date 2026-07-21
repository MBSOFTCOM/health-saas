package cn.iocoder.yudao.module.childhealth.service.external.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 设备测量数据 DTO（身高体重仪等）
 *
 * 需求 8：对接身高体重仪设备
 */
@Data
public class DeviceMeasurementDTO {

    /**
     * 设备序列号
     */
    private String deviceSerialNo;

    /**
     * 设备类型（WEIGHING_SCALE / STADIOMETER / INTEGRATED / BLUETOOTH）
     */
    private String deviceType;

    /**
     * HIS 患者ID（设备扫码识别）
     */
    private String hisPatientId;

    /**
     * 儿童档案ID（本地）
     */
    private Long childId;

    /**
     * 体重（kg，精度 0.01）
     */
    private Double weight;

    /**
     * 身高/身长（cm，精度 0.1）
     */
    private Double height;

    /**
     * 头围（cm，精度 0.1）
     */
    private Double headCircumference;

    /**
     * BMI（自动计算，可空）
     */
    private Double bmi;

    /**
     * 体脂率（%）
     */
    private Double bodyFatRate;

    /**
     * 体温（℃）
     */
    private Double temperature;

    /**
     * 测量时间（设备本地时间）
     */
    private LocalDateTime measureTime;

    /**
     * 原始数据（JSON 格式，保留原始协议字段）
     */
    private String rawData;

    /**
     * 数据传输方式（SERIAL / BLUETOOTH / NETWORK / USB）
     */
    private String transportType;

}

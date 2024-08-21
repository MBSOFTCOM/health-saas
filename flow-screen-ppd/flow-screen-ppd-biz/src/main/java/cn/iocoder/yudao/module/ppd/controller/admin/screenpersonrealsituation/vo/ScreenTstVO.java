package cn.iocoder.yudao.module.ppd.controller.admin.screenpersonrealsituation.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ScreenTstVO implements Serializable {
    /**
     * 主键
     */
    private Long id;
    /**
     * 筛查编号
     */
    private String screenId;
    /**
     * 年份
     */
    private Integer year;
    /**
     * 筛查类型
     */
    private Integer screenType;
    /**
     * 同步时唯一编码
     */
    private Long syncId;
    /**
     * 患者id
     */
    private Long patientId;
    /**
     * 硬结横径 单位mm
     */
    private Integer transverseDiameter;
    /**
     * 硬结纵径 单位mm
     */
    private Integer longitudinalDiameter;
    /**
     * 红晕横径
     */
    private Integer blushTransverseDiameter;
    /**
     * 红晕纵径
     */
    private Integer blushLongitudinalDiameter;
    /**
     * 1：水泡/2：双圈/3：坏死/4：淋巴管炎/
     */
    private Integer bleb;
    /**
     * 是否注射(1-是 0-否)
     */
    private Integer injection;
    /**
     * 注射方式（根据字典）
     */
    private Integer injectionWay;
    /**
     * 结果。1-感染 0-未感染
     */
    private Integer outcome;
    /**
     * 医生签名
     */
    private String doctorSignature;
    /**
     * 注射单位
     */
    private String injectionAgency;
    /**
     * 筛查时间
     */
    private LocalDateTime screenTime;
    /**
     * 筛查次序
     */
    private Integer screenOrder;
}

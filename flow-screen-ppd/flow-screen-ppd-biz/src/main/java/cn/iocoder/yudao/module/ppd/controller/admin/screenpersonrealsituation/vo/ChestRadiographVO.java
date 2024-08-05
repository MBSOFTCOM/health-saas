package cn.iocoder.yudao.module.ppd.controller.admin.screenpersonrealsituation.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ChestRadiographVO implements Serializable {
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
    private Long personId;
    /**
     * 胸片编号
     */
    private String chestRadiographCode;
    /**
     * 胸片
     */
    private String chestRadiograph;
    /**
     * 医生签名
     */
    private String doctorSignature;
    /**
     * 筛查时间
     */
    private LocalDateTime screenTime;
    /**
     * 结果。1-疑似结核 0-无异常
     */
    private Integer outcome;
    /**
     * 筛查次序
     */
    private Integer screenOrder;
}

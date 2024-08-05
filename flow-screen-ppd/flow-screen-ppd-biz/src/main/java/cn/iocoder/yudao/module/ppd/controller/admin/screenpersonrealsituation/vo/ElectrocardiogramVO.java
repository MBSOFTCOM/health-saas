package cn.iocoder.yudao.module.ppd.controller.admin.screenpersonrealsituation.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ElectrocardiogramVO implements Serializable {
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
     * 心电图照片
     */
    private String electrocardiogram;
    /**
     * 医生签名
     */
    private String doctorSignature;
    /**
     * 筛查时间
     */
    private LocalDateTime screenTime;
    /**
     * 筛查次序
     */
    private Integer screenOrder;
    /**
     * 筛查点
     */
    private String screenPoint;
    /**
     * 检测结果
     */
    private String testResult;
}

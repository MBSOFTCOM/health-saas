package cn.iocoder.yudao.module.ppd.controller.admin.screenpersonrealsituation.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class CollectVO implements Serializable {
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
     * 结果（分新生筛查和其他）。	新生：1-咳嗽、咳痰不小于2周 2-痰中带血或咯血 3-反复发热2周以上 4-淋巴结肿大	其他：1-咳嗽、咳痰（超过一周）2-血痰或咯血 3-发热	胸痛 4-夜间盗汗 5-食欲不振 6-乏力 7-体重减轻（超过6斤 8-卡痕异常
     */
    private String outcome;
    /**
     * 医生签名
     */
    private String doctorSignature;
    /**
     * 筛查单位
     */
    private String screenAgency;
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
}

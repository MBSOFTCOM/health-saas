package cn.iocoder.yudao.module.ppd.controller.admin.screenpersonrealsituation.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class SputumExaminationVO implements Serializable {

    /**
     * 主键
     */
    private Long id;
    /**
     * 筛查编号
     */
    private String screenId;
    /**
     * 同步时唯一编码
     */
    private Long syncId;
    /**
     * 患者id
     */
    private Long patientId;
    /**
     * 医生签名
     */
    private String doctorSignature;
    /**
     * 是否痰检(0-未痰检，1-已经痰检)
     */
    private Integer sputumExamination;
    /**
     * 痰标本类型，1-无痰 2-即时痰 3-发放晨痰 4-夜间痰盒
     */
    private Integer type;
    /**
     * 即时痰照片
     */
    private String forthwithSputum;
    /**
     * 即时痰标本号
     */
    private String forthwithSputumCode;
    /**
     * 晨痰照片
     */
    private String eveningSputum;
    /**
     * 晨痰标本号
     */
    private String eveningSputumCode;
    /**
     * 夜痰照片
     */
    private String morningSputum;
    /**
     * 夜痰标本号
     */
    private String morningSputumCode;
    /**
     * 结果
     */
    private String outcome;
    /**
     * 筛查时间
     */
    private LocalDateTime screenTime;
    /**
     * 筛查次序
     */
    private Integer screenOrder;
    /**
     * 年份
     */
    private Integer year;
    /**
     * 筛查类型
     */
    private Integer screenType;
}

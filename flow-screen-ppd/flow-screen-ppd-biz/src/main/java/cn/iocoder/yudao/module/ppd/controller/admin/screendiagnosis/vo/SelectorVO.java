package cn.iocoder.yudao.module.ppd.controller.admin.screendiagnosis.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SelectorVO {

    private Long value;

    private String label;

    // DR 结果以及 其他异常说明
    // 其他异常说明
    private String remark;

    // 结果
    private Integer outcome;

    // DR CT 心电图
    /**
     * 图片类型
     */
    private Integer type;

    /**
     * 完整图片路径
     */
    private String url;

    /**
     * 本次筛查次序/时间 图片路径列表
     */
    private List<String> imageUrls;

    /**
     * 筛查时间 用于后续在该对象集合中做排序
     */
    private LocalDateTime screenTime;


    // 实验组数据
    private ExperimentGroup experimentGroup;

    // 体检单数据
    private TBHealthScreening tbHealthScreening;
}

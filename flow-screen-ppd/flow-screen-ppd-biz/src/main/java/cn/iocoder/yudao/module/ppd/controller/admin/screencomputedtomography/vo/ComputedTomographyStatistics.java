package cn.iocoder.yudao.module.ppd.controller.admin.screencomputedtomography.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComputedTomographyStatistics {
    /**
     * 无异常人数
     */
    private Integer normal;
    /**
     * 疑似结核
     */
    private Integer suspected;
    /**
     * 总人数
     */
    private Integer allNum;
    /**
     * 今日筛查人数
     */
    private Integer dayNum;
    /**
     * 本月筛查人数
     */
    private Integer yearNum;
    /**
     * 本年筛查人数
     */
    private Integer monthNum;
}

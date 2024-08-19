package cn.iocoder.yudao.module.ppd.controller.admin.report.vo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class SummaryReqVO {

    /**
     * 区划
     */
    private String districtCode;
    /**
     * 年份
     */
    private Integer year;
    /**
     * 筛查点名称
     */
    private String screenPoint;
    /**
     * 类型（1：学校肺结核筛查结果统计表、2：医疗结构结核菌素皮肤试验开展情况统计表）
     */
    private Integer type;
}

package cn.iocoder.yudao.module.ppd.controller.admin.report.vo;

import lombok.Data;

@Data
public class ReportReqVO {
    /**
     * 多人群分类-字典单个key的值，表示：这个人群的类型
     */
    private Integer moreType;
    /**
     * 年份
     */
    private Integer year;
    /**
     * 季度
     */
    private Integer quarter;
}

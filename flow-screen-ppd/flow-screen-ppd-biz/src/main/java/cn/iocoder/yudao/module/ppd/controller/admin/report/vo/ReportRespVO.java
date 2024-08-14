package cn.iocoder.yudao.module.ppd.controller.admin.report.vo;

import lombok.Data;

import java.util.List;

@Data
public class ReportRespVO {
    /**
     * 指标集合
     */
    private List<Index> indexList;
}

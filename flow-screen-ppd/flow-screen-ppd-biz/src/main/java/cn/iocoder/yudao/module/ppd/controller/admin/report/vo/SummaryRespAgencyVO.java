package cn.iocoder.yudao.module.ppd.controller.admin.report.vo;

import cn.hutool.core.annotation.Alias;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import lombok.Builder;
import lombok.Data;


@Data
@ExcelIgnoreUnannotated
public class SummaryRespAgencyVO {

    /**
     * 学校所在区划
     */
    @Alias("筛查机构所在区划")
    private String districtName;
    /**
     * 筛查机构全称
     */
    @Alias("筛查机构全称")
    private String deptName;
    /**
     *  PPD皮试人数
     */
    @Alias("PPD试验人数")
    private Integer ppdNumber;
    /**
     * PPD复验人数
     */
    @Alias("复验结果人数")
    private Integer ppdReNumber;
    /**
     *  PPD-阴性
     */
    @Alias("阴性")
    private Integer feminine;
    /**
     *  PPD-阳性
     */
    @Alias("阳性")
    private Integer masculine;

}

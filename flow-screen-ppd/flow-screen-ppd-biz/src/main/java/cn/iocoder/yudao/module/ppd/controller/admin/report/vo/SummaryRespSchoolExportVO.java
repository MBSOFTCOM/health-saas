package cn.iocoder.yudao.module.ppd.controller.admin.report.vo;

import cn.hutool.core.annotation.Alias;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import lombok.Data;


@Data
@ExcelIgnoreUnannotated
public class SummaryRespSchoolExportVO {

    /**
     * 学校所在区划
     */
    @Alias("学校所在区划")
    private String districtName;
    /**
     * 学校全称
     */
    @Alias("学校全称")
    private String schoolName;
    /**
     * 学生类别
     */
    @Alias("学生类别")
    private String studentType;
    /**
     * 实际招生总数
     */
    @Alias("实际招生总数")
    private Integer actualStudentNumber;
    /**
     * 有肺结核可疑症状或密切接触史人数
     */
    @Alias("有肺结核可疑症状或密切接触史人数")
    private Integer symptomsNumber;
    /**
     *  PPD皮试人数
     */
    @Alias("PPD皮试人数")
    private Integer ppdNumber;
    /**
     * PPD复验人数
     */
    @Alias("PPD复验人数")
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
    /**
     *  不适宜PPD筛查人数
     */
    @Alias("不适宜PPD筛查人数")
    private Integer noPpdNumber;
    /**
     *  X线胸片检查人数--总数
     */
    @Alias("总数")
    private Integer sumXrayNumber;
    /**
     *  X线胸片检查人数--未见异常
     */
    @Alias("未见异常")
    private Integer normalXrayNumber;
    /**
     *  X线胸片检查人数--异常
     */
    @Alias("异常")
    private Integer abnormalXrayNumber;

}

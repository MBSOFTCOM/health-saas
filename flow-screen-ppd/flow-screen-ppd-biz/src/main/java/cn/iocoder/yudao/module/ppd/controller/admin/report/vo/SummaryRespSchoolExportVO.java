package cn.iocoder.yudao.module.ppd.controller.admin.report.vo;

import cn.hutool.core.annotation.Alias;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;


@Data
@ExcelIgnoreUnannotated
public class SummaryRespSchoolExportVO {

    /**
     * 学校所在区划
     */
    @ExcelProperty("学校所在区划")
    private String districtName;
    /**
     * 学校全称
     */
    @ExcelProperty("学校全称")
    private String schoolName;
    /**
     * 学生类别
     */
    @ExcelProperty("学生类别")
    private String studentType;
    /**
     * 实际招生总数
     */
    @ExcelProperty("实际招生总数")
    private Integer actualStudentNumber;
    /**
     * 有肺结核可疑症状或密切接触史人数
     */
    @ExcelProperty("有肺结核可疑症状或密切接触史人数")
    private Integer symptomsNumber;
    /**
     *  PPD皮试人数
     */
    @ExcelProperty("PPD皮试人数")
    private Integer ppdNumber;
    /**
     * PPD复验人数
     */
    @ExcelProperty("PPD复验人数")
    private Integer ppdReNumber;
    /**
     *  PPD-阴性
     */
    @ExcelProperty("阴性")
    private Integer feminine;
    /**
     *  PPD-阳性
     */
    @ExcelProperty("阳性")
    private Integer masculine;
    /**
     *  不适宜PPD筛查人数
     */
    @ExcelProperty("不适宜PPD筛查人数")
    private Integer noPpdNumber;
    /**
     *  X线胸片检查人数--总数
     */
    @ExcelProperty("总数")
    private Integer sumXrayNumber;
    /**
     *  X线胸片检查人数--未见异常
     */
    @ExcelProperty("未见异常")
    private Integer normalXrayNumber;
    /**
     *  X线胸片检查人数--异常
     */
    @ExcelProperty("异常")
    private Integer abnormalXrayNumber;

}

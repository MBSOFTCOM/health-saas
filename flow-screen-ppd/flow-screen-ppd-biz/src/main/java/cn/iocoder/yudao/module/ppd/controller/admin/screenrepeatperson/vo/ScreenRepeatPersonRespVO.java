package cn.iocoder.yudao.module.ppd.controller.admin.screenrepeatperson.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 重复筛查人员管理 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ScreenRepeatPersonRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "筛查编号（生成）")
    @ExcelProperty("筛查编号")
    private String screenId;

    @Schema(description = "姓名")
    @ExcelProperty("姓名")
    private String name;

    @Schema(description = "身份证号")
    @ExcelProperty("身份证号")
    private String idNum;

    @Schema(description = "年龄")
    @ExcelProperty("年龄")
    private Integer age;

    @Schema(description = "性别(1-女，0-男)")
    @ExcelProperty(value = "性别", converter = DictConvert.class)
    @DictFormat("tb_patient_sex")
    private Integer sex;

    @Schema(description = "民族")
    @ExcelProperty(value = "民族", converter = DictConvert.class)
    @DictFormat("tb_ethnic")
    private Integer nation;

    @Schema(description = "联系电话")
    @ExcelProperty("联系电话")
    private String tel;

    @Schema(description = "筛查类型  1--常规、2--新生、3--应急")
    @ExcelProperty(value = "筛查类型", converter = DictConvert.class)
    @DictFormat("tb_screen_type")
    private Integer screenType;

    @Schema(description = "是否需筛查(0-否，1-是)")
    @ExcelProperty(value = "是否需筛查", converter = DictConvert.class)
    @DictFormat("is_new")
    private Integer isNew;

    @Schema(description = "是否已筛查(0-未筛查，1-已筛查，2-正在筛查中)")
    @ExcelProperty(value = "是否已筛查", converter = DictConvert.class)
    @DictFormat("is_screen")
    private Integer isScreened;

    @Schema(description = "第一人群分类（1-重点人群 2-非重点人群 4-教职工）")
    @ExcelProperty(value = "第一人群分类", converter = DictConvert.class)
    @DictFormat("tb_first_people_type")
    private Integer firstType;

    @ExcelProperty(value = "多人群分类")
    private String moreTypeStr;

    @Schema(description = "多人群分类（1-学生、2-老年人、4-教职工、8-密接者、16-糖尿病、32-僧尼、64-既往患者）")
//    @ExcelProperty(value = "多人群分类", converter = DictConvert.class)
    @DictFormat("tb_more_people_type")
    private Integer moreType;

    @Schema(description = "筛查点")
    @ExcelProperty("筛查点")
    private String screenPoint;

    @Schema(description = "单位")
    @ExcelProperty("单位")
    private String schoolOrTemple;

    @Schema(description = "班级")
    @ExcelProperty("班级")
    private String classroom;

    @Schema(description = "身高")
    @ExcelProperty("身高")
    private BigDecimal height;

    @Schema(description = "体重")
    @ExcelProperty("体重")
    private BigDecimal weight;

    @Schema(description = "户籍地址")
    @ExcelProperty("户籍地址")
    private String permanentAddress;

    @Schema(description = "户籍地址-省")
    private String permanentAddressProvince;

    @Schema(description = "户籍地址-市")
    private String permanentAddressCity;

    @Schema(description = "户籍地址-县")
    private String permanentAddressCounty;

    @Schema(description = "户籍地址-乡镇")
    private String permanentAddressTown;

    @Schema(description = "现住址-省")
    private String province;

    @Schema(description = "现住址-市")
    private String city;

    @Schema(description = "现住址-县")
    private String county;

    @Schema(description = "现住址-乡镇")
    private String town;

    @Schema(description = "现住址")
    @ExcelProperty("现住址")
    private String address;

    @Schema(description = "既往有无和肺结核患者密切接触。0-否 1-是")
    private Integer contactHistory;

    @Schema(description = "是否为新生(0-否，1-是)")
//    @ExcelProperty(value = "是否为新生(0-否，1-是)", converter = DictConvert.class)
    @DictFormat("is_new")
    private Integer isNewStudent;

    @Schema(description = "计划筛查时间")
    @ExcelProperty("计划筛查时间")
    private LocalDateTime screenTime;

    @Schema(description = "所属工作年份")
    @ExcelProperty("所属工作年份")
    private Integer year;

    @Schema(description = "同步时唯一编码")
    private Long syncId;



}
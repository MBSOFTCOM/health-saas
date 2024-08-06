package cn.iocoder.yudao.module.ppd.controller.admin.screenpersonrealsituation.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 摸底 Response VO")
@Data
@ExcelIgnoreUnannotated
@Builder
public class ScreenPersonRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "筛查编号")
    @ExcelProperty("筛查编号")
    private String screenId;

    @Schema(description = "姓名")
    @ExcelProperty("姓名")
    private String name;

    @Schema(description = "身份证号")
    @ExcelProperty(value = "身份证号")
    private String idNum;


    @Schema(description = "筛查类型")
    @ExcelProperty(value = "筛查类型", converter = DictConvert.class)
    @DictFormat("tb_screen_type")
    private Integer screenType;

    @Schema(description = "是否需筛查")
    @ExcelProperty(value = "是否需筛查", converter = DictConvert.class)
    @DictFormat("is_new")
    private Integer isNew;

    @Schema(description = "是否已筛查")
    @ExcelProperty(value = "是否已筛查", converter = DictConvert.class)
    @DictFormat("is_screen")
    private Integer isScreened;

    @Schema(description = "是否为新生")
//    @ExcelProperty(value = "是否为新生", converter = DictConvert.class)
    @DictFormat("is_new")
    private Integer isNewStudent;

    @Schema(description = "第一人群分类（1-重点人群 2-非重点人群 3-教职工）")
    @ExcelProperty(value = "第一人群分类", converter = DictConvert.class)
    @DictFormat("tb_first_people_type")
    private Integer firstType;

    @Schema(description = "多人群分类（1-学生、2-老年人、4-教职工、8-密接者、16-糖尿病、32-僧尼、64-既往患者）")
    @ExcelProperty(value = "多人群分类")
    private String moreTypeStr;

    @Schema(description = "多人群分类（1-学生、2-老年人、4-教职工、8-密接者、16-糖尿病、32-僧尼、64-既往患者）")
    private Integer moreType;

    @Schema(description = "学生类型")
    @ExcelProperty(value = "学生类型", converter = DictConvert.class)
    @DictFormat("student_type")
    private Integer studentType;

    @Schema(description = "开始计划筛查时间")
    @ExcelProperty(value = "计划筛查时间")
    private LocalDateTime screenStartTime;

    @Schema(description = "结束计划筛查时间")
    @ExcelProperty(value = "计划筛查时间")
    private LocalDateTime screenEndTime;

    @Schema(description = "监护人手机号")
    @ExcelProperty(value = "监护人手机号")
    private String guardianTel;

    @Schema(description = "计划筛查时间")
    private LocalDateTime screenTime;

    @Schema(description = "性别(1-女，0-男)")
    @ExcelProperty(value = "性别", converter = DictConvert.class)
    @DictFormat("tb_patient_sex")
    private Integer sex;

    @Schema(description = "年龄")
    private Integer age;

    @Schema(description = "联系电话·")
    @ExcelProperty("联系电话")
    private String tel;

    @Schema(description = "民族")
    @ExcelProperty(value = "民族", converter = DictConvert.class)
    @DictFormat("tb_ethnic")
    private Integer nation;

    @Schema(description = "单位")
    @ExcelProperty("单位")
    private String schoolOrTemple;

    @Schema(description = "班级")
    @ExcelProperty("班级")
    private String classroom;

    @Schema(description = "身高")
    @ExcelProperty("身高(cm)")
    private BigDecimal height;

    @Schema(description = "体重")
    @ExcelProperty("体重(kg)")
    private BigDecimal weight;

    @Schema(description = "户籍地址")
    @ExcelProperty("户籍地址")
    private String permanentAddress;

    @Schema(description = "户籍地址-省")
    @ExcelProperty("户籍地址-省")
    private String permanentAddressProvince;

    @Schema(description = "户籍地址-市")
    @ExcelProperty("户籍地址-市")
    private String permanentAddressCity;

    @Schema(description = "户籍地址-县")
    @ExcelProperty("户籍地址-县")
    private String permanentAddressCounty;

    @Schema(description = "户籍地址-乡镇")
    @ExcelProperty("户籍地址-乡镇")
    private String permanentAddressTown;

    @Schema(description = "现住址")
    @ExcelProperty("现住址")
    private String address;

    @Schema(description = "现住址-省")
    @ExcelProperty("现住址-省")
    private String province;

    @Schema(description = "现住址-市")
    @ExcelProperty("现住址-市")
    private String city;

    @Schema(description = "现住址-县")
    @ExcelProperty("现住址-县")
    private String county;

    @Schema(description = "现住址-乡镇")
    @ExcelProperty("现住址-乡镇")
    private String town;

    @Schema(description = "筛查点")
    @ExcelProperty("筛查点")
    private String screenPoint;

    @Schema(description = "备注")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "年份")
    private Integer year;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;
}
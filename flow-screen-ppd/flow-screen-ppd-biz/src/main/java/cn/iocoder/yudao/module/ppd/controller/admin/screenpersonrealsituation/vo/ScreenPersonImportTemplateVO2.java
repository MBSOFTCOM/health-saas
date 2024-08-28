package cn.iocoder.yudao.module.ppd.controller.admin.screenpersonrealsituation.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;


/**
 * 待筛查人员下载导入模板
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = false) // 设置 chain = false，避免用户导入有问题
public class ScreenPersonImportTemplateVO2 {

    @ExcelProperty("序号")
    private String order;

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty(value = "身份证号")
    private String idNum;

    /*@ExcelProperty(value = "是否需筛查", converter = DictConvert.class)
    @DictFormat("is_new")
    private Integer isNew;*/

    /*@ExcelProperty(value = "是否已筛查", converter = DictConvert.class)
    @DictFormat("is_screen")
    private Integer isScreened;*/

    @ExcelProperty(value = "是否为新生", converter = DictConvert.class)
    @DictFormat("is_new")
    private Integer isNewStudent;


    @ExcelProperty(value = "第一人群分类", converter = DictConvert.class)
    @DictFormat("tb_first_people_type")
    private Integer firstType;

    @ExcelProperty(value = "多人群分类", converter = DictConvert.class)
    @DictFormat("tb_more_people_type")
    private Integer[] moreType;

    @Schema(description = "学生类型")
    @ExcelProperty(value = "学生类型", converter = DictConvert.class)
    @DictFormat("student_type")
    private Integer studentType;

    @Schema(description = "监护人手机号")
    @ExcelProperty(value = "监护人手机号")
    private String guardianTel;

    /*@ExcelProperty("计划筛查时间")
    private LocalDateTime screenTime;*/

    /*@ExcelProperty(value = "性别", converter = DictConvert.class)
    @DictFormat("system_user_sex")
    private Integer sex;*/

    @ExcelProperty("联系电话")
    private String tel;

    @ExcelProperty(value = "民族", converter = DictConvert.class)
    @DictFormat("tb_ethnic")
    private Integer nation;

    @ExcelProperty("单位")
    private String schoolOrTemple;

    @ExcelProperty("班级")
    private String classroom;

    @ExcelProperty("身高(cm)")
    private BigDecimal height;

    @ExcelProperty("体重(kg)")
    private BigDecimal weight;

    @ExcelProperty("户籍地址")
    private String permanentAddress;

    @ExcelProperty("户籍地址-省")
    private String permanentAddressProvince;

    @ExcelProperty("户籍地址-市")
    private String permanentAddressCity;

    @ExcelProperty("户籍地址-县")
    private String permanentAddressCounty;

    @ExcelProperty("户籍地址-乡镇")
    private String permanentAddressTown;

    @ExcelProperty("现住址")
    private String address;

    @ExcelProperty("现住址-省")
    private String province;

    @ExcelProperty("现住址-市")
    private String city;

    @ExcelProperty("现住址-县")
    private String county;

    @ExcelProperty("现住址-乡镇")
    private String town;

    @ExcelProperty("筛查点")
    private String screenPoint;

    @ExcelProperty("备注")
    private String remark;
}
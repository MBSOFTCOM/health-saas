package cn.iocoder.yudao.module.ppd.controller.admin.screenpersonrealsituation.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 摸底新增/修改 Request VO")
@Data
public class ScreenPersonSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "身份证号")
    @NotNull(message = "身份证号不能为空！")
    private String idNum;

    @Schema(description = "姓名")
    @NotNull(message = "姓名不能为空！")
    private String name;

    @Schema(description = "年龄")
    @NotNull(message = "年龄不能为空！")
    private Integer age;

    @Schema(description = "联系电话")
    @NotNull(message = "联系电话不能为空！")
    private String tel;

    @Schema(description = "性别(2-女，1-男)")
    @NotNull(message = "性别不能为空！")
    private Integer sex;

    @Schema(description = "身高")
    @DecimalMin(value = "0.00", inclusive = true, message = "最小值必须不小于0")
    @DecimalMax(value = "400.00", inclusive = true, message = "最大值不超过400.00")
    @Digits(integer = 3, fraction = 2, message = "请输入有效身高，最大值为400.00且最多保留两位小数")
    private BigDecimal height;

    @Schema(description = "体重")
    @DecimalMin(value = "0.00", inclusive = true, message = "最小值必须不小于0")
    @DecimalMax(value = "300.00", inclusive = true, message = "最大值不超过300.00")
    @Digits(integer = 3, fraction = 2, message = "请输入有效体重，最大值为300.00且最多保留两位小数")
    private BigDecimal weight;

    @Schema(description = "户籍地址")
    private String permanentAddress;

    @Schema(description = "户籍地址-省")
    private String permanentAddressProvince;

    @Schema(description = "户籍地址-市")
    private String permanentAddressCity;

    @Schema(description = "户籍地址-县")
    private String permanentAddressCounty;

    @Schema(description = "户籍地址-乡镇")
    private String permanentAddressTown;

    @Schema(description = "现住址")
    private String address;

    @Schema(description = "现住址-省")
    private String province;

    @Schema(description = "现住址-市")
    private String city;

    @Schema(description = "现住址-县")
    private String county;

    @Schema(description = "现住址-乡镇")
    private String town;

    @Schema(description = "民族")
//    @NotNull(message = "民族不能为空！")
    private Integer nation;

    @Schema(description = "第一人群分类（1-重点人群 2-非重点人群 3-教职工）")
    @NotNull(message = "第一人群分类不能为空！")
    private Integer firstType;

    @Schema(description = "多人群分类（1-学生、2-老年人、3-教职工、4-密接者、5-糖尿病、6-僧尼、7-既往患者）")
//    @NotNull(message = "多人群分类不能为空！")
    private Integer moreType;

    @Schema(description = "单位")
    private String schoolOrTemple;

    @Schema(description = "班级")
    private String classroom;

    @Schema(description = "是否需筛查")
    private Integer isNew;

    @Schema(description = "是否已筛查")
//    @NotNull(message = "是否筛查选项不能为空！")
    private Integer isScreened;

    @Schema(description = "是否为新生")
//    @NotNull(message = "是否为新生不能为空")
    private Integer isNewStudent;

    @Schema(description = "筛查编号")
    private String screenId;

    @Schema(description = "筛查点")
//    @NotNull(message = "筛查点不能为空！")
    private String screenPoint;

    @Schema(description = "筛查时间")
    private LocalDateTime screenTime;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "年份")
    private Integer year;

    @Schema(description = "筛查类型")
    private Integer screenType;

    @Schema(description = "学生类型")
    private Integer studentType;

    @Schema(description = "开始计划筛查时间")
    private LocalDateTime screenStartTime;

    @Schema(description = "结束计划筛查时间")
    private LocalDateTime screenEndTime;

    @Schema(description = "监护人手机号")
    private String guardianTel;

    @Schema(description = "部门id")
    private Long deptId;

}
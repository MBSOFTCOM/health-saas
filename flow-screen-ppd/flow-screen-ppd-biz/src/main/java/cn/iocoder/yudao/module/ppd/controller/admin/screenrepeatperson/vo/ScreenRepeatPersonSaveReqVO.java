package cn.iocoder.yudao.module.ppd.controller.admin.screenrepeatperson.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 重复筛查人员管理新增/修改 Request VO")
@Data
public class ScreenRepeatPersonSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "身高")
    private BigDecimal height;

    @Schema(description = "体重")
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
    private Integer nation;

    @Schema(description = "第一人群分类（1-重点人群 2-非重点人群 4-教职工）")
    private Integer firstType;

    @Schema(description = "多人群分类（1-学生、2-老年人、4-教职工、8-密接者、16-糖尿病、32-僧尼、64-既往患者）")
    private Integer moreType;

    @Schema(description = "单位")
    private String schoolOrTemple;

    @Schema(description = "班级")
    private String classroom;

    @Schema(description = "既往有无和肺结核患者密切接触。0-否 1-是")
    private Integer contactHistory;

    @Schema(description = "是否需筛查(0-否，1-是)")
    private Integer isNew;

    @Schema(description = "是否已筛查(0-未筛查，1-已筛查，2-正在筛查中)")
    private Integer isScreened;

    @Schema(description = "是否为新生(0-否，1-是)")
    private Integer isNewStudent;

    @Schema(description = "筛查点")
    private String screenPoint;

    @Schema(description = "计划筛查时间")
    private LocalDateTime screenTime;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "所属工作年份")
    private Integer year;

    @Schema(description = "筛查类型  1--常规、2--新生、3--应急")
    private Integer screenType;

    @Schema(description = "筛查编号（生成）")
    private String screenId;

    @Schema(description = "同步时唯一编码")
    private Long syncId;

    @Schema(description = "身份证号")
    private String idNum;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "年龄")
    private Integer age;

    @Schema(description = "联系电话")
    private String tel;

    @Schema(description = "性别(1-女，0-男)")
    private Integer sex;

}
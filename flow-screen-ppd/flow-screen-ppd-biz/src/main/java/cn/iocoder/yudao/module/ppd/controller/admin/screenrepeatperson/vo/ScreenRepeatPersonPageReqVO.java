package cn.iocoder.yudao.module.ppd.controller.admin.screenrepeatperson.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 重复筛查人员管理分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ScreenRepeatPersonPageReqVO extends PageParam {

    @Schema(description = "户籍地址")
    private String permanentAddress;

    @Schema(description = "现住址")
    private String address;

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
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] screenTime;

    @Schema(description = "所属工作年份")
    private Integer year;

    @Schema(description = "筛查类型  1--常规、2--新生、3--应急")
    private Integer screenType;

    @Schema(description = "身份证号")
    private String idNum;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "联系电话")
    private String tel;

    @Schema(description = "性别(1-女，0-男)")
    private Integer sex;

    @Schema(description = "身高")
    private BigDecimal height;

    @Schema(description = "体重")
    private BigDecimal weight;

    @Schema(description = "学生类别")
    private Integer studentType;

}
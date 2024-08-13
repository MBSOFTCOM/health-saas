package cn.iocoder.yudao.module.ppd.controller.admin.screenpersonrealsituation.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 摸底分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ScreenPersonPageReqVO extends PageParam {

    @Schema(description = "身份证号")
    private String idNum;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "年龄")
    private Integer age;

    @Schema(description = "联系电话·")
    private String tel;

    @Schema(description = "性别(2-女，1-男)")
    private Integer sex;

    @Schema(description = "身高")
    private BigDecimal height;

    @Schema(description = "体重")
    private BigDecimal weight;

    @Schema(description = "户籍地址")
    private String permanentAddress;

    @Schema(description = "现住址")
    private String address;

    @Schema(description = "民族")
    private Integer nation;

    @Schema(description = "第一人群分类（1-重点人群 2-非重点人群 3-教职工）")
    private Integer firstType;

    @Schema(description = "多人群分类（1-学生、2-老年人、3-教职工、4-密接者、5-糖尿病、6-僧尼、7-既往患者）")
    private Integer moreType;

    @Schema(description = "单位")
    private String schoolOrTemple;

    @Schema(description = "现住址-县")
    private String county;

    @Schema(description = "现住址-市")
    private String city;

    @Schema(description = "现住址-乡镇")
    private String town;

    @Schema(description = "班级")
    private String classroom;

    @Schema(description = "是否新增")
    private Integer isNew;

    @Schema(description = "是否筛查")
    private Integer isScreened;

    @Schema(description = "是否为新生")
    private Integer isNewStudent;

    @Schema(description = "筛查编号")
    private String screenId;

    @Schema(description = "筛查点")
    private String screenPoint;

    @Schema(description = "筛查时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] screenTime;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "年份")
    private Integer year;

    @Schema(description = "筛查类型")
    private Integer screenType;

    @Schema(description = "对应摸底表中id")
    private Long personId;

    @Schema(description = "学生类型")
    private Integer studentType;

    @Schema(description = "开始计划筛查时间")
    private LocalDateTime screenStartTime;

    @Schema(description = "结束计划筛查时间")
    private LocalDateTime screenEndTime;

    @Schema(description = "监护人手机号")
    private String guardianTel;
}
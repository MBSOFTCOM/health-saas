package cn.iocoder.yudao.module.ppd.controller.admin.synchronization.vo;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 摸底库测试 DO
 *
 * @author ywp
 */
@TableName("tb_screen_person_test")
@KeySequence("tb_screen_person_test_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScreenPersonTestDO extends BaseDO {

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 年份
     */
    private Integer year;
    /**
     * 筛查类型  1--常规、2--新生、3--应急
     */
    private Integer screenType;
    /**
     * 筛查编号（生成）
     */
    private String screenId;
    /**
     * 同步时唯一编码
     */
    private Long syncId;
    /**
     * 身份证号
     */
    private String idNum;
    /**
     * 姓名
     */
    private String name;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 联系电话
     */
    private String tel;
    /**
     * 性别(1-女，0-男)
     */
    private Integer sex;
    /**
     * 身高
     */
    private BigDecimal height;
    /**
     * 体重
     */
    private BigDecimal weight;
    /**
     * 户籍地址
     */
    private String permanentAddress;
    /**
     * 户籍地址-省
     */
    private String permanentAddressProvince;
    /**
     * 户籍地址-市
     */
    private String permanentAddressCity;
    /**
     * 户籍地址-县
     */
    private String permanentAddressCounty;
    /**
     * 户籍地址-乡镇
     */
    private String permanentAddressTown;
    /**
     * 现住址
     */
    private String address;
    /**
     * 现住址-省
     */
    private String province;
    /**
     * 现住址-市
     */
    private String city;
    /**
     * 现住址-县
     */
    private String county;
    /**
     * 现住址-乡镇
     */
    private String town;
    /**
     * 民族
     */
    private Integer nation;
    /**
     * 第一人群分类（1-重点人群 2-非重点人群 4-教职工）
     */
    private Integer firstType;
    /**
     * 多人群分类（1-学生、2-老年人、4-教职工、8-密接者、16-糖尿病、32-僧尼、64-既往患者）
     */
    private Integer moreType;
    /**
     * 学校或寺庙
     */
    private String schoolOrTemple;
    /**
     * 班级
     */
    private String classroom;
    /**
     * 既往有无和肺结核患者密切接触。0-否 1-是
     */
    private Integer contactHistory;
    /**
     * 是否需筛查(0-否，1-是)
     */
    private Integer isNew;
    /**
     * 是否已筛查(0-否，1-是)
     */
    private Integer isScreened;
    /**
     * 是否为新生(0-否，1-是)
     */
    private Integer isNewStudent;
    /**
     * 筛查点
     */
    private String screenPoint;
    /**
     * 计划筛查时间
     */
    private LocalDateTime screenTime;
    /**
     * 备注
     */
    private String remark;

}
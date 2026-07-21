package cn.iocoder.yudao.module.childhealth.dal.dataobject.exam;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 体检预约 DO
 */
@Data
@TableName("exam_appointment")
public class ExamAppointmentDO {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 儿童ID
     */
    private Long childId;

    /**
     * 关联保健计划ID
     */
    private Long planId;

    /**
     * 体检类型 1常规 2入园 3专项
     */
    private Integer examType;

    /**
     * 体检月龄
     */
    private Integer examMonthAge;

    /**
     * 预约日期
     */
    private LocalDate appointmentDate;

    /**
     * 预约时间
     */
    private LocalTime appointmentTime;

    /**
     * 科室ID
     */
    private Long deptId;

    /**
     * 医生ID
     */
    private Long doctorId;

    /**
     * 状态 1已预约 2已到检 3已完成 4已取消
     */
    private Integer status;

    /**
     * 来源 1线下 2线上 3系统自动
     */
    private Integer source;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
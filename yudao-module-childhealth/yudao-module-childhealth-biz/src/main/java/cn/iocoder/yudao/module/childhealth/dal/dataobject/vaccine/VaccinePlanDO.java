package cn.iocoder.yudao.module.childhealth.dal.dataobject.vaccine;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 疫苗接种计划 DO
 */
@Data
@TableName("vaccine_plan")
public class VaccinePlanDO {

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
     * 疫苗名称
     */
    private String vaccineName;

    /**
     * 疫苗编码
     */
    private String vaccineCode;

    /**
     * 疫苗类型：一类/二类
     */
    private String vaccineType;

    /**
     * 第几剂
     */
    private Integer doseNo;

    /**
     * 总剂次
     */
    private Integer totalDoses;

    /**
     * 计划接种日期
     */
    private LocalDate scheduledDate;

    /**
     * 起种月龄
     */
    private Integer startAgeMonth;

    /**
     * 截止月龄
     */
    private Integer endAgeMonth;

    /**
     * 与上剂间隔天
     */
    private Integer intervalDays;

    /**
     * 接种单位
     */
    private String inoculationOrg;

    /**
     * 计划状态：PENDING/COMPLETED/EXPIRED/SKIPPED
     */
    private String status;

    /**
     * 提醒状态：NOT_SENT/SENT/CONFIRMED
     */
    private String reminderStatus;

    /**
     * 提醒发送日期
     */
    private LocalDate reminderDate;

    /**
     * 家长确认
     */
    private Boolean parentConfirmed;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
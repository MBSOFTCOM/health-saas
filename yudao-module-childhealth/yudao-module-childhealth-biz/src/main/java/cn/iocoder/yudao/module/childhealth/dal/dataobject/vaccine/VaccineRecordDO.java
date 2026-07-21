package cn.iocoder.yudao.module.childhealth.dal.dataobject.vaccine;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 疫苗接种记录 DO
 */
@Data
@TableName("vaccine_record")
public class VaccineRecordDO {

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
     * 关联接种计划ID
     */
    private Long vaccinePlanId;

    /**
     * 疫苗名称
     */
    private String vaccineName;

    /**
     * 疫苗编码
     */
    private String vaccineCode;

    /**
     * 疫苗批次
     */
    private String vaccineBatch;

    /**
     * 第几剂
     */
    private Integer doseNo;

    /**
     * 实际接种日期
     */
    private LocalDate inoculationDate;

    /**
     * 接种单位
     */
    private String inoculationOrg;

    /**
     * 接种人员
     */
    private String inoculator;

    /**
     * 接种部位：LEFT_ARM/RIGHT_ARM/LEFT_THIGH/RIGHT_THIGH/ORAL
     */
    private String inoculationSite;

    /**
     * 接种剂量
     */
    private String vaccineDose;

    /**
     * 生产厂家
     */
    private String vaccineManufacturer;

    /**
     * 不良反应
     */
    private String adverseReaction;

    /**
     * 反应级别：NONE/MILD/MODERATE/SEVERE
     */
    private String reactionLevel;

    /**
     * 接种状态：COMPLETED/INCOMPLETE/DEFERRED
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
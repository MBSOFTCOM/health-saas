package cn.iocoder.yudao.module.childhealth.dal.dataobject.exam;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 体检记录主表 DO
 */
@Data
@TableName("exam_record")
public class ExamRecordDO {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 体检流水号
     */
    private String examNo;

    /**
     * 儿童ID
     */
    private Long childId;

    /**
     * 体检日期
     */
    private LocalDate examDate;

    /**
     * 体检类型
     */
    private Integer examType;

    /**
     * 体检时月龄
     */
    private Integer monthAge;

    /**
     * 科室ID
     */
    private Long deptId;

    /**
     * 医生ID
     */
    private Long doctorId;

    /**
     * 检查状态 1进行中 2待审核 3已完成
     */
    private Integer checkStatus;

    /**
     * 是否有异常
     */
    private Boolean hasAbnormal;

    /**
     * 异常标签JSON
     */
    private String abnormalTags;

    /**
     * 使用病历模板ID
     */
    private Long templateId;

    /**
     * 是否已创建专案
     */
    private Boolean caseCreatedFlag;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
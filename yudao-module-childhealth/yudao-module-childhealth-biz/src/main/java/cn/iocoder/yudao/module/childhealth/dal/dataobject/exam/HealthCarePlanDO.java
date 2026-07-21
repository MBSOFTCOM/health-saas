package cn.iocoder.yudao.module.childhealth.dal.dataobject.exam;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 公卫儿童保健计划 DO
 */
@Data
@TableName("health_care_plan")
public class HealthCarePlanDO {

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
     * 计划类型 1常规公卫 2入园 3专项
     */
    private Integer planType;

    /**
     * 计划开始日期
     */
    private LocalDate startDate;

    /**
     * 计划结束日期
     */
    private LocalDate endDate;

    /**
     * 总节点数
     */
    private Integer totalNodes;

    /**
     * 已完成节点数
     */
    private Integer completedNodes;

    /**
     * 状态 1进行中 2已完成 3已终止
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}

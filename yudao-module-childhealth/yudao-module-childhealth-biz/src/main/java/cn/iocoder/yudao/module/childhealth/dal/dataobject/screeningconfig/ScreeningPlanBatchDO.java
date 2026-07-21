package cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDate;

/**
 * 体检方案与批次关联表 DO
 *
 * 对应表: screening_plan_batch
 * 模块: C.五健筛查配置
 * 创建日期: 2026-07-20
 */
@TableName("screening_plan_batch")
@KeySequence("screening_plan_batch_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScreeningPlanBatchDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 方案ID
     */
    private Long planId;

    /**
     * 批次ID
     */
    private Long batchId;

    /**
     * 学校ID
     */
    private Long schoolId;

    /**
     * 年级ID
     */
    private Long gradeId;

    /**
     * 班级ID
     */
    private Long classId;

    /**
     * 筛查开始日期
     */
    private LocalDate screeningStart;

    /**
     * 筛查结束日期
     */
    private LocalDate screeningEnd;

    /**
     * 计划人数
     */
    private Integer targetCount;

    /**
     * 实际人数
     */
    private Integer actualCount;

    /**
     * 完成状态 0未开始 1进行中 2已完成
     */
    private Integer completionStatus;

}

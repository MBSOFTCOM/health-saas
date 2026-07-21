package cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 体检方案配置表 DO
 *
 * 对应表: screening_plan
 * 模块: C.五健筛查配置
 * 创建日期: 2026-07-20
 */
@TableName("screening_plan")
@KeySequence("screening_plan_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScreeningPlanDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 方案编码
     */
    private String planCode;

    /**
     * 方案名称
     */
    private String planName;

    /**
     * 方案类型 1五健 2基础体检 3入园入托
     */
    private Integer planType;

    /**
     * 是否默认套餐 0否 1是
     */
    private Integer defaultPlan;

    /**
     * 最小适用月龄
     */
    private Integer applicableAgeMin;

    /**
     * 最大适用月龄
     */
    private Integer applicableAgeMax;

    /**
     * 适用性别 0全部 1男 2女
     */
    private Integer applicableGender;

    /**
     * 方案内容JSON（含项目列表/问卷/量表）
     */
    private String planContentJson;

    /**
     * 关联问卷ID逗号分隔
     */
    private String questionnaireIds;

    /**
     * 关联量表ID逗号分隔
     */
    private String scaleIds;

    /**
     * 方案说明
     */
    private String description;

    /**
     * 状态 1启用 0停用
     */
    private Integer status;

}

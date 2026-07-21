package cn.iocoder.yudao.module.childhealth.dal.dataobject.casemanagement;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 专案康复达标标准表 DO
 *
 * 对应表: case_recovery_standard
 * 模块: B.专案管理
 * 创建日期: 2026-07-20
 */
@TableName("case_recovery_standard")
@KeySequence("case_recovery_standard_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaseRecoveryStandardDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 专案类型
     */
    private Integer caseType;

    /**
     * 专案子类型
     */
    private String caseSubtype;

    /**
     * 指标编码
     */
    private String indicatorCode;

    /**
     * 指标名称
     */
    private String indicatorName;

    /**
     * 达标值
     */
    private String standardValue;

    /**
     * 比较运算符
     */
    private String standardOperator;

    /**
     * 单位
     */
    private String unit;

    /**
     * 说明
     */
    private String description;

    /**
     * 状态 1启用 0停用
     */
    private Integer status;

}

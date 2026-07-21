package cn.iocoder.yudao.module.childhealth.dal.dataobject.screening;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 阳性识别规则表 DO
 *
 * @author 芋道源码
 */
@TableName("positive_rule")
@KeySequence("positive_rule_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PositiveRuleDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;
    
    /**
     * 规则编码
     */
    private String ruleCode;
    
    /**
     * 规则名称
     */
    private String ruleName;
    
    /**
     * 关联项目编码JSON
     */
    private String itemCodes;
    
    /**
     * 判定条件（可视化配置）JSON
     */
    private String conditionLogic;
    
    /**
     * 阳性等级 1轻度 2中度 3重度
     */
    private Integer positiveLevel;
    
    /**
     * 关联疾病编码
     */
    private String diseaseCode;
    
    /**
     * 是否需要复筛 0否 1是
     */
    private Integer needRecheck;
    
    /**
     * 状态 1正常 0停用
     */
    private Integer status;

}
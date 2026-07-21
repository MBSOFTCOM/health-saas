package cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 问卷定义表 DO
 *
 * 对应表: questionnaire_definition
 * 模块: C.五健筛查配置
 * 创建日期: 2026-07-20
 */
@TableName("questionnaire_definition")
@KeySequence("questionnaire_definition_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionnaireDefinitionDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 问卷编码
     */
    private String code;

    /**
     * 问卷名称
     */
    private String name;

    /**
     * 类型 1筛查问卷 2随访问卷 3健康问卷
     */
    private Integer type;

    /**
     * 描述
     */
    private String description;

    /**
     * 题目JSON
     */
    private String questionsJson;

    /**
     * 填写规则JSON
     */
    private String rulesJson;

    /**
     * 最小适用月龄
     */
    private Integer applicableAgeMin;

    /**
     * 最大适用月龄
     */
    private Integer applicableAgeMax;

    /**
     * 版本
     */
    private String version;

    /**
     * 状态 1启用 0停用
     */
    private Integer status;

}

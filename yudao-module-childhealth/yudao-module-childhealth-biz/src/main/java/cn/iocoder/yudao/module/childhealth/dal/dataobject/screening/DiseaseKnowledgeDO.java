package cn.iocoder.yudao.module.childhealth.dal.dataobject.screening;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 疾病知识库表 DO
 *
 * @author 芋道源码
 */
@TableName("disease_knowledge")
@KeySequence("disease_knowledge_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiseaseKnowledgeDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;
    
    /**
     * 疾病编码
     */
    private String diseaseCode;
    
    /**
     * 疾病名称
     */
    private String diseaseName;
    
    /**
     * 疾病分类
     */
    private String category;
    
    /**
     * 疾病描述
     */
    private String description;
    
    /**
     * 指标解读
     */
    private String indicatorExplain;
    
    /**
     * 居家护理建议
     */
    private String homeCare;
    
    /**
     * 就诊指导
     */
    private String medicalAdvice;
    
    /**
     * 风险提示
     */
    private String riskWarning;
    
    /**
     * 阳性分级标签JSON
     */
    private String positiveTags;
    
    /**
     * 状态 1正常 0停用
     */
    private Integer status;

}
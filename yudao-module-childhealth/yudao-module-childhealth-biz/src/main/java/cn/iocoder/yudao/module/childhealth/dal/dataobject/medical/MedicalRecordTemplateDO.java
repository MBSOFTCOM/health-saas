package cn.iocoder.yudao.module.childhealth.dal.dataobject.medical;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 病历模板表 DO（合并后唯一实现）
 *
 * 对应表: medical_record_template
 * 模块: A. 儿童基础健康检查（A1-病历模板表，12套×4类）
 * 兼容: 需求13/18/22/26 的体格/眼/听力/口腔保健病历模板
 * 字段: 与 init_medical_record_templates.sql 一致
 */
@TableName("medical_record_template")
@KeySequence("medical_record_template_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordTemplateDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 模板编码
     */
    private String templateCode;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 模板类型 GENERAL_CHECKUP/EYE_HEALTH/HEARING_HEALTH/ORAL_HEALTH/ENTRY_EXAM
     */
    private String templateType;

    /**
     * 适用最小月龄
     */
    private Integer ageMonthMin;

    /**
     * 适用最大月龄
     */
    private Integer ageMonthMax;

    /**
     * 所依据的国家规范
     */
    private String normVersion;

    /**
     * 模板说明
     */
    private String description;

    /**
     * 模板内容结构化JSON（schema + defaultValues）
     */
    private String templateContent;

    /**
     * 字段映射关系JSON（VO 字段 → DO 字段）
     */
    private String fieldMapping;

    /**
     * 异常判定阈值JSON
     */
    private String abnormalityThresholds;

    /**
     * 健康指导JSON
     */
    private String healthGuidance;

    /**
     * 随访建议JSON
     */
    private String followUpRecommendation;

    /**
     * 排序
     */
    private Integer sortOrder;

    /**
     * 状态 1启用 0停用
     */
    private Integer status;

}

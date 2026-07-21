package cn.iocoder.yudao.module.childhealth.dal.dataobject.medical;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 病历结构化字段值表 DO
 *
 * 对应表: medical_record_item
 * 模块: A. 儿童基础健康检查（A3-病历结构化字段值表）
 * 创建日期: 2026-07-20
 */
@TableName("medical_record_item")
@KeySequence("medical_record_item_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordItemDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 病历ID
     */
    private Long recordId;

    /**
     * 模板ID
     */
    private Long templateId;

    /**
     * 字段编码
     */
    private String fieldCode;

    /**
     * 字段名称
     */
    private String fieldName;

    /**
     * 字段值
     */
    private String fieldValue;

    /**
     * 字段类型 text/number/select/date/json
     */
    private String fieldType;

    /**
     * 单位
     */
    private String unit;

    /**
     * 参考区间
     */
    private String referenceRange;

    /**
     * 是否异常 0正常 1异常
     */
    private Integer isAbnormal;

    /**
     * 异常描述
     */
    private String abnormalDesc;

    /**
     * 排序
     */
    private Integer sort;

}

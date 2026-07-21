package cn.iocoder.yudao.module.childhealth.dal.dataobject.caseType;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 专案类型配置 DO
 *
 * @author 芋道源码
 */
@TableName("childhealth_case_type_config")
@KeySequence("childhealth_case_type_config_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaseTypeConfigDO extends BaseDO {

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 类型编码
     */
    private String typeCode;
    /**
     * 类型名称
     */
    private String typeName;
    /**
     * 分类
     */
    private String category;
    /**
     * 随访频率
     */
    private String followFrequency;
    /**
     * 个案卡模板
     */
    private String caseCardTemplate;
    /**
     * 随访模板
     */
    private String followTemplate;
    /**
     * 结案标准
     */
    private String dischargeCriteria;
    /**
     * 状态
     */
    private Integer status;

}
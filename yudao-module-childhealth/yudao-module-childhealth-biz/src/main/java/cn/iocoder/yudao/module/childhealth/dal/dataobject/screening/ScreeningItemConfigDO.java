package cn.iocoder.yudao.module.childhealth.dal.dataobject.screening;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 筛查项目配置表 DO
 *
 * @author 芋道源码
 */
@TableName("screening_item_config")
@KeySequence("screening_item_config_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScreeningItemConfigDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;
    
    /**
     * 项目编码
     */
    private String itemCode;
    
    /**
     * 项目名称
     */
    private String itemName;
    
    /**
     * 所属类别 体形/视力/口腔/骨骼/心理
     */
    private String category;
    
    /**
     * 结果类型 1数值 2选项 3多选
     */
    private Integer resultType;
    
    /**
     * 单位
     */
    private String unit;
    
    /**
     * 参考区间JSON
     */
    private String referenceRange;
    
    /**
     * 适用年龄
     */
    private String applicableAge;
    
    /**
     * 适用性别 0不限 1男 2女
     */
    private Integer applicableGender;
    
    /**
     * 排序号
     */
    private Integer sortOrder;
    
    /**
     * 状态 1正常 0停用
     */
    private Integer status;

}
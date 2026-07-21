package cn.iocoder.yudao.module.childhealth.dal.dataobject.screening;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDate;

/**
 * 复筛管理表 DO
 *
 * @author 芋道源码
 */
@TableName("recheck_record")
@KeySequence("recheck_record_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecheckRecordDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;
    
    /**
     * 关联阳性记录ID
     */
    private Long positiveId;
    
    /**
     * 学生ID
     */
    private Long studentId;
    
    /**
     * 初筛记录ID
     */
    private Long initialRecordId;
    
    /**
     * 复筛日期
     */
    private LocalDate recheckDate;
    
    /**
     * 复筛项目JSON
     */
    private String recheckItems;
    
    /**
     * 复筛结果JSON
     */
    private String recheckResult;
    
    /**
     * 是否仍为阳性 0否 1是
     */
    private Integer isStillPositive;
    
    /**
     * 复筛结论
     */
    private String recheckConclusion;
    
    /**
     * 后续随访计划
     */
    private String followPlan;
    
    /**
     * 医生ID
     */
    private Long doctorId;

}
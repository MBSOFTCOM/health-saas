package cn.iocoder.yudao.module.childhealth.dal.dataobject.screening;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 筛查批次 DO
 *
 * @author 芋道源码
 */
@TableName("screening_batch")
@KeySequence("screening_batch_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScreeningBatchDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;
    
    /**
     * 批次编号
     */
    private String batchNo;
    
    /**
     * 批次名称
     */
    private String batchName;
    
    /**
     * 学年ID
     */
    private Long yearId;
    
    /**
     * 学校ID
     */
    private Long schoolId;
    
    /**
     * 开始日期
     */
    private LocalDate startDate;
    
    /**
     * 结束日期
     */
    private LocalDate endDate;
    
    /**
     * 计划筛查人数
     */
    private Integer targetCount;
    
    /**
     * 实际筛查人数
     */
    private Integer actualCount;
    
    /**
     * 批次状态 1筹备中 2进行中 3已完成
     */
    private Integer batchStatus;

}
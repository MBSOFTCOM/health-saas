package cn.iocoder.yudao.module.childhealth.dal.dataobject.screening;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 筛查记录主表 DO
 *
 * @author 芋道源码
 */
@TableName("screening_record")
@KeySequence("screening_record_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScreeningRecordDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;
    
    /**
     * 筛查流水号
     */
    private String recordNo;
    
    /**
     * 批次ID
     */
    private Long batchId;
    
    /**
     * 学生ID
     */
    private Long studentId;
    
    /**
     * 筛查日期
     */
    private LocalDate screeningDate;
    
    /**
     * 审核状态 1进行中 2待审核 3已审核
     */
    private Integer checkStatus;
    
    /**
     * 是否有阳性 0否 1是
     */
    private Integer hasPositive;
    
    /**
     * 阳性项目JSON
     */
    private String positiveItems;
    
    /**
     * 审核医生ID
     */
    private Long auditDoctor;
    
    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

}
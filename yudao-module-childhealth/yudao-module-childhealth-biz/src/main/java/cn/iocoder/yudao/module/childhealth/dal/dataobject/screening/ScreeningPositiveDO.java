package cn.iocoder.yudao.module.childhealth.dal.dataobject.screening;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 筛查阳性记录表 DO
 *
 * @author 芋道源码
 */
@TableName("screening_positive")
@KeySequence("screening_positive_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScreeningPositiveDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;
    
    /**
     * 筛查记录ID
     */
    private Long recordId;
    
    /**
     * 学生ID
     */
    private Long studentId;
    
    /**
     * 疾病编码
     */
    private String diseaseCode;
    
    /**
     * 疾病名称
     */
    private String diseaseName;
    
    /**
     * 阳性等级 1轻度 2中度 3重度
     */
    private Integer positiveLevel;
    
    /**
     * 阳性指标JSON
     */
    private String positiveItems;
    
    /**
     * 健康指导
     */
    private String healthGuidance;
    
    /**
     * 是否需要复筛 0否 1是
     */
    private Integer needRecheck;
    
    /**
     * 复筛状态 0未通知 1通知已入队 2已完成
     */
    private Integer recheckStatus;

}
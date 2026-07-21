package cn.iocoder.yudao.module.childhealth.dal.dataobject.screening;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 筛查结果明细表 DO
 *
 * @author 芋道源码
 */
@TableName("screening_result_detail")
@KeySequence("screening_result_detail_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScreeningResultDetailDO extends BaseDO {

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
     * 筛查项目编码
     */
    private String itemCode;
    
    /**
     * 检查结果值
     */
    private String itemValue;
    
    /**
     * 是否异常 0正常 1异常
     */
    private Integer isAbnormal;
    
    /**
     * 检查科室ID
     */
    private Long deptId;
    
    /**
     * 检查人员ID
     */
    private Long checkerId;
    
    /**
     * 检查时间
     */
    private LocalDateTime checkTime;
    
    /**
     * 设备编码
     */
    private String deviceCode;

}
package cn.iocoder.yudao.module.childhealth.dal.dataobject.screening;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 筛查统计 DO
 *
 * @author 芋道源码
 */
@TableName("screening_statistics")
@KeySequence("screening_statistics_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScreeningStatisticsDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 统计日期
     */
    private LocalDate statDate;

    /**
     * 批次ID
     */
    private Long batchId;

    /**
     * 学校ID
     */
    private Long schoolId;

    /**
     * 年级ID
     */
    private Long gradeId;

    /**
     * 目标人数
     */
    private Integer targetCount;

    /**
     * 实际人数
     */
    private Integer actualCount;

    /**
     * 参与率（百分比）
     */
    private BigDecimal participationRate;

    /**
     * 阳性人数
     */
    private Integer positiveCount;

    /**
     * 阳性率（百分比）
     */
    private BigDecimal positiveRate;

    /**
     * 复筛人数
     */
    private Integer recheckCount;

    /**
     * 复筛率（百分比）
     */
    private BigDecimal recheckRate;

    /**
     * 随访人数
     */
    private Integer followCount;

    /**
     * 随访率（百分比）
     */
    private BigDecimal followRate;

}
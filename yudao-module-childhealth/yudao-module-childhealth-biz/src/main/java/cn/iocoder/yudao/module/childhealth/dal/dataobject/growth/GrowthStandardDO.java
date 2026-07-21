package cn.iocoder.yudao.module.childhealth.dal.dataobject.growth;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;

/**
 * 生长标准数据 DO
 *
 * 对应表: growth_standard
 * 数据来源: WHO 标准化生长参考 / 中国九城市儿童体格发育调查 / Fenton 早产儿生长曲线
 *
 * 模块: 需求9 ▲ WHO/九城市标准差百分位自动计算
 *
 * @author 系统
 */
@TableName("growth_standard")
@KeySequence("growth_standard_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrowthStandardDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 性别 1男 2女
     */
    private Integer gender;

    /**
     * 月龄(0-72), Fenton曲线时为周龄换算月龄
     */
    private BigDecimal ageMonths;

    /**
     * 胎龄(周), 仅 Fenton 曲线使用
     */
    private BigDecimal gestationalAgeWeeks;

    /**
     * 指标类型: WEIGHT/HEIGHT/BMI/HEAD_CIRCUMFERENCE
     */
    private String indicatorType;

    /**
     * SD-3
     */
    private BigDecimal sdNeg3;

    /**
     * SD-2
     */
    private BigDecimal sdNeg2;

    /**
     * SD-1
     */
    private BigDecimal sdNeg1;

    /**
     * 中位数(P50)
     */
    private BigDecimal median;

    /**
     * SD+1
     */
    private BigDecimal sdPos1;

    /**
     * SD+2
     */
    private BigDecimal sdPos2;

    /**
     * SD+3
     */
    private BigDecimal sdPos3;

    /**
     * 第3百分位
     */
    private BigDecimal p3;

    /**
     * 第15百分位
     */
    private BigDecimal p15;

    /**
     * 第50百分位
     */
    private BigDecimal p50;

    /**
     * 第85百分位
     */
    private BigDecimal p85;

    /**
     * 第97百分位
     */
    private BigDecimal p97;

    /**
     * 数据来源: WHO/NINE_CITY/FENTON
     */
    private String source;

}

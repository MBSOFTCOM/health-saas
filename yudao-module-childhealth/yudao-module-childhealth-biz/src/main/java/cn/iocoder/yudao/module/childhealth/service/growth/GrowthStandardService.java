package cn.iocoder.yudao.module.childhealth.service.growth;

import cn.iocoder.yudao.module.childhealth.dal.dataobject.growth.GrowthStandardDO;

import java.math.BigDecimal;
import java.util.List;

/**
 * 生长标准计算 Service 接口
 *
 * 模块: 需求9 ▲ WHO/九城市标准差百分位自动计算
 *
 * 提供基于 WHO/九城市/Fenton 标准数据的 SD 值与百分位计算。
 *
 * @author 系统
 */
public interface GrowthStandardService {

    /**
     * 查询某月龄对应的生长标准记录（精确匹配或线性插值）
     *
     * @param gender        性别 1男 2女
     * @param ageMonths     月龄（支持小数）
     * @param indicatorType 指标类型 WEIGHT/HEIGHT/BMI/HEAD_CIRCUMFERENCE
     * @param source        数据来源 WHO/NINE_CITY/FENTON
     * @return 标准数据（已线性插值），未找到返回 null
     */
    GrowthStandardDO getStandard(Integer gender, BigDecimal ageMonths,
                                String indicatorType, String source);

    /**
     * 计算 SD 值（标准差分）
     *
     * @param gender        性别
     * @param ageMonths     月龄
     * @param indicatorType 指标类型
     * @param measuredValue 实际测量值
     * @param source        数据来源
     * @return SD 值，正数表示高于中位数，负数表示低于中位数
     */
    BigDecimal calculateSd(Integer gender, BigDecimal ageMonths,
                            String indicatorType, BigDecimal measuredValue, String source);

    /**
     * 计算百分位
     *
     * @param gender        性别
     * @param ageMonths     月龄
     * @param indicatorType 指标类型
     * @param measuredValue 实际测量值
     * @param source        数据来源
     * @return 百分位（0-100），表示在同龄同性别人群中的位置
     */
    BigDecimal calculatePercentile(Integer gender, BigDecimal ageMonths,
                                   String indicatorType, BigDecimal measuredValue, String source);

    /**
     * SD 转百分位（基于标准正态分布 CDF）
     *
     * @param sd SD 值
     * @return 百分位 0-100
     */
    BigDecimal sdToPercentile(BigDecimal sd);

    /**
     * 百分位转 SD
     *
     * @param percentile 百分位 0-100
     * @return SD 值
     */
    BigDecimal percentileToSd(BigDecimal percentile);

    /**
     * 评估营养状态
     *
     * @param sd SD 值
     * @return 营养状态：SEVERE_WASTING/MODERATE_WASTING/MILD_WASTING/NORMAL/OVERWEIGHT/OBESITY/SEVERE_OBESITY
     */
    String assessNutritionStatus(BigDecimal sd);

    /**
     * 早产儿矫正月龄
     *
     * @param chronologicalAge 实际月龄
     * @param gestationalAgeWeeks 出生胎龄（周）
     * @return 矫正月龄（< 24 月龄按 (实际月龄 - (40 - 胎龄周数) / 4) 计算，否则返回实际月龄）
     */
    BigDecimal calculateCorrectedAge(BigDecimal chronologicalAge, Integer gestationalAgeWeeks);

    /**
     * 查询 Fenton 早产儿生长曲线数据
     *
     * @param gender              性别
     * @param gestationalAgeWeeks 胎龄（周）
     * @param indicatorType       指标类型
     * @return Fenton 标准数据
     */
    GrowthStandardDO getFentonStandard(Integer gender, BigDecimal gestationalAgeWeeks, String indicatorType);

    /**
     * 查询某来源下的所有月龄点（用于绘制生长曲线）
     */
    List<GrowthStandardDO> listStandardBySource(Integer gender, String indicatorType, String source);

}

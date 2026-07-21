package cn.iocoder.yudao.module.childhealth.service.growth.impl;

import cn.iocoder.yudao.module.childhealth.dal.dataobject.growth.GrowthStandardDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.growth.GrowthStandardMapper;
import cn.iocoder.yudao.module.childhealth.service.growth.GrowthStandardService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * 生长标准计算 Service 实现类
 *
 * 模块: 需求9 ▲ WHO/九城市标准差百分位自动计算
 *
 * 算法说明：
 * 1. SD 计算：基于 SD-3/-2/-1/0/+1/+2/+3 七个固定锚点，分段线性插值
 *    - value > median 时：找出 value 落在 SD+n ~ SD+(n+1) 之间，线性插值
 *    - value < median 时：找出 value 落在 SD-(n+1) ~ SD-n 之间，线性插值
 *    - value == median 时：SD = 0
 * 2. 百分位计算：基于标准正态分布的累积分布函数 Φ(SD)
 *    - 使用 Abramowitz & Stegun 近似公式计算 erf
 *    - percentile = 0.5 * (1 + erf(SD / sqrt(2))) * 100
 * 3. 早产儿矫正月龄：2 岁以内使用矫正月龄，2 岁后使用实际月龄
 *
 * @author 系统
 */
@Service
@Validated
@Slf4j
public class GrowthStandardServiceImpl implements GrowthStandardService {

    private static final BigDecimal TWO = new BigDecimal("2");
    private static final BigDecimal THREE = new BigDecimal("3");
    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");
    private static final BigDecimal SQRT_2 = new BigDecimal("1.4142135623730950488016887242097");
    private static final BigDecimal FULL_TERM_WEEKS = new BigDecimal("40");
    private static final BigDecimal CORRECTED_AGE_THRESHOLD = new BigDecimal("24");
    private static final int SCALE = 4;

    @Resource
    private GrowthStandardMapper growthStandardMapper;

    @Override
    public GrowthStandardDO getStandard(Integer gender, BigDecimal ageMonths,
                                         String indicatorType, String source) {
        if (gender == null || ageMonths == null || indicatorType == null || source == null) {
            return null;
        }
        // 1. 精确匹配
        GrowthStandardDO exact = growthStandardMapper.selectByAge(gender, ageMonths, indicatorType, source);
        if (exact != null) {
            return exact;
        }
        // 2. 线性插值：取上下界
        GrowthStandardDO lower = growthStandardMapper.selectLowerBound(gender, ageMonths, indicatorType, source);
        GrowthStandardDO upper = growthStandardMapper.selectUpperBound(gender, ageMonths, indicatorType, source);
        if (lower == null && upper == null) {
            log.warn("[getStandard] 未找到生长标准数据 gender={}, ageMonths={}, type={}, source={}",
                    gender, ageMonths, indicatorType, source);
            return null;
        }
        if (lower == null) return upper;
        if (upper == null) return lower;
        return interpolate(lower, upper, ageMonths);
    }

    @Override
    public BigDecimal calculateSd(Integer gender, BigDecimal ageMonths,
                                   String indicatorType, BigDecimal measuredValue, String source) {
        if (measuredValue == null || measuredValue.signum() <= 0) {
            return null;
        }
        GrowthStandardDO std = getStandard(gender, ageMonths, indicatorType, source);
        if (std == null || std.getMedian() == null) {
            return null;
        }
        BigDecimal median = std.getMedian();
        // SD == 0
        if (measuredValue.compareTo(median) == 0) {
            return BigDecimal.ZERO.setScale(SCALE, RoundingMode.HALF_UP);
        }
        // value > median：在 SD+n ~ SD+(n+1) 之间插值
        if (measuredValue.compareTo(median) > 0) {
            BigDecimal[] anchors = {std.getSdPos1(), std.getSdPos2(), std.getSdPos3()};
            for (int i = 0; i < anchors.length; i++) {
                BigDecimal upper = anchors[i];
                BigDecimal lower = (i == 0) ? median : anchors[i - 1];
                if (upper == null || lower == null) continue;
                // value 落在 lower ~ upper 之间
                if (measuredValue.compareTo(lower) >= 0 && measuredValue.compareTo(upper) <= 0) {
                    BigDecimal range = upper.subtract(lower);
                    if (range.signum() == 0) return new BigDecimal(i + 1).setScale(SCALE, RoundingMode.HALF_UP);
                    BigDecimal offset = measuredValue.subtract(lower).divide(range, SCALE, RoundingMode.HALF_UP);
                    return new BigDecimal(i + 1).subtract(BigDecimal.ONE).add(offset).setScale(SCALE, RoundingMode.HALF_UP);
                }
                // 超过最大锚点
                if (i == anchors.length - 1 && measuredValue.compareTo(upper) > 0) {
                    // 在 SD+3 之外，外推
                    BigDecimal prevLower = (i == 0) ? median : anchors[i - 1];
                    if (upper.subtract(prevLower).signum() == 0) {
                        return new BigDecimal(i + 1).setScale(SCALE, RoundingMode.HALF_UP);
                    }
                    BigDecimal slope = BigDecimal.ONE.divide(upper.subtract(prevLower), SCALE, RoundingMode.HALF_UP);
                    return new BigDecimal(i + 1).add(
                            measuredValue.subtract(upper).multiply(slope)).setScale(SCALE, RoundingMode.HALF_UP);
                }
            }
            return new BigDecimal("3").setScale(SCALE, RoundingMode.HALF_UP);
        }
        // value < median：在 SD-(n+1) ~ SD-n 之间插值
        BigDecimal[] anchors = {std.getSdNeg1(), std.getSdNeg2(), std.getSdNeg3()};
        for (int i = 0; i < anchors.length; i++) {
            BigDecimal lower = anchors[i];
            BigDecimal upper = (i == 0) ? median : anchors[i - 1];
            if (upper == null || lower == null) continue;
            if (measuredValue.compareTo(lower) >= 0 && measuredValue.compareTo(upper) <= 0) {
                BigDecimal range = upper.subtract(lower);
                if (range.signum() == 0) return new BigDecimal(-(i + 1)).setScale(SCALE, RoundingMode.HALF_UP);
                BigDecimal offset = upper.subtract(measuredValue).divide(range, SCALE, RoundingMode.HALF_UP);
                return new BigDecimal(-(i + 1)).add(offset).setScale(SCALE, RoundingMode.HALF_UP);
            }
            if (i == anchors.length - 1 && measuredValue.compareTo(lower) < 0) {
                BigDecimal prevUpper = (i == 0) ? median : anchors[i - 1];
                if (lower.subtract(prevUpper).signum() == 0) {
                    return new BigDecimal(-(i + 1)).setScale(SCALE, RoundingMode.HALF_UP);
                }
                BigDecimal slope = BigDecimal.ONE.divide(lower.subtract(prevUpper).abs(), SCALE, RoundingMode.HALF_UP);
                return new BigDecimal(-(i + 1)).subtract(
                        lower.subtract(measuredValue).multiply(slope)).setScale(SCALE, RoundingMode.HALF_UP);
            }
        }
        return new BigDecimal("-3").setScale(SCALE, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculatePercentile(Integer gender, BigDecimal ageMonths,
                                           String indicatorType, BigDecimal measuredValue, String source) {
        BigDecimal sd = calculateSd(gender, ageMonths, indicatorType, measuredValue, source);
        return sdToPercentile(sd);
    }

    @Override
    public BigDecimal sdToPercentile(BigDecimal sd) {
        if (sd == null) return null;
        // 标准 CDF = 0.5 * (1 + erf(sd / sqrt(2)))
        BigDecimal x = sd.divide(SQRT_2, SCALE, RoundingMode.HALF_UP);
        double erf = erf(x.doubleValue());
        double percentile = 0.5 * (1 + erf) * 100.0;
        // 限制在 [0.01, 99.99]
        if (percentile < 0.01) percentile = 0.01;
        if (percentile > 99.99) percentile = 99.99;
        return BigDecimal.valueOf(percentile).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal percentileToSd(BigDecimal percentile) {
        if (percentile == null) return null;
        double p = percentile.doubleValue();
        if (p <= 0) return new BigDecimal("-3").setScale(SCALE, RoundingMode.HALF_UP);
        if (p >= 100) return new BigDecimal("3").setScale(SCALE, RoundingMode.HALF_UP);
        // 逆运算：通过二分法求解 sd 使 Φ(sd) = p/100
        double targetP = p / 100.0;
        double lo = -3.0, hi = 3.0;
        for (int i = 0; i < 50; i++) {
            double mid = (lo + hi) / 2.0;
            double midP = 0.5 * (1 + erf(mid / Math.sqrt(2)));
            if (midP < targetP) lo = mid;
            else hi = mid;
        }
        return BigDecimal.valueOf((lo + hi) / 2.0).setScale(SCALE, RoundingMode.HALF_UP);
    }

    @Override
    public String assessNutritionStatus(BigDecimal sd) {
        if (sd == null) return "UNKNOWN";
        // WHO 营养不良分级（基于 SD）：
        // SD < -3: 重度消瘦
        // -3 <= SD < -2: 中度消瘦
        // -2 <= SD < -1: 轻度消瘦
        // -1 <= SD <= 1: 正常
        // 1 < SD <= 2: 体重过重
        // 2 < SD <= 3: 肥胖
        // SD > 3: 重度肥胖
        int cmpNeg3 = sd.compareTo(new BigDecimal("-3"));
        int cmpNeg2 = sd.compareTo(new BigDecimal("-2"));
        int cmpNeg1 = sd.compareTo(new BigDecimal("-1"));
        int cmpPos1 = sd.compareTo(BigDecimal.ONE);
        int cmpPos2 = sd.compareTo(TWO);
        int cmpPos3 = sd.compareTo(THREE);
        if (cmpNeg3 < 0) return "SEVERE_WASTING";
        if (cmpNeg2 < 0) return "MODERATE_WASTING";
        if (cmpNeg1 < 0) return "MILD_WASTING";
        if (cmpPos1 <= 0) return "NORMAL";
        if (cmpPos2 <= 0) return "OVERWEIGHT";
        if (cmpPos3 <= 0) return "OBESITY";
        return "SEVERE_OBESITY";
    }

    @Override
    public BigDecimal calculateCorrectedAge(BigDecimal chronologicalAge, Integer gestationalAgeWeeks) {
        if (chronologicalAge == null) return null;
        if (gestationalAgeWeeks == null || gestationalAgeWeeks >= 37) {
            return chronologicalAge;
        }
        // 早产儿矫正月龄：2 岁（24 月）以内按 (实际月龄 - (40 - 胎龄周数) / 4) 计算
        if (chronologicalAge.compareTo(CORRECTED_AGE_THRESHOLD) > 0) {
            return chronologicalAge;
        }
        BigDecimal weeksEarly = FULL_TERM_WEEKS.subtract(new BigDecimal(gestationalAgeWeeks));
        BigDecimal monthsToSubtract = weeksEarly.divide(new BigDecimal("4"), SCALE, RoundingMode.HALF_UP);
        BigDecimal corrected = chronologicalAge.subtract(monthsToSubtract);
        return corrected.signum() < 0 ? BigDecimal.ZERO.setScale(SCALE, RoundingMode.HALF_UP)
                : corrected.setScale(SCALE, RoundingMode.HALF_UP);
    }

    @Override
    public GrowthStandardDO getFentonStandard(Integer gender, BigDecimal gestationalAgeWeeks, String indicatorType) {
        if (gender == null || gestationalAgeWeeks == null || indicatorType == null) {
            return null;
        }
        return growthStandardMapper.selectByGestationalAge(gender, gestationalAgeWeeks, indicatorType);
    }

    @Override
    public List<GrowthStandardDO> listStandardBySource(Integer gender, String indicatorType, String source) {
        return growthStandardMapper.selectListBySource(gender, indicatorType, source);
    }

    // ============ 私有辅助方法 ============

    /**
     * 对两个月龄点的标准数据做线性插值
     */
    private GrowthStandardDO interpolate(GrowthStandardDO lower, GrowthStandardDO upper, BigDecimal ageMonths) {
        BigDecimal lowerAge = lower.getAgeMonths();
        BigDecimal upperAge = upper.getAgeMonths();
        BigDecimal range = upperAge.subtract(lowerAge);
        if (range.signum() == 0) return lower;
        BigDecimal ratio = ageMonths.subtract(lowerAge).divide(range, SCALE, RoundingMode.HALF_UP);
        GrowthStandardDO result = new GrowthStandardDO();
        result.setGender(lower.getGender());
        result.setAgeMonths(ageMonths);
        result.setIndicatorType(lower.getIndicatorType());
        result.setSource(lower.getSource());
        result.setSdNeg3(interpolateValue(lower.getSdNeg3(), upper.getSdNeg3(), ratio));
        result.setSdNeg2(interpolateValue(lower.getSdNeg2(), upper.getSdNeg2(), ratio));
        result.setSdNeg1(interpolateValue(lower.getSdNeg1(), upper.getSdNeg1(), ratio));
        result.setMedian(interpolateValue(lower.getMedian(), upper.getMedian(), ratio));
        result.setSdPos1(interpolateValue(lower.getSdPos1(), upper.getSdPos1(), ratio));
        result.setSdPos2(interpolateValue(lower.getSdPos2(), upper.getSdPos2(), ratio));
        result.setSdPos3(interpolateValue(lower.getSdPos3(), upper.getSdPos3(), ratio));
        result.setP3(interpolateValue(lower.getP3(), upper.getP3(), ratio));
        result.setP15(interpolateValue(lower.getP15(), upper.getP15(), ratio));
        result.setP50(interpolateValue(lower.getP50(), upper.getP50(), ratio));
        result.setP85(interpolateValue(lower.getP85(), upper.getP85(), ratio));
        result.setP97(interpolateValue(lower.getP97(), upper.getP97(), ratio));
        return result;
    }

    private BigDecimal interpolateValue(BigDecimal lower, BigDecimal upper, BigDecimal ratio) {
        if (lower == null || upper == null) return null;
        return lower.add(upper.subtract(lower).multiply(ratio)).setScale(SCALE, RoundingMode.HALF_UP);
    }

    /**
     * 误差函数 erf(x) 的近似实现
     * 采用 Abramowitz & Stegun 公式 7.1.26，最大误差 < 1.5e-7
     */
    private static double erf(double x) {
        double t = 1.0 / (1.0 + 0.3275911 * Math.abs(x));
        double[] a = {0.254829592, -0.284496736, 1.421413741, -1.453152027, 1.061405429};
        double poly = a[0] * t + a[1] * t * t + a[2] * t * t * t + a[3] * Math.pow(t, 4) + a[4] * Math.pow(t, 5);
        double result = 1.0 - poly * Math.exp(-x * x);
        return x >= 0 ? result : -result;
    }

}

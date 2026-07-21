package cn.iocoder.yudao.module.childhealth.dal.mysql.growth;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.growth.GrowthStandardDO;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;

/**
 * 生长标准数据 Mapper
 *
 * 模块: 需求9 ▲ WHO/九城市标准差百分位自动计算
 *
 * @author 系统
 */
@Mapper
public interface GrowthStandardMapper extends BaseMapperX<GrowthStandardDO> {

    /**
     * 根据性别 + 月龄 + 指标类型 + 数据来源精确查询
     */
    default GrowthStandardDO selectByAge(Integer gender, BigDecimal ageMonths, String indicatorType, String source) {
        return selectOne(Wrappers.<GrowthStandardDO>lambdaQuery()
                .eq(GrowthStandardDO::getGender, gender)
                .eq(GrowthStandardDO::getAgeMonths, ageMonths)
                .eq(GrowthStandardDO::getIndicatorType, indicatorType)
                .eq(GrowthStandardDO::getSource, source)
                .last("LIMIT 1"));
    }

    /**
     * 查询小于等于目标月龄的最近一条记录（用于插值）
     */
    default GrowthStandardDO selectLowerBound(Integer gender, BigDecimal ageMonths,
                                              String indicatorType, String source) {
        return selectOne(Wrappers.<GrowthStandardDO>lambdaQuery()
                .eq(GrowthStandardDO::getGender, gender)
                .eq(GrowthStandardDO::getIndicatorType, indicatorType)
                .eq(GrowthStandardDO::getSource, source)
                .le(GrowthStandardDO::getAgeMonths, ageMonths)
                .orderByDesc(GrowthStandardDO::getAgeMonths)
                .last("LIMIT 1"));
    }

    /**
     * 查询大于目标月龄的最近一条记录（用于插值）
     */
    default GrowthStandardDO selectUpperBound(Integer gender, BigDecimal ageMonths,
                                              String indicatorType, String source) {
        return selectOne(Wrappers.<GrowthStandardDO>lambdaQuery()
                .eq(GrowthStandardDO::getGender, gender)
                .eq(GrowthStandardDO::getIndicatorType, indicatorType)
                .eq(GrowthStandardDO::getSource, source)
                .gt(GrowthStandardDO::getAgeMonths, ageMonths)
                .orderByAsc(GrowthStandardDO::getAgeMonths)
                .last("LIMIT 1"));
    }

    /**
     * 根据胎龄查询 Fenton 数据
     */
    default GrowthStandardDO selectByGestationalAge(Integer gender, BigDecimal gestationalAgeWeeks,
                                                    String indicatorType) {
        return selectOne(Wrappers.<GrowthStandardDO>lambdaQuery()
                .eq(GrowthStandardDO::getGender, gender)
                .eq(GrowthStandardDO::getIndicatorType, indicatorType)
                .eq(GrowthStandardDO::getSource, "FENTON")
                .eq(GrowthStandardDO::getGestationalAgeWeeks, gestationalAgeWeeks)
                .last("LIMIT 1"));
    }

    /**
     * 查询某来源下的所有月龄点（用于绘制生长曲线）
     */
    default List<GrowthStandardDO> selectListBySource(Integer gender, String indicatorType, String source) {
        return selectList(Wrappers.<GrowthStandardDO>lambdaQuery()
                .eq(GrowthStandardDO::getGender, gender)
                .eq(GrowthStandardDO::getIndicatorType, indicatorType)
                .eq(GrowthStandardDO::getSource, source)
                .orderByAsc(GrowthStandardDO::getAgeMonths));
    }

}

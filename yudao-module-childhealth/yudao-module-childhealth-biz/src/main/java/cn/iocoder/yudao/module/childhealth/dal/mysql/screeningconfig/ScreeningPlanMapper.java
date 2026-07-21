package cn.iocoder.yudao.module.childhealth.dal.mysql.screeningconfig;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig.ScreeningPlanDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 体检方案配置 Mapper
 *
 * 模块: C.五健筛查配置
 * 创建日期: 2026-07-20
 */
@Mapper
public interface ScreeningPlanMapper extends BaseMapperX<ScreeningPlanDO> {

    /**
     * 按方案编码查询
     */
    default ScreeningPlanDO selectByPlanCode(String planCode) {
        return selectOne(ScreeningPlanDO::getPlanCode, planCode);
    }

    /**
     * 按方案类型查询启用方案列表
     */
    default List<ScreeningPlanDO> selectListByPlanType(Integer planType) {
        return selectList(new LambdaQueryWrapperX<ScreeningPlanDO>()
                .eqIfPresent(ScreeningPlanDO::getPlanType, planType)
                .eqIfPresent(ScreeningPlanDO::getStatus, 1)
                .orderByAsc(ScreeningPlanDO::getApplicableAgeMin));
    }

    /**
     * 查询指定类型的默认方案
     */
    default ScreeningPlanDO selectDefaultByPlanType(Integer planType) {
        return selectOne(new LambdaQueryWrapperX<ScreeningPlanDO>()
                .eqIfPresent(ScreeningPlanDO::getPlanType, planType)
                .eqIfPresent(ScreeningPlanDO::getDefaultPlan, 1)
                .eqIfPresent(ScreeningPlanDO::getStatus, 1));
    }

    /**
     * 按适用月龄范围查询启用方案
     */
    default List<ScreeningPlanDO> selectListByApplicableAge(Integer ageMonth) {
        return selectList(new LambdaQueryWrapperX<ScreeningPlanDO>()
                .leIfPresent(ScreeningPlanDO::getApplicableAgeMin, ageMonth)
                .geIfPresent(ScreeningPlanDO::getApplicableAgeMax, ageMonth)
                .eqIfPresent(ScreeningPlanDO::getStatus, 1));
    }

    /**
     * 按适用性别查询启用方案
     */
    default List<ScreeningPlanDO> selectListByApplicableGender(Integer applicableGender) {
        return selectList(new LambdaQueryWrapperX<ScreeningPlanDO>()
                .eqIfPresent(ScreeningPlanDO::getApplicableGender, applicableGender)
                .eqIfPresent(ScreeningPlanDO::getStatus, 1));
    }

    /**
     * 查询所有启用方案
     */
    default List<ScreeningPlanDO> selectActiveList() {
        return selectList(new LambdaQueryWrapperX<ScreeningPlanDO>()
                .eqIfPresent(ScreeningPlanDO::getStatus, 1)
                .orderByAsc(ScreeningPlanDO::getPlanType)
                .orderByAsc(ScreeningPlanDO::getApplicableAgeMin));
    }

}

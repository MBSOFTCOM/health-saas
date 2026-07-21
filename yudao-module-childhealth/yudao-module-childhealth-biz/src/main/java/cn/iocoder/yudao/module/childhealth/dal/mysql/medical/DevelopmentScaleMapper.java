package cn.iocoder.yudao.module.childhealth.dal.mysql.medical;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.medical.DevelopmentScaleDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 发育评估量表定义表 Mapper
 *
 * 模块: A. 儿童基础健康检查（A6-发育评估量表定义表，18+套）
 * 创建日期: 2026-07-20
 */
@Mapper
public interface DevelopmentScaleMapper extends BaseMapperX<DevelopmentScaleDO> {

    /**
     * 按量表编码查询
     */
    default DevelopmentScaleDO selectByCode(String scaleCode) {
        return selectOne(DevelopmentScaleDO::getScaleCode, scaleCode);
    }

    /**
     * 按量表类型查询启用量表
     */
    default List<DevelopmentScaleDO> selectListByType(String scaleType) {
        return selectList(new LambdaQueryWrapperX<DevelopmentScaleDO>()
                .eqIfPresent(DevelopmentScaleDO::getScaleType, scaleType)
                .eqIfPresent(DevelopmentScaleDO::getStatus, 1));
    }

    /**
     * 按状态查询
     */
    default List<DevelopmentScaleDO> selectListByStatus(Integer status) {
        return selectList(new LambdaQueryWrapperX<DevelopmentScaleDO>()
                .eqIfPresent(DevelopmentScaleDO::getStatus, status));
    }

    /**
     * 按月龄查询适用量表（满足最小/最大月龄区间且启用）
     */
    default List<DevelopmentScaleDO> selectListByAgeMonth(Integer ageMonth) {
        return selectList(new LambdaQueryWrapperX<DevelopmentScaleDO>()
                .leIfPresent(DevelopmentScaleDO::getApplicableAgeMin, ageMonth)
                .geIfPresent(DevelopmentScaleDO::getApplicableAgeMax, ageMonth)
                .eqIfPresent(DevelopmentScaleDO::getStatus, 1));
    }

    /**
     * 按适用性别查询
     */
    default List<DevelopmentScaleDO> selectListByGender(Integer applicableGender) {
        return selectList(new LambdaQueryWrapperX<DevelopmentScaleDO>()
                .eqIfPresent(DevelopmentScaleDO::getApplicableGender, applicableGender)
                .eqIfPresent(DevelopmentScaleDO::getStatus, 1));
    }

    /**
     * 查询所有启用量表
     */
    default List<DevelopmentScaleDO> selectActiveList() {
        return selectList(new LambdaQueryWrapperX<DevelopmentScaleDO>()
                .eqIfPresent(DevelopmentScaleDO::getStatus, 1));
    }

}

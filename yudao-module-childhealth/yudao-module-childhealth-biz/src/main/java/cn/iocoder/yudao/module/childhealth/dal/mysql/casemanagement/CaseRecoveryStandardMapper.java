package cn.iocoder.yudao.module.childhealth.dal.mysql.casemanagement;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.casemanagement.CaseRecoveryStandardDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 专案康复达标标准 Mapper
 *
 * 模块: B.专案管理
 * 创建日期: 2026-07-20
 */
@Mapper
public interface CaseRecoveryStandardMapper extends BaseMapperX<CaseRecoveryStandardDO> {

    /**
     * 按指标编码查询
     */
    default CaseRecoveryStandardDO selectByIndicatorCode(String indicatorCode) {
        return selectOne(CaseRecoveryStandardDO::getIndicatorCode, indicatorCode);
    }

    /**
     * 按专案类型查询列表
     */
    default List<CaseRecoveryStandardDO> selectListByCaseType(Integer caseType) {
        return selectList(new LambdaQueryWrapperX<CaseRecoveryStandardDO>()
                .eqIfPresent(CaseRecoveryStandardDO::getCaseType, caseType)
                .orderByAsc(CaseRecoveryStandardDO::getIndicatorCode));
    }

    /**
     * 按专案类型和子类型查询列表
     */
    default List<CaseRecoveryStandardDO> selectListByCaseTypeAndSubtype(Integer caseType, String caseSubtype) {
        return selectList(new LambdaQueryWrapperX<CaseRecoveryStandardDO>()
                .eqIfPresent(CaseRecoveryStandardDO::getCaseType, caseType)
                .eqIfPresent(CaseRecoveryStandardDO::getCaseSubtype, caseSubtype)
                .orderByAsc(CaseRecoveryStandardDO::getIndicatorCode));
    }

    /**
     * 按专案类型和状态查询列表
     */
    default List<CaseRecoveryStandardDO> selectListByCaseTypeAndStatus(Integer caseType, Integer status) {
        return selectList(new LambdaQueryWrapperX<CaseRecoveryStandardDO>()
                .eqIfPresent(CaseRecoveryStandardDO::getCaseType, caseType)
                .eqIfPresent(CaseRecoveryStandardDO::getStatus, status)
                .orderByAsc(CaseRecoveryStandardDO::getIndicatorCode));
    }

    /**
     * 查询所有启用的达标标准
     */
    default List<CaseRecoveryStandardDO> selectActiveList() {
        return selectList(new LambdaQueryWrapperX<CaseRecoveryStandardDO>()
                .eqIfPresent(CaseRecoveryStandardDO::getStatus, 1)
                .orderByAsc(CaseRecoveryStandardDO::getCaseType)
                .orderByAsc(CaseRecoveryStandardDO::getIndicatorCode));
    }

}

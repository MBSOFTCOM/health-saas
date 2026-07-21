package cn.iocoder.yudao.module.childhealth.dal.mysql.casemanagement;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.casemanagement.CaseManagementDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 专案主表 Mapper
 *
 * 模块: B.专案管理
 * 创建日期: 2026-07-20
 */
@Mapper
public interface CaseManagementMapper extends BaseMapperX<CaseManagementDO> {

    /**
     * 按专案编号查询
     */
    default CaseManagementDO selectByCaseNo(String caseNo) {
        return selectOne(CaseManagementDO::getCaseNo, caseNo);
    }

    /**
     * 按儿童ID查询专案列表
     */
    default List<CaseManagementDO> selectListByChildId(Long childId) {
        return selectList(new LambdaQueryWrapperX<CaseManagementDO>()
                .eqIfPresent(CaseManagementDO::getChildId, childId)
                .orderByDesc(CaseManagementDO::getEstablishDate));
    }

    /**
     * 按专案类型查询列表
     */
    default List<CaseManagementDO> selectListByCaseType(Integer caseType) {
        return selectList(new LambdaQueryWrapperX<CaseManagementDO>()
                .eqIfPresent(CaseManagementDO::getCaseType, caseType)
                .orderByDesc(CaseManagementDO::getEstablishDate));
    }

    /**
     * 按状态查询列表
     */
    default List<CaseManagementDO> selectListByStatus(Integer status) {
        return selectList(new LambdaQueryWrapperX<CaseManagementDO>()
                .eqIfPresent(CaseManagementDO::getStatus, status)
                .orderByDesc(CaseManagementDO::getEstablishDate));
    }

    /**
     * 按责任医生ID查询列表
     */
    default List<CaseManagementDO> selectListByResponsibleDoctorId(Long responsibleDoctorId) {
        return selectList(new LambdaQueryWrapperX<CaseManagementDO>()
                .eqIfPresent(CaseManagementDO::getResponsibleDoctorId, responsibleDoctorId)
                .orderByDesc(CaseManagementDO::getEstablishDate));
    }

    /**
     * 按专案等级查询列表
     */
    default List<CaseManagementDO> selectListByCaseLevel(Integer caseLevel) {
        return selectList(new LambdaQueryWrapperX<CaseManagementDO>()
                .eqIfPresent(CaseManagementDO::getCaseLevel, caseLevel)
                .orderByDesc(CaseManagementDO::getEstablishDate));
    }

}

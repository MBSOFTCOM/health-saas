package cn.iocoder.yudao.module.childhealth.dal.mysql.medical;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.medical.DevelopmentAssessmentRecordDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

/**
 * 发育评估记录表 Mapper
 *
 * 模块: A. 儿童基础健康检查（A8-发育评估记录表）
 * 创建日期: 2026-07-20
 */
@Mapper
public interface DevelopmentAssessmentRecordMapper extends BaseMapperX<DevelopmentAssessmentRecordDO> {

    /**
     * 按儿童ID查询评估记录
     */
    default List<DevelopmentAssessmentRecordDO> selectListByChildId(Long childId) {
        return selectList(new LambdaQueryWrapperX<DevelopmentAssessmentRecordDO>()
                .eqIfPresent(DevelopmentAssessmentRecordDO::getChildId, childId)
                .orderByDesc(DevelopmentAssessmentRecordDO::getAssessDate));
    }

    /**
     * 按量表ID查询评估记录
     */
    default List<DevelopmentAssessmentRecordDO> selectListByScaleId(Long scaleId) {
        return selectList(new LambdaQueryWrapperX<DevelopmentAssessmentRecordDO>()
                .eqIfPresent(DevelopmentAssessmentRecordDO::getScaleId, scaleId)
                .orderByDesc(DevelopmentAssessmentRecordDO::getAssessDate));
    }

    /**
     * 按评估日期范围查询
     */
    default List<DevelopmentAssessmentRecordDO> selectListByDateRange(LocalDate startDate, LocalDate endDate) {
        return selectList(new LambdaQueryWrapperX<DevelopmentAssessmentRecordDO>()
                .geIfPresent(DevelopmentAssessmentRecordDO::getAssessDate, startDate)
                .leIfPresent(DevelopmentAssessmentRecordDO::getAssessDate, endDate)
                .orderByDesc(DevelopmentAssessmentRecordDO::getAssessDate));
    }

    /**
     * 按状态查询（0进行中 1已完成 2已废弃）
     */
    default List<DevelopmentAssessmentRecordDO> selectListByStatus(Integer status) {
        return selectList(new LambdaQueryWrapperX<DevelopmentAssessmentRecordDO>()
                .eqIfPresent(DevelopmentAssessmentRecordDO::getStatus, status)
                .orderByDesc(DevelopmentAssessmentRecordDO::getAssessDate));
    }

    /**
     * 按风险等级查询（1正常 2临界 3异常）
     */
    default List<DevelopmentAssessmentRecordDO> selectListByRiskLevel(Integer riskLevel) {
        return selectList(new LambdaQueryWrapperX<DevelopmentAssessmentRecordDO>()
                .eqIfPresent(DevelopmentAssessmentRecordDO::getRiskLevel, riskLevel)
                .orderByDesc(DevelopmentAssessmentRecordDO::getAssessDate));
    }

    /**
     * 按关联病历ID查询
     */
    default List<DevelopmentAssessmentRecordDO> selectListByMedicalRecordId(Long medicalRecordId) {
        return selectList(new LambdaQueryWrapperX<DevelopmentAssessmentRecordDO>()
                .eqIfPresent(DevelopmentAssessmentRecordDO::getMedicalRecordId, medicalRecordId)
                .orderByDesc(DevelopmentAssessmentRecordDO::getAssessDate));
    }

}

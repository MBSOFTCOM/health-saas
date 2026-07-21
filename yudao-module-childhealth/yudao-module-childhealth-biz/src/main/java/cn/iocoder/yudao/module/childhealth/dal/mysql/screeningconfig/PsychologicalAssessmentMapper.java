package cn.iocoder.yudao.module.childhealth.dal.mysql.screeningconfig;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig.PsychologicalAssessmentDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

/**
 * 心理量表评估记录 Mapper
 *
 * 模块: C.五健筛查配置
 * 创建日期: 2026-07-20
 */
@Mapper
public interface PsychologicalAssessmentMapper extends BaseMapperX<PsychologicalAssessmentDO> {

    /**
     * 按儿童ID查询评估记录
     */
    default List<PsychologicalAssessmentDO> selectListByChildId(Long childId) {
        return selectList(new LambdaQueryWrapperX<PsychologicalAssessmentDO>()
                .eqIfPresent(PsychologicalAssessmentDO::getChildId, childId)
                .orderByDesc(PsychologicalAssessmentDO::getAssessDate));
    }

    /**
     * 按量表ID查询评估记录
     */
    default List<PsychologicalAssessmentDO> selectListByScaleId(Long scaleId) {
        return selectList(new LambdaQueryWrapperX<PsychologicalAssessmentDO>()
                .eqIfPresent(PsychologicalAssessmentDO::getScaleId, scaleId)
                .orderByDesc(PsychologicalAssessmentDO::getAssessDate));
    }

    /**
     * 按批次ID查询评估记录
     */
    default List<PsychologicalAssessmentDO> selectListByBatchId(Long batchId) {
        return selectList(PsychologicalAssessmentDO::getBatchId, batchId);
    }

    /**
     * 按风险等级查询评估记录
     */
    default List<PsychologicalAssessmentDO> selectListByRiskLevel(Integer riskLevel) {
        return selectList(new LambdaQueryWrapperX<PsychologicalAssessmentDO>()
                .eqIfPresent(PsychologicalAssessmentDO::getRiskLevel, riskLevel)
                .orderByDesc(PsychologicalAssessmentDO::getAssessDate));
    }

    /**
     * 按状态查询评估记录
     */
    default List<PsychologicalAssessmentDO> selectListByStatus(Integer status) {
        return selectList(new LambdaQueryWrapperX<PsychologicalAssessmentDO>()
                .eqIfPresent(PsychologicalAssessmentDO::getStatus, status)
                .orderByDesc(PsychologicalAssessmentDO::getAssessDate));
    }

    /**
     * 按评估日期范围查询
     */
    default List<PsychologicalAssessmentDO> selectListByDateRange(LocalDate startDate, LocalDate endDate) {
        return selectList(new LambdaQueryWrapperX<PsychologicalAssessmentDO>()
                .geIfPresent(PsychologicalAssessmentDO::getAssessDate, startDate)
                .leIfPresent(PsychologicalAssessmentDO::getAssessDate, endDate)
                .orderByDesc(PsychologicalAssessmentDO::getAssessDate));
    }

}

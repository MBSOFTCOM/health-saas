package cn.iocoder.yudao.module.childhealth.dal.mysql.screeningconfig;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig.QuestionnaireRecordDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 问卷答卷 Mapper
 *
 * 模块: C.五健筛查配置
 * 创建日期: 2026-07-20
 */
@Mapper
public interface QuestionnaireRecordMapper extends BaseMapperX<QuestionnaireRecordDO> {

    /**
     * 按问卷ID查询所有答卷
     */
    default List<QuestionnaireRecordDO> selectListByQuestionnaireId(Long questionnaireId) {
        return selectList(QuestionnaireRecordDO::getQuestionnaireId, questionnaireId);
    }

    /**
     * 按儿童ID查询答卷
     */
    default List<QuestionnaireRecordDO> selectListByChildId(Long childId) {
        return selectList(new LambdaQueryWrapperX<QuestionnaireRecordDO>()
                .eqIfPresent(QuestionnaireRecordDO::getChildId, childId)
                .orderByDesc(QuestionnaireRecordDO::getSubmitTime));
    }

    /**
     * 按批次ID查询答卷
     */
    default List<QuestionnaireRecordDO> selectListByBatchId(Long batchId) {
        return selectList(new LambdaQueryWrapperX<QuestionnaireRecordDO>()
                .eqIfPresent(QuestionnaireRecordDO::getBatchId, batchId)
                .orderByDesc(QuestionnaireRecordDO::getSubmitTime));
    }

    /**
     * 按状态查询答卷
     */
    default List<QuestionnaireRecordDO> selectListByStatus(Integer status) {
        return selectList(new LambdaQueryWrapperX<QuestionnaireRecordDO>()
                .eqIfPresent(QuestionnaireRecordDO::getStatus, status)
                .orderByDesc(QuestionnaireRecordDO::getSubmitTime));
    }

    /**
     * 按提交人及类型查询答卷
     */
    default List<QuestionnaireRecordDO> selectListBySubmitter(Long submitterId, Integer submitterType) {
        return selectList(new LambdaQueryWrapperX<QuestionnaireRecordDO>()
                .eqIfPresent(QuestionnaireRecordDO::getSubmitterId, submitterId)
                .eqIfPresent(QuestionnaireRecordDO::getSubmitterType, submitterType)
                .orderByDesc(QuestionnaireRecordDO::getSubmitTime));
    }

}

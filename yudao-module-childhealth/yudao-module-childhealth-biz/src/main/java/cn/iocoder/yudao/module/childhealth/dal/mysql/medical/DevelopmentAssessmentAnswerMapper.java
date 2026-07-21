package cn.iocoder.yudao.module.childhealth.dal.mysql.medical;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.medical.DevelopmentAssessmentAnswerDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 发育评估答题表 Mapper
 *
 * 模块: A. 儿童基础健康检查（A9-发育评估答题表）
 * 创建日期: 2026-07-20
 */
@Mapper
public interface DevelopmentAssessmentAnswerMapper extends BaseMapperX<DevelopmentAssessmentAnswerDO> {

    /**
     * 按评估记录ID查询所有答题
     */
    default List<DevelopmentAssessmentAnswerDO> selectListByRecordId(Long recordId) {
        return selectList(new LambdaQueryWrapperX<DevelopmentAssessmentAnswerDO>()
                .eqIfPresent(DevelopmentAssessmentAnswerDO::getRecordId, recordId));
    }

    /**
     * 按评估记录ID和题目ID查询单条答题
     */
    default DevelopmentAssessmentAnswerDO selectByRecordIdAndQuestionId(Long recordId, Long questionId) {
        return selectOne(new LambdaQueryWrapperX<DevelopmentAssessmentAnswerDO>()
                .eqIfPresent(DevelopmentAssessmentAnswerDO::getRecordId, recordId)
                .eqIfPresent(DevelopmentAssessmentAnswerDO::getQuestionId, questionId));
    }

    /**
     * 按题目ID查询所有答题（跨记录统计）
     */
    default List<DevelopmentAssessmentAnswerDO> selectListByQuestionId(Long questionId) {
        return selectList(new LambdaQueryWrapperX<DevelopmentAssessmentAnswerDO>()
                .eqIfPresent(DevelopmentAssessmentAnswerDO::getQuestionId, questionId));
    }

    /**
     * 统计评估记录的答题数量
     */
    default Long selectCountByRecordId(Long recordId) {
        return selectCount(new LambdaQueryWrapperX<DevelopmentAssessmentAnswerDO>()
                .eqIfPresent(DevelopmentAssessmentAnswerDO::getRecordId, recordId));
    }

    /**
     * 删除评估记录的所有答题（用于重新评估场景）
     */
    default int deleteByRecordId(Long recordId) {
        return delete(new LambdaQueryWrapperX<DevelopmentAssessmentAnswerDO>()
                .eqIfPresent(DevelopmentAssessmentAnswerDO::getRecordId, recordId));
    }

}

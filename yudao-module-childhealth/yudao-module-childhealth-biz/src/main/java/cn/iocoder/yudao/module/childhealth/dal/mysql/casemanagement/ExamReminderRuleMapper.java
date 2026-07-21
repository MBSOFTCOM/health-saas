package cn.iocoder.yudao.module.childhealth.dal.mysql.casemanagement;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.casemanagement.ExamReminderRuleDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 体检催检规则 Mapper
 *
 * 模块: B.体检催检
 * 创建日期: 2026-07-20
 */
@Mapper
public interface ExamReminderRuleMapper extends BaseMapperX<ExamReminderRuleDO> {

    /**
     * 按规则编码查询
     */
    default ExamReminderRuleDO selectByRuleCode(String ruleCode) {
        return selectOne(ExamReminderRuleDO::getRuleCode, ruleCode);
    }

    /**
     * 按体检类型查询列表
     */
    default List<ExamReminderRuleDO> selectListByExamType(String examType) {
        return selectList(new LambdaQueryWrapperX<ExamReminderRuleDO>()
                .eqIfPresent(ExamReminderRuleDO::getExamType, examType)
                .orderByAsc(ExamReminderRuleDO::getAgeMonth));
    }

    /**
     * 按适用月龄查询列表
     */
    default List<ExamReminderRuleDO> selectListByAgeMonth(Integer ageMonth) {
        return selectList(new LambdaQueryWrapperX<ExamReminderRuleDO>()
                .eqIfPresent(ExamReminderRuleDO::getAgeMonth, ageMonth)
                .orderByAsc(ExamReminderRuleDO::getExamType));
    }

    /**
     * 按目标人群查询列表
     */
    default List<ExamReminderRuleDO> selectListByTargetAudience(Integer targetAudience) {
        return selectList(new LambdaQueryWrapperX<ExamReminderRuleDO>()
                .eqIfPresent(ExamReminderRuleDO::getTargetAudience, targetAudience)
                .orderByAsc(ExamReminderRuleDO::getAgeMonth));
    }

    /**
     * 按科室ID查询列表
     */
    default List<ExamReminderRuleDO> selectListByDeptId(Long deptId) {
        return selectList(new LambdaQueryWrapperX<ExamReminderRuleDO>()
                .eqIfPresent(ExamReminderRuleDO::getDeptId, deptId)
                .orderByAsc(ExamReminderRuleDO::getAgeMonth));
    }

    /**
     * 查询所有启用的催检规则
     */
    default List<ExamReminderRuleDO> selectActiveList() {
        return selectList(new LambdaQueryWrapperX<ExamReminderRuleDO>()
                .eqIfPresent(ExamReminderRuleDO::getStatus, 1)
                .orderByAsc(ExamReminderRuleDO::getAgeMonth)
                .orderByAsc(ExamReminderRuleDO::getPriority));
    }

}

package cn.iocoder.yudao.module.childhealth.dal.mysql.casemanagement;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.casemanagement.ExamReminderLogDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 体检催检推送日志 Mapper
 *
 * 模块: B.体检催检
 * 创建日期: 2026-07-20
 */
@Mapper
public interface ExamReminderLogMapper extends BaseMapperX<ExamReminderLogDO> {

    /**
     * 按规则ID查询推送日志列表
     */
    default List<ExamReminderLogDO> selectListByRuleId(Long ruleId) {
        return selectList(new LambdaQueryWrapperX<ExamReminderLogDO>()
                .eqIfPresent(ExamReminderLogDO::getRuleId, ruleId)
                .orderByDesc(ExamReminderLogDO::getPushTime));
    }

    /**
     * 按儿童ID查询推送日志列表
     */
    default List<ExamReminderLogDO> selectListByChildId(Long childId) {
        return selectList(new LambdaQueryWrapperX<ExamReminderLogDO>()
                .eqIfPresent(ExamReminderLogDO::getChildId, childId)
                .orderByDesc(ExamReminderLogDO::getPushTime));
    }

    /**
     * 按推送状态查询列表
     */
    default List<ExamReminderLogDO> selectListByPushStatus(Integer pushStatus) {
        return selectList(new LambdaQueryWrapperX<ExamReminderLogDO>()
                .eqIfPresent(ExamReminderLogDO::getPushStatus, pushStatus)
                .orderByDesc(ExamReminderLogDO::getPushTime));
    }

    /**
     * 按推送渠道查询列表
     */
    default List<ExamReminderLogDO> selectListByPushChannel(String pushChannel) {
        return selectList(new LambdaQueryWrapperX<ExamReminderLogDO>()
                .eqIfPresent(ExamReminderLogDO::getPushChannel, pushChannel)
                .orderByDesc(ExamReminderLogDO::getPushTime));
    }

    /**
     * 按推送时间范围查询列表
     */
    default List<ExamReminderLogDO> selectListByPushTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        return selectList(new LambdaQueryWrapperX<ExamReminderLogDO>()
                .geIfPresent(ExamReminderLogDO::getPushTime, startTime)
                .leIfPresent(ExamReminderLogDO::getPushTime, endTime)
                .orderByDesc(ExamReminderLogDO::getPushTime));
    }

    /**
     * 按儿童ID和推送状态查询列表
     */
    default List<ExamReminderLogDO> selectListByChildIdAndStatus(Long childId, Integer pushStatus) {
        return selectList(new LambdaQueryWrapperX<ExamReminderLogDO>()
                .eqIfPresent(ExamReminderLogDO::getChildId, childId)
                .eqIfPresent(ExamReminderLogDO::getPushStatus, pushStatus)
                .orderByDesc(ExamReminderLogDO::getPushTime));
    }

}

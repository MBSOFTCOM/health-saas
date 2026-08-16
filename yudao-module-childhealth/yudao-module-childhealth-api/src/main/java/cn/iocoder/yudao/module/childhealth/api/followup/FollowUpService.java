package cn.iocoder.yudao.module.childhealth.api.followup;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.api.followup.dto.FollowUpDTO.*;

import java.util.List;

/**
 * 随访管理Service接口
 */
public interface FollowUpService {

    // ==================== 随访记录管理 ====================

    /**
     * 创建随访记录
     */
    Long createFollowRecord(FollowRecordCreateRequest request);

    /**
     * 更新随访记录
     */
    void updateFollowRecord(FollowRecordUpdateRequest request);

    /**
     * 删除随访记录
     */
    void deleteFollowRecord(Long id);

    /**
     * 获取随访记录详情
     */
    FollowRecordResponse getFollowRecord(Long id);

    /**
     * 获取儿童的随访记录列表
     */
    List<FollowRecordResponse> getFollowRecordsByChild(Long childId);

    /**
     * 获取专案的随访记录列表
     */
    List<FollowRecordResponse> getFollowRecordsByCase(Long caseId);

    /**
     * 分页查询随访记录
     */
    PageResult<FollowRecordResponse> getFollowRecordPage(FollowRecordQueryRequest request);

    /**
     * 按计划获取随访记录列表
     */
    List<FollowRecordResponse> getFollowRecordsByPlan(Long planId);

    // ==================== 随访任务管理 ====================

    /**
     * 创建随访任务
     */
    Long createFollowTask(FollowTaskCreateRequest request);

    /**
     * 执行随访任务
     */
    void executeFollowTask(Long id);

    /**
     * 完成随访任务
     */
    void completeFollowTask(Long id);

    /**
     * 取消随访任务
     */
    void cancelFollowTask(Long id);

    /**
     * 分页查询随访任务
     */
    PageResult<FollowTaskResponse> getFollowTaskPage(FollowTaskQueryRequest request);

    /**
     * 获取医生的待处理随访任务
     */
    List<FollowTaskResponse> getPendingFollowTasks(Long doctorId);

    /**
     * 获取随访任务统计
     */
    FollowTaskStatisticsResponse getFollowTaskStatistics(Long doctorId);

    // ==================== 随访计划管理 ====================

    /**
     * 创建随访计划
     */
    Long createFollowPlan(FollowPlanCreateRequest request);

    /**
     * 完成随访计划
     */
    void completeFollowPlan(Long id);

    /**
     * 取消随访计划
     */
    void cancelFollowPlan(Long id);

    /**
     * 获取专案的随访计划列表
     */
    List<FollowPlanResponse> getFollowPlansByCase(Long caseId);

    /**
     * 分页查询随访计划
     */
    PageResult<FollowPlanResponse> getFollowPlanPage(FollowPlanQueryRequest request);

    /**
     * 获取随访计划详情
     */
    FollowPlanResponse getFollowPlan(Long id);

    /**
     * 根据专案自动生成随访计划
     */
    Long generateFollowPlan(Long caseId);

    // ==================== 催检规则管理 ====================

    /**
     * 创建催检规则
     */
    Long createReminderRule(ReminderRuleCreateRequest request);

    /**
     * 更新催检规则状态
     */
    void updateReminderRuleStatus(Long id, Integer status);

    /**
     * 获取所有启用的催检规则
     */
    List<ReminderRuleResponse> getActiveReminderRules();

    /**
     * 根据体检月龄获取催检规则
     */
    ReminderRuleResponse getReminderRuleByMonthAge(Integer monthAge);

    // ==================== 催检记录管理 ====================

    /**
     * 分页查询催检记录
     */
    PageResult<ReminderLogResponse> getReminderLogPage(ReminderLogQueryRequest request);

    /**
     * 发送催检提醒
     */
    void sendReminder(Long childId, Long ruleId);

    /**
     * 批量发送催检提醒
     */
    void batchSendReminders();

    /**
     * 更新催检记录状态
     */
    void updateReminderStatus(Long id, Integer status);

    // ==================== 问卷管理 ====================

    /**
     * 创建问卷配置
     */
    Long createQuestionnaireConfig(QuestionnaireConfigCreateRequest request);

    /**
     * 获取所有启用的问卷配置
     */
    List<QuestionnaireConfigResponse> getActiveQuestionnaires();

    /**
     * 获取问卷配置详情
     */
    QuestionnaireConfigResponse getQuestionnaireConfig(Long id);

    /**
     * 提交问卷答案
     */
    Long submitQuestionnaireAnswer(QuestionnaireAnswerSubmitRequest request);

    /**
     * 获取儿童的问卷填写记录
     */
    List<QuestionnaireAnswerResponse> getQuestionnaireAnswersByChild(Long childId);
}
package cn.iocoder.yudao.module.childhealth.service.followup;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.module.childhealth.api.followup.FollowUpService;
import cn.iocoder.yudao.module.childhealth.api.followup.dto.FollowUpDTO.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.management.FollowPlanDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.management.FollowTaskDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.casemanagement.ExamReminderLogDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.ops.QuestionnaireAnswerDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.ops.QuestionnaireConfigDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.ops.ReminderRuleDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.workflow.FollowUpRecordDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.management.FollowPlanMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.management.FollowTaskMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.casemanagement.ExamReminderLogMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.ops.QuestionnaireAnswerMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.ops.QuestionnaireConfigMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.ops.ReminderRuleMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.workflow.FollowUpRecordMapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 随访管理Service实现
 */
@Slf4j
@Service
public class FollowUpServiceImpl implements FollowUpService {

    @Resource
    private FollowUpRecordMapper followRecordMapper;
    @Resource
    private FollowTaskMapper followTaskMapper;
    @Resource
    private FollowPlanMapper followPlanMapper;
    @Resource
    private ReminderRuleMapper reminderRuleMapper;
    @Resource
    private ExamReminderLogMapper reminderLogMapper;
    @Resource
    private QuestionnaireConfigMapper questionnaireConfigMapper;
    @Resource
    private QuestionnaireAnswerMapper questionnaireAnswerMapper;

    // ==================== 随访记录管理 ====================

    @Override
    @Transactional
    public Long createFollowRecord(FollowRecordCreateRequest request) {
        // 参数校验
        if (request.getFollowType() < 1 || request.getFollowType() > 4) {
            throw new ServiceException(400, "随访方式只能为1-4");
        }

        FollowUpRecordDO record = new FollowUpRecordDO();
        BeanUtils.copyProperties(request, record);
        record.setCreateTime(LocalDateTime.now());
        followRecordMapper.insert(record);
        return record.getId();
    }

    @Override
    @Transactional
    public void updateFollowRecord(FollowRecordUpdateRequest request) {
        FollowUpRecordDO existing = followRecordMapper.selectById(request.getId());
        if (existing == null) {
            throw new ServiceException(404, "随访记录不存在");
        }

        FollowUpRecordDO record = new FollowUpRecordDO();
        BeanUtils.copyProperties(request, record);
        followRecordMapper.updateById(record);
    }

    @Override
    @Transactional
    public void deleteFollowRecord(Long id) {
        followRecordMapper.deleteById(id);
    }

    @Override
    public FollowRecordResponse getFollowRecord(Long id) {
        FollowUpRecordDO record = followRecordMapper.selectById(id);
        return record == null ? null : convertToFollowRecordResponse(record);
    }

    @Override
    public List<FollowRecordResponse> getFollowRecordsByChild(Long childId) {
        return followRecordMapper.selectList(Wrappers.<FollowUpRecordDO>lambdaQuery()
                        .eq(FollowUpRecordDO::getChildId, childId)
                        .orderByDesc(FollowUpRecordDO::getFollowDate))
                .stream()
                .map(this::convertToFollowRecordResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<FollowRecordResponse> getFollowRecordsByCase(Long caseId) {
        return followRecordMapper.selectList(Wrappers.<FollowUpRecordDO>lambdaQuery()
                        .eq(FollowUpRecordDO::getCaseId, caseId)
                        .orderByDesc(FollowUpRecordDO::getFollowDate))
                .stream()
                .map(this::convertToFollowRecordResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PageResult<FollowRecordResponse> getFollowRecordPage(FollowRecordQueryRequest request) {
        List<FollowUpRecordDO> list = followRecordMapper.selectList(Wrappers.<FollowUpRecordDO>lambdaQuery()
                .eq(request.getCaseId() != null, FollowUpRecordDO::getCaseId, request.getCaseId())
                .eq(request.getChildId() != null, FollowUpRecordDO::getChildId, request.getChildId())
                .eq(request.getPlanId() != null, FollowUpRecordDO::getPlanId, request.getPlanId())
                .eq(request.getFollowType() != null, FollowUpRecordDO::getFollowType, request.getFollowType())
                .ge(request.getFollowDateStart() != null, FollowUpRecordDO::getFollowDate, request.getFollowDateStart())
                .le(request.getFollowDateEnd() != null, FollowUpRecordDO::getFollowDate, request.getFollowDateEnd())
                .orderByDesc(FollowUpRecordDO::getFollowDate));

        List<FollowRecordResponse> responseList = list.stream()
                .map(this::convertToFollowRecordResponse)
                .collect(Collectors.toList());

        return new PageResult<>(responseList, (long) responseList.size());
    }

    @Override
    public List<FollowRecordResponse> getFollowRecordsByPlan(Long planId) {
        return followRecordMapper.selectList(Wrappers.<FollowUpRecordDO>lambdaQuery()
                        .eq(FollowUpRecordDO::getPlanId, planId)
                        .orderByDesc(FollowUpRecordDO::getFollowDate))
                .stream()
                .map(this::convertToFollowRecordResponse)
                .collect(Collectors.toList());
    }

    // ==================== 随访任务管理 ====================

    @Override
    @Transactional
    public Long createFollowTask(FollowTaskCreateRequest request) {
        if (request.getPriority() != null && (request.getPriority() < 1 || request.getPriority() > 3)) {
            throw new ServiceException(400, "优先级只能为1-3");
        }

        FollowTaskDO task = new FollowTaskDO();
        BeanUtils.copyProperties(request, task);
        task.setTaskNo("TASK-" + IdWorker.getId());
        task.setTaskStatus(1);
        task.setCreateTime(LocalDateTime.now());
        followTaskMapper.insert(task);
        return task.getId();
    }

    @Override
    @Transactional
    public void executeFollowTask(Long id) {
        FollowTaskDO task = followTaskMapper.selectById(id);
        if (task == null) {
            throw new ServiceException(404, "随访任务不存在");
        }
        if (task.getTaskStatus() != 1) {
            throw new ServiceException(400, "任务状态不允许执行");
        }
        task.setTaskStatus(2);
        followTaskMapper.updateById(task);
    }

    @Override
    @Transactional
    public void completeFollowTask(Long id) {
        FollowTaskDO task = followTaskMapper.selectById(id);
        if (task == null) {
            throw new ServiceException(404, "随访任务不存在");
        }
        task.setTaskStatus(3);
        task.setCompleteTime(LocalDateTime.now());
        followTaskMapper.updateById(task);
    }

    @Override
    @Transactional
    public void cancelFollowTask(Long id) {
        FollowTaskDO task = followTaskMapper.selectById(id);
        if (task == null) {
            throw new ServiceException(404, "随访任务不存在");
        }
        task.setTaskStatus(4);
        followTaskMapper.updateById(task);
    }

    @Override
    public PageResult<FollowTaskResponse> getFollowTaskPage(FollowTaskQueryRequest request) {
        // 简化实现,实际需要分页插件支持
        List<FollowTaskDO> list = followTaskMapper.selectList(Wrappers.<FollowTaskDO>lambdaQuery()
                .eq(request.getChildId() != null, FollowTaskDO::getChildId, request.getChildId())
                .eq(request.getDoctorId() != null, FollowTaskDO::getResponsibleDoctor, request.getDoctorId())
                .eq(request.getTaskType() != null, FollowTaskDO::getTaskType, request.getTaskType())
                .eq(request.getTaskStatus() != null, FollowTaskDO::getTaskStatus, request.getTaskStatus())
                .eq(request.getPriority() != null, FollowTaskDO::getPriority, request.getPriority())
                .orderByAsc(FollowTaskDO::getPlanDate)
                .orderByAsc(FollowTaskDO::getPriority));

        List<FollowTaskResponse> responseList = list.stream()
                .map(this::convertToFollowTaskResponse)
                .collect(Collectors.toList());

        return new PageResult<>(responseList, (long) responseList.size());
    }

    @Override
    public List<FollowTaskResponse> getPendingFollowTasks(Long doctorId) {
        return followTaskMapper.selectList(Wrappers.<FollowTaskDO>lambdaQuery()
                        .eq(doctorId != null, FollowTaskDO::getResponsibleDoctor, doctorId)
                        .eq(FollowTaskDO::getTaskStatus, 1)
                        .orderByAsc(FollowTaskDO::getPlanDate))
                .stream()
                .map(this::convertToFollowTaskResponse)
                .collect(Collectors.toList());
    }

    @Override
    public FollowTaskStatisticsResponse getFollowTaskStatistics(Long doctorId) {
        List<FollowTaskDO> tasks = followTaskMapper.selectList(Wrappers.<FollowTaskDO>lambdaQuery()
                .eq(doctorId != null, FollowTaskDO::getResponsibleDoctor, doctorId));

        FollowTaskStatisticsResponse stats = new FollowTaskStatisticsResponse();
        stats.setTotalTasks(tasks.size());
        stats.setPendingTasks((int) tasks.stream().filter(t -> t.getTaskStatus() == 1).count());
        stats.setInProgressTasks((int) tasks.stream().filter(t -> t.getTaskStatus() == 2).count());
        stats.setCompletedTasks((int) tasks.stream().filter(t -> t.getTaskStatus() == 3).count());
        stats.setCancelledTasks((int) tasks.stream().filter(t -> t.getTaskStatus() == 4).count());
        stats.setHighPriorityCount((int) tasks.stream().filter(t -> t.getPriority() == 1).count());
        stats.setMediumPriorityCount((int) tasks.stream().filter(t -> t.getPriority() == 2).count());
        stats.setLowPriorityCount((int) tasks.stream().filter(t -> t.getPriority() == 3).count());

        return stats;
    }

    // ==================== 随访计划管理 ====================

    @Override
    @Transactional
    public Long createFollowPlan(FollowPlanCreateRequest request) {
        FollowPlanDO plan = new FollowPlanDO();
        BeanUtils.copyProperties(request, plan);
        plan.setPlanType(request.getPlanType() == null ? 2 : request.getPlanType());
        plan.setPlanStatus(1);
        plan.setCreateTime(LocalDateTime.now());
        followPlanMapper.insert(plan);
        return plan.getId();
    }

    @Override
    @Transactional
    public void completeFollowPlan(Long id) {
        FollowPlanDO plan = followPlanMapper.selectById(id);
        if (plan == null) {
            throw new ServiceException(404, "随访计划不存在");
        }
        plan.setPlanStatus(2);
        plan.setExecuteDate(LocalDateTime.now().toLocalDate());
        followPlanMapper.updateById(plan);
    }

    @Override
    @Transactional
    public void cancelFollowPlan(Long id) {
        FollowPlanDO plan = followPlanMapper.selectById(id);
        if (plan == null) {
            throw new ServiceException(404, "随访计划不存在");
        }
        plan.setPlanStatus(3);
        followPlanMapper.updateById(plan);
    }

    @Override
    public List<FollowPlanResponse> getFollowPlansByCase(Long caseId) {
        return followPlanMapper.selectList(Wrappers.<FollowPlanDO>lambdaQuery()
                        .eq(FollowPlanDO::getCaseId, caseId)
                        .orderByAsc(FollowPlanDO::getPlanDate))
                .stream()
                .map(this::convertToFollowPlanResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PageResult<FollowPlanResponse> getFollowPlanPage(FollowPlanQueryRequest request) {
        List<FollowPlanDO> list = followPlanMapper.selectList(Wrappers.<FollowPlanDO>lambdaQuery()
                .eq(request.getCaseId() != null, FollowPlanDO::getCaseId, request.getCaseId())
                .eq(request.getPlanType() != null, FollowPlanDO::getPlanType, request.getPlanType())
                .eq(request.getPlanStatus() != null, FollowPlanDO::getPlanStatus, request.getPlanStatus())
                .ge(request.getPlanDateStart() != null, FollowPlanDO::getPlanDate, request.getPlanDateStart())
                .le(request.getPlanDateEnd() != null, FollowPlanDO::getPlanDate, request.getPlanDateEnd())
                .orderByDesc(FollowPlanDO::getPlanDate));

        List<FollowPlanResponse> responseList = list.stream()
                .map(this::convertToFollowPlanResponse)
                .collect(Collectors.toList());

        return new PageResult<>(responseList, (long) responseList.size());
    }

    @Override
    public FollowPlanResponse getFollowPlan(Long id) {
        FollowPlanDO plan = followPlanMapper.selectById(id);
        return plan == null ? null : convertToFollowPlanResponse(plan);
    }

    @Override
    @Transactional
    public Long generateFollowPlan(Long caseId) {
        FollowPlanDO plan = new FollowPlanDO();
        plan.setCaseId(caseId);
        plan.setPlanType(1); // 自动生成
        plan.setPlanDate(LocalDate.now());
        plan.setFollowContent("自动生成随访计划");
        plan.setPlanStatus(1);
        plan.setCreateTime(LocalDateTime.now());
        followPlanMapper.insert(plan);
        return plan.getId();
    }

    // ==================== 催检规则管理 ====================

    @Override
    @Transactional
    public Long createReminderRule(ReminderRuleCreateRequest request) {
        ReminderRuleDO rule = new ReminderRuleDO();
        BeanUtils.copyProperties(request, rule);
        rule.setStatus(1);
        rule.setCreateTime(LocalDateTime.now());
        reminderRuleMapper.insert(rule);
        return rule.getId();
    }

    @Override
    @Transactional
    public void updateReminderRuleStatus(Long id, Integer status) {
        ReminderRuleDO rule = reminderRuleMapper.selectById(id);
        if (rule == null) {
            throw new ServiceException(404, "催检规则不存在");
        }
        rule.setStatus(status);
        reminderRuleMapper.updateById(rule);
    }

    @Override
    public List<ReminderRuleResponse> getActiveReminderRules() {
        return reminderRuleMapper.selectList(Wrappers.<ReminderRuleDO>lambdaQuery()
                        .eq(ReminderRuleDO::getStatus, 1))
                .stream()
                .map(this::convertToReminderRuleResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ReminderRuleResponse getReminderRuleByMonthAge(Integer monthAge) {
        List<ReminderRuleDO> rules = reminderRuleMapper.selectList(Wrappers.<ReminderRuleDO>lambdaQuery()
                .eq(ReminderRuleDO::getStatus, 1));

        for (ReminderRuleDO rule : rules) {
            try {
                List<Integer> ages = JsonUtils.parseArray(rule.getTargetAge(), Integer.class);
                if (ages != null && ages.contains(monthAge)) {
                    return convertToReminderRuleResponse(rule);
                }
            } catch (Exception e) {
                log.error("解析目标年龄失败: {}", rule.getTargetAge(), e);
            }
        }
        return null;
    }

    // ==================== 催检记录管理 ====================

    @Override
    public PageResult<ReminderLogResponse> getReminderLogPage(ReminderLogQueryRequest request) {
        List<ExamReminderLogDO> list = reminderLogMapper.selectList(Wrappers.<ExamReminderLogDO>lambdaQuery()
                .eq(request.getChildId() != null, ExamReminderLogDO::getChildId, request.getChildId())
                .eq(request.getRuleId() != null, ExamReminderLogDO::getRuleId, request.getRuleId())
                .eq(request.getExamType() != null, ExamReminderLogDO::getExamType, request.getExamType())
                .eq(request.getRemindStatus() != null, ExamReminderLogDO::getPushStatus, request.getRemindStatus())
                .orderByDesc(ExamReminderLogDO::getCreateTime));

        List<ReminderLogResponse> responseList = list.stream()
                .map(this::convertToReminderLogResponse)
                .collect(Collectors.toList());

        return new PageResult<>(responseList, (long) responseList.size());
    }

    @Override
    @Transactional
    public void sendReminder(Long childId, Long ruleId) {
        // 简化实现,实际需要调用消息推送服务
        ReminderRuleDO rule = reminderRuleMapper.selectById(ruleId);
        if (rule == null) {
            throw new ServiceException(404, "催检规则不存在");
        }

        ExamReminderLogDO log = new ExamReminderLogDO();
        log.setChildId(childId);
        log.setRuleId(ruleId);
        log.setExamType(rule.getExamType());
        log.setPushStatus(0); // 待发送
        reminderLogMapper.insert(log);
    }

    @Override
    @Transactional
    public void batchSendReminders() {
        // 批量发送催检提醒的定时任务实现
        // 简化实现
        log.info("批量发送催检提醒定时任务执行");
    }

    @Override
    @Transactional
    public void updateReminderStatus(Long id, Integer status) {
        ExamReminderLogDO log = reminderLogMapper.selectById(id);
        if (log == null) {
            throw new ServiceException(404, "催检记录不存在");
        }
        log.setPushStatus(status);
        reminderLogMapper.updateById(log);
    }

    // ==================== 问卷管理 ====================

    @Override
    @Transactional
    public Long createQuestionnaireConfig(QuestionnaireConfigCreateRequest request) {
        QuestionnaireConfigDO config = new QuestionnaireConfigDO();
        BeanUtils.copyProperties(request, config);
        config.setStatus(1);
        config.setCreateTime(LocalDateTime.now());
        questionnaireConfigMapper.insert(config);
        return config.getId();
    }

    @Override
    public List<QuestionnaireConfigResponse> getActiveQuestionnaires() {
        return questionnaireConfigMapper.selectList(Wrappers.<QuestionnaireConfigDO>lambdaQuery()
                        .eq(QuestionnaireConfigDO::getStatus, 1))
                .stream()
                .map(this::convertToQuestionnaireConfigResponse)
                .collect(Collectors.toList());
    }

    @Override
    public QuestionnaireConfigResponse getQuestionnaireConfig(Long id) {
        QuestionnaireConfigDO config = questionnaireConfigMapper.selectById(id);
        return config == null ? null : convertToQuestionnaireConfigResponse(config);
    }

    @Override
    @Transactional
    public Long submitQuestionnaireAnswer(QuestionnaireAnswerSubmitRequest request) {
        QuestionnaireAnswerDO answer = new QuestionnaireAnswerDO();
        BeanUtils.copyProperties(request, answer);
        answer.setFillTime(LocalDateTime.now());
        answer.setCreateTime(LocalDateTime.now());
        questionnaireAnswerMapper.insert(answer);
        return answer.getId();
    }

    @Override
    public List<QuestionnaireAnswerResponse> getQuestionnaireAnswersByChild(Long childId) {
        return questionnaireAnswerMapper.selectList(Wrappers.<QuestionnaireAnswerDO>lambdaQuery()
                        .eq(QuestionnaireAnswerDO::getChildId, childId)
                        .orderByDesc(QuestionnaireAnswerDO::getFillTime))
                .stream()
                .map(this::convertToQuestionnaireAnswerResponse)
                .collect(Collectors.toList());
    }

    // ==================== 辅助方法 ====================

    private FollowRecordResponse convertToFollowRecordResponse(FollowUpRecordDO record) {
        FollowRecordResponse response = new FollowRecordResponse();
        BeanUtils.copyProperties(record, response);
        return response;
    }

    private FollowTaskResponse convertToFollowTaskResponse(FollowTaskDO task) {
        FollowTaskResponse response = new FollowTaskResponse();
        BeanUtils.copyProperties(task, response);
        return response;
    }

    private FollowPlanResponse convertToFollowPlanResponse(FollowPlanDO plan) {
        FollowPlanResponse response = new FollowPlanResponse();
        BeanUtils.copyProperties(plan, response);
        return response;
    }

    private ReminderRuleResponse convertToReminderRuleResponse(ReminderRuleDO rule) {
        ReminderRuleResponse response = new ReminderRuleResponse();
        BeanUtils.copyProperties(rule, response);
        return response;
    }

    private ReminderLogResponse convertToReminderLogResponse(ExamReminderLogDO log) {
        ReminderLogResponse response = new ReminderLogResponse();
        BeanUtils.copyProperties(log, response);
        return response;
    }

    private QuestionnaireConfigResponse convertToQuestionnaireConfigResponse(QuestionnaireConfigDO config) {
        QuestionnaireConfigResponse response = new QuestionnaireConfigResponse();
        BeanUtils.copyProperties(config, response);
        return response;
    }

    private QuestionnaireAnswerResponse convertToQuestionnaireAnswerResponse(QuestionnaireAnswerDO answer) {
        QuestionnaireAnswerResponse response = new QuestionnaireAnswerResponse();
        BeanUtils.copyProperties(answer, response);
        return response;
    }
}
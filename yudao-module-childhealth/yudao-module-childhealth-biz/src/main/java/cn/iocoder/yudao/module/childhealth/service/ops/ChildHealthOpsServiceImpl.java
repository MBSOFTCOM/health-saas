package cn.iocoder.yudao.module.childhealth.service.ops;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.module.childhealth.api.ops.dto.ChildHealthOpsDTO.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.management.FollowTaskDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.ops.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.casemanagement.ExamReminderLogDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig.HealthArticleDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.workflow.ChildInfoDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.workflow.GuardianInfoDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.management.FollowTaskMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.ops.*;
import cn.iocoder.yudao.module.childhealth.dal.mysql.casemanagement.ExamReminderLogMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.screeningconfig.HealthArticleMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.workflow.ChildInfoMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.workflow.GuardianInfoMapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChildHealthOpsServiceImpl implements ChildHealthOpsService {
    @Resource private MessagePushLogMapper messagePushLogMapper;
    @Resource private ReminderRuleMapper reminderRuleMapper;
    @Resource private QuestionnaireConfigMapper questionnaireConfigMapper;
    @Resource private QuestionnaireAnswerMapper questionnaireAnswerMapper;
    @Resource private ExamReminderLogMapper examReminderLogMapper;
    @Resource private HealthArticleMapper healthArticleMapper;
    @Resource private ChildInfoMapper childInfoMapper;
    @Resource private GuardianInfoMapper guardianInfoMapper;
    @Resource private FollowTaskMapper followTaskMapper;

    @Override
    @Transactional
    public Long queueMessage(MessagePushRequest request) {
        if (request.getPushType() < 1 || request.getPushType() > 4
                || request.getPushChannel() < 1 || request.getPushChannel() > 3) throw error("推送类型或渠道无效");
        if (request.getTargetType() != null && (request.getTargetType() < 1 || request.getTargetType() > 3)) {
            throw error("推送目标类型无效");
        }
        if (Integer.valueOf(1).equals(request.getPushChannel())
                && (request.getMobile() == null || request.getMobile().isBlank())) throw error("短信推送缺少手机号");
        if (Integer.valueOf(2).equals(request.getPushChannel())
                && (request.getOpenid() == null || request.getOpenid().isBlank())) throw error("微信推送缺少OpenID");
        MessagePushLogDO item = new MessagePushLogDO(); BeanUtils.copyProperties(request, item);
        item.setPushStatus(3); item.setIsRead(false); item.setCreateTime(LocalDateTime.now());
        messagePushLogMapper.insert(item); return item.getId();
    }

    @Override
    public List<MessagePushResponse> listMessages(Long targetId, Integer status) {
        return messagePushLogMapper.selectList(Wrappers.<MessagePushLogDO>lambdaQuery()
                .eq(targetId != null, MessagePushLogDO::getTargetId, targetId)
                .eq(status != null, MessagePushLogDO::getPushStatus, status)
                .orderByDesc(MessagePushLogDO::getCreateTime)).stream().map(item -> {
                    MessagePushResponse response = new MessagePushResponse(); BeanUtils.copyProperties(item, response); return response;
                }).toList();
    }

    @Override
    @Transactional
    public void resendMessage(Long id) {
        if (messagePushLogMapper.update(null, Wrappers.<MessagePushLogDO>lambdaUpdate().eq(MessagePushLogDO::getId, id)
                .in(MessagePushLogDO::getPushStatus, 2, 4).set(MessagePushLogDO::getPushStatus, 3)
                .set(MessagePushLogDO::getPushTime, null).set(MessagePushLogDO::getErrorMsg, null)
                .set(MessagePushLogDO::getIsRead, false)) == 0) throw error("消息不存在或当前状态不可补发");
    }

    @Override
    @Transactional
    public void cancelMessage(Long id) {
        if (messagePushLogMapper.update(null, Wrappers.<MessagePushLogDO>lambdaUpdate().eq(MessagePushLogDO::getId, id)
                .eq(MessagePushLogDO::getPushStatus, 3).set(MessagePushLogDO::getPushStatus, 4)) == 0) {
            throw error("消息不存在或已发送");
        }
    }

    @Override
    @Transactional
    public void markMessageRead(Long id) {
        if (messagePushLogMapper.update(null, Wrappers.<MessagePushLogDO>lambdaUpdate().eq(MessagePushLogDO::getId, id)
                .ne(MessagePushLogDO::getPushStatus, 4).set(MessagePushLogDO::getIsRead, true)) == 0) {
            throw error("消息不存在或已取消");
        }
    }

    @Override
    @Transactional
    public Long createReminder(ReminderRequest request) {
        unique(reminderRuleMapper.selectCount(Wrappers.<ReminderRuleDO>lambdaQuery()
                .eq(ReminderRuleDO::getRuleCode, request.getRuleCode())), "催检规则编码已存在");
        json(request.getTargetAge(), "目标月龄");
        if (targetMonths(request.getTargetAge()).isEmpty()) throw error("目标月龄必须包含0至72的月龄");
        if (request.getExamType() != null && (request.getExamType() < 1 || request.getExamType() > 3)
                || request.getRemindAdvanceDays() != null && request.getRemindAdvanceDays() < 0) {
            throw error("体检类型或提前提醒天数无效");
        }
        ReminderRuleDO item = new ReminderRuleDO(); BeanUtils.copyProperties(request, item);
        item.setStatus(1); item.setCreateTime(LocalDateTime.now()); reminderRuleMapper.insert(item); return item.getId();
    }

    @Override
    @Transactional
    public int generateReminders(ReminderGenerateRequest request) {
        LocalDate asOf = request.getAsOfDate() == null ? LocalDate.now() : request.getAsOfDate();
        int generated = 0;
        List<ChildInfoDO> children = childInfoMapper.selectList(Wrappers.<ChildInfoDO>lambdaQuery()
                .eq(ChildInfoDO::getStatus, "1"));
        for (ReminderRuleDO rule : reminderRuleMapper.selectList(Wrappers.<ReminderRuleDO>lambdaQuery()
                .eq(ReminderRuleDO::getStatus, 1))) {
            List<Integer> months = targetMonths(rule.getTargetAge());
            if (months.isEmpty()) throw error("催检规则目标月龄无效：" + rule.getRuleCode());
            for (ChildInfoDO child : children) {
                for (Integer month : months) {
                    LocalDate dueDate = child.getBirthDate().plusMonths(month);
                    LocalDate remindDate = dueDate.minusDays(rule.getRemindAdvanceDays() == null ? 0 : rule.getRemindAdvanceDays());
                    if (!shouldGenerate(asOf, remindDate, dueDate) || reminderExists(child.getId(), rule.getId(), dueDate)) continue;
                    createReminderLogAndTask(child, rule, month, dueDate, remindDate);
                    generated++;
                }
            }
        }
        return generated;
    }

    @Override
    @Transactional
    public Long createQuestionnaire(QuestionnaireRequest request) {
        unique(questionnaireConfigMapper.selectCount(Wrappers.<QuestionnaireConfigDO>lambdaQuery()
                .eq(QuestionnaireConfigDO::getQuestionnaireCode, request.getQuestionnaireCode())), "问卷编码已存在");
        json(request.getQuestions(), "问卷题目"); json(request.getScoringRule(), "计分规则");
        QuestionnaireConfigDO item = new QuestionnaireConfigDO(); BeanUtils.copyProperties(request, item);
        item.setStatus(1); item.setCreateTime(LocalDateTime.now()); questionnaireConfigMapper.insert(item); return item.getId();
    }

    @Override
    @Transactional
    public Long submitQuestionnaireAnswer(QuestionnaireAnswerRequest request) {
        QuestionnaireConfigDO questionnaire = questionnaireConfigMapper.selectById(request.getQuestionnaireId());
        if (questionnaire == null || !Integer.valueOf(1).equals(questionnaire.getStatus())) throw error("问卷不存在或已停用");
        if (childInfoMapper.selectById(request.getChildId()) == null) throw error("儿童档案不存在");
        if (request.getFillType() < 1 || request.getFillType() > 2) throw error("填写人类型只能为1医生或2家长");
        json(request.getAnswers(), "问卷答案");
        QuestionnaireAnswerDO answer = new QuestionnaireAnswerDO(); BeanUtils.copyProperties(request, answer);
        answer.setFillTime(LocalDateTime.now()); answer.setCreateTime(LocalDateTime.now()); questionnaireAnswerMapper.insert(answer);
        return answer.getId();
    }

    @Override
    public List<QuestionnaireAnswerResponse> listQuestionnaireAnswers(Long childId, Long questionnaireId) {
        return questionnaireAnswerMapper.selectList(Wrappers.<QuestionnaireAnswerDO>lambdaQuery()
                .eq(childId != null, QuestionnaireAnswerDO::getChildId, childId)
                .eq(questionnaireId != null, QuestionnaireAnswerDO::getQuestionnaireId, questionnaireId)
                .orderByDesc(QuestionnaireAnswerDO::getFillTime)).stream().map(answer -> {
                    QuestionnaireAnswerResponse response = new QuestionnaireAnswerResponse();
                    BeanUtils.copyProperties(answer, response); return response;
                }).toList();
    }

    @Override
    @Transactional
    public Long createArticle(ArticleRequest request) {
        unique(healthArticleMapper.selectCount(Wrappers.<HealthArticleDO>lambdaQuery()
                .eq(HealthArticleDO::getArticleCode, request.getArticleCode())), "文章编码已存在");
        json(request.getTags(), "文章标签");
        HealthArticleDO item = new HealthArticleDO(); BeanUtils.copyProperties(request, item);
        item.setViewCount(0); item.setIsTop(request.getIsTop() == null ? 0 : request.getIsTop());
        item.setPublishStatus(request.getPublishStatus() == null ? 0 : request.getPublishStatus());
        if (item.getPublishStatus() == 2) item.setPublishTime(LocalDateTime.now());
        healthArticleMapper.insert(item); return item.getId();
    }

    @Override
    public List<ArticleResponse> listArticles(Integer status) {
        return healthArticleMapper.selectList(Wrappers.<HealthArticleDO>lambdaQuery()
                .eq(status != null, HealthArticleDO::getPublishStatus, status)
                .orderByDesc(HealthArticleDO::getIsTop).orderByDesc(HealthArticleDO::getPublishTime)).stream().map(item -> {
                    ArticleResponse response = new ArticleResponse(); BeanUtils.copyProperties(item, response); return response;
                }).toList();
    }

    private void createReminderLogAndTask(ChildInfoDO child, ReminderRuleDO rule, Integer month,
            LocalDate dueDate, LocalDate remindDate) {
        ExamReminderLogDO log = new ExamReminderLogDO(); log.setChildId(child.getId()); log.setRuleId(rule.getId());
        log.setExamType(rule.getExamType()); log.setDueDate(dueDate); log.setRemindDate(remindDate);
        log.setPushStatus(0); log.setIsCompleted(false); examReminderLogMapper.insert(log);
        String content = renderReminder(rule.getMessageTemplate(), child, month, dueDate);
        FollowTaskDO task = new FollowTaskDO(); task.setTaskNo("FOLLOW-" + IdWorker.getId()); task.setChildId(child.getId());
        task.setTaskType(1); task.setTaskSource("EXAM_REMINDER:" + log.getId()); task.setTaskContent(content);
        task.setPriority(2); task.setPlanDate(dueDate); task.setTaskStatus(1); task.setCreateTime(LocalDateTime.now());
        followTaskMapper.insert(task);
        GuardianInfoDO guardian = guardianInfoMapper.selectOne(Wrappers.<GuardianInfoDO>lambdaQuery()
                .eq(GuardianInfoDO::getChildId, child.getId()).eq(GuardianInfoDO::getIsPrimary, true).last("LIMIT 1"));
        if (guardian == null) return;
        MessagePushRequest message = new MessagePushRequest(); message.setPushType(1); message.setTargetType(2);
        message.setTargetId(guardian.getId()); message.setPushContent(content);
        if (guardian.getWechatOpenid() != null && !guardian.getWechatOpenid().isBlank()) {
            message.setPushChannel(2); message.setOpenid(guardian.getWechatOpenid()); queueMessage(message);
        } else if (guardian.getGuardianPhone() != null && !guardian.getGuardianPhone().isBlank()) {
            message.setPushChannel(1); message.setMobile(guardian.getGuardianPhone()); queueMessage(message);
        }
    }

    private boolean reminderExists(Long childId, Long ruleId, LocalDate dueDate) {
        return examReminderLogMapper.selectCount(Wrappers.<ExamReminderLogDO>lambdaQuery()
                .eq(ExamReminderLogDO::getChildId, childId).eq(ExamReminderLogDO::getRuleId, ruleId)
                .eq(ExamReminderLogDO::getDueDate, dueDate)) > 0;
    }

    static List<Integer> targetMonths(String json) {
        if (json == null || !JsonUtils.isJson(json)) return List.of();
        JsonNode root = JsonUtils.parseTree(json);
        JsonNode months = root.isArray() ? root : root.path("months");
        if (!months.isArray()) return List.of();
        List<Integer> result = new ArrayList<>();
        for (JsonNode value : months) {
            if (value.canConvertToInt() && value.asInt() >= 0 && value.asInt() <= 72 && !result.contains(value.asInt())) {
                result.add(value.asInt());
            }
        }
        return result;
    }

    static boolean shouldGenerate(LocalDate asOf, LocalDate remindDate, LocalDate dueDate) {
        return !asOf.isBefore(remindDate) && !asOf.isAfter(dueDate);
    }

    private String renderReminder(String template, ChildInfoDO child, Integer month, LocalDate dueDate) {
        String content = template == null || template.isBlank() ? "儿童健康检查提醒" : template;
        return content.replace("{childName}", child.getChildName()).replace("{monthAge}", String.valueOf(month))
                .replace("{dueDate}", dueDate.toString());
    }

    private void unique(Long count, String message) { if (count > 0) throw error(message); }
    private void json(String value, String name) {
        if (value != null && !value.isBlank() && !JsonUtils.isJson(value)) throw error(name + "必须是合法JSON");
    }
    private ServiceException error(String message) { return new ServiceException(1_010_003_001, message); }
}

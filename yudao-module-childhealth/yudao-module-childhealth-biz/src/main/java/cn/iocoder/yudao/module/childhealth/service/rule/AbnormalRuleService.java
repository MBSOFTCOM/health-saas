package cn.iocoder.yudao.module.childhealth.service.rule;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.module.childhealth.api.rule.dto.AbnormalRuleDTO.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.exam.ExamRecordDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.rule.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.workflow.CaseAlertLogDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.exam.ExamRecordMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.rule.*;
import cn.iocoder.yudao.module.childhealth.dal.mysql.workflow.CaseAlertLogMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.workflow.HealthCheckupMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AbnormalRuleService {
    @Resource
    private AbnormalRuleMapper abnormalRuleMapper;
    @Resource
    private AbnormalDetectLogMapper abnormalDetectLogMapper;
    @Resource
    private HealthCheckupMapper healthCheckupMapper;
    @Resource
    private CaseAlertLogMapper caseAlertLogMapper;
    @Resource
    private ExamRecordMapper examRecordMapper;

    public RuleResponse get(Long id) {
        AbnormalRuleDO rule = abnormalRuleMapper.selectById(id);
        if (rule == null)
            throw error("异常规则不存在");
        return response(rule);
    }

    @Transactional
    public Long create(RuleSaveRequest request) {
        validate(request, null);
        AbnormalRuleDO rule = new AbnormalRuleDO();
        BeanUtils.copyProperties(request, rule);
        rule.setAutoCreateCase(Boolean.TRUE.equals(request.getAutoCreateCase()));
        rule.setStatus(request.getStatus() == null ? 1 : request.getStatus());
        rule.setCreateTime(LocalDateTime.now());
        abnormalRuleMapper.insert(rule);
        return rule.getId();
    }

    @Transactional
    public void update(Long id, RuleSaveRequest request) {
        if (abnormalRuleMapper.selectById(id) == null)
            throw error("异常规则不存在");
        validate(request, id);
        AbnormalRuleDO rule = new AbnormalRuleDO();
        BeanUtils.copyProperties(request, rule);
        rule.setId(id);
        rule.setAutoCreateCase(Boolean.TRUE.equals(request.getAutoCreateCase()));
        rule.setStatus(request.getStatus() == null ? 1 : request.getStatus());
        abnormalRuleMapper.updateById(rule);
    }

    @Transactional
    public List<RuleResponse> match(MatchRequest request) {
        if (request.getExamId() != null && healthCheckupMapper.selectById(request.getExamId()) == null)
            throw error("体检记录不存在");
        List<RuleResponse> matched = new ArrayList<>();
        for (AbnormalRuleDO rule : abnormalRuleMapper
                .selectList(Wrappers.<AbnormalRuleDO>lambdaQuery().eq(AbnormalRuleDO::getStatus, 1))) {
            boolean hit = evaluate(rule.getConditionLogic(), request.getValues());
            if (hit) {
                matched.add(response(rule));
                executeActions(request.getExamId(), rule, request.getValues().get(rule.getCheckItem()));
            }
            if (request.getExamId() != null)
                saveLog(request.getExamId(), rule, hit, request.getValues().get(rule.getCheckItem()));
        }
        return matched;
    }

    private void executeActions(Long examId, AbnormalRuleDO rule, BigDecimal triggerValue) {
        if (examId == null) return;
        String triggerValueStr = triggerValue == null ? null : triggerValue.toPlainString();
        String alertLevel = rule.getAbnormalLevel() >= 2 ? "DANGER" : "WARNING";
        
        createAlert(examId, rule, alertLevel, triggerValueStr);
        
        if (Boolean.TRUE.equals(rule.getAutoCreateCase())) {
            updateExamStatus(examId, true);
        }
    }

    private void createAlert(Long examId, AbnormalRuleDO rule, String alertLevel, String triggerValue) {
        CaseAlertLogDO existing = caseAlertLogMapper.selectOne(Wrappers.<CaseAlertLogDO>lambdaQuery()
                .eq(CaseAlertLogDO::getExamId, examId)
                .eq(CaseAlertLogDO::getTriggerItem, rule.getCheckItem())
                .eq(CaseAlertLogDO::getIsHandled, false).last("LIMIT 1"));
        if (existing != null) {
            existing.setTriggerValue(triggerValue);
            existing.setAlertLevel(alertLevel);
            existing.setThresholdRule(rule.getConditionLogic());
            caseAlertLogMapper.updateById(existing);
            return;
        }
        
        CaseAlertLogDO alert = new CaseAlertLogDO();
        alert.setExamId(examId);
        alert.setAlertType("RULE_TRIGGER");
        alert.setAlertLevel(alertLevel);
        alert.setTriggerItem(rule.getCheckItem());
        alert.setTriggerValue(triggerValue);
        alert.setThresholdRule(rule.getConditionLogic());
        alert.setAlertContent("异常规则触发：" + rule.getRuleName() + "，检测值：" + triggerValue);
        alert.setSuggestCaseType(rule.getCaseType());
        alert.setIsHandled(false);
        alert.setCreateTime(LocalDateTime.now());
        caseAlertLogMapper.insert(alert);
    }

    private void updateExamStatus(Long examId, boolean hasAbnormal) {
        ExamRecordDO examRecord = examRecordMapper.selectById(examId);
        if (examRecord != null) {
            examRecord.setHasAbnormal(hasAbnormal);
            examRecord.setUpdateTime(LocalDateTime.now());
            examRecordMapper.updateById(examRecord);
        }
    }

    static boolean evaluate(String logicJson, Map<String, BigDecimal> values) {
        JsonNode root = JsonUtils.parseTree(logicJson);
        JsonNode conditions = root.isArray() ? root : root.path("conditions");
        if (!conditions.isArray() || conditions.isEmpty())
            return false;
        boolean result = false;
        String connector = "AND";
        int index = 0;
        for (JsonNode condition : conditions) {
            BigDecimal actual = values.get(condition.path("field").asText());
            boolean current = compare(actual, condition.path("operator").asText(), condition.path("value"));
            result = index++ == 0 ? current : "OR".equalsIgnoreCase(connector) ? result || current : result && current;
            connector = condition.path("logic").asText("AND");
        }
        return result;
    }

    private static boolean compare(BigDecimal actual, String operator, JsonNode expected) {
        if (actual == null || expected.isMissingNode() || !expected.isNumber())
            return false;
        int value = actual.compareTo(expected.decimalValue());
        return switch (operator) {
            case "<" -> value < 0;
            case "<=" -> value <= 0;
            case ">" -> value > 0;
            case ">=" -> value >= 0;
            case "=", "==" -> value == 0;
            case "!=" -> value != 0;
            default -> false;
        };
    }

    private void saveLog(Long examId, AbnormalRuleDO rule, boolean hit, BigDecimal value) {
        AbnormalDetectLogDO log = abnormalDetectLogMapper
                .selectOne(Wrappers.<AbnormalDetectLogDO>lambdaQuery().eq(AbnormalDetectLogDO::getExamId, examId)
                        .eq(AbnormalDetectLogDO::getRuleId, rule.getId()).last("LIMIT 1"));
        if (log == null) {
            log = new AbnormalDetectLogDO();
            log.setExamId(examId);
            log.setRuleId(rule.getId());
            log.setIsReminded(false);
            log.setIsHandled(false);
            log.setCreateTime(LocalDateTime.now());
        }
        log.setDetectResult(hit ? 1 : 0);
        log.setAbnormalValue(value == null ? null : value.toPlainString());
        if (log.getId() == null)
            abnormalDetectLogMapper.insert(log);
        else
            abnormalDetectLogMapper.updateById(log);
    }

    private void validate(RuleSaveRequest request, Long id) {
        if (!JsonUtils.isJson(request.getConditionLogic()))
            throw error("判定条件必须是合法JSON");
        if (request.getAbnormalLevel() < 1 || request.getAbnormalLevel() > 3)
            throw error("异常等级只能为1、2、3");
        if (abnormalRuleMapper.selectCount(Wrappers.<AbnormalRuleDO>lambdaQuery()
                .eq(AbnormalRuleDO::getRuleCode, request.getRuleCode()).ne(id != null, AbnormalRuleDO::getId, id)) > 0)
            throw error("规则编码已存在");
    }

    private RuleResponse response(AbnormalRuleDO rule) {
        RuleResponse response = new RuleResponse();
        BeanUtils.copyProperties(rule, response);
        return response;
    }

    private ServiceException error(String message) {
        return new ServiceException(1_010_005_001, message);
    }
}

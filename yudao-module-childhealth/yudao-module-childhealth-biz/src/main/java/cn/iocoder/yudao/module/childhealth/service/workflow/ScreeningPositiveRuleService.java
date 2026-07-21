package cn.iocoder.yudao.module.childhealth.service.workflow;

import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.DiseaseKnowledgeDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.PositiveRuleDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.screening.DiseaseKnowledgeMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.screening.PositiveRuleMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ScreeningPositiveRuleService {
    @Resource private PositiveRuleMapper positiveRuleMapper;
    @Resource private DiseaseKnowledgeMapper diseaseKnowledgeMapper;

    public List<Match> match(Map<String, String> values) {
        List<Match> matches = new ArrayList<>();
        for (PositiveRuleDO rule : positiveRuleMapper.selectList(Wrappers.<PositiveRuleDO>lambdaQuery()
                .eq(PositiveRuleDO::getStatus, 1))) {
            if (rule.getConditionLogic() == null || !JsonUtils.isJson(rule.getConditionLogic())) {
                throw new ServiceException(1_010_000_002, "阳性规则配置无效：" + rule.getRuleCode());
            }
            if (!evaluate(rule.getConditionLogic(), values)) continue;
            DiseaseKnowledgeDO knowledge = rule.getDiseaseCode() == null ? null
                    : diseaseKnowledgeMapper.selectOne(Wrappers.<DiseaseKnowledgeDO>lambdaQuery()
                    .eq(DiseaseKnowledgeDO::getDiseaseCode, rule.getDiseaseCode())
                    .eq(DiseaseKnowledgeDO::getStatus, 1).last("LIMIT 1"));
            matches.add(new Match(rule, knowledge));
        }
        return matches;
    }

    static boolean evaluate(String logicJson, Map<String, String> values) {
        if (logicJson == null || !JsonUtils.isJson(logicJson)) return false;
        JsonNode root = JsonUtils.parseTree(logicJson);
        JsonNode conditions = root.isArray() ? root : root.path("conditions");
        if (!conditions.isArray() || conditions.isEmpty()) return false;
        boolean result = false;
        String connector = "AND";
        int index = 0;
        for (JsonNode condition : conditions) {
            String field = condition.path("field").asText(condition.path("itemCode").asText());
            boolean current = compare(values.get(field), condition.path("operator").asText(), condition.get("value"));
            result = index++ == 0 ? current : "OR".equalsIgnoreCase(connector) ? result || current : result && current;
            connector = condition.path("logic").asText("AND");
        }
        return result;
    }

    private static boolean compare(String actual, String operator, JsonNode expected) {
        if (actual == null || expected == null || expected.isNull()) return false;
        String target = expected.asText();
        return switch (operator.toUpperCase()) {
            case "=", "==", "EQ" -> actual.equalsIgnoreCase(target);
            case "!=", "NE" -> !actual.equalsIgnoreCase(target);
            case "CONTAINS" -> actual.contains(target);
            case ">", ">=", "<", "<=" -> compareNumber(actual, target, operator);
            default -> false;
        };
    }

    private static boolean compareNumber(String actual, String target, String operator) {
        try {
            int compared = new BigDecimal(actual).compareTo(new BigDecimal(target));
            return switch (operator) {
                case ">" -> compared > 0;
                case ">=" -> compared >= 0;
                case "<" -> compared < 0;
                case "<=" -> compared <= 0;
                default -> false;
            };
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    public record Match(PositiveRuleDO rule, DiseaseKnowledgeDO knowledge) {}
}

package cn.iocoder.yudao.module.childhealth.service.scale;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.module.childhealth.api.scale.dto.PsychologicalScaleCreateReqDTO;
import cn.iocoder.yudao.module.childhealth.api.scale.dto.PsychologicalScaleRespDTO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.scale.PsychologicalScale;
import cn.iocoder.yudao.module.childhealth.dal.mysql.scale.PsychologicalScaleMapper;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.scale.*;
import cn.iocoder.yudao.module.childhealth.dal.mysql.scale.*;
import cn.iocoder.yudao.module.childhealth.api.scale.dto.ScaleAssessmentDTO.*;
import cn.iocoder.yudao.module.childhealth.dal.mysql.workflow.ChildInfoMapper;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.management.FollowTaskDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.management.FollowTaskMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Service
@Validated
public class PsychologicalScaleServiceImpl implements PsychologicalScaleService {

    @Resource
    private PsychologicalScaleMapper scaleMapper;
    @Resource
    private ScaleQuestionMapper questionMapper;
    @Resource
    private ScaleAssessmentRecordMapper assessmentMapper;
    @Autowired
    private AssessmentReportMapper assessmentReportMapper;
    @Resource
    private ChildInfoMapper childInfoMapper;
    @Resource
    private FollowTaskMapper followTaskMapper;

    @Override
    @Transactional
    public Long createPsychologicalScale(PsychologicalScaleCreateReqDTO request) {
        if (request.getApplicableAgeMin() < 0 || request.getApplicableAgeMax() > 72
                || request.getApplicableAgeMin() > request.getApplicableAgeMax()) {
            throw new ServiceException(1_010_002_001, "量表适用月龄范围无效");
        }
        validateJson(request.getItems(), "量表题目");
        validateJson(request.getScoringRules(), "计分规则");
        validateJson(request.getDiagnosticCriteria(), "诊断分界值");
        validateJson(request.getRiskLevels(), "风险等级");
        Long count = scaleMapper.selectCount(Wrappers.<PsychologicalScale>lambdaQuery()
                .eq(PsychologicalScale::getScaleCode, request.getScaleCode()));
        if (count > 0) {
            throw new ServiceException(1_010_002_002, "量表编码已存在");
        }
        PsychologicalScale scale = new PsychologicalScale();
        BeanUtils.copyProperties(request, scale);
        scale.setStatus("ACTIVE".equalsIgnoreCase(request.getStatus()) || "1".equals(request.getStatus()) ? "1" : "0");
        scale.setCreatedAt(LocalDateTime.now());
        scale.setDeleted(false);
        scaleMapper.insert(scale);
        return scale.getId();
    }

    @Override
    public PsychologicalScaleRespDTO getPsychologicalScale(Long id) {
        PsychologicalScale scale = scaleMapper.selectById(id);
        if (scale == null) {
            throw new ServiceException(1_010_002_003, "量表不存在");
        }
        return convert(scale);
    }

    @Override
    public List<PsychologicalScaleRespDTO> getPsychologicalScalesByAgeAndType(Integer ageMonths,
            String scaleType) {
        validateAge(ageMonths);
        return scaleMapper.selectList(Wrappers.<PsychologicalScale>lambdaQuery()
                .eq(PsychologicalScale::getStatus, "1")
                .eq(scaleType != null && !scaleType.isBlank(), PsychologicalScale::getScaleType, scaleType)
                .le(PsychologicalScale::getApplicableAgeMin, ageMonths)
                .ge(PsychologicalScale::getApplicableAgeMax, ageMonths)
                .orderByAsc(PsychologicalScale::getScaleName))
                .stream().map(this::convert).toList();
    }

    @Override
    public List<PsychologicalScaleRespDTO> getAllActivePsychologicalScales() {
        return scaleMapper.selectList(Wrappers.<PsychologicalScale>lambdaQuery()
                .eq(PsychologicalScale::getStatus, "1")
                .orderByAsc(PsychologicalScale::getScaleName))
                .stream().map(this::convert).toList();
    }

    @Override
    public List<QuestionResponse> getQuestions(Long scaleId) {
        if (scaleMapper.selectById(scaleId) == null)
            throw new ServiceException(1_010_002_003, "量表不存在");
        return questionMapper.selectList(Wrappers.<ScaleQuestionDO>lambdaQuery()
                .eq(ScaleQuestionDO::getScaleId, scaleId).orderByAsc(ScaleQuestionDO::getSortOrder))
                .stream().map(question -> {
                    QuestionResponse response = new QuestionResponse();
                    BeanUtils.copyProperties(question, response);
                    return response;
                }).toList();
    }

    @Override
    @Transactional
    public Long submitAssessment(SubmitRequest request) {
        PsychologicalScale scale = scaleMapper.selectById(request.getScaleId());
        if (scale == null || !"1".equals(scale.getStatus()))
            throw new ServiceException(1_010_002_003, "量表不存在或已停用");
        if (childInfoMapper.selectById(request.getChildId()) == null)
            throw new ServiceException(1_010_002_010, "儿童档案不存在");
        List<ScaleQuestionDO> questions = questionMapper.selectList(Wrappers.<ScaleQuestionDO>lambdaQuery()
                .eq(ScaleQuestionDO::getScaleId, request.getScaleId()).orderByAsc(ScaleQuestionDO::getSortOrder));
        if (questions.isEmpty())
            throw new ServiceException(1_010_002_005, "量表题目未配置");
        Map<Integer, String> answers = new HashMap<>();
        request.getAnswers().forEach(answer -> answers.put(answer.getQuestionNo(), answer.getAnswer()));
        BigDecimal total = BigDecimal.ZERO;
        Map<String, BigDecimal> dimensions = new HashMap<>();
        for (ScaleQuestionDO question : questions) {
            String answer = answers.get(question.getQuestionNo());
            if (answer == null)
                throw new ServiceException(1_010_002_006, "第" + question.getQuestionNo() + "题未作答");
            if (question.getScoreRule() == null || question.getScoreRule().isBlank())
                throw new ServiceException(1_010_002_008, "第" + question.getQuestionNo() + "题计分规则缺失");
            JsonNode rule = JsonUtils.parseTree(question.getScoreRule());
            BigDecimal score = score(rule, answer);
            total = total.add(score);
            if (rule.hasNonNull("dimension"))
                dimensions.merge(rule.get("dimension").asText(), score, BigDecimal::add);
        }
        int riskLevel = riskLevel(scale.getRiskLevels(), total);
        ScaleAssessmentRecordDO record = new ScaleAssessmentRecordDO();
        record.setRecordNo("SA-" + IdWorker.getId());
        record.setChildId(request.getChildId());
        record.setScaleId(request.getScaleId());
        record.setAssessmentDate(LocalDate.now());
        record.setAssessorType(request.getAssessorType());
        record.setAssessorId(request.getAssessorId());
        record.setAnswers(JsonUtils.toJsonString(request.getAnswers()));
        record.setTotalScore(total);
        record.setDimensionScores(JsonUtils.toJsonString(dimensions));
        record.setRiskLevel(riskLevel);
        record.setIsAbnormal(riskLevel >= 3);
        record.setAssessmentConclusion(riskLevel == 1 ? "正常" : riskLevel == 2 ? "可疑" : "异常");
        record.setSuggestion(riskLevel >= 3 ? "建议尽快由专业医生复核" : riskLevel == 2 ? "建议定期复评" : "按计划健康管理");
        record.setCreateTime(LocalDateTime.now());
        assessmentMapper.insert(record);
        if (Boolean.TRUE.equals(record.getIsAbnormal())) {
            FollowTaskDO task = new FollowTaskDO();
            task.setTaskNo("FOLLOW-" + IdWorker.getId());
            task.setChildId(record.getChildId());
            task.setTaskType(2);
            task.setTaskSource("SCALE_ASSESSMENT:" + record.getId());
            task.setTaskContent("心理/发育量表评估异常，需专业复核；评估记录：" + record.getId());
            task.setPriority(1);
            task.setPlanDate(LocalDate.now());
            task.setTaskStatus(1);
            task.setCreateTime(LocalDateTime.now());
            followTaskMapper.insert(task);
        }
        return record.getId();
    }

    @Override
    public AssessmentResponse getAssessment(Long id) {
        ScaleAssessmentRecordDO record = assessmentMapper.selectById(id);
        if (record == null)
            throw new ServiceException(1_010_002_007, "评估记录不存在");
        AssessmentResponse response = new AssessmentResponse();
        BeanUtils.copyProperties(record, response);
        return response;
    }

    @Override
    public List<AssessmentResponse> getAssessments(Long childId, Long scaleId) {
        return assessmentMapper.selectList(Wrappers.<ScaleAssessmentRecordDO>lambdaQuery()
                .eq(childId != null, ScaleAssessmentRecordDO::getChildId, childId)
                .eq(scaleId != null, ScaleAssessmentRecordDO::getScaleId, scaleId)
                .orderByDesc(ScaleAssessmentRecordDO::getAssessmentDate)).stream().map(record -> {
                    AssessmentResponse response = new AssessmentResponse();
                    BeanUtils.copyProperties(record, response);
                    return response;
                }).toList();
    }

    @Override
    public Map<String, String> getScoring(Long scaleId) {
        PsychologicalScale scale = scaleMapper.selectById(scaleId);
        if (scale == null)
            throw new ServiceException(1_010_002_003, "量表不存在");
        Map<String, String> result = new HashMap<>();
        result.put("scoringRule", scale.getScoringRules());
        result.put("riskLevelRule", scale.getRiskLevels());
        return result;
    }

    @Override
    @Transactional
    public Long createReport(Long assessmentId) {
        ScaleAssessmentRecordDO assessment = assessmentMapper.selectById(assessmentId);
        if (assessment == null)
            throw new ServiceException(1_010_002_007, "评估记录不存在");
        AssessmentReportDO existing = assessmentReportMapper.selectOne(Wrappers.<AssessmentReportDO>lambdaQuery()
                .eq(AssessmentReportDO::getAssessmentId, assessmentId).last("LIMIT 1"));
        if (existing != null)
            return existing.getId();
        AssessmentReportDO report = new AssessmentReportDO();
        report.setReportNo("SAR-" + IdWorker.getId());
        report.setChildId(assessment.getChildId());
        report.setAssessmentId(assessmentId);
        report.setReportType("SCALE_ASSESSMENT");
        report.setReportDate(LocalDate.now());
        report.setReportContent(JsonUtils.toJsonString(getAssessment(assessmentId)));
        report.setCreateTime(LocalDateTime.now());
        assessmentReportMapper.insert(report);
        return report.getId();
    }

    private BigDecimal score(JsonNode rule, String answer) {
        if (rule == null || rule.isNull())
            throw new ServiceException(1_010_002_008, "题目计分规则缺失");
        if ("direct".equals(rule.path("type").asText())) {
            try {
                return new BigDecimal(answer);
            } catch (NumberFormatException ex) {
                throw new ServiceException(1_010_002_009, "答案不是有效分数");
            }
        }
        JsonNode value = rule.path("mapping").get(answer);
        if (value == null || !value.isNumber())
            throw new ServiceException(1_010_002_009, "答案不在计分规则中");
        return value.decimalValue();
    }

    private int riskLevel(String ruleJson, BigDecimal total) {
        if (ruleJson == null || ruleJson.isBlank())
            return 1;
        JsonNode levels = JsonUtils.parseTree(ruleJson).path("levels");
        for (JsonNode level : levels)
            if (total.compareTo(level.path("minScore").decimalValue()) >= 0
                    && total.compareTo(level.path("maxScore").decimalValue()) <= 0)
                return level.path("level").asInt(1);
        return 1;
    }

    private void validateAge(Integer ageMonths) {
        if (ageMonths == null || ageMonths < 0 || ageMonths > 72) {
            throw new ServiceException(1_010_002_001, "月龄范围为0-72");
        }
    }

    private void validateJson(String value, String fieldName) {
        if (value != null && !value.isBlank() && !JsonUtils.isJson(value)) {
            throw new ServiceException(1_010_002_004, fieldName + "必须是合法JSON");
        }
    }

    private PsychologicalScaleRespDTO convert(PsychologicalScale scale) {
        PsychologicalScaleRespDTO response = new PsychologicalScaleRespDTO();
        BeanUtils.copyProperties(scale, response);
        return response;
    }
}

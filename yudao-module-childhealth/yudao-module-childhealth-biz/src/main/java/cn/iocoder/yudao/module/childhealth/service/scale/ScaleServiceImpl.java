package cn.iocoder.yudao.module.childhealth.service.scale;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.module.childhealth.api.scale.ScaleService;
import cn.iocoder.yudao.module.childhealth.api.scale.dto.ScaleDTO.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.scale.*;
import cn.iocoder.yudao.module.childhealth.dal.mysql.scale.*;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 量表评估Service实现
 */
@Slf4j
@Service
public class ScaleServiceImpl implements ScaleService {

    @Resource
    private ScaleConfigMapper scaleConfigMapper;
    @Resource
    private ScaleQuestionMapper scaleQuestionMapper;
    @Resource
    private ScaleAssessmentRecordMapper assessmentRecordMapper;
    @Autowired
    private AssessmentReportMapper assessmentReportMapper;

    // ==================== 量表配置管理 ====================

    @Override
    @Transactional
    public Long createScaleConfig(ScaleConfigCreateRequest request) {
        // 检查编码唯一性
        if (scaleConfigMapper.selectCount(Wrappers.<ScaleConfigDO>lambdaQuery()
                .eq(ScaleConfigDO::getScaleCode, request.getScaleCode())) > 0) {
            throw new ServiceException(400, "量表编码已存在");
        }

        ScaleConfigDO config = new ScaleConfigDO();
        BeanUtils.copyProperties(request, config);
        config.setStatus(0); // 默认停用,需要临床审核
        config.setCreateTime(LocalDateTime.now());
        scaleConfigMapper.insert(config);
        return config.getId();
    }

    @Override
    @Transactional
    public void updateScaleConfig(ScaleConfigUpdateRequest request) {
        ScaleConfigDO existing = scaleConfigMapper.selectById(request.getId());
        if (existing == null) {
            throw new ServiceException(404, "量表配置不存在");
        }

        ScaleConfigDO config = new ScaleConfigDO();
        BeanUtils.copyProperties(request, config);
        scaleConfigMapper.updateById(config);
    }

    @Override
    @Transactional
    public void deleteScaleConfig(Long id) {
        // 删除量表时,同时删除题目
        scaleQuestionMapper.delete(Wrappers.<ScaleQuestionDO>lambdaQuery()
                .eq(ScaleQuestionDO::getScaleId, id));
        scaleConfigMapper.deleteById(id);
    }

    @Override
    public ScaleConfigResponse getScaleConfig(Long id) {
        ScaleConfigDO config = scaleConfigMapper.selectById(id);
        return config == null ? null : convertToScaleConfigResponse(config);
    }

    @Override
    public PageResult<ScaleConfigResponse> getScaleConfigPage(ScaleQueryRequest request) {
        List<ScaleConfigDO> list = scaleConfigMapper.selectList(Wrappers.<ScaleConfigDO>lambdaQuery()
                .eq(request.getScaleType() != null, ScaleConfigDO::getScaleType, request.getScaleType())
                .eq(request.getStatus() != null, ScaleConfigDO::getStatus, request.getStatus())
                .orderByDesc(ScaleConfigDO::getCreateTime));

        List<ScaleConfigResponse> responseList = list.stream()
                .map(this::convertToScaleConfigResponse)
                .collect(Collectors.toList());

        return new PageResult<>(responseList, (long) responseList.size());
    }

    @Override
    public List<ScaleConfigResponse> getActiveScaleConfigs() {
        return scaleConfigMapper.selectList(Wrappers.<ScaleConfigDO>lambdaQuery()
                        .eq(ScaleConfigDO::getStatus, 1))
                .stream()
                .map(this::convertToScaleConfigResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ScaleConfigResponse> getApplicableScales(Integer ageMonth) {
        return scaleConfigMapper.selectList(Wrappers.<ScaleConfigDO>lambdaQuery()
                        .eq(ScaleConfigDO::getStatus, 1)
                        .le(ScaleConfigDO::getApplicableAgeMin, ageMonth)
                        .ge(ScaleConfigDO::getApplicableAgeMax, ageMonth))
                .stream()
                .map(this::convertToScaleConfigResponse)
                .collect(Collectors.toList());
    }

    // ==================== 量表题目管理 ====================

    @Override
    @Transactional
    public Long createScaleQuestion(ScaleQuestionCreateRequest request) {
        validateScaleExists(request.getScaleId());

        ScaleQuestionDO question = new ScaleQuestionDO();
        BeanUtils.copyProperties(request, question);
        question.setCreateTime(LocalDateTime.now());
        scaleQuestionMapper.insert(question);
        return question.getId();
    }

    @Override
    @Transactional
    public void batchCreateScaleQuestions(ScaleQuestionBatchCreateRequest request) {
        validateScaleExists(request.getScaleId());

        for (ScaleQuestionCreateRequest questionReq : request.getQuestions()) {
            questionReq.setScaleId(request.getScaleId());
            createScaleQuestion(questionReq);
        }
    }

    @Override
    @Transactional
    public void updateScaleQuestion(Long id, ScaleQuestionCreateRequest request) {
        ScaleQuestionDO existing = scaleQuestionMapper.selectById(id);
        if (existing == null) {
            throw new ServiceException(404, "量表题目不存在");
        }

        ScaleQuestionDO question = new ScaleQuestionDO();
        BeanUtils.copyProperties(request, question);
        question.setId(id);
        scaleQuestionMapper.updateById(question);
    }

    @Override
    @Transactional
    public void deleteScaleQuestion(Long id) {
        scaleQuestionMapper.deleteById(id);
    }

    @Override
    public List<ScaleQuestionResponse> getScaleQuestions(Long scaleId) {
        return scaleQuestionMapper.selectList(Wrappers.<ScaleQuestionDO>lambdaQuery()
                        .eq(ScaleQuestionDO::getScaleId, scaleId)
                        .orderByAsc(ScaleQuestionDO::getQuestionNo))
                .stream()
                .map(this::convertToScaleQuestionResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteScaleQuestionsByScale(Long scaleId) {
        scaleQuestionMapper.delete(Wrappers.<ScaleQuestionDO>lambdaQuery()
                .eq(ScaleQuestionDO::getScaleId, scaleId));
    }

    // ==================== 量表评估管理 ====================

    @Override
    @Transactional
    public Long submitScaleAssessment(ScaleAssessmentSubmitRequest request) {
        // 验证量表存在且启用
        ScaleConfigDO scale = scaleConfigMapper.selectById(request.getScaleId());
        if (scale == null) {
            throw new ServiceException(404, "量表不存在");
        }
        if (scale.getStatus() != 1) {
            throw new ServiceException(400, "量表未启用");
        }

        // 计算总分
        BigDecimal totalScore = calculateScaleScore(request.getScaleId(), request.getAnswers());

        // 评估风险等级
        Integer riskLevel = assessRiskLevel(request.getScaleId(), totalScore);

        // 生成评估建议
        String suggestion = generateAssessmentSuggestion(request.getScaleId(), totalScore, riskLevel);

        // 保存评估记录
        ScaleAssessmentRecordDO record = new ScaleAssessmentRecordDO();
        record.setRecordNo("ASSESS-" + IdWorker.getId());
        record.setChildId(request.getChildId());
        record.setScaleId(request.getScaleId());
        record.setAssessmentDate(request.getAssessmentDate());
        record.setAssessorType(request.getAssessorType());
        record.setAssessorId(request.getAssessorId());
        record.setAnswers(JsonUtils.toJsonString(request.getAnswers()));
        record.setTotalScore(totalScore);
        record.setRiskLevel(riskLevel);
        record.setIsAbnormal(riskLevel == 3);
        record.setSuggestion(suggestion);
        record.setCreateTime(LocalDateTime.now());
        assessmentRecordMapper.insert(record);

        return record.getId();
    }

    @Override
    public BigDecimal calculateScaleScore(Long scaleId, List<AnswerItem> answers) {
        // 获取量表题目
        List<ScaleQuestionDO> questions = scaleQuestionMapper.selectList(
                Wrappers.<ScaleQuestionDO>lambdaQuery().eq(ScaleQuestionDO::getScaleId, scaleId));

        BigDecimal totalScore = BigDecimal.ZERO;

        for (AnswerItem answer : answers) {
            // 查找对应题目
            ScaleQuestionDO question = questions.stream()
                    .filter(q -> q.getQuestionNo().equals(answer.getQuestionNo()))
                    .findFirst()
                    .orElse(null);

            if (question != null && answer.getQuestionScore() != null) {
                totalScore = totalScore.add(answer.getQuestionScore());
            }
        }

        return totalScore;
    }

    @Override
    public Integer assessRiskLevel(Long scaleId, BigDecimal totalScore) {
        ScaleConfigDO scale = scaleConfigMapper.selectById(scaleId);
        if (scale == null) {
            return 1; // 默认正常
        }

        // 根据异常阈值判定
        if (scale.getAbnormalThreshold() != null) {
            if (totalScore.compareTo(scale.getAbnormalThreshold()) >= 0) {
                return 3; // 异常
            }
        }

        // TODO: 根据riskLevelRule进行更详细的风险等级判定
        return 1; // 正常
    }

    @Override
    public String generateAssessmentSuggestion(Long scaleId, BigDecimal totalScore, Integer riskLevel) {
        // 根据风险等级生成建议
        switch (riskLevel) {
            case 1:
                return "评估结果正常,建议继续保持良好的生活习惯和定期复查。";
            case 2:
                return "评估结果可疑,建议进一步观察和定期复查,如有异常及时就医。";
            case 3:
                return "评估结果异常,建议尽快到专科门诊就诊,进行详细检查和干预治疗。";
            default:
                return "评估完成,请咨询医生获取详细建议。";
        }
    }

    @Override
    public ScaleAssessmentRecordResponse getScaleAssessment(Long id) {
        ScaleAssessmentRecordDO record = assessmentRecordMapper.selectById(id);
        return record == null ? null : convertToScaleAssessmentRecordResponse(record);
    }

    @Override
    public PageResult<ScaleAssessmentRecordResponse> getScaleAssessmentPage(ScaleAssessmentQueryRequest request) {
        List<ScaleAssessmentRecordDO> list = assessmentRecordMapper.selectList(
                Wrappers.<ScaleAssessmentRecordDO>lambdaQuery()
                        .eq(request.getChildId() != null, ScaleAssessmentRecordDO::getChildId, request.getChildId())
                        .eq(request.getScaleId() != null, ScaleAssessmentRecordDO::getScaleId, request.getScaleId())
                        .eq(request.getRiskLevel() != null, ScaleAssessmentRecordDO::getRiskLevel, request.getRiskLevel())
                        .eq(request.getIsAbnormal() != null, ScaleAssessmentRecordDO::getIsAbnormal, request.getIsAbnormal())
                        .orderByDesc(ScaleAssessmentRecordDO::getAssessmentDate));

        List<ScaleAssessmentRecordResponse> responseList = list.stream()
                .map(this::convertToScaleAssessmentRecordResponse)
                .collect(Collectors.toList());

        return new PageResult<>(responseList, (long) responseList.size());
    }

    @Override
    public List<ScaleAssessmentRecordResponse> getScaleAssessmentsByChild(Long childId) {
        return assessmentRecordMapper.selectList(Wrappers.<ScaleAssessmentRecordDO>lambdaQuery()
                        .eq(ScaleAssessmentRecordDO::getChildId, childId)
                        .orderByDesc(ScaleAssessmentRecordDO::getAssessmentDate))
                .stream()
                .map(this::convertToScaleAssessmentRecordResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteScaleAssessment(Long id) {
        assessmentRecordMapper.deleteById(id);
    }

    // ==================== 评估报告管理 ====================

    @Override
    @Transactional
    public Long generateAssessmentReport(AssessmentReportGenerateRequest request) {
        ScaleAssessmentRecordDO assessment = assessmentRecordMapper.selectById(request.getAssessmentId());
        if (assessment == null) {
            throw new ServiceException(404, "评估记录不存在");
        }

        // 生成报告内容
        String reportContent = generateReportContent(assessment);

        AssessmentReportDO report = new AssessmentReportDO();
        report.setReportNo("REPORT-" + IdWorker.getId());
        report.setChildId(assessment.getChildId());
        report.setAssessmentId(request.getAssessmentId());
        report.setReportType(request.getReportType());
        report.setReportDate(LocalDateTime.now().toLocalDate());
        report.setReportContent(reportContent);
        report.setCreateTime(LocalDateTime.now());
        assessmentReportMapper.insert(report);

        return report.getId();
    }

    @Override
    public AssessmentReportResponse getAssessmentReport(Long id) {
        AssessmentReportDO report = assessmentReportMapper.selectById(id);
        return report == null ? null : convertToAssessmentReportResponse(report);
    }

    @Override
    public AssessmentReportResponse getAssessmentReportByAssessment(Long assessmentId) {
        AssessmentReportDO report = assessmentReportMapper.selectOne(
                Wrappers.<AssessmentReportDO>lambdaQuery()
                        .eq(AssessmentReportDO::getAssessmentId, assessmentId)
                        .last("LIMIT 1"));
        return report == null ? null : convertToAssessmentReportResponse(report);
    }

    // ==================== 发育里程碑管理 ====================

    @Override
    @Transactional
    public Long createDevelopmentMilestone(DevelopmentMilestoneCreateRequest request) {
        // 简化实现,实际需要对应的DO和Mapper
        log.info("创建发育里程碑: {}", request);
        return IdWorker.getId();
    }

    @Override
    public List<DevelopmentMilestoneResponse> getAllDevelopmentMilestones() {
        // 简化实现
        return List.of();
    }

    @Override
    public List<DevelopmentMilestoneResponse> getDevelopmentMilestonesByDomain(String domain) {
        // 简化实现
        return List.of();
    }

    @Override
    public List<DevelopmentMilestoneResponse> getDevelopmentMilestonesByAge(Integer ageMonth) {
        // 简化实现
        return List.of();
    }

    // ==================== 儿童发育评估管理 ====================

    @Override
    @Transactional
    public Long createChildDevelopmentAssessment(ChildDevelopmentAssessmentCreateRequest request) {
        // 简化实现,实际需要对应的DO和Mapper
        log.info("创建儿童发育评估: {}", request);
        return IdWorker.getId();
    }

    @Override
    @Transactional
    public void updateChildDevelopmentAssessment(Long id, ChildDevelopmentAssessmentCreateRequest request) {
        log.info("更新儿童发育评估: id={}, request={}", id, request);
    }

    @Override
    public ChildDevelopmentAssessmentResponse getChildDevelopmentAssessment(Long id) {
        // 简化实现
        return null;
    }

    @Override
    public List<ChildDevelopmentAssessmentResponse> getChildDevelopmentAssessmentsByChild(Long childId) {
        // 简化实现
        return List.of();
    }

    @Override
    public Boolean checkDevelopmentDelay(Long childId, Integer monthAge) {
        // 简化实现
        return false;
    }

    // ==================== 辅助方法 ====================

    private void validateScaleExists(Long scaleId) {
        if (scaleConfigMapper.selectById(scaleId) == null) {
            throw new ServiceException(404, "量表不存在");
        }
    }

    private String generateReportContent(ScaleAssessmentRecordDO assessment) {
        return JsonUtils.toJsonString(assessment);
    }

    private ScaleConfigResponse convertToScaleConfigResponse(ScaleConfigDO config) {
        ScaleConfigResponse response = new ScaleConfigResponse();
        BeanUtils.copyProperties(config, response);
        return response;
    }

    private ScaleQuestionResponse convertToScaleQuestionResponse(ScaleQuestionDO question) {
        ScaleQuestionResponse response = new ScaleQuestionResponse();
        BeanUtils.copyProperties(question, response);
        return response;
    }

    private ScaleAssessmentRecordResponse convertToScaleAssessmentRecordResponse(ScaleAssessmentRecordDO record) {
        ScaleAssessmentRecordResponse response = new ScaleAssessmentRecordResponse();
        BeanUtils.copyProperties(record, response);

        // 获取量表名称
        ScaleConfigDO scale = scaleConfigMapper.selectById(record.getScaleId());
        if (scale != null) {
            response.setScaleName(scale.getScaleName());
        }

        return response;
    }

    private AssessmentReportResponse convertToAssessmentReportResponse(AssessmentReportDO report) {
        AssessmentReportResponse response = new AssessmentReportResponse();
        BeanUtils.copyProperties(report, response);
        return response;
    }
}
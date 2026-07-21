package cn.iocoder.yudao.module.childhealth.api.scale;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.api.scale.dto.ScaleDTO.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 量表评估Service接口
 */
public interface ScaleService {

    // ==================== 量表配置管理 ====================

    /**
     * 创建量表配置
     */
    Long createScaleConfig(ScaleConfigCreateRequest request);

    /**
     * 更新量表配置
     */
    void updateScaleConfig(ScaleConfigUpdateRequest request);

    /**
     * 删除量表配置
     */
    void deleteScaleConfig(Long id);

    /**
     * 获取量表配置详情
     */
    ScaleConfigResponse getScaleConfig(Long id);

    /**
     * 分页查询量表配置
     */
    PageResult<ScaleConfigResponse> getScaleConfigPage(ScaleQueryRequest request);

    /**
     * 获取所有启用的量表配置
     */
    List<ScaleConfigResponse> getActiveScaleConfigs();

    /**
     * 根据年龄获取适用的量表列表
     */
    List<ScaleConfigResponse> getApplicableScales(Integer ageMonth);

    // ==================== 量表题目管理 ====================

    /**
     * 创建量表题目
     */
    Long createScaleQuestion(ScaleQuestionCreateRequest request);

    /**
     * 批量创建量表题目
     */
    void batchCreateScaleQuestions(ScaleQuestionBatchCreateRequest request);

    /**
     * 更新量表题目
     */
    void updateScaleQuestion(Long id, ScaleQuestionCreateRequest request);

    /**
     * 删除量表题目
     */
    void deleteScaleQuestion(Long id);

    /**
     * 获取量表的所有题目
     */
    List<ScaleQuestionResponse> getScaleQuestions(Long scaleId);

    /**
     * 批量删除量表的题目
     */
    void deleteScaleQuestionsByScale(Long scaleId);

    // ==================== 量表评估管理 ====================

    /**
     * 提交量表评估
     */
    Long submitScaleAssessment(ScaleAssessmentSubmitRequest request);

    /**
     * 计算量表得分
     */
    BigDecimal calculateScaleScore(Long scaleId, List<AnswerItem> answers);

    /**
     * 评估风险等级
     */
    Integer assessRiskLevel(Long scaleId, BigDecimal totalScore);

    /**
     * 生成评估建议
     */
    String generateAssessmentSuggestion(Long scaleId, BigDecimal totalScore, Integer riskLevel);

    /**
     * 获取评估记录详情
     */
    ScaleAssessmentRecordResponse getScaleAssessment(Long id);

    /**
     * 分页查询评估记录
     */
    PageResult<ScaleAssessmentRecordResponse> getScaleAssessmentPage(ScaleAssessmentQueryRequest request);

    /**
     * 获取儿童的评估记录列表
     */
    List<ScaleAssessmentRecordResponse> getScaleAssessmentsByChild(Long childId);

    /**
     * 删除评估记录
     */
    void deleteScaleAssessment(Long id);

    // ==================== 评估报告管理 ====================

    /**
     * 生成评估报告
     */
    Long generateAssessmentReport(AssessmentReportGenerateRequest request);

    /**
     * 获取评估报告详情
     */
    AssessmentReportResponse getAssessmentReport(Long id);

    /**
     * 根据评估记录获取报告
     */
    AssessmentReportResponse getAssessmentReportByAssessment(Long assessmentId);

    // ==================== 发育里程碑管理 ====================

    /**
     * 创建发育里程碑
     */
    Long createDevelopmentMilestone(DevelopmentMilestoneCreateRequest request);

    /**
     * 获取所有发育里程碑
     */
    List<DevelopmentMilestoneResponse> getAllDevelopmentMilestones();

    /**
     * 根据发育领域获取里程碑
     */
    List<DevelopmentMilestoneResponse> getDevelopmentMilestonesByDomain(String domain);

    /**
     * 根据月龄获取发育里程碑
     */
    List<DevelopmentMilestoneResponse> getDevelopmentMilestonesByAge(Integer ageMonth);

    // ==================== 儿童发育评估管理 ====================

    /**
     * 创建儿童发育评估
     */
    Long createChildDevelopmentAssessment(ChildDevelopmentAssessmentCreateRequest request);

    /**
     * 更新儿童发育评估
     */
    void updateChildDevelopmentAssessment(Long id, ChildDevelopmentAssessmentCreateRequest request);

    /**
     * 获取儿童发育评估详情
     */
    ChildDevelopmentAssessmentResponse getChildDevelopmentAssessment(Long id);

    /**
     * 获取儿童的发育评估列表
     */
    List<ChildDevelopmentAssessmentResponse> getChildDevelopmentAssessmentsByChild(Long childId);

    /**
     * 判断儿童是否有发育迟缓
     */
    Boolean checkDevelopmentDelay(Long childId, Integer monthAge);
}
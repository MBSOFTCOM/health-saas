package cn.iocoder.yudao.module.childhealth.service.screeningconfig.impl;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig.PsychologicalAssessmentDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.screeningconfig.PsychologicalAssessmentMapper;
import cn.iocoder.yudao.module.childhealth.service.screeningconfig.PsychologicalAssessmentService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.PSYCHOLOGICAL_ASSESSMENT_NOT_EXISTS;

/**
 * 心理量表评估记录 Service 实现类
 *
 * 模块: C.五健筛查配置
 * 创建日期: 2026-07-20
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class PsychologicalAssessmentServiceImpl implements PsychologicalAssessmentService {

    @Resource
    private PsychologicalAssessmentMapper psychologicalAssessmentMapper;

    @Override
    public Long createPsychologicalAssessment(Object saveReqVO) {
        // TODO 后续替换为 PsychologicalAssessmentSaveReqVO
        PsychologicalAssessmentDO assessment = BeanUtils.toBean(saveReqVO, PsychologicalAssessmentDO.class);
        psychologicalAssessmentMapper.insert(assessment);
        return assessment.getId();
    }

    @Override
    public void updatePsychologicalAssessment(Object saveReqVO) {
        // TODO 后续替换为 PsychologicalAssessmentSaveReqVO
        PsychologicalAssessmentDO updateObj = BeanUtils.toBean(saveReqVO, PsychologicalAssessmentDO.class);
        validatePsychologicalAssessmentExists(updateObj.getId());
        psychologicalAssessmentMapper.updateById(updateObj);
    }

    @Override
    public void deletePsychologicalAssessment(Long id) {
        validatePsychologicalAssessmentExists(id);
        psychologicalAssessmentMapper.deleteById(id);
    }

    @Override
    public PsychologicalAssessmentDO getPsychologicalAssessment(Long id) {
        return psychologicalAssessmentMapper.selectById(id);
    }

    @Override
    public PageResult<PsychologicalAssessmentDO> getPsychologicalAssessmentPage(PageParam pageParam) {
        // TODO 后续替换为 PsychologicalAssessmentPageReqVO，并增加查询条件
        return psychologicalAssessmentMapper.selectPage(pageParam, null);
    }

    @Override
    public void autoCalculateScore(Long id) {
        // TODO 实现自动计分：拉取量表题目与答题明细，按 scoringRuleJson 计算总分及各维度得分，回写评估记录
        log.info("[autoCalculateScore] 自动计分 assessmentId={}", id);
        validatePsychologicalAssessmentExists(id);
    }

    @Override
    public void autoRiskLevel(Long id) {
        // TODO 实现自动风险分级：根据总分及量表临界值配置，确定风险等级（1正常 2轻度 3中度 4重度），回写评估记录
        log.info("[autoRiskLevel] 自动风险分级 assessmentId={}", id);
        validatePsychologicalAssessmentExists(id);
    }

    @Override
    public List<PsychologicalAssessmentDO> selectByChildAndScale(Long childId, Long scaleId) {
        // TODO 当前 Mapper 暂未提供 selectByChildAndScale 联合查询方法，可基于 selectListByChildId 在内存中过滤，
        //  后续建议在 Mapper 层增加联合查询方法
        List<PsychologicalAssessmentDO> list = psychologicalAssessmentMapper.selectListByChildId(childId);
        List<PsychologicalAssessmentDO> result = new ArrayList<>();
        for (PsychologicalAssessmentDO assessment : list) {
            if (scaleId != null && scaleId.equals(assessment.getScaleId())) {
                result.add(assessment);
            }
        }
        return result;
    }

    private void validatePsychologicalAssessmentExists(Long id) {
        if (id == null || psychologicalAssessmentMapper.selectById(id) == null) {
            throw exception(PSYCHOLOGICAL_ASSESSMENT_NOT_EXISTS);
        }
    }

}

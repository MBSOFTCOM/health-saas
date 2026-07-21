package cn.iocoder.yudao.module.childhealth.service.medical.impl;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.medical.DevelopmentAssessmentAnswerDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.medical.DevelopmentAssessmentAnswerMapper;
import cn.iocoder.yudao.module.childhealth.service.medical.DevelopmentAssessmentAnswerService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.DEVELOPMENT_ASSESSMENT_ANSWER_NOT_EXISTS;

/**
 * 发育评估答题表 Service 实现类
 *
 * 模块: A. 儿童基础健康检查（A9-发育评估答题表）
 * 创建日期: 2026-07-20
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class DevelopmentAssessmentAnswerServiceImpl implements DevelopmentAssessmentAnswerService {

    @Resource
    private DevelopmentAssessmentAnswerMapper developmentAssessmentAnswerMapper;

    @Override
    public Long createDevelopmentAssessmentAnswer(Object saveReqVO) {
        // TODO 后续替换为 DevelopmentAssessmentAnswerSaveReqVO
        DevelopmentAssessmentAnswerDO answer = BeanUtils.toBean(saveReqVO, DevelopmentAssessmentAnswerDO.class);
        developmentAssessmentAnswerMapper.insert(answer);
        return answer.getId();
    }

    @Override
    public void updateDevelopmentAssessmentAnswer(Object saveReqVO) {
        // TODO 后续替换为 DevelopmentAssessmentAnswerSaveReqVO
        DevelopmentAssessmentAnswerDO updateObj = BeanUtils.toBean(saveReqVO, DevelopmentAssessmentAnswerDO.class);
        validateDevelopmentAssessmentAnswerExists(updateObj.getId());
        developmentAssessmentAnswerMapper.updateById(updateObj);
    }

    @Override
    public void deleteDevelopmentAssessmentAnswer(Long id) {
        validateDevelopmentAssessmentAnswerExists(id);
        developmentAssessmentAnswerMapper.deleteById(id);
    }

    @Override
    public DevelopmentAssessmentAnswerDO getDevelopmentAssessmentAnswer(Long id) {
        return developmentAssessmentAnswerMapper.selectById(id);
    }

    @Override
    public PageResult<DevelopmentAssessmentAnswerDO> getDevelopmentAssessmentAnswerPage(PageParam pageParam) {
        // TODO 后续替换为 DevelopmentAssessmentAnswerPageReqVO，并增加查询条件
        return developmentAssessmentAnswerMapper.selectPage(pageParam, null);
    }

    @Override
    public void batchInsert(List<DevelopmentAssessmentAnswerDO> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        developmentAssessmentAnswerMapper.insertBatch(list);
    }

    @Override
    public List<DevelopmentAssessmentAnswerDO> selectListByRecordId(Long recordId) {
        return developmentAssessmentAnswerMapper.selectListByRecordId(recordId);
    }

    private void validateDevelopmentAssessmentAnswerExists(Long id) {
        if (id == null || developmentAssessmentAnswerMapper.selectById(id) == null) {
            throw exception(DEVELOPMENT_ASSESSMENT_ANSWER_NOT_EXISTS);
        }
    }

}

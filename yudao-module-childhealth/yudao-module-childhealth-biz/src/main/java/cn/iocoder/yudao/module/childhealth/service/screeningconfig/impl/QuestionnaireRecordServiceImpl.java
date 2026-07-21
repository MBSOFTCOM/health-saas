package cn.iocoder.yudao.module.childhealth.service.screeningconfig.impl;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig.QuestionnaireRecordDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.screeningconfig.QuestionnaireRecordMapper;
import cn.iocoder.yudao.module.childhealth.service.screeningconfig.QuestionnaireRecordService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.QUESTIONNAIRE_RECORD_NOT_EXISTS;

/**
 * 问卷答卷 Service 实现类
 *
 * 模块: C.五健筛查配置
 * 创建日期: 2026-07-20
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class QuestionnaireRecordServiceImpl implements QuestionnaireRecordService {

    @Resource
    private QuestionnaireRecordMapper questionnaireRecordMapper;

    @Override
    public Long createQuestionnaireRecord(Object saveReqVO) {
        // TODO 后续替换为 QuestionnaireRecordSaveReqVO
        QuestionnaireRecordDO record = BeanUtils.toBean(saveReqVO, QuestionnaireRecordDO.class);
        questionnaireRecordMapper.insert(record);
        return record.getId();
    }

    @Override
    public void updateQuestionnaireRecord(Object saveReqVO) {
        // TODO 后续替换为 QuestionnaireRecordSaveReqVO
        QuestionnaireRecordDO updateObj = BeanUtils.toBean(saveReqVO, QuestionnaireRecordDO.class);
        validateQuestionnaireRecordExists(updateObj.getId());
        questionnaireRecordMapper.updateById(updateObj);
    }

    @Override
    public void deleteQuestionnaireRecord(Long id) {
        validateQuestionnaireRecordExists(id);
        questionnaireRecordMapper.deleteById(id);
    }

    @Override
    public QuestionnaireRecordDO getQuestionnaireRecord(Long id) {
        return questionnaireRecordMapper.selectById(id);
    }

    @Override
    public PageResult<QuestionnaireRecordDO> getQuestionnaireRecordPage(PageParam pageParam) {
        // TODO 后续替换为 QuestionnaireRecordPageReqVO，并增加查询条件
        return questionnaireRecordMapper.selectPage(pageParam, null);
    }

    @Override
    public void autoScore(Long id) {
        // TODO 实现自动计分：根据问卷题型与答案计算总分，回写答卷 totalScore / dimensionScore
        log.info("[autoScore] 自动计分 recordId={}", id);
        validateQuestionnaireRecordExists(id);
    }

    @Override
    public List<QuestionnaireRecordDO> selectByQuestionnaireAndChild(Long questionnaireId, Long childId) {
        // TODO 当前 Mapper 暂未提供 selectByQuestionnaireAndChild，可基于 selectListByChildId 在内存中过滤，
        //  后续建议在 Mapper 层增加联合查询方法
        List<QuestionnaireRecordDO> list = questionnaireRecordMapper.selectListByChildId(childId);
        List<QuestionnaireRecordDO> result = new java.util.ArrayList<>();
        for (QuestionnaireRecordDO record : list) {
            if (questionnaireId != null && questionnaireId.equals(record.getQuestionnaireId())) {
                result.add(record);
            }
        }
        return result;
    }

    private void validateQuestionnaireRecordExists(Long id) {
        if (id == null || questionnaireRecordMapper.selectById(id) == null) {
            throw exception(QUESTIONNAIRE_RECORD_NOT_EXISTS);
        }
    }

}

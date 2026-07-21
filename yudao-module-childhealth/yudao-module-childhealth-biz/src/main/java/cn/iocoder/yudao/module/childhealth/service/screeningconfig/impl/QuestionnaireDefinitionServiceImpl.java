package cn.iocoder.yudao.module.childhealth.service.screeningconfig.impl;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig.QuestionnaireDefinitionDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.screeningconfig.QuestionnaireDefinitionMapper;
import cn.iocoder.yudao.module.childhealth.service.screeningconfig.QuestionnaireDefinitionService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.QUESTIONNAIRE_DEFINITION_CODE_DUPLICATE;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.QUESTIONNAIRE_DEFINITION_NOT_EXISTS;

/**
 * 问卷定义 Service 实现类
 *
 * 模块: C.五健筛查配置
 * 创建日期: 2026-07-20
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class QuestionnaireDefinitionServiceImpl implements QuestionnaireDefinitionService {

    @Resource
    private QuestionnaireDefinitionMapper questionnaireDefinitionMapper;

    @Override
    public Long createQuestionnaireDefinition(Object saveReqVO) {
        // TODO 后续替换为 QuestionnaireDefinitionSaveReqVO
        QuestionnaireDefinitionDO definition = BeanUtils.toBean(saveReqVO, QuestionnaireDefinitionDO.class);
        // 编码唯一性校验
        if (definition.getCode() != null
                && questionnaireDefinitionMapper.selectByCode(definition.getCode()) != null) {
            throw exception(QUESTIONNAIRE_DEFINITION_CODE_DUPLICATE);
        }
        questionnaireDefinitionMapper.insert(definition);
        return definition.getId();
    }

    @Override
    public void updateQuestionnaireDefinition(Object saveReqVO) {
        // TODO 后续替换为 QuestionnaireDefinitionSaveReqVO
        QuestionnaireDefinitionDO updateObj = BeanUtils.toBean(saveReqVO, QuestionnaireDefinitionDO.class);
        validateQuestionnaireDefinitionExists(updateObj.getId());
        questionnaireDefinitionMapper.updateById(updateObj);
    }

    @Override
    public void deleteQuestionnaireDefinition(Long id) {
        validateQuestionnaireDefinitionExists(id);
        questionnaireDefinitionMapper.deleteById(id);
    }

    @Override
    public QuestionnaireDefinitionDO getQuestionnaireDefinition(Long id) {
        return questionnaireDefinitionMapper.selectById(id);
    }

    @Override
    public PageResult<QuestionnaireDefinitionDO> getQuestionnaireDefinitionPage(PageParam pageParam) {
        // TODO 后续替换为 QuestionnaireDefinitionPageReqVO，并增加查询条件
        return questionnaireDefinitionMapper.selectPage(pageParam, null);
    }

    @Override
    public QuestionnaireDefinitionDO selectByCode(String code) {
        return questionnaireDefinitionMapper.selectByCode(code);
    }

    @Override
    public List<QuestionnaireDefinitionDO> selectActiveList() {
        return questionnaireDefinitionMapper.selectActiveList();
    }

    private void validateQuestionnaireDefinitionExists(Long id) {
        if (id == null || questionnaireDefinitionMapper.selectById(id) == null) {
            throw exception(QUESTIONNAIRE_DEFINITION_NOT_EXISTS);
        }
    }

}

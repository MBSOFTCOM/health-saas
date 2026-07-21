package cn.iocoder.yudao.module.childhealth.service.casemanagement.impl;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.casemanagement.ExamReminderTemplateDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.casemanagement.ExamReminderTemplateMapper;
import cn.iocoder.yudao.module.childhealth.service.casemanagement.ExamReminderTemplateService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.*;

/**
 * 体检提醒模板 Service 实现类
 *
 * 模块: B.体检催检
 * 创建日期: 2026-07-20
 */
@Service
@Validated
public class ExamReminderTemplateServiceImpl implements ExamReminderTemplateService {

    @Resource
    private ExamReminderTemplateMapper examReminderTemplateMapper;

    @Override
    public Long createExamReminderTemplate(Object createReqVO) {
        // TODO 后续替换为 ExamReminderTemplateSaveReqVO，并校验 templateCode 唯一
        ExamReminderTemplateDO examReminderTemplate = BeanUtils.toBean(createReqVO, ExamReminderTemplateDO.class);
        examReminderTemplateMapper.insert(examReminderTemplate);
        return examReminderTemplate.getId();
    }

    @Override
    public void updateExamReminderTemplate(Object updateReqVO) {
        // TODO 后续替换为 ExamReminderTemplateSaveReqVO，并校验存在与 templateCode 唯一
        // validateExamReminderTemplateExists(updateReqVO.getId());
        ExamReminderTemplateDO updateObj = BeanUtils.toBean(updateReqVO, ExamReminderTemplateDO.class);
        examReminderTemplateMapper.updateById(updateObj);
    }

    @Override
    public void deleteExamReminderTemplate(Long id) {
        validateExamReminderTemplateExists(id);
        examReminderTemplateMapper.deleteById(id);
    }

    private void validateExamReminderTemplateExists(Long id) {
        if (examReminderTemplateMapper.selectById(id) == null) {
            throw exception(EXAM_REMINDER_TEMPLATE_NOT_EXISTS);
        }
    }

    @Override
    public ExamReminderTemplateDO getExamReminderTemplate(Long id) {
        return examReminderTemplateMapper.selectById(id);
    }

    @Override
    public PageResult<ExamReminderTemplateDO> getExamReminderTemplatePage(PageParam pageParam) {
        // TODO 后续替换为 ExamReminderTemplatePageReqVO，并实现分页查询条件
        return examReminderTemplateMapper.selectPage(pageParam, null);
    }

    @Override
    public ExamReminderTemplateDO selectByCode(String templateCode) {
        return examReminderTemplateMapper.selectByTemplateCode(templateCode);
    }

    @Override
    public List<ExamReminderTemplateDO> selectActiveList() {
        return examReminderTemplateMapper.selectActiveList();
    }

}

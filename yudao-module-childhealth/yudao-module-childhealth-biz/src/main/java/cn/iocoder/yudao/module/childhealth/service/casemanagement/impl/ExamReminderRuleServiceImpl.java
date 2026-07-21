package cn.iocoder.yudao.module.childhealth.service.casemanagement.impl;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.casemanagement.ExamReminderRuleDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.casemanagement.ExamReminderRuleMapper;
import cn.iocoder.yudao.module.childhealth.service.casemanagement.ExamReminderRuleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.*;

/**
 * 体检催检规则 Service 实现类
 *
 * 模块: B.体检催检
 * 创建日期: 2026-07-20
 */
@Service
@Validated
public class ExamReminderRuleServiceImpl implements ExamReminderRuleService {

    @Resource
    private ExamReminderRuleMapper examReminderRuleMapper;

    @Override
    public Long createExamReminderRule(Object createReqVO) {
        // TODO 后续替换为 ExamReminderRuleSaveReqVO，并校验 ruleCode 唯一
        ExamReminderRuleDO examReminderRule = BeanUtils.toBean(createReqVO, ExamReminderRuleDO.class);
        examReminderRuleMapper.insert(examReminderRule);
        return examReminderRule.getId();
    }

    @Override
    public void updateExamReminderRule(Object updateReqVO) {
        // TODO 后续替换为 ExamReminderRuleSaveReqVO，并校验存在与 ruleCode 唯一
        // validateExamReminderRuleExists(updateReqVO.getId());
        ExamReminderRuleDO updateObj = BeanUtils.toBean(updateReqVO, ExamReminderRuleDO.class);
        examReminderRuleMapper.updateById(updateObj);
    }

    @Override
    public void deleteExamReminderRule(Long id) {
        validateExamReminderRuleExists(id);
        examReminderRuleMapper.deleteById(id);
    }

    private void validateExamReminderRuleExists(Long id) {
        if (examReminderRuleMapper.selectById(id) == null) {
            throw exception(EXAM_REMINDER_RULE_NOT_EXISTS);
        }
    }

    @Override
    public ExamReminderRuleDO getExamReminderRule(Long id) {
        return examReminderRuleMapper.selectById(id);
    }

    @Override
    public PageResult<ExamReminderRuleDO> getExamReminderRulePage(PageParam pageParam) {
        // TODO 后续替换为 ExamReminderRulePageReqVO，并实现分页查询条件
        return examReminderRuleMapper.selectPage(pageParam, null);
    }

    @Override
    public List<ExamReminderRuleDO> selectActiveList() {
        return examReminderRuleMapper.selectActiveList();
    }

    @Override
    public List<ExamReminderRuleDO> selectByExamType(String examType) {
        return examReminderRuleMapper.selectListByExamType(examType);
    }

}

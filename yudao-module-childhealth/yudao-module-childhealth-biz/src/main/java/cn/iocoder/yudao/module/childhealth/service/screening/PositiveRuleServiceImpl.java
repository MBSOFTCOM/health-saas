package cn.iocoder.yudao.module.childhealth.service.screening;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.rule.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.PositiveRuleDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.screening.PositiveRuleMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.*;

/**
 * 阳性判定规则 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class PositiveRuleServiceImpl implements PositiveRuleService {

    @Resource
    private PositiveRuleMapper positiveRuleMapper;

    @Override
    public Long createPositiveRule(PositiveRuleSaveReqVO createReqVO) {
        // 校验规则编码唯一
        validateRuleCodeUnique(null, createReqVO.getRuleCode());
        
        PositiveRuleDO positiveRule = BeanUtils.toBean(createReqVO, PositiveRuleDO.class);
        positiveRuleMapper.insert(positiveRule);
        return positiveRule.getId();
    }

    @Override
    public void updatePositiveRule(PositiveRuleSaveReqVO updateReqVO) {
        validatePositiveRuleExists(updateReqVO.getId());
        validateRuleCodeUnique(updateReqVO.getId(), updateReqVO.getRuleCode());
        
        PositiveRuleDO updateObj = BeanUtils.toBean(updateReqVO, PositiveRuleDO.class);
        positiveRuleMapper.updateById(updateObj);
    }

    @Override
    public void deletePositiveRule(Long id) {
        validatePositiveRuleExists(id);
        positiveRuleMapper.deleteById(id);
    }

    private void validatePositiveRuleExists(Long id) {
        if (positiveRuleMapper.selectById(id) == null) {
            throw exception(POSITIVE_RULE_NOT_EXISTS);
        }
    }

    private void validateRuleCodeUnique(Long id, String ruleCode) {
        PositiveRuleDO rule = positiveRuleMapper.selectByRuleCode(ruleCode);
        if (rule == null) {
            return;
        }
        if (id == null) {
            throw exception(POSITIVE_RULE_CODE_DUPLICATE);
        }
        if (!rule.getId().equals(id)) {
            throw exception(POSITIVE_RULE_CODE_DUPLICATE);
        }
    }

    @Override
    public PositiveRuleDO getPositiveRule(Long id) {
        return positiveRuleMapper.selectById(id);
    }

    @Override
    public PageResult<PositiveRuleDO> getPositiveRulePage(PositiveRulePageReqVO pageReqVO) {
        return positiveRuleMapper.selectPage(pageReqVO);
    }

    @Override
    public List<PositiveRuleDO> getPositiveRuleList(PositiveRuleListReqVO listReqVO) {
        return positiveRuleMapper.selectList(listReqVO);
    }

    @Override
    public List<PositiveRuleDO> getActiveRuleList() {
        return positiveRuleMapper.selectActiveList();
    }

}
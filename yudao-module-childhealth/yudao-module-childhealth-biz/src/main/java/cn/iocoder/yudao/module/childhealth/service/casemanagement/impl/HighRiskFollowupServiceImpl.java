package cn.iocoder.yudao.module.childhealth.service.casemanagement.impl;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.casemanagement.HighRiskFollowupDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.casemanagement.HighRiskFollowupMapper;
import cn.iocoder.yudao.module.childhealth.service.casemanagement.HighRiskFollowupService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.*;

/**
 * 高危儿随访 Service 实现类
 *
 * 模块: B.高危儿专案管理
 * 创建日期: 2026-07-20
 */
@Service
@Validated
public class HighRiskFollowupServiceImpl implements HighRiskFollowupService {

    @Resource
    private HighRiskFollowupMapper highRiskFollowupMapper;

    @Override
    public Long createHighRiskFollowup(Object createReqVO) {
        // TODO 后续替换为 HighRiskFollowupSaveReqVO
        HighRiskFollowupDO highRiskFollowup = BeanUtils.toBean(createReqVO, HighRiskFollowupDO.class);
        highRiskFollowupMapper.insert(highRiskFollowup);
        return highRiskFollowup.getId();
    }

    @Override
    public void updateHighRiskFollowup(Object updateReqVO) {
        // TODO 后续替换为 HighRiskFollowupSaveReqVO，并校验存在
        // validateHighRiskFollowupExists(updateReqVO.getId());
        HighRiskFollowupDO updateObj = BeanUtils.toBean(updateReqVO, HighRiskFollowupDO.class);
        highRiskFollowupMapper.updateById(updateObj);
    }

    @Override
    public void deleteHighRiskFollowup(Long id) {
        validateHighRiskFollowupExists(id);
        highRiskFollowupMapper.deleteById(id);
    }

    private void validateHighRiskFollowupExists(Long id) {
        if (highRiskFollowupMapper.selectById(id) == null) {
            throw exception(HIGH_RISK_FOLLOWUP_NOT_EXISTS);
        }
    }

    @Override
    public HighRiskFollowupDO getHighRiskFollowup(Long id) {
        return highRiskFollowupMapper.selectById(id);
    }

    @Override
    public PageResult<HighRiskFollowupDO> getHighRiskFollowupPage(PageParam pageParam) {
        // TODO 后续替换为 HighRiskFollowupPageReqVO，并实现分页查询条件
        return highRiskFollowupMapper.selectPage(pageParam, null);
    }

    @Override
    public List<HighRiskFollowupDO> selectListByNewbornId(Long newbornId) {
        return highRiskFollowupMapper.selectListByNewbornId(newbornId);
    }

}

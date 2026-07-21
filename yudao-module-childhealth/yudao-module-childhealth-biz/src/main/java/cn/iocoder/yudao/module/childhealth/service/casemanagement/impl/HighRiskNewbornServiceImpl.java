package cn.iocoder.yudao.module.childhealth.service.casemanagement.impl;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.casemanagement.HighRiskNewbornDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.casemanagement.HighRiskNewbornMapper;
import cn.iocoder.yudao.module.childhealth.service.casemanagement.HighRiskNewbornService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.*;

/**
 * 高危新生儿台账 Service 实现类
 *
 * 模块: B.高危儿专案管理
 * 创建日期: 2026-07-20
 */
@Service
@Validated
public class HighRiskNewbornServiceImpl implements HighRiskNewbornService {

    @Resource
    private HighRiskNewbornMapper highRiskNewbornMapper;

    @Override
    public Long createHighRiskNewborn(Object createReqVO) {
        // TODO 后续替换为 HighRiskNewbornSaveReqVO
        HighRiskNewbornDO highRiskNewborn = BeanUtils.toBean(createReqVO, HighRiskNewbornDO.class);
        highRiskNewbornMapper.insert(highRiskNewborn);
        return highRiskNewborn.getId();
    }

    @Override
    public void updateHighRiskNewborn(Object updateReqVO) {
        // TODO 后续替换为 HighRiskNewbornSaveReqVO，并校验存在
        // validateHighRiskNewbornExists(updateReqVO.getId());
        HighRiskNewbornDO updateObj = BeanUtils.toBean(updateReqVO, HighRiskNewbornDO.class);
        highRiskNewbornMapper.updateById(updateObj);
    }

    @Override
    public void deleteHighRiskNewborn(Long id) {
        validateHighRiskNewbornExists(id);
        highRiskNewbornMapper.deleteById(id);
    }

    private void validateHighRiskNewbornExists(Long id) {
        if (highRiskNewbornMapper.selectById(id) == null) {
            throw exception(HIGH_RISK_NEWBORN_NOT_EXISTS);
        }
    }

    @Override
    public HighRiskNewbornDO getHighRiskNewborn(Long id) {
        return highRiskNewbornMapper.selectById(id);
    }

    @Override
    public PageResult<HighRiskNewbornDO> getHighRiskNewbornPage(PageParam pageParam) {
        // TODO 后续替换为 HighRiskNewbornPageReqVO，并实现分页查询条件
        return highRiskNewbornMapper.selectPage(pageParam, null);
    }

    @Override
    public List<HighRiskNewbornDO> autoRemindFollowup() {
        // TODO 业务方法：未建册随访预警
        //  1. 查询已建册（isRegistered=1）且随访状态为未随访（followupStatus=0）的高危新生儿
        //  2. 根据建册日期与当前日期差，对比随访计划，筛选超期未随访的记录
        //  3. 可结合定时任务生成预警消息推送至责任医生
        return highRiskNewbornMapper.selectListByIsRegistered(1);
    }

}

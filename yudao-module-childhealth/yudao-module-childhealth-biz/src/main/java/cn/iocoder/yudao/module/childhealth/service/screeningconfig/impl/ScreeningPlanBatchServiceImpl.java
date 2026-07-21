package cn.iocoder.yudao.module.childhealth.service.screeningconfig.impl;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig.ScreeningPlanBatchDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.screeningconfig.ScreeningPlanBatchMapper;
import cn.iocoder.yudao.module.childhealth.service.screeningconfig.ScreeningPlanBatchService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.SCREENING_PLAN_BATCH_NOT_EXISTS;

/**
 * 体检方案与批次关联 Service 实现类
 *
 * 模块: C.五健筛查配置
 * 创建日期: 2026-07-20
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class ScreeningPlanBatchServiceImpl implements ScreeningPlanBatchService {

    @Resource
    private ScreeningPlanBatchMapper screeningPlanBatchMapper;

    @Override
    public Long createScreeningPlanBatch(Object saveReqVO) {
        // TODO 后续替换为 ScreeningPlanBatchSaveReqVO
        ScreeningPlanBatchDO planBatch = BeanUtils.toBean(saveReqVO, ScreeningPlanBatchDO.class);
        screeningPlanBatchMapper.insert(planBatch);
        return planBatch.getId();
    }

    @Override
    public void updateScreeningPlanBatch(Object saveReqVO) {
        // TODO 后续替换为 ScreeningPlanBatchSaveReqVO
        ScreeningPlanBatchDO updateObj = BeanUtils.toBean(saveReqVO, ScreeningPlanBatchDO.class);
        validateScreeningPlanBatchExists(updateObj.getId());
        screeningPlanBatchMapper.updateById(updateObj);
    }

    @Override
    public void deleteScreeningPlanBatch(Long id) {
        validateScreeningPlanBatchExists(id);
        screeningPlanBatchMapper.deleteById(id);
    }

    @Override
    public ScreeningPlanBatchDO getScreeningPlanBatch(Long id) {
        return screeningPlanBatchMapper.selectById(id);
    }

    @Override
    public PageResult<ScreeningPlanBatchDO> getScreeningPlanBatchPage(PageParam pageParam) {
        // TODO 后续替换为 ScreeningPlanBatchPageReqVO，并增加查询条件
        return screeningPlanBatchMapper.selectPage(pageParam, null);
    }

    @Override
    public List<ScreeningPlanBatchDO> selectListByPlanId(Long planId) {
        return screeningPlanBatchMapper.selectListByPlanId(planId);
    }

    @Override
    public List<ScreeningPlanBatchDO> selectListByBatchId(Long batchId) {
        return screeningPlanBatchMapper.selectListByBatchId(batchId);
    }

    private void validateScreeningPlanBatchExists(Long id) {
        if (id == null || screeningPlanBatchMapper.selectById(id) == null) {
            throw exception(SCREENING_PLAN_BATCH_NOT_EXISTS);
        }
    }

}

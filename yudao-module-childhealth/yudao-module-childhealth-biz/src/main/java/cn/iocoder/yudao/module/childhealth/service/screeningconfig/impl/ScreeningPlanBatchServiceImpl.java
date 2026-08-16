package cn.iocoder.yudao.module.childhealth.service.screeningconfig.impl;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.screeningconfig.vo.ScreeningPlanBatchPageReqVO;
import cn.iocoder.yudao.module.childhealth.controller.admin.screeningconfig.vo.ScreeningPlanBatchSaveReqVO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig.ScreeningPlanBatchDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.screeningconfig.ScreeningPlanBatchMapper;
import cn.iocoder.yudao.module.childhealth.service.screeningconfig.ScreeningPlanBatchService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.SCREENING_PLAN_BATCH_NOT_EXISTS;

/**
 * 体检方案与批次关联 Service 实现类
 *
 * 模块: C.五健筛查配置
 */
@Service
@Validated
@Slf4j
public class ScreeningPlanBatchServiceImpl implements ScreeningPlanBatchService {

    @Resource
    private ScreeningPlanBatchMapper screeningPlanBatchMapper;

    @Override
    public Long createScreeningPlanBatch(ScreeningPlanBatchSaveReqVO saveReqVO) {
        ScreeningPlanBatchDO planBatch = BeanUtils.toBean(saveReqVO, ScreeningPlanBatchDO.class);
        screeningPlanBatchMapper.insert(planBatch);
        return planBatch.getId();
    }

    @Override
    public void updateScreeningPlanBatch(ScreeningPlanBatchSaveReqVO saveReqVO) {
        validateScreeningPlanBatchExists(saveReqVO.getId());
        ScreeningPlanBatchDO updateObj = BeanUtils.toBean(saveReqVO, ScreeningPlanBatchDO.class);
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
    public PageResult<ScreeningPlanBatchDO> getScreeningPlanBatchPage(ScreeningPlanBatchPageReqVO pageReqVO) {
        return screeningPlanBatchMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ScreeningPlanBatchDO> selectListByPlanId(Long planId) {
        return screeningPlanBatchMapper.selectListByPlanId(planId);
    }

    @Override
    public List<ScreeningPlanBatchDO> selectListByBatchId(Long batchId) {
        return screeningPlanBatchMapper.selectListByBatchId(batchId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdateCompletionStatus(List<Long> ids, Integer status) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        for (Long id : ids) {
            validateScreeningPlanBatchExists(id);
            ScreeningPlanBatchDO updateObj = new ScreeningPlanBatchDO();
            updateObj.setId(id);
            updateObj.setCompletionStatus(status);
            screeningPlanBatchMapper.updateById(updateObj);
        }
    }

    @Override
    public Map<Integer, Long> statusStatisticsByBatch(Long batchId) {
        List<ScreeningPlanBatchDO> list = screeningPlanBatchMapper.selectListByBatchId(batchId);
        Map<Integer, Long> result = new HashMap<>();
        // 初始化三个状态
        result.put(0, 0L);
        result.put(1, 0L);
        result.put(2, 0L);
        for (ScreeningPlanBatchDO item : list) {
            Integer status = item.getCompletionStatus() == null ? 0 : item.getCompletionStatus();
            result.merge(status, 1L, Long::sum);
        }
        return result;
    }

    private void validateScreeningPlanBatchExists(Long id) {
        if (id == null || screeningPlanBatchMapper.selectById(id) == null) {
            throw exception(SCREENING_PLAN_BATCH_NOT_EXISTS);
        }
    }

}

package cn.iocoder.yudao.module.childhealth.service.screening;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.batch.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.ScreeningBatchDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.screening.ScreeningBatchMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.*;

/**
 * 筛查批次 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ScreeningBatchServiceImpl implements ScreeningBatchService {

    @Resource
    private ScreeningBatchMapper screeningBatchMapper;

    @Override
    public Long createScreeningBatch(ScreeningBatchSaveReqVO createReqVO) {
        // 校验批次编号唯一
        validateBatchNoUnique(null, createReqVO.getBatchNo());
        // 插入
        ScreeningBatchDO screeningBatch = BeanUtils.toBean(createReqVO, ScreeningBatchDO.class);
        screeningBatchMapper.insert(screeningBatch);
        return screeningBatch.getId();
    }

    @Override
    public void updateScreeningBatch(ScreeningBatchSaveReqVO updateReqVO) {
        // 校验存在
        validateScreeningBatchExists(updateReqVO.getId());
        // 校验批次编号唯一
        validateBatchNoUnique(updateReqVO.getId(), updateReqVO.getBatchNo());
        // 更新
        ScreeningBatchDO updateObj = BeanUtils.toBean(updateReqVO, ScreeningBatchDO.class);
        screeningBatchMapper.updateById(updateObj);
    }

    @Override
    public void deleteScreeningBatch(Long id) {
        // 校验存在
        validateScreeningBatchExists(id);
        // 删除
        screeningBatchMapper.deleteById(id);
    }

    private void validateScreeningBatchExists(Long id) {
        if (screeningBatchMapper.selectById(id) == null) {
            throw exception(SCREENING_BATCH_NOT_EXISTS);
        }
    }

    private void validateBatchNoUnique(Long id, String batchNo) {
        ScreeningBatchDO batch = screeningBatchMapper.selectByBatchNo(batchNo);
        if (batch == null) {
            return;
        }
        if (id == null) {
            throw exception(SCREENING_BATCH_NO_DUPLICATE);
        }
        if (!batch.getId().equals(id)) {
            throw exception(SCREENING_BATCH_NO_DUPLICATE);
        }
    }

    @Override
    public ScreeningBatchDO getScreeningBatch(Long id) {
        return screeningBatchMapper.selectById(id);
    }

    @Override
    public PageResult<ScreeningBatchDO> getScreeningBatchPage(ScreeningBatchPageReqVO pageReqVO) {
        return screeningBatchMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ScreeningBatchDO> getScreeningBatchList(ScreeningBatchListReqVO listReqVO) {
        return screeningBatchMapper.selectList(listReqVO);
    }

    @Override
    public void updateBatchStatus(Long id, Integer status) {
        // 校验存在
        validateScreeningBatchExists(id);
        // 更新状态
        ScreeningBatchDO updateObj = new ScreeningBatchDO();
        updateObj.setId(id);
        updateObj.setBatchStatus(status);
        screeningBatchMapper.updateById(updateObj);
    }

}
package cn.iocoder.yudao.module.childhealth.service.screening;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.positive.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.ScreeningPositiveDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.screening.ScreeningPositiveMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.*;

/**
 * 筛查阳性记录 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ScreeningPositiveServiceImpl implements ScreeningPositiveService {

    @Resource
    private ScreeningPositiveMapper screeningPositiveMapper;

    @Override
    public Long createScreeningPositive(ScreeningPositiveSaveReqVO createReqVO) {
        ScreeningPositiveDO screeningPositive = BeanUtils.toBean(createReqVO, ScreeningPositiveDO.class);
        screeningPositiveMapper.insert(screeningPositive);
        return screeningPositive.getId();
    }

    @Override
    public void updateScreeningPositive(ScreeningPositiveSaveReqVO updateReqVO) {
        validateScreeningPositiveExists(updateReqVO.getId());
        ScreeningPositiveDO updateObj = BeanUtils.toBean(updateReqVO, ScreeningPositiveDO.class);
        screeningPositiveMapper.updateById(updateObj);
    }

    @Override
    public void deleteScreeningPositive(Long id) {
        validateScreeningPositiveExists(id);
        screeningPositiveMapper.deleteById(id);
    }

    private void validateScreeningPositiveExists(Long id) {
        if (screeningPositiveMapper.selectById(id) == null) {
            throw exception(POSITIVE_RECORD_NOT_EXISTS);
        }
    }

    @Override
    public ScreeningPositiveDO getScreeningPositive(Long id) {
        return screeningPositiveMapper.selectById(id);
    }

    @Override
    public PageResult<ScreeningPositiveDO> getScreeningPositivePage(ScreeningPositivePageReqVO pageReqVO) {
        return screeningPositiveMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ScreeningPositiveDO> getScreeningPositiveByRecordId(Long recordId) {
        return screeningPositiveMapper.selectByRecordId(recordId);
    }

    @Override
    public void updateRecheckStatus(Long id, Integer recheckStatus) {
        validateScreeningPositiveExists(id);
        ScreeningPositiveDO updateObj = new ScreeningPositiveDO();
        updateObj.setId(id);
        updateObj.setRecheckStatus(recheckStatus);
        screeningPositiveMapper.updateById(updateObj);
    }

}
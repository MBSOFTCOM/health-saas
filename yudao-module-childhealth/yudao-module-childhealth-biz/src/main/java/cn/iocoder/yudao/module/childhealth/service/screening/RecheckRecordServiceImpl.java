package cn.iocoder.yudao.module.childhealth.service.screening;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.recheck.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.RecheckRecordDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.screening.RecheckRecordMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.*;

/**
 * 复筛记录 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class RecheckRecordServiceImpl implements RecheckRecordService {

    @Resource
    private RecheckRecordMapper recheckRecordMapper;

    @Override
    public Long createRecheckRecord(RecheckRecordSaveReqVO createReqVO) {
        // 校验阳性记录是否已有复筛记录
        RecheckRecordDO existRecord = recheckRecordMapper.selectByPositiveId(createReqVO.getPositiveId());
        if (existRecord != null) {
            throw exception(RECHECK_RECORD_ALREADY_EXISTS);
        }
        
        RecheckRecordDO recheckRecord = BeanUtils.toBean(createReqVO, RecheckRecordDO.class);
        recheckRecordMapper.insert(recheckRecord);
        return recheckRecord.getId();
    }

    @Override
    public void updateRecheckRecord(RecheckRecordSaveReqVO updateReqVO) {
        validateRecheckRecordExists(updateReqVO.getId());
        RecheckRecordDO updateObj = BeanUtils.toBean(updateReqVO, RecheckRecordDO.class);
        recheckRecordMapper.updateById(updateObj);
    }

    @Override
    public void deleteRecheckRecord(Long id) {
        validateRecheckRecordExists(id);
        recheckRecordMapper.deleteById(id);
    }

    private void validateRecheckRecordExists(Long id) {
        if (recheckRecordMapper.selectById(id) == null) {
            throw exception(RECHECK_RECORD_NOT_EXISTS);
        }
    }

    @Override
    public RecheckRecordDO getRecheckRecord(Long id) {
        return recheckRecordMapper.selectById(id);
    }

    @Override
    public PageResult<RecheckRecordDO> getRecheckRecordPage(RecheckRecordPageReqVO pageReqVO) {
        return recheckRecordMapper.selectPage(pageReqVO);
    }

    @Override
    public RecheckRecordDO getByPositiveId(Long positiveId) {
        return recheckRecordMapper.selectByPositiveId(positiveId);
    }

}
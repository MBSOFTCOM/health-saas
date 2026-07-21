package cn.iocoder.yudao.module.childhealth.service.referral;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.referral.vo.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.workflow.ReferralRecordDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.workflow.ReferralRecordMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.*;

/**
 * 转介管理 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ReferralRecordServiceImpl implements ReferralRecordService {

    @Resource
    private ReferralRecordMapper referralRecordMapper;

    @Override
    public Long createReferralRecord(ReferralRecordSaveReqVO createReqVO) {
        // 校验转介编号是否重复
        ReferralRecordDO existRecord = referralRecordMapper.selectByReferralNo(createReqVO.getReferralNo());
        if (existRecord != null) {
            throw exception(REFERRAL_NO_DUPLICATE);
        }
        
        ReferralRecordDO referralRecord = BeanUtils.toBean(createReqVO, ReferralRecordDO.class);
        referralRecordMapper.insert(referralRecord);
        return referralRecord.getId();
    }

    @Override
    public void updateReferralRecord(ReferralRecordSaveReqVO updateReqVO) {
        validateReferralRecordExists(updateReqVO.getId());
        // 校验转介编号是否重复（排除自身）
        ReferralRecordDO existRecord = referralRecordMapper.selectByReferralNo(updateReqVO.getReferralNo());
        if (existRecord != null && !existRecord.getId().equals(updateReqVO.getId())) {
            throw exception(REFERRAL_NO_DUPLICATE);
        }
        ReferralRecordDO updateObj = BeanUtils.toBean(updateReqVO, ReferralRecordDO.class);
        referralRecordMapper.updateById(updateObj);
    }

    @Override
    public void deleteReferralRecord(Long id) {
        validateReferralRecordExists(id);
        referralRecordMapper.deleteById(id);
    }

    private void validateReferralRecordExists(Long id) {
        if (referralRecordMapper.selectById(id) == null) {
            throw exception(REFERRAL_NOT_EXISTS);
        }
    }

    @Override
    public ReferralRecordDO getReferralRecord(Long id) {
        return referralRecordMapper.selectById(id);
    }

    @Override
    public PageResult<ReferralRecordDO> getReferralRecordPage(ReferralRecordPageReqVO pageReqVO) {
        return referralRecordMapper.selectPage(pageReqVO);
    }

}
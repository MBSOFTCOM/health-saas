package cn.iocoder.yudao.module.childhealth.service.device.impl;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.device.RecheckCheckinDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.device.RecheckCheckinMapper;
import cn.iocoder.yudao.module.childhealth.service.device.RecheckCheckinService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.RECHECK_CHECKIN_NO_DUPLICATE;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.RECHECK_CHECKIN_NOT_EXISTS;

/**
 * 复筛报到登记 Service 实现类
 *
 * 模块: D. 移动端功能补全
 * 创建日期: 2026-07-20
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class RecheckCheckinServiceImpl implements RecheckCheckinService {

    @Resource
    private RecheckCheckinMapper recheckCheckinMapper;

    @Override
    public Long createRecheckCheckin(Object saveReqVO) {
        // TODO 后续替换为 RecheckCheckinSaveReqVO
        RecheckCheckinDO record = BeanUtils.toBean(saveReqVO, RecheckCheckinDO.class);
        // 报到编号唯一性校验
        if (record.getCheckinNo() != null
                && recheckCheckinMapper.selectByCheckinNo(record.getCheckinNo()) != null) {
            throw exception(RECHECK_CHECKIN_NO_DUPLICATE);
        }
        recheckCheckinMapper.insert(record);
        return record.getId();
    }

    @Override
    public void updateRecheckCheckin(Object saveReqVO) {
        // TODO 后续替换为 RecheckCheckinSaveReqVO
        RecheckCheckinDO updateObj = BeanUtils.toBean(saveReqVO, RecheckCheckinDO.class);
        validateRecheckCheckinExists(updateObj.getId());
        // 报到编号唯一性校验
        if (updateObj.getCheckinNo() != null) {
            RecheckCheckinDO existing = recheckCheckinMapper.selectByCheckinNo(updateObj.getCheckinNo());
            if (existing != null && !existing.getId().equals(updateObj.getId())) {
                throw exception(RECHECK_CHECKIN_NO_DUPLICATE);
            }
        }
        recheckCheckinMapper.updateById(updateObj);
    }

    @Override
    public void deleteRecheckCheckin(Long id) {
        validateRecheckCheckinExists(id);
        recheckCheckinMapper.deleteById(id);
    }

    @Override
    public RecheckCheckinDO getRecheckCheckin(Long id) {
        return recheckCheckinMapper.selectById(id);
    }

    @Override
    public PageResult<RecheckCheckinDO> getRecheckCheckinPage(PageParam pageParam) {
        // TODO 后续替换为 RecheckCheckinPageReqVO，并增加查询条件
        return recheckCheckinMapper.selectPage(pageParam, null);
    }

    @Override
    public RecheckCheckinDO selectByCheckinNo(String checkinNo) {
        return recheckCheckinMapper.selectByCheckinNo(checkinNo);
    }

    @Override
    public RecheckCheckinDO checkinByQrcode(String qrcodeContent) {
        // TODO 实现扫码报到：根据 qrcodeContent 查询报到记录 -> 校验是否已报到 -> 回写报到时间/方式/现场状态
        log.info("[checkinByQrcode] 扫码报到 qrcodeContent={}", qrcodeContent);
        RecheckCheckinDO record = recheckCheckinMapper.selectByQrcodeContent(qrcodeContent);
        if (record == null) {
            throw exception(RECHECK_CHECKIN_NOT_EXISTS);
        }
        RecheckCheckinDO updateObj = new RecheckCheckinDO();
        updateObj.setId(record.getId());
        updateObj.setCheckinTime(LocalDateTime.now());
        updateObj.setCheckinMethod(1);
        updateObj.setOnSiteStatus(0);
        recheckCheckinMapper.updateById(updateObj);
        return recheckCheckinMapper.selectById(record.getId());
    }

    @Override
    public List<RecheckCheckinDO> selectListByOnSiteStatus(Integer onSiteStatus) {
        return recheckCheckinMapper.selectListByOnSiteStatus(onSiteStatus);
    }

    private void validateRecheckCheckinExists(Long id) {
        if (id == null || recheckCheckinMapper.selectById(id) == null) {
            throw exception(RECHECK_CHECKIN_NOT_EXISTS);
        }
    }

}

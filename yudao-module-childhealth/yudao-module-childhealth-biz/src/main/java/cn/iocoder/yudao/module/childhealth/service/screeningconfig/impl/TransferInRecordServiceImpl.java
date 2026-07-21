package cn.iocoder.yudao.module.childhealth.service.screeningconfig.impl;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig.TransferInRecordDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.screeningconfig.TransferInRecordMapper;
import cn.iocoder.yudao.module.childhealth.service.screeningconfig.TransferInRecordService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.TRANSFER_IN_RECORD_NO_DUPLICATE;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.TRANSFER_IN_RECORD_NOT_EXISTS;

/**
 * 转入档案 Service 实现类
 *
 * 模块: C.五健筛查配置
 * 创建日期: 2026-07-20
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class TransferInRecordServiceImpl implements TransferInRecordService {

    @Resource
    private TransferInRecordMapper transferInRecordMapper;

    @Override
    public Long createTransferInRecord(Object saveReqVO) {
        // TODO 后续替换为 TransferInRecordSaveReqVO
        TransferInRecordDO record = BeanUtils.toBean(saveReqVO, TransferInRecordDO.class);
        // 编号唯一性校验
        if (record.getTransferNo() != null
                && transferInRecordMapper.selectByTransferNo(record.getTransferNo()) != null) {
            throw exception(TRANSFER_IN_RECORD_NO_DUPLICATE);
        }
        transferInRecordMapper.insert(record);
        return record.getId();
    }

    @Override
    public void updateTransferInRecord(Object saveReqVO) {
        // TODO 后续替换为 TransferInRecordSaveReqVO
        TransferInRecordDO updateObj = BeanUtils.toBean(saveReqVO, TransferInRecordDO.class);
        validateTransferInRecordExists(updateObj.getId());
        transferInRecordMapper.updateById(updateObj);
    }

    @Override
    public void deleteTransferInRecord(Long id) {
        validateTransferInRecordExists(id);
        transferInRecordMapper.deleteById(id);
    }

    @Override
    public TransferInRecordDO getTransferInRecord(Long id) {
        return transferInRecordMapper.selectById(id);
    }

    @Override
    public PageResult<TransferInRecordDO> getTransferInRecordPage(PageParam pageParam) {
        // TODO 后续替换为 TransferInRecordPageReqVO，并增加查询条件
        return transferInRecordMapper.selectPage(pageParam, null);
    }

    @Override
    public TransferInRecordDO selectByTransferNo(String transferNo) {
        return transferInRecordMapper.selectByTransferNo(transferNo);
    }

    @Override
    public void receiveTransfer(Long id, Long receiveDoctorId) {
        // TODO 实现接诊转入：校验当前状态为待接诊，更新 receiveStatus=1、receiveDoctorId、receiveDate=今天
        log.info("[receiveTransfer] 接诊转入 id={} receiveDoctorId={}", id, receiveDoctorId);
        validateTransferInRecordExists(id);
        TransferInRecordDO updateObj = new TransferInRecordDO();
        updateObj.setId(id);
        updateObj.setReceiveStatus(1);
        updateObj.setReceiveDoctorId(receiveDoctorId);
        updateObj.setReceiveDate(LocalDate.now());
        transferInRecordMapper.updateById(updateObj);
    }

    private void validateTransferInRecordExists(Long id) {
        if (id == null || transferInRecordMapper.selectById(id) == null) {
            throw exception(TRANSFER_IN_RECORD_NOT_EXISTS);
        }
    }

}

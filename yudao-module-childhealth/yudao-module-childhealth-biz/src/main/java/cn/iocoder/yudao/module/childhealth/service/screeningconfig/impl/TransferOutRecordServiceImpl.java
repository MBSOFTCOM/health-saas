package cn.iocoder.yudao.module.childhealth.service.screeningconfig.impl;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig.TransferOutRecordDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.screeningconfig.TransferOutRecordMapper;
import cn.iocoder.yudao.module.childhealth.service.screeningconfig.TransferOutRecordService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.TRANSFER_OUT_RECORD_NO_DUPLICATE;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.TRANSFER_OUT_RECORD_NOT_EXISTS;

/**
 * 转出档案 Service 实现类
 *
 * 模块: C.五健筛查配置
 * 创建日期: 2026-07-20
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class TransferOutRecordServiceImpl implements TransferOutRecordService {

    @Resource
    private TransferOutRecordMapper transferOutRecordMapper;

    @Override
    public Long createTransferOutRecord(Object saveReqVO) {
        // TODO 后续替换为 TransferOutRecordSaveReqVO
        TransferOutRecordDO record = BeanUtils.toBean(saveReqVO, TransferOutRecordDO.class);
        // 编号唯一性校验
        if (record.getTransferNo() != null
                && transferOutRecordMapper.selectByTransferNo(record.getTransferNo()) != null) {
            throw exception(TRANSFER_OUT_RECORD_NO_DUPLICATE);
        }
        transferOutRecordMapper.insert(record);
        return record.getId();
    }

    @Override
    public void updateTransferOutRecord(Object saveReqVO) {
        // TODO 后续替换为 TransferOutRecordSaveReqVO
        TransferOutRecordDO updateObj = BeanUtils.toBean(saveReqVO, TransferOutRecordDO.class);
        validateTransferOutRecordExists(updateObj.getId());
        transferOutRecordMapper.updateById(updateObj);
    }

    @Override
    public void deleteTransferOutRecord(Long id) {
        validateTransferOutRecordExists(id);
        transferOutRecordMapper.deleteById(id);
    }

    @Override
    public TransferOutRecordDO getTransferOutRecord(Long id) {
        return transferOutRecordMapper.selectById(id);
    }

    @Override
    public PageResult<TransferOutRecordDO> getTransferOutRecordPage(PageParam pageParam) {
        // TODO 后续替换为 TransferOutRecordPageReqVO，并增加查询条件
        return transferOutRecordMapper.selectPage(pageParam, null);
    }

    @Override
    public TransferOutRecordDO selectByTransferNo(String transferNo) {
        return transferOutRecordMapper.selectByTransferNo(transferNo);
    }

    @Override
    public void archiveTransferOut(Long id) {
        // TODO 实现归档转出：生成档案归档文件（PDF/打包）、上传至对象存储、回写 archiveUrl 与 followupStatus=2已结案
        log.info("[archiveTransferOut] 归档转出 id={}", id);
        validateTransferOutRecordExists(id);
        TransferOutRecordDO updateObj = new TransferOutRecordDO();
        updateObj.setId(id);
        updateObj.setFollowupStatus(2);
        transferOutRecordMapper.updateById(updateObj);
    }

    private void validateTransferOutRecordExists(Long id) {
        if (id == null || transferOutRecordMapper.selectById(id) == null) {
            throw exception(TRANSFER_OUT_RECORD_NOT_EXISTS);
        }
    }

}

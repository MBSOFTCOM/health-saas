package cn.iocoder.yudao.module.childhealth.service.transfer;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.transfer.vo.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.workflow.TransferArchiveDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.workflow.TransferArchiveMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.*;

/**
 * 档案转递 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class TransferArchiveServiceImpl implements TransferArchiveService {

    @Resource
    private TransferArchiveMapper transferArchiveMapper;

    @Override
    public Long createTransferArchive(TransferArchiveSaveReqVO createReqVO) {
        // 校验转递编号是否重复
        TransferArchiveDO existRecord = transferArchiveMapper.selectByTransferNo(createReqVO.getTransferNo());
        if (existRecord != null) {
            throw exception(TRANSFER_ARCHIVE_NO_DUPLICATE);
        }
        
        TransferArchiveDO transferArchive = BeanUtils.toBean(createReqVO, TransferArchiveDO.class);
        transferArchiveMapper.insert(transferArchive);
        return transferArchive.getId();
    }

    @Override
    public void updateTransferArchive(TransferArchiveSaveReqVO updateReqVO) {
        validateTransferArchiveExists(updateReqVO.getId());
        // 校验转递编号是否重复（排除自身）
        TransferArchiveDO existRecord = transferArchiveMapper.selectByTransferNo(updateReqVO.getTransferNo());
        if (existRecord != null && !existRecord.getId().equals(updateReqVO.getId())) {
            throw exception(TRANSFER_ARCHIVE_NO_DUPLICATE);
        }
        TransferArchiveDO updateObj = BeanUtils.toBean(updateReqVO, TransferArchiveDO.class);
        transferArchiveMapper.updateById(updateObj);
    }

    @Override
    public void deleteTransferArchive(Long id) {
        validateTransferArchiveExists(id);
        transferArchiveMapper.deleteById(id);
    }

    private void validateTransferArchiveExists(Long id) {
        if (transferArchiveMapper.selectById(id) == null) {
            throw exception(TRANSFER_ARCHIVE_NOT_EXISTS);
        }
    }

    @Override
    public TransferArchiveDO getTransferArchive(Long id) {
        return transferArchiveMapper.selectById(id);
    }

    @Override
    public PageResult<TransferArchiveDO> getTransferArchivePage(TransferArchivePageReqVO pageReqVO) {
        return transferArchiveMapper.selectPage(pageReqVO);
    }

}
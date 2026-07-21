package cn.iocoder.yudao.module.childhealth.service.transfer;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.controller.admin.transfer.vo.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.workflow.TransferArchiveDO;

import jakarta.validation.Valid;

/**
 * 档案转递 Service 接口
 *
 * @author 芋道源码
 */
public interface TransferArchiveService {

    /**
     * 创建档案转递记录
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createTransferArchive(@Valid TransferArchiveSaveReqVO createReqVO);

    /**
     * 更新档案转递记录
     *
     * @param updateReqVO 更新信息
     */
    void updateTransferArchive(@Valid TransferArchiveSaveReqVO updateReqVO);

    /**
     * 删除档案转递记录
     *
     * @param id 编号
     */
    void deleteTransferArchive(Long id);

    /**
     * 获得档案转递记录
     *
     * @param id 编号
     * @return 档案转递记录
     */
    TransferArchiveDO getTransferArchive(Long id);

    /**
     * 获得档案转递记录分页
     *
     * @param pageReqVO 分页查询
     * @return 档案转递记录分页
     */
    PageResult<TransferArchiveDO> getTransferArchivePage(TransferArchivePageReqVO pageReqVO);

}
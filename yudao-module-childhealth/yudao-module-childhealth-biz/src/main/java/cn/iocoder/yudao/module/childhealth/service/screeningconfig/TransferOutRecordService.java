package cn.iocoder.yudao.module.childhealth.service.screeningconfig;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig.TransferOutRecordDO;

/**
 * 转出档案 Service 接口
 *
 * 模块: C.五健筛查配置
 * 创建日期: 2026-07-20
 *
 * @author 芋道源码
 */
public interface TransferOutRecordService {

    /**
     * 创建转出档案
     *
     * @param saveReqVO 创建信息（后续替换为 TransferOutRecordSaveReqVO）
     * @return 编号
     */
    Long createTransferOutRecord(Object saveReqVO);

    /**
     * 更新转出档案
     *
     * @param saveReqVO 更新信息（后续替换为 TransferOutRecordSaveReqVO）
     */
    void updateTransferOutRecord(Object saveReqVO);

    /**
     * 删除转出档案
     *
     * @param id 编号
     */
    void deleteTransferOutRecord(Long id);

    /**
     * 获得转出档案
     *
     * @param id 编号
     * @return 转出档案
     */
    TransferOutRecordDO getTransferOutRecord(Long id);

    /**
     * 获得转出档案分页
     *
     * @param pageParam 分页查询（后续替换为 TransferOutRecordPageReqVO）
     * @return 转出档案分页
     */
    PageResult<TransferOutRecordDO> getTransferOutRecordPage(PageParam pageParam);

    /**
     * 按转出编号查询
     *
     * @param transferNo 转出编号
     * @return 转出档案
     */
    TransferOutRecordDO selectByTransferNo(String transferNo);

    /**
     * 归档转出：生成归档文件、回写 archiveUrl 与跟踪状态
     *
     * @param id 转出档案ID
     */
    void archiveTransferOut(Long id);

}

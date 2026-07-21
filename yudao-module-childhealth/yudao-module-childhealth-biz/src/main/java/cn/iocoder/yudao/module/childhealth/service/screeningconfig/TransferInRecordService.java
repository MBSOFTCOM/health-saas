package cn.iocoder.yudao.module.childhealth.service.screeningconfig;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig.TransferInRecordDO;

/**
 * 转入档案 Service 接口
 *
 * 模块: C.五健筛查配置
 * 创建日期: 2026-07-20
 *
 * @author 芋道源码
 */
public interface TransferInRecordService {

    /**
     * 创建转入档案
     *
     * @param saveReqVO 创建信息（后续替换为 TransferInRecordSaveReqVO）
     * @return 编号
     */
    Long createTransferInRecord(Object saveReqVO);

    /**
     * 更新转入档案
     *
     * @param saveReqVO 更新信息（后续替换为 TransferInRecordSaveReqVO）
     */
    void updateTransferInRecord(Object saveReqVO);

    /**
     * 删除转入档案
     *
     * @param id 编号
     */
    void deleteTransferInRecord(Long id);

    /**
     * 获得转入档案
     *
     * @param id 编号
     * @return 转入档案
     */
    TransferInRecordDO getTransferInRecord(Long id);

    /**
     * 获得转入档案分页
     *
     * @param pageParam 分页查询（后续替换为 TransferInRecordPageReqVO）
     * @return 转入档案分页
     */
    PageResult<TransferInRecordDO> getTransferInRecordPage(PageParam pageParam);

    /**
     * 按转入编号查询
     *
     * @param transferNo 转入编号
     * @return 转入档案
     */
    TransferInRecordDO selectByTransferNo(String transferNo);

    /**
     * 接诊转入：更新接诊状态、接诊医生、接诊日期等
     *
     * @param id 转入档案ID
     * @param receiveDoctorId 接诊医生ID
     */
    void receiveTransfer(Long id, Long receiveDoctorId);

}

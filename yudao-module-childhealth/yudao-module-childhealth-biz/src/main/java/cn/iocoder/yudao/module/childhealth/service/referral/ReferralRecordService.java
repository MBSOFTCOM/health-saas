package cn.iocoder.yudao.module.childhealth.service.referral;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.controller.admin.referral.vo.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.workflow.ReferralRecordDO;

import jakarta.validation.Valid;

/**
 * 转介管理 Service 接口
 *
 * @author 芋道源码
 */
public interface ReferralRecordService {

    /**
     * 创建转介记录
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createReferralRecord(@Valid ReferralRecordSaveReqVO createReqVO);

    /**
     * 更新转介记录
     *
     * @param updateReqVO 更新信息
     */
    void updateReferralRecord(@Valid ReferralRecordSaveReqVO updateReqVO);

    /**
     * 删除转介记录
     *
     * @param id 编号
     */
    void deleteReferralRecord(Long id);

    /**
     * 获得转介记录
     *
     * @param id 编号
     * @return 转介记录
     */
    ReferralRecordDO getReferralRecord(Long id);

    /**
     * 获得转介记录分页
     *
     * @param pageReqVO 分页查询
     * @return 转介记录分页
     */
    PageResult<ReferralRecordDO> getReferralRecordPage(ReferralRecordPageReqVO pageReqVO);

}
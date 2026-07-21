package cn.iocoder.yudao.module.childhealth.service.screening;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.recheck.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.RecheckRecordDO;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 复筛记录 Service 接口
 *
 * @author 芋道源码
 */
public interface RecheckRecordService {

    /**
     * 创建复筛记录
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createRecheckRecord(@Valid RecheckRecordSaveReqVO createReqVO);

    /**
     * 更新复筛记录
     *
     * @param updateReqVO 更新信息
     */
    void updateRecheckRecord(@Valid RecheckRecordSaveReqVO updateReqVO);

    /**
     * 删除复筛记录
     *
     * @param id 编号
     */
    void deleteRecheckRecord(Long id);

    /**
     * 获得复筛记录
     *
     * @param id 编号
     * @return 复筛记录
     */
    RecheckRecordDO getRecheckRecord(Long id);

    /**
     * 获得复筛记录分页
     *
     * @param pageReqVO 分页查询
     * @return 复筛记录分页
     */
    PageResult<RecheckRecordDO> getRecheckRecordPage(RecheckRecordPageReqVO pageReqVO);

    /**
     * 根据阳性记录ID获取复筛记录
     *
     * @param positiveId 阳性记录ID
     * @return 复筛记录
     */
    RecheckRecordDO getByPositiveId(Long positiveId);

}
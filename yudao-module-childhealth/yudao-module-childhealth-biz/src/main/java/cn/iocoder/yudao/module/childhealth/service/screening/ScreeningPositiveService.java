package cn.iocoder.yudao.module.childhealth.service.screening;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.positive.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.ScreeningPositiveDO;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 筛查阳性记录 Service 接口
 *
 * @author 芋道源码
 */
public interface ScreeningPositiveService {

    /**
     * 创建筛查阳性记录
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createScreeningPositive(@Valid ScreeningPositiveSaveReqVO createReqVO);

    /**
     * 更新筛查阳性记录
     *
     * @param updateReqVO 更新信息
     */
    void updateScreeningPositive(@Valid ScreeningPositiveSaveReqVO updateReqVO);

    /**
     * 删除筛查阳性记录
     *
     * @param id 编号
     */
    void deleteScreeningPositive(Long id);

    /**
     * 获得筛查阳性记录
     *
     * @param id 编号
     * @return 筛查阳性记录
     */
    ScreeningPositiveDO getScreeningPositive(Long id);

    /**
     * 获得筛查阳性记录分页
     *
     * @param pageReqVO 分页查询
     * @return 筛查阳性记录分页
     */
    PageResult<ScreeningPositiveDO> getScreeningPositivePage(ScreeningPositivePageReqVO pageReqVO);

    /**
     * 获得筛查阳性记录列表（根据筛查记录ID）
     *
     * @param recordId 筛查记录ID
     * @return 筛查阳性记录列表
     */
    List<ScreeningPositiveDO> getScreeningPositiveByRecordId(Long recordId);

    /**
     * 更新复筛状态
     *
     * @param id 编号
     * @param recheckStatus 复筛状态
     */
    void updateRecheckStatus(Long id, Integer recheckStatus);

}
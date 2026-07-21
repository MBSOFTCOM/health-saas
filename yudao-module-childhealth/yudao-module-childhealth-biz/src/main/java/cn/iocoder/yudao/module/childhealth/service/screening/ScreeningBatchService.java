package cn.iocoder.yudao.module.childhealth.service.screening;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.batch.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.ScreeningBatchDO;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 筛查批次 Service 接口
 *
 * @author 芋道源码
 */
public interface ScreeningBatchService {

    /**
     * 创建筛查批次
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createScreeningBatch(@Valid ScreeningBatchSaveReqVO createReqVO);

    /**
     * 更新筛查批次
     *
     * @param updateReqVO 更新信息
     */
    void updateScreeningBatch(@Valid ScreeningBatchSaveReqVO updateReqVO);

    /**
     * 删除筛查批次
     *
     * @param id 编号
     */
    void deleteScreeningBatch(Long id);

    /**
     * 获得筛查批次
     *
     * @param id 编号
     * @return 筛查批次
     */
    ScreeningBatchDO getScreeningBatch(Long id);

    /**
     * 获得筛查批次分页
     *
     * @param pageReqVO 分页查询
     * @return 筛查批次分页
     */
    PageResult<ScreeningBatchDO> getScreeningBatchPage(ScreeningBatchPageReqVO pageReqVO);

    /**
     * 获得筛查批次列表
     *
     * @param listReqVO 列表查询
     * @return 筛查批次列表
     */
    List<ScreeningBatchDO> getScreeningBatchList(ScreeningBatchListReqVO listReqVO);

    /**
     * 更新筛查批次状态
     *
     * @param id 编号
     * @param status 状态
     */
    void updateBatchStatus(Long id, Integer status);

}
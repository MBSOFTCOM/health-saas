package cn.iocoder.yudao.module.childhealth.service.screeningconfig;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.controller.admin.screeningconfig.vo.ScreeningPlanBatchPageReqVO;
import cn.iocoder.yudao.module.childhealth.controller.admin.screeningconfig.vo.ScreeningPlanBatchSaveReqVO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig.ScreeningPlanBatchDO;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 体检方案与批次关联 Service 接口
 *
 * 模块: C.五健筛查配置
 */
public interface ScreeningPlanBatchService {

    /**
     * 创建方案批次关联
     */
    Long createScreeningPlanBatch(@Valid ScreeningPlanBatchSaveReqVO saveReqVO);

    /**
     * 更新方案批次关联
     */
    void updateScreeningPlanBatch(@Valid ScreeningPlanBatchSaveReqVO saveReqVO);

    /**
     * 删除方案批次关联
     */
    void deleteScreeningPlanBatch(Long id);

    /**
     * 获得方案批次关联
     */
    ScreeningPlanBatchDO getScreeningPlanBatch(Long id);

    /**
     * 获得方案批次关联分页
     */
    PageResult<ScreeningPlanBatchDO> getScreeningPlanBatchPage(ScreeningPlanBatchPageReqVO pageReqVO);

    /**
     * 按方案ID查询所有关联批次
     */
    List<ScreeningPlanBatchDO> selectListByPlanId(Long planId);

    /**
     * 按批次ID查询所有关联方案（执行单元列表）
     */
    List<ScreeningPlanBatchDO> selectListByBatchId(Long batchId);

    /**
     * 批量更新完成状态（统一管理用）
     *
     * @param ids    执行单元ID列表
     * @param status 完成状态 0未开始 1进行中 2已完成
     */
    void batchUpdateCompletionStatus(List<Long> ids, Integer status);

    /**
     * 按批次统计各完成状态数量
     *
     * @param batchId 批次ID
     * @return key=completionStatus, value=count
     */
    Map<Integer, Long> statusStatisticsByBatch(Long batchId);

}

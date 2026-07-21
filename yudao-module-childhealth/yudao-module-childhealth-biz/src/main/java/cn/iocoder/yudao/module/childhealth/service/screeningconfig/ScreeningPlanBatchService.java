package cn.iocoder.yudao.module.childhealth.service.screeningconfig;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig.ScreeningPlanBatchDO;

import java.util.List;

/**
 * 体检方案与批次关联 Service 接口
 *
 * 模块: C.五健筛查配置
 * 创建日期: 2026-07-20
 *
 * @author 芋道源码
 */
public interface ScreeningPlanBatchService {

    /**
     * 创建方案批次关联
     *
     * @param saveReqVO 创建信息（后续替换为 ScreeningPlanBatchSaveReqVO）
     * @return 编号
     */
    Long createScreeningPlanBatch(Object saveReqVO);

    /**
     * 更新方案批次关联
     *
     * @param saveReqVO 更新信息（后续替换为 ScreeningPlanBatchSaveReqVO）
     */
    void updateScreeningPlanBatch(Object saveReqVO);

    /**
     * 删除方案批次关联
     *
     * @param id 编号
     */
    void deleteScreeningPlanBatch(Long id);

    /**
     * 获得方案批次关联
     *
     * @param id 编号
     * @return 方案批次关联
     */
    ScreeningPlanBatchDO getScreeningPlanBatch(Long id);

    /**
     * 获得方案批次关联分页
     *
     * @param pageParam 分页查询（后续替换为 ScreeningPlanBatchPageReqVO）
     * @return 方案批次关联分页
     */
    PageResult<ScreeningPlanBatchDO> getScreeningPlanBatchPage(PageParam pageParam);

    /**
     * 按方案ID查询所有关联批次
     *
     * @param planId 方案ID
     * @return 关联批次列表
     */
    List<ScreeningPlanBatchDO> selectListByPlanId(Long planId);

    /**
     * 按批次ID查询所有关联方案
     *
     * @param batchId 批次ID
     * @return 关联方案列表
     */
    List<ScreeningPlanBatchDO> selectListByBatchId(Long batchId);

}

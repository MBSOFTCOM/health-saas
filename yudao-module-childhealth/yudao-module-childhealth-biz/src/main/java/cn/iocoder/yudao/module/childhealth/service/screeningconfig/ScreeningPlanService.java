package cn.iocoder.yudao.module.childhealth.service.screeningconfig;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig.ScreeningPlanDO;

import java.util.List;

/**
 * 体检方案配置 Service 接口
 *
 * 模块: C.五健筛查配置
 * 创建日期: 2026-07-20
 *
 * @author 芋道源码
 */
public interface ScreeningPlanService {

    /**
     * 创建体检方案
     *
     * @param saveReqVO 创建信息（后续替换为 ScreeningPlanSaveReqVO）
     * @return 编号
     */
    Long createScreeningPlan(Object saveReqVO);

    /**
     * 更新体检方案
     *
     * @param saveReqVO 更新信息（后续替换为 ScreeningPlanSaveReqVO）
     */
    void updateScreeningPlan(Object saveReqVO);

    /**
     * 删除体检方案
     *
     * @param id 编号
     */
    void deleteScreeningPlan(Long id);

    /**
     * 获得体检方案
     *
     * @param id 编号
     * @return 体检方案
     */
    ScreeningPlanDO getScreeningPlan(Long id);

    /**
     * 获得体检方案分页
     *
     * @param pageParam 分页查询（后续替换为 ScreeningPlanPageReqVO）
     * @return 体检方案分页
     */
    PageResult<ScreeningPlanDO> getScreeningPlanPage(PageParam pageParam);

    /**
     * 按方案编码查询
     *
     * @param planCode 方案编码
     * @return 体检方案
     */
    ScreeningPlanDO selectByCode(String planCode);

    /**
     * 获取指定类型的默认方案
     *
     * @param planType 方案类型
     * @return 默认体检方案
     */
    ScreeningPlanDO selectDefaultPlan(Integer planType);

    /**
     * 查询所有启用方案
     *
     * @return 启用方案列表
     */
    List<ScreeningPlanDO> selectActiveList();

}

package cn.iocoder.yudao.module.childhealth.service.screeningconfig;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.controller.admin.screeningconfig.vo.ScreeningPlanPageReqVO;
import cn.iocoder.yudao.module.childhealth.controller.admin.screeningconfig.vo.ScreeningPlanSaveReqVO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig.ScreeningPlanDO;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 体检方案配置 Service 接口
 *
 * 模块: C.五健筛查配置
 */
public interface ScreeningPlanService {

    /**
     * 创建体检方案
     */
    Long createScreeningPlan(@Valid ScreeningPlanSaveReqVO saveReqVO);

    /**
     * 更新体检方案
     */
    void updateScreeningPlan(@Valid ScreeningPlanSaveReqVO saveReqVO);

    /**
     * 删除体检方案
     */
    void deleteScreeningPlan(Long id);

    /**
     * 获得体检方案
     */
    ScreeningPlanDO getScreeningPlan(Long id);

    /**
     * 获得体检方案分页
     */
    PageResult<ScreeningPlanDO> getScreeningPlanPage(ScreeningPlanPageReqVO pageReqVO);

    /**
     * 按方案编码查询
     */
    ScreeningPlanDO selectByCode(String planCode);

    /**
     * 获取指定类型的默认方案
     */
    ScreeningPlanDO selectDefaultPlan(Integer planType);

    /**
     * 设置默认方案（同类型互斥：同 plan_type 下仅一个 default_plan=1）
     *
     * @param id       方案ID
     * @param planType 方案类型
     */
    void setDefaultPlan(Long id, Integer planType);

    /**
     * 查询所有启用方案
     */
    List<ScreeningPlanDO> selectActiveList();

}

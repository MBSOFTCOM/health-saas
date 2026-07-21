package cn.iocoder.yudao.module.childhealth.service.screening;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.item.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.ScreeningItemConfigDO;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 筛查项目配置 Service 接口
 *
 * @author 芋道源码
 */
public interface ScreeningItemConfigService {

    /**
     * 创建筛查项目配置
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createScreeningItemConfig(@Valid ScreeningItemConfigSaveReqVO createReqVO);

    /**
     * 更新筛查项目配置
     *
     * @param updateReqVO 更新信息
     */
    void updateScreeningItemConfig(@Valid ScreeningItemConfigSaveReqVO updateReqVO);

    /**
     * 删除筛查项目配置
     *
     * @param id 编号
     */
    void deleteScreeningItemConfig(Long id);

    /**
     * 获得筛查项目配置
     *
     * @param id 编号
     * @return 筛查项目配置
     */
    ScreeningItemConfigDO getScreeningItemConfig(Long id);

    /**
     * 获得筛查项目配置分页
     *
     * @param pageReqVO 分页查询
     * @return 筛查项目配置分页
     */
    PageResult<ScreeningItemConfigDO> getScreeningItemConfigPage(ScreeningItemConfigPageReqVO pageReqVO);

    /**
     * 获得筛查项目配置列表
     *
     * @param listReqVO 列表查询
     * @return 筛查项目配置列表
     */
    List<ScreeningItemConfigDO> getScreeningItemConfigList(ScreeningItemConfigListReqVO listReqVO);

    /**
     * 根据类别获取筛查项目配置
     *
     * @param category 类别
     * @return 筛查项目配置列表
     */
    List<ScreeningItemConfigDO> getByCategory(String category);

    /**
     * 获取所有启用的筛查项目配置
     *
     * @return 筛查项目配置列表
     */
    List<ScreeningItemConfigDO> getActiveList();

}
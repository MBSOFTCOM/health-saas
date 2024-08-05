package cn.iocoder.yudao.module.ppd.service.screenchestradiograph;


import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.ppd.controller.admin.screenchestradiograph.vo.*;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenchestradiograph.ScreenChestRadiographDO;
import jakarta.validation.Valid;

import java.util.List;

/**
 * ct、dr组 Service 接口
 *
 * @author 芋道源码
 */
public interface ScreenChestRadiographService {

    /**
     * 创建ct、dr组
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createScreenChestRadiograph(@Valid ScreenChestRadiographSaveReqVO createReqVO);

    /**
     * 更新ct、dr组
     *
     * @param updateReqVO 更新信息
     */
    void updateScreenChestRadiograph(@Valid ScreenChestRadiographSaveReqVO updateReqVO);

    /**
     * 删除ct、dr组
     *
     * @param id 编号
     */
    void deleteScreenChestRadiograph(Long id);

    /**
     * 获得ct、dr组
     *
     * @param id 编号
     * @return ct、dr组
     */
    ScreenChestRadiographDO getScreenChestRadiograph(Long id);

    ChestRadiographStatistics getTimeStatistics(Integer screenType);
    /**
     * 获得ct、dr组分页
     *
     * @param pageReqVO 分页查询
     * @return ct、dr组分页
     */
    PageResult<ScreenChestRadiographPersonRespVO> getScreenChestRadiographPage(ScreenChestRadiographPersonPageReqVO pageReqVO);

    List<ChestRadiographOrderRespVO> getOrderAndTime(Long personId);
    ScreenChestRadiographRespVO getOneByPersonId(Long personId,Integer screenOrder,Integer screenType);

    /**
     * 根据患者id查询最大筛查次数
     * @param personId 患者id
     * @return 最大筛查次数
     */
    Integer getMaxOrder(Long personId,Integer screenType);
}
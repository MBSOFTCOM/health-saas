package cn.iocoder.yudao.module.ppd.service.screencomputedtomography;


import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.ppd.controller.admin.screencomputedtomography.vo.*;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screencomputedtomography.ScreenComputedTomographyDO;
import jakarta.validation.Valid;

import java.util.List;

/**
 * ct、dr组 Service 接口
 *
 * @author 芋道源码
 */
public interface ScreenComputedTomographyService {

    /**
     * 创建ct、dr组
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createScreenComputedTomography(@Valid ScreenComputedTomographySaveReqVO createReqVO);

    /**
     * 更新ct、dr组
     *
     * @param updateReqVO 更新信息
     */
    void updateScreenComputedTomography(@Valid ScreenComputedTomographySaveReqVO updateReqVO);

    /**
     * 删除ct、dr组
     *
     * @param id 编号
     */
    void deleteScreenComputedTomography(Long id);

    /**
     * 获得ct、dr组
     *
     * @param id 编号
     * @return ct、dr组
     */
    ScreenComputedTomographyDO getScreenComputedTomography(Long id);

    ComputedTomographyStatistics getTimeStatistics(Integer screenType);
    /**
     * 获得ct、dr组分页
     *
     * @param pageReqVO 分页查询
     * @return ct、dr组分页
     */
    PageResult<ScreenComputedTomographyPersonRespVO> getScreenComputedTomographyPage(ScreenComputedTomographyPersonPageReqVO pageReqVO);

    List<ComputedTomographyOrderRespVO> getOrderAndTime(Long personId, Integer screenType, Integer year);
    ScreenComputedTomographyRespVO getOneByPersonId(Long personId, Integer screenOrder, Integer screenType,Integer year);

    Integer getOutcomeByOrder(ScreenComputedTomographyPageReqVO reqVO);

    /**
     * 根据患者id查询最大筛查次数
     * @param personId 患者id
     * @return 最大筛查次数
     */
    Integer getMaxOrder(Long personId,Integer screenType,Integer year);
}
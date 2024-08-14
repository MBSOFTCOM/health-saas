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
     * 创建ct组
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createScreenComputedTomography(@Valid ScreenComputedTomographySaveReqVO createReqVO);
    /**
     * 创建ct组事务处理
     *
     * @param createReqVO 创建信息
     */
    void createScreenComputedTomographyTrans(@Valid ScreenComputedTomographySaveReqVO createReqVO);

    /**
     * 更新ct组
     *
     * @param updateReqVO 更新信息
     */
    void updateScreenComputedTomography(@Valid ScreenComputedTomographySaveReqVO updateReqVO);    /**
     * 更新ct组
     *
     * @param updateReqVO 更新信息
     */
    void updateScreenComputedTomographyTrans(@Valid ScreenComputedTomographySaveReqVO updateReqVO);

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
    ScreenComputedTomographyRespVO getOneByPersonIdNum(String idNum, Integer screenOrder, Integer screenType,Integer year);

    Integer getOutcomeByOrder(ScreenComputedTomographyPageReqVO reqVO);

    /**
     * 根据患者id查询最大筛查次数
     * @param idNum 患者身份证
     * @return 最大筛查次数
     */
    Integer getMaxOrder(String idNum,Integer screenType,Integer year);
}
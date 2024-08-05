package cn.iocoder.yudao.module.ppd.dal.mysql.screenchestradiograph;


import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ppd.controller.admin.screenchestradiograph.vo.*;
import cn.iocoder.yudao.module.ppd.controller.admin.screendiagnosis.vo.TBHealthScreening;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenchestradiograph.ScreenChestRadiographDO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ct、dr组 Mapper
 *
 * @author 芋道源码
 */
@Mapper
@InterceptorIgnore(tenantLine = "true")
public interface ScreenChestRadiographMapper extends BaseMapperX<ScreenChestRadiographDO> {

    List<ScreenChestRadiographPersonRespVO> selectScreenChestRadiographPage(ScreenChestRadiographPersonPageReqVO reqVO);
    Long countScreenChestRadiographPage(ScreenChestRadiographPersonPageReqVO reqVO);

    List<ChestRadiographOrderRespVO> getOrderAndTime(Long personId);

    Integer getMaxOrder(@Param("personId") Long personId,@Param("screenType") Integer screenType);

    ScreenChestRadiographRespVO getOneByPersonId(@Param("personId") Long personId, @Param("screenOrder") Integer screenOrder, @Param("screenType") Integer screenType);

    ChestRadiographStatistics getTypeStatistics(@Param("screenType") Integer screenType);
    Integer getYearTimeStatistics(@Param("year") String year,@Param("screenType") Integer screenType);
    Integer getMonthTimeStatistics(@Param("year")String year,@Param("month")String month,@Param("screenType") Integer screenType);
    Integer getDayTimeStatistics(@Param("curDay")String curDay,@Param("screenType") Integer screenType);

    default PageResult<ScreenChestRadiographDO> selectPage(ScreenChestRadiographPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ScreenChestRadiographDO>()
                .eqIfPresent(ScreenChestRadiographDO::getScreenId, reqVO.getScreenId())
                .eqIfPresent(ScreenChestRadiographDO::getScreenType, reqVO.getScreenType())
                .eqIfPresent(ScreenChestRadiographDO::getOutcome, reqVO.getOutcome())
                .orderByDesc(ScreenChestRadiographDO::getId));
    }

    /**
     * 根据摸底表id获取dr ct组最后一次筛查数据
     * @param personId 摸底表id
     * @return dr ct组最后一次筛查数据
     */
    TBHealthScreening selectByPersonIdLastTime(Long personId);

    /**
     * 根据摸底表id和筛查次序 查询DR表
     * @param personId 摸底表id
     * @param screenOrder 筛查次序
     * @return DR对象
     */
    ScreenChestRadiographDO selectOutComeByPersonIdAndOrder(@Param("personId") Long personId,
                                                            @Param("screenOrder") Integer screenOrder);

    /**
     * 同步-ct/dr 分页
     */
    List<ScreenChestRadiographDO> getCtDrData(ScreenChestRadiographPageReqVO reqVO);

    /**
     * 同步-ct/dr 分页 条数
     */
    Long getCtDrCount(ScreenChestRadiographPageReqVO reqVO);
}
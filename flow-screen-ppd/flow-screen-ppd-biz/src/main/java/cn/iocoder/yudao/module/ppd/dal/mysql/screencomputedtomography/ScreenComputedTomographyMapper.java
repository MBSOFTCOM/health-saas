package cn.iocoder.yudao.module.ppd.dal.mysql.screencomputedtomography;


import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ppd.controller.admin.screencomputedtomography.vo.*;
import cn.iocoder.yudao.module.ppd.controller.admin.screendiagnosis.vo.TBHealthScreening;
import cn.iocoder.yudao.module.ppd.controller.admin.screensum.vo.CommonReq;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screencomputedtomography.ScreenComputedTomographyDO;
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
public interface ScreenComputedTomographyMapper extends BaseMapperX<ScreenComputedTomographyDO> {

    List<ScreenComputedTomographyPersonRespVO> selectScreenComputedTomographyPage(ScreenComputedTomographyPersonPageReqVO reqVO);
    Long countScreenComputedTomographyPage(ScreenComputedTomographyPersonPageReqVO reqVO);

    List<ComputedTomographyOrderRespVO> getOrderAndTime(@Param("personId") Long personId, @Param("screenType") Integer screenType, @Param("year") Integer year);


    /**
     * 获取某患者在某year、某screenType 的最近的筛查流程
     * @param idNum 身份证
     * @param screenType 筛查类型
     * @param year 年份
     * @return Integer
     */
    Integer getMaxOrder(@Param("idNum") String idNum, @Param("screenType") Integer screenType, @Param("year") Integer year);

    ScreenComputedTomographyRespVO getOneByPersonIdNum(@Param("idNum") String idNum, @Param("screenOrder") Integer screenOrder, @Param("screenType") Integer screenType, @Param("year") Integer year);

    ComputedTomographyStatistics getTypeStatistics(@Param("screenType") Integer screenType, @Param("year") Integer year);
    Integer getYearTimeStatistics(@Param("year") String year,@Param("screenType") Integer screenType);
    Integer getMonthTimeStatistics(@Param("year")String year,@Param("month")String month,@Param("screenType") Integer screenType);
    Integer getDayTimeStatistics(@Param("curDay")String curDay,@Param("screenType") Integer screenType);

    default PageResult<ScreenComputedTomographyDO> selectPage(ScreenComputedTomographyPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ScreenComputedTomographyDO>()
                .eqIfPresent(ScreenComputedTomographyDO::getScreenId, reqVO.getScreenId())
                .eqIfPresent(ScreenComputedTomographyDO::getScreenType, reqVO.getScreenType())
                .eqIfPresent(ScreenComputedTomographyDO::getOutcome, reqVO.getOutcome())
                .orderByDesc(ScreenComputedTomographyDO::getId));
    }

    /**
     * 获取某患者在某year、某screenType 的最近的筛查流程
     * @param req CommonReq
     * @return Integer
     */
    Integer selectMaxOrder(CommonReq req);
    /**
     * 计算某患者在某年某筛查类型，某次的ct记录
     * @param req CommonReq{}
     * @return Integer
     */
    Integer countByIdNum(CommonReq req);
    /**
     * 根据次序获得 ct的审核结果
     * @param reqVO ScreenComputedTomographyPageReqVO
     * @return Integer
     */
    Integer getOutcomeByOrder(ScreenComputedTomographyPageReqVO reqVO);

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
    ScreenComputedTomographyDO selectOutComeByPersonIdAndOrder(@Param("personId") Long personId,
                                                            @Param("screenOrder") Integer screenOrder);

    /**
     * 根据以下字段，判断记录是否存在
     */
    Long getIsExist(@Param("personId") Long personId,
                    @Param("screenId") String screenId,
                    @Param("screenOrder") Integer screenOrder,
                    @Param("screenType") Integer screenType,
                    @Param("year") Integer year);
    Long getCtIsExist(
                    @Param("idNum") String idNum,
                    @Param("screenOrder") Integer screenOrder,
                    @Param("screenType") Integer screenType,
                    @Param("year") Integer year);
}
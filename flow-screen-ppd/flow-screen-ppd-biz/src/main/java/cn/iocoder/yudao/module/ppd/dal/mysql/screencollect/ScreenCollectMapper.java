package cn.iocoder.yudao.module.ppd.dal.mysql.screencollect;


import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ppd.controller.admin.screencollect.vo.ScreenCollectPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenpersonrealsituation.vo.CollectVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screencollect.ScreenCollectDO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 采集 Mapper
 *
 * @author 芋道源码
 */
@Mapper
@InterceptorIgnore(tenantLine = "true")
public interface ScreenCollectMapper extends BaseMapperX<ScreenCollectDO> {

    default PageResult<ScreenCollectDO> selectPage(ScreenCollectPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ScreenCollectDO>()
                .likeIfPresent(ScreenCollectDO::getScreenId, reqVO.getScreenId())
                .eqIfPresent(ScreenCollectDO::getSyncId, reqVO.getSyncId())
                .eqIfPresent(ScreenCollectDO::getDoctorSignature, reqVO.getDoctorSignature())
                .eqIfPresent(ScreenCollectDO::getScreenAgency, reqVO.getScreenAgency())
                .betweenIfPresent(ScreenCollectDO::getScreenTime, reqVO.getScreenTime())
                .eqIfPresent(ScreenCollectDO::getScreenOrder, reqVO.getScreenOrder())
                .eqIfPresent(ScreenCollectDO::getPersonId, reqVO.getPersonId())
                .eqIfPresent(ScreenCollectDO::getOutcome, reqVO.getOutcome())
                .eqIfPresent(ScreenCollectDO::getScreenPoint, reqVO.getScreenPoint())
                .eqIfPresent(ScreenCollectDO::getScreenType,reqVO.getScreenType())
                .orderByDesc(ScreenCollectDO::getId));
    }

    /**
     * 同步-采集组分页
     */
    List<ScreenCollectDO> getCollectData(ScreenCollectPageReqVO reqVO);
    /**
     * 同步-采集组数据分页
     */
    List<ScreenCollectDO> selectCollectDataDiffSource(ScreenCollectPageReqVO reqVO);

    /**
     * 同步-采集组分页 条数
     */
    Long getCollectCount(ScreenCollectPageReqVO reqVO);
    /**
     * 同步-采集组分页 条数
     */
    Long countCollectDiffSource(ScreenCollectPageReqVO reqVO);

    /**
     * 根据摸底表id获取到采集组中最近一次采集结果数据
     * @param personId 摸底表id
     * @param year 筛查年份
     * @param screenType 筛查类型
     * @return 采集结果数据
     */
    ScreenCollectDO selectByPersonIdLastTime(@Param("personId") Long personId,
                                             @Param("year") Integer year,
                                             @Param("screenType") Integer screenType);
    /**
     * 根据摸底表id获取到采集组中一年内的所有采集结果数据
     * @param personId 摸底表id
     * @param year 筛查年份
     * @param screenType 筛查类型
     * @return 采集结果数据
     */
    List<CollectVO> selectByPersonId(@Param("personId") Long personId,
                               @Param("year") Integer year,
                               @Param("screenType") Integer screenType);
    /**
     * 根据摸底表id获取到采集组中最近一次采集结果数据
     * @param personId 摸底表id
     * @param year 筛查年份
     * @param screenType 筛查类型
     * @return 采集结果数据
     */
    ScreenCollectDO selectWxLastTime(@Param("personId") Long personId, @Param("year") Integer year,
                                     @Param("screenType") Integer screenType,@Param("dataSource") Integer dataSource);
}
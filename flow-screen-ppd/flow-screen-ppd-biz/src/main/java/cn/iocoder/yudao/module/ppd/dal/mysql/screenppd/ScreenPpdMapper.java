package cn.iocoder.yudao.module.ppd.dal.mysql.screenppd;


import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ppd.controller.admin.screendiagnosis.vo.TBHealthScreening;
import cn.iocoder.yudao.module.ppd.controller.admin.screenppd.vo.ScreenPpdPageReqVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenppd.ScreenPpdDO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * ppd组记录 Mapper
 *
 * @author 芋道源码
 */
@Mapper
@InterceptorIgnore(tenantLine = "true")
public interface ScreenPpdMapper extends BaseMapperX<ScreenPpdDO> {

    default PageResult<ScreenPpdDO> selectPage(ScreenPpdPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ScreenPpdDO>()
                .eqIfPresent(ScreenPpdDO::getScreenId, reqVO.getScreenId())
                .eqIfPresent(ScreenPpdDO::getSyncId, reqVO.getSyncId())
                .eqIfPresent(ScreenPpdDO::getPersonId, reqVO.getPersonId())
                .eqIfPresent(ScreenPpdDO::getTransverseDiameter, reqVO.getTransverseDiameter())
                .eqIfPresent(ScreenPpdDO::getLongitudinalDiameter, reqVO.getLongitudinalDiameter())
                .eqIfPresent(ScreenPpdDO::getBleb, reqVO.getBleb())
                .eqIfPresent(ScreenPpdDO::getInjection, reqVO.getInjection())
                .eqIfPresent(ScreenPpdDO::getInjectionWay, reqVO.getInjectionWay())
                .eqIfPresent(ScreenPpdDO::getOutcome, reqVO.getOutcome())
                .eqIfPresent(ScreenPpdDO::getDoctorSignature, reqVO.getDoctorSignature())
                .eqIfPresent(ScreenPpdDO::getInjectionAgency, reqVO.getInjectionAgency())
                .eqIfPresent(ScreenPpdDO::getScreenOrder, reqVO.getScreenOrder())
                .betweenIfPresent(ScreenPpdDO::getScreenTime, reqVO.getScreenTime())
                .orderByDesc(ScreenPpdDO::getId));
    }

    /**
     * 同步-ppd分页
     */
    List<ScreenPpdDO> getPpdData(ScreenPpdPageReqVO reqVO);

    /**
     * 同步-ppd分页 条数
     */
    Long getPpdCount(ScreenPpdPageReqVO reqVO);

    /**
     * 根据personId 获取最近一次ppd筛查记录
     * @param personId 摸底表id
     * @return ppd筛查记录数据
     */
    TBHealthScreening selectByPersonIdLastTime(Long personId);
}
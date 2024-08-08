package cn.iocoder.yudao.module.ppd.dal.mysql.screensum;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ppd.controller.admin.screensum.vo.CommonReq;
import cn.iocoder.yudao.module.ppd.controller.admin.screensum.vo.ScreenSumPageReqVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screensum.ScreenSumDO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 汇总 Mapper
 *
 * @author 芋道源码
 */
@Mapper
@InterceptorIgnore(tenantLine = "true")
public interface ScreenSumMapper extends BaseMapperX<ScreenSumDO> {

    default PageResult<ScreenSumDO> selectPage(ScreenSumPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ScreenSumDO>()
                .eqIfPresent(ScreenSumDO::getYear, reqVO.getYear())
                .eqIfPresent(ScreenSumDO::getPersonId, reqVO.getPersonId())
                .eqIfPresent(ScreenSumDO::getSyncId, reqVO.getSyncId())
                .eqIfPresent(ScreenSumDO::getCollectId, reqVO.getCollectId())
                .eqIfPresent(ScreenSumDO::getPpdId, reqVO.getPpdId())
                .eqIfPresent(ScreenSumDO::getChestRadiographId, reqVO.getChestRadiographId())
                .orderByDesc(ScreenSumDO::getId));
    }

    Integer countByPersonId(CommonReq req);

    /**
     * 同步 - 汇总表
     */
    List<ScreenSumDO> selectSumDataForSynchronize(ScreenSumPageReqVO reqVO);

    /**
     * 获取某患者在某year、某screenType 的最大次序号
     * @param reqVO {idNum:String ,year:String ,screenType :Integer}
     * @return Integer
     */
    Integer getMaxOrder(ScreenSumPageReqVO reqVO);
    /**
     * 获取某患者在某year、某screenType 的最近的筛查流程
     * @param reqVO {idNum:String ,year:String ,screenType :Integer}
     * @return ScreenSumDO
     */
    ScreenSumDO getMaxOrderSumDO(CommonReq reqVO);

    Integer countExists(CommonReq reqVO);

}
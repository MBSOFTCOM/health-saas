package cn.iocoder.yudao.module.ppd.dal.mysql.screenconsume;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ppd.controller.admin.screenconsume.vo.ScreenConsumePageReqVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenconsume.ScreenConsumeDO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;

/**
 * 消耗管理 Mapper
 *
 * @author 侯卿
 */
@Mapper
@InterceptorIgnore(tenantLine = "true")
public interface ScreenConsumeMapper extends BaseMapperX<ScreenConsumeDO> {

    default PageResult<ScreenConsumeDO> selectPage(ScreenConsumePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ScreenConsumeDO>()
                .eqIfPresent(ScreenConsumeDO::getReagentId, reqVO.getReagentId())
                .likeIfPresent(ScreenConsumeDO::getReagentName, reqVO.getReagentName())
                .eqIfPresent(ScreenConsumeDO::getReagentType, reqVO.getReagentType())
                .eqIfPresent(ScreenConsumeDO::getConsumeOrder, reqVO.getConsumeOrder())
                .eqIfPresent(ScreenConsumeDO::getBathNumber, reqVO.getBathNumber())
                .eqIfPresent(ScreenConsumeDO::getInboundNumber, reqVO.getInboundNumber())
                .betweenIfPresent(ScreenConsumeDO::getManufactureDate, reqVO.getManufactureDate())
                .betweenIfPresent(ScreenConsumeDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(ScreenConsumeDO::getReagentSpecsNum, reqVO.getReagentSpecsNum())
                .eqIfPresent(ScreenConsumeDO::getThreshold, reqVO.getThreshold())
                .betweenIfPresent(ScreenConsumeDO::getIndate, reqVO.getIndate())
                .orderByDesc(ScreenConsumeDO::getId));
    }

}
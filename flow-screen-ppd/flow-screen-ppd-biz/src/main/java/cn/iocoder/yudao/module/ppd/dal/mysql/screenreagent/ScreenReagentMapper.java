package cn.iocoder.yudao.module.ppd.dal.mysql.screenreagent;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.ppd.controller.admin.screenreagent.vo.ScreenReagentPageReqVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenreagent.ScreenReagentDO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;

/**
 * 试剂 Mapper
 *
 * @author 侯卿
 */
@Mapper
@InterceptorIgnore(tenantLine = "true")
public interface ScreenReagentMapper extends BaseMapperX<ScreenReagentDO> {

    default PageResult<ScreenReagentDO> selectPage(ScreenReagentPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ScreenReagentDO>()
                .likeIfPresent(ScreenReagentDO::getName, reqVO.getName())
                .eqIfPresent(ScreenReagentDO::getType, reqVO.getType())
                .eqIfPresent(ScreenReagentDO::getReagentSpecsNum, reqVO.getReagentSpecsNum())
                .eqIfPresent(ScreenReagentDO::getUsable, reqVO.getUsable())
                .eqIfPresent(ScreenReagentDO::getTiter, reqVO.getTiter())
                .eqIfPresent(ScreenReagentDO::getPotencyUnit, reqVO.getPotencyUnit())
                .eqIfPresent(ScreenReagentDO::getSpecification, reqVO.getSpecification())
                .eqIfPresent(ScreenReagentDO::getSpecificationUnit, reqVO.getSpecificationUnit())
                .eqIfPresent(ScreenReagentDO::getPackageUnit, reqVO.getPackageUnit())
                .eqIfPresent(ScreenReagentDO::getManufacturer, reqVO.getManufacturer())
                .eqIfPresent(ScreenReagentDO::getThreshold, reqVO.getThreshold())
                .betweenIfPresent(ScreenReagentDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ScreenReagentDO::getId));
    }

}
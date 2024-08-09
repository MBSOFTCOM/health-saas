package cn.iocoder.yudao.module.ppd.dal.mysql.screenreagent;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.ppd.controller.admin.screenreagent.vo.ScreenReagentPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenreagent.vo.ScreenReagentRespVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenreagent.ScreenReagentDO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

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
                .eqIfPresent(ScreenReagentDO::getUsable, reqVO.getUsable())
                .likeIfPresent(ScreenReagentDO::getManufacturer, reqVO.getManufacturer())
                .eqIfPresent(ScreenReagentDO::getUsable, reqVO.getUsable())
                .orderByDesc(ScreenReagentDO::getId));
    }

    /**
     *禁用试剂
     */
    Integer forbidReagent(Long id);

    /**
     *启用试剂
     */
    Integer recoverReagent(Long id);

    /**
     *判断是否已经有这种试剂了
     */
    /*Integer selectIsExist(@Param("name") String name,
                          @Param("type") Integer type,
                          @Param("reagentSpecsNum") Integer reagentSpecsNum,
                          @Param("titer") BigDecimal titer,
                          @Param("potencyUnit") Integer potencyUnit,
                          @Param("specification") BigDecimal specification,
                          @Param("specificationUnit") Integer specificationUnit,
                          @Param("packageUnit") Integer packageUnit,
                          @Param("manufacturer") String manufacturer,
                          @Param("threshold") Integer threshold);*/

    /**
     *判断是否已经有这种试剂了
     */
    Integer selectIsExist(@Param("name") String name);

    /**
     * 获取试剂列表
     */
    List<ScreenReagentDO> getReagentList();

    /**
     * 获取试剂列表--名称
     */
    List<String> getReagentList2();

    /**
     * 根据试剂名称获取
     */
    ScreenReagentDO selectByName(String reagentName);
}
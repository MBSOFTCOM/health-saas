package cn.iocoder.yudao.module.ppd.dal.mysql.screenreagent;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ppd.controller.admin.screenreagent.vo.ScreenReagentPageReqVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenreagent.ScreenReagentDO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
                .eqIfPresent(ScreenReagentDO::getDeptId, reqVO.getDeptList())
                .orderByDesc(ScreenReagentDO::getId));
    }

    /**
     * 获取所有启用的试剂
     * @param reqVO ScreenReagentPageReqVO
     * @return List<ScreenReagentDO>
     */
    default List<ScreenReagentDO> selectUsable(ScreenReagentPageReqVO reqVO){
      return selectList(new LambdaQueryWrapperX<ScreenReagentDO>()
              .eqIfPresent(ScreenReagentDO::getUsable,0)
              .eqIfPresent(ScreenReagentDO::getDeleted,0)
              .eqIfPresent(ScreenReagentDO::getType,reqVO.getType())
              .select(ScreenReagentDO::getId,ScreenReagentDO::getType,ScreenReagentDO::getReagentSpecsNum,ScreenReagentDO::getUsable,
                      ScreenReagentDO::getTiter,ScreenReagentDO::getPotencyUnit,ScreenReagentDO::getSpecification,ScreenReagentDO::getSpecificationUnit,
                      ScreenReagentDO::getPackageUnit,ScreenReagentDO::getManufacturer,ScreenReagentDO::getThreshold,ScreenReagentDO::getName
                      )
      );
    };
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
    Integer selectIsExist(@Param("name") String name, @Param("deptId") Long deptId);

    /**
     * 获取试剂列表
     */
    List<ScreenReagentDO> getReagentList(@Param("deptId") Long deptId);

    /**
     * 获取试剂列表--名称
     */
    List<String> getReagentList2(@Param("deptId") Long deptId);

    /**
     * 根据试剂名称获取
     */
    ScreenReagentDO selectByName(@Param("reagentName") String reagentName,
                                 @Param("deptId") Long deptId);
}
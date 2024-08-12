package cn.iocoder.yudao.module.ppd.dal.mysql.screenconsume;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ppd.controller.admin.screenconsume.vo.ScreenConsumePageReqVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenconsume.ScreenConsumeDO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

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
                .likeIfPresent(ScreenConsumeDO::getReagentName, reqVO.getReagentName())
                .eqIfPresent(ScreenConsumeDO::getReagentType, reqVO.getReagentType())
                .likeIfPresent(ScreenConsumeDO::getBathNumber, reqVO.getBathNumber())
                .orderByDesc(ScreenConsumeDO::getId));
    }

    /**
     * 获取所有可用的试剂批号数据，现有库存大于0
     * @param reqVO ScreenConsumePageReqVO
     * @return List<ScreenConsumeDO>
     */
    default List<ScreenConsumeDO> listUsable(ScreenConsumePageReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ScreenConsumeDO>()
                .eqIfPresent(ScreenConsumeDO::getReagentId,reqVO.getReagentId())
                .eqIfPresent(ScreenConsumeDO::getUsable,0)
                .eqIfPresent(ScreenConsumeDO::getDeleted,0)
                .gt(ScreenConsumeDO::getCurrentNumber,0)
                .select(
                        ScreenConsumeDO::getId,ScreenConsumeDO::getReagentId,
                        ScreenConsumeDO::getReagentName,ScreenConsumeDO::getReagentType,
                        ScreenConsumeDO::getReagentSpecsNum,ScreenConsumeDO::getConsumeOrder,
                        ScreenConsumeDO::getThreshold,ScreenConsumeDO::getUsable,
                        ScreenConsumeDO::getBathNumber,ScreenConsumeDO::getCurrentNumber,
                        ScreenConsumeDO::getInboundNumber,ScreenConsumeDO::getIndate,
                        ScreenConsumeDO::getManufactureDate
                )
        );
    }

    /**
     *  增加入库量、当前库存
     */
    Integer increaseScreenConsume(@Param("id") Long id,
                                  @Param("number") Integer number);

    /**
     * 减少当前库存
     */
    Integer decreaseScreenConsume(@Param("id") Long id,
                                  @Param("number") Integer number);

    /**
     * 根据试剂id，找出消耗管理列表
     */
    List<ScreenConsumeDO> selcetByReagentId(Long reagentId);

    /**
     * 禁用
     */
    Integer forbid(Long id);

    /**
     * 启用
     */
    Integer recover(Long id);

    /**
     * 新增、修改时判断是否已经有相同的记录
     */
    Integer isExist(@Param("reagentId") Long reagentId,
                    @Param("consumeOrder") Integer consumeOrder,
                    @Param("bathNumber") String bathNumber,
                    @Param("indate") String indate,
                    @Param("manufactureDate") LocalDateTime manufactureDate);

    Integer isExist2(@Param("reagentName") String reagentName,
                     @Param("consumeOrder") Integer consumeOrder,
                     @Param("bathNumber") String bathNumber,
                     @Param("indate") String indate,
                     @Param("manufactureDate") LocalDateTime manufactureDate);

    List<ScreenConsumeDO> selectByTime(@Param("startDateTime") LocalDateTime startDateTime,
                                       @Param("endDateTime") LocalDateTime endDateTime,
                                       @Param("reagentName") String reagentName);
}
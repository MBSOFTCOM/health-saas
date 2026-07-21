package cn.iocoder.yudao.module.childhealth.dal.mysql.screening;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.ScreeningItemConfigDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 筛查项目配置 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ScreeningItemConfigMapper extends BaseMapperX<ScreeningItemConfigDO> {

    default PageResult<ScreeningItemConfigDO> selectPage(cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.item.ScreeningItemConfigPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ScreeningItemConfigDO>()
                .likeIfPresent(ScreeningItemConfigDO::getItemCode, reqVO.getItemCode())
                .likeIfPresent(ScreeningItemConfigDO::getItemName, reqVO.getItemName())
                .eqIfPresent(ScreeningItemConfigDO::getCategory, reqVO.getCategory())
                .eqIfPresent(ScreeningItemConfigDO::getResultType, reqVO.getResultType())
                .eqIfPresent(ScreeningItemConfigDO::getStatus, reqVO.getStatus())
                .orderByAsc(ScreeningItemConfigDO::getSortOrder)
                .orderByDesc(ScreeningItemConfigDO::getId));
    }

    default List<ScreeningItemConfigDO> selectList(cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.item.ScreeningItemConfigListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ScreeningItemConfigDO>()
                .likeIfPresent(ScreeningItemConfigDO::getItemCode, reqVO.getItemCode())
                .likeIfPresent(ScreeningItemConfigDO::getItemName, reqVO.getItemName())
                .eqIfPresent(ScreeningItemConfigDO::getCategory, reqVO.getCategory())
                .eqIfPresent(ScreeningItemConfigDO::getStatus, reqVO.getStatus())
                .orderByAsc(ScreeningItemConfigDO::getSortOrder)
                .orderByDesc(ScreeningItemConfigDO::getId));
    }

    default ScreeningItemConfigDO selectByItemCode(String itemCode) {
        return selectOne(ScreeningItemConfigDO::getItemCode, itemCode);
    }

    default List<ScreeningItemConfigDO> selectByCategory(String category) {
        return selectList(new LambdaQueryWrapperX<ScreeningItemConfigDO>()
                .eq(ScreeningItemConfigDO::getCategory, category)
                .eq(ScreeningItemConfigDO::getStatus, 1)
                .orderByAsc(ScreeningItemConfigDO::getSortOrder));
    }

    default List<ScreeningItemConfigDO> selectActiveList() {
        return selectList(new LambdaQueryWrapperX<ScreeningItemConfigDO>()
                .eq(ScreeningItemConfigDO::getStatus, 1)
                .orderByAsc(ScreeningItemConfigDO::getSortOrder));
    }

}
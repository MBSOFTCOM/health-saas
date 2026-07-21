package cn.iocoder.yudao.module.childhealth.dal.mysql.screening;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.resultDetail.ScreeningResultDetailPageReqVO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.ScreeningResultDetailDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 筛查结果明细 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ScreeningResultDetailMapper extends BaseMapperX<ScreeningResultDetailDO> {

    default PageResult<ScreeningResultDetailDO> selectPage(ScreeningResultDetailPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ScreeningResultDetailDO>()
                .eqIfPresent(ScreeningResultDetailDO::getRecordId, reqVO.getRecordId())
                .likeIfPresent(ScreeningResultDetailDO::getItemCode, reqVO.getItemCode())
                .eqIfPresent(ScreeningResultDetailDO::getIsAbnormal, reqVO.getIsAbnormal())
                .eqIfPresent(ScreeningResultDetailDO::getDeptId, reqVO.getDeptId())
                .orderByDesc(ScreeningResultDetailDO::getId));
    }

    default List<ScreeningResultDetailDO> selectByRecordId(Long recordId) {
        return selectList(new LambdaQueryWrapperX<ScreeningResultDetailDO>()
                .eq(ScreeningResultDetailDO::getRecordId, recordId)
                .orderByAsc(ScreeningResultDetailDO::getId));
    }

    default ScreeningResultDetailDO selectByRecordIdAndItemCode(Long recordId, String itemCode) {
        return selectOne(new LambdaQueryWrapperX<ScreeningResultDetailDO>()
                .eq(ScreeningResultDetailDO::getRecordId, recordId)
                .eq(ScreeningResultDetailDO::getItemCode, itemCode));
    }

    /**
     * 按筛查记录ID列表查询全部明细（用于按批次/项目编码聚合统计）
     */
    default List<ScreeningResultDetailDO> selectListByRecordIds(List<Long> recordIds) {
        if (recordIds == null || recordIds.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        return selectList(new LambdaQueryWrapperX<ScreeningResultDetailDO>()
                .in(ScreeningResultDetailDO::getRecordId, recordIds));
    }

    /**
     * 按筛查记录ID列表查询异常明细（is_abnormal=1）
     */
    default List<ScreeningResultDetailDO> selectAbnormalListByRecordIds(List<Long> recordIds) {
        if (recordIds == null || recordIds.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        return selectList(new LambdaQueryWrapperX<ScreeningResultDetailDO>()
                .in(ScreeningResultDetailDO::getRecordId, recordIds)
                .eq(ScreeningResultDetailDO::getIsAbnormal, 1));
    }

}
package cn.iocoder.yudao.module.childhealth.dal.mysql.screening;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.RecheckRecordDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

/**
 * 复筛记录 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface RecheckRecordMapper extends BaseMapperX<RecheckRecordDO> {

    default PageResult<RecheckRecordDO> selectPage(cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.recheck.RecheckRecordPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<RecheckRecordDO>()
                .eqIfPresent(RecheckRecordDO::getPositiveId, reqVO.getPositiveId())
                .eqIfPresent(RecheckRecordDO::getStudentId, reqVO.getStudentId())
                .betweenIfPresent(RecheckRecordDO::getRecheckDate, reqVO.getRecheckDate())
                .eqIfPresent(RecheckRecordDO::getIsStillPositive, reqVO.getIsStillPositive())
                .orderByDesc(RecheckRecordDO::getId));
    }

    default RecheckRecordDO selectByPositiveId(Long positiveId) {
        return selectOne(RecheckRecordDO::getPositiveId, positiveId);
    }

    default List<RecheckRecordDO> selectByStudentId(Long studentId) {
        return selectList(new LambdaQueryWrapperX<RecheckRecordDO>()
                .eq(RecheckRecordDO::getStudentId, studentId)
                .orderByDesc(RecheckRecordDO::getId));
    }

    /**
     * 按阳性记录ID列表查询所有复筛记录
     */
    default List<RecheckRecordDO> selectListByPositiveIds(List<Long> positiveIds) {
        if (positiveIds == null || positiveIds.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        return selectList(new LambdaQueryWrapperX<RecheckRecordDO>()
                .in(RecheckRecordDO::getPositiveId, positiveIds));
    }

    /**
     * 按日期范围统计复筛仍阳性数
     */
    default Long countStillPositive(LocalDate startDate, LocalDate endDate) {
        return selectCount(new LambdaQueryWrapperX<RecheckRecordDO>()
                .eq(RecheckRecordDO::getIsStillPositive, 1)
                .geIfPresent(RecheckRecordDO::getRecheckDate, startDate)
                .leIfPresent(RecheckRecordDO::getRecheckDate, endDate));
    }

}
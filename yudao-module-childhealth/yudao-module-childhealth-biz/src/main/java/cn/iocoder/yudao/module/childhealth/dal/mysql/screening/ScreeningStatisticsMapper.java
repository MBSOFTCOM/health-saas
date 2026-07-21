package cn.iocoder.yudao.module.childhealth.dal.mysql.screening;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.statistics.ScreeningStatisticsPageReqVO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.ScreeningStatisticsDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 筛查统计 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ScreeningStatisticsMapper extends BaseMapperX<ScreeningStatisticsDO> {

    default PageResult<ScreeningStatisticsDO> selectPage(ScreeningStatisticsPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ScreeningStatisticsDO>()
                .betweenIfPresent(ScreeningStatisticsDO::getStatDate, reqVO.getStatDate())
                .eqIfPresent(ScreeningStatisticsDO::getBatchId, reqVO.getBatchId())
                .eqIfPresent(ScreeningStatisticsDO::getSchoolId, reqVO.getSchoolId())
                .eqIfPresent(ScreeningStatisticsDO::getGradeId, reqVO.getGradeId())
                .orderByDesc(ScreeningStatisticsDO::getId));
    }

    default List<ScreeningStatisticsDO> selectListByBatchId(Long batchId) {
        return selectList(new LambdaQueryWrapperX<ScreeningStatisticsDO>()
                .eq(ScreeningStatisticsDO::getBatchId, batchId)
                .orderByDesc(ScreeningStatisticsDO::getStatDate));
    }

    default List<ScreeningStatisticsDO> selectListBySchoolId(Long schoolId) {
        return selectList(new LambdaQueryWrapperX<ScreeningStatisticsDO>()
                .eq(ScreeningStatisticsDO::getSchoolId, schoolId)
                .orderByDesc(ScreeningStatisticsDO::getStatDate));
    }

}
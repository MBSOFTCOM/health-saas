package cn.iocoder.yudao.module.childhealth.dal.mysql.screening;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.ScreeningBatchDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

/**
 * 筛查批次 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ScreeningBatchMapper extends BaseMapperX<ScreeningBatchDO> {

    default PageResult<ScreeningBatchDO> selectPage(cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.batch.ScreeningBatchPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ScreeningBatchDO>()
                .likeIfPresent(ScreeningBatchDO::getBatchNo, reqVO.getBatchNo())
                .likeIfPresent(ScreeningBatchDO::getBatchName, reqVO.getBatchName())
                .eqIfPresent(ScreeningBatchDO::getYearId, reqVO.getYearId())
                .eqIfPresent(ScreeningBatchDO::getSchoolId, reqVO.getSchoolId())
                .betweenIfPresent(ScreeningBatchDO::getStartDate, reqVO.getStartDate())
                .betweenIfPresent(ScreeningBatchDO::getEndDate, reqVO.getEndDate())
                .eqIfPresent(ScreeningBatchDO::getBatchStatus, reqVO.getBatchStatus())
                .orderByDesc(ScreeningBatchDO::getId));
    }

    default List<ScreeningBatchDO> selectList(cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.batch.ScreeningBatchListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ScreeningBatchDO>()
                .likeIfPresent(ScreeningBatchDO::getBatchNo, reqVO.getBatchNo())
                .likeIfPresent(ScreeningBatchDO::getBatchName, reqVO.getBatchName())
                .eqIfPresent(ScreeningBatchDO::getYearId, reqVO.getYearId())
                .eqIfPresent(ScreeningBatchDO::getSchoolId, reqVO.getSchoolId())
                .eqIfPresent(ScreeningBatchDO::getBatchStatus, reqVO.getBatchStatus())
                .orderByDesc(ScreeningBatchDO::getId));
    }

    default ScreeningBatchDO selectByBatchNo(String batchNo) {
        return selectOne(ScreeningBatchDO::getBatchNo, batchNo);
    }

    /**
     * 按学校/学年/状态查询批次列表（用于统计接口）
     */
    default List<ScreeningBatchDO> selectListByConditions(Long schoolId, Long yearId, Integer batchStatus) {
        return selectList(new LambdaQueryWrapperX<ScreeningBatchDO>()
                .eqIfPresent(ScreeningBatchDO::getSchoolId, schoolId)
                .eqIfPresent(ScreeningBatchDO::getYearId, yearId)
                .eqIfPresent(ScreeningBatchDO::getBatchStatus, batchStatus)
                .orderByDesc(ScreeningBatchDO::getId));
    }

    /**
     * 按日期范围查询批次
     */
    default List<ScreeningBatchDO> selectListByDateRange(LocalDate startDate, LocalDate endDate) {
        return selectList(new LambdaQueryWrapperX<ScreeningBatchDO>()
                .geIfPresent(ScreeningBatchDO::getStartDate, startDate)
                .leIfPresent(ScreeningBatchDO::getEndDate, endDate)
                .orderByDesc(ScreeningBatchDO::getId));
    }

}
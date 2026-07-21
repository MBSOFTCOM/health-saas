package cn.iocoder.yudao.module.childhealth.dal.mysql.ops;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.ops.OpsIndicatorSnapshotDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

/**
 * 运营指标日快照 Mapper
 *
 * 创建日期: 2026-07-20
 */
@Mapper
public interface OpsIndicatorSnapshotMapper extends BaseMapperX<OpsIndicatorSnapshotDO> {

    /**
     * 按日期查询快照（默认按日期降序）
     */
    default OpsIndicatorSnapshotDO selectByDate(LocalDate snapshotDate, Long batchId, Long schoolId, Long gradeId) {
        return selectOne(new LambdaQueryWrapperX<OpsIndicatorSnapshotDO>()
                .eqIfPresent(OpsIndicatorSnapshotDO::getSnapshotDate, snapshotDate)
                .eqIfPresent(OpsIndicatorSnapshotDO::getBatchId, batchId)
                .eqIfPresent(OpsIndicatorSnapshotDO::getSchoolId, schoolId)
                .eqIfPresent(OpsIndicatorSnapshotDO::getGradeId, gradeId));
    }

    /**
     * 按日期范围查询趋势数据
     */
    default List<OpsIndicatorSnapshotDO> selectListByDateRange(LocalDate startDate, LocalDate endDate, Long batchId) {
        return selectList(new LambdaQueryWrapperX<OpsIndicatorSnapshotDO>()
                .geIfPresent(OpsIndicatorSnapshotDO::getSnapshotDate, startDate)
                .leIfPresent(OpsIndicatorSnapshotDO::getSnapshotDate, endDate)
                .eqIfPresent(OpsIndicatorSnapshotDO::getBatchId, batchId)
                .orderByAsc(OpsIndicatorSnapshotDO::getSnapshotDate));
    }

    /**
     * 按学校查询最新快照
     */
    default OpsIndicatorSnapshotDO selectLatestBySchool(Long schoolId, Long batchId) {
        return selectOne(new LambdaQueryWrapperX<OpsIndicatorSnapshotDO>()
                .eqIfPresent(OpsIndicatorSnapshotDO::getSchoolId, schoolId)
                .eqIfPresent(OpsIndicatorSnapshotDO::getBatchId, batchId)
                .orderByDesc(OpsIndicatorSnapshotDO::getSnapshotDate)
                .last("LIMIT 1"));
    }

    /**
     * 按区域代码查询
     */
    default List<OpsIndicatorSnapshotDO> selectListByRegion(String regionCode, LocalDate snapshotDate) {
        return selectList(new LambdaQueryWrapperX<OpsIndicatorSnapshotDO>()
                .eqIfPresent(OpsIndicatorSnapshotDO::getRegionCode, regionCode)
                .eqIfPresent(OpsIndicatorSnapshotDO::getSnapshotDate, snapshotDate));
    }

    /**
     * 按批次查询所有学校快照
     */
    default List<OpsIndicatorSnapshotDO> selectListByBatch(Long batchId, LocalDate snapshotDate) {
        return selectList(new LambdaQueryWrapperX<OpsIndicatorSnapshotDO>()
                .eqIfPresent(OpsIndicatorSnapshotDO::getBatchId, batchId)
                .eqIfPresent(OpsIndicatorSnapshotDO::getSnapshotDate, snapshotDate));
    }

}

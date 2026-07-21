package cn.iocoder.yudao.module.childhealth.dal.mysql.ops;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.ops.StaffWorkloadStatisticsDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

/**
 * 工作量统计 Mapper
 *
 * 创建日期: 2026-07-20
 */
@Mapper
public interface StaffWorkloadStatisticsMapper extends BaseMapperX<StaffWorkloadStatisticsDO> {

    /**
     * 按医护ID和日期查询工作量
     */
    default StaffWorkloadStatisticsDO selectByStaffAndDate(Long staffId, LocalDate statDate) {
        return selectOne(new LambdaQueryWrapperX<StaffWorkloadStatisticsDO>()
                .eqIfPresent(StaffWorkloadStatisticsDO::getStaffId, staffId)
                .eqIfPresent(StaffWorkloadStatisticsDO::getStatDate, statDate));
    }

    /**
     * 按日期范围查询某医护工作量趋势
     */
    default List<StaffWorkloadStatisticsDO> selectListByStaffAndDateRange(Long staffId, LocalDate startDate, LocalDate endDate) {
        return selectList(new LambdaQueryWrapperX<StaffWorkloadStatisticsDO>()
                .eqIfPresent(StaffWorkloadStatisticsDO::getStaffId, staffId)
                .geIfPresent(StaffWorkloadStatisticsDO::getStatDate, startDate)
                .leIfPresent(StaffWorkloadStatisticsDO::getStatDate, endDate)
                .orderByAsc(StaffWorkloadStatisticsDO::getStatDate));
    }

    /**
     * 按机构查询当日工作量排名
     */
    default List<StaffWorkloadStatisticsDO> selectListByOrgAndDate(Long orgId, LocalDate statDate) {
        return selectList(new LambdaQueryWrapperX<StaffWorkloadStatisticsDO>()
                .eqIfPresent(StaffWorkloadStatisticsDO::getOrgId, orgId)
                .eqIfPresent(StaffWorkloadStatisticsDO::getStatDate, statDate)
                .orderByDesc(StaffWorkloadStatisticsDO::getTotalWorkload));
    }

    /**
     * 按学校查询工作量
     */
    default List<StaffWorkloadStatisticsDO> selectListBySchoolAndDate(Long schoolId, LocalDate statDate) {
        return selectList(new LambdaQueryWrapperX<StaffWorkloadStatisticsDO>()
                .eqIfPresent(StaffWorkloadStatisticsDO::getSchoolId, schoolId)
                .eqIfPresent(StaffWorkloadStatisticsDO::getStatDate, statDate));
    }

    /**
     * 按科室查询工作量
     */
    default List<StaffWorkloadStatisticsDO> selectListByDeptAndDate(Long deptId, LocalDate statDate) {
        return selectList(new LambdaQueryWrapperX<StaffWorkloadStatisticsDO>()
                .eqIfPresent(StaffWorkloadStatisticsDO::getDeptId, deptId)
                .eqIfPresent(StaffWorkloadStatisticsDO::getStatDate, statDate));
    }

    /**
     * 按日期范围 + 机构/学校维度查询工作量（用于统计接口聚合）
     *
     * @param orgId    机构ID（可空）
     * @param schoolId 学校ID（可空）
     * @param startDate 起始日期（可空）
     * @param endDate   结束日期（可空）
     */
    default List<StaffWorkloadStatisticsDO> selectListByConditions(Long orgId, Long schoolId,
                                                                     LocalDate startDate, LocalDate endDate) {
        return selectList(new LambdaQueryWrapperX<StaffWorkloadStatisticsDO>()
                .eqIfPresent(StaffWorkloadStatisticsDO::getOrgId, orgId)
                .eqIfPresent(StaffWorkloadStatisticsDO::getSchoolId, schoolId)
                .geIfPresent(StaffWorkloadStatisticsDO::getStatDate, startDate)
                .leIfPresent(StaffWorkloadStatisticsDO::getStatDate, endDate)
                .orderByDesc(StaffWorkloadStatisticsDO::getStatDate));
    }

}

package cn.iocoder.yudao.module.childhealth.dal.mysql.management;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.management.FollowTaskDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 随访任务 Mapper
 */
@Mapper
public interface FollowTaskMapper extends BaseMapperX<FollowTaskDO> {

    /**
     * 按计划日期统计当日随访数
     */
    default Long countByPlanDate(LocalDate planDate) {
        if (planDate == null) {
            return 0L;
        }
        return selectCount(new LambdaQueryWrapperX<FollowTaskDO>()
                .between(FollowTaskDO::getPlanDate, planDate, planDate));
    }

    /**
     * 按计划日期范围统计随访任务数
     */
    default Long countByPlanDateRange(LocalDate startDate, LocalDate endDate) {
        return selectCount(new LambdaQueryWrapperX<FollowTaskDO>()
                .geIfPresent(FollowTaskDO::getPlanDate, startDate)
                .leIfPresent(FollowTaskDO::getPlanDate, endDate));
    }

    /**
     * 按计划日期范围 + 任务状态统计
     */
    default Long countByDateRangeAndStatus(LocalDate startDate, LocalDate endDate, Integer taskStatus) {
        return selectCount(new LambdaQueryWrapperX<FollowTaskDO>()
                .geIfPresent(FollowTaskDO::getPlanDate, startDate)
                .leIfPresent(FollowTaskDO::getPlanDate, endDate)
                .eqIfPresent(FollowTaskDO::getTaskStatus, taskStatus));
    }

    /**
     * 当日创建的随访数（按 create_time）
     */
    default Long countTodayCreated() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
        return selectCount(new LambdaQueryWrapperX<FollowTaskDO>()
                .ge(FollowTaskDO::getCreateTime, startOfDay)
                .le(FollowTaskDO::getCreateTime, endOfDay));
    }

}

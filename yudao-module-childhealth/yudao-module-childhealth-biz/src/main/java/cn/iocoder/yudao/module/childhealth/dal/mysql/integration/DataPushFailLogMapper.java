package cn.iocoder.yudao.module.childhealth.dal.mysql.integration;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.integration.DataPushFailLogDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 数据上报失败重试日志 Mapper
 *
 * 创建日期: 2026-07-20
 */
@Mapper
public interface DataPushFailLogMapper extends BaseMapperX<DataPushFailLogDO> {

    /**
     * 按任务ID查询所有失败日志（按尝试次数升序）
     */
    default List<DataPushFailLogDO> selectListByTask(Long taskId) {
        return selectList(new LambdaQueryWrapperX<DataPushFailLogDO>()
                .eqIfPresent(DataPushFailLogDO::getTaskId, taskId)
                .orderByAsc(DataPushFailLogDO::getAttemptNo));
    }

    /**
     * 按错误类型查询
     */
    default List<DataPushFailLogDO> selectListByErrorType(String errorType) {
        return selectList(new LambdaQueryWrapperX<DataPushFailLogDO>()
                .eqIfPresent(DataPushFailLogDO::getErrorType, errorType)
                .orderByDesc(DataPushFailLogDO::getFailTime));
    }

    /**
     * 按时间范围查询失败日志（用于失败趋势分析）
     */
    default List<DataPushFailLogDO> selectListByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        return selectList(new LambdaQueryWrapperX<DataPushFailLogDO>()
                .geIfPresent(DataPushFailLogDO::getFailTime, startTime)
                .leIfPresent(DataPushFailLogDO::getFailTime, endTime)
                .orderByDesc(DataPushFailLogDO::getFailTime));
    }

    /**
     * 统计任务失败次数
     */
    default Long countByTask(Long taskId) {
        return selectCount(new LambdaQueryWrapperX<DataPushFailLogDO>()
                .eqIfPresent(DataPushFailLogDO::getTaskId, taskId));
    }

}

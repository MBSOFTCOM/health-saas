package cn.iocoder.yudao.module.childhealth.dal.mysql.integration;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.integration.DataPushTaskDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 数据上报任务队列 Mapper
 *
 * 创建日期: 2026-07-20
 */
@Mapper
public interface DataPushTaskMapper extends BaseMapperX<DataPushTaskDO> {

    /**
     * 按任务编号查询
     */
    default DataPushTaskDO selectByTaskNo(String taskNo) {
        return selectOne(DataPushTaskDO::getTaskNo, taskNo);
    }

    /**
     * 分页查询（按目标系统/业务类型/状态过滤）
     */
    default PageResult<DataPushTaskDO> selectPage(String targetSystem, String businessType,
                                                   Integer pushStatus, PageParam pageParam) {
        return selectPage(pageParam, new LambdaQueryWrapperX<DataPushTaskDO>()
                .eqIfPresent(DataPushTaskDO::getTargetSystem, targetSystem)
                .eqIfPresent(DataPushTaskDO::getBusinessType, businessType)
                .eqIfPresent(DataPushTaskDO::getPushStatus, pushStatus)
                .orderByDesc(DataPushTaskDO::getId));
    }

    /**
     * 查询待推送任务
     */
    default List<DataPushTaskDO> selectPendingList() {
        return selectList(new LambdaQueryWrapperX<DataPushTaskDO>()
                .eqIfPresent(DataPushTaskDO::getPushStatus, 0)
                .orderByAsc(DataPushTaskDO::getPriority)
                .orderByAsc(DataPushTaskDO::getId));
    }

    /**
     * 查询需重试的任务（已失败且到达重试时间）
     */
    default List<DataPushTaskDO> selectRetryList() {
        return selectList(new LambdaQueryWrapperX<DataPushTaskDO>()
                .eqIfPresent(DataPushTaskDO::getPushStatus, 3)
                .le(DataPushTaskDO::getNextRetryTime, LocalDateTime.now())
                .orderByAsc(DataPushTaskDO::getNextRetryTime));
    }

    /**
     * 按批次ID查询上报任务
     */
    default List<DataPushTaskDO> selectListByBatch(Long batchId) {
        return selectList(new LambdaQueryWrapperX<DataPushTaskDO>()
                .eqIfPresent(DataPushTaskDO::getBatchId, batchId));
    }

    /**
     * 按目标系统统计推送数量
     */
    default Long countBySystemAndStatus(String targetSystem, Integer pushStatus) {
        return selectCount(new LambdaQueryWrapperX<DataPushTaskDO>()
                .eqIfPresent(DataPushTaskDO::getTargetSystem, targetSystem)
                .eqIfPresent(DataPushTaskDO::getPushStatus, pushStatus));
    }

}

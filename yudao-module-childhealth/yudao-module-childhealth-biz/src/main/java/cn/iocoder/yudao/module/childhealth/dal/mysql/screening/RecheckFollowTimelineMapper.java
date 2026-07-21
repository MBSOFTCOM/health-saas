package cn.iocoder.yudao.module.childhealth.dal.mysql.screening;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.RecheckFollowTimelineDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 复筛随访时间轴 Mapper
 *
 * 创建日期: 2026-07-20
 */
@Mapper
public interface RecheckFollowTimelineMapper extends BaseMapperX<RecheckFollowTimelineDO> {

    /**
     * 按阳性记录ID查询时间轴（默认时间升序）
     * 用于复筛工作台时间轴展示
     */
    default List<RecheckFollowTimelineDO> selectListByPositive(Long positiveId) {
        return selectList(new LambdaQueryWrapperX<RecheckFollowTimelineDO>()
                .eqIfPresent(RecheckFollowTimelineDO::getPositiveId, positiveId)
                .orderByAsc(RecheckFollowTimelineDO::getEventTime));
    }

    /**
     * 按学生ID查询时间轴
     */
    default List<RecheckFollowTimelineDO> selectListByStudent(Long studentId) {
        return selectList(new LambdaQueryWrapperX<RecheckFollowTimelineDO>()
                .eqIfPresent(RecheckFollowTimelineDO::getStudentId, studentId)
                .orderByDesc(RecheckFollowTimelineDO::getEventTime));
    }

    /**
     * 按事件类型查询
     */
    default List<RecheckFollowTimelineDO> selectListByEventType(String eventType) {
        return selectList(new LambdaQueryWrapperX<RecheckFollowTimelineDO>()
                .eqIfPresent(RecheckFollowTimelineDO::getEventType, eventType)
                .orderByDesc(RecheckFollowTimelineDO::getEventTime));
    }

    /**
     * 按阳性记录ID和事件类型查询
     */
    default List<RecheckFollowTimelineDO> selectListByPositiveAndType(Long positiveId, String eventType) {
        return selectList(new LambdaQueryWrapperX<RecheckFollowTimelineDO>()
                .eqIfPresent(RecheckFollowTimelineDO::getPositiveId, positiveId)
                .eqIfPresent(RecheckFollowTimelineDO::getEventType, eventType)
                .orderByDesc(RecheckFollowTimelineDO::getEventTime));
    }

}

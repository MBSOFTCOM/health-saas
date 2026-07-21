package cn.iocoder.yudao.module.childhealth.dal.mysql.screening;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.PositiveRuleExecLogDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 阳性规则执行日志 Mapper
 *
 * 创建日期: 2026-07-20
 */
@Mapper
public interface PositiveRuleExecLogMapper extends BaseMapperX<PositiveRuleExecLogDO> {

    /**
     * 按规则ID分页查询
     */
    default PageResult<PositiveRuleExecLogDO> selectPageByRule(Long ruleId, Integer pageNo, Integer pageSize) {
        cn.iocoder.yudao.framework.common.pojo.PageParam pageParam = new cn.iocoder.yudao.framework.common.pojo.PageParam();
        pageParam.setPageNo(pageNo);
        pageParam.setPageSize(pageSize);
        return selectPage(pageParam,
                new LambdaQueryWrapperX<PositiveRuleExecLogDO>()
                        .eqIfPresent(PositiveRuleExecLogDO::getRuleId, ruleId)
                        .orderByDesc(PositiveRuleExecLogDO::getMatchedAt));
    }

    /**
     * 按筛查记录ID查询所有命中日志
     */
    default List<PositiveRuleExecLogDO> selectListByRecord(Long recordId) {
        return selectList(new LambdaQueryWrapperX<PositiveRuleExecLogDO>()
                .eqIfPresent(PositiveRuleExecLogDO::getRecordId, recordId)
                .orderByDesc(PositiveRuleExecLogDO::getMatchedAt));
    }

    /**
     * 按学生ID查询所有命中日志
     */
    default List<PositiveRuleExecLogDO> selectListByStudent(Long studentId) {
        return selectList(new LambdaQueryWrapperX<PositiveRuleExecLogDO>()
                .eqIfPresent(PositiveRuleExecLogDO::getStudentId, studentId)
                .orderByDesc(PositiveRuleExecLogDO::getMatchedAt));
    }

    /**
     * 按时间范围查询命中日志（用于规则调优分析）
     */
    default List<PositiveRuleExecLogDO> selectListByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        return selectList(new LambdaQueryWrapperX<PositiveRuleExecLogDO>()
                .geIfPresent(PositiveRuleExecLogDO::getMatchedAt, startTime)
                .leIfPresent(PositiveRuleExecLogDO::getMatchedAt, endTime)
                .orderByDesc(PositiveRuleExecLogDO::getMatchedAt));
    }

    /**
     * 统计规则命中次数
     */
    default Long countByRule(Long ruleId) {
        return selectCount(new LambdaQueryWrapperX<PositiveRuleExecLogDO>()
                .eqIfPresent(PositiveRuleExecLogDO::getRuleId, ruleId)
                .eqIfPresent(PositiveRuleExecLogDO::getIsPositive, 1));
    }

}

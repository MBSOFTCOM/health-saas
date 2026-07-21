package cn.iocoder.yudao.module.childhealth.dal.mysql.report;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.report.ReportGenerationTaskDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 报告生成任务 Mapper
 *
 * 创建日期: 2026-07-20
 */
@Mapper
public interface ReportGenerationTaskMapper extends BaseMapperX<ReportGenerationTaskDO> {

    /**
     * 按任务编号查询
     */
    default ReportGenerationTaskDO selectByTaskNo(String taskNo) {
        return selectOne(ReportGenerationTaskDO::getTaskNo, taskNo);
    }

    /**
     * 分页查询（按模板/类型/状态/批次过滤）
     */
    default PageResult<ReportGenerationTaskDO> selectPage(Long templateId, Integer reportType,
                                                          Integer taskStatus, Long batchId, PageParam pageParam) {
        return selectPage(pageParam, new LambdaQueryWrapperX<ReportGenerationTaskDO>()
                .eqIfPresent(ReportGenerationTaskDO::getTemplateId, templateId)
                .eqIfPresent(ReportGenerationTaskDO::getReportType, reportType)
                .eqIfPresent(ReportGenerationTaskDO::getTaskStatus, taskStatus)
                .eqIfPresent(ReportGenerationTaskDO::getBatchId, batchId)
                .orderByDesc(ReportGenerationTaskDO::getId));
    }

    /**
     * 查询待生成任务
     */
    default List<ReportGenerationTaskDO> selectPendingList() {
        return selectList(new LambdaQueryWrapperX<ReportGenerationTaskDO>()
                .eqIfPresent(ReportGenerationTaskDO::getTaskStatus, 0)
                .orderByAsc(ReportGenerationTaskDO::getId));
    }

    /**
     * 按目标ID查询所有任务（学生/学校/区域）
     */
    default List<ReportGenerationTaskDO> selectListByTarget(Integer reportType, Long targetId) {
        return selectList(new LambdaQueryWrapperX<ReportGenerationTaskDO>()
                .eqIfPresent(ReportGenerationTaskDO::getReportType, reportType)
                .eqIfPresent(ReportGenerationTaskDO::getTargetId, targetId)
                .orderByDesc(ReportGenerationTaskDO::getId));
    }

    /**
     * 按批次查询所有生成任务
     */
    default List<ReportGenerationTaskDO> selectListByBatch(Long batchId) {
        return selectList(new LambdaQueryWrapperX<ReportGenerationTaskDO>()
                .eqIfPresent(ReportGenerationTaskDO::getBatchId, batchId)
                .orderByDesc(ReportGenerationTaskDO::getId));
    }

    /**
     * 按批次统计已成功生成的报告数
     */
    default Long countSuccessByBatch(Long batchId) {
        return selectCount(new LambdaQueryWrapperX<ReportGenerationTaskDO>()
                .eqIfPresent(ReportGenerationTaskDO::getBatchId, batchId)
                .eqIfPresent(ReportGenerationTaskDO::getTaskStatus, 2));
    }

}

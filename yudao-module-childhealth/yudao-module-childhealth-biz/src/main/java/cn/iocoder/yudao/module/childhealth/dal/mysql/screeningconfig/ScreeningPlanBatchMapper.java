package cn.iocoder.yudao.module.childhealth.dal.mysql.screeningconfig;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.controller.admin.screeningconfig.vo.ScreeningPlanBatchPageReqVO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig.ScreeningPlanBatchDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

/**
 * 体检方案与批次关联 Mapper
 *
 * 模块: C.五健筛查配置
 */
@Mapper
public interface ScreeningPlanBatchMapper extends BaseMapperX<ScreeningPlanBatchDO> {

    /**
     * 分页查询（支持方案/批次/学校/年级/班级/状态过滤）
     */
    default PageResult<ScreeningPlanBatchDO> selectPage(ScreeningPlanBatchPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ScreeningPlanBatchDO>()
                .eqIfPresent(ScreeningPlanBatchDO::getPlanId, reqVO.getPlanId())
                .eqIfPresent(ScreeningPlanBatchDO::getBatchId, reqVO.getBatchId())
                .eqIfPresent(ScreeningPlanBatchDO::getSchoolId, reqVO.getSchoolId())
                .eqIfPresent(ScreeningPlanBatchDO::getGradeId, reqVO.getGradeId())
                .eqIfPresent(ScreeningPlanBatchDO::getClassId, reqVO.getClassId())
                .eqIfPresent(ScreeningPlanBatchDO::getCompletionStatus, reqVO.getCompletionStatus())
                .orderByDesc(ScreeningPlanBatchDO::getId));
    }

    /**
     * 按方案ID查询所有关联批次
     */
    default List<ScreeningPlanBatchDO> selectListByPlanId(Long planId) {
        return selectList(ScreeningPlanBatchDO::getPlanId, planId);
    }

    /**
     * 按批次ID查询所有关联方案（执行单元列表）
     */
    default List<ScreeningPlanBatchDO> selectListByBatchId(Long batchId) {
        return selectList(new LambdaQueryWrapperX<ScreeningPlanBatchDO>()
                .eqIfPresent(ScreeningPlanBatchDO::getBatchId, batchId)
                .orderByAsc(ScreeningPlanBatchDO::getSchoolId)
                .orderByAsc(ScreeningPlanBatchDO::getGradeId)
                .orderByAsc(ScreeningPlanBatchDO::getClassId));
    }

    /**
     * 按学校ID查询关联记录
     */
    default List<ScreeningPlanBatchDO> selectListBySchoolId(Long schoolId) {
        return selectList(new LambdaQueryWrapperX<ScreeningPlanBatchDO>()
                .eqIfPresent(ScreeningPlanBatchDO::getSchoolId, schoolId)
                .orderByDesc(ScreeningPlanBatchDO::getScreeningStart));
    }

    /**
     * 按班级ID查询关联记录
     */
    default List<ScreeningPlanBatchDO> selectListByClassId(Long classId) {
        return selectList(ScreeningPlanBatchDO::getClassId, classId);
    }

    /**
     * 按完成状态查询
     */
    default List<ScreeningPlanBatchDO> selectListByCompletionStatus(Integer completionStatus) {
        return selectList(new LambdaQueryWrapperX<ScreeningPlanBatchDO>()
                .eqIfPresent(ScreeningPlanBatchDO::getCompletionStatus, completionStatus)
                .orderByDesc(ScreeningPlanBatchDO::getScreeningStart));
    }

    /**
     * 按筛查日期范围查询
     */
    default List<ScreeningPlanBatchDO> selectListByDateRange(LocalDate startDate, LocalDate endDate) {
        return selectList(new LambdaQueryWrapperX<ScreeningPlanBatchDO>()
                .geIfPresent(ScreeningPlanBatchDO::getScreeningStart, startDate)
                .leIfPresent(ScreeningPlanBatchDO::getScreeningEnd, endDate)
                .orderByAsc(ScreeningPlanBatchDO::getScreeningStart));
    }

}

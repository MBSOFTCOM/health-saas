package cn.iocoder.yudao.module.childhealth.controller.admin.report;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.controller.admin.report.vo.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.report.ReportGenerationTaskDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.report.ReportGenerationTaskMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.REPORT_GENERATION_TASK_NOT_EXISTS;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.REPORT_GENERATION_TASK_NO_DUPLICATE;

/**
 * 管理后台 - 报告生成任务
 *
 * 创建日期: 2026-07-20
 * 模块: 19. 多维度报告体系
 */
@Tag(name = "管理后台 - 报告生成任务")
@RestController
@RequestMapping("/childhealth/report-generation-task")
@Validated
public class ReportGenerationTaskController {

    @Resource
    private ReportGenerationTaskMapper reportGenerationTaskMapper;

    @PostMapping("/create")
    @Operation(summary = "创建报告生成任务")
    @PreAuthorize("@ss.hasPermission('childhealth:report-generation-task:create')")
    public CommonResult<Long> create(@Valid @RequestBody ReportGenerationTaskSaveReqVO createReqVO) {
        validateTaskNoUnique(null, createReqVO.getTaskNo());
        ReportGenerationTaskDO task = BeanUtils.toBean(createReqVO, ReportGenerationTaskDO.class);
        reportGenerationTaskMapper.insert(task);
        return success(task.getId());
    }

    @PutMapping("/update")
    @Operation(summary = "更新报告生成任务")
    @PreAuthorize("@ss.hasPermission('childhealth:report-generation-task:update')")
    public CommonResult<Boolean> update(@Valid @RequestBody ReportGenerationTaskSaveReqVO updateReqVO) {
        validateExists(updateReqVO.getId());
        validateTaskNoUnique(updateReqVO.getId(), updateReqVO.getTaskNo());
        ReportGenerationTaskDO updateObj = BeanUtils.toBean(updateReqVO, ReportGenerationTaskDO.class);
        reportGenerationTaskMapper.updateById(updateObj);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除报告生成任务")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:report-generation-task:delete')")
    public CommonResult<Boolean> delete(@RequestParam("id") Long id) {
        validateExists(id);
        reportGenerationTaskMapper.deleteById(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得报告生成任务详情")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:report-generation-task:query')")
    public CommonResult<ReportGenerationTaskRespVO> get(@RequestParam("id") Long id) {
        ReportGenerationTaskDO task = reportGenerationTaskMapper.selectById(id);
        return success(BeanUtils.toBean(task, ReportGenerationTaskRespVO.class));
    }

    @GetMapping("/get-by-no")
    @Operation(summary = "按任务编号查询")
    @Parameter(name = "taskNo", description = "任务编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:report-generation-task:query')")
    public CommonResult<ReportGenerationTaskRespVO> getByNo(@RequestParam("taskNo") String taskNo) {
        ReportGenerationTaskDO task = reportGenerationTaskMapper.selectByTaskNo(taskNo);
        return success(BeanUtils.toBean(task, ReportGenerationTaskRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得报告生成任务分页")
    @PreAuthorize("@ss.hasPermission('childhealth:report-generation-task:query')")
    public CommonResult<PageResult<ReportGenerationTaskRespVO>> page(@Valid ReportGenerationTaskPageReqVO pageReqVO) {
        PageResult<ReportGenerationTaskDO> pageResult = reportGenerationTaskMapper.selectPage(pageReqVO,
                new LambdaQueryWrapperX<ReportGenerationTaskDO>()
                        .likeIfPresent(ReportGenerationTaskDO::getTaskNo, pageReqVO.getTaskNo())
                        .eqIfPresent(ReportGenerationTaskDO::getTemplateId, pageReqVO.getTemplateId())
                        .eqIfPresent(ReportGenerationTaskDO::getReportType, pageReqVO.getReportType())
                        .eqIfPresent(ReportGenerationTaskDO::getTaskStatus, pageReqVO.getTaskStatus())
                        .eqIfPresent(ReportGenerationTaskDO::getBatchId, pageReqVO.getBatchId())
                        .geIfPresent(ReportGenerationTaskDO::getCreateTime, pageReqVO.getCreateTimeStart())
                        .leIfPresent(ReportGenerationTaskDO::getCreateTime, pageReqVO.getCreateTimeEnd())
                        .orderByDesc(ReportGenerationTaskDO::getId));
        return success(BeanUtils.toBean(pageResult, ReportGenerationTaskRespVO.class));
    }

    @GetMapping("/list-by-target")
    @Operation(summary = "按目标ID查询所有任务（学生/学校/区域）")
    @PreAuthorize("@ss.hasPermission('childhealth:report-generation-task:query')")
    public CommonResult<List<ReportGenerationTaskRespVO>> listByTarget(
            @RequestParam("reportType") Integer reportType,
            @RequestParam("targetId") Long targetId) {
        List<ReportGenerationTaskDO> list = reportGenerationTaskMapper.selectListByTarget(reportType, targetId);
        return success(BeanUtils.toBean(list, ReportGenerationTaskRespVO.class));
    }

    @GetMapping("/list-by-batch")
    @Operation(summary = "按批次查询所有生成任务")
    @Parameter(name = "batchId", description = "批次ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:report-generation-task:query')")
    public CommonResult<List<ReportGenerationTaskRespVO>> listByBatch(@RequestParam("batchId") Long batchId) {
        List<ReportGenerationTaskDO> list = reportGenerationTaskMapper.selectListByBatch(batchId);
        return success(BeanUtils.toBean(list, ReportGenerationTaskRespVO.class));
    }

    @PostMapping("/retry")
    @Operation(summary = "失败任务重试")
    @Parameter(name = "id", description = "任务ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:report-generation-task:push')")
    public CommonResult<Boolean> retry(@RequestParam("id") Long id) {
        validateExists(id);
        ReportGenerationTaskDO task = reportGenerationTaskMapper.selectById(id);
        ReportGenerationTaskDO update = new ReportGenerationTaskDO();
        update.setId(id);
        update.setTaskStatus(0);
        update.setProgress(0);
        update.setErrorMsg(null);
        update.setRetryCount(task.getRetryCount() + 1);
        reportGenerationTaskMapper.updateById(update);
        return success(true);
    }

    @PostMapping("/cancel")
    @Operation(summary = "取消生成任务")
    @Parameter(name = "id", description = "任务ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:report-generation-task:push')")
    public CommonResult<Boolean> cancel(@RequestParam("id") Long id) {
        validateExists(id);
        ReportGenerationTaskDO update = new ReportGenerationTaskDO();
        update.setId(id);
        update.setTaskStatus(4);
        reportGenerationTaskMapper.updateById(update);
        return success(true);
    }

    @GetMapping("/pending-list")
    @Operation(summary = "查询待生成任务列表")
    @PreAuthorize("@ss.hasPermission('childhealth:report-generation-task:query')")
    public CommonResult<List<ReportGenerationTaskRespVO>> pendingList() {
        List<ReportGenerationTaskDO> list = reportGenerationTaskMapper.selectPendingList();
        return success(BeanUtils.toBean(list, ReportGenerationTaskRespVO.class));
    }

    private void validateExists(Long id) {
        if (id == null || reportGenerationTaskMapper.selectById(id) == null) {
            throw exception(REPORT_GENERATION_TASK_NOT_EXISTS);
        }
    }

    private void validateTaskNoUnique(Long id, String taskNo) {
        ReportGenerationTaskDO task = reportGenerationTaskMapper.selectByTaskNo(taskNo);
        if (task == null) {
            return;
        }
        if (id == null || !task.getId().equals(id)) {
            throw exception(REPORT_GENERATION_TASK_NO_DUPLICATE);
        }
    }

}

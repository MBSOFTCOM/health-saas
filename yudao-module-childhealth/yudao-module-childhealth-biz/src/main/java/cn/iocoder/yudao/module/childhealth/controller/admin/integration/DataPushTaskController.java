package cn.iocoder.yudao.module.childhealth.controller.admin.integration;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.integration.vo.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.integration.DataPushTaskDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.integration.DataPushTaskMapper;
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
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.*;

/**
 * 管理后台 - 数据上报任务
 *
 * 创建日期: 2026-07-20
 * 模块: 2. 数据上报对接
 */
@Tag(name = "管理后台 - 数据上报任务")
@RestController
@RequestMapping("/childhealth/data-push-task")
@Validated
public class DataPushTaskController {

    @Resource
    private DataPushTaskMapper dataPushTaskMapper;

    @PostMapping("/create")
    @Operation(summary = "创建数据上报任务")
    @PreAuthorize("@ss.hasPermission('childhealth:data-push-task:create')")
    public CommonResult<Long> create(@Valid @RequestBody DataPushTaskSaveReqVO createReqVO) {
        validateTaskNoUnique(null, createReqVO.getTaskNo());
        DataPushTaskDO task = BeanUtils.toBean(createReqVO, DataPushTaskDO.class);
        dataPushTaskMapper.insert(task);
        return success(task.getId());
    }

    @PutMapping("/update")
    @Operation(summary = "更新数据上报任务")
    @PreAuthorize("@ss.hasPermission('childhealth:data-push-task:update')")
    public CommonResult<Boolean> update(@Valid @RequestBody DataPushTaskSaveReqVO updateReqVO) {
        validateExists(updateReqVO.getId());
        validateTaskNoUnique(updateReqVO.getId(), updateReqVO.getTaskNo());
        DataPushTaskDO updateObj = BeanUtils.toBean(updateReqVO, DataPushTaskDO.class);
        dataPushTaskMapper.updateById(updateObj);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除数据上报任务")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:data-push-task:delete')")
    public CommonResult<Boolean> delete(@RequestParam("id") Long id) {
        validateExists(id);
        dataPushTaskMapper.deleteById(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得数据上报任务详情")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:data-push-task:query')")
    public CommonResult<DataPushTaskRespVO> get(@RequestParam("id") Long id) {
        DataPushTaskDO task = dataPushTaskMapper.selectById(id);
        return success(BeanUtils.toBean(task, DataPushTaskRespVO.class));
    }

    @GetMapping("/get-by-no")
    @Operation(summary = "按任务编号查询")
    @Parameter(name = "taskNo", description = "任务编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:data-push-task:query')")
    public CommonResult<DataPushTaskRespVO> getByNo(@RequestParam("taskNo") String taskNo) {
        DataPushTaskDO task = dataPushTaskMapper.selectByTaskNo(taskNo);
        return success(BeanUtils.toBean(task, DataPushTaskRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得数据上报任务分页")
    @PreAuthorize("@ss.hasPermission('childhealth:data-push-task:query')")
    public CommonResult<PageResult<DataPushTaskRespVO>> page(@Valid DataPushTaskPageReqVO pageReqVO) {
        PageResult<DataPushTaskDO> pageResult = dataPushTaskMapper.selectPage(pageReqVO,
                new cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX<DataPushTaskDO>()
                        .likeIfPresent(DataPushTaskDO::getTaskNo, pageReqVO.getTaskNo())
                        .eqIfPresent(DataPushTaskDO::getTargetSystem, pageReqVO.getTargetSystem())
                        .eqIfPresent(DataPushTaskDO::getBusinessType, pageReqVO.getBusinessType())
                        .eqIfPresent(DataPushTaskDO::getPushStatus, pageReqVO.getPushStatus())
                        .eqIfPresent(DataPushTaskDO::getBatchId, pageReqVO.getBatchId())
                        .eqIfPresent(DataPushTaskDO::getPushMode, pageReqVO.getPushMode())
                        .geIfPresent(DataPushTaskDO::getCreateTime, pageReqVO.getCreateTimeStart())
                        .leIfPresent(DataPushTaskDO::getCreateTime, pageReqVO.getCreateTimeEnd())
                        .orderByDesc(DataPushTaskDO::getId));
        return success(BeanUtils.toBean(pageResult, DataPushTaskRespVO.class));
    }

    @PostMapping("/retry")
    @Operation(summary = "失败重推")
    @Parameter(name = "id", description = "任务ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:data-push-task:push')")
    public CommonResult<Boolean> retry(@RequestParam("id") Long id) {
        validateExists(id);
        // 重置状态为待推送，重试次数+1
        DataPushTaskDO task = dataPushTaskMapper.selectById(id);
        DataPushTaskDO update = new DataPushTaskDO();
        update.setId(id);
        update.setPushStatus(0);
        update.setRetryCount(task.getRetryCount() + 1);
        update.setErrorMsg(null);
        dataPushTaskMapper.updateById(update);
        return success(true);
    }

    @PostMapping("/cancel")
    @Operation(summary = "取消推送")
    @Parameter(name = "id", description = "任务ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:data-push-task:push')")
    public CommonResult<Boolean> cancel(@RequestParam("id") Long id) {
        validateExists(id);
        DataPushTaskDO update = new DataPushTaskDO();
        update.setId(id);
        update.setPushStatus(5);
        dataPushTaskMapper.updateById(update);
        return success(true);
    }

    @GetMapping("/pending-list")
    @Operation(summary = "查询待推送任务列表")
    @PreAuthorize("@ss.hasPermission('childhealth:data-push-task:query')")
    public CommonResult<List<DataPushTaskRespVO>> pendingList() {
        List<DataPushTaskDO> list = dataPushTaskMapper.selectPendingList();
        return success(BeanUtils.toBean(list, DataPushTaskRespVO.class));
    }

    @GetMapping("/list-by-batch")
    @Operation(summary = "按批次查询上报任务")
    @Parameter(name = "batchId", description = "批次ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:data-push-task:query')")
    public CommonResult<List<DataPushTaskRespVO>> listByBatch(@RequestParam("batchId") Long batchId) {
        List<DataPushTaskDO> list = dataPushTaskMapper.selectListByBatch(batchId);
        return success(BeanUtils.toBean(list, DataPushTaskRespVO.class));
    }

    private void validateExists(Long id) {
        if (id == null || dataPushTaskMapper.selectById(id) == null) {
            throw exception(DATA_PUSH_TASK_NOT_EXISTS);
        }
    }

    private void validateTaskNoUnique(Long id, String taskNo) {
        DataPushTaskDO task = dataPushTaskMapper.selectByTaskNo(taskNo);
        if (task == null) {
            return;
        }
        if (id == null || !task.getId().equals(id)) {
            throw exception(DATA_PUSH_TASK_NO_DUPLICATE);
        }
    }

}

package cn.iocoder.yudao.module.childhealth.controller.admin.integration;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.controller.admin.integration.vo.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.integration.DataPushFailLogDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.integration.DataPushFailLogMapper;
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
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.DATA_PUSH_FAIL_LOG_NOT_EXISTS;

/**
 * 管理后台 - 数据上报失败日志
 *
 * 创建日期: 2026-07-20
 * 模块: 2. 数据上报对接
 */
@Tag(name = "管理后台 - 数据上报失败日志")
@RestController
@RequestMapping("/childhealth/data-push-fail-log")
@Validated
public class DataPushFailLogController {

    @Resource
    private DataPushFailLogMapper dataPushFailLogMapper;

    @PostMapping("/create")
    @Operation(summary = "创建失败日志")
    @PreAuthorize("@ss.hasPermission('childhealth:data-push-fail-log:create')")
    public CommonResult<Long> create(@Valid @RequestBody DataPushFailLogSaveReqVO createReqVO) {
        DataPushFailLogDO log = BeanUtils.toBean(createReqVO, DataPushFailLogDO.class);
        dataPushFailLogMapper.insert(log);
        return success(log.getId());
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除失败日志")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:data-push-fail-log:delete')")
    public CommonResult<Boolean> delete(@RequestParam("id") Long id) {
        validateExists(id);
        dataPushFailLogMapper.deleteById(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得失败日志详情")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:data-push-fail-log:query')")
    public CommonResult<DataPushFailLogRespVO> get(@RequestParam("id") Long id) {
        DataPushFailLogDO log = dataPushFailLogMapper.selectById(id);
        return success(BeanUtils.toBean(log, DataPushFailLogRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得失败日志分页")
    @PreAuthorize("@ss.hasPermission('childhealth:data-push-fail-log:query')")
    public CommonResult<PageResult<DataPushFailLogRespVO>> page(@Valid DataPushFailLogPageReqVO pageReqVO) {
        PageResult<DataPushFailLogDO> pageResult = dataPushFailLogMapper.selectPage(pageReqVO,
                new LambdaQueryWrapperX<DataPushFailLogDO>()
                        .eqIfPresent(DataPushFailLogDO::getTaskId, pageReqVO.getTaskId())
                        .eqIfPresent(DataPushFailLogDO::getErrorType, pageReqVO.getErrorType())
                        .likeIfPresent(DataPushFailLogDO::getErrorCode, pageReqVO.getErrorCode())
                        .geIfPresent(DataPushFailLogDO::getFailTime, pageReqVO.getFailTimeStart())
                        .leIfPresent(DataPushFailLogDO::getFailTime, pageReqVO.getFailTimeEnd())
                        .orderByDesc(DataPushFailLogDO::getFailTime));
        return success(BeanUtils.toBean(pageResult, DataPushFailLogRespVO.class));
    }

    @GetMapping("/list-by-task")
    @Operation(summary = "按任务ID查询所有失败日志")
    @Parameter(name = "taskId", description = "任务ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:data-push-fail-log:query')")
    public CommonResult<List<DataPushFailLogRespVO>> listByTask(@RequestParam("taskId") Long taskId) {
        List<DataPushFailLogDO> list = dataPushFailLogMapper.selectListByTask(taskId);
        return success(BeanUtils.toBean(list, DataPushFailLogRespVO.class));
    }

    private void validateExists(Long id) {
        if (id == null || dataPushFailLogMapper.selectById(id) == null) {
            throw exception(DATA_PUSH_FAIL_LOG_NOT_EXISTS);
        }
    }

}

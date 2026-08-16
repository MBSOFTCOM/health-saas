package cn.iocoder.yudao.module.childhealth.controller.admin.screeningconfig;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.screeningconfig.vo.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig.ScreeningPlanBatchDO;
import cn.iocoder.yudao.module.childhealth.service.screeningconfig.ScreeningPlanBatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 体检方案与批次关联")
@RestController
@RequestMapping("/childhealth/screening-plan-batch")
@Validated
public class ScreeningPlanBatchController {

    @Resource
    private ScreeningPlanBatchService screeningPlanBatchService;

    @PostMapping("/create")
    @Operation(summary = "创建方案批次关联")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-plan-batch:create')")
    public CommonResult<Long> createScreeningPlanBatch(@Valid @RequestBody ScreeningPlanBatchSaveReqVO createReqVO) {
        return success(screeningPlanBatchService.createScreeningPlanBatch(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新方案批次关联")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-plan-batch:update')")
    public CommonResult<Boolean> updateScreeningPlanBatch(@Valid @RequestBody ScreeningPlanBatchSaveReqVO updateReqVO) {
        screeningPlanBatchService.updateScreeningPlanBatch(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除方案批次关联")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:screening-plan-batch:delete')")
    public CommonResult<Boolean> deleteScreeningPlanBatch(@RequestParam("id") Long id) {
        screeningPlanBatchService.deleteScreeningPlanBatch(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得方案批次关联")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-plan-batch:query')")
    public CommonResult<ScreeningPlanBatchRespVO> getScreeningPlanBatch(@RequestParam("id") Long id) {
        ScreeningPlanBatchDO screeningPlanBatch = screeningPlanBatchService.getScreeningPlanBatch(id);
        return success(BeanUtils.toBean(screeningPlanBatch, ScreeningPlanBatchRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得方案批次关联分页")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-plan-batch:query')")
    public CommonResult<PageResult<ScreeningPlanBatchRespVO>> getScreeningPlanBatchPage(@Valid ScreeningPlanBatchPageReqVO pageReqVO) {
        PageResult<ScreeningPlanBatchDO> pageResult = screeningPlanBatchService.getScreeningPlanBatchPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ScreeningPlanBatchRespVO.class));
    }

    @GetMapping("/list-by-batch")
    @Operation(summary = "按批次查询所有执行单元（含方案/班级/状态）")
    @Parameter(name = "batchId", description = "批次ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:screening-plan-batch:query')")
    public CommonResult<List<ScreeningPlanBatchRespVO>> listByBatch(@RequestParam("batchId") Long batchId) {
        List<ScreeningPlanBatchDO> list = screeningPlanBatchService.selectListByBatchId(batchId);
        return success(BeanUtils.toBean(list, ScreeningPlanBatchRespVO.class));
    }

    @PutMapping("/batch-update-status")
    @Operation(summary = "批量更新完成状态（统一管理用）")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-plan-batch:update')")
    public CommonResult<Boolean> batchUpdateStatus(@RequestBody Map<String, Object> body) {
        @SuppressWarnings("unchecked")
        List<Long> ids = (List<Long>) body.get("ids");
        Integer status = (Integer) body.get("status");
        screeningPlanBatchService.batchUpdateCompletionStatus(ids, status);
        return success(true);
    }

    @GetMapping("/status-statistics")
    @Operation(summary = "按批次统计各完成状态数量")
    @Parameter(name = "batchId", description = "批次ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:screening-plan-batch:query')")
    public CommonResult<Map<Integer, Long>> statusStatistics(@RequestParam("batchId") Long batchId) {
        return success(screeningPlanBatchService.statusStatisticsByBatch(batchId));
    }

}

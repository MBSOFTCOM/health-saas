package cn.iocoder.yudao.module.childhealth.controller.admin.screening;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.batch.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.ScreeningBatchDO;
import cn.iocoder.yudao.module.childhealth.service.screening.ScreeningBatchService;
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

@Tag(name = "管理后台 - 筛查批次")
@RestController
@RequestMapping("/childhealth/screening-batch")
@Validated
public class ScreeningBatchController {

    @Resource
    private ScreeningBatchService screeningBatchService;

    @PostMapping("/create")
    @Operation(summary = "创建筛查批次")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-batch:create')")
    public CommonResult<Long> createScreeningBatch(@Valid @RequestBody ScreeningBatchSaveReqVO createReqVO) {
        return success(screeningBatchService.createScreeningBatch(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新筛查批次")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-batch:update')")
    public CommonResult<Boolean> updateScreeningBatch(@Valid @RequestBody ScreeningBatchSaveReqVO updateReqVO) {
        screeningBatchService.updateScreeningBatch(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除筛查批次")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:screening-batch:delete')")
    public CommonResult<Boolean> deleteScreeningBatch(@RequestParam("id") Long id) {
        screeningBatchService.deleteScreeningBatch(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得筛查批次")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-batch:query')")
    public CommonResult<ScreeningBatchRespVO> getScreeningBatch(@RequestParam("id") Long id) {
        ScreeningBatchDO screeningBatch = screeningBatchService.getScreeningBatch(id);
        return success(BeanUtils.toBean(screeningBatch, ScreeningBatchRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得筛查批次分页")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-batch:query')")
    public CommonResult<PageResult<ScreeningBatchRespVO>> getScreeningBatchPage(@Valid ScreeningBatchPageReqVO pageReqVO) {
        PageResult<ScreeningBatchDO> pageResult = screeningBatchService.getScreeningBatchPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ScreeningBatchRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得筛查批次列表")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-batch:query')")
    public CommonResult<List<ScreeningBatchRespVO>> getScreeningBatchList(@Valid ScreeningBatchListReqVO listReqVO) {
        List<ScreeningBatchDO> list = screeningBatchService.getScreeningBatchList(listReqVO);
        return success(BeanUtils.toBean(list, ScreeningBatchRespVO.class));
    }

    @PutMapping("/update-status")
    @Operation(summary = "更新筛查批次状态")
    @Parameter(name = "id", description = "编号", required = true)
    @Parameter(name = "status", description = "状态", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:screening-batch:update')")
    public CommonResult<Boolean> updateBatchStatus(@RequestParam("id") Long id,
                                                    @RequestParam("status") Integer status) {
        screeningBatchService.updateBatchStatus(id, status);
        return success(true);
    }

}
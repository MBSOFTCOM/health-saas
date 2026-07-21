package cn.iocoder.yudao.module.childhealth.controller.admin.screening;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.positive.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.ScreeningPositiveDO;
import cn.iocoder.yudao.module.childhealth.service.screening.ScreeningPositiveService;
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

@Tag(name = "管理后台 - 筛查阳性记录")
@RestController
@RequestMapping("/childhealth/screening-positive")
@Validated
public class ScreeningPositiveController {

    @Resource
    private ScreeningPositiveService screeningPositiveService;

    @PostMapping("/create")
    @Operation(summary = "创建筛查阳性记录")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-positive:create')")
    public CommonResult<Long> createScreeningPositive(@Valid @RequestBody ScreeningPositiveSaveReqVO createReqVO) {
        return success(screeningPositiveService.createScreeningPositive(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新筛查阳性记录")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-positive:update')")
    public CommonResult<Boolean> updateScreeningPositive(@Valid @RequestBody ScreeningPositiveSaveReqVO updateReqVO) {
        screeningPositiveService.updateScreeningPositive(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除筛查阳性记录")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:screening-positive:delete')")
    public CommonResult<Boolean> deleteScreeningPositive(@RequestParam("id") Long id) {
        screeningPositiveService.deleteScreeningPositive(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得筛查阳性记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-positive:query')")
    public CommonResult<ScreeningPositiveRespVO> getScreeningPositive(@RequestParam("id") Long id) {
        ScreeningPositiveDO screeningPositive = screeningPositiveService.getScreeningPositive(id);
        return success(BeanUtils.toBean(screeningPositive, ScreeningPositiveRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得筛查阳性记录分页")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-positive:query')")
    public CommonResult<PageResult<ScreeningPositiveRespVO>> getScreeningPositivePage(@Valid ScreeningPositivePageReqVO pageReqVO) {
        PageResult<ScreeningPositiveDO> pageResult = screeningPositiveService.getScreeningPositivePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ScreeningPositiveRespVO.class));
    }

    @GetMapping("/list-by-record")
    @Operation(summary = "根据筛查记录ID获取阳性记录列表")
    @Parameter(name = "recordId", description = "筛查记录ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:screening-positive:query')")
    public CommonResult<List<ScreeningPositiveRespVO>> getByRecordId(@RequestParam("recordId") Long recordId) {
        List<ScreeningPositiveDO> list = screeningPositiveService.getScreeningPositiveByRecordId(recordId);
        return success(BeanUtils.toBean(list, ScreeningPositiveRespVO.class));
    }

    @PutMapping("/update-recheck-status")
    @Operation(summary = "更新复筛状态")
    @Parameter(name = "id", description = "编号", required = true)
    @Parameter(name = "recheckStatus", description = "复筛状态", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:screening-positive:update')")
    public CommonResult<Boolean> updateRecheckStatus(@RequestParam("id") Long id,
                                                      @RequestParam("recheckStatus") Integer recheckStatus) {
        screeningPositiveService.updateRecheckStatus(id, recheckStatus);
        return success(true);
    }

}
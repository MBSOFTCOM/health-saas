package cn.iocoder.yudao.module.childhealth.controller.admin.screening;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.resultDetail.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.ScreeningResultDetailDO;
import cn.iocoder.yudao.module.childhealth.service.screening.ScreeningResultDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 筛查结果明细")
@RestController
@RequestMapping("/childhealth/screeningResultDetail")
@Validated
public class ScreeningResultDetailController {

    @Resource
    private ScreeningResultDetailService screeningResultDetailService;

    @PostMapping("/create")
    @Operation(summary = "创建筛查结果明细")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-result-detail:create')")
    public CommonResult<Long> createScreeningResultDetail(@Valid @RequestBody ScreeningResultDetailSaveReqVO createReqVO) {
        return success(screeningResultDetailService.createScreeningResultDetail(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新筛查结果明细")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-result-detail:update')")
    public CommonResult<Boolean> updateScreeningResultDetail(@Valid @RequestBody ScreeningResultDetailSaveReqVO updateReqVO) {
        screeningResultDetailService.updateScreeningResultDetail(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除筛查结果明细")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:screening-result-detail:delete')")
    public CommonResult<Boolean> deleteScreeningResultDetail(@RequestParam("id") Long id) {
        screeningResultDetailService.deleteScreeningResultDetail(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得筛查结果明细")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-result-detail:query')")
    public CommonResult<ScreeningResultDetailRespVO> getScreeningResultDetail(@RequestParam("id") Long id) {
        ScreeningResultDetailDO screeningResultDetail = screeningResultDetailService.getScreeningResultDetail(id);
        return success(BeanUtils.toBean(screeningResultDetail, ScreeningResultDetailRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得筛查结果明细分页")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-result-detail:query')")
    public CommonResult<PageResult<ScreeningResultDetailRespVO>> getScreeningResultDetailPage(@Valid ScreeningResultDetailPageReqVO pageReqVO) {
        PageResult<ScreeningResultDetailDO> pageResult = screeningResultDetailService.getScreeningResultDetailPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ScreeningResultDetailRespVO.class));
    }

}
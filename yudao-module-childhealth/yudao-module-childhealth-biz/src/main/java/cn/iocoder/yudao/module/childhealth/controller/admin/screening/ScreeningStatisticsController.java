package cn.iocoder.yudao.module.childhealth.controller.admin.screening;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.statistics.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.ScreeningStatisticsDO;
import cn.iocoder.yudao.module.childhealth.service.screening.ScreeningStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 筛查统计")
@RestController
@RequestMapping("/childhealth/screeningStatistics")
@Validated
public class ScreeningStatisticsController {

    @Resource
    private ScreeningStatisticsService screeningStatisticsService;

    @PostMapping("/create")
    @Operation(summary = "创建筛查统计")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-statistics:create')")
    public CommonResult<Long> createScreeningStatistics(@Valid @RequestBody ScreeningStatisticsSaveReqVO createReqVO) {
        return success(screeningStatisticsService.createScreeningStatistics(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新筛查统计")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-statistics:update')")
    public CommonResult<Boolean> updateScreeningStatistics(@Valid @RequestBody ScreeningStatisticsSaveReqVO updateReqVO) {
        screeningStatisticsService.updateScreeningStatistics(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除筛查统计")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:screening-statistics:delete')")
    public CommonResult<Boolean> deleteScreeningStatistics(@RequestParam("id") Long id) {
        screeningStatisticsService.deleteScreeningStatistics(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得筛查统计")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-statistics:query')")
    public CommonResult<ScreeningStatisticsRespVO> getScreeningStatistics(@RequestParam("id") Long id) {
        ScreeningStatisticsDO screeningStatistics = screeningStatisticsService.getScreeningStatistics(id);
        return success(BeanUtils.toBean(screeningStatistics, ScreeningStatisticsRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得筛查统计分页")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-statistics:query')")
    public CommonResult<PageResult<ScreeningStatisticsRespVO>> getScreeningStatisticsPage(@Valid ScreeningStatisticsPageReqVO pageReqVO) {
        PageResult<ScreeningStatisticsDO> pageResult = screeningStatisticsService.getScreeningStatisticsPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ScreeningStatisticsRespVO.class));
    }

}
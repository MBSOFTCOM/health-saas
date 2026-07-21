package cn.iocoder.yudao.module.childhealth.controller.admin.dashboard;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.childhealth.controller.admin.dashboard.vo.*;
import cn.iocoder.yudao.module.childhealth.service.dashboard.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

/**
 * 管理后台 - 数据看板
 *
 * 创建日期: 2026-07-20
 * 模块: 1. 数据看板
 * 接口: 参检率/阳性率/复筛率/随访率 + 趋势图 + 多维度统计
 */
@Tag(name = "管理后台 - 数据看板")
@RestController
@RequestMapping("/childhealth/dashboard")
@Validated
public class DashboardController {

    @Resource
    private DashboardService dashboardService;

    @GetMapping("/overview")
    @Operation(summary = "看板总览（参检率/阳性率/复筛率/随访率）")
    @PreAuthorize("@ss.hasPermission('childhealth:dashboard:query')")
    public CommonResult<DashboardOverviewRespVO> getOverview(@Valid DashboardQueryReqVO reqVO) {
        return success(dashboardService.getOverview(reqVO));
    }

    @GetMapping("/trend")
    @Operation(summary = "趋势图（按日期展示参检率/阳性率/复筛率/随访率）")
    @PreAuthorize("@ss.hasPermission('childhealth:dashboard:query')")
    public CommonResult<List<DashboardTrendRespVO>> getTrend(@Valid DashboardQueryReqVO reqVO) {
        return success(dashboardService.getTrend(reqVO));
    }

    @GetMapping("/by-region")
    @Operation(summary = "按区域聚合统计")
    @PreAuthorize("@ss.hasPermission('childhealth:dashboard:query')")
    public CommonResult<List<DashboardCategoryRespVO>> getByRegion(@Valid DashboardQueryReqVO reqVO) {
        return success(dashboardService.getByRegion(reqVO));
    }

    @GetMapping("/by-school")
    @Operation(summary = "按学校聚合统计")
    @PreAuthorize("@ss.hasPermission('childhealth:dashboard:query')")
    public CommonResult<List<DashboardCategoryRespVO>> getBySchool(@Valid DashboardQueryReqVO reqVO) {
        return success(dashboardService.getBySchool(reqVO));
    }

    @GetMapping("/by-category")
    @Operation(summary = "按五健专项聚合（眼/口腔/骨骼/心理/体形）")
    @PreAuthorize("@ss.hasPermission('childhealth:dashboard:query')")
    public CommonResult<List<DashboardCategoryRespVO>> getByCategory(@Valid DashboardQueryReqVO reqVO) {
        return success(dashboardService.getByCategory(reqVO));
    }

}

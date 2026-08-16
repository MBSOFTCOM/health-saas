package cn.iocoder.yudao.module.childhealth.controller.admin.screeningconfig;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.screeningconfig.vo.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig.ScreeningPlanDO;
import cn.iocoder.yudao.module.childhealth.service.screeningconfig.ScreeningPlanService;
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

@Tag(name = "管理后台 - 体检方案配置")
@RestController
@RequestMapping("/childhealth/screening-plan")
@Validated
public class ScreeningPlanController {

    @Resource
    private ScreeningPlanService screeningPlanService;

    @PostMapping("/create")
    @Operation(summary = "创建体检方案")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-plan:create')")
    public CommonResult<Long> createScreeningPlan(@Valid @RequestBody ScreeningPlanSaveReqVO createReqVO) {
        return success(screeningPlanService.createScreeningPlan(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新体检方案")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-plan:update')")
    public CommonResult<Boolean> updateScreeningPlan(@Valid @RequestBody ScreeningPlanSaveReqVO updateReqVO) {
        screeningPlanService.updateScreeningPlan(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除体检方案")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:screening-plan:delete')")
    public CommonResult<Boolean> deleteScreeningPlan(@RequestParam("id") Long id) {
        screeningPlanService.deleteScreeningPlan(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得体检方案")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-plan:query')")
    public CommonResult<ScreeningPlanRespVO> getScreeningPlan(@RequestParam("id") Long id) {
        ScreeningPlanDO screeningPlan = screeningPlanService.getScreeningPlan(id);
        return success(BeanUtils.toBean(screeningPlan, ScreeningPlanRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得体检方案分页")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-plan:query')")
    public CommonResult<PageResult<ScreeningPlanRespVO>> getScreeningPlanPage(@Valid ScreeningPlanPageReqVO pageReqVO) {
        PageResult<ScreeningPlanDO> pageResult = screeningPlanService.getScreeningPlanPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ScreeningPlanRespVO.class));
    }

    @GetMapping("/get-default")
    @Operation(summary = "按类型获取默认方案")
    @Parameter(name = "planType", description = "方案类型 1五健 2基础体检 3入园入托", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-plan:query')")
    public CommonResult<ScreeningPlanRespVO> getDefaultPlan(@RequestParam("planType") Integer planType) {
        ScreeningPlanDO plan = screeningPlanService.selectDefaultPlan(planType);
        return success(BeanUtils.toBean(plan, ScreeningPlanRespVO.class));
    }

    @PutMapping("/set-default")
    @Operation(summary = "设置默认方案（同类型互斥）")
    @Parameter(name = "id", description = "方案ID", required = true)
    @Parameter(name = "planType", description = "方案类型", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:screening-plan:update')")
    public CommonResult<Boolean> setDefaultPlan(@RequestParam("id") Long id,
                                                @RequestParam("planType") Integer planType) {
        screeningPlanService.setDefaultPlan(id, planType);
        return success(true);
    }

    @GetMapping("/active-list")
    @Operation(summary = "获取所有启用的方案（下拉选择用）")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-plan:query')")
    public CommonResult<List<ScreeningPlanRespVO>> getActiveList() {
        List<ScreeningPlanDO> list = screeningPlanService.selectActiveList();
        return success(BeanUtils.toBean(list, ScreeningPlanRespVO.class));
    }

}

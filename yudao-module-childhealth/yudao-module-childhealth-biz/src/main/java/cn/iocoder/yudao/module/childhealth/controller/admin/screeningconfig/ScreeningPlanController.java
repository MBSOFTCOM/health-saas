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

}

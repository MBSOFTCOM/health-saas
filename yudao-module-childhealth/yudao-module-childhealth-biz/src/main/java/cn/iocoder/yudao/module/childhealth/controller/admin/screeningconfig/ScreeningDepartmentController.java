package cn.iocoder.yudao.module.childhealth.controller.admin.screeningconfig;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.screeningconfig.vo.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig.ScreeningDepartmentDO;
import cn.iocoder.yudao.module.childhealth.service.screeningconfig.ScreeningDepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 筛查科室")
@RestController
@RequestMapping("/childhealth/screening-department")
@Validated
public class ScreeningDepartmentController {

    @Resource
    private ScreeningDepartmentService screeningDepartmentService;

    @PostMapping("/create")
    @Operation(summary = "创建筛查科室")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-department:create')")
    public CommonResult<Long> createScreeningDepartment(@Valid @RequestBody ScreeningDepartmentSaveReqVO createReqVO) {
        return success(screeningDepartmentService.createScreeningDepartment(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新筛查科室")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-department:update')")
    public CommonResult<Boolean> updateScreeningDepartment(@Valid @RequestBody ScreeningDepartmentSaveReqVO updateReqVO) {
        screeningDepartmentService.updateScreeningDepartment(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除筛查科室")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:screening-department:delete')")
    public CommonResult<Boolean> deleteScreeningDepartment(@RequestParam("id") Long id) {
        screeningDepartmentService.deleteScreeningDepartment(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得筛查科室")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-department:query')")
    public CommonResult<ScreeningDepartmentRespVO> getScreeningDepartment(@RequestParam("id") Long id) {
        ScreeningDepartmentDO screeningDepartment = screeningDepartmentService.getScreeningDepartment(id);
        return success(BeanUtils.toBean(screeningDepartment, ScreeningDepartmentRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得筛查科室分页")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-department:query')")
    public CommonResult<PageResult<ScreeningDepartmentRespVO>> getScreeningDepartmentPage(@Valid ScreeningDepartmentPageReqVO pageReqVO) {
        PageResult<ScreeningDepartmentDO> pageResult = screeningDepartmentService.getScreeningDepartmentPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ScreeningDepartmentRespVO.class));
    }

}

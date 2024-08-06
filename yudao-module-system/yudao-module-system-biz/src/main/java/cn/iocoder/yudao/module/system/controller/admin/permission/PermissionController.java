package cn.iocoder.yudao.module.system.controller.admin.permission;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.datapermission.core.annotation.DataPermission;
import cn.iocoder.yudao.module.system.controller.admin.permission.vo.permission.PermissionAssignRoleDataScopeReqVO;
import cn.iocoder.yudao.module.system.controller.admin.permission.vo.permission.PermissionAssignRoleMenuReqVO;
import cn.iocoder.yudao.module.system.controller.admin.permission.vo.permission.PermissionAssignUserRoleReqVO;
import cn.iocoder.yudao.module.system.service.permission.PermissionService;
import cn.iocoder.yudao.module.system.service.tenant.TenantService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.Set;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

/**
 * 权限 Controller，提供赋予用户、角色的权限的 API 接口
 *
 * @author 芋道源码
 */
@Tag(name = "管理后台 - 权限")
@RestController
@RequestMapping("/system/permission")
public class PermissionController {

    @Resource
    private PermissionService permissionService;
    @Resource
    private TenantService tenantService;

    @Operation(summary = "获得角色拥有的菜单编号")
    @Parameter(name = "roleId", description = "角色编号", required = true)
    @GetMapping("/list-role-menus")
    @PreAuthorize("@ss.hasPermission('system:permission:assign-role-menu')")
    public CommonResult<Set<Long>> getRoleMenuList(Long roleId) {
        return success(permissionService.getRoleMenuListByRoleId(roleId));
    }

    @PostMapping("/assign-role-menu")
    @Operation(summary = "赋予角色菜单")
    @PreAuthorize("@ss.hasPermission('system:permission:assign-role-menu')")
    public CommonResult<Boolean> assignRoleMenu(@Validated @RequestBody PermissionAssignRoleMenuReqVO reqVO) {
        // 开启多租户的情况下，需要过滤掉未开通的菜单
        tenantService.handleTenantMenu(menuIds -> reqVO.getMenuIds().removeIf(menuId -> !CollUtil.contains(menuIds, menuId)));

        // 执行菜单的分配
        permissionService.assignRoleMenu(reqVO.getRoleId(), reqVO.getMenuIds());
        return success(true);
    }

    @PostMapping("/assign-role-data-scope")
    @Operation(summary = "赋予角色数据权限")
    @PreAuthorize("@ss.hasPermission('system:permission:assign-role-data-scope')")
    public CommonResult<Boolean> assignRoleDataScope(@Valid @RequestBody PermissionAssignRoleDataScopeReqVO reqVO) {
        permissionService.assignRoleDataScope(reqVO.getRoleId(), reqVO.getDataScope(), reqVO.getDataScopeDeptIds());
        return success(true);
    }

    @Operation(summary = "获得管理员拥有的角色编号列表")
    @Parameter(name = "userId", description = "用户编号", required = true)
    @GetMapping("/list-user-roles")
    @PreAuthorize("@ss.hasPermission('system:permission:assign-user-role')")
    public CommonResult<Set<Long>> listAdminRoles(@RequestParam("userId") Long userId) {
        return success(permissionService.getUserRoleIdListByUserId(userId));
    }

    @Operation(summary = "赋予用户角色")
    @PostMapping("/assign-user-role")
    @PreAuthorize("@ss.hasPermission('system:permission:assign-user-role')")
    public CommonResult<Boolean> assignUserRole(@Validated @RequestBody PermissionAssignUserRoleReqVO reqVO) {
        permissionService.assignUserRole(reqVO.getUserId(), reqVO.getRoleIds());
        return success(true);
    }


    @GetMapping("/get-capital-roleid")
    @Operation(summary = "获得队长的角色id")
    @DataPermission(enable = false) // 对这个接口放开数据权限范围
    public CommonResult<Long> getCapitalRoleId(){
        Long capitalRoleId = permissionService.getCapitalRoleId();
        return success(capitalRoleId);
    }


    @GetMapping("/get-collect-roleid")
    @Operation(summary = "获得采集组的角色id")
    @DataPermission(enable = false) // 对这个接口放开数据权限范围
    public CommonResult<Long> getCollectRoleId(){
        Long collectRoleId = permissionService.getCollectRoleId();
        return success(collectRoleId);
    }


    @GetMapping("/get-ppd-roleid")
    @Operation(summary = "获得PPD组的角色id")
    @DataPermission(enable = false) // 对这个接口放开数据权限范围
    public CommonResult<Long> getPPDRoleId(){
        Long PPDRoleId = permissionService.getPPDRoleId();
        return success(PPDRoleId);
    }

    @GetMapping("/get-drct-roleid")
    @Operation(summary = "获得DRCT组的角色id")
    @DataPermission(enable = false) // 对这个接口放开数据权限范围
    public CommonResult<Long> getDrctRoleId(){
        Long drctRoleId = permissionService.getDrctRoleId();
        return success(drctRoleId);
    }

    @GetMapping("/get-sputum-roleid")
    @Operation(summary = "获得痰检组的角色id")
    @DataPermission(enable = false) // 对这个接口放开数据权限范围
    public CommonResult<Long> getSputumRoleId(){
        Long sputumRoleId = permissionService.getSputumRoleId();
        return success(sputumRoleId);
    }

    @GetMapping("/get-experiment-roleid")
    @Operation(summary = "获得实验组的角色id")
    @DataPermission(enable = false) // 对这个接口放开数据权限范围
    public CommonResult<Long> getExperimentRoleId(){
        Long experimentRoleId = permissionService.getExperimentRoleId();
        return success(experimentRoleId);
    }

    @GetMapping("/get-electrocardiogram-roleid")
    @Operation(summary = "获得心电图组的角色id")
    @DataPermission(enable = false) // 对这个接口放开数据权限范围
    public CommonResult<Long> getElectrocardiogramRoleId(){
        Long electrocardiogramRoleId = permissionService.getElectrocardiogramRoleId();
        return success(electrocardiogramRoleId);
    }

    @GetMapping("/get-diagnosis-roleid")
    @Operation(summary = "获得诊断组组的角色id")
    @DataPermission(enable = false) // 对这个接口放开数据权限范围
    public CommonResult<Long> getDiagnosisRoleId(){
        Long diagnosisRoleId = permissionService.getDiagnosisRoleId();
        return success(diagnosisRoleId);
    }

}

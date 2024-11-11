package cn.iocoder.yudao.module.ppd.controller.admin.screenpoint;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.datapermission.core.annotation.DataPermission;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.ppd.controller.admin.screenpersonrealsituation.vo.ScreenPersonImportRespVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenpoint.vo.*;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screendistrict.ScreenDistrictDO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenpoint.ScreenPointDO;
import cn.iocoder.yudao.module.ppd.service.screendistrict.ScreenDistrictService;
import cn.iocoder.yudao.module.ppd.service.screenpoint.ScreenPointService;
import cn.iocoder.yudao.module.ppd.utils.CustomSheetWriteHandler;
import cn.iocoder.yudao.module.system.controller.admin.dept.vo.dept.DeptSaveReqVO;
import cn.iocoder.yudao.module.system.controller.admin.permission.vo.permission.PermissionAssignUserRoleReqVO;
import cn.iocoder.yudao.module.system.controller.admin.user.vo.user.UserPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.user.vo.user.UserRespVO;
import cn.iocoder.yudao.module.system.convert.user.UserConvert;
import cn.iocoder.yudao.module.system.dal.dataobject.dept.DeptDO;
import cn.iocoder.yudao.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.yudao.module.system.dal.mysql.dept.DeptMapper;
import cn.iocoder.yudao.module.system.service.dept.DeptService;
import cn.iocoder.yudao.module.system.service.permission.PermissionService;
import cn.iocoder.yudao.module.system.service.user.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;


/**
 * @author lenovo
 */
@Tag(name = "管理后台 - 筛查点")
@RestController
@RequestMapping("/tb/screen-point")
@Validated
public class ScreenPointController {

    @Resource
    private ScreenPointService screenPointService;
    @Resource
    private DeptMapper deptMapper;
    @Resource
    private AdminUserService userService;
    @Resource
    private DeptService deptService;
    @Resource
    private PermissionService permissionService;
    @Resource
    private ScreenDistrictService districtService;

    @PostMapping("/create")
    @Operation(summary = "创建筛查点")
    @PreAuthorize("@ss.hasPermission('tb:screen-point:create')")
    public CommonResult<Long> createScreenPoint(@Valid @RequestBody ScreenPointSaveReqVO createReqVO) {
        return success(screenPointService.createScreenPoint(createReqVO));
    }

    @DeleteMapping("/deleteUser")
    @Operation(summary = "删除用户")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:user:delete')")
    public CommonResult<Boolean> deleteUser(@RequestParam("id") Long id) {
        screenPointService.deleteUser(id);
        return success(true);
    }

    @PutMapping("/update")
    @Operation(summary = "更新筛查点")
    @PreAuthorize("@ss.hasPermission('tb:screen-point:update')")
    public CommonResult<Boolean> updateScreenPoint(@Valid @RequestBody ScreenPointSaveReqVO updateReqVO) {
        screenPointService.updateScreenPoint(updateReqVO);
        return success(true);
    }


    @DeleteMapping("/delete")
    @Operation(summary = "删除筛查点")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('tb:screen-point:delete')")
    public CommonResult<Boolean> deleteScreenPoint(@RequestParam("id") Long id) {
        screenPointService.deleteScreenPoint(id);
        return success(true);
    }


    @GetMapping("/get")
    @Operation(summary = "获得筛查点")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('tb:screen-point:query')")
    public CommonResult<ScreenPointRespVO> getScreenPoint(@RequestParam("id") Long id) {
        ScreenPointDO screenPoint = screenPointService.getScreenPoint(id);
        return success(BeanUtils.toBean(screenPoint, ScreenPointRespVO.class));
    }


    @GetMapping("/page")
    @Operation(summary = "获得筛查点分页")
    @PreAuthorize("@ss.hasPermission('tb:screen-point:query')")
    public CommonResult<PageResult<ScreenPointRespVO>> getScreenPointPage(@Valid ScreenPointPageReqVO pageReqVO) {
        PageResult<ScreenPointDO> pageResult = screenPointService.getScreenPointPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ScreenPointRespVO.class));
    }


    @GetMapping("/export-excel")
    @Operation(summary = "导出筛查点 Excel")
    @PreAuthorize("@ss.hasPermission('tb:screen-point:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportScreenPointExcel(@Valid ScreenPointPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ScreenPointDO> list = screenPointService.getScreenPointPage2(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write2(response, "筛查点.xls", "数据", ScreenPointRespVO.class,
                        BeanUtils.toBean(list, ScreenPointRespVO.class));
    }


    @GetMapping("/get-list")
    @Operation(summary = "获得筛查点列表")
    public CommonResult<List<String>> getScreenPointList() {
        List<String> list = screenPointService.getScreenPointList();
        return success(list);
    }


    @PostMapping("/update-point")
    @Operation(summary = "分配筛查点")
    public CommonResult<Boolean> screenUpdatePoint( @RequestBody ScreenUpdateVO screenUpdateVO) {
        return success(screenPointService.screenUpdatePoint(screenUpdateVO));
    }


    @PostMapping("/import-template")
    @Operation(summary = "导入筛查点")
    @Parameters({
            @Parameter(name = "file", description = "Excel 文件", required = true),
    })
    @PreAuthorize("@ss.hasPermission('tb:screen-point:create')")
    public CommonResult<ScreenPersonImportRespVO> importExcel(@RequestParam("file") MultipartFile file) throws Exception {
        List<ScreenPointImportVO> list = ExcelUtils.read(file, ScreenPointImportVO.class);
        ScreenPersonImportRespVO respVO = screenPointService.importScreenPoint(list);
        return success(respVO);
    }


    @GetMapping("/get-import-template")
    @Operation(summary = "下载筛查点模板")
    @ApiAccessLog(operateType = EXPORT)
    public void exportScreenPersonExcel(HttpServletResponse response) throws IOException {
        // 模板数据
        List<ScreenPointImportVO> list = screenPointService.createSampleData();
        // 存放下拉选列表数据
        Map<Integer, List<String>> selectedData = new HashMap<>();

        List<ScreenDistrictDO> districtList = districtService.getDistrictList2();
        List<String> codeList = districtList.stream().map(ScreenDistrictDO::getCode).toList();
        List<String> deptList = districtService.getDeptList(codeList);

        if (ObjectUtil.isNotNull(deptList) && ObjectUtil.isEmpty(deptList)){
            selectedData.put(1, deptList);
        }
        // 导出 Excel 模板
        ExcelUtils.write(response, "筛查点导入模板.xls", "筛查点数据", ScreenPointImportVO.class,
                BeanUtils.toBean(list, ScreenPointImportVO.class), new CustomSheetWriteHandler().setMap(selectedData));
    }


    @GetMapping("/get-userList")
    @Operation(summary = "获得用户列表")
    @DataPermission(enable = false) // 对这个接口放开数据权限范围
    public CommonResult<List<UserVO>> getUserList() {
        List<UserVO> list = screenPointService.getAllUserList();
        return success(list);
    }


    @GetMapping("/get-deptList")
    @Operation(summary = "获取部门列表")
//    @DataPermission(enable = false) // 对这个接口放开数据权限范围
    public CommonResult<List<DeptVO>> getDeptList(){
        List<DeptVO> deptList = screenPointService.getDeptList();
        return success(deptList);
    }


    @GetMapping("/set-worker")
    @Operation(summary = "在筛查点表中写入队长")
    public CommonResult<Boolean> setWorker(@RequestParam("userId") Long userId,
                                           @RequestParam("screenPointId") Long screenPointId){
        screenPointService.setWorker(userId, screenPointId);
        return success(true);
    }


    /*@GetMapping("/get-isDistribute")
    @Operation(summary = "查看当前用户分配的筛查点")
    @DataPermission(enable = false) // 对这个接口放开数据权限范围
    public CommonResult<Long> getIsDistribute(@RequestParam("userId") Long userId){
        return success(screenPointService.getIsDistribute(userId));
    }*/


    @GetMapping("/get-distributeyear")
    @Operation(summary = "在当前年度是否被分配组长")
    public CommonResult<Boolean> getDistributeYear(@RequestParam("userId") Long userId,
                                                @RequestParam("year") Integer year){
        Boolean isDistributeYear = screenPointService.getDistributeYear(userId, year);
        return success(isDistributeYear);
    }


    // 各组可共用
    @GetMapping("/resolve-collect-list")
    @DataPermission(enable = false) // 对这个放开数据权限范围
    @Operation(summary = "各组工作人员回显在form表单中")
    public CommonResult<List<UserRespVO>> getresolveCollectList(@RequestParam("collectList") List<Long> collectList){
        List<UserRespVO> list = screenPointService.getCollectList(collectList);
        return success(list);
    }


    @GetMapping("/delete-collect")
    @Operation(summary = "删除各组的工作人员")
    @DataPermission(enable = false) // 对这个接口放开数据权限范围
    public CommonResult<Boolean> deleteScreenPointCollect(@RequestParam("collectWorker") String collectWorker,
                                                          @RequestParam("screenPointId") Long screenPointId,
                                                          @RequestParam("deletedData") Long deletedData,
                                                          @RequestParam("type") Integer type){
        screenPointService.deleteScreenPointCollect(collectWorker, screenPointId, deletedData, type);
        return success(true);
    }


    @GetMapping("/set-user-screenpoint")
    @Operation(summary = "用户-筛查点表插入数据")
    @DataPermission(enable = false) // 对这个接口放开数据权限范围
    public CommonResult<Boolean> setUserScreenPoint(@RequestParam("userId") Long userId,
                                                    @RequestParam("screenPointId") Long screenPointId,
                                                    @RequestParam("roleId") Long roleId){
        screenPointService.updateScreenPoint(screenPointId, userId, roleId);
        return success(true);
    }


    /**
     * 各组通用,分配各组角色
     */
    @GetMapping("/set-screenpoint-collect")
    @Operation(summary = "分配各组角色")
    @DataPermission(enable = false) // 对这个接口放开数据权限范围
    public CommonResult<Boolean> setCollect(@RequestParam("collectUserIdStr") String collectUserIdStr,
                                            @RequestParam("screenPointId") Long screenPointId,
                                            @RequestParam("type") Integer type){
        screenPointService.setCollect(collectUserIdStr, screenPointId, type);
        return success(true);
    }


    @GetMapping("/set-cap")
    @Operation(summary = "分配队长")
    @DataPermission(enable = false) // 对这个接口放开数据权限范围
    public CommonResult<Boolean> setCap(@RequestParam("newCapId") Long newCapId,
                                        @RequestParam("oldCapId") Long oldCapId,
                                        @RequestParam("screenPointId") Long screenPointId){
        screenPointService.setCap(newCapId, oldCapId, screenPointId);
        return success(true);
    }


    @GetMapping("/user-page")
    @Operation(summary = "获得用户分页列表")
    @PreAuthorize("@ss.hasPermission('tb:screen-point:update')")
    public CommonResult<PageResult<UserRespVO>> getUserPage(@Valid UserPageReqVO pageReqVO) {
        // 获得用户分页列表
        PageResult<AdminUserDO> pageResult = userService.getUserPage(pageReqVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return success(new PageResult<>(pageResult.getTotal()));
        }
        // 拼接数据
        Map<Long, DeptDO> deptMap = deptService.getDeptMap(
                convertList(pageResult.getList(), AdminUserDO::getDeptId));
        return success(new PageResult<>(UserConvert.INSTANCE.convertList(pageResult.getList(), deptMap),
                pageResult.getTotal()));
    }


    @Operation(summary = "赋予用户角色")
    @PostMapping("/assign-user-role")
    @PreAuthorize("@ss.hasPermission('tb:screen-point:update')")
    public CommonResult<Boolean> assignUserRole(@Validated @RequestBody PermissionAssignUserRoleReqVO reqVO) {
        permissionService.assignUserRole(reqVO.getUserId(), reqVO.getRoleIds());
        return success(true);
    }


    @Operation(summary = "获得管理员拥有的角色编号列表")
    @Parameter(name = "userId", description = "用户编号", required = true)
    @GetMapping("/list-user-roles")
    @PreAuthorize("@ss.hasPermission('tb:screen-point:update')")
    public CommonResult<Set<Long>> listAdminRoles(@RequestParam("userId") Long userId) {
        return success(permissionService.getUserRoleIdListByUserId(userId));
    }


    @PutMapping("/dept-update")
    @Operation(summary = "更新部门")
    @PreAuthorize("@ss.hasPermission('system:dept:update')")
    public CommonResult<Boolean> updateDept(@Valid @RequestBody DeptSaveReqVO updateReqVO) {
        screenPointService.updateDept(updateReqVO);
        return success(true);
    }


}
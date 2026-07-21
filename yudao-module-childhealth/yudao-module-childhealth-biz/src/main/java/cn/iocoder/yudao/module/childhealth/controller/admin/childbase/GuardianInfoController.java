package cn.iocoder.yudao.module.childhealth.controller.admin.childbase;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.childhealth.controller.admin.childbase.vo.GuardianInfoCreateReqVO;
import cn.iocoder.yudao.module.childhealth.controller.admin.childbase.vo.GuardianInfoRespVO;
import cn.iocoder.yudao.module.childhealth.service.childbase.GuardianInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

/**
 * 管理后台 - 监护人信息 Controller
 *
 * @author 系统
 */
@Tag(name = "管理后台 - 监护人信息")
@RestController
@RequestMapping("/childhealth/guardian-info")
@Validated
public class GuardianInfoController {

    @Resource
    private GuardianInfoService guardianInfoService;

    @PostMapping("/create")
    @Operation(summary = "创建监护人信息")
    @PreAuthorize("@ss.hasPermission('childhealth:guardian-info:create')")
    public CommonResult<Long> createGuardianInfo(@Valid @RequestBody GuardianInfoCreateReqVO createReqVO) {
        Long guardianId = guardianInfoService.createGuardianInfo(createReqVO);
        return success(guardianId);
    }

    @PutMapping("/update")
    @Operation(summary = "更新监护人信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('childhealth:guardian-info:update')")
    public CommonResult<Boolean> updateGuardianInfo(@RequestParam("id") Long id,
                                                      @Valid @RequestBody GuardianInfoCreateReqVO updateReqVO) {
        guardianInfoService.updateGuardianInfo(id, updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除监护人信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('childhealth:guardian-info:delete')")
    public CommonResult<Boolean> deleteGuardianInfo(@RequestParam("id") Long id) {
        guardianInfoService.deleteGuardianInfo(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得监护人信息详情")
    @Parameter(name = "id", description = "编号", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('childhealth:guardian-info:query')")
    public CommonResult<GuardianInfoRespVO> getGuardianInfo(@RequestParam("id") Long id) {
        GuardianInfoRespVO guardianInfo = guardianInfoService.getGuardianInfo(id);
        return success(guardianInfo);
    }

    @GetMapping("/list-by-child")
    @Operation(summary = "根据儿童ID获取监护人信息列表")
    @Parameter(name = "childId", description = "儿童ID", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('childhealth:guardian-info:query')")
    public CommonResult<List<GuardianInfoRespVO>> getGuardianInfoListByChildId(@RequestParam("childId") Long childId) {
        List<GuardianInfoRespVO> guardianList = guardianInfoService.getGuardianInfoListByChildId(childId);
        return success(guardianList);
    }

    @GetMapping("/get-primary")
    @Operation(summary = "根据儿童ID获取主要监护人信息")
    @Parameter(name = "childId", description = "儿童ID", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('childhealth:guardian-info:query')")
    public CommonResult<GuardianInfoRespVO> getPrimaryGuardianByChildId(@RequestParam("childId") Long childId) {
        GuardianInfoRespVO guardianInfo = guardianInfoService.getPrimaryGuardianByChildId(childId);
        return success(guardianInfo);
    }

}
package cn.iocoder.yudao.module.childhealth.controller.admin.casemanagement;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.casemanagement.vo.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.casemanagement.CaseManagementDO;
import cn.iocoder.yudao.module.childhealth.service.casemanagement.CaseManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 专案主表")
@RestController
@RequestMapping("/childhealth/case-management")
@Validated
public class CaseManagementController {

    @Resource
    private CaseManagementService caseManagementService;

    @PostMapping("/create")
    @Operation(summary = "创建专案")
    @PreAuthorize("@ss.hasPermission('childhealth:case-management:create')")
    public CommonResult<Long> createCaseManagement(@Valid @RequestBody CaseManagementSaveReqVO createReqVO) {
        return success(caseManagementService.createCaseManagement(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新专案")
    @PreAuthorize("@ss.hasPermission('childhealth:case-management:update')")
    public CommonResult<Boolean> updateCaseManagement(@Valid @RequestBody CaseManagementSaveReqVO updateReqVO) {
        caseManagementService.updateCaseManagement(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除专案")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:case-management:delete')")
    public CommonResult<Boolean> deleteCaseManagement(@RequestParam("id") Long id) {
        caseManagementService.deleteCaseManagement(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得专案")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:case-management:query')")
    public CommonResult<CaseManagementRespVO> getCaseManagement(@RequestParam("id") Long id) {
        CaseManagementDO caseManagement = caseManagementService.getCaseManagement(id);
        return success(BeanUtils.toBean(caseManagement, CaseManagementRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得专案分页")
    @PreAuthorize("@ss.hasPermission('childhealth:case-management:query')")
    public CommonResult<PageResult<CaseManagementRespVO>> getCaseManagementPage(@Valid CaseManagementPageReqVO pageReqVO) {
        PageResult<CaseManagementDO> pageResult = caseManagementService.getCaseManagementPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, CaseManagementRespVO.class));
    }

    @PostMapping("/auto-create-case")
    @Operation(summary = "异常自动建专案（根据异常检测/筛查/体检异常记录，自动建立对应类型的专案）")
    @Parameter(name = "abnormalRecord", description = "异常记录（占位，后续替换为具体异常记录类型）", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:case-management:create')")
    public CommonResult<Long> autoCreateCaseFromAbnormal(@RequestBody Object abnormalRecord) {
        return success(caseManagementService.autoCreateCaseFromAbnormal(abnormalRecord));
    }

    @PostMapping("/generate-card")
    @Operation(summary = "生成个案卡（根据专案ID生成个案卡文件并回写URL）")
    @Parameter(name = "caseId", description = "专案ID", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:case-management:update')")
    public CommonResult<String> generateCaseCard(@RequestParam("caseId") Long caseId) {
        return success(caseManagementService.generateCaseCard(caseId));
    }

    @PutMapping("/close")
    @Operation(summary = "结案归档")
    @PreAuthorize("@ss.hasPermission('childhealth:case-management:update')")
    public CommonResult<Boolean> closeCase(@RequestParam("caseId") Long caseId,
                                            @RequestParam("closeType") Integer closeType,
                                            @RequestParam(value = "closeReason", required = false) String closeReason) {
        caseManagementService.closeCase(caseId, closeType, closeReason);
        return success(true);
    }

}

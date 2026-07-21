package cn.iocoder.yudao.module.childhealth.controller.admin.casemanagement;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.casemanagement.vo.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.casemanagement.CaseRecoveryStandardDO;
import cn.iocoder.yudao.module.childhealth.service.casemanagement.CaseRecoveryStandardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 专案康复达标标准")
@RestController
@RequestMapping("/childhealth/case-recovery-standard")
@Validated
public class CaseRecoveryStandardController {

    @Resource
    private CaseRecoveryStandardService caseRecoveryStandardService;

    @PostMapping("/create")
    @Operation(summary = "创建专案康复达标标准")
    @PreAuthorize("@ss.hasPermission('childhealth:case-recovery-standard:create')")
    public CommonResult<Long> createCaseRecoveryStandard(@Valid @RequestBody CaseRecoveryStandardSaveReqVO createReqVO) {
        return success(caseRecoveryStandardService.createCaseRecoveryStandard(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新专案康复达标标准")
    @PreAuthorize("@ss.hasPermission('childhealth:case-recovery-standard:update')")
    public CommonResult<Boolean> updateCaseRecoveryStandard(@Valid @RequestBody CaseRecoveryStandardSaveReqVO updateReqVO) {
        caseRecoveryStandardService.updateCaseRecoveryStandard(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除专案康复达标标准")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:case-recovery-standard:delete')")
    public CommonResult<Boolean> deleteCaseRecoveryStandard(@RequestParam("id") Long id) {
        caseRecoveryStandardService.deleteCaseRecoveryStandard(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得专案康复达标标准")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:case-recovery-standard:query')")
    public CommonResult<CaseRecoveryStandardRespVO> getCaseRecoveryStandard(@RequestParam("id") Long id) {
        CaseRecoveryStandardDO caseRecoveryStandard = caseRecoveryStandardService.getCaseRecoveryStandard(id);
        return success(BeanUtils.toBean(caseRecoveryStandard, CaseRecoveryStandardRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得专案康复达标标准分页")
    @PreAuthorize("@ss.hasPermission('childhealth:case-recovery-standard:query')")
    public CommonResult<PageResult<CaseRecoveryStandardRespVO>> getCaseRecoveryStandardPage(@Valid CaseRecoveryStandardPageReqVO pageReqVO) {
        PageResult<CaseRecoveryStandardDO> pageResult = caseRecoveryStandardService.getCaseRecoveryStandardPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, CaseRecoveryStandardRespVO.class));
    }

    @PostMapping("/check-recovery")
    @Operation(summary = "康复达标判断（根据专案ID及最新随访指标数据，判断是否全部达标）")
    @Parameter(name = "caseId", description = "专案ID", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:case-recovery-standard:query')")
    public CommonResult<Boolean> checkRecovery(@RequestParam("caseId") Long caseId,
                                                @RequestBody Object indicatorValues) {
        return success(caseRecoveryStandardService.checkRecovery(caseId, indicatorValues));
    }

}

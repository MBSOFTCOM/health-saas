package cn.iocoder.yudao.module.childhealth.controller.admin.childbase;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.controller.admin.childbase.vo.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.childbase.*;
import cn.iocoder.yudao.module.childhealth.service.childbase.ChildBaseInfoService;
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
 * 管理后台 - 儿童基本信息 Controller
 *
 * @author 系统
 */
@Tag(name = "管理后台 - 儿童基本信息")
@RestController
@RequestMapping("/childhealth/child-base-info")
@Validated
public class ChildBaseInfoController {

    @Resource
    private ChildBaseInfoService childBaseInfoService;

    @PostMapping("/create")
    @Operation(summary = "创建儿童档案")
    @PreAuthorize("@ss.hasPermission('childhealth:child-base-info:create')")
    public CommonResult<Long> createChildBaseInfo(@Valid @RequestBody ChildBaseInfoCreateReqVO createReqVO) {
        Long childId = childBaseInfoService.createChildBaseInfo(createReqVO);
        return success(childId);
    }

    @PutMapping("/update")
    @Operation(summary = "更新儿童档案")
    @PreAuthorize("@ss.hasPermission('childhealth:child-base-info:update')")
    public CommonResult<Boolean> updateChildBaseInfo(@Valid @RequestBody ChildBaseInfoUpdateReqVO updateReqVO) {
        childBaseInfoService.updateChildBaseInfo(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除儿童档案")
    @Parameter(name = "id", description = "编号", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('childhealth:child-base-info:delete')")
    public CommonResult<Boolean> deleteChildBaseInfo(@RequestParam("id") Long id) {
        childBaseInfoService.deleteChildBaseInfo(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得儿童档案详情")
    @Parameter(name = "id", description = "编号", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('childhealth:child-base-info:query')")
    public CommonResult<ChildBaseInfoRespVO> getChildBaseInfo(@RequestParam("id") Long id) {
        ChildBaseInfoRespVO childBaseInfo = childBaseInfoService.getChildBaseInfo(id);
        return success(childBaseInfo);
    }

    @GetMapping("/page")
    @Operation(summary = "获得儿童档案分页列表")
    @PreAuthorize("@ss.hasPermission('childhealth:child-base-info:query')")
    public CommonResult<PageResult<ChildBaseInfoRespVO>> getChildBaseInfoPage(@Valid ChildBaseInfoPageReqVO pageReqVO) {
        PageResult<ChildBaseInfoRespVO> pageResult = childBaseInfoService.getChildBaseInfoPage(pageReqVO);
        return success(pageResult);
    }

    @PostMapping("/generate-qrcode")
    @Operation(summary = "生成儿童二维码")
    @Parameter(name = "id", description = "儿童ID", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('childhealth:child-base-info:update')")
    public CommonResult<String> generateQrCode(@RequestParam("id") Long id) {
        String qrCode = childBaseInfoService.generateQrCode(id);
        return success(qrCode);
    }

    @GetMapping("/get-qrcode")
    @Operation(summary = "根据儿童编码获取二维码")
    @Parameter(name = "childCode", description = "儿童编码", required = true, example = "CHILD-20240101-001")
    @PreAuthorize("@ss.hasPermission('childhealth:child-base-info:query')")
    public CommonResult<String> getQrCodeByChildCode(@RequestParam("childCode") String childCode) {
        String qrCode = childBaseInfoService.getQrCodeByChildCode(childCode);
        return success(qrCode);
    }

    // ==================== 高危识别与评估 ====================

    @PostMapping("/auto-detect-high-risk")
    @Operation(summary = "根据分娩信息自动识别高危因素")
    @Parameter(name = "childId", description = "儿童ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:child-base-info:update')")
    public CommonResult<List<String>> autoDetectHighRisk(@RequestParam("childId") Long childId) {
        return success(childBaseInfoService.autoDetectHighRisk(childId));
    }

    @PostMapping("/manual-assess-high-risk")
    @Operation(summary = "手工评估儿童高危情况")
    @PreAuthorize("@ss.hasPermission('childhealth:child-base-info:update')")
    public CommonResult<Integer> manualAssessHighRisk(@RequestParam("childId") Long childId,
                                                      @RequestParam("factorCodes") List<String> factorCodes,
                                                      @RequestParam(value = "assessmentDoctor", required = false) Long assessmentDoctor) {
        return success(childBaseInfoService.manualAssessHighRisk(childId, factorCodes, assessmentDoctor));
    }

    @GetMapping("/high-risk-factor-configs")
    @Operation(summary = "获取所有启用的高危因素配置")
    @PreAuthorize("@ss.hasPermission('childhealth:child-base-info:query')")
    public CommonResult<List<HighRiskFactorConfigDO>> getHighRiskFactorConfigs() {
        return success(childBaseInfoService.getHighRiskFactorConfigs());
    }

    // ==================== 档案子信息查询 ====================

    @GetMapping("/delivery-info")
    @Operation(summary = "获取儿童分娩信息")
    @Parameter(name = "childId", description = "儿童ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:child-base-info:query')")
    public CommonResult<DeliveryInfoDO> getDeliveryInfo(@RequestParam("childId") Long childId) {
        return success(childBaseInfoService.getDeliveryInfo(childId));
    }

    @GetMapping("/family-info")
    @Operation(summary = "获取儿童家庭信息")
    @Parameter(name = "childId", description = "儿童ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:child-base-info:query')")
    public CommonResult<FamilyInfoDO> getFamilyInfo(@RequestParam("childId") Long childId) {
        return success(childBaseInfoService.getFamilyInfo(childId));
    }

    @GetMapping("/immunization-list")
    @Operation(summary = "获取儿童免疫信息列表")
    @Parameter(name = "childId", description = "儿童ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:child-base-info:query')")
    public CommonResult<List<ImmunizationInfoDO>> getImmunizationList(@RequestParam("childId") Long childId) {
        return success(childBaseInfoService.getImmunizationList(childId));
    }

    @GetMapping("/neonatal-diagnosis-list")
    @Operation(summary = "获取新生儿住院诊断列表（含预警）")
    @Parameter(name = "childId", description = "儿童ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:child-base-info:query')")
    public CommonResult<List<NeonatalDiagnosisDO>> getNeonatalDiagnosisList(@RequestParam("childId") Long childId) {
        return success(childBaseInfoService.getNeonatalDiagnosisList(childId));
    }

}
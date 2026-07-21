package cn.iocoder.yudao.module.childhealth.controller.admin.caseType;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.caseType.vo.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.caseType.CaseTypeConfigDO;
import cn.iocoder.yudao.module.childhealth.service.caseType.CaseTypeConfigService;
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

@Tag(name = "管理后台 - 专案类型配置")
@RestController
@RequestMapping("/childhealth/caseTypeConfig")
@Validated
public class CaseTypeConfigController {

    @Resource
    private CaseTypeConfigService caseTypeConfigService;

    @PostMapping("/create")
    @Operation(summary = "创建专案类型配置")
    @PreAuthorize("@ss.hasPermission('childhealth:case-type-config:create')")
    public CommonResult<Long> createCaseTypeConfig(@Valid @RequestBody CaseTypeConfigSaveReqVO createReqVO) {
        return success(caseTypeConfigService.createCaseTypeConfig(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新专案类型配置")
    @PreAuthorize("@ss.hasPermission('childhealth:case-type-config:update')")
    public CommonResult<Boolean> updateCaseTypeConfig(@Valid @RequestBody CaseTypeConfigSaveReqVO updateReqVO) {
        caseTypeConfigService.updateCaseTypeConfig(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除专案类型配置")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:case-type-config:delete')")
    public CommonResult<Boolean> deleteCaseTypeConfig(@RequestParam("id") Long id) {
        caseTypeConfigService.deleteCaseTypeConfig(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得专案类型配置")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:case-type-config:query')")
    public CommonResult<CaseTypeConfigRespVO> getCaseTypeConfig(@RequestParam("id") Long id) {
        CaseTypeConfigDO caseTypeConfig = caseTypeConfigService.getCaseTypeConfig(id);
        return success(BeanUtils.toBean(caseTypeConfig, CaseTypeConfigRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得专案类型配置分页")
    @PreAuthorize("@ss.hasPermission('childhealth:case-type-config:query')")
    public CommonResult<PageResult<CaseTypeConfigRespVO>> getCaseTypeConfigPage(@Valid CaseTypeConfigPageReqVO pageReqVO) {
        PageResult<CaseTypeConfigDO> pageResult = caseTypeConfigService.getCaseTypeConfigPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, CaseTypeConfigRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得专案类型配置列表")
    @PreAuthorize("@ss.hasPermission('childhealth:case-type-config:query')")
    public CommonResult<List<CaseTypeConfigRespVO>> getCaseTypeConfigList() {
        List<CaseTypeConfigDO> list = caseTypeConfigService.getCaseTypeConfigList();
        return success(BeanUtils.toBean(list, CaseTypeConfigRespVO.class));
    }

}
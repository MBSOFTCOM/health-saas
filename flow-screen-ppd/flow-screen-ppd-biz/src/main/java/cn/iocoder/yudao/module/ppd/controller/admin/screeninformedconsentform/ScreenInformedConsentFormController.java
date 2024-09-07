package cn.iocoder.yudao.module.ppd.controller.admin.screeninformedconsentform;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.ppd.controller.admin.screeninformedconsentform.vo.ScreenInformedConsentFormPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screeninformedconsentform.vo.ScreenInformedConsentFormRespVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screeninformedconsentform.vo.ScreenInformedConsentFormSaveReqVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screeninformedconsentform.ScreenInformedConsentFormDO;
import cn.iocoder.yudao.module.ppd.service.screeninformedconsentform.ScreenInformedConsentFormService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.successMsg;

@Tag(name = "管理后台 - 知情同意书")
@RestController
@RequestMapping("/tb/screen-informed-consent-form")
@Validated
public class ScreenInformedConsentFormController {

    @Resource
    private ScreenInformedConsentFormService screenInformedConsentFormService;

    @PostMapping("/create")
    @Operation(summary = "创建知情同意书")
    @PreAuthorize("@ss.hasPermission('ppd:screen-informed-consent-form:create')")
    public CommonResult<Long> createScreenInformedConsentForm(@Valid @RequestBody ScreenInformedConsentFormSaveReqVO createReqVO) {
        return successMsg(screenInformedConsentFormService.createScreenInformedConsentForm(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新知情同意书")
    @PreAuthorize("@ss.hasPermission('ppd:screen-informed-consent-form:update')")
    public CommonResult<Boolean> updateScreenInformedConsentForm(@Valid @RequestBody ScreenInformedConsentFormSaveReqVO updateReqVO) {
        screenInformedConsentFormService.updateScreenInformedConsentForm(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除知情同意书")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('ppd:screen-informed-consent-form:delete')")
    public CommonResult<Boolean> deleteScreenInformedConsentForm(@RequestParam("id") Long id) {
        screenInformedConsentFormService.deleteScreenInformedConsentForm(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得知情同意书")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('ppd:screen-informed-consent-form:query')")
    public CommonResult<ScreenInformedConsentFormRespVO> getScreenInformedConsentForm(@RequestParam("id") Long id) {
        ScreenInformedConsentFormDO screenInformedConsentForm = screenInformedConsentFormService.getScreenInformedConsentForm(id);
        return success(BeanUtils.toBean(screenInformedConsentForm, ScreenInformedConsentFormRespVO.class));
    }
    @GetMapping("/get/last")
    @Operation(summary = "获得知情同意书")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('ppd:screen-informed-consent-form:query')")
    public CommonResult<ScreenInformedConsentFormRespVO> getLastInformedConsentForm(@RequestParam("studentId") Long id) {
        ScreenInformedConsentFormRespVO screenInformedConsentForm = screenInformedConsentFormService.getLastInformedConsentForm(id);
        return successMsg(screenInformedConsentForm);
    }

    @GetMapping("/page")
    @Operation(summary = "获得知情同意书分页")
    @PreAuthorize("@ss.hasPermission('ppd:screen-informed-consent-form:query')")
    public CommonResult<PageResult<ScreenInformedConsentFormRespVO>> getScreenInformedConsentFormPage(@Valid ScreenInformedConsentFormPageReqVO pageReqVO) {
        PageResult<ScreenInformedConsentFormDO> pageResult = screenInformedConsentFormService.getScreenInformedConsentFormPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ScreenInformedConsentFormRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出知情同意书 Excel")
    @PreAuthorize("@ss.hasPermission('ppd:screen-informed-consent-form:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportScreenInformedConsentFormExcel(@Valid ScreenInformedConsentFormPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ScreenInformedConsentFormDO> list = screenInformedConsentFormService.getScreenInformedConsentFormPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "知情同意书.xls", "数据", ScreenInformedConsentFormRespVO.class,
                        BeanUtils.toBean(list, ScreenInformedConsentFormRespVO.class));
    }

}
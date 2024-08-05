package cn.iocoder.yudao.module.ppd.controller.admin.screensputumexamination;


import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.ppd.controller.admin.screensputumexamination.vo.ScreenSputumExaminationPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screensputumexamination.vo.ScreenSputumExaminationRespVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screensputumexamination.vo.ScreenSputumExaminationSaveReqVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screensputumexamination.ScreenSputumExaminationDO;
import cn.iocoder.yudao.module.ppd.service.screensputumexamination.ScreenSputumExaminationService;
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

@Tag(name = "管理后台 - 痰检组")
@RestController
@RequestMapping("/tb/screen-sputum-examination")
@Validated
public class ScreenSputumExaminationController {

    @Resource
    private ScreenSputumExaminationService screenSputumExaminationService;

    @PostMapping("/create")
    @Operation(summary = "创建痰检组")
    @PreAuthorize("@ss.hasPermission('tb:screen-sputum-examination:create')")
    public CommonResult<Long> createScreenSputumExamination(@Valid @RequestBody ScreenSputumExaminationSaveReqVO createReqVO) {
        return success(screenSputumExaminationService.createScreenSputumExamination(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新痰检组")
    @PreAuthorize("@ss.hasPermission('tb:screen-sputum-examination:update')")
    public CommonResult<Boolean> updateScreenSputumExamination(@Valid @RequestBody ScreenSputumExaminationSaveReqVO updateReqVO) {
        screenSputumExaminationService.updateScreenSputumExamination(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除痰检组")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('tb:screen-sputum-examination:delete')")
    public CommonResult<Boolean> deleteScreenSputumExamination(@RequestParam("id") Long id) {
        screenSputumExaminationService.deleteScreenSputumExamination(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得痰检组")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('tb:screen-sputum-examination:query')")
    public CommonResult<ScreenSputumExaminationRespVO> getScreenSputumExamination(@RequestParam("id") Long id) {
        ScreenSputumExaminationDO screenSputumExamination = screenSputumExaminationService.getScreenSputumExamination(id);
        return success(BeanUtils.toBean(screenSputumExamination, ScreenSputumExaminationRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得痰检组分页")
    @PreAuthorize("@ss.hasPermission('tb:screen-sputum-examination:query')")
    public CommonResult<PageResult<ScreenSputumExaminationRespVO>> getScreenSputumExaminationPage(@Valid ScreenSputumExaminationPageReqVO pageReqVO) {
        PageResult<ScreenSputumExaminationDO> pageResult = screenSputumExaminationService.getScreenSputumExaminationPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ScreenSputumExaminationRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出痰检组 Excel")
    @PreAuthorize("@ss.hasPermission('tb:screen-sputum-examination:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportScreenSputumExaminationExcel(@Valid ScreenSputumExaminationPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ScreenSputumExaminationDO> list = screenSputumExaminationService.getScreenSputumExaminationPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "痰检组.xls", "数据", ScreenSputumExaminationRespVO.class,
                        BeanUtils.toBean(list, ScreenSputumExaminationRespVO.class));
    }

}
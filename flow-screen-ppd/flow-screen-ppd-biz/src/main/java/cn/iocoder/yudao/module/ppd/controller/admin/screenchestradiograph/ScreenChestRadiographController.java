package cn.iocoder.yudao.module.ppd.controller.admin.screenchestradiograph;


import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.ppd.controller.admin.screenchestradiograph.vo.*;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenchestradiograph.ScreenChestRadiographDO;
import cn.iocoder.yudao.module.ppd.service.screenchestradiograph.ScreenChestRadiographService;
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

@Tag(name = "管理后台 - ct、dr组")
@RestController
@RequestMapping("/tb/screen-chest-radiograph")
@Validated
public class ScreenChestRadiographController {

    @Resource
    private ScreenChestRadiographService screenChestRadiographService;

    @PostMapping("/create")
    @Operation(summary = "创建ct、dr组")
    @PreAuthorize("@ss.hasPermission('tb:screen-chest-radiograph:create')")
    public CommonResult<Long> createScreenChestRadiograph(@Valid @RequestBody ScreenChestRadiographSaveReqVO createReqVO) {
        return success(screenChestRadiographService.createScreenChestRadiograph(createReqVO));
    }

    @PostMapping("/update")
    @Operation(summary = "更新ct、dr组")
    @PreAuthorize("@ss.hasPermission('tb:screen-chest-radiograph:update')")
    public CommonResult<Boolean> updateScreenChestRadiograph(@Valid @RequestBody ScreenChestRadiographSaveReqVO updateReqVO) {
        screenChestRadiographService.updateScreenChestRadiograph(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除ct、dr组")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('tb:screen-chest-radiograph:delete')")
    public CommonResult<Boolean> deleteScreenChestRadiograph(@RequestParam("id") Long id) {
        screenChestRadiographService.deleteScreenChestRadiograph(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得ct、dr组")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('tb:screen-chest-radiograph:query')")
    public CommonResult<ScreenChestRadiographRespVO> getScreenChestRadiograph(@RequestParam("id") Long id) {
        ScreenChestRadiographDO screenChestRadiograph = screenChestRadiographService.getScreenChestRadiograph(id);
        return success(BeanUtils.toBean(screenChestRadiograph, ScreenChestRadiographRespVO.class));
    }

     @GetMapping("/getOne")
    @Operation(summary = "获得ct、dr组")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('tb:screen-chest-radiograph:query')")
    public CommonResult<ScreenChestRadiographRespVO> getScreenChestRadiograph(@RequestParam("personId") Long personId,@RequestParam("screenOrder") Integer screenOrder,@RequestParam("screenType") Integer screenType) {
          return success(screenChestRadiographService.getOneByPersonId(personId, screenOrder,screenType));
    }

    @GetMapping("/page")
    @Operation(summary = "获得ct、dr组分页")
    @PreAuthorize("@ss.hasPermission('tb:screen-chest-radiograph:query')")
    public CommonResult<PageResult<ScreenChestRadiographPersonRespVO>> getScreenChestRadiographPage(@Valid ScreenChestRadiographPersonPageReqVO pageReqVO) {
        PageResult<ScreenChestRadiographPersonRespVO> pageResult = screenChestRadiographService.getScreenChestRadiographPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ScreenChestRadiographPersonRespVO.class));
    }
    @GetMapping("/maxOrder")
    @Operation(summary = "最大筛查次序")
    @PreAuthorize("@ss.hasPermission('tb:screen-chest-radiograph:query')")
    public Integer getScreenChestRadiographPage(@RequestParam("personId") Long personId,@RequestParam("screenType") Integer screenType) {
        return screenChestRadiographService.getMaxOrder(personId,screenType);
    }
    @GetMapping("/statiscs")
    @Operation(summary = "页面统计")
    @PreAuthorize("@ss.hasPermission('tb:screen-chest-radiograph:query')")
    public CommonResult<ChestRadiographStatistics> getScreenChestRadiographStatistics(@RequestParam("screenType") Integer screenType) {
        return success(screenChestRadiographService.getTimeStatistics(screenType));
    }

    @GetMapping("/getOrders")
    @Operation(summary = "获取时间和次序")
    public CommonResult<List<ChestRadiographOrderRespVO>> getOrderAndTime(Long personId){
        return success(screenChestRadiographService.getOrderAndTime(personId));
    }
    @GetMapping("/export-excel")
    @Operation(summary = "导出ct、dr组 Excel")
    @PreAuthorize("@ss.hasPermission('tb:screen-chest-radiograph:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportScreenChestRadiographExcel(@Valid ScreenChestRadiographPersonPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ScreenChestRadiographPersonRespVO> list = screenChestRadiographService.getScreenChestRadiographPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "ct、dr组.xls", "数据", ScreenChestRadiographRespVO.class,
                        BeanUtils.toBean(list, ScreenChestRadiographRespVO.class));
    }

}
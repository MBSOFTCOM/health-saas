package cn.iocoder.yudao.module.ppd.controller.admin.screencomputedtomography;


import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.ppd.controller.admin.screencomputedtomography.vo.*;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screencomputedtomography.ScreenComputedTomographyDO;
import cn.iocoder.yudao.module.ppd.service.screencomputedtomography.ScreenComputedTomographyService;
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

@Tag(name = "管理后台 - ct组")
@RestController
@RequestMapping("/tb/screen-computed-tomography")
@Validated
public class ScreenComputedTomographyController {

    @Resource
    private ScreenComputedTomographyService screenComputedTomographyService;

    @PostMapping("/create")
    @Operation(summary = "创建ct组")
    @PreAuthorize("@ss.hasPermission('tb:screen-chest-radiograph:create')")
    public CommonResult<Long> createScreenChestRadiograph(@Valid @RequestBody ScreenComputedTomographySaveReqVO createReqVO) {
        return success(screenComputedTomographyService.createScreenComputedTomography(createReqVO));
    }
    @PostMapping("/createTrans")
    @Operation(summary = "创建ct组的事务")
    @PreAuthorize("@ss.hasPermission('tb:screen-chest-radiograph:create')")
    public CommonResult<Boolean> createScreenChestRadiographTrans(@Valid @RequestBody ScreenComputedTomographySaveReqVO createReqVO) {
        screenComputedTomographyService.createScreenComputedTomographyTrans(createReqVO);
        return success(true);
    }
    @PostMapping("/create-order")
    @Operation(summary = "获取创建ct组时的次序")
    @PreAuthorize("@ss.hasPermission('tb:screen-chest-radiograph:create')")
    public CommonResult<Integer> getCreateOrder(@Valid @RequestBody ScreenComputedTomographySaveReqVO createReqVO) {
        return success(screenComputedTomographyService.getCreateOrder(createReqVO));
    }

    @PostMapping("/update")
    @Operation(summary = "更新ct、dr组")
    @PreAuthorize("@ss.hasPermission('tb:screen-chest-radiograph:update')")
    public CommonResult<Boolean> updateScreenChestRadiograph(@Valid @RequestBody ScreenComputedTomographySaveReqVO updateReqVO) {
        screenComputedTomographyService.updateScreenComputedTomography(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除ct、dr组")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('tb:screen-chest-radiograph:delete')")
    public CommonResult<Boolean> deleteScreenChestRadiograph(@RequestParam("id") Long id) {
        screenComputedTomographyService.deleteScreenComputedTomography(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得ct、dr组")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('tb:screen-chest-radiograph:query')")
    public CommonResult<ScreenComputedTomographyRespVO> getScreenChestRadiograph(@RequestParam("id") Long id) {
        ScreenComputedTomographyDO screenComputedTomography = screenComputedTomographyService.getScreenComputedTomography(id);
        return success(BeanUtils.toBean(screenComputedTomography, ScreenComputedTomographyRespVO.class));
    }

     @GetMapping("/getOne")
    @Operation(summary = "获得ct组")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('tb:screen-chest-radiograph:query')")
    public CommonResult<ScreenComputedTomographyRespVO> getScreenChestRadiograph(@RequestParam("idNum") String  idNum, @RequestParam("screenOrder") Integer screenOrder, @RequestParam("screenType") Integer screenType, @RequestParam("year") Integer year) {
         ScreenComputedTomographyRespVO one = screenComputedTomographyService.getOneByPersonIdNum(idNum, screenOrder, screenType,year);
         return success(one);
    }

    @GetMapping("/page")
    @Operation(summary = "获得ct组分页")
    @PreAuthorize("@ss.hasPermission('tb:screen-chest-radiograph:query')")
    public CommonResult<PageResult<ScreenComputedTomographyPersonRespVO>> getScreenChestRadiographPage(@Valid ScreenComputedTomographyPersonPageReqVO pageReqVO) {
        PageResult<ScreenComputedTomographyPersonRespVO> pageResult = screenComputedTomographyService.getScreenComputedTomographyPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ScreenComputedTomographyPersonRespVO.class));
    }
    @GetMapping("/outcome")
    @Operation(summary = "获得ct检测结果")
    @PreAuthorize("@ss.hasPermission('tb:screen-chest-radiograph:query')")
    public CommonResult<Integer> getScreenChestRadiographOutcome(ScreenComputedTomographyPageReqVO reqVO) {
//        screenComputedTomographyService.getOutcomeByOrder(reqVO);
        return success(screenComputedTomographyService.getOutcomeByOrder(reqVO));
    }
    @GetMapping("/maxOrder")
    @Operation(summary = "最大筛查次序")
    @PreAuthorize("@ss.hasPermission('tb:screen-chest-radiograph:query')")
    public Integer getScreenChestRadiographPage(@RequestParam("idNum") String idNum,@RequestParam("screenType") Integer screenType,@RequestParam("year") Integer year) {
        return screenComputedTomographyService.getMaxOrder(idNum,screenType,year);
    }
    @GetMapping("/statiscs")
    @Operation(summary = "页面统计")
    @PreAuthorize("@ss.hasPermission('tb:screen-chest-radiograph:query')")
    public CommonResult<ComputedTomographyStatistics> getScreenChestRadiographStatistics(@RequestParam("screenType") Integer screenType) {
        return success(screenComputedTomographyService.getTimeStatistics(screenType));
    }

    @GetMapping("/getOrders")
    @Operation(summary = "获取时间和次序")
    public CommonResult<List<ComputedTomographyOrderRespVO>> getOrderAndTime(@RequestParam("personId") Long personId, @RequestParam("screenType") Integer screenType, @RequestParam("year") Integer year){
        List<ComputedTomographyOrderRespVO> orderAndTime = screenComputedTomographyService.getOrderAndTime(personId, screenType, year);
        return success(orderAndTime);
    }
    @GetMapping("/export-excel")
    @Operation(summary = "导出ct、dr组 Excel")
    @PreAuthorize("@ss.hasPermission('tb:screen-chest-radiograph:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportScreenChestRadiographExcel(@Valid ScreenComputedTomographyPersonPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ScreenComputedTomographyPersonRespVO> list = screenComputedTomographyService.getScreenComputedTomographyPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "ct、dr组.xls", "数据", ScreenComputedTomographyRespVO.class,
                        BeanUtils.toBean(list, ScreenComputedTomographyRespVO.class));
    }

}
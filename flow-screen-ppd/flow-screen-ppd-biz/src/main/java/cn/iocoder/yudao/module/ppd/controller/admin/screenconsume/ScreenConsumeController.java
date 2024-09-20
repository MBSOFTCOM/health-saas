package cn.iocoder.yudao.module.ppd.controller.admin.screenconsume;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.ppd.controller.admin.screenconsume.vo.*;
import cn.iocoder.yudao.module.ppd.controller.admin.screenreagent.vo.ScreenReagentImportRespVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenconsume.ScreenConsumeDO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenreagent.ScreenReagentDO;
import cn.iocoder.yudao.module.ppd.service.screenconsume.ScreenConsumeService;
import cn.iocoder.yudao.module.ppd.service.screenreagent.ScreenReagentService;
import cn.iocoder.yudao.module.ppd.utils.CustomSheetWriteHandler;
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

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 消耗管理")
@RestController
@RequestMapping("/tb/screen-consume")
@Validated
public class ScreenConsumeController {

    @Resource
    private ScreenConsumeService screenConsumeService;
    @Resource
    private ScreenReagentService screenReagentService;

    @PostMapping("/create")
    @Operation(summary = "创建消耗管理")
    @PreAuthorize("@ss.hasPermission('tb:screen-consume:create')")
    public CommonResult<Long> createScreenConsume(@Valid @RequestBody ScreenConsumeSaveReqVO createReqVO) {
        return success(screenConsumeService.createScreenConsume(createReqVO));
    }

    @GetMapping("/getUsable")
    @Operation(summary = "获取可用的试剂批号明细")
    @PreAuthorize("@ss.hasPermission('tb:screen-consume:query')")
    public CommonResult<List<ScreenConsumeDO>> getUsableConsume(ScreenConsumePageReqVO reqVO){
        List<ScreenConsumeDO> consume = screenConsumeService.getUsableScreenConsume(reqVO);
        return success(consume);
    }

    @PutMapping("/update")
    @Operation(summary = "更新消耗管理")
    @PreAuthorize("@ss.hasPermission('tb:screen-consume:update')")
    public CommonResult<Boolean> updateScreenConsume(@Valid @RequestBody ScreenConsumeSaveReqVO updateReqVO) {
        screenConsumeService.updateScreenConsume(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除消耗管理")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('tb:screen-consume:delete')")
    public CommonResult<Boolean> deleteScreenConsume(@RequestParam("id") Long id) {
        screenConsumeService.deleteScreenConsume(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得消耗管理")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('tb:screen-consume:query')")
    public CommonResult<ScreenConsumeRespVO> getScreenConsume(@RequestParam("id") Long id) {
        ScreenConsumeDO screenConsume = screenConsumeService.getScreenConsume(id);
        return success(BeanUtils.toBean(screenConsume, ScreenConsumeRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得消耗管理分页")
    @PreAuthorize("@ss.hasPermission('tb:screen-consume:query')")
    public CommonResult<PageResult<ScreenConsumeRespVO>> getScreenConsumePage(@Valid ScreenConsumePageReqVO pageReqVO) {
        PageResult<ScreenConsumeDO> pageResult = screenConsumeService.getScreenConsumePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ScreenConsumeRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出消耗管理 Excel")
    @PreAuthorize("@ss.hasPermission('tb:screen-consume:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportScreenConsumeExcel(@Valid ScreenConsumePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ScreenConsumeDO> list = screenConsumeService.getScreenConsumePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write2(response, "消耗管理.xls", "数据", ScreenConsumeRespVO.class,
                        BeanUtils.toBean(list, ScreenConsumeRespVO.class));
    }

    @GetMapping("/get-reagent-list")
    @Operation(summary = "获取试剂列表")
    @PreAuthorize("@ss.hasPermission('tb:screen-consume:create')")
    public CommonResult<List<ScreenReagentDO>> getReagentList() {
        List<ScreenReagentDO> list = screenReagentService.getReagentInfoList();
        return success(list);
    }

    @GetMapping("/increase")
    @Operation(summary = "增加入库量、当前库存")
    @Parameters({
            @Parameter(name = "id", description = "消耗管理id", required = true),
            @Parameter(name = "number", description = "增加的量", required = true)
    })
    @PreAuthorize("@ss.hasPermission('tb:screen-consume:update')")
    public CommonResult<Boolean> increaseScreenConsume(@RequestParam("id") Long id,
                                                       @RequestParam("number") Integer number) {
        Boolean isSuccess = screenConsumeService.increaseScreenConsume(id, number);
        return success(isSuccess);
    }

    @GetMapping("/decrease")
    @Operation(summary = "减少当前库存")
    @Parameters({
            @Parameter(name = "id", description = "消耗管理id", required = true),
            @Parameter(name = "number", description = "减少的量", required = true)
    })
    @PreAuthorize("@ss.hasPermission('tb:screen-consume:update')")
    public CommonResult<Boolean> decreaseScreenConsume(@RequestParam("id") Long id,
                                                       @RequestParam("number") Integer number) {
        Boolean isSuccess = screenConsumeService.decreaseScreenConsume(id, number);
        return success(isSuccess);
    }

    @GetMapping("/import-template")
    @Operation(summary = "下载消耗管理导入模板")
    @PreAuthorize("@ss.hasPermission('tb:screen-consume:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void getImportTemplateScreenConsumeExcel(HttpServletResponse response) throws IOException {

        // 模板数据
        List<ScreenConsumeImportVO> list = screenConsumeService.createSampleData();
        // 存放下拉选列表数据
        Map<Integer, List<String>> selectedData = new HashMap<>();
        List<String> reagentList = screenReagentService.getReagentList2();
        if (reagentList.size() > 0){
            selectedData.put(0, reagentList);
        }
        ExcelUtils.write(response, "消耗管理导入模板.xls", "消耗管理数据", ScreenConsumeImportVO.class,
                BeanUtils.toBean(list, ScreenConsumeImportVO.class), new CustomSheetWriteHandler().setMap(selectedData));
    }

    @PostMapping("/import-excel")
    @Operation(summary = "导入消耗管理")
    @PreAuthorize("@ss.hasPermission('tb:screen-consume:create')")
    @Parameter(name = "file", description = "Excel 文件", required = true)
    public CommonResult<ScreenReagentImportRespVO> importExcel(@RequestParam("file") MultipartFile file) throws Exception {
        List<ScreenConsumeImportVO> list = ExcelUtils.read(file, ScreenConsumeImportVO.class);
        return success(screenConsumeService.importScreenConsume(list));
    }

    @GetMapping("/statistics")
    @Operation(summary = "获得消耗管理数据统计")
    public CommonResult<List<ScreenConsumeStatisticsRespVO>> getScreenConsumeStatistics(@Valid ScreenConsumePageReqVO pageReqVO) {
        List<ScreenConsumeStatisticsRespVO> pageResult = screenConsumeService.getScreenConsumeStatistics(pageReqVO);
        return success(pageResult);
    }

    @GetMapping("/export-statistics")
    @Operation(summary = "导出消耗管理数据统计")
    public void exportScreenConsumeStatistics(@Valid ScreenConsumePageReqVO pageReqVO,
                                         HttpServletResponse response) throws IOException {
        List<ScreenConsumeStatisticsRespVO> pageResult = screenConsumeService.getScreenConsumeStatistics(pageReqVO);
        // 导出 Excel
        ExcelUtils.write2(response, "消耗数据统计.xls", "数据", ScreenConsumeStatisticsRespVO.class,
                BeanUtils.toBean(pageResult, ScreenConsumeStatisticsRespVO.class));
    }

}
package cn.iocoder.yudao.module.ppd.controller.admin.screenreagent;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.ppd.controller.admin.screenconsume.vo.ScreenConsumePageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenreagent.vo.*;
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



@Tag(name = "管理后台 - 试剂")
@RestController
@RequestMapping("/tb/screen-reagent")
@Validated
public class ScreenReagentController {

    @Resource
    private ScreenReagentService screenReagentService;
    @Resource
    private ScreenConsumeService screenConsumeService;

    @PostMapping("/create")
    @Operation(summary = "创建试剂")
    @PreAuthorize("@ss.hasPermission('tb:screen-reagent:create')")
    public CommonResult<Long> createScreenReagent(@Valid @RequestBody ScreenReagentSaveReqVO createReqVO) {
        return success(screenReagentService.createScreenReagent(createReqVO));
    }

    @GetMapping("/getRegent")
    @Operation(summary = "获取所有可用试剂")
    @PreAuthorize("@ss.hasPermission('tb:screen-reagent:query')")
    public CommonResult<List<ScreenReagentRespVO>> getReagent(ScreenReagentPageReqVO pageReqVO) {
        List<ScreenReagentDO> usableReagent = screenReagentService.getUsableReagent(pageReqVO);
        return success(BeanUtils.toBean(usableReagent,ScreenReagentRespVO.class));
    }

    @PutMapping("/update")
    @Operation(summary = "更新试剂")
    @PreAuthorize("@ss.hasPermission('tb:screen-reagent:update')")
    public CommonResult<Boolean> updateScreenReagent(@Valid @RequestBody ScreenReagentSaveReqVO updateReqVO) {
        screenReagentService.updateScreenReagent(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除试剂")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('tb:screen-reagent:delete')")
    public CommonResult<Boolean> deleteScreenReagent(@RequestParam("id") Long id) {
        screenReagentService.deleteScreenReagent(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得试剂")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('tb:screen-reagent:query')")
    public CommonResult<ScreenReagentRespVO> getScreenReagent(@RequestParam("id") Long id) {
        ScreenReagentDO screenReagent = screenReagentService.getScreenReagent(id);
        return success(BeanUtils.toBean(screenReagent, ScreenReagentRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得试剂分页")
    @PreAuthorize("@ss.hasPermission('tb:screen-reagent:query')")
    public CommonResult<PageResult<ScreenReagentRespVO>> getScreenReagentPage(@Valid ScreenReagentPageReqVO pageReqVO) {
        PageResult<ScreenReagentDO> pageResult = screenReagentService.getScreenReagentPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ScreenReagentRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出试剂 Excel")
    @PreAuthorize("@ss.hasPermission('tb:screen-reagent:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportScreenReagentExcel(@Valid ScreenReagentPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ScreenReagentDO> list = screenReagentService.getScreenReagentPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write2(response, "试剂名录.xls", "试剂数据", ScreenReagentRespVO.class,
                        BeanUtils.toBean(list, ScreenReagentRespVO.class));
    }

    @GetMapping("/import-template")
    @Operation(summary = "下载试剂导入模板")
    @PreAuthorize("@ss.hasPermission('tb:screen-reagent:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void getImportTemplateReagentExcel(HttpServletResponse response) throws IOException {

        // 模板数据
        List<ScreenReagentImportVO> list = screenReagentService.createSampleData();
        // 存放下拉选列表数据
        Map<Integer, List<String>> selectedData = new HashMap<>();
        screenReagentService.addSelectedData("dosage_form", 1, selectedData);
        screenReagentService.addSelectedData("tb_potency_unit", 4, selectedData);
        screenReagentService.addSelectedData("tb_specification", 6, selectedData);
        screenReagentService.addSelectedData("tb_package", 7, selectedData);

        ExcelUtils.write(response, "试剂导入模板.xls", "试剂数据", ScreenReagentImportVO.class,
                        BeanUtils.toBean(list, ScreenReagentImportVO.class), new CustomSheetWriteHandler().setMap(selectedData));
    }

    @PostMapping("/import-excel")
    @Operation(summary = "导入试剂")
    @PreAuthorize("@ss.hasPermission('tb:screen-reagent:create')")
    @Parameters({
            @Parameter(name = "file", description = "Excel 文件", required = true),
    })
    public CommonResult<ScreenReagentImportRespVO> importExcel(@RequestParam("file") MultipartFile file)
            throws Exception {
        List<ScreenReagentImportVO> list = ExcelUtils.read(file, ScreenReagentImportVO.class);
        return success(screenReagentService.importReagent(list));
    }


    @GetMapping("/forbid")
    @Operation(summary = "禁用试剂")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('tb:screen-reagent:delete')")
    public CommonResult<Boolean> forbidScreenReagent(@RequestParam("id") Long id) {
        Boolean isForbid = screenReagentService.forbidScreenReagent(id);
        return success(isForbid);
    }

    @GetMapping("/recover")
    @Operation(summary = "启用试剂")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('tb:screen-reagent:delete')")
    public CommonResult<Boolean> recoverScreenReagent(@RequestParam("id") Long id) {
        Boolean isForbid = screenReagentService.recoverScreenReagent(id);
        return success(isForbid);
    }

    @GetMapping("/getObj")
    public Object get(Object o){
        ScreenConsumePageReqVO pageReqVO = BeanUtils.toBean(o, ScreenConsumePageReqVO.class);
        List<ScreenConsumeDO> consume = screenConsumeService.getUsableScreenConsume(pageReqVO);
        return consume;
    }
    @PostMapping("/postObj")
    public Object post(Object o){
        ScreenReagentPageReqVO pageReqVO = BeanUtils.toBean(o, ScreenReagentPageReqVO.class);
        return screenReagentService.getUsableReagent(pageReqVO);
    }
}
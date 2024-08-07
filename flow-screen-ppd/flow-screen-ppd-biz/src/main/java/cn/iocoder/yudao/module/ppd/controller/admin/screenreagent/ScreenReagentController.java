package cn.iocoder.yudao.module.tb.controller.admin.screenreagent;

import cn.iocoder.yudao.module.ppd.controller.admin.screenreagent.vo.ScreenReagentPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenreagent.vo.ScreenReagentRespVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenreagent.vo.ScreenReagentSaveReqVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenreagent.ScreenReagentDO;
import cn.iocoder.yudao.module.ppd.service.screenreagent.ScreenReagentService;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.validation.*;
import jakarta.servlet.http.*;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;



@Tag(name = "管理后台 - 试剂")
@RestController
@RequestMapping("/tb/screen-reagent")
@Validated
public class ScreenReagentController {

    @Resource
    private ScreenReagentService screenReagentService;

    @PostMapping("/create")
    @Operation(summary = "创建试剂")
    @PreAuthorize("@ss.hasPermission('tb:screen-reagent:create')")
    public CommonResult<Long> createScreenReagent(@Valid @RequestBody ScreenReagentSaveReqVO createReqVO) {
        return success(screenReagentService.createScreenReagent(createReqVO));
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
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
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
        ExcelUtils.write(response, "试剂.xls", "数据", ScreenReagentRespVO.class,
                        BeanUtils.toBean(list, ScreenReagentRespVO.class));
    }

}
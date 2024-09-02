package cn.iocoder.yudao.module.ppd.controller.admin.screenstaticshistory;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.ppd.controller.admin.screenstaticshistory.vo.ScreenStaticsHistoryPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenstaticshistory.vo.ScreenStaticsHistoryRespVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenstaticshistory.vo.ScreenStaticsHistorySaveReqVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenstaticshistory.ScreenStaticsHistoryDO;
import cn.iocoder.yudao.module.ppd.service.screenstaticshistory.ScreenStaticsHistoryService;
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

@Tag(name = "管理后台 - 工作进展报告-统计表-导出的历史选项")
@RestController
@RequestMapping("/tb/screen-statics-history")
@Validated
public class ScreenStaticsHistoryController {

    @Resource
    private ScreenStaticsHistoryService screenStaticsHistoryService;

    @PostMapping("/create")
    @Operation(summary = "创建工作进展报告-统计表-导出的历史选项")
    @PreAuthorize("@ss.hasPermission('tb:screen-statics-history:create')")
    public CommonResult<Long> createScreenStaticsHistory(@Valid @RequestBody ScreenStaticsHistorySaveReqVO createReqVO) {
        return success(screenStaticsHistoryService.createScreenStaticsHistory(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新工作进展报告-统计表-导出的历史选项")
    @PreAuthorize("@ss.hasPermission('tb:screen-statics-history:update')")
    public CommonResult<Boolean> updateScreenStaticsHistory(@Valid @RequestBody ScreenStaticsHistorySaveReqVO updateReqVO) {
        screenStaticsHistoryService.updateScreenStaticsHistory(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除工作进展报告-统计表-导出的历史选项")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('tb:screen-statics-history:delete')")
    public CommonResult<Boolean> deleteScreenStaticsHistory(@RequestParam("id") Long id) {
        screenStaticsHistoryService.deleteScreenStaticsHistory(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得工作进展报告-统计表-导出的历史选项")
//    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('tb:screen-statics-history:query')")
    public CommonResult<ScreenStaticsHistoryRespVO> getScreenStaticsHistory() {
//        ScreenStaticsHistoryDO screenStaticsHistory = screenStaticsHistoryService.getScreenStaticsHistory(id);
        ScreenStaticsHistoryRespVO history = screenStaticsHistoryService.getHistory(null);
        return success(history);
    }

    @GetMapping("/page")
    @Operation(summary = "获得工作进展报告-统计表-导出的历史选项分页")
    @PreAuthorize("@ss.hasPermission('tb:screen-statics-history:query')")
    public CommonResult<PageResult<ScreenStaticsHistoryRespVO>> getScreenStaticsHistoryPage(@Valid ScreenStaticsHistoryPageReqVO pageReqVO) {
        PageResult<ScreenStaticsHistoryDO> pageResult = screenStaticsHistoryService.getScreenStaticsHistoryPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ScreenStaticsHistoryRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出工作进展报告-统计表-导出的历史选项 Excel")
    @PreAuthorize("@ss.hasPermission('tb:screen-statics-history:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportScreenStaticsHistoryExcel(@Valid ScreenStaticsHistoryPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ScreenStaticsHistoryDO> list = screenStaticsHistoryService.getScreenStaticsHistoryPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "工作进展报告-统计表-导出的历史选项.xls", "数据", ScreenStaticsHistoryRespVO.class,
                        BeanUtils.toBean(list, ScreenStaticsHistoryRespVO.class));
    }

}
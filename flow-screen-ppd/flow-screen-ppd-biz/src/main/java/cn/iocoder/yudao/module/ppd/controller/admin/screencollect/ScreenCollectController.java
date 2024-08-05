package cn.iocoder.yudao.module.ppd.controller.admin.screencollect;


import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.ppd.controller.admin.screencollect.vo.ScreenCollectPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screencollect.vo.ScreenCollectRespVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screencollect.vo.ScreenCollectSaveReqVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screencollect.ScreenCollectDO;
import cn.iocoder.yudao.module.ppd.service.screencollect.ScreenCollectService;
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

@Tag(name = "管理后台 - 采集")
@RestController
@RequestMapping("/tb/screen-collect")
@Validated
public class ScreenCollectController {

    @Resource
    private ScreenCollectService screenCollectService;

    @PostMapping("/create")
    @Operation(summary = "创建采集")
    @PreAuthorize("@ss.hasPermission('tb:screen-collect:create')")
    public CommonResult<Long> createScreenCollect(@Valid @RequestBody ScreenCollectSaveReqVO createReqVO) {
        return success(screenCollectService.createScreenCollect(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新采集")
    @PreAuthorize("@ss.hasPermission('tb:screen-collect:update')")
    public CommonResult<Boolean> updateScreenCollect(@Valid @RequestBody ScreenCollectSaveReqVO updateReqVO) {
        screenCollectService.updateScreenCollect(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除采集")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('tb:screen-collect:delete')")
    public CommonResult<Boolean> deleteScreenCollect(@RequestParam("id") Long id) {
        screenCollectService.deleteScreenCollect(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得采集")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('tb:screen-collect:query')")
    public CommonResult<ScreenCollectRespVO> getScreenCollect(@RequestParam("id") Long id) {
        ScreenCollectDO screenCollect = screenCollectService.getScreenCollect(id);
        return success(BeanUtils.toBean(screenCollect, ScreenCollectRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得采集分页")
    @PreAuthorize("@ss.hasPermission('tb:screen-collect:query')")
    public CommonResult<PageResult<ScreenCollectRespVO>> getScreenCollectPage(@Valid ScreenCollectPageReqVO pageReqVO) {
        PageResult<ScreenCollectDO> pageResult = screenCollectService.getScreenCollectPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ScreenCollectRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出采集 Excel")
    @PreAuthorize("@ss.hasPermission('tb:screen-collect:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportScreenCollectExcel(@Valid ScreenCollectPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ScreenCollectDO> list = screenCollectService.getScreenCollectPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "采集.xls", "数据", ScreenCollectRespVO.class,
                        BeanUtils.toBean(list, ScreenCollectRespVO.class));
    }

}
package cn.iocoder.yudao.module.ppd.controller.admin.screenppd;


import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.ppd.controller.admin.screenppd.vo.ScreenPpdPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenppd.vo.ScreenPpdRespVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenppd.vo.ScreenPpdSaveReqVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenppd.ScreenPpdDO;
import cn.iocoder.yudao.module.ppd.service.screenppd.ScreenPpdService;
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

@Tag(name = "管理后台 - ppd组记录")
@RestController
@RequestMapping("/tb/screen-ppd")
@Validated
public class ScreenPpdController {

    @Resource
    private ScreenPpdService screenPpdService;

    @PostMapping("/create")
    @Operation(summary = "创建ppd组记录")
    @PreAuthorize("@ss.hasPermission('tb:screen-ppd:create')")
    public CommonResult<Long> createScreenPpd(@Valid @RequestBody ScreenPpdSaveReqVO createReqVO) {
        return success(screenPpdService.createScreenPpd(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新ppd组记录")
    @PreAuthorize("@ss.hasPermission('tb:screen-ppd:update')")
    public CommonResult<Boolean> updateScreenPpd(@Valid @RequestBody ScreenPpdSaveReqVO updateReqVO) {
        screenPpdService.updateScreenPpd(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除ppd组记录")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('tb:screen-ppd:delete')")
    public CommonResult<Boolean> deleteScreenPpd(@RequestParam("id") Long id) {
        screenPpdService.deleteScreenPpd(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得ppd组记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('tb:screen-ppd:query')")
    public CommonResult<ScreenPpdRespVO> getScreenPpd(@RequestParam("id") Long id) {
        ScreenPpdDO screenPpd = screenPpdService.getScreenPpd(id);
        return success(BeanUtils.toBean(screenPpd, ScreenPpdRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得ppd组记录分页")
    @PreAuthorize("@ss.hasPermission('tb:screen-ppd:query')")
    public CommonResult<PageResult<ScreenPpdRespVO>> getScreenPpdPage(@Valid ScreenPpdPageReqVO pageReqVO) {
        PageResult<ScreenPpdDO> pageResult = screenPpdService.getScreenPpdPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ScreenPpdRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出ppd组记录 Excel")
    @PreAuthorize("@ss.hasPermission('tb:screen-ppd:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportScreenPpdExcel(@Valid ScreenPpdPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ScreenPpdDO> list = screenPpdService.getScreenPpdPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "ppd组记录.xls", "数据", ScreenPpdRespVO.class,
                        BeanUtils.toBean(list, ScreenPpdRespVO.class));
    }

}
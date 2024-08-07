package cn.iocoder.yudao.module.ppd.controller.admin.screenconsume;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.ppd.controller.admin.screenconsume.vo.ScreenConsumePageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenconsume.vo.ScreenConsumeRespVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenconsume.vo.ScreenConsumeSaveReqVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenconsume.ScreenConsumeDO;
import cn.iocoder.yudao.module.ppd.service.screenconsume.ScreenConsumeService;
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

@Tag(name = "管理后台 - 消耗管理")
@RestController
@RequestMapping("/tb/screen-consume")
@Validated
public class ScreenConsumeController {

    @Resource
    private ScreenConsumeService screenConsumeService;

    @PostMapping("/create")
    @Operation(summary = "创建消耗管理")
    @PreAuthorize("@ss.hasPermission('tb:screen-consume:create')")
    public CommonResult<Long> createScreenConsume(@Valid @RequestBody ScreenConsumeSaveReqVO createReqVO) {
        return success(screenConsumeService.createScreenConsume(createReqVO));
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
        ExcelUtils.write(response, "消耗管理.xls", "数据", ScreenConsumeRespVO.class,
                        BeanUtils.toBean(list, ScreenConsumeRespVO.class));
    }

}
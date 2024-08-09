package cn.iocoder.yudao.module.ppd.controller.admin.screenconsumerecord;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.ppd.controller.admin.screenconsumerecord.vo.ScreenConsumeRecordPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenconsumerecord.vo.ScreenConsumeRecordRespVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenconsumerecord.vo.ScreenConsumeRecordSaveReqVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenconsumerecord.ScreenConsumeRecordDO;
import cn.iocoder.yudao.module.ppd.service.screenconsumerecord.ScreenConsumeRecordService;
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

@Tag(name = "管理后台 - 消耗管理记录")
@RestController
@RequestMapping("/tb/screen-consume-record")
@Validated
public class ScreenConsumeRecordController {

    @Resource
    private ScreenConsumeRecordService screenConsumeRecordService;

    @PostMapping("/create")
    @Operation(summary = "创建消耗管理记录")
    @PreAuthorize("@ss.hasPermission('tb:screen-consume-record:create')")
    public CommonResult<Long> createScreenConsumeRecord(@Valid @RequestBody ScreenConsumeRecordSaveReqVO createReqVO) {
        return success(screenConsumeRecordService.createScreenConsumeRecord(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新消耗管理记录")
    @PreAuthorize("@ss.hasPermission('tb:screen-consume-record:update')")
    public CommonResult<Boolean> updateScreenConsumeRecord(@Valid @RequestBody ScreenConsumeRecordSaveReqVO updateReqVO) {
        screenConsumeRecordService.updateScreenConsumeRecord(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除消耗管理记录")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('tb:screen-consume-record:delete')")
    public CommonResult<Boolean> deleteScreenConsumeRecord(@RequestParam("id") Long id) {
        screenConsumeRecordService.deleteScreenConsumeRecord(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得消耗管理记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('tb:screen-consume-record:query')")
    public CommonResult<ScreenConsumeRecordRespVO> getScreenConsumeRecord(@RequestParam("id") Long id) {
        ScreenConsumeRecordDO screenConsumeRecord = screenConsumeRecordService.getScreenConsumeRecord(id);
        return success(BeanUtils.toBean(screenConsumeRecord, ScreenConsumeRecordRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得消耗管理记录分页")
    @PreAuthorize("@ss.hasPermission('tb:screen-consume-record:query')")
    public CommonResult<PageResult<ScreenConsumeRecordRespVO>> getScreenConsumeRecordPage(@Valid ScreenConsumeRecordPageReqVO pageReqVO) {
        PageResult<ScreenConsumeRecordDO> pageResult = screenConsumeRecordService.getScreenConsumeRecordPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ScreenConsumeRecordRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出消耗管理记录 Excel")
    @PreAuthorize("@ss.hasPermission('tb:screen-consume-record:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportScreenConsumeRecordExcel(@Valid ScreenConsumeRecordPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ScreenConsumeRecordDO> list = screenConsumeRecordService.getScreenConsumeRecordPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "消耗管理记录.xls", "数据", ScreenConsumeRecordRespVO.class,
                        BeanUtils.toBean(list, ScreenConsumeRecordRespVO.class));
    }

    @GetMapping("/get-list")
    @Operation(summary = "获得消耗管理记录列表")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<List<ScreenConsumeRecordRespVO>> getScreenConsumeRecordList(@RequestParam("id") Long id) {
        List<ScreenConsumeRecordRespVO> list = screenConsumeRecordService.getScreenConsumeRecordList(id);
        return success(list);
    }

}
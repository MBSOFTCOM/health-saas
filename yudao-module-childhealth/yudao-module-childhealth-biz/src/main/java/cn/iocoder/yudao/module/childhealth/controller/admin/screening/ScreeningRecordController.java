package cn.iocoder.yudao.module.childhealth.controller.admin.screening;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.record.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.ScreeningRecordDO;
import cn.iocoder.yudao.module.childhealth.service.screening.ScreeningRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 筛查记录")
@RestController
@RequestMapping("/childhealth/screening-record")
@Validated
public class ScreeningRecordController {

    @Resource
    private ScreeningRecordService screeningRecordService;

    @PostMapping("/create")
    @Operation(summary = "创建筛查记录")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-record:create')")
    public CommonResult<Long> createScreeningRecord(@Valid @RequestBody ScreeningRecordSaveReqVO createReqVO) {
        return success(screeningRecordService.createScreeningRecord(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新筛查记录")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-record:update')")
    public CommonResult<Boolean> updateScreeningRecord(@Valid @RequestBody ScreeningRecordSaveReqVO updateReqVO) {
        screeningRecordService.updateScreeningRecord(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除筛查记录")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:screening-record:delete')")
    public CommonResult<Boolean> deleteScreeningRecord(@RequestParam("id") Long id) {
        screeningRecordService.deleteScreeningRecord(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得筛查记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-record:query')")
    public CommonResult<ScreeningRecordRespVO> getScreeningRecord(@RequestParam("id") Long id) {
        ScreeningRecordDO screeningRecord = screeningRecordService.getScreeningRecord(id);
        return success(BeanUtils.toBean(screeningRecord, ScreeningRecordRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得筛查记录分页")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-record:query')")
    public CommonResult<PageResult<ScreeningRecordRespVO>> getScreeningRecordPage(@Valid ScreeningRecordPageReqVO pageReqVO) {
        PageResult<ScreeningRecordDO> pageResult = screeningRecordService.getScreeningRecordPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ScreeningRecordRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得筛查记录列表")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-record:query')")
    public CommonResult<List<ScreeningRecordRespVO>> getScreeningRecordList(@Valid ScreeningRecordListReqVO listReqVO) {
        List<ScreeningRecordDO> list = screeningRecordService.getScreeningRecordList(listReqVO);
        return success(BeanUtils.toBean(list, ScreeningRecordRespVO.class));
    }

    @PutMapping("/submit-audit")
    @Operation(summary = "提交审核")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:screening-record:update')")
    public CommonResult<Boolean> submitAudit(@RequestParam("id") Long id) {
        screeningRecordService.submitAudit(id);
        return success(true);
    }

    @PutMapping("/audit")
    @Operation(summary = "审核筛查记录")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:screening-record:audit')")
    public CommonResult<Boolean> auditScreeningRecord(@RequestParam("id") Long id) {
        Long auditDoctor = SecurityFrameworkUtils.getLoginUserId();
        screeningRecordService.auditScreeningRecord(id, auditDoctor);
        return success(true);
    }

}
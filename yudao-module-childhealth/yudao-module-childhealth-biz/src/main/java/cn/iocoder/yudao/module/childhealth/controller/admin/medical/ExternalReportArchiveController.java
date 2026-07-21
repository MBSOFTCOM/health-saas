package cn.iocoder.yudao.module.childhealth.controller.admin.medical;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.medical.vo.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.medical.ExternalReportArchiveDO;
import cn.iocoder.yudao.module.childhealth.service.medical.ExternalReportArchiveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 外部报告归档")
@RestController
@RequestMapping("/childhealth/external-report-archive")
@Validated
public class ExternalReportArchiveController {

    @Resource
    private ExternalReportArchiveService externalReportArchiveService;

    @PostMapping("/create")
    @Operation(summary = "创建外部报告归档")
    @PreAuthorize("@ss.hasPermission('childhealth:external-report-archive:create')")
    public CommonResult<Long> createExternalReportArchive(@Valid @RequestBody ExternalReportArchiveSaveReqVO createReqVO) {
        return success(externalReportArchiveService.createExternalReportArchive(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新外部报告归档")
    @PreAuthorize("@ss.hasPermission('childhealth:external-report-archive:update')")
    public CommonResult<Boolean> updateExternalReportArchive(@Valid @RequestBody ExternalReportArchiveSaveReqVO updateReqVO) {
        externalReportArchiveService.updateExternalReportArchive(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除外部报告归档")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:external-report-archive:delete')")
    public CommonResult<Boolean> deleteExternalReportArchive(@RequestParam("id") Long id) {
        externalReportArchiveService.deleteExternalReportArchive(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得外部报告归档")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:external-report-archive:query')")
    public CommonResult<ExternalReportArchiveRespVO> getExternalReportArchive(@RequestParam("id") Long id) {
        ExternalReportArchiveDO externalReportArchive = externalReportArchiveService.getExternalReportArchive(id);
        return success(BeanUtils.toBean(externalReportArchive, ExternalReportArchiveRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得外部报告归档分页")
    @PreAuthorize("@ss.hasPermission('childhealth:external-report-archive:query')")
    public CommonResult<PageResult<ExternalReportArchiveRespVO>> getExternalReportArchivePage(@Valid ExternalReportArchivePageReqVO pageReqVO) {
        PageResult<ExternalReportArchiveDO> pageResult = externalReportArchiveService.getExternalReportArchivePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ExternalReportArchiveRespVO.class));
    }

}

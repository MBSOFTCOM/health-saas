package cn.iocoder.yudao.module.childhealth.controller.admin.medical;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.medical.vo.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.medical.LisPacsReportDO;
import cn.iocoder.yudao.module.childhealth.service.medical.LisPacsReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - LIS/PACS检验检查报告")
@RestController
@RequestMapping("/childhealth/lis-pacs-report")
@Validated
public class LisPacsReportController {

    @Resource
    private LisPacsReportService lisPacsReportService;

    @PostMapping("/create")
    @Operation(summary = "创建 LIS/PACS 报告")
    @PreAuthorize("@ss.hasPermission('childhealth:lis-pacs-report:create')")
    public CommonResult<Long> createLisPacsReport(@Valid @RequestBody LisPacsReportSaveReqVO createReqVO) {
        return success(lisPacsReportService.createLisPacsReport(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新 LIS/PACS 报告")
    @PreAuthorize("@ss.hasPermission('childhealth:lis-pacs-report:update')")
    public CommonResult<Boolean> updateLisPacsReport(@Valid @RequestBody LisPacsReportSaveReqVO updateReqVO) {
        lisPacsReportService.updateLisPacsReport(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除 LIS/PACS 报告")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:lis-pacs-report:delete')")
    public CommonResult<Boolean> deleteLisPacsReport(@RequestParam("id") Long id) {
        lisPacsReportService.deleteLisPacsReport(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得 LIS/PACS 报告")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:lis-pacs-report:query')")
    public CommonResult<LisPacsReportRespVO> getLisPacsReport(@RequestParam("id") Long id) {
        LisPacsReportDO lisPacsReport = lisPacsReportService.getLisPacsReport(id);
        return success(BeanUtils.toBean(lisPacsReport, LisPacsReportRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得 LIS/PACS 报告分页")
    @PreAuthorize("@ss.hasPermission('childhealth:lis-pacs-report:query')")
    public CommonResult<PageResult<LisPacsReportRespVO>> getLisPacsReportPage(@Valid LisPacsReportPageReqVO pageReqVO) {
        PageResult<LisPacsReportDO> pageResult = lisPacsReportService.getLisPacsReportPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, LisPacsReportRespVO.class));
    }

    @PostMapping("/fetch")
    @Operation(summary = "从外部系统抓取 LIS/PACS 报告")
    @PreAuthorize("@ss.hasPermission('childhealth:lis-pacs-report:create')")
    public CommonResult<LisPacsReportRespVO> fetchFromExternal(@RequestParam("reportNo") String reportNo,
                                                              @RequestParam("sourceSystem") String sourceSystem) {
        LisPacsReportDO report = lisPacsReportService.fetchFromExternal(reportNo, sourceSystem);
        return success(BeanUtils.toBean(report, LisPacsReportRespVO.class));
    }

    @PostMapping("/auto-fill")
    @Operation(summary = "自动填充 LIS/PACS 报告数据到病历")
    @PreAuthorize("@ss.hasPermission('childhealth:lis-pacs-report:update')")
    public CommonResult<Boolean> autoFillToMedicalRecord(@RequestParam("reportId") Long reportId,
                                                         @RequestParam("recordId") Long recordId) {
        return success(lisPacsReportService.autoFillToMedicalRecord(reportId, recordId));
    }

}

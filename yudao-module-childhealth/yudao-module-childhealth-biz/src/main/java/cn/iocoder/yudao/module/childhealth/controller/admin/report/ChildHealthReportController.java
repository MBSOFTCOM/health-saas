package cn.iocoder.yudao.module.childhealth.controller.admin.report;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.childhealth.api.report.dto.ChildHealthReportDTO.*;
import cn.iocoder.yudao.module.childhealth.service.report.ChildHealthReportService;
import cn.iocoder.yudao.module.childhealth.service.report.ChildHealthReportServiceImpl;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@RestController
@RequestMapping("/childhealth/report")
public class ChildHealthReportController {

    @Resource
    private ChildHealthReportService service;
    @Resource
    private ChildHealthReportServiceImpl serviceImpl;

    @GetMapping("/exam/{id}")
    @PreAuthorize("@ss.hasPermission('childhealth:report:query')")
    public CommonResult<Map<String, Object>> exam(@PathVariable Long id) {
        return success(service.exam(id));
    }

    @GetMapping("/screen/personal")
    @PreAuthorize("@ss.hasPermission('childhealth:report:query')")
    public CommonResult<Map<String, Object>> personal(@RequestParam Long recordId) {
        return success(service.personalScreen(recordId));
    }

    @GetMapping("/screen/school")
    @PreAuthorize("@ss.hasPermission('childhealth:report:query')")
    public CommonResult<Map<String, Object>> school(@RequestParam Long batchId) {
        return success(service.schoolScreen(batchId));
    }

    @GetMapping("/screen/grade")
    @PreAuthorize("@ss.hasPermission('childhealth:report:query')")
    public CommonResult<Map<String, Object>> grade(@RequestParam Long batchId, @RequestParam Long gradeId) {
        return success(service.gradeScreen(batchId, gradeId));
    }

    @GetMapping("/screen/region")
    @PreAuthorize("@ss.hasPermission('childhealth:report:query')")
    public CommonResult<List<Map<String, Object>>> region(@RequestParam(required = false) String regionCode) {
        return success(service.regionScreen(regionCode));
    }

    @PostMapping("/workload")
    @PreAuthorize("@ss.hasPermission('childhealth:report:query')")
    public CommonResult<Map<String, Object>> workload(@Valid @RequestBody StatisticsRequest request) {
        return success(service.workload(request));
    }

    @PostMapping("/statistics")
    @PreAuthorize("@ss.hasPermission('childhealth:report:query')")
    public CommonResult<Map<String, Object>> statistics(@Valid @RequestBody StatisticsRequest request) {
        return success(service.statistics(request));
    }

    @GetMapping("/growth-trend")
    @PreAuthorize("@ss.hasPermission('childhealth:report:query')")
    public CommonResult<List<Map<String, Object>>> growthTrend(@RequestParam Long childId) {
        return success(service.growthTrend(childId));
    }

    @GetMapping("/abnormality-distribution")
    @PreAuthorize("@ss.hasPermission('childhealth:report:query')")
    public CommonResult<Map<String, Object>> abnormalityDistribution() {
        return success(service.abnormalityDistribution());
    }

    @GetMapping("/screening-coverage")
    @PreAuthorize("@ss.hasPermission('childhealth:report:query')")
    public CommonResult<List<Map<String, Object>>> screeningCoverage(@RequestParam Long batchId) {
        return success(service.screeningCoverage(batchId));
    }

    @PostMapping("/export")
    @PreAuthorize("@ss.hasPermission('childhealth:report:export')")
    public ResponseEntity<byte[]> export(@Valid @RequestBody ExportRequest request) {
        String format = request.getFormat() == null ? "csv" : request.getFormat().toLowerCase();
        byte[] body = serviceImpl.exportWithFormat(request, format);
        String fileName = "child-health-statistics." +
                (("excel".equals(format) || "xlsx".equals(format)) ? "xlsx"
                        : ("pdf".equals(format) ? "txt" : "csv"));
        MediaType ct = ("excel".equals(format) || "xlsx".equals(format))
                ? MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8")
                : ("pdf".equals(format) ? MediaType.TEXT_PLAIN
                        : MediaType.parseMediaType("text/csv;charset=UTF-8"));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(ct)
                .body(body);
    }
}

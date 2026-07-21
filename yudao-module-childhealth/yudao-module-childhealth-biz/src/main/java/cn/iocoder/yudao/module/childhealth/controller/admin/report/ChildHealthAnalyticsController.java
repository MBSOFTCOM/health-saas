package cn.iocoder.yudao.module.childhealth.controller.admin.report;
import cn.iocoder.yudao.framework.common.pojo.CommonResult; import cn.iocoder.yudao.module.childhealth.service.report.ChildHealthReportService; import jakarta.annotation.Resource; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*; import java.util.*; import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
@RestController @RequestMapping("/childhealth/analytics") @PreAuthorize("@ss.hasPermission('childhealth:report:query')") public class ChildHealthAnalyticsController { @Resource private ChildHealthReportService service;
 @GetMapping("/growth-trend") public CommonResult<List<Map<String,Object>>> growth(@RequestParam Long childId){return success(service.growthTrend(childId));}
 @GetMapping("/abnormality-distribution") public CommonResult<Map<String,Object>> abnormality(){return success(service.abnormalityDistribution());}
 @GetMapping("/screening-coverage") public CommonResult<List<Map<String,Object>>> coverage(@RequestParam(required=false) Long batchId){return success(service.screeningCoverage(batchId));}
}

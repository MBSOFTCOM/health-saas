package cn.iocoder.yudao.module.childhealth.service.report;

import cn.iocoder.yudao.module.childhealth.api.report.dto.ChildHealthReportDTO.StatisticsRequest;
import java.util.List;
import java.util.Map;

public interface ChildHealthReportService {
    Map<String, Object> exam(Long id);

    Map<String, Object> personalScreen(Long recordId);

    Map<String, Object> schoolScreen(Long batchId);

    Map<String, Object> gradeScreen(Long batchId, Long gradeId);

    List<Map<String, Object>> regionScreen(String regionCode);

    Map<String, Object> workload(StatisticsRequest request);

    Map<String, Object> statistics(StatisticsRequest request);

    byte[] export(StatisticsRequest request);

    List<Map<String, Object>> growthTrend(Long childId);

    Map<String, Object> abnormalityDistribution();

    List<Map<String, Object>> screeningCoverage(Long batchId);
}

package cn.iocoder.yudao.module.childhealth.service.external.mock;

import cn.iocoder.yudao.module.childhealth.service.external.adapter.LisIntegrationAdapter;
import cn.iocoder.yudao.module.childhealth.service.external.dto.LisReportDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * LIS 检验系统对接 Mock 实现
 *
 * 提供常见儿童检验报告的模拟数据：
 * - 血常规（HGB/HCT/WBC/PLT 等）
 * - 贫血识别（HGB<110）
 * - 微量元素（铁/锌/钙）
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "childhealth.external.lis", name = "mode", havingValue = "mock", matchIfMissing = true)
public class MockLisIntegrationAdapter implements LisIntegrationAdapter {

    @Override
    public List<LisReportDTO> fetchReportsByPatient(String hisPatientId) {
        log.info("[MockLIS] fetchReportsByPatient hisPatientId={}", hisPatientId);
        List<LisReportDTO> list = new ArrayList<>();
        list.add(buildBloodRoutineReport(hisPatientId));
        list.add(buildTraceElementReport(hisPatientId));
        return list;
    }

    @Override
    public LisReportDTO fetchReportByNo(String reportNo) {
        log.info("[MockLIS] fetchReportByNo reportNo={}", reportNo);
        return buildBloodRoutineReport("MOCK_HIS_001");
    }

    @Override
    public List<LisReportDTO> fetchReportsByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        log.info("[MockLIS] fetchReportsByTimeRange {} ~ {}", startTime, endTime);
        return fetchReportsByPatient("MOCK_HIS_001");
    }

    @Override
    public List<LisReportDTO> fetchReportsByChildName(String childName, LocalDateTime startDate, LocalDateTime endDate) {
        log.info("[MockLIS] fetchReportsByChildName name={} {} ~ {}", childName, startDate, endDate);
        return fetchReportsByPatient("MOCK_HIS_001");
    }

    @Override
    public boolean checkConnection() {
        log.info("[MockLIS] checkConnection → true (mock)");
        return true;
    }

    /**
     * 构建血常规报告（含贫血识别）
     */
    private LisReportDTO buildBloodRoutineReport(String hisPatientId) {
        LisReportDTO report = new LisReportDTO();
        report.setLisReportNo("MOCK_LIS_001");
        report.setHisPatientId(hisPatientId);
        report.setChildName("张小明");
        report.setTestItemCode("CBC");
        report.setTestItemName("血常规");
        report.setSpecimenType("静脉血");
        report.setTestDoctor("陈检验师");
        report.setTestTime(LocalDateTime.now().minusDays(1));
        report.setReportTime(LocalDateTime.now().minusHours(2));
        report.setSourceSystem("MOCK_LIS");

        List<LisReportDTO.TestResultItem> items = new ArrayList<>();
        items.add(buildItem("HGB", "血红蛋白", "108", "g/L", "110-160", 2, true));  // 偏低+危急值（贫血）
        items.add(buildItem("WBC", "白细胞计数", "8.5", "10^9/L", "5.0-12.0", 0, false));
        items.add(buildItem("RBC", "红细胞计数", "4.2", "10^12/L", "4.0-5.5", 0, false));
        items.add(buildItem("PLT", "血小板计数", "250", "10^9/L", "100-300", 0, false));
        items.add(buildItem("HCT", "红细胞压积", "0.35", "L/L", "0.36-0.50", 2, false));
        items.add(buildItem("MCV", "平均红细胞体积", "78", "fL", "80-100", 2, false));
        items.add(buildItem("MCH", "平均红细胞血红蛋白", "25", "pg", "27-34", 2, false));
        report.setResultItems(items);
        return report;
    }

    /**
     * 构建微量元素报告
     */
    private LisReportDTO buildTraceElementReport(String hisPatientId) {
        LisReportDTO report = new LisReportDTO();
        report.setLisReportNo("MOCK_LIS_002");
        report.setHisPatientId(hisPatientId);
        report.setChildName("张小明");
        report.setTestItemCode("TRACE");
        report.setTestItemName("微量元素六项");
        report.setSpecimenType("静脉血");
        report.setTestDoctor("陈检验师");
        report.setTestTime(LocalDateTime.now().minusDays(7));
        report.setReportTime(LocalDateTime.now().minusDays(7).plusHours(2));
        report.setSourceSystem("MOCK_LIS");

        List<LisReportDTO.TestResultItem> items = new ArrayList<>();
        items.add(buildItem("FE", "铁", "7.2", "mmol/L", "7.5-11.0", 2, false));
        items.add(buildItem("ZN", "锌", "65.5", "μmol/L", "76.5-170.0", 2, false));
        items.add(buildItem("CA", "钙", "1.55", "mmol/L", "1.55-2.10", 0, false));
        items.add(buildItem("CU", "铜", "12.6", "μmol/L", "11.8-39.3", 0, false));
        items.add(buildItem("MG", "镁", "1.50", "mmol/L", "1.12-1.49", 1, false));
        items.add(buildItem("PB", "铅", "<100", "μg/L", "<100", 0, false));
        report.setResultItems(items);
        return report;
    }

    private LisReportDTO.TestResultItem buildItem(String code, String name, String value,
                                                   String unit, String ref, Integer abnormal, Boolean critical) {
        LisReportDTO.TestResultItem item = new LisReportDTO.TestResultItem();
        item.setItemCode(code);
        item.setItemName(name);
        item.setResultValue(value);
        item.setUnit(unit);
        item.setReferenceRange(ref);
        item.setAbnormalFlag(abnormal);
        item.setCriticalFlag(critical);
        return item;
    }

}

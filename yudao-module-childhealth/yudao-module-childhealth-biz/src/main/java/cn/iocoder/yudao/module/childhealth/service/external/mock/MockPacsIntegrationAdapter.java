package cn.iocoder.yudao.module.childhealth.service.external.mock;

import cn.iocoder.yudao.module.childhealth.service.external.adapter.PacsIntegrationAdapter;
import cn.iocoder.yudao.module.childhealth.service.external.dto.PacsReportDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * PACS 影像系统对接 Mock 实现
 *
 * 提供常见儿童影像检查的模拟数据：
 * - 胸部 X 线（肺炎/佝偻病筛查）
 * - 髋关节 B 超（发育性髋关节脱位筛查）
 * - 头部 CT/MRI（HIE 随访）
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "childhealth.external.pacs", name = "mode", havingValue = "mock", matchIfMissing = true)
public class MockPacsIntegrationAdapter implements PacsIntegrationAdapter {

    @Override
    public List<PacsReportDTO> fetchReportsByPatient(String hisPatientId) {
        log.info("[MockPACS] fetchReportsByPatient hisPatientId={}", hisPatientId);
        List<PacsReportDTO> list = new ArrayList<>();
        list.add(buildChestXray(hisPatientId));
        list.add(buildHipUltrasound(hisPatientId));
        return list;
    }

    @Override
    public PacsReportDTO fetchReportByNo(String reportNo) {
        log.info("[MockPACS] fetchReportByNo reportNo={}", reportNo);
        return buildChestXray("MOCK_HIS_001");
    }

    @Override
    public List<PacsReportDTO> fetchReportsByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        log.info("[MockPACS] fetchReportsByTimeRange {} ~ {}", startTime, endTime);
        return fetchReportsByPatient("MOCK_HIS_001");
    }

    @Override
    public List<PacsReportDTO> fetchReportsByChildNameAndPart(String childName, String examPart) {
        log.info("[MockPACS] fetchReportsByChildNameAndPart name={} part={}", childName, examPart);
        return fetchReportsByPatient("MOCK_HIS_001");
    }

    @Override
    public boolean checkConnection() {
        log.info("[MockPACS] checkConnection → true (mock)");
        return true;
    }

    /**
     * 胸部 X 线报告（佝偻病筛查）
     */
    private PacsReportDTO buildChestXray(String hisPatientId) {
        PacsReportDTO report = new PacsReportDTO();
        report.setPacsReportNo("MOCK_PACS_001");
        report.setHisPatientId(hisPatientId);
        report.setChildName("张小明");
        report.setExamPart("胸部");
        report.setModality("X-Ray");
        report.setImagingFindings("双肺纹理增多，未见明显实变影；肋骨前端膨隆，可见串珠样改变，符合佝偻病活动期征象。");
        report.setDiagnosticOpinion("符合活动期佝偻病X线征象");
        report.setPositiveFindings(Arrays.asList("肋骨串珠", "肋骨膨隆"));
        report.setIsPositive(true);
        report.setExamDoctor("王放射");
        report.setReportDoctor("李放射");
        report.setExamTime(LocalDateTime.now().minusDays(3));
        report.setReportTime(LocalDateTime.now().minusDays(3).plusHours(1));
        report.setImageUrlList(Arrays.asList(
                "https://mock.pacs.local/chest_xray_001.jpg",
                "https://mock.pacs.local/chest_xray_002.jpg"));
        report.setSourceSystem("MOCK_PACS");
        return report;
    }

    /**
     * 髋关节 B 超报告（DDH 筛查）
     */
    private PacsReportDTO buildHipUltrasound(String hisPatientId) {
        PacsReportDTO report = new PacsReportDTO();
        report.setPacsReportNo("MOCK_PACS_002");
        report.setHisPatientId(hisPatientId);
        report.setChildName("张小明");
        report.setExamPart("髋关节");
        report.setModality("B超");
        report.setImagingFindings("双侧髋关节 Graf α角 60°，β角 55°，髋关节形态正常，未见脱位征象。");
        report.setDiagnosticOpinion("双侧髋关节超声未见明显异常（Graf I 型）");
        report.setPositiveFindings(new ArrayList<>());
        report.setIsPositive(false);
        report.setExamDoctor("王超声");
        report.setReportDoctor("李超声");
        report.setExamTime(LocalDateTime.now().minusDays(7));
        report.setReportTime(LocalDateTime.now().minusDays(7).plusHours(1));
        report.setImageUrlList(Arrays.asList("https://mock.pacs.local/hip_us_001.jpg"));
        report.setSourceSystem("MOCK_PACS");
        return report;
    }

}

package cn.iocoder.yudao.module.childhealth.service.external.mock;

import cn.iocoder.yudao.module.childhealth.service.external.adapter.HisIntegrationAdapter;
import cn.iocoder.yudao.module.childhealth.service.external.dto.HisChildInfoDTO;
import cn.iocoder.yudao.module.childhealth.service.external.dto.HisNeonatalDiagnosisDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * HIS 系统对接 Mock 实现
 *
 * 开发期使用，提供模拟数据用于联调。
 * 部署时通过配置 childhealth.external.mode=real 切换到真实实现。
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "childhealth.external.his", name = "mode", havingValue = "mock", matchIfMissing = true)
public class MockHisIntegrationAdapter implements HisIntegrationAdapter {

    @Override
    public HisChildInfoDTO fetchChildInfo(String hisPatientId, String medicareCardNo) {
        log.info("[MockHIS] fetchChildInfo hisPatientId={} medicareCardNo={}", hisPatientId, medicareCardNo);
        HisChildInfoDTO dto = new HisChildInfoDTO();
        dto.setHisPatientId(hisPatientId != null ? hisPatientId : "MOCK_HIS_001");
        dto.setMedicareCardNo(medicareCardNo != null ? medicareCardNo : "MOCK_MED_001");
        dto.setChildName("张小明");
        dto.setGender(1);
        dto.setBirthDate(LocalDate.now().minusMonths(6));
        dto.setBirthWeight(3200);
        dto.setBirthLength(50.0);
        dto.setGestationalAge(39);
        dto.setApgarScore1Min(10);
        dto.setMotherName("张母");
        dto.setMotherIdCard("110101199001011234");
        dto.setMotherPhone("13800138000");
        dto.setFatherName("张父");
        dto.setResidenceAddress("北京市东城区测试街道100号");
        dto.setHisRecordTime(LocalDateTime.now());
        return dto;
    }

    @Override
    public List<HisChildInfoDTO> fetchChildrenByMotherIdCard(String motherIdCard) {
        log.info("[MockHIS] fetchChildrenByMotherIdCard motherIdCard={}", motherIdCard);
        List<HisChildInfoDTO> list = new ArrayList<>();
        list.add(fetchChildInfo(null, null));
        HisChildInfoDTO dto2 = new HisChildInfoDTO();
        dto2.setHisPatientId("MOCK_HIS_002");
        dto2.setChildName("张小红");
        dto2.setGender(2);
        dto2.setBirthDate(LocalDate.now().minusMonths(2));
        dto2.setBirthWeight(2900);
        dto2.setBirthLength(48.0);
        dto2.setGestationalAge(38);
        dto2.setApgarScore1Min(9);
        dto2.setMotherName("张母");
        dto2.setMotherIdCard(motherIdCard);
        list.add(dto2);
        return list;
    }

    @Override
    public List<HisNeonatalDiagnosisDTO> fetchNeonatalDiagnoses(String hisPatientId) {
        log.info("[MockHIS] fetchNeonatalDiagnoses hisPatientId={}", hisPatientId);
        List<HisNeonatalDiagnosisDTO> list = new ArrayList<>();
        HisNeonatalDiagnosisDTO d1 = new HisNeonatalDiagnosisDTO();
        d1.setHisDiagnosisId("MOCK_DIA_001");
        d1.setHisPatientId(hisPatientId);
        d1.setDiagnosisCode("P07.3");
        d1.setDiagnosisName("早产儿（胎龄<37周）");
        d1.setDiagnosisType("出院主要诊断");
        d1.setAdmissionDate(LocalDate.now().minusMonths(6));
        d1.setDischargeDate(LocalDate.now().minusMonths(6).plusDays(15));
        d1.setDiagnosisDoctor("王医生");
        d1.setSeverityLevel(2);
        d1.setIsHighRisk(true);
        list.add(d1);
        return list;
    }

    @Override
    public List<HisNeonatalDiagnosisDTO> fetchNeonatalDiagnosesByMother(String motherIdCard) {
        log.info("[MockHIS] fetchNeonatalDiagnosesByMother motherIdCard={}", motherIdCard);
        return fetchNeonatalDiagnoses("MOCK_HIS_001");
    }

    @Override
    public boolean checkConnection() {
        log.info("[MockHIS] checkConnection → true (mock)");
        return true;
    }

}

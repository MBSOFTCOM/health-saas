package cn.iocoder.yudao.module.childhealth.service.external.mock;

import cn.iocoder.yudao.module.childhealth.service.external.adapter.ObstetricsIntegrationAdapter;
import cn.iocoder.yudao.module.childhealth.service.external.dto.ObstetricsHighRiskNewbornDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 产科系统对接 Mock 实现
 *
 * 提供高危新生儿列表的模拟数据，覆盖 5 类高危类型：
 * - PRETERM（早产儿）
 * - LOW_WEIGHT（低体重儿）
 * - ASPHYXIA（新生儿窒息）
 * - HYPERBILIRUBINEMIA（高胆红素血症）
 * - HIE（新生儿缺氧缺血性脑病）
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "childhealth.external.obstetrics", name = "mode", havingValue = "mock", matchIfMissing = true)
public class MockObstetricsIntegrationAdapter implements ObstetricsIntegrationAdapter {

    private static final List<String> HIGH_RISK_TYPES = Arrays.asList(
            "PRETERM", "LOW_WEIGHT", "ASPHYXIA", "HYPERBILIRUBINEMIA", "HIE", "INHERITED_METABOLIC");

    @Override
    public List<ObstetricsHighRiskNewbornDTO> fetchHighRiskNewbornList(LocalDate startDate, LocalDate endDate) {
        log.info("[MockObstetrics] fetchHighRiskNewbornList {} ~ {}", startDate, endDate);
        List<ObstetricsHighRiskNewbornDTO> list = new ArrayList<>();
        list.add(buildMock("MOCK_OBS_001", "李一", 1, LocalDate.now().minusDays(30),
                2200, 45.0, 34, "PRETERM", 3, "剖宫产", false));
        list.add(buildMock("MOCK_OBS_002", "李二", 2, LocalDate.now().minusDays(20),
                2400, 46.0, 36, "LOW_WEIGHT", 2, "自然分娩", false));
        list.add(buildMock("MOCK_OBS_003", "李三", 1, LocalDate.now().minusDays(15),
                3500, 51.0, 40, "ASPHYXIA", 3, "剖宫产", true));
        return list;
    }

    @Override
    public List<ObstetricsHighRiskNewbornDTO> fetchByHighRiskType(String highRiskType,
                                                                    LocalDate startDate, LocalDate endDate) {
        log.info("[MockObstetrics] fetchByHighRiskType type={} {} ~ {}", highRiskType, startDate, endDate);
        List<ObstetricsHighRiskNewbornDTO> filtered = new ArrayList<>();
        for (ObstetricsHighRiskNewbornDTO dto : fetchHighRiskNewbornList(startDate, endDate)) {
            if (highRiskType.equals(dto.getHighRiskType())) {
                filtered.add(dto);
            }
        }
        return filtered;
    }

    @Override
    public List<ObstetricsHighRiskNewbornDTO> fetchByMotherIdCard(String motherIdCard) {
        log.info("[MockObstetrics] fetchByMotherIdCard motherIdCard={}", motherIdCard);
        return fetchHighRiskNewbornList(LocalDate.now().minusDays(60), LocalDate.now());
    }

    @Override
    public ObstetricsHighRiskNewbornDTO fetchByObstetricsId(String obstetricsNewbornId) {
        log.info("[MockObstetrics] fetchByObstetricsId id={}", obstetricsNewbornId);
        return buildMock(obstetricsNewbornId, "李查询", 1, LocalDate.now().minusDays(10),
                2300, 45.5, 35, "PRETERM", 3, "剖宫产", false);
    }

    @Override
    public boolean checkConnection() {
        log.info("[MockObstetrics] checkConnection → true (mock)");
        return true;
    }

    private ObstetricsHighRiskNewbornDTO buildMock(String obsId, String name, Integer gender, LocalDate birthDate,
                                                    Integer weight, Double length, Integer ga, String type,
                                                    Integer level, String delivery, Boolean hasRecord) {
        ObstetricsHighRiskNewbornDTO dto = new ObstetricsHighRiskNewbornDTO();
        dto.setObstetricsNewbornId(obsId);
        dto.setMotherName("李母");
        dto.setMotherIdCard("110101199002021234");
        dto.setMotherPhone("13900139000");
        dto.setNewbornName(name);
        dto.setGender(gender);
        dto.setBirthDate(birthDate);
        dto.setBirthWeight(weight);
        dto.setBirthLength(length);
        dto.setGestationalAge(ga);
        dto.setApgarScore(type.equals("ASPHYXIA") ? 5 : 9);
        dto.setDeliveryMethod(delivery);
        dto.setHighRiskType(type);
        dto.setRiskLevel(level);
        dto.setHasChildHealthRecord(hasRecord);
        dto.setDischargeDate(birthDate.plusDays(7));
        dto.setObstetricsDoctor("刘医生");
        return dto;
    }

}

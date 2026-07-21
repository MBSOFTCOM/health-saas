package cn.iocoder.yudao.module.childhealth.service.vaccine;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.vaccine.VaccinePlanDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.vaccine.VaccineRecordDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.vaccine.VaccinePlanMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.vaccine.VaccineRecordMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 疫苗接种计划 Service 实现类
 */
@Service
@Validated
public class VaccinePlanServiceImpl implements VaccinePlanService {

    private static final ErrorCode VACCINE_PLAN_NOT_EXISTS = new ErrorCode(100117900, "疫苗接种计划不存在");

    @Resource
    private VaccinePlanMapper vaccinePlanMapper;

    @Resource
    private VaccineRecordMapper vaccineRecordMapper;

    @Override
    public PageResult<VaccinePlanDO> getVaccinePlanPage(PageParam pageParam, Long childId, String vaccineName, String status) {
        return vaccinePlanMapper.selectPage(pageParam, childId, vaccineName, status);
    }

    @Override
    public VaccinePlanDO getVaccinePlan(Long id) {
        return vaccinePlanMapper.selectById(id);
    }

    @Override
    public Long createVaccinePlan(VaccinePlanDO plan) {
        vaccinePlanMapper.insert(plan);
        return plan.getId();
    }

    @Override
    public void updateVaccinePlan(VaccinePlanDO plan) {
        validateVaccinePlanExists(plan.getId());
        vaccinePlanMapper.updateById(plan);
    }

    @Override
    public void deleteVaccinePlan(Long id) {
        validateVaccinePlanExists(id);
        vaccinePlanMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int generatePlanByChild(Long childId) {
        if (childId == null) {
            throw exception(VACCINE_PLAN_NOT_EXISTS);
        }
        int generated = 0;
        // 国家免疫规划疫苗程序（0-6岁）
        // 二维数组：{疫苗编码, 疫苗名称, 剂次, 起种月龄, 截止月龄, 与上剂间隔天}
        Object[][] programs = {
                {"HEPB", "乙肝疫苗", 1, 0, 0, 0},
                {"BCG", "卡介苗", 1, 0, 12, 0},
                {"HEPB", "乙肝疫苗", 2, 1, 12, 30},
                {"OPV", "脊灰疫苗", 1, 2, 12, 0},
                {"HEPB", "乙肝疫苗", 3, 6, 18, 150},
                {"OPV", "脊灰疫苗", 2, 3, 12, 28},
                {"DTP", "百白破疫苗", 1, 3, 12, 0},
                {"OPV", "脊灰疫苗", 3, 4, 12, 28},
                {"DTP", "百白破疫苗", 2, 4, 12, 28},
                {"DTP", "百白破疫苗", 3, 5, 12, 28},
                {"MR", "麻风疫苗", 1, 8, 18, 0},
                {"JE_LIVE", "乙脑减毒活疫苗", 1, 8, 18, 0},
                {"MEN_A", "A群流脑疫苗", 1, 6, 12, 0},
                {"MEN_A", "A群流脑疫苗", 2, 9, 18, 84},
                {"DTP", "百白破疫苗", 4, 18, 24, 0},
                {"HEP_A_LIVE", "甲肝减毒活疫苗", 1, 18, 24, 0},
                {"JE_LIVE", "乙脑减毒活疫苗", 2, 24, 36, 700},
                {"MEN_AC", "A+C群流脑疫苗", 1, 36, 48, 0},
                {"MR", "麻腮风疫苗", 2, 18, 24, 0},
                {"MEN_AC", "A+C群流脑疫苗", 2, 72, 84, 1095}
        };
        LocalDate today = LocalDate.now();
        for (Object[] p : programs) {
            String code = (String) p[0];
            String name = (String) p[1];
            Integer doseNo = (Integer) p[2];
            Integer startMonth = (Integer) p[3];
            Integer endMonth = (Integer) p[4];
            Integer intervalDays = (Integer) p[5];

            // 去重：已有同剂次计划则跳过
            VaccinePlanDO exists = vaccinePlanMapper.selectByChildIdAndVaccineCode(childId, code, doseNo);
            if (exists != null) {
                continue;
            }

            VaccinePlanDO plan = new VaccinePlanDO();
            plan.setChildId(childId);
            plan.setVaccineName(name);
            plan.setVaccineCode(code);
            plan.setVaccineType("一类");
            plan.setDoseNo(doseNo);
            plan.setTotalDoses(getTotalDoses(code));
            plan.setStartAgeMonth(startMonth);
            plan.setEndAgeMonth(endMonth);
            plan.setIntervalDays(intervalDays);
            plan.setScheduledDate(today.plusMonths(startMonth));
            plan.setStatus("PENDING");
            plan.setReminderStatus("NOT_SENT");
            plan.setParentConfirmed(false);

            vaccinePlanMapper.insert(plan);
            generated++;
        }
        return generated;
    }

    @Override
    public List<VaccinePlanDO> getUpcomingPlans(int days) {
        LocalDate today = LocalDate.now();
        LocalDate end = today.plusDays(days);
        return vaccinePlanMapper.selectListByScheduledDateRange(today, end);
    }

    @Override
    public boolean sendReminder(Long planId) {
        VaccinePlanDO plan = vaccinePlanMapper.selectById(planId);
        if (plan == null) {
            throw exception(VACCINE_PLAN_NOT_EXISTS);
        }
        // 标记提醒已发送（实际推送由 message 模块异步处理）
        plan.setReminderStatus("SENT");
        plan.setReminderDate(LocalDate.now());
        vaccinePlanMapper.updateById(plan);
        return true;
    }

    /**
     * 获取疫苗总剂次
     */
    private Integer getTotalDoses(String code) {
        switch (code) {
            case "HEPB": return 3;
            case "BCG": return 1;
            case "OPV": return 3;
            case "DTP": return 4;
            case "MR": return 2;
            case "JE_LIVE": return 2;
            case "MEN_A": return 2;
            case "MEN_AC": return 2;
            case "HEP_A_LIVE": return 1;
            default: return 1;
        }
    }

    private void validateVaccinePlanExists(Long id) {
        if (id == null || vaccinePlanMapper.selectById(id) == null) {
            throw exception(VACCINE_PLAN_NOT_EXISTS);
        }
    }
}

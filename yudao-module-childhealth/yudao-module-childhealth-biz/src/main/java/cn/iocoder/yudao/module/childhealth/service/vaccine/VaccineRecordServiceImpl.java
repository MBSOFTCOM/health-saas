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

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 疫苗接种记录 Service 实现类
 */
@Service
@Validated
public class VaccineRecordServiceImpl implements VaccineRecordService {

    private static final ErrorCode VACCINE_RECORD_NOT_EXISTS = new ErrorCode(100117910, "疫苗接种记录不存在");
    private static final ErrorCode VACCINE_PLAN_NOT_EXISTS = new ErrorCode(100117911, "关联的接种计划不存在");

    @Resource
    private VaccineRecordMapper vaccineRecordMapper;

    @Resource
    private VaccinePlanMapper vaccinePlanMapper;

    @Override
    public PageResult<VaccineRecordDO> getVaccineRecordPage(PageParam pageParam, Long childId, String vaccineName, String status) {
        return vaccineRecordMapper.selectPage(pageParam, childId, vaccineName, status);
    }

    @Override
    public VaccineRecordDO getVaccineRecord(Long id) {
        return vaccineRecordMapper.selectById(id);
    }

    @Override
    public Long createVaccineRecord(VaccineRecordDO record) {
        vaccineRecordMapper.insert(record);
        return record.getId();
    }

    @Override
    public void updateVaccineRecord(VaccineRecordDO record) {
        validateVaccineRecordExists(record.getId());
        vaccineRecordMapper.updateById(record);
    }

    @Override
    public void deleteVaccineRecord(Long id) {
        validateVaccineRecordExists(id);
        vaccineRecordMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long confirmInoculation(VaccineRecordDO record) {
        // 1. 插入接种记录
        if (record.getStatus() == null || record.getStatus().isEmpty()) {
            record.setStatus("COMPLETED");
        }
        if (record.getReactionLevel() == null) {
            record.setReactionLevel("NONE");
        }
        vaccineRecordMapper.insert(record);

        // 2. 同步更新关联计划状态为 COMPLETED
        Long planId = record.getVaccinePlanId();
        if (planId != null) {
            VaccinePlanDO plan = vaccinePlanMapper.selectById(planId);
            if (plan != null) {
                plan.setStatus("COMPLETED");
                plan.setReminderStatus("CONFIRMED");
                vaccinePlanMapper.updateById(plan);
            }
        }
        return record.getId();
    }

    private void validateVaccineRecordExists(Long id) {
        if (id == null || vaccineRecordMapper.selectById(id) == null) {
            throw exception(VACCINE_RECORD_NOT_EXISTS);
        }
    }
}

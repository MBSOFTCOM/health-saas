package cn.iocoder.yudao.module.childhealth.service.medical.impl;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.exam.EyeExamRecordDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.exam.HearingExamRecordDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.exam.LabReportDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.exam.OralExamRecordDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.exam.PhysicalExamRecordDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.medical.MedicalRecordDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.workflow.HealthCheckupDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.exam.EyeExamRecordMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.exam.HearingExamRecordMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.exam.LabReportMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.exam.OralExamRecordMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.exam.PhysicalExamRecordMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.medical.MedicalRecordMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.workflow.HealthCheckupMapper;
import cn.iocoder.yudao.module.childhealth.service.medical.MedicalRecordService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.MEDICAL_RECORD_NO_DUPLICATE;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.MEDICAL_RECORD_NOT_EXISTS;

/**
 * 病历主表 Service 实现类
 *
 * 模块: A. 儿童基础健康检查（A2-病历主表）
 * 创建日期: 2026-07-20
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class MedicalRecordServiceImpl implements MedicalRecordService {

    @Resource
    private MedicalRecordMapper medicalRecordMapper;
    @Resource
    private HealthCheckupMapper healthCheckupMapper;
    @Resource
    private PhysicalExamRecordMapper physicalExamRecordMapper;
    @Resource
    private EyeExamRecordMapper eyeExamRecordMapper;
    @Resource
    private HearingExamRecordMapper hearingExamRecordMapper;
    @Resource
    private OralExamRecordMapper oralExamRecordMapper;
    @Resource
    private LabReportMapper labReportMapper;

    @Override
    public Long createMedicalRecord(Object saveReqVO) {
        // TODO 后续替换为 MedicalRecordSaveReqVO
        MedicalRecordDO record = BeanUtils.toBean(saveReqVO, MedicalRecordDO.class);
        // 病历号唯一性校验
        if (record.getRecordNo() != null
                && medicalRecordMapper.selectByRecordNo(record.getRecordNo()) != null) {
            throw exception(MEDICAL_RECORD_NO_DUPLICATE);
        }
        medicalRecordMapper.insert(record);
        return record.getId();
    }

    @Override
    public void updateMedicalRecord(Object saveReqVO) {
        // TODO 后续替换为 MedicalRecordSaveReqVO
        MedicalRecordDO updateObj = BeanUtils.toBean(saveReqVO, MedicalRecordDO.class);
        validateMedicalRecordExists(updateObj.getId());
        medicalRecordMapper.updateById(updateObj);
    }

    @Override
    public void deleteMedicalRecord(Long id) {
        validateMedicalRecordExists(id);
        medicalRecordMapper.deleteById(id);
    }

    @Override
    public MedicalRecordDO getMedicalRecord(Long id) {
        return medicalRecordMapper.selectById(id);
    }

    @Override
    public PageResult<MedicalRecordDO> getMedicalRecordPage(PageParam pageParam) {
        // TODO 后续替换为 MedicalRecordPageReqVO，并增加查询条件
        return medicalRecordMapper.selectPage(pageParam, null);
    }

    @Override
    public MedicalRecordDO selectByRecordNo(String recordNo) {
        return medicalRecordMapper.selectByRecordNo(recordNo);
    }

    @Override
    public void autoFillFromTemplate(Long recordId, Long templateId) {
        // TODO 实现一键填充：根据模板 defaultValuesJson 自动填充病历各字段值，并写入 medical_record_item 表
        log.info("[autoFillFromTemplate] 一键填充病历字段 recordId={}, templateId={}", recordId, templateId);
    }

    @Override
    @Transactional
    public void aggregateToMedicalRecord(Long childId, LocalDate visitDate) {
        if (childId == null) {
            log.warn("[aggregateToMedicalRecord] childId 为空，跳过归集");
            return;
        }
        if (visitDate == null) {
            log.warn("[aggregateToMedicalRecord] visitDate 为空，跳过归集 childId={}", childId);
            return;
        }
        // 1. 查询当日该儿童的所有体检记录（HealthCheckupDO 存储在 exam_record 表）
        List<HealthCheckupDO> checkups = healthCheckupMapper.selectList(
                Wrappers.<HealthCheckupDO>lambdaQuery()
                        .eq(HealthCheckupDO::getChildId, childId)
                        .eq(HealthCheckupDO::getCheckupDate, visitDate)
                        .orderByDesc(HealthCheckupDO::getCreatedAt));
        if (checkups.isEmpty()) {
            log.info("[aggregateToMedicalRecord] 当日无体检记录 childId={}, visitDate={}", childId, visitDate);
            return;
        }
        // 2. 收集当日所有体检相关的明细数据
        List<PhysicalExamRecordDO> physicals = new ArrayList<>();
        List<EyeExamRecordDO> eyes = new ArrayList<>();
        List<HearingExamRecordDO> hearings = new ArrayList<>();
        List<OralExamRecordDO> orals = new ArrayList<>();
        List<LabReportDO> labReports = new ArrayList<>();
        List<Long> examIds = new ArrayList<>();
        Integer ageMonth = null;
        Long doctorId = null;
        boolean anyAbnormal = false;
        for (HealthCheckupDO checkup : checkups) {
            examIds.add(checkup.getId());
            if (ageMonth == null && checkup.getAgeMonths() != null) ageMonth = checkup.getAgeMonths();
            if (doctorId == null && checkup.getDoctorId() != null) doctorId = checkup.getDoctorId();
            if (Boolean.TRUE.equals(checkup.getIsAbnormal())) anyAbnormal = true;
        }
        if (!examIds.isEmpty()) {
            physicals.addAll(physicalExamRecordMapper.selectList(
                    Wrappers.<PhysicalExamRecordDO>lambdaQuery().in(PhysicalExamRecordDO::getExamId, examIds)));
            eyes.addAll(eyeExamRecordMapper.selectList(
                    Wrappers.<EyeExamRecordDO>lambdaQuery().in(EyeExamRecordDO::getExamId, examIds)));
            hearings.addAll(hearingExamRecordMapper.selectList(
                    Wrappers.<HearingExamRecordDO>lambdaQuery().in(HearingExamRecordDO::getExamId, examIds)));
            orals.addAll(oralExamRecordMapper.selectList(
                    Wrappers.<OralExamRecordDO>lambdaQuery().in(OralExamRecordDO::getExamId, examIds)));
            labReports.addAll(labReportMapper.selectList(
                    Wrappers.<LabReportDO>lambdaQuery().in(LabReportDO::getExamId, examIds)
                            .orderByAsc(LabReportDO::getReportDate)));
        }
        // 3. 查找或创建当日病历主表
        MedicalRecordDO record = medicalRecordMapper.selectOne(
                Wrappers.<MedicalRecordDO>lambdaQuery()
                        .eq(MedicalRecordDO::getChildId, childId)
                        .eq(MedicalRecordDO::getVisitDate, visitDate)
                        .last("LIMIT 1"));
        boolean isNew = record == null;
        if (isNew) {
            record = new MedicalRecordDO();
            record.setChildId(childId);
            record.setVisitDate(visitDate);
            record.setRecordNo("MR-" + childId + "-" + visitDate.toString().replace("-", ""));
            record.setCaseRemindStatus(0);
            record.setAuditStatus(0);
        }
        // 4. 填充基本字段（仅当原值为空时，避免覆盖医生手填内容）
        if (record.getAgeMonth() == null) record.setAgeMonth(ageMonth);
        if (record.getDoctorId() == null) record.setDoctorId(doctorId);
        if (record.getVisitType() == null) {
            // 默认按当日体检类型推断：入园体检优先，否则常规体检
            record.setVisitType(checkups.stream()
                    .map(HealthCheckupDO::getCheckupType)
                    .filter("2"::equals)
                    .findFirst()
                    .map(t -> 3)
                    .orElse(2));
        }
        // 5. 汇总体格检查数据 → physicalExam 字段（JSON）
        if (!physicals.isEmpty()) {
            Map<String, Object> physicalMap = new LinkedHashMap<>();
            physicalMap.put("examCount", physicals.size());
            physicalMap.put("records", physicals);
            // 汇总异常标记
            physicalMap.put("hasAbnormal", physicals.stream().anyMatch(p -> Boolean.TRUE.equals(p.getIsAbnormal())));
            record.setPhysicalExam(JsonUtils.toJsonString(physicalMap));
        }
        // 6. 汇总专科检查（眼/听力/口腔）→ specialistExam 字段（JSON）
        Map<String, Object> specialtyMap = new LinkedHashMap<>();
        if (!eyes.isEmpty()) {
            Map<String, Object> eyeMap = new LinkedHashMap<>();
            eyeMap.put("count", eyes.size());
            eyeMap.put("records", eyes);
            eyeMap.put("hasAbnormal", eyes.stream().anyMatch(e -> Boolean.TRUE.equals(e.getIsAbnormal())));
            specialtyMap.put("eye", eyeMap);
        }
        if (!hearings.isEmpty()) {
            Map<String, Object> hearingMap = new LinkedHashMap<>();
            hearingMap.put("count", hearings.size());
            hearingMap.put("records", hearings);
            hearingMap.put("hasAbnormal", hearings.stream().anyMatch(h -> Boolean.TRUE.equals(h.getIsAbnormal())));
            specialtyMap.put("hearing", hearingMap);
        }
        if (!orals.isEmpty()) {
            Map<String, Object> oralMap = new LinkedHashMap<>();
            oralMap.put("count", orals.size());
            oralMap.put("records", orals);
            oralMap.put("hasAbnormal", orals.stream().anyMatch(o -> Boolean.TRUE.equals(o.getIsAbnormal())));
            specialtyMap.put("oral", oralMap);
        }
        if (!specialtyMap.isEmpty()) {
            record.setSpecialistExam(JsonUtils.toJsonString(specialtyMap));
        }
        // 7. 汇总辅助检查（LIS/PACS/手工录入）→ auxExam 字段（JSON）
        if (!labReports.isEmpty()) {
            Map<String, Object> labMap = new LinkedHashMap<>();
            labMap.put("count", labReports.size());
            labMap.put("records", labReports);
            // 按来源分组统计
            Map<String, List<LabReportDO>> bySource = labReports.stream()
                    .collect(Collectors.groupingBy(r -> r.getSourceSystem() == null ? "LOCAL" : r.getSourceSystem()));
            labMap.put("bySource", bySource.keySet());
            labMap.put("hasAbnormal", labReports.stream().anyMatch(l -> Boolean.TRUE.equals(l.getIsAbnormal())));
            record.setAuxExam(JsonUtils.toJsonString(labMap));
        }
        // 8. 汇总诊断 → diagnosis 字段（拼接文本，仅当原值为空时）
        if (record.getDiagnosis() == null || record.getDiagnosis().isBlank()) {
            List<String> diagnosisList = new ArrayList<>();
            checkups.stream()
                    .map(HealthCheckupDO::getAbnormalItems)
                    .filter(s -> s != null && !s.isBlank())
                    .distinct()
                    .forEach(diagnosisList::add);
            eyes.stream().map(EyeExamRecordDO::getDiagnosis)
                    .filter(s -> s != null && !s.isBlank()).distinct().forEach(d -> diagnosisList.add("眼保健：" + d));
            hearings.stream().map(HearingExamRecordDO::getDiagnosis)
                    .filter(s -> s != null && !s.isBlank()).distinct().forEach(d -> diagnosisList.add("听力：" + d));
            orals.stream().map(OralExamRecordDO::getDiagnosis)
                    .filter(s -> s != null && !s.isBlank()).distinct().forEach(d -> diagnosisList.add("口腔：" + d));
            if (!diagnosisList.isEmpty()) {
                record.setDiagnosis(String.join("；", diagnosisList));
            } else if (anyAbnormal) {
                record.setDiagnosis("体检发现异常，待进一步诊断");
            } else {
                record.setDiagnosis("未见明显异常");
            }
        }
        // 9. 设置主诉（仅当为空且无医生手填）
        if (record.getChiefComplaint() == null || record.getChiefComplaint().isBlank()) {
            StringBuilder chief = new StringBuilder();
            chief.append(ageMonth != null ? ageMonth + "月龄儿童" : "儿童");
            chief.append("常规健康体检");
            if (anyAbnormal) chief.append("，发现异常项目");
            record.setChiefComplaint(chief.toString());
        }
        // 10. 持久化
        if (isNew) {
            medicalRecordMapper.insert(record);
            log.info("[aggregateToMedicalRecord] 新建病历 recordId={}, childId={}, visitDate={}",
                    record.getId(), childId, visitDate);
        } else {
            medicalRecordMapper.updateById(record);
            log.info("[aggregateToMedicalRecord] 更新病历 recordId={}, childId={}, visitDate={}",
                    record.getId(), childId, visitDate);
        }
    }

    private void validateMedicalRecordExists(Long id) {
        if (id == null || medicalRecordMapper.selectById(id) == null) {
            throw exception(MEDICAL_RECORD_NOT_EXISTS);
        }
    }

}

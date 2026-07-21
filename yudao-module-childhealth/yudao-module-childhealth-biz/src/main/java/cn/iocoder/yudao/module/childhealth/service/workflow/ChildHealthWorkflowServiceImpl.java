package cn.iocoder.yudao.module.childhealth.service.workflow;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.module.childhealth.api.rule.dto.AbnormalRuleDTO.MatchRequest;
import cn.iocoder.yudao.module.childhealth.api.workflow.dto.ChildHealthWorkflowDTO.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.medical.MedicalRecordTemplateDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.medical.MedicalRecordDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.management.FollowTaskDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.casemanagement.ExamReminderLogDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.exam.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.caseType.CaseTypeConfigDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.casemanagement.HighRiskNewbornDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.workflow.*;
import cn.iocoder.yudao.module.childhealth.dal.mysql.medical.MedicalRecordTemplateMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.medical.MedicalRecordMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.management.FollowTaskMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.casemanagement.HighRiskNewbornMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.casemanagement.ExamReminderLogMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.exam.*;
import cn.iocoder.yudao.module.childhealth.dal.mysql.screening.*;
import cn.iocoder.yudao.module.childhealth.dal.mysql.caseType.CaseTypeConfigMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.workflow.*;
import cn.iocoder.yudao.module.childhealth.service.rule.AbnormalRuleService;
import cn.iocoder.yudao.module.childhealth.service.ops.ChildHealthOpsService;
import cn.iocoder.yudao.module.childhealth.api.ops.dto.ChildHealthOpsDTO.MessagePushRequest;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ChildHealthWorkflowServiceImpl implements ChildHealthWorkflowService {
    @Resource private ChildInfoMapper childInfoMapper;
    @Resource private GuardianInfoMapper guardianInfoMapper;
    @Resource private HealthCheckupMapper healthCheckupMapper;
    @Resource private PhysicalExamRecordMapper physicalExamRecordMapper;
    @Resource private CaseRegistrationMapper caseRegistrationMapper;
    @Resource private FollowUpRecordMapper followUpRecordMapper;
    @Resource private ScreeningBatchMapper screeningBatchMapper;
    @Resource private ScreeningRecordMapper screeningRecordMapper;
    @Resource private ScreeningResultDetailMapper screeningResultDetailMapper;
    @Resource private ScreeningPositiveMapper screeningPositiveMapper;
    @Resource private RecheckRecordMapper recheckRecordMapper;
    @Resource private QrCodeManagementMapper qrCodeManagementMapper;
    @Resource private HearingExamRecordMapper hearingExamRecordMapper;
    @Resource private OralExamRecordMapper oralExamRecordMapper;
    @Resource private EyeExamRecordMapper eyeExamRecordMapper;
    @Resource private LabReportMapper labReportMapper;
    @Resource private MedicalRecordMapper medicalRecordMapper;
    @Resource private GrowthCurveDataMapper growthCurveDataMapper;
    @Resource private CaseAlertLogMapper caseAlertLogMapper;
    @Resource private MedicalRecordTemplateMapper medicalTemplateMapper;
    @Resource private AbnormalRuleService abnormalRuleService;
    @Resource private KindergartenExamMapper kindergartenExamMapper;
    @Resource private ExternalReportMapper externalReportMapper;
    @Resource private CaseTypeConfigMapper caseTypeConfigMapper;
    @Resource private CaseCardMapper caseCardMapper;
    @Resource private CaseRecoveryAssessmentMapper caseRecoveryAssessmentMapper;
    @Resource private HighRiskNewbornMapper highRiskNewbornMapper;
    @Resource private ScreeningPositiveRuleService screeningPositiveRuleService;
    @Resource private FollowTaskMapper followTaskMapper;
    @Resource private ExamReminderLogMapper examReminderLogMapper;
    @Resource private StudentInfoMapper studentInfoMapper;
    @Resource private ReferralRecordMapper referralRecordMapper;
    @Resource private TransferArchiveMapper transferArchiveMapper;
    @Resource private DiseaseKnowledgeMapper diseaseKnowledgeMapper;
    @Resource private ChildHealthOpsService opsService;

    @Override
    @Transactional
    public Long createChild(ChildCreateRequest request) {
        if (request.getBirthDate().isAfter(LocalDate.now())) {
            throw error("出生日期不能晚于当前日期");
        }
        if (childInfoMapper.selectCount(Wrappers.<ChildInfoDO>lambdaQuery()
                .eq(ChildInfoDO::getChildCode, request.getChildCode())) > 0) {
            throw error("儿童编码已存在");
        }
        if (request.getIdCard() != null && childInfoMapper.selectCount(Wrappers.<ChildInfoDO>lambdaQuery()
                .eq(ChildInfoDO::getIdCard, request.getIdCard())) > 0) {
            throw error("身份证号已存在");
        }
        validateChildFields(request.getGender(), request.getHighRiskType());
        ChildInfoDO child = new ChildInfoDO();
        BeanUtils.copyProperties(request, child);
        child.setIsHighRisk(hasHighRiskTags(child.getHighRiskTags()) ? 1 : 0);
        child.setStatus(1);
        child.setCreateTime(LocalDateTime.now());
        child.setUpdateTime(LocalDateTime.now());
        childInfoMapper.insert(child);
        savePrimaryGuardian(child.getId(), request.getParentName(), request.getParentPhone());
        syncHighRiskLedger(child);
        return child.getId();
    }

    @Override
    public ChildResponse getChild(Long id) {
        ChildInfoDO child = requireChild(id);
        attachPrimaryGuardian(child);
        ChildResponse response = new ChildResponse();
        BeanUtils.copyProperties(child, response);
        return response;
    }

    @Override
    @Transactional
    public void updateChild(Long id, ChildUpdateRequest request) {
        ChildInfoDO child = requireChild(id);
        String currentStatus = String.valueOf(child.getStatus());
        if (request.getBirthDate().isAfter(LocalDate.now())) throw error("出生日期不能晚于当前日期");
        validateChildFields(request.getGender(), request.getHighRiskType());
        if (request.getIdCard() != null && childInfoMapper.selectCount(Wrappers.<ChildInfoDO>lambdaQuery()
                .eq(ChildInfoDO::getIdCard, request.getIdCard()).ne(ChildInfoDO::getId, id)) > 0) {
            throw error("身份证号已存在");
        }
        BeanUtils.copyProperties(request, child);
        child.setIsHighRisk(hasHighRiskTags(child.getHighRiskTags()) ? 1 : 0);
        if (request.getStatus() == null || String.valueOf(request.getStatus()).isBlank()) child.setStatus(Integer.valueOf(currentStatus));
        if (!List.of(1, 2, 3).contains(child.getStatus())) throw error("儿童状态只能为1正常、2转出或3死亡");
        child.setUpdateTime(LocalDateTime.now());
        childInfoMapper.updateById(child);
        syncHighRiskLedger(child);
        GuardianInfoDO guardian = guardianInfoMapper.selectOne(Wrappers.<GuardianInfoDO>lambdaQuery()
                .eq(GuardianInfoDO::getChildId, id).eq(GuardianInfoDO::getIsPrimary, true).last("LIMIT 1"));
        if (guardian == null) {
            savePrimaryGuardian(id, request.getParentName(), request.getParentPhone());
        } else {
            guardian.setGuardianName(request.getParentName());
            guardian.setGuardianPhone(request.getParentPhone());
            guardianInfoMapper.updateById(guardian);
        }
    }

    @Override
    public List<ChildResponse> searchChildren(String keyword, String status) {
        List<Long> guardianChildIds = keyword == null || keyword.isBlank() ? List.of()
                : guardianInfoMapper.selectList(Wrappers.<GuardianInfoDO>lambdaQuery()
                        .like(GuardianInfoDO::getGuardianPhone, keyword))
                .stream().map(GuardianInfoDO::getChildId).distinct().toList();
        return childInfoMapper.selectList(Wrappers.<ChildInfoDO>lambdaQuery()
                        .and(keyword != null && !keyword.isBlank(), query -> query
                                .like(ChildInfoDO::getChildName, keyword)
                                .or().like(ChildInfoDO::getChildCode, keyword)
                                .or(!guardianChildIds.isEmpty()).in(!guardianChildIds.isEmpty(),
                                        ChildInfoDO::getId, guardianChildIds))
                        .eq(status != null && !status.isBlank(), ChildInfoDO::getStatus, status)
                        .orderByDesc(ChildInfoDO::getCreateTime))
                .stream().peek(this::attachPrimaryGuardian).map(this::toChildResponse).toList();
    }

    @Override
    @Transactional
    public String generateChildQrCode(Long id) {
        ChildInfoDO child = requireChild(id);
        // ponytail: 存储稳定的二维码内容，需服务端PNG时再接文件存储。
        String content = "childhealth://child/" + child.getId() + "?code=" + child.getChildCode();
        child.setQrCode(content); child.setUpdateTime(LocalDateTime.now()); childInfoMapper.updateById(child); return content;
    }

    @Override
    @Transactional
    public List<String> generateChildQrCodes(List<Long> ids) {
        if (ids == null || ids.isEmpty()) throw error("儿童ID不能为空");
        return ids.stream().distinct().map(this::generateChildQrCode).toList();
    }

    @Override
    @Transactional
    public Long createCheckup(CheckupCreateRequest request) {
        requireChild(request.getChildId());
        if (request.getAgeMonths() < 0 || request.getAgeMonths() > 72
                || request.getHeight() != null && request.getHeight().signum() <= 0
                || request.getWeight() != null && request.getWeight().signum() <= 0) {
            throw error("月龄、身高或体重无效");
        }
        if (!List.of("1", "2", "3").contains(request.getCheckupType())) {
            throw error("体检类型只能为1常规、2入园或3专项");
        }
        if (request.getAbnormalItems() != null && !request.getAbnormalItems().isBlank()
                && !JsonUtils.isJson(request.getAbnormalItems())) {
            throw error("异常项目必须是合法JSON");
        }
        HealthCheckupDO checkup = new HealthCheckupDO();
        BeanUtils.copyProperties(request, checkup);
        boolean abnormal = request.getAbnormalItems() != null && !request.getAbnormalItems().isBlank()
                || request.getNutritionStatus() != null && !"NORMAL".equals(request.getNutritionStatus());
        checkup.setIsAbnormal(abnormal);
        checkup.setExamNo("EXAM-" + IdWorker.getId());
        checkup.setReviewStatus(1);
        checkup.setCreatedAt(LocalDateTime.now());
        checkup.setUpdatedAt(LocalDateTime.now());
        healthCheckupMapper.insert(checkup);
        completeExamReminders(checkup);
        if (request.getHeight() != null || request.getWeight() != null || request.getNutritionStatus() != null) {
            PhysicalExamRequest physical = new PhysicalExamRequest();
            physical.setHeight(request.getHeight()); physical.setWeight(request.getWeight());
            physical.setGrowthAssessment(request.getNutritionStatus());
            savePhysicalExam(checkup.getId(), physical);
        }
        return checkup.getId();
    }

    @Override
    public CheckupResponse getCheckup(Long id) {
        HealthCheckupDO checkup = healthCheckupMapper.selectById(id);
        if (checkup == null) throw error("体检记录不存在");
        return toCheckupResponse(checkup);
    }

    @Override
    @Transactional
    public void updateCheckup(Long id, CheckupCreateRequest request) {
        HealthCheckupDO checkup = requireCheckup(id);
        if (!checkup.getChildId().equals(request.getChildId())) throw error("不允许修改体检记录所属儿童");
        if (!"1".equals(checkup.getReviewStatus())) throw error("只有进行中的体检记录可修改");
        if (!List.of("1", "2", "3").contains(request.getCheckupType())) throw error("体检类型只能为1、2、3");
        checkup.setCheckupDate(request.getCheckupDate());
        checkup.setAgeMonths(request.getAgeMonths());
        checkup.setCheckupType(request.getCheckupType());
        checkup.setDoctorId(request.getDoctorId());
        checkup.setAbnormalItems(request.getAbnormalItems());
        checkup.setIsAbnormal(request.getAbnormalItems() != null && !request.getAbnormalItems().isBlank());
        checkup.setUpdatedAt(LocalDateTime.now());
        healthCheckupMapper.updateById(checkup);
        PhysicalExamRequest physical = new PhysicalExamRequest();
        physical.setHeight(request.getHeight()); physical.setWeight(request.getWeight()); physical.setGrowthAssessment(request.getNutritionStatus());
        savePhysicalExam(id, physical);
    }

    @Override
    public List<CheckupResponse> getAbnormalCheckups() {
        return healthCheckupMapper.selectList(Wrappers.<HealthCheckupDO>lambdaQuery()
                .eq(HealthCheckupDO::getIsAbnormal, true).orderByDesc(HealthCheckupDO::getCheckupDate))
                .stream().map(this::toCheckupResponse).toList();
    }

    @Override
    @Transactional
    public void submitCheckup(Long id) {
        requireEditableCheckup(id);
        if (physicalExamRecordMapper.selectCount(Wrappers.<PhysicalExamRecordDO>lambdaQuery()
                .eq(PhysicalExamRecordDO::getExamId, id)) == 0) {
            throw error("请先完成体格检查");
        }
        if (medicalRecordMapper.selectCount(Wrappers.<MedicalRecordDO>lambdaQuery()
                .eq(MedicalRecordDO::getExamId, id)) == 0) {
            throw error("请先生成完整病历");
        }
        if (healthCheckupMapper.update(null, Wrappers.<HealthCheckupDO>lambdaUpdate().eq(HealthCheckupDO::getId, id)
                .eq(HealthCheckupDO::getReviewStatus, 1).set(HealthCheckupDO::getReviewStatus, 2)
                .set(HealthCheckupDO::getUpdatedAt, LocalDateTime.now())) == 0) throw error("体检记录不存在或已提交");
    }

    @Override
    @Transactional
    public void reviewCheckup(Long id, Long reviewerId, boolean approved) {
        LambdaUpdateWrapper<HealthCheckupDO> update = Wrappers.<HealthCheckupDO>lambdaUpdate()
                .eq(HealthCheckupDO::getId, id).eq(HealthCheckupDO::getReviewStatus, 2)
                .set(HealthCheckupDO::getReviewStatus, approved ? 3 : 1)
                .set(HealthCheckupDO::getUpdatedAt, LocalDateTime.now());
        if (healthCheckupMapper.update(null, update) == 0) {
            throw error("体检记录不存在或已审核");
        }
    }

    @Override
    @Transactional
    public void batchReviewCheckup(List<Long> ids, Long reviewerId, boolean approved) {
        if (ids == null || ids.isEmpty()) throw error("体检记录ID列表不能为空");
        LambdaUpdateWrapper<HealthCheckupDO> update = Wrappers.<HealthCheckupDO>lambdaUpdate()
                .in(HealthCheckupDO::getId, ids).eq(HealthCheckupDO::getReviewStatus, 2)
                .set(HealthCheckupDO::getReviewStatus, approved ? 3 : 1)
                .set(HealthCheckupDO::getUpdatedAt, LocalDateTime.now());
        int updated = healthCheckupMapper.update(null, update);
        if (updated == 0) throw error("没有可审核的体检记录");
    }

    @Override
    @Transactional
    public Long savePhysicalExam(Long examId, PhysicalExamRequest request) {
        HealthCheckupDO checkup = requireEditableCheckup(examId);
        if (request.getHeight() != null && request.getHeight().signum() <= 0
                || request.getWeight() != null && request.getWeight().signum() <= 0) throw error("身高或体重无效");
        PhysicalExamRecordDO record = physicalExamRecordMapper.selectOne(Wrappers.<PhysicalExamRecordDO>lambdaQuery()
                .eq(PhysicalExamRecordDO::getExamId, examId).last("LIMIT 1"));
        if (record == null) { record = new PhysicalExamRecordDO(); record.setExamId(examId); record.setCreateTime(LocalDateTime.now()); }
        BeanUtils.copyProperties(request, record);
        if (record.getHeight() != null && record.getWeight() != null) record.setBmi(record.getWeight()
                .multiply(java.math.BigDecimal.valueOf(10000)).divide(record.getHeight().multiply(record.getHeight()), 2, RoundingMode.HALF_UP));
        record.setIsAbnormal(hasAbnormalAssessment(record.getGrowthAssessment()));
        if (record.getHeightSd() != null && record.getHeightSd().abs().compareTo(java.math.BigDecimal.valueOf(2)) > 0
                || record.getWeightSd() != null && record.getWeightSd().abs().compareTo(java.math.BigDecimal.valueOf(2)) > 0) {
            record.setIsAbnormal(true);
        }
        if (record.getId() == null) physicalExamRecordMapper.insert(record); else physicalExamRecordMapper.updateById(record);
        saveGrowthCurve(checkup, record);
        MatchRequest matchRequest = new MatchRequest();
        matchRequest.setExamId(examId);
        Map<String, java.math.BigDecimal> values = new LinkedHashMap<>();
        values.put("height", record.getHeight()); values.put("weight", record.getWeight()); values.put("bmi", record.getBmi());
        values.put("heightSd", record.getHeightSd()); values.put("weightSd", record.getWeightSd());
        matchRequest.setValues(values);
        if (!abnormalRuleService.match(matchRequest).isEmpty() && !Boolean.TRUE.equals(record.getIsAbnormal())) {
            record.setIsAbnormal(true);
            physicalExamRecordMapper.updateById(record);
        }
        refreshExamAbnormal(checkup);
        return record.getId();
    }

    @Override
    @Transactional
    public Long recordEyeExam(EyeExamRequest request) {
        HealthCheckupDO checkup = requireEditableCheckup(request.getExamId());
        if (eyeExamRecordMapper.selectCount(Wrappers.<EyeExamRecordDO>lambdaQuery()
                .eq(EyeExamRecordDO::getExamId, request.getExamId())) > 0) throw error("该体检记录已有眼保健检查");
        EyeExamRecordDO record = new EyeExamRecordDO();
        BeanUtils.copyProperties(request, record);
        record.setIsAbnormal(hasAbnormalAssessment(request.getDiagnosis()));
        record.setCreateTime(LocalDateTime.now());
        eyeExamRecordMapper.insert(record);
        refreshExamAbnormal(checkup);
        return record.getId();
    }

    @Override
    @Transactional
    public Long recordHearingExam(HearingExamRequest request) {
        if (request.getHearingScreening() == null || request.getHearingScreening().isBlank()) {
            throw error("听力筛查结果不能为空");
        }
        HealthCheckupDO checkup = requireEditableCheckup(request.getExamId());
        if (hearingExamRecordMapper.selectCount(Wrappers.<HearingExamRecordDO>lambdaQuery()
                .eq(HearingExamRecordDO::getExamId, request.getExamId())) > 0) {
            throw error("该体检记录已有听力检查");
        }
        HearingExamRecordDO record = new HearingExamRecordDO();
        BeanUtils.copyProperties(request, record);
        record.setIsAbnormal(!"PASS".equalsIgnoreCase(request.getHearingScreening())
                || isAbnormalText(request.getLeftEarResult()) || isAbnormalText(request.getRightEarResult()));
        record.setCreateTime(LocalDateTime.now());
        hearingExamRecordMapper.insert(record);
        refreshExamAbnormal(checkup);
        return record.getId();
    }

    @Override
    @Transactional
    public Long recordOralExam(OralExamRequest request) {
        HealthCheckupDO checkup = requireEditableCheckup(request.getExamId());
        if (request.getToothCount() != null && request.getToothCount() < 0
                || request.getCariesCount() != null && request.getCariesCount() < 0) {
            throw error("牙齿数量和龋齿数量不能小于0");
        }
        if (oralExamRecordMapper.selectCount(Wrappers.<OralExamRecordDO>lambdaQuery()
                .eq(OralExamRecordDO::getExamId, request.getExamId())) > 0) {
            throw error("该体检记录已有口腔检查");
        }
        OralExamRecordDO record = new OralExamRecordDO();
        BeanUtils.copyProperties(request, record);
        record.setIsAbnormal(request.getCariesCount() != null && request.getCariesCount() > 0
                || hasAbnormalAssessment(request.getGumCondition()) || hasAbnormalAssessment(request.getMalocclusion())
                || hasAbnormalAssessment(request.getDiagnosis()));
        record.setCreateTime(LocalDateTime.now());
        oralExamRecordMapper.insert(record);
        refreshExamAbnormal(checkup);
        return record.getId();
    }

    @Override
    @Transactional
    public Long recordLabReport(Long examId, LabReportRequest request) {
        HealthCheckupDO checkup = requireEditableCheckup(examId);
        if (!List.of(1, 2).contains(request.getReportType())) throw error("报告类型只能为1检验或2检查");
        if (!List.of(1, 2, 3).contains(request.getSource())) throw error("报告来源只能为1LIS、2PACS或3手工录入");
        if (!JsonUtils.isJson(request.getReportContent())) throw error("报告内容必须是合法JSON");
        if (request.getAbnormalItems() != null && !request.getAbnormalItems().isBlank()
                && !JsonUtils.isJson(request.getAbnormalItems())) throw error("异常项目必须是合法JSON");
        LabReportDO report = new LabReportDO();
        BeanUtils.copyProperties(request, report);
        report.setExamId(examId);
        report.setReportDate(request.getReportDate() == null ? LocalDateTime.now() : request.getReportDate());
        report.setIsAbnormal(Boolean.TRUE.equals(request.getIsAbnormal())
                || request.getAbnormalItems() != null && !request.getAbnormalItems().isBlank());
        report.setCreateTime(LocalDateTime.now());
        labReportMapper.insert(report);
        refreshExamAbnormal(checkup);
        return report.getId();
    }

    @Override
    public List<LabReportResponse> getLabReports(Long examId) {
        requireCheckup(examId);
        return labReportMapper.selectList(Wrappers.<LabReportDO>lambdaQuery()
                .eq(LabReportDO::getExamId, examId).orderByDesc(LabReportDO::getReportDate))
                .stream().map(report -> {
                    LabReportResponse response = new LabReportResponse();
                    BeanUtils.copyProperties(report, response);
                    return response;
                }).toList();
    }

    @Override
    @Transactional
    public Long generateMedicalRecord(Long examId, MedicalRecordGenerateRequest request) {
        HealthCheckupDO checkup = requireEditableCheckup(examId);
        PhysicalExamRecordDO physical = physicalExamRecordMapper.selectOne(Wrappers.<PhysicalExamRecordDO>lambdaQuery()
                .eq(PhysicalExamRecordDO::getExamId, examId).last("LIMIT 1"));
        if (physical == null) throw error("请先完成体格检查");
        MedicalRecordTemplateDO template = medicalTemplateMapper.selectOne(Wrappers.<MedicalRecordTemplateDO>lambdaQuery()
                .eq(MedicalRecordTemplateDO::getTemplateType, "GENERAL_CHECKUP").eq(MedicalRecordTemplateDO::getStatus, 1)
                .le(MedicalRecordTemplateDO::getAgeMonthMin, checkup.getAgeMonths())
                .ge(MedicalRecordTemplateDO::getAgeMonthMax, checkup.getAgeMonths()).last("LIMIT 1"));
        if (template == null) throw error("未找到当前月龄的儿童健康检查模板");
        MedicalRecordDO record = medicalRecordMapper.selectOne(Wrappers.<MedicalRecordDO>lambdaQuery()
                .eq(MedicalRecordDO::getExamId, examId).last("LIMIT 1"));
        if (record == null) { record = new MedicalRecordDO(); record.setExamId(examId); record.setCreateTime(LocalDateTime.now()); }
        BeanUtils.copyProperties(request, record);
        record.setTemplateId(template.getId());
        record.setPhysicalExam(JsonUtils.toJsonString(physical));
        Map<String, Object> specialty = new LinkedHashMap<>();
        specialty.put("eye", selectEye(examId)); specialty.put("hearing", selectHearing(examId)); specialty.put("oral", selectOral(examId));
        record.setSpecialistExam(JsonUtils.toJsonString(specialty));
        List<LabReportDO> reports = labReportMapper.selectList(Wrappers.<LabReportDO>lambdaQuery()
                .eq(LabReportDO::getExamId, examId).orderByAsc(LabReportDO::getReportDate));
        record.setAuxExam(JsonUtils.toJsonString(reports));
        refreshExamAbnormal(checkup);
        checkup = requireCheckup(examId);
        
        List<CaseAlertLogDO> alerts = caseAlertLogMapper.selectList(Wrappers.<CaseAlertLogDO>lambdaQuery()
                .eq(CaseAlertLogDO::getExamId, examId).eq(CaseAlertLogDO::getIsHandled, false));
        
        if (record.getChiefComplaint() == null || record.getChiefComplaint().isEmpty()) {
            record.setChiefComplaint(generateChiefComplaint(checkup, physical, alerts));
        }
        if (record.getPresentIllness() == null || record.getPresentIllness().isEmpty()) {
            record.setPresentIllness(generatePresentIllness(checkup, physical, alerts));
        }
        if (record.getDiagnosis() == null || record.getDiagnosis().isEmpty()) {
            record.setDiagnosis(generateDiagnosis(checkup, physical, alerts));
        }
        if (record.getHealthGuidance() == null || record.getHealthGuidance().isEmpty()) {
            record.setHealthGuidance(generateHealthGuidance(checkup, physical, alerts, template));
        }
        
        record.setNextVisitAdvice(nextVisitPlan(checkup.getAgeMonths()));
        record.setIsAutoGenerated(true);
        record.setDoctorId(request.getDoctorId() == null ? checkup.getDoctorId() : request.getDoctorId());
        record.setRecordTime(LocalDateTime.now());
        if (record.getId() == null) medicalRecordMapper.insert(record); else medicalRecordMapper.updateById(record);
        return record.getId();
    }

    private String generateChiefComplaint(HealthCheckupDO checkup, PhysicalExamRecordDO physical, List<CaseAlertLogDO> alerts) {
        StringBuilder sb = new StringBuilder();
        String childName = getChildName(checkup.getChildId());
        sb.append(childName).append("，");
        sb.append(checkup.getAgeMonths()).append("月龄，");
        sb.append("常规健康体检");
        
        if (!alerts.isEmpty()) {
            List<String> abnormalItems = alerts.stream()
                    .map(CaseAlertLogDO::getTriggerItem)
                    .distinct()
                    .toList();
            if (!abnormalItems.isEmpty()) {
                sb.append("，发现");
                sb.append(String.join("、", abnormalItems));
                sb.append("异常");
            }
        }
        return sb.toString();
    }

    private String generatePresentIllness(HealthCheckupDO checkup, PhysicalExamRecordDO physical, List<CaseAlertLogDO> alerts) {
        StringBuilder sb = new StringBuilder();
        String childName = getChildName(checkup.getChildId());
        sb.append("患儿").append(childName).append("，");
        sb.append(checkup.getAgeMonths()).append("月龄，");
        sb.append("于").append(checkup.getCheckupDate()).append("进行健康体检。");
        
        sb.append("体格检查：");
        if (physical.getHeight() != null) sb.append("身高").append(physical.getHeight()).append("cm，");
        if (physical.getWeight() != null) sb.append("体重").append(physical.getWeight()).append("kg，");
        if (physical.getHeadCircumference() != null) sb.append("头围").append(physical.getHeadCircumference()).append("cm，");
        if (physical.getBmi() != null) sb.append("BMI").append(physical.getBmi()).append("，");
        sb.append("生长评估：").append(physical.getGrowthAssessment() != null ? physical.getGrowthAssessment() : "未评估");
        
        if (!alerts.isEmpty()) {
            sb.append("。异常发现：");
            for (CaseAlertLogDO alert : alerts) {
                sb.append(alert.getTriggerItem()).append("异常（检测值：").append(alert.getTriggerValue());
                sb.append("，规则：").append(alert.getThresholdRule()).append("）；");
            }
        }
        return sb.toString();
    }

    private String generateDiagnosis(HealthCheckupDO checkup, PhysicalExamRecordDO physical, List<CaseAlertLogDO> alerts) {
        if (Boolean.TRUE.equals(checkup.getIsAbnormal()) && !alerts.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            List<String> diagnoses = new ArrayList<>();
            
            for (CaseAlertLogDO alert : alerts) {
                String item = alert.getTriggerItem();
                String diagnosis = diagnoseByItem(item, alert.getTriggerValue(), physical);
                if (diagnosis != null && !diagnoses.contains(diagnosis)) {
                    diagnoses.add(diagnosis);
                }
            }
            
            if (diagnoses.isEmpty()) {
                sb.append("存在异常指标，请结合检查结果进一步评估");
            } else {
                sb.append(String.join("；", diagnoses));
            }
            return sb.toString();
        }
        return "未见明显异常";
    }

    private String diagnoseByItem(String item, String value, PhysicalExamRecordDO physical) {
        if ("heightSd".equals(item)) {
            BigDecimal sd = new BigDecimal(value);
            if (sd.abs().compareTo(BigDecimal.valueOf(3)) > 0) {
                return sd.compareTo(BigDecimal.ZERO) < 0 ? "重度身材矮小" : "身高过高";
            } else if (sd.abs().compareTo(BigDecimal.valueOf(2)) > 0) {
                return sd.compareTo(BigDecimal.ZERO) < 0 ? "中度身材矮小" : "身高偏高";
            }
        } else if ("weightSd".equals(item)) {
            BigDecimal sd = new BigDecimal(value);
            if (sd.compareTo(BigDecimal.valueOf(2)) > 0) return "超重";
            if (sd.compareTo(BigDecimal.valueOf(3)) > 0) return "肥胖";
            if (sd.compareTo(BigDecimal.valueOf(-2)) < 0) return "低体重";
            if (sd.compareTo(BigDecimal.valueOf(-3)) < 0) return "重度低体重";
        } else if ("bmi".equals(item)) {
            BigDecimal bmi = new BigDecimal(value);
            if (bmi.compareTo(BigDecimal.valueOf(18.5)) < 0) return "偏瘦";
            if (bmi.compareTo(BigDecimal.valueOf(24)) >= 0) return "超重";
            if (bmi.compareTo(BigDecimal.valueOf(28)) >= 0) return "肥胖";
        } else if ("headSd".equals(item)) {
            BigDecimal sd = new BigDecimal(value);
            if (sd.abs().compareTo(BigDecimal.valueOf(2)) > 0) {
                return sd.compareTo(BigDecimal.ZERO) < 0 ? "头围偏小" : "头围偏大";
            }
        }
        return item + "异常";
    }

    private String generateHealthGuidance(HealthCheckupDO checkup, PhysicalExamRecordDO physical, 
            List<CaseAlertLogDO> alerts, MedicalRecordTemplateDO template) {
        StringBuilder sb = new StringBuilder();
        
        if (template.getHealthGuidance() != null) {
            sb.append(template.getHealthGuidance());
        } else {
            sb.append("1. 饮食指导：保证营养均衡，合理搭配膳食；");
            sb.append("2. 运动指导：适当户外活动，促进生长发育；");
            sb.append("3. 睡眠指导：保证充足睡眠，养成良好作息；");
        }
        
        if (!alerts.isEmpty()) {
            sb.append("\n【异常指标健康指导】");
            Map<String, DiseaseKnowledgeDO> knowledgeMap = buildDiseaseKnowledgeMap();
            for (CaseAlertLogDO alert : alerts) {
                String diseaseCode = mapAlertToDiseaseCode(alert.getTriggerItem(), physical);
                DiseaseKnowledgeDO knowledge = knowledgeMap.get(diseaseCode);
                
                String guidance;
                if (knowledge != null && knowledge.getHomeCare() != null) {
                    guidance = knowledge.getHomeCare();
                    if (knowledge.getMedicalAdvice() != null) {
                        guidance += "；就诊指导：" + knowledge.getMedicalAdvice();
                    }
                } else {
                    guidance = getHealthGuidanceByItem(alert.getTriggerItem(), physical);
                }
                
                if (guidance != null) {
                    sb.append("\n- ").append(alert.getTriggerItem()).append("：").append(guidance);
                }
            }
        }
        
        sb.append("\n【复查建议】").append(nextVisitPlan(checkup.getAgeMonths()));
        return sb.toString();
    }
    
    private Map<String, DiseaseKnowledgeDO> buildDiseaseKnowledgeMap() {
        Map<String, DiseaseKnowledgeDO> map = new LinkedHashMap<>();
        List<DiseaseKnowledgeDO> list = diseaseKnowledgeMapper.selectList(
                Wrappers.<DiseaseKnowledgeDO>lambdaQuery().eq(DiseaseKnowledgeDO::getStatus, 1));
        for (DiseaseKnowledgeDO knowledge : list) {
            map.put(knowledge.getDiseaseCode(), knowledge);
        }
        return map;
    }
    
    private String mapAlertToDiseaseCode(String item, PhysicalExamRecordDO physical) {
        if ("heightSd".equals(item)) {
            BigDecimal sd = physical.getHeightSd();
            if (sd != null && sd.compareTo(BigDecimal.valueOf(-3)) < 0) return "GROWTH-001";
            if (sd != null && sd.compareTo(BigDecimal.valueOf(-2)) < 0) return "GROWTH-002";
            if (sd != null && sd.compareTo(BigDecimal.valueOf(3)) > 0) return "GROWTH-003";
            return "GROWTH-002";
        } else if ("weightSd".equals(item)) {
            BigDecimal sd = physical.getWeightSd();
            if (sd != null && sd.compareTo(BigDecimal.valueOf(3)) > 0) return "NUTRITION-002";
            if (sd != null && sd.compareTo(BigDecimal.valueOf(2)) > 0) return "NUTRITION-001";
            if (sd != null && sd.compareTo(BigDecimal.valueOf(-3)) < 0) return "NUTRITION-004";
            if (sd != null && sd.compareTo(BigDecimal.valueOf(-2)) < 0) return "NUTRITION-003";
            return "NUTRITION-001";
        } else if ("bmi".equals(item)) {
            BigDecimal bmi = physical.getBmi();
            if (bmi != null && bmi.compareTo(BigDecimal.valueOf(28)) >= 0) return "NUTRITION-002";
            if (bmi != null && bmi.compareTo(BigDecimal.valueOf(24)) >= 0) return "NUTRITION-001";
            if (bmi != null && bmi.compareTo(BigDecimal.valueOf(18.5)) < 0) return "NUTRITION-003";
            return "NUTRITION-001";
        } else if ("headSd".equals(item)) {
            BigDecimal sd = physical.getHeadSd();
            if (sd != null && sd.compareTo(BigDecimal.valueOf(-2)) < 0) return "NEURO-001";
            if (sd != null && sd.compareTo(BigDecimal.valueOf(2)) > 0) return "NEURO-002";
            return "NEURO-001";
        }
        return "GENERAL-001";
    }

    private String getHealthGuidanceByItem(String item, PhysicalExamRecordDO physical) {
        if ("heightSd".equals(item)) {
            return "建议增加蛋白质摄入，保证充足睡眠，定期监测身高变化，必要时进一步检查生长激素水平";
        } else if ("weightSd".equals(item)) {
            BigDecimal sd = physical.getWeightSd();
            if (sd != null && sd.compareTo(BigDecimal.ZERO) > 0) {
                return "建议控制热量摄入，减少甜食和油炸食品，增加运动量";
            } else {
                return "建议增加营养摄入，保证蛋白质和热量供应，定期监测体重";
            }
        } else if ("bmi".equals(item)) {
            BigDecimal bmi = physical.getBmi();
            if (bmi != null && bmi.compareTo(BigDecimal.valueOf(24)) >= 0) {
                return "建议控制体重，减少高热量食物摄入，增加体育锻炼";
            } else {
                return "建议增加营养，保证均衡饮食，促进体重增长";
            }
        } else if ("headSd".equals(item)) {
            return "建议定期监测头围变化，评估神经系统发育情况，必要时进行头颅影像学检查";
        }
        return "建议定期复查，关注指标变化";
    }

    @Override
    public MedicalRecordResponse getMedicalRecord(Long checkupId) {
        HealthCheckupDO checkup = requireCheckup(checkupId);
        MedicalRecordResponse response = new MedicalRecordResponse();
        response.setCheckup(toCheckupResponse(checkup));
        HearingExamRecordDO hearing = selectHearing(checkupId);
        OralExamRecordDO oral = selectOral(checkupId);
        EyeExamRecordDO eye = selectEye(checkupId);
        response.setHearingExamId(hearing == null ? null : hearing.getId());
        response.setOralExamId(oral == null ? null : oral.getId());
        response.setEyeExamId(eye == null ? null : eye.getId());
        MedicalRecordDO record = medicalRecordMapper.selectOne(Wrappers.<MedicalRecordDO>lambdaQuery()
                .eq(MedicalRecordDO::getExamId, checkupId).last("LIMIT 1"));
        if (record != null) {
            BeanUtils.copyProperties(record, response);
            response.setMedicalRecordId(record.getId());
            response.setCheckup(toCheckupResponse(checkup));
            response.setHearingExamId(hearing == null ? null : hearing.getId());
            response.setOralExamId(oral == null ? null : oral.getId());
            response.setEyeExamId(eye == null ? null : eye.getId());
        }
        return response;
    }

    @Override
    public CheckupFlowResponse getCheckupFlow(Long checkupId) {
        HealthCheckupDO checkup = requireCheckup(checkupId);
        CheckupFlowResponse response = new CheckupFlowResponse();
        response.setExamId(checkupId); response.setStatus(checkup.getReviewStatus() != null ? checkup.getReviewStatus().toString() : null); response.setAbnormal(checkup.getIsAbnormal());
        response.setPhysicalCompleted(existsPhysical(checkupId));
        response.setEyeCompleted(selectEye(checkupId) != null);
        response.setHearingCompleted(selectHearing(checkupId) != null);
        response.setOralCompleted(selectOral(checkupId) != null);
        response.setLabCompleted(labReportMapper.selectCount(Wrappers.<LabReportDO>lambdaQuery().eq(LabReportDO::getExamId, checkupId)) > 0);
        response.setMedicalRecordGenerated(medicalRecordMapper.selectCount(Wrappers.<MedicalRecordDO>lambdaQuery()
                .eq(MedicalRecordDO::getExamId, checkupId)) > 0);
        if (Integer.valueOf(3).equals(checkup.getReviewStatus())) {
            response.setNextAction("COMPLETED");
        } else if (Integer.valueOf(2).equals(checkup.getReviewStatus())) {
            response.setNextAction("REVIEW");
        } else if (!response.getPhysicalCompleted()) {
            response.setNextAction("PHYSICAL_EXAM");
        } else if (!response.getMedicalRecordGenerated()) {
            response.setNextAction("GENERATE_MEDICAL_RECORD");
        } else {
            response.setNextAction("SUBMIT");
        }
        return response;
    }

    @Override
    @Transactional
    public Long archiveKindergartenExam(Long examId, KindergartenArchiveRequest request) {
        HealthCheckupDO checkup = requireEditableCheckup(examId);
        if (!"2".equals(checkup.getCheckupType())) throw error("只有入园入托体检可以生成入园档案");
        MedicalRecordDO medicalRecord = medicalRecordMapper.selectOne(Wrappers.<MedicalRecordDO>lambdaQuery()
                .eq(MedicalRecordDO::getExamId, examId).last("LIMIT 1"));
        if (medicalRecord == null) throw error("请先生成完整病历");
        KindergartenExamDO archive = kindergartenExamMapper.selectOne(Wrappers.<KindergartenExamDO>lambdaQuery()
                .eq(KindergartenExamDO::getExamId, examId).last("LIMIT 1"));
        if (archive == null) {
            archive = new KindergartenExamDO(); archive.setExamId(examId);
            archive.setChildId(checkup.getChildId()); archive.setExamDate(checkup.getCheckupDate());
            archive.setCreateTime(LocalDateTime.now());
        }
        BeanUtils.copyProperties(request, archive);
        archive.setDoctorId(request.getDoctorId() == null ? checkup.getDoctorId() : request.getDoctorId());
        archive.setRecordContent(JsonUtils.toJsonString(getMedicalRecord(examId)));
        if (archive.getId() == null) kindergartenExamMapper.insert(archive); else kindergartenExamMapper.updateById(archive);
        return archive.getId();
    }

    @Override
    public KindergartenArchiveResponse getKindergartenExam(Long examId) {
        requireCheckup(examId);
        KindergartenExamDO archive = kindergartenExamMapper.selectOne(Wrappers.<KindergartenExamDO>lambdaQuery()
                .eq(KindergartenExamDO::getExamId, examId).last("LIMIT 1"));
        if (archive == null) throw error("入园入托体检档案不存在");
        KindergartenArchiveResponse response = new KindergartenArchiveResponse();
        BeanUtils.copyProperties(archive, response);
        return response;
    }

    @Override
    @Transactional
    public Long archiveExternalReport(Long childId, ExternalReportRequest request) {
        requireChild(childId);
        if (!hasReportPayload(request.getReportContent(), request.getFileUrl())) {
            throw error("报告内容和文件地址至少填写一项");
        }
        ExternalReportDO report = new ExternalReportDO();
        BeanUtils.copyProperties(request, report);
        report.setChildId(childId); report.setUploadTime(LocalDateTime.now()); report.setCreateTime(LocalDateTime.now());
        externalReportMapper.insert(report);
        return report.getId();
    }

    @Override
    public List<ExternalReportResponse> getExternalReports(Long childId) {
        requireChild(childId);
        return externalReportMapper.selectList(Wrappers.<ExternalReportDO>lambdaQuery()
                .eq(ExternalReportDO::getChildId, childId).orderByDesc(ExternalReportDO::getReportDate))
                .stream().map(report -> {
                    ExternalReportResponse response = new ExternalReportResponse();
                    BeanUtils.copyProperties(report, response);
                    return response;
                }).toList();
    }

    @Override
    @Transactional
    public Long openCase(CaseCreateRequest request) {
        requireChild(request.getChildId());
        CaseTypeConfigDO caseType = requireCaseType(request.getCaseTypeId());
        if (request.getCaseSource() != null && (request.getCaseSource() < 1 || request.getCaseSource() > 3)) {
            throw error("专案来源只能为1、2、3");
        }
        if (request.getCaseLevel() != null && (request.getCaseLevel() < 1 || request.getCaseLevel() > 3)) {
            throw error("专案等级只能为1、2、3");
        }
        if (caseRegistrationMapper.selectCount(Wrappers.<CaseRegistrationDO>lambdaQuery()
                .eq(CaseRegistrationDO::getChildId, request.getChildId())
                .eq(CaseRegistrationDO::getCaseTypeId, request.getCaseTypeId())
                .eq(CaseRegistrationDO::getCaseStatus, 1)) > 0) throw error("该儿童已有同类型进行中专案");
        CaseRegistrationDO item = new CaseRegistrationDO();
        BeanUtils.copyProperties(request, item);
        item.setCaseSource(request.getCaseSource() == null ? 2 : request.getCaseSource());
        item.setId(IdWorker.getId());
        item.setCaseNo("CASE-" + item.getId());
        item.setRegistrationDate(LocalDate.now());
        item.setCaseStatus(1);
        item.setCreatedAt(LocalDateTime.now());
        item.setUpdatedAt(LocalDateTime.now());
        caseRegistrationMapper.insert(item);
        createCaseCard(item, caseType);
        ensureFollowTask(item.getChildId(), 4, "CASE:" + item.getId(),
                "专案随访任务：" + caseType.getTypeName(), LocalDate.now(), item.getResponsibleDoctor());
        if ("HIGH_RISK".equals(caseType.getTypeCode())) markHighRiskRegistered(item.getChildId());
        return item.getId();
    }

    @Override
    @Transactional
    public Long autoOpenHighRiskCase(Long childId, String initialDiagnosis, Integer caseLevel) {
        requireChild(childId);
        // 1. 查找 HIGH_RISK 专案类型配置
        CaseTypeConfigDO caseType = caseTypeConfigMapper.selectOne(Wrappers.<CaseTypeConfigDO>lambdaQuery()
                .eq(CaseTypeConfigDO::getTypeCode, "HIGH_RISK")
                .eq(CaseTypeConfigDO::getStatus, 1).last("LIMIT 1"));
        if (caseType == null) return null;
        // 2. 已有同类型进行中专案则跳过
        long activeCount = caseRegistrationMapper.selectCount(Wrappers.<CaseRegistrationDO>lambdaQuery()
                .eq(CaseRegistrationDO::getChildId, childId)
                .eq(CaseRegistrationDO::getCaseTypeId, caseType.getId())
                .eq(CaseRegistrationDO::getCaseStatus, 1));
        if (activeCount > 0) return null;
        // 3. 自动建专案
        try {
            CaseCreateRequest create = new CaseCreateRequest();
            create.setChildId(childId);
            create.setCaseTypeId(caseType.getId());
            create.setCaseSource(1); // 1自动
            create.setInitialDiagnosis(initialDiagnosis == null ? "建档时自动识别高危儿，自动建专案" : initialDiagnosis);
            create.setCaseLevel(caseLevel == null ? 2 : caseLevel);
            return openCase(create);
        } catch (Exception e) {
            // 自动建专案失败不影响建档主流程
            System.err.println("[autoOpenHighRiskCase] 自动建专案失败 childId=" + childId + " err=" + e.getMessage());
            return null;
        }
    }

    @Override
    public CaseResponse getCase(Long id) {
        CaseRegistrationDO item = caseRegistrationMapper.selectById(id);
        if (item == null) throw error("专案不存在");
        CaseResponse response = new CaseResponse();
        BeanUtils.copyProperties(item, response);
        return response;
    }

    @Override
    public List<CaseResponse> getCases(Long childId, Integer status) {
        return caseRegistrationMapper.selectList(Wrappers.<CaseRegistrationDO>lambdaQuery()
                .eq(childId != null, CaseRegistrationDO::getChildId, childId)
                .eq(status != null, CaseRegistrationDO::getCaseStatus, status)
                .orderByDesc(CaseRegistrationDO::getRegistrationDate)).stream().map(item -> {
                    CaseResponse response = new CaseResponse(); BeanUtils.copyProperties(item, response); return response;
                }).toList();
    }

    @Override
    public Map<String, Object> getCaseStatistics() {
        long total = caseRegistrationMapper.selectCount(Wrappers.<CaseRegistrationDO>lambdaQuery());
        long active = caseRegistrationMapper.selectCount(Wrappers.<CaseRegistrationDO>lambdaQuery().eq(CaseRegistrationDO::getCaseStatus, 1));
        return Map.of("total", total, "active", active, "closed", total - active);
    }

    @Override
    @Transactional
    public void updateCase(Long id, CaseUpdateRequest request) {
        CaseRegistrationDO item = caseRegistrationMapper.selectById(id);
        if (item == null) throw error("专案不存在");
        if (!Integer.valueOf(1).equals(item.getCaseStatus())) throw error("已结案或转出的专案不可修改");
        if (request.getCaseSource() != null) item.setCaseSource(request.getCaseSource());
        if (request.getInitialDiagnosis() != null) item.setInitialDiagnosis(request.getInitialDiagnosis());
        if (request.getCaseLevel() != null) item.setCaseLevel(request.getCaseLevel());
        if (request.getResponsibleDoctor() != null) item.setResponsibleDoctor(request.getResponsibleDoctor());
        item.setUpdatedAt(LocalDateTime.now()); caseRegistrationMapper.updateById(item);
    }

    @Override
    @Transactional
    public void dischargeCase(Long id) {
        CaseRegistrationDO item = requireCase(id);
        if (Integer.valueOf(2).equals(item.getCaseStatus())) return;
        if (!Integer.valueOf(1).equals(item.getCaseStatus())) throw error("已转出的专案不能结案");
        CaseRecoveryAssessmentDO assessment = caseRecoveryAssessmentMapper.selectOne(
                Wrappers.<CaseRecoveryAssessmentDO>lambdaQuery().eq(CaseRecoveryAssessmentDO::getCaseId, id)
                        .eq(CaseRecoveryAssessmentDO::getIsQualified, true)
                        .orderByDesc(CaseRecoveryAssessmentDO::getAssessmentDate).last("LIMIT 1"));
        if (assessment == null) throw error("请先完成达到结案标准的康复评估");
        if (caseRegistrationMapper.update(null, Wrappers.<CaseRegistrationDO>lambdaUpdate()
                .eq(CaseRegistrationDO::getId, id).eq(CaseRegistrationDO::getCaseStatus, 1)
                .set(CaseRegistrationDO::getCaseStatus, 2)
                .set(CaseRegistrationDO::getDischargeDate, LocalDate.now())
                .set(CaseRegistrationDO::getDischargeReason, "康复评估达标")
                .set(CaseRegistrationDO::getUpdatedAt, LocalDateTime.now())) == 0) {
            throw error("专案不存在或已结案");
        }
        finishFollowTask("CASE:" + id);
    }

    @Override
    public List<CaseAlertResponse> getCaseAlerts(Long childId, Boolean handled) {
        return caseAlertLogMapper.selectList(Wrappers.<CaseAlertLogDO>lambdaQuery()
                .eq(childId != null, CaseAlertLogDO::getChildId, childId)
                .eq(handled != null, CaseAlertLogDO::getIsHandled, handled)
                .orderByDesc(CaseAlertLogDO::getCreateTime)).stream().map(alert -> {
                    CaseAlertResponse response = new CaseAlertResponse();
                    BeanUtils.copyProperties(alert, response);
                    return response;
                }).toList();
    }

    @Override
    @Transactional
    public Long openCaseFromAlert(Long alertId, CaseAlertOpenRequest request) {
        CaseAlertLogDO alert = caseAlertLogMapper.selectById(alertId);
        if (alert == null) throw error("专案提醒不存在");
        if (Boolean.TRUE.equals(alert.getIsHandled())) throw error("专案提醒已处理");
        CaseCreateRequest create = new CaseCreateRequest();
        create.setChildId(alert.getChildId()); create.setCaseTypeId(request.getCaseTypeId()); create.setCaseSource(1);
        create.setInitialDiagnosis(request.getInitialDiagnosis() == null ? alert.getAlertContent() : request.getInitialDiagnosis());
        create.setCaseLevel(request.getCaseLevel()); create.setResponsibleDoctor(request.getResponsibleDoctor());
        Long caseId = openCase(create);
        alert.setIsHandled(true); alert.setHandleResult("已创建专案：" + caseId); caseAlertLogMapper.updateById(alert);
        return caseId;
    }

    @Override
    public CaseCardResponse getCaseCard(Long caseId) {
        requireCase(caseId);
        CaseCardDO card = caseCardMapper.selectOne(Wrappers.<CaseCardDO>lambdaQuery()
                .eq(CaseCardDO::getCaseId, caseId).last("LIMIT 1"));
        if (card == null) throw error("专案个案卡不存在");
        CaseCardResponse response = new CaseCardResponse(); BeanUtils.copyProperties(card, response); return response;
    }

    @Override
    @Transactional
    public Long assessCaseRecovery(Long caseId, CaseRecoveryAssessmentRequest request) {
        CaseRegistrationDO item = requireCase(caseId);
        if (!Integer.valueOf(1).equals(item.getCaseStatus())) throw error("只有进行中的专案可以进行康复评估");
        if (!JsonUtils.isJson(request.getAssessmentContent())) throw error("康复评估内容必须是合法JSON");
        if (request.getRecoveryLevel() < 1 || request.getRecoveryLevel() > 3) throw error("康复程度只能为1、2、3");
        if (!validRecovery(request.getRecoveryLevel(), request.getIsQualified())) {
            throw error("达到结案标准时康复程度必须为3达标");
        }
        CaseRecoveryAssessmentDO assessment = new CaseRecoveryAssessmentDO();
        BeanUtils.copyProperties(request, assessment); assessment.setCaseId(caseId);
        assessment.setAssessmentDate(request.getAssessmentDate() == null ? LocalDate.now() : request.getAssessmentDate());
        assessment.setCreateTime(LocalDateTime.now()); caseRecoveryAssessmentMapper.insert(assessment);
        if (Boolean.TRUE.equals(request.getIsQualified())) {
            caseRegistrationMapper.update(null, Wrappers.<CaseRegistrationDO>lambdaUpdate().eq(CaseRegistrationDO::getId, caseId)
                    .eq(CaseRegistrationDO::getCaseStatus, 1).set(CaseRegistrationDO::getCaseStatus, 2)
                    .set(CaseRegistrationDO::getDischargeDate, assessment.getAssessmentDate())
                    .set(CaseRegistrationDO::getDischargeReason,
                            request.getDischargeReason() == null ? "康复评估达标" : request.getDischargeReason())
                    .set(CaseRegistrationDO::getUpdatedAt, LocalDateTime.now()));
            finishFollowTask("CASE:" + caseId);
        }
        return assessment.getId();
    }

    @Override
    public List<CaseRecoveryAssessmentResponse> getCaseRecoveryAssessments(Long caseId) {
        requireCase(caseId);
        return caseRecoveryAssessmentMapper.selectList(Wrappers.<CaseRecoveryAssessmentDO>lambdaQuery()
                .eq(CaseRecoveryAssessmentDO::getCaseId, caseId)
                .orderByDesc(CaseRecoveryAssessmentDO::getAssessmentDate)).stream().map(item -> {
                    CaseRecoveryAssessmentResponse response = new CaseRecoveryAssessmentResponse();
                    BeanUtils.copyProperties(item, response); return response;
                }).toList();
    }

    @Override
    public List<HighRiskWarningResponse> getHighRiskWarnings(Boolean registered, Boolean followed) {
        return highRiskNewbornMapper.selectList(Wrappers.<HighRiskNewbornDO>lambdaQuery()
                .eq(registered != null, HighRiskNewbornDO::getIsRegistered, registered)
                .eq(followed != null, HighRiskNewbornDO::getIsFollowed, followed)
                .orderByDesc(HighRiskNewbornDO::getCreateTime)).stream().map(item -> {
                    HighRiskWarningResponse response = new HighRiskWarningResponse();
                    BeanUtils.copyProperties(item, response); return response;
                }).toList();
    }

    @Override
    @Transactional
    public Long addFollowUp(FollowUpCreateRequest request) {
        requireChild(request.getChildId());
        if (request.getFollowType() < 1 || request.getFollowType() > 4) {
            throw error("随访方式只能为1电话、2短信、3微信或4面诊");
        }
        if (request.getCaseId() != null) {
            CaseRegistrationDO item = caseRegistrationMapper.selectById(request.getCaseId());
            if (item == null || !request.getChildId().equals(item.getChildId())) {
                throw error("专案与儿童不匹配");
            }
        }
        FollowUpRecordDO record = new FollowUpRecordDO();
        BeanUtils.copyProperties(request, record);
        if (request.getMeasureData() != null && !request.getMeasureData().isBlank()
                && !JsonUtils.isJson(request.getMeasureData())) throw error("随访测量数据必须是合法JSON");
        record.setCreateTime(LocalDateTime.now());
        followUpRecordMapper.insert(record);
        highRiskNewbornMapper.update(null, Wrappers.<HighRiskNewbornDO>lambdaUpdate()
                .eq(HighRiskNewbornDO::getChildId, request.getChildId())
                .set(HighRiskNewbornDO::getIsFollowed, true).set(HighRiskNewbornDO::getAlertStatus, 0));
        return record.getId();
    }

    @Override
    public FollowUpResponse getFollowUp(Long id) {
        FollowUpRecordDO record = followUpRecordMapper.selectById(id);
        if (record == null) throw error("随访记录不存在");
        FollowUpResponse response = new FollowUpResponse(); BeanUtils.copyProperties(record, response); return response;
    }

    @Override
    public List<FollowUpResponse> getFollowUps(Long childId, Long caseId) {
        return followUpRecordMapper.selectList(Wrappers.<FollowUpRecordDO>lambdaQuery()
                .eq(childId != null, FollowUpRecordDO::getChildId, childId)
                .eq(caseId != null, FollowUpRecordDO::getCaseId, caseId)
                .orderByDesc(FollowUpRecordDO::getFollowDate)).stream().map(record -> {
                    FollowUpResponse response = new FollowUpResponse(); BeanUtils.copyProperties(record, response); return response;
                }).toList();
    }

    @Override
    public Map<String, Object> getFollowUpStatistics() {
        long total = followUpRecordMapper.selectCount(Wrappers.<FollowUpRecordDO>lambdaQuery());
        long currentMonth = followUpRecordMapper.selectCount(Wrappers.<FollowUpRecordDO>lambdaQuery()
                .between(FollowUpRecordDO::getFollowDate, LocalDate.now().withDayOfMonth(1), LocalDate.now()));
        return Map.of("total", total, "currentMonth", currentMonth);
    }

    @Override
    @Transactional
    public void updateFollowUp(Long id, FollowUpCreateRequest request) {
        FollowUpRecordDO existing = followUpRecordMapper.selectById(id);
        if (existing == null) throw error("随访记录不存在");
        if (!existing.getChildId().equals(request.getChildId()) || !existing.getCaseId().equals(request.getCaseId())) {
            throw error("不允许修改随访所属儿童或专案");
        }
        if (request.getFollowType() < 1 || request.getFollowType() > 4) {
            throw error("随访方式只能为1电话、2短信、3微信或4面诊");
        }
        if (request.getMeasureData() != null && !request.getMeasureData().isBlank() && !JsonUtils.isJson(request.getMeasureData())) {
            throw error("随访测量数据必须是合法JSON");
        }
        Long createId = existing.getId(); LocalDateTime createTime = existing.getCreateTime();
        BeanUtils.copyProperties(request, existing); existing.setId(createId); existing.setCreateTime(createTime);
        followUpRecordMapper.updateById(existing);
    }

    @Override
    @Transactional
    public List<Long> addFollowUps(FollowUpBatchRequest request) {
        if (request.getRecords().isEmpty()) throw error("批量随访记录不能为空");
        return request.getRecords().stream().map(this::addFollowUp).toList();
    }

    @Override
    @Transactional
    public Long createScreeningBatch(ScreeningBatchCreateRequest request) {
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw error("筛查开始日期不能晚于结束日期");
        }
        if (screeningBatchMapper.selectCount(Wrappers.<ScreeningBatchDO>lambdaQuery()
                .eq(ScreeningBatchDO::getBatchNo, request.getBatchNo())) > 0) {
            throw error("筛查批次编码已存在");
        }
        ScreeningBatchDO batch = new ScreeningBatchDO();
        BeanUtils.copyProperties(request, batch);
        batch.setActualCount(0);
        batch.setBatchStatus(1);
        batch.setCreateTime(LocalDateTime.now());
        screeningBatchMapper.insert(batch);
        return batch.getId();
    }

    @Override
    public ScreeningBatchResponse getScreeningBatch(Long id) {
        ScreeningBatchDO batch = screeningBatchMapper.selectById(id);
        if (batch == null) throw error("筛查批次不存在");
        ScreeningBatchResponse response = new ScreeningBatchResponse(); BeanUtils.copyProperties(batch, response); return response;
    }

    @Override
    public List<ScreeningBatchResponse> getScreeningBatches(Integer status) {
        return screeningBatchMapper.selectList(Wrappers.<ScreeningBatchDO>lambdaQuery()
                .eq(status != null, ScreeningBatchDO::getBatchStatus, status)
                .orderByDesc(ScreeningBatchDO::getStartDate)).stream().map(batch -> {
                    ScreeningBatchResponse response = new ScreeningBatchResponse(); BeanUtils.copyProperties(batch, response); return response;
                }).toList();
    }

    @Override
    @Transactional
    public void updateScreeningBatchStatus(Long id, Integer status) {
        if (status == null || status < 1 || status > 3) throw error("批次状态只能为1、2、3");
        ScreeningBatchDO batch = screeningBatchMapper.selectById(id);
        if (batch == null) throw error("筛查批次不存在");
        if (status < batch.getBatchStatus()) throw error("筛查批次状态不能倒退");
        batch.setBatchStatus(status); screeningBatchMapper.updateById(batch);
    }

    @Override
    public Map<String, Object> getScreeningBatchStatistics(Long id) {
        ScreeningBatchResponse batch = getScreeningBatch(id);
        long actual = screeningRecordMapper.selectCount(Wrappers.<ScreeningRecordDO>lambdaQuery()
                .eq(ScreeningRecordDO::getBatchId, id));
        long positive = screeningRecordMapper.selectCount(Wrappers.<ScreeningRecordDO>lambdaQuery()
                .eq(ScreeningRecordDO::getBatchId, id).eq(ScreeningRecordDO::getHasPositive, 1));
        Map<String, Object> result = new LinkedHashMap<>(); result.put("batch", batch); result.put("actualCount", actual);
        result.put("positiveCount", positive); result.put("positiveRate", actual == 0 ? 0D : (double) positive / actual); return result;
    }

    @Override
    @Transactional
    public Long recordScreening(ScreeningRecordCreateRequest request) {
        ScreeningBatchDO batch = screeningBatchMapper.selectById(request.getBatchId());
        if (batch == null) throw error("筛查批次不存在");
        if (Integer.valueOf(3).equals(batch.getBatchStatus())) throw error("已完成批次不能继续录入");
        if (request.getHeight() != null && request.getHeight().signum() <= 0
                || request.getWeight() != null && request.getWeight().signum() <= 0) throw error("身高或体重无效");
        ScreeningRecordDO record = screeningRecordMapper.selectOne(Wrappers.<ScreeningRecordDO>lambdaQuery()
                .eq(ScreeningRecordDO::getBatchId, request.getBatchId())
                .eq(ScreeningRecordDO::getStudentId, request.getStudentId()).last("LIMIT 1"));
        if (record != null && !Integer.valueOf(1).equals(record.getCheckStatus())) {
            throw error("该学生在本批次已有筛查记录");
        }
        boolean newRecord = record == null;
        if (newRecord) { record = new ScreeningRecordDO(); record.setRecordNo("SCREEN-" + IdWorker.getId());
            record.setBatchId(request.getBatchId()); record.setStudentId(request.getStudentId()); record.setCreateTime(LocalDateTime.now()); }
        record.setScreeningDate(request.getScreeningDate());
        record.setCheckStatus(1);
        record.setUpdateTime(LocalDateTime.now());
        if (newRecord) screeningRecordMapper.insert(record); else screeningRecordMapper.updateById(record);
        for (Map.Entry<String, String> item : screeningItems(request, false).entrySet()) {
            ScreeningResultDetailDO detail = screeningResultDetailMapper.selectOne(
                    Wrappers.<ScreeningResultDetailDO>lambdaQuery().eq(ScreeningResultDetailDO::getRecordId, record.getId())
                            .eq(ScreeningResultDetailDO::getItemCode, item.getKey()).last("LIMIT 1"));
            if (detail == null) {
                detail = new ScreeningResultDetailDO(); detail.setRecordId(record.getId());
                detail.setItemCode(item.getKey()); detail.setCreateTime(LocalDateTime.now());
            }
            detail.setItemValue(item.getValue());
            detail.setIsAbnormal(isAbnormalText(item.getValue()) ? 1 : 0);
            detail.setDeptId(request.getDeptId()); detail.setCheckerId(request.getCheckerId());
            detail.setCheckTime(LocalDateTime.now());
            if (detail.getId() == null) screeningResultDetailMapper.insert(detail); else screeningResultDetailMapper.updateById(detail);
        }
        finalizeScreening(record);
        if (newRecord) screeningBatchMapper.update(null, Wrappers.<ScreeningBatchDO>lambdaUpdate()
                .eq(ScreeningBatchDO::getId, request.getBatchId()).setSql("actual_count = actual_count + 1")
                .set(ScreeningBatchDO::getBatchStatus, 2));
        return record.getId();
    }

    @Override
    @Transactional
    public Long scanCreateScreening(String qrCode, Long batchId) {
        if (screeningBatchMapper.selectById(batchId) == null) throw error("筛查批次不存在");
        QrCodeManagementDO code = qrCodeManagementMapper.selectOne(Wrappers.<QrCodeManagementDO>lambdaQuery()
                .and(query -> query.eq(QrCodeManagementDO::getQrcodeNo, qrCode)
                        .or().eq(QrCodeManagementDO::getQrcodeContent, qrCode)).last("LIMIT 1"));
        if (code == null || code.getStudentId() == null) throw error("筛查二维码不存在或未绑定学生");
        if (code.getBatchId() != null && !batchId.equals(code.getBatchId())) throw error("二维码不属于当前筛查批次");
        ScreeningRecordDO existing = screeningRecordMapper.selectOne(Wrappers.<ScreeningRecordDO>lambdaQuery()
                .eq(ScreeningRecordDO::getBatchId, batchId).eq(ScreeningRecordDO::getStudentId, code.getStudentId()).last("LIMIT 1"));
        if (existing != null) return existing.getId();
        ScreeningRecordDO record = new ScreeningRecordDO(); record.setRecordNo("SCREEN-" + IdWorker.getId());
        record.setBatchId(batchId); record.setStudentId(code.getStudentId()); record.setScreeningDate(LocalDate.now());
        record.setCheckStatus(1); record.setHasPositive(0); record.setPositiveItems("{}");
        record.setCreateTime(LocalDateTime.now()); record.setUpdateTime(LocalDateTime.now()); screeningRecordMapper.insert(record);
        code.setIsUsed(true); qrCodeManagementMapper.updateById(code); return record.getId();
    }

    @Override
    @Transactional
    public void submitScreeningRecord(Long recordId) {
        ScreeningRecordDO record = screeningRecordMapper.selectById(recordId);
        if (record == null) throw error("筛查记录不存在");
        if (!Integer.valueOf(1).equals(record.getCheckStatus())) throw error("只有进行中的筛查记录可以提交");
        if (screeningResultDetailMapper.selectCount(Wrappers.<ScreeningResultDetailDO>lambdaQuery()
                .eq(ScreeningResultDetailDO::getRecordId, recordId)) == 0) throw error("请先录入筛查项目结果");
        finalizeScreening(record);
    }

    @Override
    public ScreeningRecordResponse getScreeningRecord(Long id) {
        ScreeningRecordDO record = screeningRecordMapper.selectById(id);
        if (record == null) throw error("筛查记录不存在");
        return toScreeningResponse(record);
    }

    @Override
    @Transactional
    public void auditScreening(ScreeningAuditRequest request) {
        if (screeningRecordMapper.update(null, Wrappers.<ScreeningRecordDO>lambdaUpdate()
                .eq(ScreeningRecordDO::getId, request.getRecordId())
                .eq(ScreeningRecordDO::getCheckStatus, 2)
                .set(ScreeningRecordDO::getCheckStatus, request.getApproved() ? 3 : 1)
                .set(ScreeningRecordDO::getAuditDoctor, request.getAuditDoctor())
                .set(ScreeningRecordDO::getAuditTime, LocalDateTime.now())) == 0) {
            throw error("筛查记录不存在或已审核");
        }
    }

    @Override
    @Transactional
    public void batchAuditScreening(ScreeningBatchAuditRequest request) {
        if (request.getRecordIds().isEmpty()) throw error("批量审核记录不能为空");
        for (Long id : request.getRecordIds()) {
            ScreeningAuditRequest item = new ScreeningAuditRequest(); item.setRecordId(id);
            item.setApproved(request.getApproved()); item.setAuditDoctor(request.getAuditDoctor()); auditScreening(item);
        }
    }

    @Override
    public List<ScreeningRecordResponse> getPositiveScreenings(Long batchId) {
        return screeningRecordMapper.selectList(Wrappers.<ScreeningRecordDO>lambdaQuery()
                        .eq(batchId != null, ScreeningRecordDO::getBatchId, batchId)
                        .eq(ScreeningRecordDO::getHasPositive, 1)
                        .orderByDesc(ScreeningRecordDO::getScreeningDate))
                .stream().map(this::toScreeningResponse).toList();
    }

    @Override
    @Transactional
    public void recheckScreening(RecheckRequest request) {
        ScreeningPositiveDO positive = screeningPositiveMapper.selectById(request.getPositiveId());
        if (positive == null || !Boolean.TRUE.equals(positive.getNeedRecheck())) throw error("阳性记录不存在或无需复筛");
        Map<String, String> results = new LinkedHashMap<>();
        results.put("BODY_SHAPE", request.getBodyShapeResult());
        results.put("VISION", request.getVisionResult());
        results.put("ORAL", request.getOralResult());
        results.put("SCOLIOSIS", request.getScoliosisResult());
        results.put("PSYCHOLOGICAL", request.getPsychologicalResult());
        boolean stillPositive = results.values().stream().anyMatch(this::isAbnormalText);
        RecheckRecordDO record = new RecheckRecordDO();
        record.setPositiveId(positive.getId());
        record.setStudentId(positive.getStudentId());
        record.setInitialRecordId(positive.getRecordId());
        record.setRecheckDate(request.getRecheckDate());
        record.setRecheckItems(positive.getPositiveItems());
        record.setRecheckResult(JsonUtils.toJsonString(results));
        record.setIsStillPositive(stillPositive ? 1 : 0);
        record.setRecheckConclusion(request.getRecheckConclusion());
        record.setFollowPlan(request.getFollowPlan());
        record.setDoctorId(request.getDoctorId());
        record.setCreateTime(LocalDateTime.now());
        recheckRecordMapper.insert(record);
        positive.setRecheckStatus(2);
        screeningPositiveMapper.updateById(positive);
    }

    @Override
    @Transactional
    public List<ScreeningQrResponse> generateScreeningQrs(ScreeningQrBatchRequest request) {
        if (screeningBatchMapper.selectById(request.getBatchId()) == null) throw error("筛查批次不存在");
        List<ScreeningQrResponse> responses = new ArrayList<>();
        for (Long studentId : request.getStudentIds().stream().distinct().toList()) {
            StudentInfoDO student = studentInfoMapper.selectById(studentId);
            if (student == null || !Integer.valueOf(1).equals(student.getStatus())) throw error("学生不存在或不在读：" + studentId);
            QrCodeManagementDO code = qrCodeManagementMapper.selectOne(Wrappers.<QrCodeManagementDO>lambdaQuery()
                    .eq(QrCodeManagementDO::getBatchId, request.getBatchId())
                    .eq(QrCodeManagementDO::getStudentId, studentId).last("LIMIT 1"));
            if (code == null) {
                code = new QrCodeManagementDO(); code.setQrcodeNo("QR-" + IdWorker.getId());
                code.setBatchId(request.getBatchId()); code.setStudentId(studentId);
                String content = screeningQrContent(request.getBatchId(), studentId);
                code.setQrcodeContent(content);
                // ponytail: 后端保存稳定内容，二维码图片由现有前端打印组件渲染。
                code.setQrcodeUrl(content); code.setPrintStatus(0); code.setIsUsed(false);
                code.setCreateTime(LocalDateTime.now()); qrCodeManagementMapper.insert(code);
            }
            ScreeningQrResponse response = new ScreeningQrResponse(); BeanUtils.copyProperties(code, response);
            responses.add(response);
        }
        return responses;
    }

    @Override
    @Transactional
    public void markScreeningQrsPrinted(List<Long> ids) {
        if (ids == null || ids.isEmpty()) throw error("二维码ID不能为空");
        List<Long> distinctIds = ids.stream().distinct().toList();
        int updated = qrCodeManagementMapper.update(null, Wrappers.<QrCodeManagementDO>lambdaUpdate()
                .in(QrCodeManagementDO::getId, distinctIds).set(QrCodeManagementDO::getPrintStatus, 1)
                .set(QrCodeManagementDO::getPrintTime, LocalDateTime.now()));
        if (updated != distinctIds.size()) throw error("部分二维码不存在");
    }

    @Override
    @Transactional
    public Long notifyRecheck(Long positiveId, RecheckNotifyRequest request) {
        ScreeningPositiveDO positive = screeningPositiveMapper.selectById(positiveId);
        if (positive == null || !Boolean.TRUE.equals(positive.getNeedRecheck())) throw error("阳性记录不存在或无需复筛");
        if (Integer.valueOf(2).equals(positive.getRecheckStatus())) throw error("复筛已完成");
        if (Integer.valueOf(1).equals(positive.getRecheckStatus())) throw error("复筛通知已发送，请使用消息补发接口");
        StudentInfoDO student = studentInfoMapper.selectById(positive.getStudentId());
        if (student == null || student.getGuardianMobile() == null || student.getGuardianMobile().isBlank()) {
            throw error("学生监护人手机号未维护");
        }
        MessagePushRequest message = new MessagePushRequest(); message.setPushType(2); message.setPushChannel(1);
        message.setTargetType(3); message.setTargetId(student.getId()); message.setMobile(student.getGuardianMobile());
        message.setPushContent(request.getNoticeContent()); Long messageId = opsService.queueMessage(message);
        positive.setRecheckStatus(1); screeningPositiveMapper.updateById(positive); return messageId;
    }

    @Override
    public List<RecheckTimelineResponse> getRecheckTimeline(Long studentId) {
        if (studentInfoMapper.selectById(studentId) == null) throw error("学生不存在");
        List<RecheckTimelineResponse> timeline = new ArrayList<>();
        for (ScreeningPositiveDO positive : screeningPositiveMapper.selectList(Wrappers.<ScreeningPositiveDO>lambdaQuery()
                .eq(ScreeningPositiveDO::getStudentId, studentId).orderByDesc(ScreeningPositiveDO::getCreateTime))) {
            List<RecheckRecordDO> records = recheckRecordMapper.selectList(Wrappers.<RecheckRecordDO>lambdaQuery()
                    .eq(RecheckRecordDO::getPositiveId, positive.getId()).orderByDesc(RecheckRecordDO::getRecheckDate));
            if (records.isEmpty()) timeline.add(toRecheckTimeline(positive, null));
            else records.forEach(record -> timeline.add(toRecheckTimeline(positive, record)));
        }
        return timeline;
    }

    @Override
    @Transactional
    public Long createReferral(ReferralCreateRequest request) {
        ScreeningPositiveDO positive = screeningPositiveMapper.selectById(request.getPositiveId());
        if (positive == null) throw error("阳性记录不存在");
        if (request.getReferralItems() != null && !request.getReferralItems().isBlank()
                && !JsonUtils.isJson(request.getReferralItems())) throw error("转介项目必须是合法JSON");
        if (referralRecordMapper.selectCount(Wrappers.<ReferralRecordDO>lambdaQuery()
                .eq(ReferralRecordDO::getPositiveId, request.getPositiveId())
                .in(ReferralRecordDO::getReferralStatus, 1, 2)) > 0) throw error("该阳性记录已有处理中转介");
        ReferralRecordDO referral = new ReferralRecordDO(); BeanUtils.copyProperties(request, referral);
        referral.setReferralNo("REF-" + IdWorker.getId()); referral.setStudentId(positive.getStudentId());
        referral.setReferralStatus(1); referral.setCreateTime(LocalDateTime.now()); referralRecordMapper.insert(referral);
        return referral.getId();
    }

    @Override
    @Transactional
    public void receiveReferral(Long id) {
        if (referralRecordMapper.update(null, Wrappers.<ReferralRecordDO>lambdaUpdate().eq(ReferralRecordDO::getId, id)
                .eq(ReferralRecordDO::getReferralStatus, 1).set(ReferralRecordDO::getReferralStatus, 2)) == 0) {
            throw error("转介记录不存在或不可接收");
        }
    }

    @Override
    @Transactional
    public void completeReferral(Long id, String feedbackContent) {
        if (feedbackContent == null || feedbackContent.isBlank()) throw error("转介反馈不能为空");
        if (referralRecordMapper.update(null, Wrappers.<ReferralRecordDO>lambdaUpdate().eq(ReferralRecordDO::getId, id)
                .eq(ReferralRecordDO::getReferralStatus, 2).set(ReferralRecordDO::getReferralStatus, 3)
                .set(ReferralRecordDO::getFeedbackContent, feedbackContent)) == 0) throw error("转介记录不存在或尚未接收");
    }

    @Override
    public List<ReferralResponse> getReferrals(Long studentId, Integer status) {
        return referralRecordMapper.selectList(Wrappers.<ReferralRecordDO>lambdaQuery()
                .eq(studentId != null, ReferralRecordDO::getStudentId, studentId)
                .eq(status != null, ReferralRecordDO::getReferralStatus, status)
                .orderByDesc(ReferralRecordDO::getCreateTime)).stream().map(item -> {
                    ReferralResponse response = new ReferralResponse(); BeanUtils.copyProperties(item, response); return response;
                }).toList();
    }

    @Override
    @Transactional
    public Long createTransfer(TransferCreateRequest request) {
        requireChild(request.getChildId());
        if (request.getTransferType() < 1 || request.getTransferType() > 2) throw error("转档类型只能为1转入或2转出");
        if (request.getCaseList() != null && !request.getCaseList().isBlank()
                && !JsonUtils.isJson(request.getCaseList())) throw error("专案清单必须是合法JSON");
        if (request.getTransferType() == 1 && (request.getSourceHospital() == null || request.getSourceHospital().isBlank())) {
            throw error("转入档案必须填写来源医院");
        }
        if (request.getTransferType() == 2 && (request.getTargetHospital() == null || request.getTargetHospital().isBlank())) {
            throw error("转出档案必须填写目标医院");
        }
        TransferArchiveDO transfer = new TransferArchiveDO(); BeanUtils.copyProperties(request, transfer);
        transfer.setTransferNo("TRANSFER-" + IdWorker.getId()); transfer.setStatus(1);
        transfer.setCreateTime(LocalDateTime.now()); transferArchiveMapper.insert(transfer); return transfer.getId();
    }

    @Override
    @Transactional
    public void completeTransfer(Long id, String feedbackContent) {
        TransferArchiveDO transfer = transferArchiveMapper.selectById(id);
        if (transfer == null || !Integer.valueOf(1).equals(transfer.getStatus())) throw error("转档记录不存在或已完成");
        transfer.setFeedbackContent(feedbackContent); transfer.setStatus(2); transferArchiveMapper.updateById(transfer);
        ChildInfoDO child = requireChild(transfer.getChildId()); child.setStatus(transfer.getTransferType() == 2 ? 2 : 1);
        child.setUpdateTime(LocalDateTime.now()); childInfoMapper.updateById(child);
    }

    @Override
    public List<TransferResponse> getTransfers(Long childId, Integer type, Integer status) {
        return transferArchiveMapper.selectList(Wrappers.<TransferArchiveDO>lambdaQuery()
                .eq(childId != null, TransferArchiveDO::getChildId, childId)
                .eq(type != null, TransferArchiveDO::getTransferType, type)
                .eq(status != null, TransferArchiveDO::getStatus, status)
                .orderByDesc(TransferArchiveDO::getTransferDate)).stream().map(item -> {
                    TransferResponse response = new TransferResponse(); BeanUtils.copyProperties(item, response); return response;
                }).toList();
    }

    private void saveGrowthCurve(HealthCheckupDO checkup, PhysicalExamRecordDO physical) {
        GrowthCurveDataDO curve = growthCurveDataMapper.selectOne(Wrappers.<GrowthCurveDataDO>lambdaQuery()
                .eq(GrowthCurveDataDO::getChildId, checkup.getChildId())
                .eq(GrowthCurveDataDO::getMeasureDate, checkup.getCheckupDate())
                .eq(GrowthCurveDataDO::getDataSource, 1).last("LIMIT 1"));
        if (curve == null) {
            curve = new GrowthCurveDataDO();
            curve.setChildId(checkup.getChildId()); curve.setMeasureDate(checkup.getCheckupDate());
            curve.setDataSource(1); curve.setCreateTime(LocalDateTime.now());
        }
        curve.setMonthAge(checkup.getAgeMonths()); curve.setHeight(physical.getHeight()); curve.setWeight(physical.getWeight());
        curve.setHeadCircumference(physical.getHeadCircumference()); curve.setBmi(physical.getBmi());
        if (curve.getId() == null) growthCurveDataMapper.insert(curve); else growthCurveDataMapper.updateById(curve);
    }

    private RecheckTimelineResponse toRecheckTimeline(ScreeningPositiveDO positive, RecheckRecordDO record) {
        RecheckTimelineResponse response = new RecheckTimelineResponse();
        response.setPositiveId(positive.getId()); response.setStudentId(positive.getStudentId());
        response.setRecheckStatus(positive.getRecheckStatus()); response.setPositiveItems(positive.getPositiveItems());
        if (record != null) {
            response.setRecheckId(record.getId()); response.setRecheckDate(record.getRecheckDate());
            response.setRecheckResult(record.getRecheckResult()); response.setIsStillPositive(Integer.valueOf(1).equals(record.getIsStillPositive()));
            response.setRecheckConclusion(record.getRecheckConclusion()); response.setFollowPlan(record.getFollowPlan());
        }
        return response;
    }

    private void refreshExamAbnormal(HealthCheckupDO checkup) {
        List<String> tags = new ArrayList<>();
        PhysicalExamRecordDO physical = physicalExamRecordMapper.selectOne(Wrappers.<PhysicalExamRecordDO>lambdaQuery()
                .eq(PhysicalExamRecordDO::getExamId, checkup.getId()).last("LIMIT 1"));
        EyeExamRecordDO eye = selectEye(checkup.getId());
        HearingExamRecordDO hearing = selectHearing(checkup.getId());
        OralExamRecordDO oral = selectOral(checkup.getId());
        // P2-19/P2-20 营养不良/肥胖：根据 growthAssessment 区分 GROWTH/OBESITY
        if (physical != null && Boolean.TRUE.equals(physical.getIsAbnormal())) {
            String assessment = physical.getGrowthAssessment();
            if (assessment != null && (assessment.contains("OVERWEIGHT")
                    || assessment.contains("OBESITY") || assessment.contains("SEVERE_OBESITY"))) {
                tags.add("OBESITY");
            } else {
                tags.add("GROWTH");
            }
        }
        // P2-22 佝偻病：扫描体检字段中的佝偻病体征关键词
        if (physical != null && containsRicketsSign(physical)) {
            tags.add("RICKETS");
        }
        if (eye != null && Boolean.TRUE.equals(eye.getIsAbnormal())) tags.add("EYE");
        if (hearing != null && Boolean.TRUE.equals(hearing.getIsAbnormal())) tags.add("HEARING");
        if (oral != null && Boolean.TRUE.equals(oral.getIsAbnormal())) tags.add("ORAL");
        // P2-21 贫血：扫描检验报告的异常项，识别 HGB<110
        List<LabReportDO> labReports = labReportMapper.selectList(Wrappers.<LabReportDO>lambdaQuery()
                .eq(LabReportDO::getExamId, checkup.getId()).eq(LabReportDO::getIsAbnormal, true));
        if (!labReports.isEmpty()) {
            tags.add("LAB");
            if (labReports.stream().anyMatch(this::isAnemiaReport)) {
                tags.add("ANEMIA");
            }
        }
        checkup.setIsAbnormal(!tags.isEmpty());
        checkup.setAbnormalItems(tags.isEmpty() ? null : JsonUtils.toJsonString(tags));
        checkup.setUpdatedAt(LocalDateTime.now());
        healthCheckupMapper.updateById(checkup);
        if (!tags.isEmpty()) saveCaseAlert(checkup, tags);
    }

    /** 佝偻病体征关键词检测：方颅/串珠/鸡胸/漏斗胸/O型腿/X型腿/手足镯/前囟过大/前囟闭合延迟 */
    private boolean containsRicketsSign(PhysicalExamRecordDO physical) {
        String[] keywords = {"方颅", "串珠", "鸡胸", "漏斗胸", "O型腿", "X型腿", "手足镯",
                "赫氏沟", "佝偻", "rickets", "前囟过大", "前囟闭合延迟"};
        String[] fields = {physical.getHeadShape(), physical.getFontanelle(), physical.getChestExam(),
                physical.getLimbsExam(), physical.getSpineExam(), physical.getGrowthAssessment()};
        for (String field : fields) {
            if (field == null || field.isBlank()) continue;
            for (String kw : keywords) {
                if (field.contains(kw)) return true;
            }
        }
        return false;
    }

    /** 贫血识别：检验报告异常项包含 HGB<110 或 血红蛋白 偏低 */
    private boolean isAnemiaReport(LabReportDO report) {
        String content = report.getReportContent();
        String items = report.getAbnormalItems();
        String name = report.getReportName();
        if (name != null && (name.contains("血常规") || name.toLowerCase().contains("blood"))) {
            // 检查异常项是否含血红蛋白/HGB 关键词
            String[] anemiaKeys = {"血红蛋白", "HGB", "Hb", "hemoglobin", "贫血"};
            for (String key : anemiaKeys) {
                if ((items != null && items.contains(key)) || (content != null && content.contains(key))) {
                    return true;
                }
            }
        }
        return false;
    }

    private void saveCaseAlert(HealthCheckupDO checkup, List<String> tags) {
        String alertType = tags.contains("GROWTH") ? "GROWTH_ABNORMAL" : "EXAM_ABNORMAL";
        if (caseAlertLogMapper.selectCount(Wrappers.<CaseAlertLogDO>lambdaQuery()
                .eq(CaseAlertLogDO::getExamId, checkup.getId()).eq(CaseAlertLogDO::getAlertType, alertType)) > 0) return;
        // P2-19~P2-23：根据 tag 选择主推专案类型（用于前端提示）
        String suggestCaseType;
        if (tags.contains("GROWTH")) suggestCaseType = "GROWTH_MANAGEMENT";
        else if (tags.contains("OBESITY")) suggestCaseType = "OBESITY";
        else if (tags.contains("ANEMIA")) suggestCaseType = "ANEMIA";
        else if (tags.contains("RICKETS")) suggestCaseType = "RICKETS";
        else if (tags.contains("DEVELOPMENTAL")) suggestCaseType = "DEVELOPMENTAL_DELAY";
        else if (tags.contains("EYE")) suggestCaseType = "EYE_HEALTH";
        else if (tags.contains("HEARING")) suggestCaseType = "HEARING_HEALTH";
        else if (tags.contains("ORAL")) suggestCaseType = "ORAL_HEALTH";
        else suggestCaseType = "SPECIALTY_FOLLOW_UP";
        CaseAlertLogDO alert = new CaseAlertLogDO();
        alert.setExamId(checkup.getId()); alert.setChildId(checkup.getChildId()); alert.setAlertType(alertType);
        alert.setAlertContent("儿童健康检查发现异常：" + String.join("、", tags));
        alert.setSuggestCaseType(suggestCaseType);
        alert.setIsHandled(false); alert.setCreateTime(LocalDateTime.now());
        caseAlertLogMapper.insert(alert);

        // P1-3 需求16▲ 异常自动建专案：按异常类型映射专案类型并自动建专案
        autoCreateCasesForAlert(checkup, tags, alert);
    }

    /**
     * 需求16▲：异常自动建专案
     * 根据异常 tags 映射到对应的专案类型（眼/听力/口腔/生长发育/营养等），自动建专案。
     * 已存在同类型进行中专案时跳过。
     */
    private void autoCreateCasesForAlert(HealthCheckupDO checkup, List<String> tags, CaseAlertLogDO alert) {
        // P2-18~P2-23：6 类专案自动建案 tag 映射
        Map<String, String> tagToCaseType = new LinkedHashMap<>();
        tagToCaseType.put("GROWTH", "GROWTH_MANAGEMENT");
        tagToCaseType.put("OBESITY", "OBESITY");
        tagToCaseType.put("ANEMIA", "ANEMIA");
        tagToCaseType.put("RICKETS", "RICKETS");
        tagToCaseType.put("DEVELOPMENTAL", "DEVELOPMENTAL_DELAY");
        tagToCaseType.put("EYE", "EYE_HEALTH");
        tagToCaseType.put("HEARING", "HEARING_HEALTH");
        tagToCaseType.put("ORAL", "ORAL_HEALTH");
        tagToCaseType.put("LAB", "SPECIALTY_FOLLOW_UP");

        List<String> createdCases = new ArrayList<>();
        for (String tag : tags) {
            String caseTypeCode = tagToCaseType.get(tag);
            if (caseTypeCode == null) continue;

            // 1. 查找专案类型配置
            CaseTypeConfigDO caseType = caseTypeConfigMapper.selectOne(Wrappers.<CaseTypeConfigDO>lambdaQuery()
                    .eq(CaseTypeConfigDO::getTypeCode, caseTypeCode)
                    .eq(CaseTypeConfigDO::getStatus, 1).last("LIMIT 1"));
            if (caseType == null) continue;

            // 2. 校验是否已有同类型进行中专案
            long activeCount = caseRegistrationMapper.selectCount(Wrappers.<CaseRegistrationDO>lambdaQuery()
                    .eq(CaseRegistrationDO::getChildId, checkup.getChildId())
                    .eq(CaseRegistrationDO::getCaseTypeId, caseType.getId())
                    .eq(CaseRegistrationDO::getCaseStatus, 1));
            if (activeCount > 0) continue;

            // 3. 自动建专案
            try {
                CaseCreateRequest create = new CaseCreateRequest();
                create.setChildId(checkup.getChildId());
                create.setCaseTypeId(caseType.getId());
                create.setCaseSource(1); // 1自动
                create.setInitialDiagnosis("自动建专案：" + tag + " 异常");
                create.setCaseLevel(2);
                Long caseId = openCase(create);
                createdCases.add(caseType.getTypeName() + "=" + caseId);
            } catch (Exception e) {
                // 自动建专案失败不影响主流程
                // ponytail: 记录日志便于排查
                System.err.println("[autoCreateCasesForAlert] 自动建专案失败 childId=" + checkup.getChildId()
                        + " caseType=" + caseTypeCode + " err=" + e.getMessage());
            }
        }

        // 4. 回写处理结果
        if (!createdCases.isEmpty()) {
            alert.setIsHandled(true);
            alert.setHandleResult("已自动建专案：" + String.join("，", createdCases));
            caseAlertLogMapper.updateById(alert);
        }
    }

    private void syncHighRiskLedger(ChildInfoDO child) {
        boolean highRisk = hasHighRiskTags(child.getHighRiskTags());
        HighRiskNewbornDO ledger = highRiskNewbornMapper.selectOne(Wrappers.<HighRiskNewbornDO>lambdaQuery()
                .eq(HighRiskNewbornDO::getChildId, child.getId()).last("LIMIT 1"));
        if (!highRisk && ledger == null) return;
        if (ledger == null) {
            ledger = new HighRiskNewbornDO(); ledger.setChildId(child.getId()); ledger.setIsRegistered(0);
            ledger.setIsFollowed(false); ledger.setCreateTime(LocalDateTime.now());
        }
        ledger.setRiskFactors(child.getHighRiskTags()); ledger.setAlertStatus(highRisk ? 1 : 0);
        if (ledger.getId() == null) highRiskNewbornMapper.insert(ledger); else highRiskNewbornMapper.updateById(ledger);
        if (!highRisk) {
            caseAlertLogMapper.update(null, Wrappers.<CaseAlertLogDO>lambdaUpdate()
                    .eq(CaseAlertLogDO::getChildId, child.getId()).eq(CaseAlertLogDO::getAlertType, "HIGH_RISK")
                    .eq(CaseAlertLogDO::getIsHandled, false).set(CaseAlertLogDO::getIsHandled, true)
                    .set(CaseAlertLogDO::getHandleResult, "高危标签已移除"));
            followTaskMapper.update(null, Wrappers.<FollowTaskDO>lambdaUpdate()
                    .eq(FollowTaskDO::getTaskSource, "HIGH_RISK:" + ledger.getId())
                    .in(FollowTaskDO::getTaskStatus, 1, 2).set(FollowTaskDO::getTaskStatus, 4)
                    .set(FollowTaskDO::getCompleteTime, LocalDateTime.now()));
            return;
        }
        if (caseAlertLogMapper.selectCount(Wrappers.<CaseAlertLogDO>lambdaQuery()
                .eq(CaseAlertLogDO::getChildId, child.getId()).eq(CaseAlertLogDO::getAlertType, "HIGH_RISK")
                .eq(CaseAlertLogDO::getIsHandled, false)) == 0) {
            CaseAlertLogDO alert = new CaseAlertLogDO(); alert.setChildId(child.getId()); alert.setAlertType("HIGH_RISK");
            alert.setAlertContent("儿童高危标签待建专案：" + child.getHighRiskTags()); alert.setSuggestCaseType("HIGH_RISK");
            alert.setIsHandled(false); alert.setCreateTime(LocalDateTime.now()); caseAlertLogMapper.insert(alert);
        }
        ensureFollowTask(child.getId(), 3, "HIGH_RISK:" + ledger.getId(),
                "高危儿童待随访：" + child.getHighRiskTags(), LocalDate.now(), null);
    }

    private void createCaseCard(CaseRegistrationDO item, CaseTypeConfigDO type) {
        Map<String, Object> content = new LinkedHashMap<>();
        content.put("caseId", item.getId()); content.put("caseNo", item.getCaseNo());
        content.put("childId", item.getChildId()); content.put("caseType", type.getTypeCode());
        content.put("initialDiagnosis", item.getInitialDiagnosis()); content.put("registrationDate", item.getRegistrationDate());
        if (type.getCaseCardTemplate() != null && JsonUtils.isJson(type.getCaseCardTemplate())) {
            content.put("template", JsonUtils.parseTree(type.getCaseCardTemplate()));
        }
        CaseCardDO card = new CaseCardDO(); card.setCaseId(item.getId()); card.setCardContent(JsonUtils.toJsonString(content));
        card.setCreateTime(LocalDateTime.now()); card.setUpdateTime(LocalDateTime.now()); caseCardMapper.insert(card);
    }

    private void markHighRiskRegistered(Long childId) {
        highRiskNewbornMapper.update(null, Wrappers.<HighRiskNewbornDO>lambdaUpdate()
                .eq(HighRiskNewbornDO::getChildId, childId).set(HighRiskNewbornDO::getIsRegistered, 1)
                .set(HighRiskNewbornDO::getRegisterDate, LocalDate.now()).set(HighRiskNewbornDO::getAlertStatus, 0));
    }

    private void ensureFollowTask(Long childId, Integer type, String source, String content,
            LocalDate planDate, Long doctorId) {
        if (followTaskMapper.selectCount(Wrappers.<FollowTaskDO>lambdaQuery()
                .eq(FollowTaskDO::getTaskSource, source).in(FollowTaskDO::getTaskStatus, 1, 2)) > 0) return;
        FollowTaskDO task = new FollowTaskDO(); task.setTaskNo("FOLLOW-" + IdWorker.getId()); task.setChildId(childId);
        task.setTaskType(type); task.setTaskSource(source); task.setTaskContent(content); task.setPriority(type == 2 ? 1 : 2);
        task.setPlanDate(planDate); task.setResponsibleDoctor(doctorId); task.setTaskStatus(1);
        task.setCreateTime(LocalDateTime.now()); followTaskMapper.insert(task);
    }

    private void finishFollowTask(String source) {
        followTaskMapper.update(null, Wrappers.<FollowTaskDO>lambdaUpdate()
                .eq(FollowTaskDO::getTaskSource, source).in(FollowTaskDO::getTaskStatus, 1, 2)
                .set(FollowTaskDO::getTaskStatus, 3).set(FollowTaskDO::getCompleteTime, LocalDateTime.now()));
    }

    private void completeExamReminders(HealthCheckupDO checkup) {
        List<ExamReminderLogDO> reminders = examReminderLogMapper.selectList(Wrappers.<ExamReminderLogDO>lambdaQuery()
                .eq(ExamReminderLogDO::getChildId, checkup.getChildId())
                .eq(ExamReminderLogDO::getExamType, Integer.valueOf(checkup.getCheckupType()))
                .le(ExamReminderLogDO::getDueDate, checkup.getCheckupDate())
                .eq(ExamReminderLogDO::getIsCompleted, false));
        for (ExamReminderLogDO reminder : reminders) {
            reminder.setIsCompleted(true); reminder.setPushStatus(2); reminder.setResponseTime(LocalDateTime.now());
            examReminderLogMapper.updateById(reminder); finishFollowTask("EXAM_REMINDER:" + reminder.getId());
        }
    }

    static boolean hasHighRiskTags(String tags) {
        return tags != null && !tags.isBlank() && !"[]".equals(tags.trim());
    }

    static boolean validRecovery(Integer level, Boolean qualified) {
        return !Boolean.TRUE.equals(qualified) || Integer.valueOf(3).equals(level);
    }

    static String screeningQrContent(Long batchId, Long studentId) {
        return "childhealth://screening/" + batchId + "/student/" + studentId;
    }

    private CaseTypeConfigDO requireCaseType(Long id) {
        CaseTypeConfigDO type = caseTypeConfigMapper.selectById(id);
        if (type == null || !Integer.valueOf(1).equals(type.getStatus())) throw error("专案类型不存在或已停用");
        return type;
    }

    private CaseRegistrationDO requireCase(Long id) {
        CaseRegistrationDO item = caseRegistrationMapper.selectById(id);
        if (item == null) throw error("专案不存在");
        return item;
    }

    private EyeExamRecordDO selectEye(Long examId) {
        return eyeExamRecordMapper.selectOne(Wrappers.<EyeExamRecordDO>lambdaQuery()
                .eq(EyeExamRecordDO::getExamId, examId).last("LIMIT 1"));
    }

    private HearingExamRecordDO selectHearing(Long examId) {
        return hearingExamRecordMapper.selectOne(Wrappers.<HearingExamRecordDO>lambdaQuery()
                .eq(HearingExamRecordDO::getExamId, examId).last("LIMIT 1"));
    }

    private OralExamRecordDO selectOral(Long examId) {
        return oralExamRecordMapper.selectOne(Wrappers.<OralExamRecordDO>lambdaQuery()
                .eq(OralExamRecordDO::getExamId, examId).last("LIMIT 1"));
    }

    private boolean existsPhysical(Long examId) {
        return physicalExamRecordMapper.selectCount(Wrappers.<PhysicalExamRecordDO>lambdaQuery()
                .eq(PhysicalExamRecordDO::getExamId, examId)) > 0;
    }

    static String nextVisitPlan(Integer ageMonths) {
        for (int next : new int[]{1, 3, 6, 8, 12, 18, 24, 30, 36, 48, 60, 72}) {
            if (next > ageMonths) return "建议于" + next + "月龄进行下一次儿童健康检查";
        }
        return "已完成0-6岁儿童健康检查周期，按需复诊";
    }

    static boolean hasReportPayload(String content, String fileUrl) {
        return content != null && !content.isBlank() || fileUrl != null && !fileUrl.isBlank();
    }

    private void finalizeScreening(ScreeningRecordDO record) {
        List<ScreeningResultDetailDO> details = screeningResultDetailMapper.selectList(
                Wrappers.<ScreeningResultDetailDO>lambdaQuery().eq(ScreeningResultDetailDO::getRecordId, record.getId()));
        Map<String, String> values = new LinkedHashMap<>();
        Map<String, String> abnormalItems = new LinkedHashMap<>();
        for (ScreeningResultDetailDO detail : details) {
            values.put(detail.getItemCode(), detail.getItemValue());
            if (Boolean.TRUE.equals(detail.getIsAbnormal())) abnormalItems.put(detail.getItemCode(), detail.getItemValue());
        }
        List<ScreeningPositiveRuleService.Match> matches = screeningPositiveRuleService.match(values);
        screeningPositiveMapper.delete(Wrappers.<ScreeningPositiveDO>lambdaQuery()
                .eq(ScreeningPositiveDO::getRecordId, record.getId()));
        for (ScreeningPositiveRuleService.Match match : matches) {
            ScreeningPositiveDO positive = new ScreeningPositiveDO();
            positive.setRecordId(record.getId()); positive.setStudentId(record.getStudentId());
            positive.setDiseaseCode(match.rule().getDiseaseCode());
            positive.setDiseaseName(match.knowledge() == null ? match.rule().getRuleName() : match.knowledge().getDiseaseName());
            positive.setPositiveLevel(match.rule().getPositiveLevel() == null ? 1 : match.rule().getPositiveLevel());
            positive.setPositiveItems(JsonUtils.toJsonString(abnormalItems.isEmpty() ? values : abnormalItems));
            positive.setHealthGuidance(healthGuidance(match.knowledge()));
            positive.setNeedRecheck(!Boolean.FALSE.equals(match.rule().getNeedRecheck()) ? 1 : 0);
            positive.setRecheckStatus(0); positive.setCreateTime(LocalDateTime.now());
            screeningPositiveMapper.insert(positive);
        }
        if (matches.isEmpty() && !abnormalItems.isEmpty()) {
            // ponytail: 保留人工异常兜底；医院配置完全部阳性规则后可删除。
            ScreeningPositiveDO positive = new ScreeningPositiveDO(); positive.setRecordId(record.getId());
            positive.setStudentId(record.getStudentId()); positive.setDiseaseName("待总检判定");
            positive.setPositiveLevel(Math.min(3, abnormalItems.size()));
            positive.setPositiveItems(JsonUtils.toJsonString(abnormalItems)); positive.setNeedRecheck(1);
            positive.setRecheckStatus(0); positive.setCreateTime(LocalDateTime.now()); screeningPositiveMapper.insert(positive);
        }
        boolean positive = !matches.isEmpty() || !abnormalItems.isEmpty();
        record.setHasPositive(positive ? 1 : 0);
        record.setPositiveItems(positive ? JsonUtils.toJsonString(abnormalItems.isEmpty() ? values : abnormalItems) : "{}");
        record.setCheckStatus(2); record.setUpdateTime(LocalDateTime.now()); screeningRecordMapper.updateById(record);
    }

    private String healthGuidance(DiseaseKnowledgeDO knowledge) {
        if (knowledge == null) return null;
        List<String> parts = new ArrayList<>();
        if (knowledge.getIndicatorExplain() != null) parts.add("指标解读：" + knowledge.getIndicatorExplain());
        if (knowledge.getHomeCare() != null) parts.add("居家护理：" + knowledge.getHomeCare());
        if (knowledge.getMedicalAdvice() != null) parts.add("就诊指导：" + knowledge.getMedicalAdvice());
        if (knowledge.getRiskWarning() != null) parts.add("风险提示：" + knowledge.getRiskWarning());
        return String.join("\n", parts);
    }

    private Map<String, String> screeningItems(ScreeningRecordCreateRequest request, boolean abnormalOnly) {
        Map<String, String> items = new LinkedHashMap<>();
        if (!abnormalOnly && request.getHeight() != null) items.put("HEIGHT", request.getHeight().toPlainString());
        if (!abnormalOnly && request.getWeight() != null) items.put("WEIGHT", request.getWeight().toPlainString());
        if (!abnormalOnly && request.getHeight() != null && request.getWeight() != null
                && request.getHeight().signum() > 0) {
            items.put("BMI", request.getWeight().multiply(java.math.BigDecimal.valueOf(10000))
                    .divide(request.getHeight().multiply(request.getHeight()), 2, RoundingMode.HALF_UP).toPlainString());
        }
        putScreeningItem(items, "BODY_SHAPE", request.getBodyShapeResult(), abnormalOnly);
        putScreeningItem(items, "VISION", request.getVisionResult(), abnormalOnly);
        putScreeningItem(items, "ORAL", request.getOralResult(), abnormalOnly);
        putScreeningItem(items, "SCOLIOSIS", request.getScoliosisResult(), abnormalOnly);
        putScreeningItem(items, "PSYCHOLOGICAL", request.getPsychologicalResult(), abnormalOnly);
        return items;
    }

    private void putScreeningItem(Map<String, String> items, String code, String value, boolean abnormalOnly) {
        if (value != null && (!abnormalOnly || isAbnormalText(value))) items.put(code, value);
    }

    private boolean isAbnormalText(String value) {
        return "ABNORMAL".equalsIgnoreCase(value) || "FAIL".equalsIgnoreCase(value)
                || "未通过".equals(value) || "异常".equals(value);
    }

    private boolean hasAbnormalAssessment(String value) {
        return value != null && !value.isBlank()
                && !List.of("NORMAL", "PASS", "正常", "通过", "达标", "无", "良好", "正常发育", "未见异常", "未见明显异常")
                .contains(value.trim().toUpperCase());
    }

    private ChildResponse toChildResponse(ChildInfoDO child) {
        ChildResponse response = new ChildResponse();
        BeanUtils.copyProperties(child, response);
        return response;
    }

    private void validateChildFields(String gender, String highRiskTags) {
        if (!"1".equals(gender) && !"2".equals(gender)) throw error("性别只能为1男或2女");
        if (highRiskTags != null && !highRiskTags.isBlank() && !JsonUtils.isJson(highRiskTags)) {
            throw error("高危标签必须是合法JSON数组");
        }
    }

    private void savePrimaryGuardian(Long childId, String name, String phone) {
        if (name == null || name.isBlank() || phone == null || phone.isBlank()) return;
        GuardianInfoDO guardian = new GuardianInfoDO();
        guardian.setChildId(childId);
        guardian.setRelation(3);
        guardian.setGuardianName(name);
        guardian.setGuardianPhone(phone);
        guardian.setIsPrimary(1);
        guardian.setCreateTime(LocalDateTime.now());
        guardianInfoMapper.insert(guardian);
    }

    private void attachPrimaryGuardian(ChildInfoDO child) {
        GuardianInfoDO guardian = guardianInfoMapper.selectOne(Wrappers.<GuardianInfoDO>lambdaQuery()
                .eq(GuardianInfoDO::getChildId, child.getId()).eq(GuardianInfoDO::getIsPrimary, true)
                .last("LIMIT 1"));
        if (guardian != null) {
            child.setParentName(guardian.getGuardianName());
            child.setParentPhone(guardian.getGuardianPhone());
        }
    }

    private CheckupResponse toCheckupResponse(HealthCheckupDO checkup) {
        CheckupResponse response = new CheckupResponse();
        BeanUtils.copyProperties(checkup, response);
        PhysicalExamRecordDO physical = physicalExamRecordMapper.selectOne(Wrappers.<PhysicalExamRecordDO>lambdaQuery()
                .eq(PhysicalExamRecordDO::getExamId, checkup.getId()).last("LIMIT 1"));
        if (physical != null) {
            response.setHeight(physical.getHeight());
            response.setWeight(physical.getWeight());
            response.setNutritionStatus(physical.getGrowthAssessment());
        }
        return response;
    }

    private ScreeningRecordResponse toScreeningResponse(ScreeningRecordDO record) {
        ScreeningRecordResponse response = new ScreeningRecordResponse();
        BeanUtils.copyProperties(record, response);
        response.setOverallResult(Integer.valueOf(1).equals(record.getHasPositive()) ? "ABNORMAL" : "NORMAL");
        response.setReviewStatus(String.valueOf(record.getCheckStatus()));
        response.setRescreenNeeded(Integer.valueOf(1).equals(record.getHasPositive()));
        List<ScreeningResultDetailDO> details = screeningResultDetailMapper.selectList(
                Wrappers.<ScreeningResultDetailDO>lambdaQuery().eq(ScreeningResultDetailDO::getRecordId, record.getId()));
        int abnormalCount = 0;
        for (ScreeningResultDetailDO detail : details) {
            if (Boolean.TRUE.equals(detail.getIsAbnormal())) abnormalCount++;
            switch (detail.getItemCode()) {
                case "HEIGHT" -> response.setHeight(new java.math.BigDecimal(detail.getItemValue()));
                case "WEIGHT" -> response.setWeight(new java.math.BigDecimal(detail.getItemValue()));
                case "BMI" -> response.setBmi(new java.math.BigDecimal(detail.getItemValue()));
                case "BODY_SHAPE" -> response.setBodyShapeResult(detail.getItemValue());
                case "VISION" -> response.setVisionResult(detail.getItemValue());
                case "ORAL" -> response.setOralResult(detail.getItemValue());
                case "SCOLIOSIS" -> response.setScoliosisResult(detail.getItemValue());
                case "PSYCHOLOGICAL" -> response.setPsychologicalResult(detail.getItemValue());
            }
        }
        response.setAbnormalCount(abnormalCount);
        return response;
    }

    private HealthCheckupDO requireCheckup(Long id) {
        if (id == null) throw error("体检记录ID不能为空");
        HealthCheckupDO checkup = healthCheckupMapper.selectById(id);
        if (checkup == null) throw error("体检记录不存在");
        return checkup;
    }

    private HealthCheckupDO requireEditableCheckup(Long id) {
        HealthCheckupDO checkup = requireCheckup(id);
        if (!"1".equals(checkup.getReviewStatus())) throw error("只有进行中的体检记录可录入或修改");
        return checkup;
    }

    private ChildInfoDO requireChild(Long id) {
        ChildInfoDO child = childInfoMapper.selectById(id);
        if (child == null) throw error("儿童档案不存在");
        return child;
    }

    private ServiceException error(String message) {
        return new ServiceException(1_010_000_001, message);
    }

    private String getChildName(Long childId) {
        if (childId == null) return "未知儿童";
        cn.iocoder.yudao.module.childhealth.dal.dataobject.workflow.ChildInfoDO child = childInfoMapper.selectById(childId);
        return child != null && child.getChildName() != null ? child.getChildName() : "未知儿童";
    }

    @Override
    @Transactional
    public AbnormalDetectResponse detectAbnormal(Long examId) {
        HealthCheckupDO checkup = requireCheckup(examId);
        PhysicalExamRecordDO physical = physicalExamRecordMapper.selectOne(Wrappers.<PhysicalExamRecordDO>lambdaQuery()
                .eq(PhysicalExamRecordDO::getExamId, examId).last("LIMIT 1"));
        
        MatchRequest matchRequest = new MatchRequest();
        matchRequest.setExamId(examId);
        
        Map<String, BigDecimal> values = new LinkedHashMap<>();
        if (physical != null) {
            if (physical.getHeightSd() != null) values.put("heightSd", physical.getHeightSd());
            if (physical.getWeightSd() != null) values.put("weightSd", physical.getWeightSd());
            if (physical.getHeadSd() != null) values.put("headSd", physical.getHeadSd());
            if (physical.getBmi() != null) values.put("bmi", physical.getBmi());
        }
        matchRequest.setValues(values);
        
        List<cn.iocoder.yudao.module.childhealth.api.rule.dto.AbnormalRuleDTO.RuleResponse> matchedRules = abnormalRuleService.match(matchRequest);
        
        AbnormalDetectResponse response = new AbnormalDetectResponse();
        response.setExamId(examId);
        response.setHasAbnormal(!matchedRules.isEmpty());
        response.setAbnormalItems(matchedRules.stream().map(r -> r.getCheckItem()).distinct().toList());
        response.setMatchedRules(matchedRules.stream().map(r -> r.getRuleName()).distinct().toList());
        
        return response;
    }

    @Override
    @Transactional
    public Long autoGenerateMedicalRecord(Long examId) {
        MedicalRecordGenerateRequest request = new MedicalRecordGenerateRequest();
        return generateMedicalRecord(examId, request);
    }

    @Override
    @Transactional
    public Long receiveLisData(List<LisDataRequest> dataList) {
        if (dataList == null || dataList.isEmpty()) throw error("LIS数据不能为空");
        
        Long examId = dataList.get(0).getExamId();
        requireCheckup(examId);
        
        for (LisDataRequest data : dataList) {
            LabReportDO report = new LabReportDO();
            report.setExamId(data.getExamId());
            report.setReportType(3);
            report.setReportCode(data.getLabCode());
            report.setReportName(data.getLabName());
            report.setReportContent(data.getResult() + (data.getUnit() != null ? " " + data.getUnit() : "") +
                    (data.getReferenceRange() != null ? " 参考范围: " + data.getReferenceRange() : ""));
            report.setReportDate(data.getReportTime());
            report.setSource(data.getLabDepartment() != null ? 2 : 1);
            report.setIsAbnormal(data.getIsAbnormal());
            report.setAbnormalItems(data.getIsAbnormal() != null && data.getIsAbnormal() ? data.getLabName() : null);
            report.setCreateTime(LocalDateTime.now());
            labReportMapper.insert(report);
        }
        
        return examId;
    }

    @Override
    @Transactional
    public Long receivePacsData(PacsDataRequest data) {
        requireCheckup(data.getExamId());
        
        LabReportDO report = new LabReportDO();
        report.setExamId(data.getExamId());
        report.setReportType(4);
        report.setReportCode(data.getImagingType());
        report.setReportName(data.getImagingType() + "检查");
        report.setReportContent(data.getReportContent() != null ? data.getReportContent() : "");
        report.setReportUrl(data.getImageUrl());
        report.setReportDate(data.getExamDate());
        report.setSource(data.getImagingDepartment() != null ? 2 : 1);
        report.setIsAbnormal(data.getDiagnosis() != null && !data.getDiagnosis().contains("未见异常"));
        report.setAbnormalItems(report.getIsAbnormal() != null && report.getIsAbnormal() ? data.getImagingType() : null);
        report.setCreateTime(LocalDateTime.now());
        labReportMapper.insert(report);
        
        return data.getExamId();
    }
}

package cn.iocoder.yudao.module.childhealth.service.exam;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.exam.vo.*;
import cn.iocoder.yudao.module.childhealth.controller.admin.medical.vo.MedicalRecordTemplatePageReqVO;
import cn.iocoder.yudao.module.childhealth.controller.admin.medical.vo.MedicalRecordTemplateRespVO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.exam.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.medical.MedicalRecordTemplateDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.workflow.ChildInfoDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.exam.*;
import cn.iocoder.yudao.module.childhealth.dal.mysql.medical.MedicalRecordTemplateMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.workflow.ChildInfoMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.*;

/**
 * 体检业务 Service 实现类
 */
@Service
@Validated
@Slf4j
public class ExamServiceImpl implements ExamService {

    @Resource
    private ExamAppointmentMapper appointmentMapper;
    @Resource
    private ExamRecordMapper examRecordMapper;
    @Resource
    private PhysicalExamRecordMapper physicalExamMapper;
    @Resource
    private EyeExamRecordMapper eyeExamMapper;
    @Resource
    private HearingExamRecordMapper hearingExamMapper;
    @Resource
    private OralExamRecordMapper oralExamMapper;
    @Resource
    private LabReportMapper labReportMapper;
    @Resource
    private MedicalRecordTemplateMapper templateMapper;
    @Resource
    private HealthCarePlanMapper carePlanMapper;
    @Resource
    private ChildInfoMapper childInfoMapper;

    // ==================== 预约管理 ====================

    @Override
    public Long createAppointment(ExamAppointmentCreateReqVO reqVO) {
        // 参数校验
        if (reqVO.getChildId() == null) {
            throw exception(EXAM_APPOINTMENT_CHILD_ID_REQUIRED);
        }
        if (reqVO.getExamType() == null || reqVO.getExamType() < 1 || reqVO.getExamType() > 3) {
            throw exception(EXAM_APPOINTMENT_TYPE_INVALID);
        }
        if (reqVO.getAppointmentDate() == null) {
            throw exception(EXAM_APPOINTMENT_DATE_REQUIRED);
        }
        
        ExamAppointmentDO appointment = BeanUtils.toBean(reqVO, ExamAppointmentDO.class);
        appointment.setStatus(1); // 已预约
        appointment.setSource(reqVO.getSource() != null ? reqVO.getSource() : 1); // 默认线下
        appointment.setCreateTime(LocalDateTime.now());
        appointmentMapper.insert(appointment);
        return appointment.getId();
    }

    @Override
    public void updateAppointmentStatus(Long id, Integer status) {
        ExamAppointmentDO appointment = appointmentMapper.selectById(id);
        if (appointment == null) {
            throw exception(EXAM_APPOINTMENT_NOT_EXISTS);
        }
        appointment.setStatus(status);
        appointmentMapper.updateById(appointment);
    }

    @Override
    public PageResult<ExamAppointmentRespVO> getAppointmentPage(ExamAppointmentPageReqVO reqVO) {
        PageResult<ExamAppointmentDO> pageResult = appointmentMapper.selectPage(reqVO, 
            new LambdaQueryWrapper<ExamAppointmentDO>()
                .eq(reqVO.getChildId() != null, ExamAppointmentDO::getChildId, reqVO.getChildId())
                .eq(reqVO.getExamType() != null, ExamAppointmentDO::getExamType, reqVO.getExamType())
                .eq(reqVO.getAppointmentDate() != null, ExamAppointmentDO::getAppointmentDate, reqVO.getAppointmentDate())
                .eq(reqVO.getStatus() != null, ExamAppointmentDO::getStatus, reqVO.getStatus())
                .orderByDesc(ExamAppointmentDO::getCreateTime));
        return BeanUtils.toBean(pageResult, ExamAppointmentRespVO.class);
    }

    @Override
    public ExamAppointmentRespVO getAppointment(Long id) {
        ExamAppointmentDO appointment = appointmentMapper.selectById(id);
        if (appointment == null) {
            throw exception(EXAM_APPOINTMENT_NOT_EXISTS);
        }
        return BeanUtils.toBean(appointment, ExamAppointmentRespVO.class);
    }

    @Override
    public void cancelAppointment(Long id) {
        ExamAppointmentDO appointment = appointmentMapper.selectById(id);
        if (appointment == null) {
            throw exception(EXAM_APPOINTMENT_NOT_EXISTS);
        }
        if (appointment.getStatus() == 3) {
            throw exception(EXAM_APPOINTMENT_ALREADY_COMPLETED);
        }
        appointment.setStatus(4); // 已取消
        appointmentMapper.updateById(appointment);
    }

    // ==================== 体检记录管理 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createExamRecord(ExamRecordCreateReqVO reqVO) {
        // 参数校验
        if (reqVO.getChildId() == null) {
            throw exception(EXAM_RECORD_CHILD_ID_REQUIRED);
        }
        if (reqVO.getExamDate() == null) {
            throw exception(EXAM_RECORD_DATE_REQUIRED);
        }
        if (reqVO.getExamType() == null || reqVO.getExamType() < 1 || reqVO.getExamType() > 3) {
            throw exception(EXAM_RECORD_TYPE_INVALID);
        }
        
        ExamRecordDO examRecord = new ExamRecordDO();
        examRecord.setExamNo("EXAM-" + IdWorker.getId());
        examRecord.setChildId(reqVO.getChildId());
        examRecord.setExamDate(reqVO.getExamDate());
        examRecord.setExamType(reqVO.getExamType());
        examRecord.setMonthAge(reqVO.getMonthAge());
        examRecord.setDeptId(reqVO.getDeptId());
        examRecord.setDoctorId(reqVO.getDoctorId());
        examRecord.setCheckStatus(1); // 进行中
        examRecord.setHasAbnormal(false);
        examRecord.setCreateTime(LocalDateTime.now());
        examRecordMapper.insert(examRecord);
        
        // 如果有体格检查数据，同时创建
        if (reqVO.getHeight() != null || reqVO.getWeight() != null) {
            PhysicalExamCreateReqVO physicalReq = new PhysicalExamCreateReqVO();
            physicalReq.setExamId(examRecord.getId());
            physicalReq.setHeight(reqVO.getHeight());
            physicalReq.setWeight(reqVO.getWeight());
            physicalReq.setHeadCircumference(reqVO.getHeadCircumference());
            physicalReq.setChestCircumference(reqVO.getChestCircumference());
            physicalReq.setHeightSd(reqVO.getHeightSd());
            physicalReq.setWeightSd(reqVO.getWeightSd());
            physicalReq.setGrowthAssessment(reqVO.getGrowthAssessment());
            createPhysicalExam(physicalReq);
        }
        
        return examRecord.getId();
    }

    @Override
    public PageResult<ExamRecordRespVO> getExamRecordPage(ExamRecordPageReqVO reqVO) {
        PageResult<ExamRecordDO> pageResult = examRecordMapper.selectPage(reqVO,
            new LambdaQueryWrapper<ExamRecordDO>()
                .eq(reqVO.getChildId() != null, ExamRecordDO::getChildId, reqVO.getChildId())
                .eq(reqVO.getExamType() != null, ExamRecordDO::getExamType, reqVO.getExamType())
                .eq(reqVO.getExamDate() != null, ExamRecordDO::getExamDate, reqVO.getExamDate())
                .eq(reqVO.getCheckStatus() != null, ExamRecordDO::getCheckStatus, reqVO.getCheckStatus())
                .eq(reqVO.getHasAbnormal() != null, ExamRecordDO::getHasAbnormal, reqVO.getHasAbnormal())
                .orderByDesc(ExamRecordDO::getCreateTime));
        PageResult<ExamRecordRespVO> voPageResult = BeanUtils.toBean(pageResult, ExamRecordRespVO.class);
        // 填充体格检查数据
        voPageResult.getList().forEach(this::fillPhysicalExamData);
        return voPageResult;
    }

    @Override
    public ExamRecordRespVO getExamRecord(Long id) {
        ExamRecordDO examRecord = examRecordMapper.selectById(id);
        if (examRecord == null) {
            throw exception(EXAM_RECORD_NOT_EXISTS);
        }
        ExamRecordRespVO respVO = BeanUtils.toBean(examRecord, ExamRecordRespVO.class);
        fillPhysicalExamData(respVO);
        return respVO;
    }

    @Override
    public void submitExamRecord(Long id) {
        ExamRecordDO examRecord = examRecordMapper.selectById(id);
        if (examRecord == null) {
            throw exception(EXAM_RECORD_NOT_EXISTS);
        }
        if (examRecord.getCheckStatus() != 1) {
            throw exception(EXAM_RECORD_STATUS_INVALID);
        }
        // 检查是否完成体格检查
        PhysicalExamRecordDO physical = physicalExamMapper.selectOne(
            Wrappers.<PhysicalExamRecordDO>lambdaQuery().eq(PhysicalExamRecordDO::getExamId, id));
        if (physical == null) {
            throw exception(EXAM_PHYSICAL_NOT_EXISTS);
        }
        examRecord.setCheckStatus(2); // 待审核
        examRecord.setUpdateTime(LocalDateTime.now());
        examRecordMapper.updateById(examRecord);
    }

    @Override
    public void reviewExamRecord(Long id, Boolean approved) {
        ExamRecordDO examRecord = examRecordMapper.selectById(id);
        if (examRecord == null) {
            throw exception(EXAM_RECORD_NOT_EXISTS);
        }
        if (examRecord.getCheckStatus() != 2) {
            throw exception(EXAM_RECORD_NOT_PENDING_REVIEW);
        }
        examRecord.setCheckStatus(approved ? 3 : 1); // 已完成或退回
        examRecord.setUpdateTime(LocalDateTime.now());
        examRecordMapper.updateById(examRecord);
    }

    // ==================== 体格检查 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createPhysicalExam(PhysicalExamCreateReqVO reqVO) {
        if (reqVO.getExamId() == null) {
            throw exception(EXAM_RECORD_ID_REQUIRED);
        }
        ExamRecordDO examRecord = examRecordMapper.selectById(reqVO.getExamId());
        if (examRecord == null) {
            throw exception(EXAM_RECORD_NOT_EXISTS);
        }
        if (examRecord.getCheckStatus() != 1) {
            throw exception(EXAM_RECORD_STATUS_INVALID);
        }
        
        // 检查是否已存在
        PhysicalExamRecordDO existRecord = physicalExamMapper.selectOne(
            Wrappers.<PhysicalExamRecordDO>lambdaQuery()
                .eq(PhysicalExamRecordDO::getExamId, reqVO.getExamId()));
        if (existRecord != null) {
            // 更新
            org.springframework.beans.BeanUtils.copyProperties(reqVO, existRecord);
            existRecord.setIsAbnormal(checkPhysicalExamAbnormal(reqVO));
            calculateBmi(existRecord);
            physicalExamMapper.updateById(existRecord);
            updateExamRecordAbnormal(reqVO.getExamId());
            return existRecord.getId();
        }
        
        PhysicalExamRecordDO physicalExam = BeanUtils.toBean(reqVO, PhysicalExamRecordDO.class);
        physicalExam.setIsAbnormal(checkPhysicalExamAbnormal(reqVO));
        calculateBmi(physicalExam);
        physicalExam.setCreateTime(LocalDateTime.now());
        physicalExamMapper.insert(physicalExam);
        
        // 更新体检记录的异常标记
        updateExamRecordAbnormal(reqVO.getExamId());
        
        return physicalExam.getId();
    }

    @Override
    public void updatePhysicalExam(Long id, PhysicalExamCreateReqVO reqVO) {
        PhysicalExamRecordDO physicalExam = physicalExamMapper.selectById(id);
        if (physicalExam == null) {
            throw exception(EXAM_PHYSICAL_NOT_EXISTS);
        }
        org.springframework.beans.BeanUtils.copyProperties(reqVO, physicalExam, "id", "examId", "createTime");
        physicalExam.setIsAbnormal(checkPhysicalExamAbnormal(reqVO));
        calculateBmi(physicalExam);
        physicalExamMapper.updateById(physicalExam);
        
        // 更新体检记录的异常标记
        updateExamRecordAbnormal(physicalExam.getExamId());
    }

    // ==================== 专科检查 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createEyeExam(EyeExamCreateReqVO reqVO) {
        if (reqVO.getExamId() == null) {
            throw exception(EXAM_RECORD_ID_REQUIRED);
        }
        ExamRecordDO examRecord = examRecordMapper.selectById(reqVO.getExamId());
        if (examRecord == null) {
            throw exception(EXAM_RECORD_NOT_EXISTS);
        }
        if (examRecord.getCheckStatus() != 1) {
            throw exception(EXAM_RECORD_STATUS_INVALID);
        }
        
        // 检查是否已存在
        if (eyeExamMapper.selectCount(Wrappers.<EyeExamRecordDO>lambdaQuery()
            .eq(EyeExamRecordDO::getExamId, reqVO.getExamId())) > 0) {
            throw exception(EXAM_EYE_ALREADY_EXISTS);
        }
        
        EyeExamRecordDO eyeExam = BeanUtils.toBean(reqVO, EyeExamRecordDO.class);
        eyeExam.setIsAbnormal(checkEyeExamAbnormal(reqVO));
        eyeExam.setCreateTime(LocalDateTime.now());
        eyeExamMapper.insert(eyeExam);
        
        // 更新体检记录的异常标记
        updateExamRecordAbnormal(reqVO.getExamId());
        
        return eyeExam.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createHearingExam(HearingExamCreateReqVO reqVO) {
        if (reqVO.getExamId() == null) {
            throw exception(EXAM_RECORD_ID_REQUIRED);
        }
        ExamRecordDO examRecord = examRecordMapper.selectById(reqVO.getExamId());
        if (examRecord == null) {
            throw exception(EXAM_RECORD_NOT_EXISTS);
        }
        if (examRecord.getCheckStatus() != 1) {
            throw exception(EXAM_RECORD_STATUS_INVALID);
        }
        
        // 检查是否已存在
        if (hearingExamMapper.selectCount(Wrappers.<HearingExamRecordDO>lambdaQuery()
            .eq(HearingExamRecordDO::getExamId, reqVO.getExamId())) > 0) {
            throw exception(EXAM_HEARING_ALREADY_EXISTS);
        }
        
        HearingExamRecordDO hearingExam = BeanUtils.toBean(reqVO, HearingExamRecordDO.class);
        hearingExam.setIsAbnormal(checkHearingExamAbnormal(reqVO));
        hearingExam.setCreateTime(LocalDateTime.now());
        hearingExamMapper.insert(hearingExam);
        
        // 更新体检记录的异常标记
        updateExamRecordAbnormal(reqVO.getExamId());
        
        return hearingExam.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createOralExam(OralExamCreateReqVO reqVO) {
        if (reqVO.getExamId() == null) {
            throw exception(EXAM_RECORD_ID_REQUIRED);
        }
        ExamRecordDO examRecord = examRecordMapper.selectById(reqVO.getExamId());
        if (examRecord == null) {
            throw exception(EXAM_RECORD_NOT_EXISTS);
        }
        if (examRecord.getCheckStatus() != 1) {
            throw exception(EXAM_RECORD_STATUS_INVALID);
        }
        
        // 检查是否已存在
        if (oralExamMapper.selectCount(Wrappers.<OralExamRecordDO>lambdaQuery()
            .eq(OralExamRecordDO::getExamId, reqVO.getExamId())) > 0) {
            throw exception(EXAM_ORAL_ALREADY_EXISTS);
        }
        
        OralExamRecordDO oralExam = BeanUtils.toBean(reqVO, OralExamRecordDO.class);
        oralExam.setIsAbnormal(checkOralExamAbnormal(reqVO));
        oralExam.setCreateTime(LocalDateTime.now());
        oralExamMapper.insert(oralExam);
        
        // 更新体检记录的异常标记
        updateExamRecordAbnormal(reqVO.getExamId());
        
        return oralExam.getId();
    }

    // ==================== 辅助检查报告 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createLabReport(LabReportCreateReqVO reqVO) {
        if (reqVO.getExamId() == null) {
            throw exception(EXAM_RECORD_ID_REQUIRED);
        }
        ExamRecordDO examRecord = examRecordMapper.selectById(reqVO.getExamId());
        if (examRecord == null) {
            throw exception(EXAM_RECORD_NOT_EXISTS);
        }
        if (examRecord.getCheckStatus() != 1) {
            throw exception(EXAM_RECORD_STATUS_INVALID);
        }
        
        LabReportDO labReport = BeanUtils.toBean(reqVO, LabReportDO.class);
        labReport.setIsAbnormal(reqVO.getIsAbnormal() != null ? reqVO.getIsAbnormal() : false);
        labReport.setCreateTime(LocalDateTime.now());
        labReportMapper.insert(labReport);
        
        // 更新体检记录的异常标记
        if (Boolean.TRUE.equals(labReport.getIsAbnormal())) {
            updateExamRecordAbnormal(reqVO.getExamId());
        }
        
        return labReport.getId();
    }

    @Override
    public List<LabReportRespVO> getLabReportsByExamId(Long examId) {
        List<LabReportDO> reports = labReportMapper.selectList(
            Wrappers.<LabReportDO>lambdaQuery()
                .eq(LabReportDO::getExamId, examId)
                .orderByDesc(LabReportDO::getReportDate));
        return BeanUtils.toBean(reports, LabReportRespVO.class);
    }

    // ==================== 病历模板 ====================

    @Override
    public MedicalRecordTemplateRespVO getApplicableTemplate(String templateType, Integer monthAge) {
        MedicalRecordTemplateDO template = templateMapper.selectOne(
            Wrappers.<MedicalRecordTemplateDO>lambdaQuery()
                .eq(MedicalRecordTemplateDO::getTemplateType, templateType)
                .eq(MedicalRecordTemplateDO::getStatus, 1)
                .le(MedicalRecordTemplateDO::getAgeMonthMin, monthAge)
                .ge(MedicalRecordTemplateDO::getAgeMonthMax, monthAge)
                .last("LIMIT 1"));
        return BeanUtils.toBean(template, MedicalRecordTemplateRespVO.class);
    }

    @Override
    public PageResult<MedicalRecordTemplateRespVO> getTemplatePage(MedicalRecordTemplatePageReqVO reqVO) {
        PageResult<MedicalRecordTemplateDO> pageResult = templateMapper.selectPage(reqVO,
            new LambdaQueryWrapper<MedicalRecordTemplateDO>()
                .eq(reqVO.getTemplateType() != null, MedicalRecordTemplateDO::getTemplateType, reqVO.getTemplateType())
                .like(reqVO.getTemplateName() != null, MedicalRecordTemplateDO::getTemplateName, reqVO.getTemplateName())
                .eq(reqVO.getStatus() != null, MedicalRecordTemplateDO::getStatus, reqVO.getStatus())
                .orderByAsc(MedicalRecordTemplateDO::getSortOrder));
        return BeanUtils.toBean(pageResult, MedicalRecordTemplateRespVO.class);
    }

    @Override
    public MedicalRecordTemplateRespVO getTemplate(Long id) {
        MedicalRecordTemplateDO template = templateMapper.selectById(id);
        if (template == null) {
            throw exception(EXAM_TEMPLATE_NOT_EXISTS);
        }
        return BeanUtils.toBean(template, MedicalRecordTemplateRespVO.class);
    }

    // ==================== 公卫保健计划 ====================

    /** 标准月龄节点：匹配国家公卫规范 0-6岁 */
    private static final int[] STANDARD_MONTH_AGES = {1, 3, 6, 8, 12, 18, 24, 30, 36, 48, 60, 72};

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long generateCarePlan(Long childId) {
        // 1. 查询儿童基本信息
        ChildInfoDO child = childInfoMapper.selectById(childId);
        if (child == null) {
            throw exception(EXAM_RECORD_NOT_EXISTS);
        }
        if (child.getBirthDate() == null) {
            throw new IllegalArgumentException("儿童出生日期不能为空，无法生成保健计划");
        }
        LocalDate birthDate = child.getBirthDate();
        LocalDate firstVisitDate = child.getFirstVisitDate();

        // 2. 检查是否已存在进行中的常规公卫计划
        Long existCount = carePlanMapper.selectCount(Wrappers.<HealthCarePlanDO>lambdaQuery()
                .eq(HealthCarePlanDO::getChildId, childId)
                .eq(HealthCarePlanDO::getPlanType, 1)
                .eq(HealthCarePlanDO::getStatus, 1));
        if (existCount > 0) {
            throw new IllegalStateException("该儿童已存在进行中的常规公卫保健计划，请勿重复生成");
        }

        // 3. 创建保健计划
        HealthCarePlanDO plan = new HealthCarePlanDO();
        plan.setChildId(childId);
        plan.setPlanType(1); // 常规公卫
        plan.setStartDate(birthDate);
        plan.setEndDate(birthDate.plusYears(6));
        plan.setStatus(1); // 进行中
        plan.setCompletedNodes(0);
        plan.setCreateTime(LocalDateTime.now());
        plan.setUpdateTime(LocalDateTime.now());

        // 4. 逐节点生成预约，过滤已过节点
        List<ExamAppointmentDO> appointments = new ArrayList<>();
        for (int monthAge : STANDARD_MONTH_AGES) {
            LocalDate apptDate = birthDate.plusMonths(monthAge);
            // 如果首次就诊日期存在，跳过已过节点
            if (firstVisitDate != null && apptDate.isBefore(firstVisitDate)) {
                continue;
            }
            ExamAppointmentDO appt = new ExamAppointmentDO();
            appt.setChildId(childId);
            appt.setExamType(1); // 常规
            appt.setExamMonthAge(monthAge);
            appt.setAppointmentDate(apptDate);
            appt.setSource(3); // 系统自动
            appt.setStatus(1); // 已预约
            appt.setCreateTime(LocalDateTime.now());
            appointments.add(appt);
        }

        plan.setTotalNodes(appointments.size());
        carePlanMapper.insert(plan);

        // 5. 批量插入预约，关联计划ID
        for (ExamAppointmentDO appt : appointments) {
            appt.setPlanId(plan.getId());
            appointmentMapper.insert(appt);
        }

        return plan.getId();
    }

    @Override
    public Long addManualAppointment(ExamAppointmentCreateReqVO reqVO) {
        // 参数校验
        if (reqVO.getChildId() == null) {
            throw exception(EXAM_APPOINTMENT_CHILD_ID_REQUIRED);
        }
        if (reqVO.getAppointmentDate() == null) {
            throw exception(EXAM_APPOINTMENT_DATE_REQUIRED);
        }

        ExamAppointmentDO appointment = BeanUtils.toBean(reqVO, ExamAppointmentDO.class);
        appointment.setStatus(1); // 已预约
        appointment.setSource(1); // 线下手动
        appointment.setExamType(reqVO.getExamType() != null ? reqVO.getExamType() : 1);
        appointment.setCreateTime(LocalDateTime.now());
        // 手动添加的预约不关联保健计划
        appointment.setPlanId(null);
        appointmentMapper.insert(appointment);
        return appointment.getId();
    }

    @Override
    public List<HealthCarePlanRespVO> getCarePlansByChildId(Long childId) {
        List<HealthCarePlanDO> plans = carePlanMapper.selectList(
                Wrappers.<HealthCarePlanDO>lambdaQuery()
                        .eq(HealthCarePlanDO::getChildId, childId)
                        .orderByDesc(HealthCarePlanDO::getCreateTime));
        return BeanUtils.toBean(plans, HealthCarePlanRespVO.class);
    }

    @Override
    public List<ExamAppointmentRespVO> getAppointmentsByPlanId(Long planId) {
        List<ExamAppointmentDO> appointments = appointmentMapper.selectList(
                Wrappers.<ExamAppointmentDO>lambdaQuery()
                        .eq(ExamAppointmentDO::getPlanId, planId)
                        .orderByAsc(ExamAppointmentDO::getAppointmentDate));
        return BeanUtils.toBean(appointments, ExamAppointmentRespVO.class);
    }

    // ==================== 私有方法 ====================

    /**
     * 填充体格检查数据
     */
    private void fillPhysicalExamData(ExamRecordRespVO respVO) {
        PhysicalExamRecordDO physical = physicalExamMapper.selectOne(
            Wrappers.<PhysicalExamRecordDO>lambdaQuery()
                .eq(PhysicalExamRecordDO::getExamId, respVO.getId()));
        if (physical != null) {
            respVO.setHeight(physical.getHeight());
            respVO.setWeight(physical.getWeight());
            respVO.setHeadCircumference(physical.getHeadCircumference());
            respVO.setChestCircumference(physical.getChestCircumference());
            respVO.setBmi(physical.getBmi());
            respVO.setHeightSd(physical.getHeightSd());
            respVO.setWeightSd(physical.getWeightSd());
            respVO.setGrowthAssessment(physical.getGrowthAssessment());
        }
    }

    /**
     * 检查体格检查是否异常
     */
    private Boolean checkPhysicalExamAbnormal(PhysicalExamCreateReqVO reqVO) {
        // 检查生长评估
        if (reqVO.getGrowthAssessment() != null && !reqVO.getGrowthAssessment().isEmpty()) {
            String assessment = reqVO.getGrowthAssessment().toUpperCase();
            if (!assessment.contains("正常") && !assessment.contains("NORMAL") && !assessment.contains("达标")) {
                return true;
            }
        }
        // 检查SD值是否异常（绝对值 > 2）
        if (reqVO.getHeightSd() != null && reqVO.getHeightSd().abs().compareTo(BigDecimal.valueOf(2)) > 0) {
            return true;
        }
        if (reqVO.getWeightSd() != null && reqVO.getWeightSd().abs().compareTo(BigDecimal.valueOf(2)) > 0) {
            return true;
        }
        return false;
    }

    /**
     * 检查眼保健检查是否异常
     */
    private Boolean checkEyeExamAbnormal(EyeExamCreateReqVO reqVO) {
        if (reqVO.getDiagnosis() != null && !reqVO.getDiagnosis().isEmpty()) {
            String diagnosis = reqVO.getDiagnosis();
            return !diagnosis.contains("正常") && !diagnosis.contains("未见异常") && !diagnosis.contains("NORMAL");
        }
        return false;
    }

    /**
     * 检查听力检查是否异常
     */
    private Boolean checkHearingExamAbnormal(HearingExamCreateReqVO reqVO) {
        if (reqVO.getHearingScreening() != null) {
            return !"通过".equals(reqVO.getHearingScreening()) && !"PASS".equalsIgnoreCase(reqVO.getHearingScreening());
        }
        return false;
    }

    /**
     * 检查口腔检查是否异常
     */
    private Boolean checkOralExamAbnormal(OralExamCreateReqVO reqVO) {
        // 龋齿数 > 0
        if (reqVO.getCariesCount() != null && reqVO.getCariesCount() > 0) {
            return true;
        }
        // 牙龈异常
        if (reqVO.getGumCondition() != null && !reqVO.getGumCondition().isEmpty()) {
            String condition = reqVO.getGumCondition();
            if (!condition.contains("正常") && !condition.contains("良好")) {
                return true;
            }
        }
        // 咬合异常
        if (reqVO.getMalocclusion() != null && !reqVO.getMalocclusion().isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * 计算BMI
     */
    private void calculateBmi(PhysicalExamRecordDO physicalExam) {
        if (physicalExam.getHeight() != null && physicalExam.getWeight() != null 
            && physicalExam.getHeight().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal heightM = physicalExam.getHeight().divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
            BigDecimal bmi = physicalExam.getWeight().divide(heightM.multiply(heightM), 2, RoundingMode.HALF_UP);
            physicalExam.setBmi(bmi);
        }
    }

    /**
     * 更新体检记录的异常标记
     */
    private void updateExamRecordAbnormal(Long examId) {
        ExamRecordDO examRecord = examRecordMapper.selectById(examId);
        if (examRecord == null) {
            return;
        }
        
        boolean hasAbnormal = false;
        
        // 检查体格检查
        PhysicalExamRecordDO physical = physicalExamMapper.selectOne(
            Wrappers.<PhysicalExamRecordDO>lambdaQuery().eq(PhysicalExamRecordDO::getExamId, examId));
        if (physical != null && Boolean.TRUE.equals(physical.getIsAbnormal())) {
            hasAbnormal = true;
        }
        
        // 检查眼保健检查
        EyeExamRecordDO eye = eyeExamMapper.selectOne(
            Wrappers.<EyeExamRecordDO>lambdaQuery().eq(EyeExamRecordDO::getExamId, examId));
        if (eye != null && Boolean.TRUE.equals(eye.getIsAbnormal())) {
            hasAbnormal = true;
        }
        
        // 检查听力检查
        HearingExamRecordDO hearing = hearingExamMapper.selectOne(
            Wrappers.<HearingExamRecordDO>lambdaQuery().eq(HearingExamRecordDO::getExamId, examId));
        if (hearing != null && Boolean.TRUE.equals(hearing.getIsAbnormal())) {
            hasAbnormal = true;
        }
        
        // 检查口腔检查
        OralExamRecordDO oral = oralExamMapper.selectOne(
            Wrappers.<OralExamRecordDO>lambdaQuery().eq(OralExamRecordDO::getExamId, examId));
        if (oral != null && Boolean.TRUE.equals(oral.getIsAbnormal())) {
            hasAbnormal = true;
        }
        
        // 检查辅助检查报告
        if (labReportMapper.selectCount(Wrappers.<LabReportDO>lambdaQuery()
            .eq(LabReportDO::getExamId, examId).eq(LabReportDO::getIsAbnormal, true)) > 0) {
            hasAbnormal = true;
        }
        
        examRecord.setHasAbnormal(hasAbnormal);
        examRecord.setUpdateTime(LocalDateTime.now());
        examRecordMapper.updateById(examRecord);
    }
}
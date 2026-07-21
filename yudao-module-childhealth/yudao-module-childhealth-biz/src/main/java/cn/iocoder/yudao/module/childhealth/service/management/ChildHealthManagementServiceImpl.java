package cn.iocoder.yudao.module.childhealth.service.management;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.module.childhealth.api.management.dto.ChildHealthManagementDTO.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.management.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.exam.ExamAppointmentDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig.ScreeningPlanDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.workflow.ChildInfoDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.management.*;
import cn.iocoder.yudao.module.childhealth.dal.mysql.exam.ExamAppointmentMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.workflow.ChildInfoMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.workflow.CaseRegistrationMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.screening.ScreeningBatchMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.screeningconfig.ScreeningPlanMapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.List;

@Service
public class ChildHealthManagementServiceImpl implements ChildHealthManagementService {
    @Resource private ChildInfoMapper childInfoMapper;
    @Resource private ExamAppointmentMapper examAppointmentMapper;
    @Resource private ScreeningPlanMapper screeningPlanMapper;
    @Resource private FollowTaskMapper followTaskMapper;
    @Resource private FollowPlanMapper followPlanMapper;
    @Resource private CaseRegistrationMapper caseRegistrationMapper;
    @Resource private ScreeningBatchMapper screeningBatchMapper;

    @Override
    @Transactional
    public Long createAppointment(AppointmentCreateRequest request) {
        requireChild(request.getChildId());
        if (request.getExamType() < 1 || request.getExamType() > 3) throw error("体检类型只能为1、2、3");
        if (request.getExamMonthAge() != null && (request.getExamMonthAge() < 0 || request.getExamMonthAge() > 72)
                || request.getAppointmentDate().isBefore(LocalDate.now())) throw error("预约日期或体检月龄无效");
        ExamAppointmentDO appointment = new ExamAppointmentDO();
        BeanUtils.copyProperties(request, appointment);
        appointment.setStatus(1);
        appointment.setCreateTime(LocalDateTime.now());
        examAppointmentMapper.insert(appointment);
        return appointment.getId();
    }

    @Override
    @Transactional
    public Long createScreeningPlan(ScreeningPlanCreateRequest request) {
        if (screeningBatchMapper.selectById(request.getBatchId()) == null) throw error("筛查批次不存在");
        if (screeningPlanMapper.selectCount(Wrappers.<ScreeningPlanDO>lambdaQuery()
                .eq(ScreeningPlanDO::getPlanCode, request.getPlanCode())) > 0) throw error("筛查方案编码已存在");
        validateJson(request.getScreeningItems(), "筛查项目");
        validateJson(request.getQuestionnaires(), "问卷配置");
        validateJson(request.getScales(), "量表配置");
        validateJson(request.getTargetSchools(), "目标学校");
        validateJson(request.getTargetGrades(), "目标年级");
        ScreeningPlanDO plan = new ScreeningPlanDO();
        BeanUtils.copyProperties(request, plan);
        plan.setCreateTime(LocalDateTime.now());
        screeningPlanMapper.insert(plan);
        return plan.getId();
    }

    @Override
    @Transactional
    public Long createFollowTask(FollowTaskCreateRequest request) {
        requireChild(request.getChildId());
        if (request.getPriority() != null && (request.getPriority() < 1 || request.getPriority() > 3)) {
            throw error("优先级只能为1、2、3");
        }
        FollowTaskDO task = new FollowTaskDO();
        BeanUtils.copyProperties(request, task);
        task.setTaskNo("FOLLOW-" + IdWorker.getId());
        task.setPriority(request.getPriority() == null ? 2 : request.getPriority());
        task.setTaskStatus(1);
        task.setCreateTime(LocalDateTime.now());
        followTaskMapper.insert(task);
        return task.getId();
    }

    @Override
    @Transactional
    public Long createFollowPlan(FollowPlanCreateRequest request) {
        if (caseRegistrationMapper.selectById(request.getCaseId()) == null) throw error("专案不存在");
        FollowPlanDO plan = new FollowPlanDO();
        BeanUtils.copyProperties(request, plan);
        plan.setPlanType(request.getPlanType() == null ? 2 : request.getPlanType());
        plan.setPlanStatus(1);
        plan.setCreateTime(LocalDateTime.now());
        followPlanMapper.insert(plan);
        return plan.getId();
    }

    @Override
    public List<FollowTaskResponse> getFollowTasks(Long doctorId, Integer status) {
        return followTaskMapper.selectList(Wrappers.<FollowTaskDO>lambdaQuery()
                        .eq(doctorId != null, FollowTaskDO::getResponsibleDoctor, doctorId)
                        .eq(status != null, FollowTaskDO::getTaskStatus, status)
                        .orderByAsc(FollowTaskDO::getPlanDate).orderByAsc(FollowTaskDO::getPriority))
                .stream().map(task -> {
                    FollowTaskResponse response = new FollowTaskResponse();
                    BeanUtils.copyProperties(task, response);
                    return response;
                }).toList();
    }

    @Override
    @Transactional
    public void executeFollowTask(Long id) {
        if (followTaskMapper.update(null, Wrappers.<FollowTaskDO>lambdaUpdate()
                .eq(FollowTaskDO::getId, id).eq(FollowTaskDO::getTaskStatus, 1)
                .set(FollowTaskDO::getTaskStatus, 2)) == 0) throw error("随访任务不存在或不可执行");
    }

    @Override
    @Transactional
    public void completeFollowTask(Long id) {
        if (followTaskMapper.update(null, Wrappers.<FollowTaskDO>lambdaUpdate()
                .eq(FollowTaskDO::getId, id).eq(FollowTaskDO::getTaskStatus, 2)
                .set(FollowTaskDO::getTaskStatus, 3).set(FollowTaskDO::getCompleteTime, LocalDateTime.now())) == 0) {
            throw error("随访任务不存在或尚未执行");
        }
    }

    @Override
    @Transactional
    public void cancelFollowTask(Long id) {
        if (followTaskMapper.update(null, Wrappers.<FollowTaskDO>lambdaUpdate()
                .eq(FollowTaskDO::getId, id).in(FollowTaskDO::getTaskStatus, 1, 2)
                .set(FollowTaskDO::getTaskStatus, 4).set(FollowTaskDO::getCompleteTime, LocalDateTime.now())) == 0) {
            throw error("随访任务不存在或已结束");
        }
    }

    private void requireChild(Long id) {
        ChildInfoDO child = childInfoMapper.selectById(id);
        if (child == null) throw error("儿童档案不存在");
    }

    private void validateJson(String value, String name) {
        if (value != null && !value.isBlank() && !JsonUtils.isJson(value)) throw error(name + "必须是合法JSON");
    }

    private ServiceException error(String message) {
        return new ServiceException(1_010_003_001, message);
    }
}

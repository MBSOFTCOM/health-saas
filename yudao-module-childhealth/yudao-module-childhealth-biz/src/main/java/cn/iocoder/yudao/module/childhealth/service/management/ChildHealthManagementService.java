package cn.iocoder.yudao.module.childhealth.service.management;

import cn.iocoder.yudao.module.childhealth.api.management.dto.ChildHealthManagementDTO.*;

import java.util.List;

public interface ChildHealthManagementService {
    Long createAppointment(AppointmentCreateRequest request);
    Long createScreeningPlan(ScreeningPlanCreateRequest request);
    Long createFollowTask(FollowTaskCreateRequest request);
    Long createFollowPlan(FollowPlanCreateRequest request);
    List<FollowTaskResponse> getFollowTasks(Long doctorId, Integer status);
    void executeFollowTask(Long id);
    void completeFollowTask(Long id);
    void cancelFollowTask(Long id);
}

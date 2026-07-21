package cn.iocoder.yudao.module.childhealth.controller.admin.management;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.childhealth.api.management.dto.ChildHealthManagementDTO.*;
import cn.iocoder.yudao.module.childhealth.service.management.ChildHealthManagementService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 儿童健康业务管理")
@RestController
@RequestMapping("/childhealth")
public class ChildHealthManagementController {
    @Resource private ChildHealthManagementService service;

    @PostMapping("/exam/appointment")
    @PreAuthorize("@ss.hasPermission('childhealth:exam:appointment')")
    public CommonResult<Long> appointment(@Valid @RequestBody AppointmentCreateRequest request) {
        return success(service.createAppointment(request));
    }

    @PostMapping("/screen/plan")
    @PreAuthorize("@ss.hasPermission('childhealth:screening:plan')")
    public CommonResult<Long> screeningPlan(@Valid @RequestBody ScreeningPlanCreateRequest request) {
        return success(service.createScreeningPlan(request));
    }

    @PostMapping("/follow/task")
    @PreAuthorize("@ss.hasPermission('childhealth:followup:create')")
    public CommonResult<Long> followTask(@Valid @RequestBody FollowTaskCreateRequest request) {
        return success(service.createFollowTask(request));
    }

    @PostMapping("/case/{id}/follow-plan")
    @PreAuthorize("@ss.hasPermission('childhealth:followup:create')")
    public CommonResult<Long> followPlan(@PathVariable Long id, @Valid @RequestBody FollowPlanCreateRequest request) {
        request.setCaseId(id);
        return success(service.createFollowPlan(request));
    }

    @GetMapping("/follow/task/list")
    @PreAuthorize("@ss.hasPermission('childhealth:followup:query')")
    public CommonResult<List<FollowTaskResponse>> followTasks(
            @RequestParam(required = false) Long doctorId, @RequestParam(required = false) Integer status) {
        return success(service.getFollowTasks(doctorId, status));
    }
    @GetMapping("/follow-up/pending")
    @PreAuthorize("@ss.hasPermission('childhealth:followup:query')")
    public CommonResult<List<FollowTaskResponse>> pendingFollowUps(@RequestParam(required = false) Long doctorId) {
        return success(service.getFollowTasks(doctorId, 1));
    }

    @PostMapping("/follow/task/{id}/execute")
    @PreAuthorize("@ss.hasPermission('childhealth:followup:update')")
    public CommonResult<Boolean> execute(@PathVariable Long id) {
        service.executeFollowTask(id); return success(true);
    }

    @PutMapping("/follow/task/{id}/complete")
    @PreAuthorize("@ss.hasPermission('childhealth:followup:update')")
    public CommonResult<Boolean> complete(@PathVariable Long id) {
        service.completeFollowTask(id); return success(true);
    }

    @PutMapping("/follow/task/{id}/cancel")
    @PreAuthorize("@ss.hasPermission('childhealth:followup:update')")
    public CommonResult<Boolean> cancel(@PathVariable Long id) {
        service.cancelFollowTask(id); return success(true);
    }
}

package cn.iocoder.yudao.module.childhealth.controller.admin.followup;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.api.followup.FollowUpService;
import cn.iocoder.yudao.module.childhealth.api.followup.dto.FollowUpDTO.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

/**
 * 随访管理Controller
 */
@Tag(name = "管理后台 - 随访管理")
@RestController
@RequestMapping("/childhealth/followup")
public class FollowUpController {

    @Resource
    private FollowUpService followUpService;

    // ==================== 随访记录管理 ====================

    @PostMapping("/record")
    @Operation(summary = "创建随访记录")
    @PreAuthorize("@ss.hasPermission('childhealth:followup:create')")
    public CommonResult<Long> createFollowRecord(@Valid @RequestBody FollowRecordCreateRequest request) {
        return success(followUpService.createFollowRecord(request));
    }

    @PutMapping("/record")
    @Operation(summary = "更新随访记录")
    @PreAuthorize("@ss.hasPermission('childhealth:followup:update')")
    public CommonResult<Boolean> updateFollowRecord(@Valid @RequestBody FollowRecordUpdateRequest request) {
        followUpService.updateFollowRecord(request);
        return success(true);
    }

    @DeleteMapping("/record/{id}")
    @Operation(summary = "删除随访记录")
    @Parameter(name = "id", description = "随访记录ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:followup:delete')")
    public CommonResult<Boolean> deleteFollowRecord(@PathVariable Long id) {
        followUpService.deleteFollowRecord(id);
        return success(true);
    }

    @GetMapping("/record/{id}")
    @Operation(summary = "获取随访记录详情")
    @Parameter(name = "id", description = "随访记录ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:followup:query')")
    public CommonResult<FollowRecordResponse> getFollowRecord(@PathVariable Long id) {
        return success(followUpService.getFollowRecord(id));
    }

    @GetMapping("/record/child/{childId}")
    @Operation(summary = "获取儿童的随访记录列表")
    @Parameter(name = "childId", description = "儿童ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:followup:query')")
    public CommonResult<List<FollowRecordResponse>> getFollowRecordsByChild(@PathVariable Long childId) {
        return success(followUpService.getFollowRecordsByChild(childId));
    }

    @GetMapping("/record/case/{caseId}")
    @Operation(summary = "获取专案的随访记录列表")
    @Parameter(name = "caseId", description = "专案ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:followup:query')")
    public CommonResult<List<FollowRecordResponse>> getFollowRecordsByCase(@PathVariable Long caseId) {
        return success(followUpService.getFollowRecordsByCase(caseId));
    }

    // ==================== 随访任务管理 ====================

    @PostMapping("/task")
    @Operation(summary = "创建随访任务")
    @PreAuthorize("@ss.hasPermission('childhealth:followup:create')")
    public CommonResult<Long> createFollowTask(@Valid @RequestBody FollowTaskCreateRequest request) {
        return success(followUpService.createFollowTask(request));
    }

    @PutMapping("/task/{id}/execute")
    @Operation(summary = "执行随访任务")
    @Parameter(name = "id", description = "任务ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:followup:update')")
    public CommonResult<Boolean> executeFollowTask(@PathVariable Long id) {
        followUpService.executeFollowTask(id);
        return success(true);
    }

    @PutMapping("/task/{id}/complete")
    @Operation(summary = "完成随访任务")
    @Parameter(name = "id", description = "任务ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:followup:update')")
    public CommonResult<Boolean> completeFollowTask(@PathVariable Long id) {
        followUpService.completeFollowTask(id);
        return success(true);
    }

    @PutMapping("/task/{id}/cancel")
    @Operation(summary = "取消随访任务")
    @Parameter(name = "id", description = "任务ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:followup:update')")
    public CommonResult<Boolean> cancelFollowTask(@PathVariable Long id) {
        followUpService.cancelFollowTask(id);
        return success(true);
    }

    @PostMapping("/task/page")
    @Operation(summary = "分页查询随访任务")
    @PreAuthorize("@ss.hasPermission('childhealth:followup:query')")
    public CommonResult<PageResult<FollowTaskResponse>> getFollowTaskPage(@RequestBody FollowTaskQueryRequest request) {
        return success(followUpService.getFollowTaskPage(request));
    }

    @GetMapping("/task/pending")
    @Operation(summary = "获取待处理随访任务")
    @Parameter(name = "doctorId", description = "医生ID")
    @PreAuthorize("@ss.hasPermission('childhealth:followup:query')")
    public CommonResult<List<FollowTaskResponse>> getPendingFollowTasks(
            @RequestParam(required = false) Long doctorId) {
        return success(followUpService.getPendingFollowTasks(doctorId));
    }

    @GetMapping("/task/statistics")
    @Operation(summary = "获取随访任务统计")
    @Parameter(name = "doctorId", description = "医生ID")
    @PreAuthorize("@ss.hasPermission('childhealth:followup:query')")
    public CommonResult<FollowTaskStatisticsResponse> getFollowTaskStatistics(
            @RequestParam(required = false) Long doctorId) {
        return success(followUpService.getFollowTaskStatistics(doctorId));
    }

    // ==================== 随访计划管理 ====================

    @PostMapping("/plan")
    @Operation(summary = "创建随访计划")
    @PreAuthorize("@ss.hasPermission('childhealth:followup:create')")
    public CommonResult<Long> createFollowPlan(@Valid @RequestBody FollowPlanCreateRequest request) {
        return success(followUpService.createFollowPlan(request));
    }

    @PutMapping("/plan/{id}/complete")
    @Operation(summary = "完成随访计划")
    @Parameter(name = "id", description = "计划ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:followup:update')")
    public CommonResult<Boolean> completeFollowPlan(@PathVariable Long id) {
        followUpService.completeFollowPlan(id);
        return success(true);
    }

    @PutMapping("/plan/{id}/cancel")
    @Operation(summary = "取消随访计划")
    @Parameter(name = "id", description = "计划ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:followup:update')")
    public CommonResult<Boolean> cancelFollowPlan(@PathVariable Long id) {
        followUpService.cancelFollowPlan(id);
        return success(true);
    }

    @GetMapping("/plan/case/{caseId}")
    @Operation(summary = "获取专案的随访计划列表")
    @Parameter(name = "caseId", description = "专案ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:followup:query')")
    public CommonResult<List<FollowPlanResponse>> getFollowPlansByCase(@PathVariable Long caseId) {
        return success(followUpService.getFollowPlansByCase(caseId));
    }

    // ==================== 催检规则管理 ====================

    @PostMapping("/reminder-rule")
    @Operation(summary = "创建催检规则")
    @PreAuthorize("@ss.hasPermission('childhealth:reminder:create')")
    public CommonResult<Long> createReminderRule(@Valid @RequestBody ReminderRuleCreateRequest request) {
        return success(followUpService.createReminderRule(request));
    }

    @PutMapping("/reminder-rule/{id}/status")
    @Operation(summary = "更新催检规则状态")
    @PreAuthorize("@ss.hasPermission('childhealth:reminder:update')")
    public CommonResult<Boolean> updateReminderRuleStatus(
            @PathVariable Long id,
            @RequestParam Integer status) {
        followUpService.updateReminderRuleStatus(id, status);
        return success(true);
    }

    @GetMapping("/reminder-rule/active")
    @Operation(summary = "获取所有启用的催检规则")
    @PreAuthorize("@ss.hasPermission('childhealth:reminder:query')")
    public CommonResult<List<ReminderRuleResponse>> getActiveReminderRules() {
        return success(followUpService.getActiveReminderRules());
    }

    @GetMapping("/reminder-rule/month-age/{monthAge}")
    @Operation(summary = "根据月龄获取催检规则")
    @Parameter(name = "monthAge", description = "月龄", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:reminder:query')")
    public CommonResult<ReminderRuleResponse> getReminderRuleByMonthAge(@PathVariable Integer monthAge) {
        return success(followUpService.getReminderRuleByMonthAge(monthAge));
    }

    // ==================== 催检记录管理 ====================

    @PostMapping("/reminder-log/page")
    @Operation(summary = "分页查询催检记录")
    @PreAuthorize("@ss.hasPermission('childhealth:reminder:query')")
    public CommonResult<PageResult<ReminderLogResponse>> getReminderLogPage(
            @RequestBody ReminderLogQueryRequest request) {
        return success(followUpService.getReminderLogPage(request));
    }

    @PostMapping("/reminder/send")
    @Operation(summary = "发送催检提醒")
    @PreAuthorize("@ss.hasPermission('childhealth:reminder:send')")
    public CommonResult<Boolean> sendReminder(
            @RequestParam Long childId,
            @RequestParam Long ruleId) {
        followUpService.sendReminder(childId, ruleId);
        return success(true);
    }

    @PutMapping("/reminder-log/{id}/status")
    @Operation(summary = "更新催检记录状态")
    @PreAuthorize("@ss.hasPermission('childhealth:reminder:update')")
    public CommonResult<Boolean> updateReminderStatus(
            @PathVariable Long id,
            @RequestParam Integer status) {
        followUpService.updateReminderStatus(id, status);
        return success(true);
    }

    // ==================== 问卷管理 ====================

    @PostMapping("/questionnaire")
    @Operation(summary = "创建问卷配置")
    @PreAuthorize("@ss.hasPermission('childhealth:questionnaire:create')")
    public CommonResult<Long> createQuestionnaireConfig(@Valid @RequestBody QuestionnaireConfigCreateRequest request) {
        return success(followUpService.createQuestionnaireConfig(request));
    }

    @GetMapping("/questionnaire/active")
    @Operation(summary = "获取所有启用的问卷")
    @PreAuthorize("@ss.hasPermission('childhealth:questionnaire:query')")
    public CommonResult<List<QuestionnaireConfigResponse>> getActiveQuestionnaires() {
        return success(followUpService.getActiveQuestionnaires());
    }

    @GetMapping("/questionnaire/{id}")
    @Operation(summary = "获取问卷配置详情")
    @Parameter(name = "id", description = "问卷ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:questionnaire:query')")
    public CommonResult<QuestionnaireConfigResponse> getQuestionnaireConfig(@PathVariable Long id) {
        return success(followUpService.getQuestionnaireConfig(id));
    }

    @PostMapping("/questionnaire/submit")
    @Operation(summary = "提交问卷答案")
    @PreAuthorize("@ss.hasPermission('childhealth:questionnaire:submit')")
    public CommonResult<Long> submitQuestionnaireAnswer(@Valid @RequestBody QuestionnaireAnswerSubmitRequest request) {
        return success(followUpService.submitQuestionnaireAnswer(request));
    }

    @GetMapping("/questionnaire-answer/child/{childId}")
    @Operation(summary = "获取儿童的问卷填写记录")
    @Parameter(name = "childId", description = "儿童ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:questionnaire:query')")
    public CommonResult<List<QuestionnaireAnswerResponse>> getQuestionnaireAnswersByChild(@PathVariable Long childId) {
        return success(followUpService.getQuestionnaireAnswersByChild(childId));
    }
}
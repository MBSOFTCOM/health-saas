package cn.iocoder.yudao.module.childhealth.controller.admin.casemanagement;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.casemanagement.vo.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.casemanagement.ExamReminderLogDO;
import cn.iocoder.yudao.module.childhealth.service.casemanagement.ExamReminderLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 体检催检推送日志")
@RestController
@RequestMapping("/childhealth/exam-reminder-log")
@Validated
public class ExamReminderLogController {

    @Resource
    private ExamReminderLogService examReminderLogService;

    @PostMapping("/create")
    @Operation(summary = "创建体检催检日志")
    @PreAuthorize("@ss.hasPermission('childhealth:exam-reminder-log:create')")
    public CommonResult<Long> createExamReminderLog(@Valid @RequestBody ExamReminderLogSaveReqVO createReqVO) {
        return success(examReminderLogService.createExamReminderLog(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新体检催检日志")
    @PreAuthorize("@ss.hasPermission('childhealth:exam-reminder-log:update')")
    public CommonResult<Boolean> updateExamReminderLog(@Valid @RequestBody ExamReminderLogSaveReqVO updateReqVO) {
        examReminderLogService.updateExamReminderLog(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除体检催检日志")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:exam-reminder-log:delete')")
    public CommonResult<Boolean> deleteExamReminderLog(@RequestParam("id") Long id) {
        examReminderLogService.deleteExamReminderLog(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得体检催检日志")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:exam-reminder-log:query')")
    public CommonResult<ExamReminderLogRespVO> getExamReminderLog(@RequestParam("id") Long id) {
        ExamReminderLogDO examReminderLog = examReminderLogService.getExamReminderLog(id);
        return success(BeanUtils.toBean(examReminderLog, ExamReminderLogRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得体检催检日志分页")
    @PreAuthorize("@ss.hasPermission('childhealth:exam-reminder-log:query')")
    public CommonResult<PageResult<ExamReminderLogRespVO>> getExamReminderLogPage(@Valid ExamReminderLogPageReqVO pageReqVO) {
        PageResult<ExamReminderLogDO> pageResult = examReminderLogService.getExamReminderLogPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ExamReminderLogRespVO.class));
    }

    @PostMapping("/resend")
    @Operation(summary = "手动补发推送（对失败或漏发的日志进行手动补发，并更新日志状态为已补发）")
    @Parameter(name = "id", description = "日志ID", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:exam-reminder-log:update')")
    public CommonResult<Boolean> resendPush(@RequestParam("id") Long id) {
        examReminderLogService.resendPush(id);
        return success(true);
    }

    @PutMapping("/cancel")
    @Operation(summary = "取消推送（对待发送状态的日志进行取消，并更新日志状态为已取消）")
    @Parameter(name = "id", description = "日志ID", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:exam-reminder-log:update')")
    public CommonResult<Boolean> cancelPush(@RequestParam("id") Long id) {
        examReminderLogService.cancelPush(id);
        return success(true);
    }

}

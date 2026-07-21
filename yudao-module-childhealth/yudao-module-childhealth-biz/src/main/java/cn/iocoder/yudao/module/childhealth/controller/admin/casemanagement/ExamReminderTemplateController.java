package cn.iocoder.yudao.module.childhealth.controller.admin.casemanagement;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.casemanagement.vo.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.casemanagement.ExamReminderTemplateDO;
import cn.iocoder.yudao.module.childhealth.service.casemanagement.ExamReminderTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 体检提醒模板")
@RestController
@RequestMapping("/childhealth/exam-reminder-template")
@Validated
public class ExamReminderTemplateController {

    @Resource
    private ExamReminderTemplateService examReminderTemplateService;

    @PostMapping("/create")
    @Operation(summary = "创建体检提醒模板")
    @PreAuthorize("@ss.hasPermission('childhealth:exam-reminder-template:create')")
    public CommonResult<Long> createExamReminderTemplate(@Valid @RequestBody ExamReminderTemplateSaveReqVO createReqVO) {
        return success(examReminderTemplateService.createExamReminderTemplate(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新体检提醒模板")
    @PreAuthorize("@ss.hasPermission('childhealth:exam-reminder-template:update')")
    public CommonResult<Boolean> updateExamReminderTemplate(@Valid @RequestBody ExamReminderTemplateSaveReqVO updateReqVO) {
        examReminderTemplateService.updateExamReminderTemplate(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除体检提醒模板")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:exam-reminder-template:delete')")
    public CommonResult<Boolean> deleteExamReminderTemplate(@RequestParam("id") Long id) {
        examReminderTemplateService.deleteExamReminderTemplate(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得体检提醒模板")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:exam-reminder-template:query')")
    public CommonResult<ExamReminderTemplateRespVO> getExamReminderTemplate(@RequestParam("id") Long id) {
        ExamReminderTemplateDO examReminderTemplate = examReminderTemplateService.getExamReminderTemplate(id);
        return success(BeanUtils.toBean(examReminderTemplate, ExamReminderTemplateRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得体检提醒模板分页")
    @PreAuthorize("@ss.hasPermission('childhealth:exam-reminder-template:query')")
    public CommonResult<PageResult<ExamReminderTemplateRespVO>> getExamReminderTemplatePage(@Valid ExamReminderTemplatePageReqVO pageReqVO) {
        PageResult<ExamReminderTemplateDO> pageResult = examReminderTemplateService.getExamReminderTemplatePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ExamReminderTemplateRespVO.class));
    }

}

package cn.iocoder.yudao.module.childhealth.controller.admin.casemanagement;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.casemanagement.vo.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.casemanagement.ExamReminderRuleDO;
import cn.iocoder.yudao.module.childhealth.service.casemanagement.ExamReminderRuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 体检催检规则")
@RestController
@RequestMapping("/childhealth/exam-reminder-rule")
@Validated
public class ExamReminderRuleController {

    @Resource
    private ExamReminderRuleService examReminderRuleService;

    @PostMapping("/create")
    @Operation(summary = "创建体检催检规则")
    @PreAuthorize("@ss.hasPermission('childhealth:exam-reminder-rule:create')")
    public CommonResult<Long> createExamReminderRule(@Valid @RequestBody ExamReminderRuleSaveReqVO createReqVO) {
        return success(examReminderRuleService.createExamReminderRule(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新体检催检规则")
    @PreAuthorize("@ss.hasPermission('childhealth:exam-reminder-rule:update')")
    public CommonResult<Boolean> updateExamReminderRule(@Valid @RequestBody ExamReminderRuleSaveReqVO updateReqVO) {
        examReminderRuleService.updateExamReminderRule(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除体检催检规则")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:exam-reminder-rule:delete')")
    public CommonResult<Boolean> deleteExamReminderRule(@RequestParam("id") Long id) {
        examReminderRuleService.deleteExamReminderRule(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得体检催检规则")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:exam-reminder-rule:query')")
    public CommonResult<ExamReminderRuleRespVO> getExamReminderRule(@RequestParam("id") Long id) {
        ExamReminderRuleDO examReminderRule = examReminderRuleService.getExamReminderRule(id);
        return success(BeanUtils.toBean(examReminderRule, ExamReminderRuleRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得体检催检规则分页")
    @PreAuthorize("@ss.hasPermission('childhealth:exam-reminder-rule:query')")
    public CommonResult<PageResult<ExamReminderRuleRespVO>> getExamReminderRulePage(@Valid ExamReminderRulePageReqVO pageReqVO) {
        PageResult<ExamReminderRuleDO> pageResult = examReminderRuleService.getExamReminderRulePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ExamReminderRuleRespVO.class));
    }

}

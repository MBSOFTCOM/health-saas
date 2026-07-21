package cn.iocoder.yudao.module.childhealth.controller.admin.screeningconfig;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.screeningconfig.vo.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig.QuestionnaireDefinitionDO;
import cn.iocoder.yudao.module.childhealth.service.screeningconfig.QuestionnaireDefinitionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 问卷定义")
@RestController
@RequestMapping("/childhealth/questionnaire-definition")
@Validated
public class QuestionnaireDefinitionController {

    @Resource
    private QuestionnaireDefinitionService questionnaireDefinitionService;

    @PostMapping("/create")
    @Operation(summary = "创建问卷定义")
    @PreAuthorize("@ss.hasPermission('childhealth:questionnaire-definition:create')")
    public CommonResult<Long> createQuestionnaireDefinition(@Valid @RequestBody QuestionnaireDefinitionSaveReqVO createReqVO) {
        return success(questionnaireDefinitionService.createQuestionnaireDefinition(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新问卷定义")
    @PreAuthorize("@ss.hasPermission('childhealth:questionnaire-definition:update')")
    public CommonResult<Boolean> updateQuestionnaireDefinition(@Valid @RequestBody QuestionnaireDefinitionSaveReqVO updateReqVO) {
        questionnaireDefinitionService.updateQuestionnaireDefinition(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除问卷定义")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:questionnaire-definition:delete')")
    public CommonResult<Boolean> deleteQuestionnaireDefinition(@RequestParam("id") Long id) {
        questionnaireDefinitionService.deleteQuestionnaireDefinition(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得问卷定义")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:questionnaire-definition:query')")
    public CommonResult<QuestionnaireDefinitionRespVO> getQuestionnaireDefinition(@RequestParam("id") Long id) {
        QuestionnaireDefinitionDO questionnaireDefinition = questionnaireDefinitionService.getQuestionnaireDefinition(id);
        return success(BeanUtils.toBean(questionnaireDefinition, QuestionnaireDefinitionRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得问卷定义分页")
    @PreAuthorize("@ss.hasPermission('childhealth:questionnaire-definition:query')")
    public CommonResult<PageResult<QuestionnaireDefinitionRespVO>> getQuestionnaireDefinitionPage(@Valid QuestionnaireDefinitionPageReqVO pageReqVO) {
        PageResult<QuestionnaireDefinitionDO> pageResult = questionnaireDefinitionService.getQuestionnaireDefinitionPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, QuestionnaireDefinitionRespVO.class));
    }

}

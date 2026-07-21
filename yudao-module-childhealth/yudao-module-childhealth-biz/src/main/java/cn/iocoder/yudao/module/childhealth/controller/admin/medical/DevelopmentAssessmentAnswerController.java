package cn.iocoder.yudao.module.childhealth.controller.admin.medical;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.medical.vo.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.medical.DevelopmentAssessmentAnswerDO;
import cn.iocoder.yudao.module.childhealth.service.medical.DevelopmentAssessmentAnswerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 发育评估答题")
@RestController
@RequestMapping("/childhealth/development-assessment-answer")
@Validated
public class DevelopmentAssessmentAnswerController {

    @Resource
    private DevelopmentAssessmentAnswerService developmentAssessmentAnswerService;

    @PostMapping("/create")
    @Operation(summary = "创建发育评估答题")
    @PreAuthorize("@ss.hasPermission('childhealth:development-assessment-answer:create')")
    public CommonResult<Long> createDevelopmentAssessmentAnswer(@Valid @RequestBody DevelopmentAssessmentAnswerSaveReqVO createReqVO) {
        return success(developmentAssessmentAnswerService.createDevelopmentAssessmentAnswer(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新发育评估答题")
    @PreAuthorize("@ss.hasPermission('childhealth:development-assessment-answer:update')")
    public CommonResult<Boolean> updateDevelopmentAssessmentAnswer(@Valid @RequestBody DevelopmentAssessmentAnswerSaveReqVO updateReqVO) {
        developmentAssessmentAnswerService.updateDevelopmentAssessmentAnswer(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除发育评估答题")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:development-assessment-answer:delete')")
    public CommonResult<Boolean> deleteDevelopmentAssessmentAnswer(@RequestParam("id") Long id) {
        developmentAssessmentAnswerService.deleteDevelopmentAssessmentAnswer(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得发育评估答题")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:development-assessment-answer:query')")
    public CommonResult<DevelopmentAssessmentAnswerRespVO> getDevelopmentAssessmentAnswer(@RequestParam("id") Long id) {
        DevelopmentAssessmentAnswerDO developmentAssessmentAnswer = developmentAssessmentAnswerService.getDevelopmentAssessmentAnswer(id);
        return success(BeanUtils.toBean(developmentAssessmentAnswer, DevelopmentAssessmentAnswerRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得发育评估答题分页")
    @PreAuthorize("@ss.hasPermission('childhealth:development-assessment-answer:query')")
    public CommonResult<PageResult<DevelopmentAssessmentAnswerRespVO>> getDevelopmentAssessmentAnswerPage(@Valid DevelopmentAssessmentAnswerPageReqVO pageReqVO) {
        PageResult<DevelopmentAssessmentAnswerDO> pageResult = developmentAssessmentAnswerService.getDevelopmentAssessmentAnswerPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, DevelopmentAssessmentAnswerRespVO.class));
    }

}

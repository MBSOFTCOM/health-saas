package cn.iocoder.yudao.module.childhealth.controller.admin.medical;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.medical.vo.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.medical.DevelopmentScaleQuestionDO;
import cn.iocoder.yudao.module.childhealth.service.medical.DevelopmentScaleQuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 发育评估量表题目")
@RestController
@RequestMapping("/childhealth/development-scale-question")
@Validated
public class DevelopmentScaleQuestionController {

    @Resource
    private DevelopmentScaleQuestionService developmentScaleQuestionService;

    @PostMapping("/create")
    @Operation(summary = "创建量表题目")
    @PreAuthorize("@ss.hasPermission('childhealth:development-scale-question:create')")
    public CommonResult<Long> createDevelopmentScaleQuestion(@Valid @RequestBody DevelopmentScaleQuestionSaveReqVO createReqVO) {
        return success(developmentScaleQuestionService.createDevelopmentScaleQuestion(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新量表题目")
    @PreAuthorize("@ss.hasPermission('childhealth:development-scale-question:update')")
    public CommonResult<Boolean> updateDevelopmentScaleQuestion(@Valid @RequestBody DevelopmentScaleQuestionSaveReqVO updateReqVO) {
        developmentScaleQuestionService.updateDevelopmentScaleQuestion(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除量表题目")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:development-scale-question:delete')")
    public CommonResult<Boolean> deleteDevelopmentScaleQuestion(@RequestParam("id") Long id) {
        developmentScaleQuestionService.deleteDevelopmentScaleQuestion(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得量表题目")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:development-scale-question:query')")
    public CommonResult<DevelopmentScaleQuestionRespVO> getDevelopmentScaleQuestion(@RequestParam("id") Long id) {
        DevelopmentScaleQuestionDO developmentScaleQuestion = developmentScaleQuestionService.getDevelopmentScaleQuestion(id);
        return success(BeanUtils.toBean(developmentScaleQuestion, DevelopmentScaleQuestionRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得量表题目分页")
    @PreAuthorize("@ss.hasPermission('childhealth:development-scale-question:query')")
    public CommonResult<PageResult<DevelopmentScaleQuestionRespVO>> getDevelopmentScaleQuestionPage(@Valid DevelopmentScaleQuestionPageReqVO pageReqVO) {
        PageResult<DevelopmentScaleQuestionDO> pageResult = developmentScaleQuestionService.getDevelopmentScaleQuestionPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, DevelopmentScaleQuestionRespVO.class));
    }

}

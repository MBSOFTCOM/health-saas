package cn.iocoder.yudao.module.childhealth.controller.admin.casemanagement;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.casemanagement.vo.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.casemanagement.HighRiskFollowupDO;
import cn.iocoder.yudao.module.childhealth.service.casemanagement.HighRiskFollowupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 高危儿随访")
@RestController
@RequestMapping("/childhealth/high-risk-followup")
@Validated
public class HighRiskFollowupController {

    @Resource
    private HighRiskFollowupService highRiskFollowupService;

    @PostMapping("/create")
    @Operation(summary = "创建高危儿随访")
    @PreAuthorize("@ss.hasPermission('childhealth:high-risk-followup:create')")
    public CommonResult<Long> createHighRiskFollowup(@Valid @RequestBody HighRiskFollowupSaveReqVO createReqVO) {
        return success(highRiskFollowupService.createHighRiskFollowup(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新高危儿随访")
    @PreAuthorize("@ss.hasPermission('childhealth:high-risk-followup:update')")
    public CommonResult<Boolean> updateHighRiskFollowup(@Valid @RequestBody HighRiskFollowupSaveReqVO updateReqVO) {
        highRiskFollowupService.updateHighRiskFollowup(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除高危儿随访")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:high-risk-followup:delete')")
    public CommonResult<Boolean> deleteHighRiskFollowup(@RequestParam("id") Long id) {
        highRiskFollowupService.deleteHighRiskFollowup(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得高危儿随访")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:high-risk-followup:query')")
    public CommonResult<HighRiskFollowupRespVO> getHighRiskFollowup(@RequestParam("id") Long id) {
        HighRiskFollowupDO highRiskFollowup = highRiskFollowupService.getHighRiskFollowup(id);
        return success(BeanUtils.toBean(highRiskFollowup, HighRiskFollowupRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得高危儿随访分页")
    @PreAuthorize("@ss.hasPermission('childhealth:high-risk-followup:query')")
    public CommonResult<PageResult<HighRiskFollowupRespVO>> getHighRiskFollowupPage(@Valid HighRiskFollowupPageReqVO pageReqVO) {
        PageResult<HighRiskFollowupDO> pageResult = highRiskFollowupService.getHighRiskFollowupPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, HighRiskFollowupRespVO.class));
    }

}

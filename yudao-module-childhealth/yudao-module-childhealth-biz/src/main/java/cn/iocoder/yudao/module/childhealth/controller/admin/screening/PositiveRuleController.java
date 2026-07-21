package cn.iocoder.yudao.module.childhealth.controller.admin.screening;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.rule.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.PositiveRuleDO;
import cn.iocoder.yudao.module.childhealth.service.screening.PositiveRuleService;
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

@Tag(name = "管理后台 - 阳性判定规则")
@RestController
@RequestMapping("/childhealth/positive-rule")
@Validated
public class PositiveRuleController {

    @Resource
    private PositiveRuleService positiveRuleService;

    @PostMapping("/create")
    @Operation(summary = "创建阳性判定规则")
    @PreAuthorize("@ss.hasPermission('childhealth:positive-rule:create')")
    public CommonResult<Long> createPositiveRule(@Valid @RequestBody PositiveRuleSaveReqVO createReqVO) {
        return success(positiveRuleService.createPositiveRule(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新阳性判定规则")
    @PreAuthorize("@ss.hasPermission('childhealth:positive-rule:update')")
    public CommonResult<Boolean> updatePositiveRule(@Valid @RequestBody PositiveRuleSaveReqVO updateReqVO) {
        positiveRuleService.updatePositiveRule(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除阳性判定规则")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:positive-rule:delete')")
    public CommonResult<Boolean> deletePositiveRule(@RequestParam("id") Long id) {
        positiveRuleService.deletePositiveRule(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得阳性判定规则")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:positive-rule:query')")
    public CommonResult<PositiveRuleRespVO> getPositiveRule(@RequestParam("id") Long id) {
        PositiveRuleDO positiveRule = positiveRuleService.getPositiveRule(id);
        return success(BeanUtils.toBean(positiveRule, PositiveRuleRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得阳性判定规则分页")
    @PreAuthorize("@ss.hasPermission('childhealth:positive-rule:query')")
    public CommonResult<PageResult<PositiveRuleRespVO>> getPositiveRulePage(@Valid PositiveRulePageReqVO pageReqVO) {
        PageResult<PositiveRuleDO> pageResult = positiveRuleService.getPositiveRulePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, PositiveRuleRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得阳性判定规则列表")
    @PreAuthorize("@ss.hasPermission('childhealth:positive-rule:query')")
    public CommonResult<List<PositiveRuleRespVO>> getPositiveRuleList(@Valid PositiveRuleListReqVO listReqVO) {
        List<PositiveRuleDO> list = positiveRuleService.getPositiveRuleList(listReqVO);
        return success(BeanUtils.toBean(list, PositiveRuleRespVO.class));
    }

    @GetMapping("/active-list")
    @Operation(summary = "获取所有启用的规则")
    @PreAuthorize("@ss.hasPermission('childhealth:positive-rule:query')")
    public CommonResult<List<PositiveRuleRespVO>> getActiveList() {
        List<PositiveRuleDO> list = positiveRuleService.getActiveRuleList();
        return success(BeanUtils.toBean(list, PositiveRuleRespVO.class));
    }

}
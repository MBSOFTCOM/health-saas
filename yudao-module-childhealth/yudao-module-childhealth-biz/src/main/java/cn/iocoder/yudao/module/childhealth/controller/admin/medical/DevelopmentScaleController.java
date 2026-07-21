package cn.iocoder.yudao.module.childhealth.controller.admin.medical;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.medical.vo.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.medical.DevelopmentScaleDO;
import cn.iocoder.yudao.module.childhealth.service.medical.DevelopmentScaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 发育评估量表")
@RestController
@RequestMapping("/childhealth/development-scale")
@Validated
public class DevelopmentScaleController {

    @Resource
    private DevelopmentScaleService developmentScaleService;

    @PostMapping("/create")
    @Operation(summary = "创建发育评估量表")
    @PreAuthorize("@ss.hasPermission('childhealth:development-scale:create')")
    public CommonResult<Long> createDevelopmentScale(@Valid @RequestBody DevelopmentScaleSaveReqVO createReqVO) {
        return success(developmentScaleService.createDevelopmentScale(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新发育评估量表")
    @PreAuthorize("@ss.hasPermission('childhealth:development-scale:update')")
    public CommonResult<Boolean> updateDevelopmentScale(@Valid @RequestBody DevelopmentScaleSaveReqVO updateReqVO) {
        developmentScaleService.updateDevelopmentScale(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除发育评估量表")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:development-scale:delete')")
    public CommonResult<Boolean> deleteDevelopmentScale(@RequestParam("id") Long id) {
        developmentScaleService.deleteDevelopmentScale(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得发育评估量表")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:development-scale:query')")
    public CommonResult<DevelopmentScaleRespVO> getDevelopmentScale(@RequestParam("id") Long id) {
        DevelopmentScaleDO developmentScale = developmentScaleService.getDevelopmentScale(id);
        return success(BeanUtils.toBean(developmentScale, DevelopmentScaleRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得发育评估量表分页")
    @PreAuthorize("@ss.hasPermission('childhealth:development-scale:query')")
    public CommonResult<PageResult<DevelopmentScaleRespVO>> getDevelopmentScalePage(@Valid DevelopmentScalePageReqVO pageReqVO) {
        PageResult<DevelopmentScaleDO> pageResult = developmentScaleService.getDevelopmentScalePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, DevelopmentScaleRespVO.class));
    }

}

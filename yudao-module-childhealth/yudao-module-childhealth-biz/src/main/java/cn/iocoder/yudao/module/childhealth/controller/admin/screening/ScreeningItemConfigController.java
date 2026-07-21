package cn.iocoder.yudao.module.childhealth.controller.admin.screening;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.item.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.ScreeningItemConfigDO;
import cn.iocoder.yudao.module.childhealth.service.screening.ScreeningItemConfigService;
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

@Tag(name = "管理后台 - 筛查项目配置")
@RestController
@RequestMapping("/childhealth/screening-item-config")
@Validated
public class ScreeningItemConfigController {

    @Resource
    private ScreeningItemConfigService screeningItemConfigService;

    @PostMapping("/create")
    @Operation(summary = "创建筛查项目配置")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-item-config:create')")
    public CommonResult<Long> createScreeningItemConfig(@Valid @RequestBody ScreeningItemConfigSaveReqVO createReqVO) {
        return success(screeningItemConfigService.createScreeningItemConfig(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新筛查项目配置")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-item-config:update')")
    public CommonResult<Boolean> updateScreeningItemConfig(@Valid @RequestBody ScreeningItemConfigSaveReqVO updateReqVO) {
        screeningItemConfigService.updateScreeningItemConfig(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除筛查项目配置")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:screening-item-config:delete')")
    public CommonResult<Boolean> deleteScreeningItemConfig(@RequestParam("id") Long id) {
        screeningItemConfigService.deleteScreeningItemConfig(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得筛查项目配置")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-item-config:query')")
    public CommonResult<ScreeningItemConfigRespVO> getScreeningItemConfig(@RequestParam("id") Long id) {
        ScreeningItemConfigDO screeningItemConfig = screeningItemConfigService.getScreeningItemConfig(id);
        return success(BeanUtils.toBean(screeningItemConfig, ScreeningItemConfigRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得筛查项目配置分页")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-item-config:query')")
    public CommonResult<PageResult<ScreeningItemConfigRespVO>> getScreeningItemConfigPage(@Valid ScreeningItemConfigPageReqVO pageReqVO) {
        PageResult<ScreeningItemConfigDO> pageResult = screeningItemConfigService.getScreeningItemConfigPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ScreeningItemConfigRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得筛查项目配置列表")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-item-config:query')")
    public CommonResult<List<ScreeningItemConfigRespVO>> getScreeningItemConfigList(@Valid ScreeningItemConfigListReqVO listReqVO) {
        List<ScreeningItemConfigDO> list = screeningItemConfigService.getScreeningItemConfigList(listReqVO);
        return success(BeanUtils.toBean(list, ScreeningItemConfigRespVO.class));
    }

    @GetMapping("/list-by-category")
    @Operation(summary = "根据类别获取筛查项目配置")
    @Parameter(name = "category", description = "类别", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:screening-item-config:query')")
    public CommonResult<List<ScreeningItemConfigRespVO>> getByCategory(@RequestParam("category") String category) {
        List<ScreeningItemConfigDO> list = screeningItemConfigService.getByCategory(category);
        return success(BeanUtils.toBean(list, ScreeningItemConfigRespVO.class));
    }

    @GetMapping("/active-list")
    @Operation(summary = "获取所有启用的筛查项目配置")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-item-config:query')")
    public CommonResult<List<ScreeningItemConfigRespVO>> getActiveList() {
        List<ScreeningItemConfigDO> list = screeningItemConfigService.getActiveList();
        return success(BeanUtils.toBean(list, ScreeningItemConfigRespVO.class));
    }

}
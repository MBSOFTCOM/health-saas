package cn.iocoder.yudao.module.childhealth.controller.admin.screeningconfig;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.screeningconfig.vo.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig.HealthArticleDO;
import cn.iocoder.yudao.module.childhealth.service.screeningconfig.HealthArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 健康文章")
@RestController
@RequestMapping("/childhealth/health-article")
@Validated
public class HealthArticleController {

    @Resource
    private HealthArticleService healthArticleService;

    @PostMapping("/create")
    @Operation(summary = "创建健康文章")
    @PreAuthorize("@ss.hasPermission('childhealth:health-article:create')")
    public CommonResult<Long> createHealthArticle(@Valid @RequestBody HealthArticleSaveReqVO createReqVO) {
        return success(healthArticleService.createHealthArticle(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新健康文章")
    @PreAuthorize("@ss.hasPermission('childhealth:health-article:update')")
    public CommonResult<Boolean> updateHealthArticle(@Valid @RequestBody HealthArticleSaveReqVO updateReqVO) {
        healthArticleService.updateHealthArticle(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除健康文章")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:health-article:delete')")
    public CommonResult<Boolean> deleteHealthArticle(@RequestParam("id") Long id) {
        healthArticleService.deleteHealthArticle(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得健康文章")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:health-article:query')")
    public CommonResult<HealthArticleRespVO> getHealthArticle(@RequestParam("id") Long id) {
        HealthArticleDO healthArticle = healthArticleService.getHealthArticle(id);
        return success(BeanUtils.toBean(healthArticle, HealthArticleRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得健康文章分页")
    @PreAuthorize("@ss.hasPermission('childhealth:health-article:query')")
    public CommonResult<PageResult<HealthArticleRespVO>> getHealthArticlePage(@Valid HealthArticlePageReqVO pageReqVO) {
        PageResult<HealthArticleDO> pageResult = healthArticleService.getHealthArticlePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, HealthArticleRespVO.class));
    }

    @PostMapping("/publish")
    @Operation(summary = "定时发布：将到时间的待发布文章置为已发布")
    @PreAuthorize("@ss.hasPermission('childhealth:health-article:update')")
    public CommonResult<Integer> publishArticle() {
        return success(healthArticleService.publishArticle());
    }

    @PutMapping("/top")
    @Operation(summary = "置顶/取消置顶文章")
    @Parameter(name = "id", description = "文章ID", required = true)
    @Parameter(name = "isTop", description = "是否置顶 0否 1是", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:health-article:update')")
    public CommonResult<Boolean> topArticle(@RequestParam("id") Long id,
                                            @RequestParam("isTop") Integer isTop) {
        healthArticleService.topArticle(id, isTop);
        return success(true);
    }

}

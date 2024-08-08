package cn.iocoder.yudao.module.ppd.controller.admin.screensum;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.ppd.controller.admin.screensum.vo.CommonReq;
import cn.iocoder.yudao.module.ppd.controller.admin.screensum.vo.ScreenSumPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screensum.vo.ScreenSumRespVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screensum.vo.ScreenSumSaveReqVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screensum.ScreenSumDO;
import cn.iocoder.yudao.module.ppd.service.screensum.ScreenSumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;


@Tag(name = "管理后台 - 汇总")
@RestController
@RequestMapping("/tb/screen-sum")
@Validated
public class ScreenSumController {

    @Resource
    private ScreenSumService screenSumService;

    @PostMapping("/create")
    @Operation(summary = "创建汇总")
    @PreAuthorize("@ss.hasPermission('tb:screen-sum:create')")
    public CommonResult<Long> createScreenSum(@Valid @RequestBody ScreenSumSaveReqVO createReqVO) {
        return success(screenSumService.createScreenSum(createReqVO));
    }

    @PostMapping("/update")
    @Operation(summary = "更新汇总")
    @PreAuthorize("@ss.hasPermission('tb:screen-sum:update')")
    public CommonResult<Boolean> updateScreenSum(@Valid @RequestBody ScreenSumSaveReqVO updateReqVO) {
        screenSumService.updateScreenSum(updateReqVO);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得汇总")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('tb:screen-sum:query')")
    public CommonResult<ScreenSumRespVO> getScreenSum(@RequestParam("id") Long id) {
        ScreenSumDO screenSum = screenSumService.getScreenSum(id);
        return success(BeanUtils.toBean(screenSum, ScreenSumRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得汇总分页")
    @PreAuthorize("@ss.hasPermission('tb:screen-sum:query')")
    public CommonResult<PageResult<ScreenSumRespVO>> getScreenSumPage(@Valid ScreenSumPageReqVO pageReqVO) {
        PageResult<ScreenSumDO> pageResult = screenSumService.getScreenSumPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ScreenSumRespVO.class));
    }
    @GetMapping("/count")
    @Operation(summary = "查询是否存在")
    @PreAuthorize("@ss.hasPermission('tb:screen-sum:query')")
    public Integer getScreenSumPage(CommonReq req) {
        return screenSumService.countByPersonId(req);
    }

}
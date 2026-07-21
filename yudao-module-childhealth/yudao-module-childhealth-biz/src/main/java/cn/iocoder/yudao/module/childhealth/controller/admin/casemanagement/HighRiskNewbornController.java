package cn.iocoder.yudao.module.childhealth.controller.admin.casemanagement;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.casemanagement.vo.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.casemanagement.HighRiskNewbornDO;
import cn.iocoder.yudao.module.childhealth.service.casemanagement.HighRiskNewbornService;
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

@Tag(name = "管理后台 - 高危新生儿台账")
@RestController
@RequestMapping("/childhealth/high-risk-newborn")
@Validated
public class HighRiskNewbornController {

    @Resource
    private HighRiskNewbornService highRiskNewbornService;

    @PostMapping("/create")
    @Operation(summary = "创建高危新生儿台账")
    @PreAuthorize("@ss.hasPermission('childhealth:high-risk-newborn:create')")
    public CommonResult<Long> createHighRiskNewborn(@Valid @RequestBody HighRiskNewbornSaveReqVO createReqVO) {
        return success(highRiskNewbornService.createHighRiskNewborn(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新高危新生儿台账")
    @PreAuthorize("@ss.hasPermission('childhealth:high-risk-newborn:update')")
    public CommonResult<Boolean> updateHighRiskNewborn(@Valid @RequestBody HighRiskNewbornSaveReqVO updateReqVO) {
        highRiskNewbornService.updateHighRiskNewborn(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除高危新生儿台账")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:high-risk-newborn:delete')")
    public CommonResult<Boolean> deleteHighRiskNewborn(@RequestParam("id") Long id) {
        highRiskNewbornService.deleteHighRiskNewborn(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得高危新生儿台账")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:high-risk-newborn:query')")
    public CommonResult<HighRiskNewbornRespVO> getHighRiskNewborn(@RequestParam("id") Long id) {
        HighRiskNewbornDO highRiskNewborn = highRiskNewbornService.getHighRiskNewborn(id);
        return success(BeanUtils.toBean(highRiskNewborn, HighRiskNewbornRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得高危新生儿台账分页")
    @PreAuthorize("@ss.hasPermission('childhealth:high-risk-newborn:query')")
    public CommonResult<PageResult<HighRiskNewbornRespVO>> getHighRiskNewbornPage(@Valid HighRiskNewbornPageReqVO pageReqVO) {
        PageResult<HighRiskNewbornDO> pageResult = highRiskNewbornService.getHighRiskNewbornPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, HighRiskNewbornRespVO.class));
    }

    @GetMapping("/auto-remind")
    @Operation(summary = "未建册随访预警（扫描已建册但未按计划随访的高危新生儿）")
    @PreAuthorize("@ss.hasPermission('childhealth:high-risk-newborn:query')")
    public CommonResult<List<HighRiskNewbornRespVO>> autoRemindFollowup() {
        List<HighRiskNewbornDO> list = highRiskNewbornService.autoRemindFollowup();
        return success(BeanUtils.toBean(list, HighRiskNewbornRespVO.class));
    }

}

package cn.iocoder.yudao.module.childhealth.controller.admin.screening;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.recheck.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.RecheckRecordDO;
import cn.iocoder.yudao.module.childhealth.service.screening.RecheckRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 复筛记录")
@RestController
@RequestMapping("/childhealth/recheck-record")
@Validated
public class RecheckRecordController {

    @Resource
    private RecheckRecordService recheckRecordService;

    @PostMapping("/create")
    @Operation(summary = "创建复筛记录")
    @PreAuthorize("@ss.hasPermission('childhealth:recheck-record:create')")
    public CommonResult<Long> createRecheckRecord(@Valid @RequestBody RecheckRecordSaveReqVO createReqVO) {
        return success(recheckRecordService.createRecheckRecord(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新复筛记录")
    @PreAuthorize("@ss.hasPermission('childhealth:recheck-record:update')")
    public CommonResult<Boolean> updateRecheckRecord(@Valid @RequestBody RecheckRecordSaveReqVO updateReqVO) {
        recheckRecordService.updateRecheckRecord(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除复筛记录")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:recheck-record:delete')")
    public CommonResult<Boolean> deleteRecheckRecord(@RequestParam("id") Long id) {
        recheckRecordService.deleteRecheckRecord(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得复筛记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:recheck-record:query')")
    public CommonResult<RecheckRecordRespVO> getRecheckRecord(@RequestParam("id") Long id) {
        RecheckRecordDO recheckRecord = recheckRecordService.getRecheckRecord(id);
        return success(BeanUtils.toBean(recheckRecord, RecheckRecordRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得复筛记录分页")
    @PreAuthorize("@ss.hasPermission('childhealth:recheck-record:query')")
    public CommonResult<PageResult<RecheckRecordRespVO>> getRecheckRecordPage(@Valid RecheckRecordPageReqVO pageReqVO) {
        PageResult<RecheckRecordDO> pageResult = recheckRecordService.getRecheckRecordPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, RecheckRecordRespVO.class));
    }

    @GetMapping("/get-by-positive")
    @Operation(summary = "根据阳性记录ID获取复筛记录")
    @Parameter(name = "positiveId", description = "阳性记录ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:recheck-record:query')")
    public CommonResult<RecheckRecordRespVO> getByPositiveId(@RequestParam("positiveId") Long positiveId) {
        RecheckRecordDO recheckRecord = recheckRecordService.getByPositiveId(positiveId);
        return success(BeanUtils.toBean(recheckRecord, RecheckRecordRespVO.class));
    }

}
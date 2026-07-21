package cn.iocoder.yudao.module.childhealth.controller.admin.referral;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.referral.vo.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.workflow.ReferralRecordDO;
import cn.iocoder.yudao.module.childhealth.service.referral.ReferralRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 转介管理")
@RestController
@RequestMapping("/childhealth/referralRecord")
@Validated
public class ReferralRecordController {

    @Resource
    private ReferralRecordService referralRecordService;

    @PostMapping("/create")
    @Operation(summary = "创建转介记录")
    @PreAuthorize("@ss.hasPermission('childhealth:referral-record:create')")
    public CommonResult<Long> createReferralRecord(@Valid @RequestBody ReferralRecordSaveReqVO createReqVO) {
        return success(referralRecordService.createReferralRecord(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新转介记录")
    @PreAuthorize("@ss.hasPermission('childhealth:referral-record:update')")
    public CommonResult<Boolean> updateReferralRecord(@Valid @RequestBody ReferralRecordSaveReqVO updateReqVO) {
        referralRecordService.updateReferralRecord(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除转介记录")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:referral-record:delete')")
    public CommonResult<Boolean> deleteReferralRecord(@RequestParam("id") Long id) {
        referralRecordService.deleteReferralRecord(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得转介记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:referral-record:query')")
    public CommonResult<ReferralRecordRespVO> getReferralRecord(@RequestParam("id") Long id) {
        ReferralRecordDO referralRecord = referralRecordService.getReferralRecord(id);
        return success(BeanUtils.toBean(referralRecord, ReferralRecordRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得转介记录分页")
    @PreAuthorize("@ss.hasPermission('childhealth:referral-record:query')")
    public CommonResult<PageResult<ReferralRecordRespVO>> getReferralRecordPage(@Valid ReferralRecordPageReqVO pageReqVO) {
        PageResult<ReferralRecordDO> pageResult = referralRecordService.getReferralRecordPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ReferralRecordRespVO.class));
    }

}
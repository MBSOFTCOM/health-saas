package cn.iocoder.yudao.module.childhealth.controller.admin.casemanagement;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.casemanagement.vo.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.casemanagement.CaseFollowupRecordDO;
import cn.iocoder.yudao.module.childhealth.service.casemanagement.CaseFollowupRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 专案随访记录")
@RestController
@RequestMapping("/childhealth/case-followup-record")
@Validated
public class CaseFollowupRecordController {

    @Resource
    private CaseFollowupRecordService caseFollowupRecordService;

    @PostMapping("/create")
    @Operation(summary = "创建专案随访记录")
    @PreAuthorize("@ss.hasPermission('childhealth:case-followup-record:create')")
    public CommonResult<Long> createCaseFollowupRecord(@Valid @RequestBody CaseFollowupRecordSaveReqVO createReqVO) {
        return success(caseFollowupRecordService.createCaseFollowupRecord(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新专案随访记录")
    @PreAuthorize("@ss.hasPermission('childhealth:case-followup-record:update')")
    public CommonResult<Boolean> updateCaseFollowupRecord(@Valid @RequestBody CaseFollowupRecordSaveReqVO updateReqVO) {
        caseFollowupRecordService.updateCaseFollowupRecord(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除专案随访记录")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:case-followup-record:delete')")
    public CommonResult<Boolean> deleteCaseFollowupRecord(@RequestParam("id") Long id) {
        caseFollowupRecordService.deleteCaseFollowupRecord(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得专案随访记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:case-followup-record:query')")
    public CommonResult<CaseFollowupRecordRespVO> getCaseFollowupRecord(@RequestParam("id") Long id) {
        CaseFollowupRecordDO caseFollowupRecord = caseFollowupRecordService.getCaseFollowupRecord(id);
        return success(BeanUtils.toBean(caseFollowupRecord, CaseFollowupRecordRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得专案随访记录分页")
    @PreAuthorize("@ss.hasPermission('childhealth:case-followup-record:query')")
    public CommonResult<PageResult<CaseFollowupRecordRespVO>> getCaseFollowupRecordPage(@Valid CaseFollowupRecordPageReqVO pageReqVO) {
        PageResult<CaseFollowupRecordDO> pageResult = caseFollowupRecordService.getCaseFollowupRecordPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, CaseFollowupRecordRespVO.class));
    }

}

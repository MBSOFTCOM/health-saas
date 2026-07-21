package cn.iocoder.yudao.module.childhealth.controller.admin.transfer;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.transfer.vo.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.workflow.TransferArchiveDO;
import cn.iocoder.yudao.module.childhealth.service.transfer.TransferArchiveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 档案转递")
@RestController
@RequestMapping("/childhealth/transferArchive")
@Validated
public class TransferArchiveController {

    @Resource
    private TransferArchiveService transferArchiveService;

    @PostMapping("/create")
    @Operation(summary = "创建档案转递记录")
    @PreAuthorize("@ss.hasPermission('childhealth:transfer-archive:create')")
    public CommonResult<Long> createTransferArchive(@Valid @RequestBody TransferArchiveSaveReqVO createReqVO) {
        return success(transferArchiveService.createTransferArchive(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新档案转递记录")
    @PreAuthorize("@ss.hasPermission('childhealth:transfer-archive:update')")
    public CommonResult<Boolean> updateTransferArchive(@Valid @RequestBody TransferArchiveSaveReqVO updateReqVO) {
        transferArchiveService.updateTransferArchive(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除档案转递记录")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:transfer-archive:delete')")
    public CommonResult<Boolean> deleteTransferArchive(@RequestParam("id") Long id) {
        transferArchiveService.deleteTransferArchive(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得档案转递记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:transfer-archive:query')")
    public CommonResult<TransferArchiveRespVO> getTransferArchive(@RequestParam("id") Long id) {
        TransferArchiveDO transferArchive = transferArchiveService.getTransferArchive(id);
        return success(BeanUtils.toBean(transferArchive, TransferArchiveRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得档案转递记录分页")
    @PreAuthorize("@ss.hasPermission('childhealth:transfer-archive:query')")
    public CommonResult<PageResult<TransferArchiveRespVO>> getTransferArchivePage(@Valid TransferArchivePageReqVO pageReqVO) {
        PageResult<TransferArchiveDO> pageResult = transferArchiveService.getTransferArchivePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, TransferArchiveRespVO.class));
    }

}
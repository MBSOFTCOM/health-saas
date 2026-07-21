package cn.iocoder.yudao.module.childhealth.controller.admin.medical;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.medical.vo.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.medical.MedicalRecordItemDO;
import cn.iocoder.yudao.module.childhealth.service.medical.MedicalRecordItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 病历结构化字段值")
@RestController
@RequestMapping("/childhealth/medical-record-item")
@Validated
public class MedicalRecordItemController {

    @Resource
    private MedicalRecordItemService medicalRecordItemService;

    @PostMapping("/create")
    @Operation(summary = "创建病历字段值")
    @PreAuthorize("@ss.hasPermission('childhealth:medical-record-item:create')")
    public CommonResult<Long> createMedicalRecordItem(@Valid @RequestBody MedicalRecordItemSaveReqVO createReqVO) {
        return success(medicalRecordItemService.createMedicalRecordItem(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新病历字段值")
    @PreAuthorize("@ss.hasPermission('childhealth:medical-record-item:update')")
    public CommonResult<Boolean> updateMedicalRecordItem(@Valid @RequestBody MedicalRecordItemSaveReqVO updateReqVO) {
        medicalRecordItemService.updateMedicalRecordItem(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除病历字段值")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:medical-record-item:delete')")
    public CommonResult<Boolean> deleteMedicalRecordItem(@RequestParam("id") Long id) {
        medicalRecordItemService.deleteMedicalRecordItem(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得病历字段值")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:medical-record-item:query')")
    public CommonResult<MedicalRecordItemRespVO> getMedicalRecordItem(@RequestParam("id") Long id) {
        MedicalRecordItemDO medicalRecordItem = medicalRecordItemService.getMedicalRecordItem(id);
        return success(BeanUtils.toBean(medicalRecordItem, MedicalRecordItemRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得病历字段值分页")
    @PreAuthorize("@ss.hasPermission('childhealth:medical-record-item:query')")
    public CommonResult<PageResult<MedicalRecordItemRespVO>> getMedicalRecordItemPage(@Valid MedicalRecordItemPageReqVO pageReqVO) {
        PageResult<MedicalRecordItemDO> pageResult = medicalRecordItemService.getMedicalRecordItemPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, MedicalRecordItemRespVO.class));
    }

}

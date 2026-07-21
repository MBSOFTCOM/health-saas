package cn.iocoder.yudao.module.childhealth.controller.admin.medical;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.medical.vo.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.medical.MedicalRecordTemplateDO;
import cn.iocoder.yudao.module.childhealth.service.medical.MedicalRecordTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 病历模板")
@RestController
@RequestMapping("/childhealth/medical-record-template")
@Validated
public class MedicalRecordTemplateController {

    @Resource
    private MedicalRecordTemplateService medicalRecordTemplateService;

    @PostMapping("/create")
    @Operation(summary = "创建病历模板")
    @PreAuthorize("@ss.hasPermission('childhealth:medical-record-template:create')")
    public CommonResult<Long> createMedicalRecordTemplate(@Valid @RequestBody MedicalRecordTemplateSaveReqVO createReqVO) {
        return success(medicalRecordTemplateService.createMedicalRecordTemplate(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新病历模板")
    @PreAuthorize("@ss.hasPermission('childhealth:medical-record-template:update')")
    public CommonResult<Boolean> updateMedicalRecordTemplate(@Valid @RequestBody MedicalRecordTemplateSaveReqVO updateReqVO) {
        medicalRecordTemplateService.updateMedicalRecordTemplate(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除病历模板")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:medical-record-template:delete')")
    public CommonResult<Boolean> deleteMedicalRecordTemplate(@RequestParam("id") Long id) {
        medicalRecordTemplateService.deleteMedicalRecordTemplate(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得病历模板")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:medical-record-template:query')")
    public CommonResult<MedicalRecordTemplateRespVO> getMedicalRecordTemplate(@RequestParam("id") Long id) {
        MedicalRecordTemplateDO medicalRecordTemplate = medicalRecordTemplateService.getMedicalRecordTemplate(id);
        return success(BeanUtils.toBean(medicalRecordTemplate, MedicalRecordTemplateRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得病历模板分页")
    @PreAuthorize("@ss.hasPermission('childhealth:medical-record-template:query')")
    public CommonResult<PageResult<MedicalRecordTemplateRespVO>> getMedicalRecordTemplatePage(@Valid MedicalRecordTemplatePageReqVO pageReqVO) {
        PageResult<MedicalRecordTemplateDO> pageResult = medicalRecordTemplateService.getMedicalRecordTemplatePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, MedicalRecordTemplateRespVO.class));
    }

}

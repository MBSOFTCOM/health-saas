package cn.iocoder.yudao.module.childhealth.controller.admin.medical;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.medical.vo.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.medical.MedicalRecordDO;
import cn.iocoder.yudao.module.childhealth.service.medical.MedicalRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 病历主表")
@RestController
@RequestMapping("/childhealth/medical-record")
@Validated
public class MedicalRecordController {

    @Resource
    private MedicalRecordService medicalRecordService;

    @PostMapping("/create")
    @Operation(summary = "创建病历")
    @PreAuthorize("@ss.hasPermission('childhealth:medical-record:create')")
    public CommonResult<Long> createMedicalRecord(@Valid @RequestBody MedicalRecordSaveReqVO createReqVO) {
        return success(medicalRecordService.createMedicalRecord(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新病历")
    @PreAuthorize("@ss.hasPermission('childhealth:medical-record:update')")
    public CommonResult<Boolean> updateMedicalRecord(@Valid @RequestBody MedicalRecordSaveReqVO updateReqVO) {
        medicalRecordService.updateMedicalRecord(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除病历")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:medical-record:delete')")
    public CommonResult<Boolean> deleteMedicalRecord(@RequestParam("id") Long id) {
        medicalRecordService.deleteMedicalRecord(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得病历")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:medical-record:query')")
    public CommonResult<MedicalRecordRespVO> getMedicalRecord(@RequestParam("id") Long id) {
        MedicalRecordDO medicalRecord = medicalRecordService.getMedicalRecord(id);
        return success(BeanUtils.toBean(medicalRecord, MedicalRecordRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得病历分页")
    @PreAuthorize("@ss.hasPermission('childhealth:medical-record:query')")
    public CommonResult<PageResult<MedicalRecordRespVO>> getMedicalRecordPage(@Valid MedicalRecordPageReqVO pageReqVO) {
        PageResult<MedicalRecordDO> pageResult = medicalRecordService.getMedicalRecordPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, MedicalRecordRespVO.class));
    }

    @PostMapping("/auto-fill")
    @Operation(summary = "一键填充：根据模板默认值填充病历字段")
    @PreAuthorize("@ss.hasPermission('childhealth:medical-record:update')")
    public CommonResult<Boolean> autoFillFromTemplate(@RequestParam("recordId") Long recordId,
                                                     @RequestParam("templateId") Long templateId) {
        medicalRecordService.autoFillFromTemplate(recordId, templateId);
        return success(true);
    }

    @PostMapping("/aggregate")
    @Operation(summary = "归集当日数据到病历")
    @PreAuthorize("@ss.hasPermission('childhealth:medical-record:update')")
    public CommonResult<Boolean> aggregateToMedicalRecord(@RequestParam("childId") Long childId,
                                                          @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam("visitDate") LocalDate visitDate) {
        medicalRecordService.aggregateToMedicalRecord(childId, visitDate);
        return success(true);
    }

}

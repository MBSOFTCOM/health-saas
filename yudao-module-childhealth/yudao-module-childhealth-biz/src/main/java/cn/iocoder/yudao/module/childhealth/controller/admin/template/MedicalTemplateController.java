package cn.iocoder.yudao.module.childhealth.controller.admin.template;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.childhealth.api.template.dto.MedicalTemplateRespDTO;
import cn.iocoder.yudao.module.childhealth.service.template.MedicalTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 儿童健康病历模板")
@RestController
@RequestMapping("/childhealth/template")
public class MedicalTemplateController {

    @Resource
    private MedicalTemplateService templateService;

    @GetMapping("/match")
    @Operation(summary = "按月龄匹配病历模板")
    @PreAuthorize("@ss.hasPermission('childhealth:template:query')")
    public CommonResult<MedicalTemplateRespDTO> match(@RequestParam Integer ageMonths,
            @RequestParam(defaultValue = "GENERAL_CHECKUP") String templateType) {
        return success(switch (templateType) {
            case "EYE_HEALTH" -> templateService.getEyeHealthTemplate(ageMonths);
            case "HEARING_HEALTH" -> templateService.getHearingHealthTemplate(ageMonths);
            case "ORAL_HEALTH" -> templateService.getOralHealthTemplate(ageMonths);
            default -> templateService.getTemplateByAge(ageMonths);
        });
    }

    @GetMapping("/list")
    @Operation(summary = "查询病历模板")
    @PreAuthorize("@ss.hasPermission('childhealth:template:query')")
    public CommonResult<List<MedicalTemplateRespDTO>> list(@RequestParam(required = false) String templateType) {
        return success(templateType == null ? templateService.getAllActiveTemplates()
                : templateService.getTemplatesByType(templateType));
    }

    @PostMapping("/create")
    @Operation(summary = "创建自定义病历模板")
    @PreAuthorize("@ss.hasPermission('childhealth:template:create')")
    public CommonResult<Long> create(@RequestBody MedicalTemplateRespDTO request) {
        return success(templateService.createCustomTemplate(request));
    }
}

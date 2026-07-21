package cn.iocoder.yudao.module.childhealth.service.template;

import cn.iocoder.yudao.module.childhealth.api.template.dto.MedicalTemplateRespDTO;

import java.util.List;

public interface MedicalTemplateService {
    MedicalTemplateRespDTO getTemplateByAge(Integer ageMonths);
    MedicalTemplateRespDTO getEyeHealthTemplate(Integer ageMonths);
    MedicalTemplateRespDTO getHearingHealthTemplate(Integer ageMonths);
    MedicalTemplateRespDTO getOralHealthTemplate(Integer ageMonths);
    List<MedicalTemplateRespDTO> getAllActiveTemplates();
    List<MedicalTemplateRespDTO> getTemplatesByType(String templateType);
    Long createCustomTemplate(MedicalTemplateRespDTO template);
}

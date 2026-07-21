package cn.iocoder.yudao.module.childhealth.service.template.impl;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.module.childhealth.api.template.dto.MedicalTemplateRespDTO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.medical.MedicalRecordTemplateDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.medical.MedicalRecordTemplateMapper;
import cn.iocoder.yudao.module.childhealth.service.template.MedicalTemplateService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 病历模板服务实现
 *
 * 功能：
 * 1. 根据儿童年龄自动匹配合适的病历模板
 * 2. 提供模板中的字段定义与验证规则
 * 3. 支持自定义模板扩展
 *
 * 注：已合并到 medical 包下的 MedicalRecordTemplateDO，本类作为向后兼容层保留
 */
@Slf4j
@Service
public class MedicalTemplateServiceImpl extends ServiceImpl<MedicalRecordTemplateMapper, MedicalRecordTemplateDO>
        implements MedicalTemplateService {

    @Resource
    private MedicalRecordTemplateMapper templateMapper;

    /**
     * 根据年龄获取体检模板
     */
    @Override
    public MedicalTemplateRespDTO getTemplateByAge(Integer ageMonths) {
        log.info("获取体检模板 - 年龄: {}个月", ageMonths);

        validateAge(ageMonths);

        LambdaQueryWrapper<MedicalRecordTemplateDO> wrapper = Wrappers
                .<MedicalRecordTemplateDO>lambdaQuery()
                .eq(MedicalRecordTemplateDO::getTemplateType, "GENERAL_CHECKUP")
                .eq(MedicalRecordTemplateDO::getStatus, 1)
                .le(MedicalRecordTemplateDO::getAgeMonthMin, ageMonths)
                .ge(MedicalRecordTemplateDO::getAgeMonthMax, ageMonths)
                .orderByDesc(MedicalRecordTemplateDO::getAgeMonthMin)
                .last("LIMIT 1");

        MedicalRecordTemplateDO template = templateMapper.selectOne(wrapper);
        if (template == null) {
            throw new ServiceException(9999, "未找到符合条件的病历模板");
        }

        return convertToDTO(template);
    }

    /**
     * 获取眼保健模板
     */
    @Override
    public MedicalTemplateRespDTO getEyeHealthTemplate(Integer ageMonths) {
        return getSpecializedTemplate("EYE_HEALTH", ageMonths);
    }

    /**
     * 获取听力保健模板
     */
    @Override
    public MedicalTemplateRespDTO getHearingHealthTemplate(Integer ageMonths) {
        return getSpecializedTemplate("HEARING_HEALTH", ageMonths);
    }

    /**
     * 获取口腔保健模板
     */
    @Override
    public MedicalTemplateRespDTO getOralHealthTemplate(Integer ageMonths) {
        return getSpecializedTemplate("ORAL_HEALTH", ageMonths);
    }

    /**
     * 获取特定类型的模板
     */
    private MedicalTemplateRespDTO getSpecializedTemplate(String templateType, Integer ageMonths) {
        validateAge(ageMonths);

        LambdaQueryWrapper<MedicalRecordTemplateDO> wrapper = Wrappers
                .<MedicalRecordTemplateDO>lambdaQuery()
                .eq(MedicalRecordTemplateDO::getTemplateType, templateType)
                .eq(MedicalRecordTemplateDO::getStatus, 1)
                .le(MedicalRecordTemplateDO::getAgeMonthMin, ageMonths)
                .ge(MedicalRecordTemplateDO::getAgeMonthMax, ageMonths)
                .orderByDesc(MedicalRecordTemplateDO::getAgeMonthMin)
                .last("LIMIT 1");

        MedicalRecordTemplateDO template = templateMapper.selectOne(wrapper);
        if (template == null) {
            throw new ServiceException(9999, "未找到" + templateType + "年龄模板");
        }

        return convertToDTO(template);
    }

    /**
     * 获取所有活跃模板
     */
    @Override
    public List<MedicalTemplateRespDTO> getAllActiveTemplates() {
        LambdaQueryWrapper<MedicalRecordTemplateDO> wrapper = Wrappers
                .<MedicalRecordTemplateDO>lambdaQuery()
                .eq(MedicalRecordTemplateDO::getStatus, 1)
                .orderByAsc(MedicalRecordTemplateDO::getAgeMonthMin);

        List<MedicalRecordTemplateDO> templates = templateMapper.selectList(wrapper);
        return templates.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * 获取特定类型的所有模板
     */
    @Override
    public List<MedicalTemplateRespDTO> getTemplatesByType(String templateType) {
        LambdaQueryWrapper<MedicalRecordTemplateDO> wrapper = Wrappers
                .<MedicalRecordTemplateDO>lambdaQuery()
                .eq(MedicalRecordTemplateDO::getTemplateType, templateType)
                .eq(MedicalRecordTemplateDO::getStatus, 1)
                .orderByAsc(MedicalRecordTemplateDO::getAgeMonthMin);

        List<MedicalRecordTemplateDO> templates = templateMapper.selectList(wrapper);
        return templates.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * 创建自定义模板
     */
    @Override
    @Transactional
    public Long createCustomTemplate(MedicalTemplateRespDTO templateDTO) {
        if (templateDTO == null || templateDTO.getAgeMonthMin() == null || templateDTO.getAgeMonthMax() == null
                || templateDTO.getAgeMonthMin() < 0 || templateDTO.getAgeMonthMax() > 72) {
            throw new ServiceException(9999, "年龄范围无效");
        }

        if (templateDTO.getAgeMonthMin() > templateDTO.getAgeMonthMax()) {
            throw new ServiceException(9999, "最小年龄不能大于最大年龄");
        }

        MedicalRecordTemplateDO template = new MedicalRecordTemplateDO();
        BeanUtils.copyProperties(templateDTO, template);
        // 默认启用（1=启用）；BaseDO 的 createTime/updateTime/createBy/updateBy 由框架自动填充
        if (template.getStatus() == null) {
            template.setStatus(1);
        }

        templateMapper.insert(template);

        log.info("自定义模板创建成功 - 模板名: {}", template.getTemplateName());

        return template.getId();
    }

    private void validateAge(Integer ageMonths) {
        if (ageMonths == null || ageMonths < 0 || ageMonths > 72) {
            throw new ServiceException(9999, "年龄超出范围");
        }
    }

    /**
     * 将实体转换为DTO
     */
    private MedicalTemplateRespDTO convertToDTO(MedicalRecordTemplateDO template) {
        if (template == null) {
            return null;
        }
        MedicalTemplateRespDTO dto = new MedicalTemplateRespDTO();
        BeanUtils.copyProperties(template, dto);
        return dto;
    }

}

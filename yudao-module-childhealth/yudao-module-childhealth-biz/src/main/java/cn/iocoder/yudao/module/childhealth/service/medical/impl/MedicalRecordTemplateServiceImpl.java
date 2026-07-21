package cn.iocoder.yudao.module.childhealth.service.medical.impl;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.medical.vo.MedicalRecordTemplatePageReqVO;
import cn.iocoder.yudao.module.childhealth.controller.admin.medical.vo.MedicalRecordTemplateSaveReqVO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.medical.MedicalRecordTemplateDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.medical.MedicalRecordTemplateMapper;
import cn.iocoder.yudao.module.childhealth.service.medical.MedicalRecordTemplateService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.MEDICAL_RECORD_TEMPLATE_CODE_DUPLICATE;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.MEDICAL_RECORD_TEMPLATE_NOT_EXISTS;

/**
 * 病历模板表 Service 实现类
 *
 * 模块: A. 儿童基础健康检查（A1-病历模板表，12套×4类）
 * 创建日期: 2026-07-20
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class MedicalRecordTemplateServiceImpl implements MedicalRecordTemplateService {

    @Resource
    private MedicalRecordTemplateMapper medicalRecordTemplateMapper;

    @Override
    public Long createMedicalRecordTemplate(MedicalRecordTemplateSaveReqVO saveReqVO) {
        MedicalRecordTemplateDO template = BeanUtils.toBean(saveReqVO, MedicalRecordTemplateDO.class);
        // 编码唯一性校验
        if (template.getTemplateCode() != null
                && medicalRecordTemplateMapper.selectByCode(template.getTemplateCode()) != null) {
            throw exception(MEDICAL_RECORD_TEMPLATE_CODE_DUPLICATE);
        }
        medicalRecordTemplateMapper.insert(template);
        return template.getId();
    }

    @Override
    public void updateMedicalRecordTemplate(MedicalRecordTemplateSaveReqVO saveReqVO) {
        MedicalRecordTemplateDO updateObj = BeanUtils.toBean(saveReqVO, MedicalRecordTemplateDO.class);
        // 校验存在
        validateMedicalRecordTemplateExists(updateObj.getId());
        medicalRecordTemplateMapper.updateById(updateObj);
    }

    @Override
    public void deleteMedicalRecordTemplate(Long id) {
        validateMedicalRecordTemplateExists(id);
        medicalRecordTemplateMapper.deleteById(id);
    }

    @Override
    public MedicalRecordTemplateDO getMedicalRecordTemplate(Long id) {
        return medicalRecordTemplateMapper.selectById(id);
    }

    @Override
    public PageResult<MedicalRecordTemplateDO> getMedicalRecordTemplatePage(MedicalRecordTemplatePageReqVO pageReqVO) {
        return medicalRecordTemplateMapper.selectPage(pageReqVO,
                new LambdaQueryWrapper<MedicalRecordTemplateDO>()
                        .eq(pageReqVO.getTemplateCode() != null,
                                MedicalRecordTemplateDO::getTemplateCode, pageReqVO.getTemplateCode())
                        .like(pageReqVO.getTemplateName() != null,
                                MedicalRecordTemplateDO::getTemplateName, pageReqVO.getTemplateName())
                        .eq(pageReqVO.getTemplateType() != null,
                                MedicalRecordTemplateDO::getTemplateType, pageReqVO.getTemplateType())
                        .eq(pageReqVO.getStatus() != null,
                                MedicalRecordTemplateDO::getStatus, pageReqVO.getStatus())
                        .orderByAsc(MedicalRecordTemplateDO::getTemplateType)
                        .orderByAsc(MedicalRecordTemplateDO::getAgeMonthMin));
    }

    @Override
    public MedicalRecordTemplateDO selectByCode(String templateCode) {
        return medicalRecordTemplateMapper.selectByCode(templateCode);
    }

    @Override
    public List<MedicalRecordTemplateDO> selectActiveList() {
        return medicalRecordTemplateMapper.selectActiveList();
    }

    @Override
    public List<MedicalRecordTemplateDO> selectListByType(String templateType) {
        return medicalRecordTemplateMapper.selectListByType(templateType);
    }

    private void validateMedicalRecordTemplateExists(Long id) {
        if (id == null || medicalRecordTemplateMapper.selectById(id) == null) {
            throw exception(MEDICAL_RECORD_TEMPLATE_NOT_EXISTS);
        }
    }

}

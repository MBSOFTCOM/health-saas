package cn.iocoder.yudao.module.childhealth.service.medical;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.controller.admin.medical.vo.MedicalRecordTemplatePageReqVO;
import cn.iocoder.yudao.module.childhealth.controller.admin.medical.vo.MedicalRecordTemplateSaveReqVO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.medical.MedicalRecordTemplateDO;

import java.util.List;

/**
 * 病历模板表 Service 接口
 *
 * 模块: A. 儿童基础健康检查（A1-病历模板表，12套×4类）
 * 创建日期: 2026-07-20
 *
 * @author 芋道源码
 */
public interface MedicalRecordTemplateService {

    /**
     * 创建病历模板
     *
     * @param saveReqVO 创建信息
     * @return 编号
     */
    Long createMedicalRecordTemplate(MedicalRecordTemplateSaveReqVO saveReqVO);

    /**
     * 更新病历模板
     *
     * @param saveReqVO 更新信息
     */
    void updateMedicalRecordTemplate(MedicalRecordTemplateSaveReqVO saveReqVO);

    /**
     * 删除病历模板
     *
     * @param id 编号
     */
    void deleteMedicalRecordTemplate(Long id);

    /**
     * 获得病历模板
     *
     * @param id 编号
     * @return 病历模板
     */
    MedicalRecordTemplateDO getMedicalRecordTemplate(Long id);

    /**
     * 获得病历模板分页
     *
     * @param pageReqVO 分页查询
     * @return 病历模板分页
     */
    PageResult<MedicalRecordTemplateDO> getMedicalRecordTemplatePage(MedicalRecordTemplatePageReqVO pageReqVO);

    /**
     * 按模板编码查询
     *
     * @param templateCode 模板编码
     * @return 病历模板
     */
    MedicalRecordTemplateDO selectByCode(String templateCode);

    /**
     * 查询所有启用模板
     *
     * @return 启用模板列表
     */
    List<MedicalRecordTemplateDO> selectActiveList();

    /**
     * 按模板类型查询启用模板
     *
     * @param templateType 模板类型 GENERAL_CHECKUP/EYE_HEALTH/HEARING_HEALTH/ORAL_HEALTH/ENTRY_EXAM
     * @return 模板列表
     */
    List<MedicalRecordTemplateDO> selectListByType(String templateType);

}

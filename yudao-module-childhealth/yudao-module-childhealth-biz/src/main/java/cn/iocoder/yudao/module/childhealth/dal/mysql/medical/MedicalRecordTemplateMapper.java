package cn.iocoder.yudao.module.childhealth.dal.mysql.medical;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.medical.MedicalRecordTemplateDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 病历模板表 Mapper（合并后唯一实现）
 *
 * 模块: A. 儿童基础健康检查（A1-病历模板表，12套×4类）
 */
@Mapper
public interface MedicalRecordTemplateMapper extends BaseMapperX<MedicalRecordTemplateDO> {

    /**
     * 按模板编码查询
     */
    default MedicalRecordTemplateDO selectByCode(String templateCode) {
        return selectOne(MedicalRecordTemplateDO::getTemplateCode, templateCode);
    }

    /**
     * 按模板类型查询启用模板
     */
    default List<MedicalRecordTemplateDO> selectListByType(String templateType) {
        return selectList(new LambdaQueryWrapperX<MedicalRecordTemplateDO>()
                .eqIfPresent(MedicalRecordTemplateDO::getTemplateType, templateType)
                .eqIfPresent(MedicalRecordTemplateDO::getStatus, 1)
                .orderByAsc(MedicalRecordTemplateDO::getAgeMonthMin));
    }

    /**
     * 按状态查询
     */
    default List<MedicalRecordTemplateDO> selectListByStatus(Integer status) {
        return selectList(new LambdaQueryWrapperX<MedicalRecordTemplateDO>()
                .eqIfPresent(MedicalRecordTemplateDO::getStatus, status)
                .orderByAsc(MedicalRecordTemplateDO::getTemplateType)
                .orderByAsc(MedicalRecordTemplateDO::getAgeMonthMin));
    }

    /**
     * 按月龄查询适用模板（满足最小/最大月龄区间且启用）
     */
    default List<MedicalRecordTemplateDO> selectListByAgeMonth(Integer ageMonth) {
        return selectList(new LambdaQueryWrapperX<MedicalRecordTemplateDO>()
                .leIfPresent(MedicalRecordTemplateDO::getAgeMonthMin, ageMonth)
                .geIfPresent(MedicalRecordTemplateDO::getAgeMonthMax, ageMonth)
                .eqIfPresent(MedicalRecordTemplateDO::getStatus, 1));
    }

    /**
     * 查询所有启用模板
     */
    default List<MedicalRecordTemplateDO> selectActiveList() {
        return selectList(new LambdaQueryWrapperX<MedicalRecordTemplateDO>()
                .eqIfPresent(MedicalRecordTemplateDO::getStatus, 1)
                .orderByAsc(MedicalRecordTemplateDO::getTemplateType)
                .orderByAsc(MedicalRecordTemplateDO::getAgeMonthMin));
    }

}

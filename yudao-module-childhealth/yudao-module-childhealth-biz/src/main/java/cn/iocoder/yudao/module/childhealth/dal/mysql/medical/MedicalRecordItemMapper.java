package cn.iocoder.yudao.module.childhealth.dal.mysql.medical;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.medical.MedicalRecordItemDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 病历结构化字段值表 Mapper
 *
 * 模块: A. 儿童基础健康检查（A3-病历结构化字段值表）
 * 创建日期: 2026-07-20
 */
@Mapper
public interface MedicalRecordItemMapper extends BaseMapperX<MedicalRecordItemDO> {

    /**
     * 按病历ID查询所有字段值
     */
    default List<MedicalRecordItemDO> selectListByRecordId(Long recordId) {
        return selectList(new LambdaQueryWrapperX<MedicalRecordItemDO>()
                .eqIfPresent(MedicalRecordItemDO::getRecordId, recordId)
                .orderByAsc(MedicalRecordItemDO::getSort));
    }

    /**
     * 按模板ID查询字段定义
     */
    default List<MedicalRecordItemDO> selectListByTemplateId(Long templateId) {
        return selectList(new LambdaQueryWrapperX<MedicalRecordItemDO>()
                .eqIfPresent(MedicalRecordItemDO::getTemplateId, templateId)
                .orderByAsc(MedicalRecordItemDO::getSort));
    }

    /**
     * 按病历ID和字段编码查询单个字段值
     */
    default MedicalRecordItemDO selectByFieldCode(Long recordId, String fieldCode) {
        return selectOne(new LambdaQueryWrapperX<MedicalRecordItemDO>()
                .eqIfPresent(MedicalRecordItemDO::getRecordId, recordId)
                .eqIfPresent(MedicalRecordItemDO::getFieldCode, fieldCode));
    }

    /**
     * 按病历ID和字段类型查询
     */
    default List<MedicalRecordItemDO> selectListByFieldType(Long recordId, String fieldType) {
        return selectList(new LambdaQueryWrapperX<MedicalRecordItemDO>()
                .eqIfPresent(MedicalRecordItemDO::getRecordId, recordId)
                .eqIfPresent(MedicalRecordItemDO::getFieldType, fieldType)
                .orderByAsc(MedicalRecordItemDO::getSort));
    }

    /**
     * 按病历ID和异常标记查询
     */
    default List<MedicalRecordItemDO> selectListByIsAbnormal(Long recordId, Integer isAbnormal) {
        return selectList(new LambdaQueryWrapperX<MedicalRecordItemDO>()
                .eqIfPresent(MedicalRecordItemDO::getRecordId, recordId)
                .eqIfPresent(MedicalRecordItemDO::getIsAbnormal, isAbnormal)
                .orderByAsc(MedicalRecordItemDO::getSort));
    }

}

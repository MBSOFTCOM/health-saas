package cn.iocoder.yudao.module.childhealth.dal.mysql.medical;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.medical.MedicalRecordDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

/**
 * 病历主表 Mapper
 *
 * 模块: A. 儿童基础健康检查（A2-病历主表）
 * 创建日期: 2026-07-20
 */
@Mapper
public interface MedicalRecordMapper extends BaseMapperX<MedicalRecordDO> {

    /**
     * 按病历号查询
     */
    default MedicalRecordDO selectByRecordNo(String recordNo) {
        return selectOne(MedicalRecordDO::getRecordNo, recordNo);
    }

    /**
     * 按儿童档案ID查询
     */
    default List<MedicalRecordDO> selectListByChildId(Long childId) {
        return selectList(new LambdaQueryWrapperX<MedicalRecordDO>()
                .eqIfPresent(MedicalRecordDO::getChildId, childId)
                .orderByDesc(MedicalRecordDO::getVisitDate));
    }

    /**
     * 按就诊日期范围查询
     */
    default List<MedicalRecordDO> selectListByDateRange(LocalDate startDate, LocalDate endDate) {
        return selectList(new LambdaQueryWrapperX<MedicalRecordDO>()
                .geIfPresent(MedicalRecordDO::getVisitDate, startDate)
                .leIfPresent(MedicalRecordDO::getVisitDate, endDate)
                .orderByDesc(MedicalRecordDO::getVisitDate));
    }

    /**
     * 按就诊类型查询
     */
    default List<MedicalRecordDO> selectListByVisitType(Integer visitType) {
        return selectList(new LambdaQueryWrapperX<MedicalRecordDO>()
                .eqIfPresent(MedicalRecordDO::getVisitType, visitType)
                .orderByDesc(MedicalRecordDO::getVisitDate));
    }

    /**
     * 按审核状态查询
     */
    default List<MedicalRecordDO> selectListByAuditStatus(Integer auditStatus) {
        return selectList(new LambdaQueryWrapperX<MedicalRecordDO>()
                .eqIfPresent(MedicalRecordDO::getAuditStatus, auditStatus)
                .orderByDesc(MedicalRecordDO::getVisitDate));
    }

    /**
     * 按筛查批次ID查询
     */
    default List<MedicalRecordDO> selectListByBatchId(Long batchId) {
        return selectList(new LambdaQueryWrapperX<MedicalRecordDO>()
                .eqIfPresent(MedicalRecordDO::getBatchId, batchId)
                .orderByDesc(MedicalRecordDO::getVisitDate));
    }

}

package cn.iocoder.yudao.module.childhealth.dal.mysql.medical;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.medical.LisPacsReportDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * LIS/PACS 检验检查报告对接表 Mapper
 *
 * 模块: A. 儿童基础健康检查（A4-LIS/PACS检验检查报告对接表）
 * 创建日期: 2026-07-20
 */
@Mapper
public interface LisPacsReportMapper extends BaseMapperX<LisPacsReportDO> {

    /**
     * 按报告单号查询
     */
    default LisPacsReportDO selectByReportNo(String reportNo) {
        return selectOne(LisPacsReportDO::getReportNo, reportNo);
    }

    /**
     * 按儿童档案ID查询
     */
    default List<LisPacsReportDO> selectListByChildId(Long childId) {
        return selectList(new LambdaQueryWrapperX<LisPacsReportDO>()
                .eqIfPresent(LisPacsReportDO::getChildId, childId)
                .orderByDesc(LisPacsReportDO::getReceivedTime));
    }

    /**
     * 按报告类型（LIS/PACS）查询
     */
    default List<LisPacsReportDO> selectListByReportType(String reportType) {
        return selectList(new LambdaQueryWrapperX<LisPacsReportDO>()
                .eqIfPresent(LisPacsReportDO::getReportType, reportType)
                .orderByDesc(LisPacsReportDO::getReceivedTime));
    }

    /**
     * 按接收时间范围查询
     */
    default List<LisPacsReportDO> selectListByDateRange(LocalDateTime startTime, LocalDateTime endTime) {
        return selectList(new LambdaQueryWrapperX<LisPacsReportDO>()
                .geIfPresent(LisPacsReportDO::getReceivedTime, startTime)
                .leIfPresent(LisPacsReportDO::getReceivedTime, endTime)
                .orderByDesc(LisPacsReportDO::getReceivedTime));
    }

    /**
     * 按状态查询（0待处理 1已归档 2已忽略）
     */
    default List<LisPacsReportDO> selectListByStatus(Integer status) {
        return selectList(new LambdaQueryWrapperX<LisPacsReportDO>()
                .eqIfPresent(LisPacsReportDO::getStatus, status)
                .orderByDesc(LisPacsReportDO::getReceivedTime));
    }

    /**
     * 按关联病历ID查询
     */
    default List<LisPacsReportDO> selectListByMedicalRecordId(Long medicalRecordId) {
        return selectList(new LambdaQueryWrapperX<LisPacsReportDO>()
                .eqIfPresent(LisPacsReportDO::getMedicalRecordId, medicalRecordId)
                .orderByDesc(LisPacsReportDO::getReceivedTime));
    }

}

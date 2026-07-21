package cn.iocoder.yudao.module.childhealth.dal.mysql.medical;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.medical.ExternalReportArchiveDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

/**
 * 外部报告归档表 Mapper
 *
 * 模块: A. 儿童基础健康检查（A5-外部报告归档表）
 * 创建日期: 2026-07-20
 */
@Mapper
public interface ExternalReportArchiveMapper extends BaseMapperX<ExternalReportArchiveDO> {

    /**
     * 按儿童档案ID查询
     */
    default List<ExternalReportArchiveDO> selectListByChildId(Long childId) {
        return selectList(new LambdaQueryWrapperX<ExternalReportArchiveDO>()
                .eqIfPresent(ExternalReportArchiveDO::getChildId, childId)
                .orderByDesc(ExternalReportArchiveDO::getReportDate));
    }

    /**
     * 按报告类型查询
     */
    default List<ExternalReportArchiveDO> selectListByReportType(String reportType) {
        return selectList(new LambdaQueryWrapperX<ExternalReportArchiveDO>()
                .eqIfPresent(ExternalReportArchiveDO::getReportType, reportType)
                .orderByDesc(ExternalReportArchiveDO::getReportDate));
    }

    /**
     * 按报告日期范围查询
     */
    default List<ExternalReportArchiveDO> selectListByDateRange(LocalDate startDate, LocalDate endDate) {
        return selectList(new LambdaQueryWrapperX<ExternalReportArchiveDO>()
                .geIfPresent(ExternalReportArchiveDO::getReportDate, startDate)
                .leIfPresent(ExternalReportArchiveDO::getReportDate, endDate)
                .orderByDesc(ExternalReportArchiveDO::getReportDate));
    }

    /**
     * 按来源机构查询
     */
    default List<ExternalReportArchiveDO> selectListBySourceOrg(String sourceOrg) {
        return selectList(new LambdaQueryWrapperX<ExternalReportArchiveDO>()
                .eqIfPresent(ExternalReportArchiveDO::getSourceOrg, sourceOrg)
                .orderByDesc(ExternalReportArchiveDO::getReportDate));
    }

    /**
     * 按文件格式查询（PDF/JPG/PNG/DICOM）
     */
    default List<ExternalReportArchiveDO> selectListByFileFormat(String fileFormat) {
        return selectList(new LambdaQueryWrapperX<ExternalReportArchiveDO>()
                .eqIfPresent(ExternalReportArchiveDO::getFileFormat, fileFormat)
                .orderByDesc(ExternalReportArchiveDO::getReportDate));
    }

}

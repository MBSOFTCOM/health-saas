package cn.iocoder.yudao.module.childhealth.dal.mysql.report;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.report.ReportTemplateConfigDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 报告模板配置 Mapper
 *
 * 创建日期: 2026-07-20
 */
@Mapper
public interface ReportTemplateConfigMapper extends BaseMapperX<ReportTemplateConfigDO> {

    /**
     * 按模板编码查询
     */
    default ReportTemplateConfigDO selectByCode(String templateCode) {
        return selectOne(ReportTemplateConfigDO::getTemplateCode, templateCode);
    }

    /**
     * 按报告类型查询所有启用模板
     */
    default List<ReportTemplateConfigDO> selectListByType(Integer reportType) {
        return selectList(new LambdaQueryWrapperX<ReportTemplateConfigDO>()
                .eqIfPresent(ReportTemplateConfigDO::getReportType, reportType)
                .eqIfPresent(ReportTemplateConfigDO::getStatus, 1)
                .orderByAsc(ReportTemplateConfigDO::getSortOrder));
    }

    /**
     * 按报告类型查询默认模板
     */
    default ReportTemplateConfigDO selectDefaultByType(Integer reportType) {
        return selectOne(new LambdaQueryWrapperX<ReportTemplateConfigDO>()
                .eqIfPresent(ReportTemplateConfigDO::getReportType, reportType)
                .eqIfPresent(ReportTemplateConfigDO::getIsDefault, 1)
                .eqIfPresent(ReportTemplateConfigDO::getStatus, 1));
    }

    /**
     * 查询所有启用模板
     */
    default List<ReportTemplateConfigDO> selectActiveList() {
        return selectList(new LambdaQueryWrapperX<ReportTemplateConfigDO>()
                .eqIfPresent(ReportTemplateConfigDO::getStatus, 1)
                .orderByAsc(ReportTemplateConfigDO::getReportType)
                .orderByAsc(ReportTemplateConfigDO::getSortOrder));
    }

}

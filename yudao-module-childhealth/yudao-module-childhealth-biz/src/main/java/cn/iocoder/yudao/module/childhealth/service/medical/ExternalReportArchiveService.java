package cn.iocoder.yudao.module.childhealth.service.medical;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.medical.ExternalReportArchiveDO;

/**
 * 外部报告归档表 Service 接口
 *
 * 模块: A. 儿童基础健康检查（A5-外部报告归档表）
 * 创建日期: 2026-07-20
 *
 * @author 芋道源码
 */
public interface ExternalReportArchiveService {

    /**
     * 创建外部报告归档
     *
     * @param saveReqVO 创建信息（后续替换为 ExternalReportArchiveSaveReqVO）
     * @return 编号
     */
    Long createExternalReportArchive(Object saveReqVO);

    /**
     * 更新外部报告归档
     *
     * @param saveReqVO 更新信息（后续替换为 ExternalReportArchiveSaveReqVO）
     */
    void updateExternalReportArchive(Object saveReqVO);

    /**
     * 删除外部报告归档
     *
     * @param id 编号
     */
    void deleteExternalReportArchive(Long id);

    /**
     * 获得外部报告归档
     *
     * @param id 编号
     * @return 外部报告归档
     */
    ExternalReportArchiveDO getExternalReportArchive(Long id);

    /**
     * 获得外部报告归档分页
     *
     * @param pageParam 分页查询（后续替换为 ExternalReportArchivePageReqVO）
     * @return 外部报告归档分页
     */
    PageResult<ExternalReportArchiveDO> getExternalReportArchivePage(PageParam pageParam);

    /**
     * 归档外部报告
     *
     * @param childId 儿童档案ID
     * @param fileUrl 文件URL
     * @param reportType 报告类型
     * @param reportName 报告名称
     * @return 归档ID
     */
    Long archiveReport(Long childId, String fileUrl, String reportType, String reportName);

}

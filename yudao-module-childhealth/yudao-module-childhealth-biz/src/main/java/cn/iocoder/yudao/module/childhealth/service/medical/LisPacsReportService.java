package cn.iocoder.yudao.module.childhealth.service.medical;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.medical.LisPacsReportDO;

/**
 * LIS/PACS 检验检查报告对接表 Service 接口
 *
 * 模块: A. 儿童基础健康检查（A4-LIS/PACS检验检查报告对接表）
 * 创建日期: 2026-07-20
 *
 * @author 芋道源码
 */
public interface LisPacsReportService {

    /**
     * 创建 LIS/PACS 报告
     *
     * @param saveReqVO 创建信息（后续替换为 LisPacsReportSaveReqVO）
     * @return 编号
     */
    Long createLisPacsReport(Object saveReqVO);

    /**
     * 更新 LIS/PACS 报告
     *
     * @param saveReqVO 更新信息（后续替换为 LisPacsReportSaveReqVO）
     */
    void updateLisPacsReport(Object saveReqVO);

    /**
     * 删除 LIS/PACS 报告
     *
     * @param id 编号
     */
    void deleteLisPacsReport(Long id);

    /**
     * 获得 LIS/PACS 报告
     *
     * @param id 编号
     * @return LIS/PACS 报告
     */
    LisPacsReportDO getLisPacsReport(Long id);

    /**
     * 获得 LIS/PACS 报告分页
     *
     * @param pageParam 分页查询（后续替换为 LisPacsReportPageReqVO）
     * @return LIS/PACS 报告分页
     */
    PageResult<LisPacsReportDO> getLisPacsReportPage(PageParam pageParam);

    /**
     * 对接 LIS/PACS 系统：根据报告单号从外部系统拉取报告
     *
     * @param reportNo 报告单号
     * @param sourceSystem 来源系统
     * @return 拉取到的报告
     */
    LisPacsReportDO fetchFromExternal(String reportNo, String sourceSystem);

    /**
     * 自动填充到病历：将 LIS/PACS 报告数据写入病历结构化字段
     *
     * @param reportId 报告ID
     * @param recordId 病历ID
     * @return 是否填充成功
     */
    boolean autoFillToMedicalRecord(Long reportId, Long recordId);

}

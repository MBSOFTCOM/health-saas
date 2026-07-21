package cn.iocoder.yudao.module.childhealth.service.external.adapter;

import cn.iocoder.yudao.module.childhealth.service.external.dto.PacsReportDTO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * PACS 影像系统对接适配器
 *
 * 适用需求：
 * - 需求 14：PACS 报告数据自动获取填充
 *
 * 真实实现需对接影像科 PACS 系统（DICOM/HL7/HTTPS），
 * 当前提供 Mock 实现用于开发期联调。
 */
public interface PacsIntegrationAdapter {

    /**
     * 需求 14：根据 HIS 患者ID 拉取所有 PACS 报告
     *
     * @param hisPatientId HIS 患者ID
     * @return PACS 报告列表
     */
    List<PacsReportDTO> fetchReportsByPatient(String hisPatientId);

    /**
     * 需求 14：根据报告单号拉取单个 PACS 报告
     *
     * @param reportNo 报告单号
     * @return PACS 报告
     */
    PacsReportDTO fetchReportByNo(String reportNo);

    /**
     * 需求 14：按时间范围拉取新增 PACS 报告（用于定时同步）
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return PACS 报告列表
     */
    List<PacsReportDTO> fetchReportsByTimeRange(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 需求 14：根据检查部位+儿童姓名拉取报告
     *
     * @param childName 儿童姓名
     * @param examPart 检查部位（胸部/髋关节/头部等）
     * @return PACS 报告列表
     */
    List<PacsReportDTO> fetchReportsByChildNameAndPart(String childName, String examPart);

    /**
     * 检查 PACS 系统连接状态
     */
    boolean checkConnection();

}

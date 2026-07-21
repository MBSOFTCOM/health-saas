package cn.iocoder.yudao.module.childhealth.service.external.adapter;

import cn.iocoder.yudao.module.childhealth.service.external.dto.LisReportDTO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * LIS 检验系统对接适配器
 *
 * 适用需求：
 * - 需求 14：LIS 报告数据自动获取填充
 *
 * 真实实现需对接检验科 LIS 系统（HL7/HTTPS/数据库），
 * 当前提供 Mock 实现用于开发期联调。
 */
public interface LisIntegrationAdapter {

    /**
     * 需求 14：根据 HIS 患者ID 拉取所有 LIS 报告
     *
     * @param hisPatientId HIS 患者ID
     * @return LIS 报告列表
     */
    List<LisReportDTO> fetchReportsByPatient(String hisPatientId);

    /**
     * 需求 14：根据报告单号拉取单个 LIS 报告
     *
     * @param reportNo 报告单号
     * @return LIS 报告
     */
    LisReportDTO fetchReportByNo(String reportNo);

    /**
     * 需求 14：按时间范围拉取新增 LIS 报告（用于定时同步）
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return LIS 报告列表
     */
    List<LisReportDTO> fetchReportsByTimeRange(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 需求 14：根据儿童姓名+检测日期范围拉取报告
     *
     * @param childName 儿童姓名
     * @param startDate 检测开始日期
     * @param endDate 检测结束日期
     * @return LIS 报告列表
     */
    List<LisReportDTO> fetchReportsByChildName(String childName,
                                                LocalDateTime startDate,
                                                LocalDateTime endDate);

    /**
     * 检查 LIS 系统连接状态
     */
    boolean checkConnection();

}

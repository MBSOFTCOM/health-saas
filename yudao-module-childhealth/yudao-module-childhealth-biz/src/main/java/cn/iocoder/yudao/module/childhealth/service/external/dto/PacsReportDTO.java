package cn.iocoder.yudao.module.childhealth.service.external.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * PACS 影像报告 DTO
 *
 * 需求 14：PACS 报告数据自动获取填充
 */
@Data
public class PacsReportDTO {

    /**
     * PACS 报告单号
     */
    private String pacsReportNo;

    /**
     * HIS 患者ID
     */
    private String hisPatientId;

    /**
     * 儿童姓名
     */
    private String childName;

    /**
     * 检查部位（如：胸部、髋关节、头部）
     */
    private String examPart;

    /**
     * 检查方法（X 线/CT/MRI/B 超）
     */
    private String modality;

    /**
     * 影像所见（描述）
     */
    private String imagingFindings;

    /**
     * 诊断意见
     */
    private String diagnosticOpinion;

    /**
     * 阳性发现列表
     */
    private List<String> positiveFindings;

    /**
     * 是否阳性
     */
    private Boolean isPositive;

    /**
     * 检查医生
     */
    private String examDoctor;

    /**
     * 报告医生
     */
    private String reportDoctor;

    /**
     * 检查时间
     */
    private LocalDateTime examTime;

    /**
     * 报告时间
     */
    private LocalDateTime reportTime;

    /**
     * 影像文件 URL 列表（DICOM 转换后的 JPG/PNG）
     */
    private List<String> imageUrlList;

    /**
     * PACS 系统标识
     */
    private String sourceSystem;

}

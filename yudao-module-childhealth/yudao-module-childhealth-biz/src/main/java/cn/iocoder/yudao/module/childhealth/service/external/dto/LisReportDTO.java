package cn.iocoder.yudao.module.childhealth.service.external.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * LIS 检验报告 DTO
 *
 * 需求 14：LIS 报告数据自动获取填充
 */
@Data
public class LisReportDTO {

    /**
     * LIS 报告单号
     */
    private String lisReportNo;

    /**
     * HIS 患者ID
     */
    private String hisPatientId;

    /**
     * 儿童姓名
     */
    private String childName;

    /**
     * 检验项目编码
     */
    private String testItemCode;

    /**
     * 检验项目名称（如：血常规、血红蛋白测定）
     */
    private String testItemName;

    /**
     * 标本类型（血液/尿液/粪便等）
     */
    private String specimenType;

    /**
     * 检验结果项列表
     */
    private List<TestResultItem> resultItems;

    /**
     * 检验医生
     */
    private String testDoctor;

    /**
     * 检验日期时间
     */
    private LocalDateTime testTime;

    /**
     * 报告日期时间
     */
    private LocalDateTime reportTime;

    /**
     * LIS 系统标识
     */
    private String sourceSystem;

    /**
     * 单项检验结果
     */
    @Data
    public static class TestResultItem {

        /**
         * 项目编码（如 HGB、WBC、RBC）
         */
        private String itemCode;

        /**
         * 项目名称（如 血红蛋白、白细胞计数、红细胞计数）
         */
        private String itemName;

        /**
         * 结果值
         */
        private String resultValue;

        /**
         * 结果单位（g/L、10^9/L 等）
         */
        private String unit;

        /**
         * 参考范围
         */
        private String referenceRange;

        /**
         * 是否异常 0正常 1偏高 2偏低 3异常
         */
        private Integer abnormalFlag;

        /**
         * 危急值标记
         */
        private Boolean criticalFlag;
    }

}

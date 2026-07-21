package cn.iocoder.yudao.module.childhealth.dal.dataobject.exam;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 辅助检查报告 DO
 */
@Data
@TableName("lab_report")
public class LabReportDO {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 体检记录ID
     */
    private Long examId;

    /**
     * 报告类型 1检验 2检查
     */
    private Integer reportType;

    /**
     * 报告编号
     */
    private String reportCode;

    /**
     * 报告名称
     */
    private String reportName;

    /**
     * 报告日期
     */
    private LocalDateTime reportDate;

    /**
     * 报告内容JSON
     */
    private String reportContent;

    /**
     * 报告文件URL
     */
    private String reportUrl;

    /**
     * 来源 1LIS 2PACS 3手工录入
     */
    private Integer source;

    /**
     * 来源系统 LIS/PACS/LOCAL
     */
    private String sourceSystem;

    /**
     * 源系统报告ID
     */
    private String sourceId;

    /**
     * 是否异常
     */
    private Boolean isAbnormal;

    /**
     * 异常项目JSON
     */
    private String abnormalItems;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
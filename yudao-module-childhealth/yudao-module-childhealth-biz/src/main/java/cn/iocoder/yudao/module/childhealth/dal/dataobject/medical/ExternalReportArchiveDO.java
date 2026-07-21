package cn.iocoder.yudao.module.childhealth.dal.dataobject.medical;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDate;

/**
 * 外部报告归档表 DO
 *
 * 对应表: external_report_archive
 * 模块: A. 儿童基础健康检查（A5-外部报告归档表）
 * 创建日期: 2026-07-20
 */
@TableName("external_report_archive")
@KeySequence("external_report_archive_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExternalReportArchiveDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 儿童档案ID
     */
    private Long childId;

    /**
     * 来源机构
     */
    private String sourceOrg;

    /**
     * 报告类型
     */
    private String reportType;

    /**
     * 报告名称
     */
    private String reportName;

    /**
     * 文件URL
     */
    private String fileUrl;

    /**
     * 文件大小(字节)
     */
    private Long fileSize;

    /**
     * 文件格式 PDF/JPG/PNG/DICOM
     */
    private String fileFormat;

    /**
     * 报告日期
     */
    private LocalDate reportDate;

    /**
     * 归档日期
     */
    private LocalDate archiveDate;

    /**
     * 描述
     */
    private String description;

}

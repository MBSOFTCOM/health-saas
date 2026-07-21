package cn.iocoder.yudao.module.childhealth.dal.dataobject.medical;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * LIS/PACS 检验检查报告对接表 DO
 *
 * 对应表: lis_pacs_report
 * 模块: A. 儿童基础健康检查（A4-LIS/PACS检验检查报告对接表）
 * 创建日期: 2026-07-20
 */
@TableName("lis_pacs_report")
@KeySequence("lis_pacs_report_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LisPacsReportDO extends BaseDO {

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
     * 报告单号
     */
    private String reportNo;

    /**
     * 报告类型 LIS/PACS
     */
    private String reportType;

    /**
     * 来源系统（HIS系统名/设备名）
     */
    private String sourceSystem;

    /**
     * 检查项目
     */
    private String examItem;

    /**
     * 报告数据JSON
     */
    private String reportDataJson;

    /**
     * 报告文件URL
     */
    private String reportFileUrl;

    /**
     * 接收时间
     */
    private LocalDateTime receivedTime;

    /**
     * 报告时间
     */
    private LocalDateTime reportTime;

    /**
     * 状态 0待处理 1已归档到病历 2已忽略
     */
    private Integer status;

    /**
     * 关联病历ID
     */
    private Long medicalRecordId;

}

package cn.iocoder.yudao.module.childhealth.dal.dataobject.report;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 报告模板配置表 DO
 *
 * 对应表: report_template_config
 * 模块: 19. 多维度报告体系
 * 创建日期: 2026-07-20
 */
@TableName("report_template_config")
@KeySequence("report_template_config_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportTemplateConfigDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 模板编码
     */
    private String templateCode;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 报告类型 1学生个人 2学校汇总 3年级 4区域监管
     */
    private Integer reportType;

    /**
     * 模板内容(FTL/HTML/JSON)
     */
    private String templateContent;

    /**
     * 数据来源(SQL路径或接口)
     */
    private String dataSource;

    /**
     * 默认格式 PDF/EXCEL/XML/HTML
     */
    private String defaultFormat;

    /**
     * 适用最小月龄
     */
    private Integer applicableAgeMin;

    /**
     * 适用最大月龄
     */
    private Integer applicableAgeMax;

    /**
     * 是否该类型默认模板 0否 1是
     */
    private Integer isDefault;

    /**
     * 排序
     */
    private Integer sortOrder;

    /**
     * 状态 1启用 0停用
     */
    private Integer status;

    /**
     * 模板说明
     */
    private String description;

}

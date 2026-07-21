package cn.iocoder.yudao.module.childhealth.dal.dataobject.report;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 报告生成任务表 DO
 *
 * 对应表: report_generation_task
 * 模块: 19. 多维度报告体系
 * 创建日期: 2026-07-20
 */
@TableName("report_generation_task")
@KeySequence("report_generation_task_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportGenerationTaskDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 任务编号
     */
    private String taskNo;

    /**
     * 模板ID（关联report_template_config.id）
     */
    private Long templateId;

    /**
     * 报告类型 1学生 2学校 3年级 4区域
     */
    private Integer reportType;

    /**
     * 关联筛查批次ID
     */
    private Long batchId;

    /**
     * 学校ID
     */
    private Long schoolId;

    /**
     * 年级ID
     */
    private Long gradeId;

    /**
     * 目标ID（学生/学校/区域）
     */
    private Long targetId;

    /**
     * 目标名称
     */
    private String targetName;

    /**
     * 报告格式 PDF/EXCEL/XML
     */
    private String reportFormat;

    /**
     * 生成文件URL
     */
    private String fileUrl;

    /**
     * 文件大小(字节)
     */
    private Long fileSize;

    /**
     * 0待生成 1生成中 2成功 3失败 4已取消
     */
    private Integer taskStatus;

    /**
     * 生成进度%
     */
    private Integer progress;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 重试次数
     */
    private Integer retryCount;

    /**
     * 完成时间
     */
    private LocalDateTime finishTime;

}

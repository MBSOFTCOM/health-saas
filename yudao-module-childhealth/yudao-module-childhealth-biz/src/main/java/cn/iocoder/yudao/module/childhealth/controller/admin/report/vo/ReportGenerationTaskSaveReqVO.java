package cn.iocoder.yudao.module.childhealth.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 报告生成任务新增/修改 Request VO
 *
 * 创建日期: 2026-07-20
 */
@Schema(description = "管理后台 - 报告生成任务新增/修改 Request VO")
@Data
public class ReportGenerationTaskSaveReqVO {

    @Schema(description = "主键ID", example = "1")
    private Long id;

    @Schema(description = "任务编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "RG20260720001")
    private String taskNo;

    @Schema(description = "模板ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long templateId;

    @Schema(description = "报告类型 1学生 2学校 3年级 4区域", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer reportType;

    @Schema(description = "关联筛查批次ID", example = "100")
    private Long batchId;

    @Schema(description = "学校ID", example = "10")
    private Long schoolId;

    @Schema(description = "年级ID", example = "100")
    private Long gradeId;

    @Schema(description = "目标ID（学生/学校/区域）", example = "1001")
    private Long targetId;

    @Schema(description = "目标名称", example = "张三")
    private String targetName;

    @Schema(description = "报告格式 PDF/EXCEL/XML", example = "PDF")
    private String reportFormat;

    @Schema(description = "生成文件URL")
    private String fileUrl;

    @Schema(description = "文件大小(字节)", example = "102400")
    private Long fileSize;

    @Schema(description = "0待生成 1生成中 2成功 3失败 4已取消", example = "0")
    private Integer taskStatus;

    @Schema(description = "生成进度%", example = "0")
    private Integer progress;

    @Schema(description = "错误信息")
    private String errorMsg;

    @Schema(description = "重试次数", example = "0")
    private Integer retryCount;

    @Schema(description = "完成时间")
    private LocalDateTime finishTime;

}

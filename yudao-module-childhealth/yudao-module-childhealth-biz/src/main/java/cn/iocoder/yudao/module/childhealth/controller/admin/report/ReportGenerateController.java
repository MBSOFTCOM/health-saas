package cn.iocoder.yudao.module.childhealth.controller.admin.report;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.controller.admin.report.vo.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.report.ReportGenerationTaskDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.report.ReportTemplateConfigDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.report.ReportGenerationTaskMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.report.ReportTemplateConfigMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.*;

/**
 * 管理后台 - 报告生成聚合接口
 *
 * 创建日期: 2026-07-20
 * 模块: 19. 多维度报告体系
 * 用途: 触发异步生成学生/学校/年级/区域报告，并提供任务分页与下载
 *
 * 报告类型约定:
 *   1 学生个人报告
 *   2 学校汇总报告
 *   3 年级报告
 *   4 区域监管报告
 */
@Tag(name = "管理后台 - 报告生成聚合")
@RestController
@RequestMapping("/childhealth/report-generate")
@Validated
public class ReportGenerateController {

    /** 报告类型常量 */
    private static final int REPORT_TYPE_STUDENT = 1;
    private static final int REPORT_TYPE_SCHOOL  = 2;
    private static final int REPORT_TYPE_GRADE   = 3;
    private static final int REPORT_TYPE_REGION  = 4;

    /** 任务状态常量 */
    private static final Integer TASK_STATUS_PENDING  = 0;
    private static final Integer TASK_STATUS_SUCCESS  = 2;

    /** 默认报告格式 */
    private static final String DEFAULT_FORMAT = "PDF";

    @Resource
    private ReportGenerationTaskMapper reportGenerationTaskMapper;
    @Resource
    private ReportTemplateConfigMapper reportTemplateConfigMapper;

    // ==================== 1. 生成学生个人报告 ====================

    @PostMapping("/student")
    @Operation(summary = "生成学生个人报告（异步）")
    @PreAuthorize("@ss.hasPermission('childhealth:report-generation-task:create')")
    public CommonResult<Long> generateStudent(@Valid @RequestBody ReportGenerateReqVO reqVO) {
        if (reqVO.getStudentId() == null) {
            throw exception(REPORT_GENERATION_TASK_STATUS_ERROR);
        }
        ReportTemplateConfigDO template = validateTemplate(reqVO.getTemplateId());
        ReportGenerationTaskDO task = buildTask(reqVO, REPORT_TYPE_STUDENT,
                reqVO.getStudentId(), "学生#" + reqVO.getStudentId(), template);
        reportGenerationTaskMapper.insert(task);
        // TODO 异步触发生成器: ReportGeneratorClient.generate(task.getId())
        return success(task.getId());
    }

    // ==================== 2. 生成学校汇总报告 ====================

    @PostMapping("/school")
    @Operation(summary = "生成学校汇总报告（异步）")
    @PreAuthorize("@ss.hasPermission('childhealth:report-generation-task:create')")
    public CommonResult<Long> generateSchool(@Valid @RequestBody ReportGenerateReqVO reqVO) {
        if (reqVO.getSchoolId() == null) {
            throw exception(REPORT_GENERATION_TASK_STATUS_ERROR);
        }
        ReportTemplateConfigDO template = validateTemplate(reqVO.getTemplateId());
        ReportGenerationTaskDO task = buildTask(reqVO, REPORT_TYPE_SCHOOL,
                reqVO.getSchoolId(), "学校#" + reqVO.getSchoolId(), template);
        reportGenerationTaskMapper.insert(task);
        // TODO 异步触发生成器
        return success(task.getId());
    }

    // ==================== 3. 生成年级报告 ====================

    @PostMapping("/grade")
    @Operation(summary = "生成年级报告（异步）")
    @PreAuthorize("@ss.hasPermission('childhealth:report-generation-task:create')")
    public CommonResult<Long> generateGrade(@Valid @RequestBody ReportGenerateReqVO reqVO) {
        if (reqVO.getGradeId() == null || reqVO.getSchoolId() == null) {
            throw exception(REPORT_GENERATION_TASK_STATUS_ERROR);
        }
        ReportTemplateConfigDO template = validateTemplate(reqVO.getTemplateId());
        ReportGenerationTaskDO task = buildTask(reqVO, REPORT_TYPE_GRADE,
                reqVO.getGradeId(), "学校#" + reqVO.getSchoolId() + "/年级#" + reqVO.getGradeId(), template);
        reportGenerationTaskMapper.insert(task);
        // TODO 异步触发生成器
        return success(task.getId());
    }

    // ==================== 4. 生成区域监管报告 ====================

    @PostMapping("/region")
    @Operation(summary = "生成区域监管报告（异步）")
    @PreAuthorize("@ss.hasPermission('childhealth:report-generation-task:create')")
    public CommonResult<Long> generateRegion(@Valid @RequestBody ReportGenerateReqVO reqVO) {
        if (reqVO.getRegionCode() == null || reqVO.getRegionCode().isEmpty()) {
            throw exception(REPORT_GENERATION_TASK_STATUS_ERROR);
        }
        ReportTemplateConfigDO template = validateTemplate(reqVO.getTemplateId());
        // 区域监管报告的 targetId 用 regionCode 的哈希值占位，targetName 用 regionCode
        Long regionTargetId = (long) reqVO.getRegionCode().hashCode();
        ReportGenerationTaskDO task = buildTask(reqVO, REPORT_TYPE_REGION,
                regionTargetId, "区域#" + reqVO.getRegionCode(), template);
        reportGenerationTaskMapper.insert(task);
        // TODO 异步触发生成器
        return success(task.getId());
    }

    // ==================== 5. 报告生成任务分页 ====================

    @GetMapping("/task-page")
    @Operation(summary = "报告生成任务分页")
    @PreAuthorize("@ss.hasPermission('childhealth:report-generation-task:query')")
    public CommonResult<PageResult<ReportGenerationTaskRespVO>> taskPage(@Valid ReportGenerationTaskPageReqVO pageReqVO) {
        PageResult<ReportGenerationTaskDO> pageResult = reportGenerationTaskMapper.selectPage(pageReqVO,
                new LambdaQueryWrapperX<ReportGenerationTaskDO>()
                        .likeIfPresent(ReportGenerationTaskDO::getTaskNo, pageReqVO.getTaskNo())
                        .eqIfPresent(ReportGenerationTaskDO::getTemplateId, pageReqVO.getTemplateId())
                        .eqIfPresent(ReportGenerationTaskDO::getReportType, pageReqVO.getReportType())
                        .eqIfPresent(ReportGenerationTaskDO::getTaskStatus, pageReqVO.getTaskStatus())
                        .eqIfPresent(ReportGenerationTaskDO::getBatchId, pageReqVO.getBatchId())
                        .geIfPresent(ReportGenerationTaskDO::getCreateTime, pageReqVO.getCreateTimeStart())
                        .leIfPresent(ReportGenerationTaskDO::getCreateTime, pageReqVO.getCreateTimeEnd())
                        .orderByDesc(ReportGenerationTaskDO::getId));
        return success(BeanUtils.toBean(pageResult, ReportGenerationTaskRespVO.class));
    }

    // ==================== 6. 下载报告 ====================

    @GetMapping("/download")
    @Operation(summary = "下载报告文件")
    @Parameter(name = "id", description = "任务ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:report-generation-task:query')")
    public void download(@RequestParam("id") Long id, HttpServletResponse response) throws IOException {
        ReportGenerationTaskDO task = reportGenerationTaskMapper.selectById(id);
        if (task == null) {
            throw exception(REPORT_GENERATION_TASK_NOT_EXISTS);
        }
        if (!TASK_STATUS_SUCCESS.equals(task.getTaskStatus()) || task.getFileUrl() == null) {
            throw exception(REPORT_GENERATION_TASK_STATUS_ERROR);
        }
        String fileName = task.getTaskNo() + "." + lowerCaseFormat(task.getReportFormat());
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader("Content-Disposition",
                "attachment; filename=\"" + fileName + "\"");
        if (task.getFileSize() != null) {
            response.setContentLengthLong(task.getFileSize());
        }
        // 从文件URL读取并写入响应流
        try (InputStream in = new URL(task.getFileUrl()).openStream();
             OutputStream out = response.getOutputStream()) {
            byte[] buffer = new byte[8192];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
        }
    }

    // ==================== 内部辅助方法 ====================

    /**
     * 校验模板存在并返回模板配置
     */
    private ReportTemplateConfigDO validateTemplate(Long templateId) {
        ReportTemplateConfigDO template = reportTemplateConfigMapper.selectById(templateId);
        if (template == null) {
            throw exception(REPORT_TEMPLATE_NOT_EXISTS);
        }
        return template;
    }

    /**
     * 构建报告生成任务（默认待生成状态）
     */
    private ReportGenerationTaskDO buildTask(ReportGenerateReqVO reqVO, Integer reportType,
                                             Long targetId, String targetName,
                                             ReportTemplateConfigDO template) {
        ReportGenerationTaskDO task = new ReportGenerationTaskDO();
        task.setTaskNo(generateTaskNo(reportType));
        task.setTemplateId(template.getId());
        task.setReportType(reportType);
        task.setBatchId(reqVO.getBatchId());
        task.setSchoolId(reqVO.getSchoolId());
        task.setGradeId(reqVO.getGradeId());
        task.setTargetId(targetId);
        task.setTargetName(targetName);
        task.setReportFormat(reqVO.getReportFormat() == null ? DEFAULT_FORMAT : reqVO.getReportFormat());
        task.setTaskStatus(TASK_STATUS_PENDING);
        task.setProgress(0);
        task.setRetryCount(0);
        return task;
    }

    /**
     * 生成任务编号: RG + yyyyMMddHHmmss + 4位随机数
     */
    private String generateTaskNo(Integer reportType) {
        String prefix;
        switch (reportType) {
            case REPORT_TYPE_STUDENT: prefix = "RG-S-"; break;
            case REPORT_TYPE_SCHOOL:  prefix = "RG-H-"; break;
            case REPORT_TYPE_GRADE:   prefix = "RG-G-"; break;
            case REPORT_TYPE_REGION:  prefix = "RG-R-"; break;
            default: prefix = "RG-X-";
        }
        String timePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int random = ThreadLocalRandom.current().nextInt(1000, 10000);
        return prefix + timePart + random;
    }

    /**
     * 报告格式统一转小写（用作文件扩展名）
     */
    private String lowerCaseFormat(String format) {
        if (format == null || format.isEmpty()) {
            return DEFAULT_FORMAT.toLowerCase();
        }
        return format.toLowerCase();
    }

}

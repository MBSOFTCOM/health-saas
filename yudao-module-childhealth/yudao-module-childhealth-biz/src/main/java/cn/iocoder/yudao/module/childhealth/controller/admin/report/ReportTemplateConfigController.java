package cn.iocoder.yudao.module.childhealth.controller.admin.report;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.controller.admin.report.vo.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.report.ReportTemplateConfigDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.report.ReportTemplateConfigMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.REPORT_TEMPLATE_NOT_EXISTS;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.REPORT_TEMPLATE_CODE_DUPLICATE;

/**
 * 管理后台 - 报告模板配置
 *
 * 创建日期: 2026-07-20
 * 模块: 19. 多维度报告体系
 */
@Tag(name = "管理后台 - 报告模板配置")
@RestController
@RequestMapping("/childhealth/report-template")
@Validated
public class ReportTemplateConfigController {

    @Resource
    private ReportTemplateConfigMapper reportTemplateConfigMapper;

    @PostMapping("/create")
    @Operation(summary = "创建报告模板")
    @PreAuthorize("@ss.hasPermission('childhealth:report-template:create')")
    public CommonResult<Long> create(@Valid @RequestBody ReportTemplateConfigSaveReqVO createReqVO) {
        validateCodeUnique(null, createReqVO.getTemplateCode());
        ReportTemplateConfigDO template = BeanUtils.toBean(createReqVO, ReportTemplateConfigDO.class);
        reportTemplateConfigMapper.insert(template);
        return success(template.getId());
    }

    @PutMapping("/update")
    @Operation(summary = "更新报告模板")
    @PreAuthorize("@ss.hasPermission('childhealth:report-template:update')")
    public CommonResult<Boolean> update(@Valid @RequestBody ReportTemplateConfigSaveReqVO updateReqVO) {
        validateExists(updateReqVO.getId());
        validateCodeUnique(updateReqVO.getId(), updateReqVO.getTemplateCode());
        ReportTemplateConfigDO updateObj = BeanUtils.toBean(updateReqVO, ReportTemplateConfigDO.class);
        reportTemplateConfigMapper.updateById(updateObj);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除报告模板")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:report-template:delete')")
    public CommonResult<Boolean> delete(@RequestParam("id") Long id) {
        validateExists(id);
        reportTemplateConfigMapper.deleteById(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得报告模板详情")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:report-template:query')")
    public CommonResult<ReportTemplateConfigRespVO> get(@RequestParam("id") Long id) {
        ReportTemplateConfigDO template = reportTemplateConfigMapper.selectById(id);
        return success(BeanUtils.toBean(template, ReportTemplateConfigRespVO.class));
    }

    @GetMapping("/get-by-code")
    @Operation(summary = "按模板编码查询")
    @Parameter(name = "templateCode", description = "模板编码", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:report-template:query')")
    public CommonResult<ReportTemplateConfigRespVO> getByCode(@RequestParam("templateCode") String templateCode) {
        ReportTemplateConfigDO template = reportTemplateConfigMapper.selectByCode(templateCode);
        return success(BeanUtils.toBean(template, ReportTemplateConfigRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得报告模板分页")
    @PreAuthorize("@ss.hasPermission('childhealth:report-template:query')")
    public CommonResult<PageResult<ReportTemplateConfigRespVO>> page(@Valid ReportTemplateConfigPageReqVO pageReqVO) {
        PageResult<ReportTemplateConfigDO> pageResult = reportTemplateConfigMapper.selectPage(pageReqVO,
                new LambdaQueryWrapperX<ReportTemplateConfigDO>()
                        .likeIfPresent(ReportTemplateConfigDO::getTemplateCode, pageReqVO.getTemplateCode())
                        .likeIfPresent(ReportTemplateConfigDO::getTemplateName, pageReqVO.getTemplateName())
                        .eqIfPresent(ReportTemplateConfigDO::getReportType, pageReqVO.getReportType())
                        .eqIfPresent(ReportTemplateConfigDO::getStatus, pageReqVO.getStatus())
                        .orderByAsc(ReportTemplateConfigDO::getReportType)
                        .orderByAsc(ReportTemplateConfigDO::getSortOrder));
        return success(BeanUtils.toBean(pageResult, ReportTemplateConfigRespVO.class));
    }

    @GetMapping("/list-by-type")
    @Operation(summary = "按报告类型查询所有启用模板")
    @Parameter(name = "reportType", description = "报告类型 1学生 2学校 3年级 4区域", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:report-template:query')")
    public CommonResult<List<ReportTemplateConfigRespVO>> listByType(@RequestParam("reportType") Integer reportType) {
        List<ReportTemplateConfigDO> list = reportTemplateConfigMapper.selectListByType(reportType);
        return success(BeanUtils.toBean(list, ReportTemplateConfigRespVO.class));
    }

    @GetMapping("/default-by-type")
    @Operation(summary = "按报告类型查询默认模板")
    @Parameter(name = "reportType", description = "报告类型", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:report-template:query')")
    public CommonResult<ReportTemplateConfigRespVO> defaultByType(@RequestParam("reportType") Integer reportType) {
        ReportTemplateConfigDO template = reportTemplateConfigMapper.selectDefaultByType(reportType);
        return success(BeanUtils.toBean(template, ReportTemplateConfigRespVO.class));
    }

    @GetMapping("/active-list")
    @Operation(summary = "查询所有启用的报告模板")
    @PreAuthorize("@ss.hasPermission('childhealth:report-template:query')")
    public CommonResult<List<ReportTemplateConfigRespVO>> activeList() {
        List<ReportTemplateConfigDO> list = reportTemplateConfigMapper.selectActiveList();
        return success(BeanUtils.toBean(list, ReportTemplateConfigRespVO.class));
    }

    private void validateExists(Long id) {
        if (id == null || reportTemplateConfigMapper.selectById(id) == null) {
            throw exception(REPORT_TEMPLATE_NOT_EXISTS);
        }
    }

    private void validateCodeUnique(Long id, String templateCode) {
        ReportTemplateConfigDO template = reportTemplateConfigMapper.selectByCode(templateCode);
        if (template == null) {
            return;
        }
        if (id == null || !template.getId().equals(id)) {
            throw exception(REPORT_TEMPLATE_CODE_DUPLICATE);
        }
    }

}

package cn.iocoder.yudao.module.childhealth.controller.admin.medical;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.medical.vo.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.medical.DevelopmentAssessmentRecordDO;
import cn.iocoder.yudao.module.childhealth.service.medical.DevelopmentAssessmentRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 发育评估记录")
@RestController
@RequestMapping("/childhealth/development-assessment-record")
@Validated
public class DevelopmentAssessmentRecordController {

    @Resource
    private DevelopmentAssessmentRecordService developmentAssessmentRecordService;

    @PostMapping("/create")
    @Operation(summary = "创建发育评估记录")
    @PreAuthorize("@ss.hasPermission('childhealth:development-assessment-record:create')")
    public CommonResult<Long> createDevelopmentAssessmentRecord(@Valid @RequestBody DevelopmentAssessmentRecordSaveReqVO createReqVO) {
        return success(developmentAssessmentRecordService.createDevelopmentAssessmentRecord(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新发育评估记录")
    @PreAuthorize("@ss.hasPermission('childhealth:development-assessment-record:update')")
    public CommonResult<Boolean> updateDevelopmentAssessmentRecord(@Valid @RequestBody DevelopmentAssessmentRecordSaveReqVO updateReqVO) {
        developmentAssessmentRecordService.updateDevelopmentAssessmentRecord(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除发育评估记录")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:development-assessment-record:delete')")
    public CommonResult<Boolean> deleteDevelopmentAssessmentRecord(@RequestParam("id") Long id) {
        developmentAssessmentRecordService.deleteDevelopmentAssessmentRecord(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得发育评估记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:development-assessment-record:query')")
    public CommonResult<DevelopmentAssessmentRecordRespVO> getDevelopmentAssessmentRecord(@RequestParam("id") Long id) {
        DevelopmentAssessmentRecordDO developmentAssessmentRecord = developmentAssessmentRecordService.getDevelopmentAssessmentRecord(id);
        return success(BeanUtils.toBean(developmentAssessmentRecord, DevelopmentAssessmentRecordRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得发育评估记录分页")
    @PreAuthorize("@ss.hasPermission('childhealth:development-assessment-record:query')")
    public CommonResult<PageResult<DevelopmentAssessmentRecordRespVO>> getDevelopmentAssessmentRecordPage(@Valid DevelopmentAssessmentRecordPageReqVO pageReqVO) {
        PageResult<DevelopmentAssessmentRecordDO> pageResult = developmentAssessmentRecordService.getDevelopmentAssessmentRecordPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, DevelopmentAssessmentRecordRespVO.class));
    }

    @PostMapping("/calculate-score")
    @Operation(summary = "自动计分：根据答题情况自动计算总分、各维度得分、风险等级")
    @PreAuthorize("@ss.hasPermission('childhealth:development-assessment-record:update')")
    public CommonResult<Boolean> calculateScore(@RequestParam("recordId") Long recordId) {
        developmentAssessmentRecordService.autoCalculateScore(recordId);
        return success(true);
    }

    @PostMapping("/generate-report")
    @Operation(summary = "生成报告：根据评估结果生成 PDF 报告")
    @PreAuthorize("@ss.hasPermission('childhealth:development-assessment-record:update')")
    public CommonResult<String> generateReport(@RequestParam("recordId") Long recordId) {
        return success(developmentAssessmentRecordService.generateReport(recordId));
    }

}

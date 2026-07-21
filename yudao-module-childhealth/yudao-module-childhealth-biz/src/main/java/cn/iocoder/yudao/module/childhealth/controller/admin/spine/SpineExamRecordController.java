package cn.iocoder.yudao.module.childhealth.controller.admin.spine;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.spine.SpineExamRecordDO;
import cn.iocoder.yudao.module.childhealth.service.spine.SpineExamRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

/**
 * 脊柱骨骼筛查 Controller
 */
@Tag(name = "管理后台 - 脊柱骨骼筛查")
@RestController
@RequestMapping("/childhealth/spineExamRecord")
@Validated
public class SpineExamRecordController {

    @Resource
    private SpineExamRecordService spineExamRecordService;

    @PostMapping("/create")
    @Operation(summary = "新增脊柱骨骼筛查记录")
    @PreAuthorize("@ss.hasPermission('childhealth:spineExamRecord:create')")
    public CommonResult<Long> createSpineExamRecord(@Valid @RequestBody SpineExamRecordDO record) {
        return success(spineExamRecordService.createSpineExamRecord(record));
    }

    @PutMapping("/update")
    @Operation(summary = "修改脊柱骨骼筛查记录")
    @PreAuthorize("@ss.hasPermission('childhealth:spineExamRecord:update')")
    public CommonResult<Boolean> updateSpineExamRecord(@Valid @RequestBody SpineExamRecordDO record) {
        spineExamRecordService.updateSpineExamRecord(record);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除脊柱骨骼筛查记录")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:spineExamRecord:delete')")
    public CommonResult<Boolean> deleteSpineExamRecord(@RequestParam("id") Long id) {
        spineExamRecordService.deleteSpineExamRecord(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "查询脊柱骨骼筛查记录详情")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:spineExamRecord:query')")
    public CommonResult<SpineExamRecordDO> getSpineExamRecord(@RequestParam("id") Long id) {
        return success(spineExamRecordService.getSpineExamRecord(id));
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询脊柱骨骼筛查记录")
    @PreAuthorize("@ss.hasPermission('childhealth:spineExamRecord:query')")
    public CommonResult<PageResult<SpineExamRecordDO>> getSpineExamRecordPage(@Valid PageParam pageParam,
                                                                              @RequestParam(value = "examId", required = false) Long examId,
                                                                              @RequestParam(value = "childId", required = false) Long childId,
                                                                              @RequestParam(value = "riskLevel", required = false) Integer riskLevel) {
        return success(spineExamRecordService.getSpineExamRecordPage(pageParam, examId, childId, riskLevel));
    }

    @PostMapping("/auto-assess")
    @Operation(summary = "智能评估脊柱侧弯风险（基于 ATR 角度/Cobb 角/足弓指数自动判定）")
    @PreAuthorize("@ss.hasPermission('childhealth:spineExamRecord:query')")
    public CommonResult<SpineExamRecordDO> autoAssessSpineRisk(@Valid @RequestBody SpineExamRecordDO record) {
        return success(spineExamRecordService.autoAssessSpineRisk(record));
    }
}

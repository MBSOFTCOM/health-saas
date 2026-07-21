package cn.iocoder.yudao.module.childhealth.controller.admin.ops;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.controller.admin.ops.vo.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.ops.OpsIndicatorSnapshotDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.ops.OpsIndicatorSnapshotMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.OPS_INDICATOR_SNAPSHOT_NOT_EXISTS;

/**
 * 管理后台 - 运营指标快照
 *
 * 创建日期: 2026-07-20
 * 模块: 1. 数据看板 + 12. 运营工作台
 */
@Tag(name = "管理后台 - 运营指标快照")
@RestController
@RequestMapping("/childhealth/ops-indicator-snapshot")
@Validated
public class OpsIndicatorSnapshotController {

    @Resource
    private OpsIndicatorSnapshotMapper opsIndicatorSnapshotMapper;

    @PostMapping("/create")
    @Operation(summary = "创建运营指标快照")
    @PreAuthorize("@ss.hasPermission('childhealth:ops-indicator-snapshot:create')")
    public CommonResult<Long> create(@Valid @RequestBody OpsIndicatorSnapshotSaveReqVO createReqVO) {
        OpsIndicatorSnapshotDO snapshot = BeanUtils.toBean(createReqVO, OpsIndicatorSnapshotDO.class);
        opsIndicatorSnapshotMapper.insert(snapshot);
        return success(snapshot.getId());
    }

    @PutMapping("/update")
    @Operation(summary = "更新运营指标快照")
    @PreAuthorize("@ss.hasPermission('childhealth:ops-indicator-snapshot:update')")
    public CommonResult<Boolean> update(@Valid @RequestBody OpsIndicatorSnapshotSaveReqVO updateReqVO) {
        validateExists(updateReqVO.getId());
        OpsIndicatorSnapshotDO updateObj = BeanUtils.toBean(updateReqVO, OpsIndicatorSnapshotDO.class);
        opsIndicatorSnapshotMapper.updateById(updateObj);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除运营指标快照")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:ops-indicator-snapshot:delete')")
    public CommonResult<Boolean> delete(@RequestParam("id") Long id) {
        validateExists(id);
        opsIndicatorSnapshotMapper.deleteById(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得运营指标快照详情")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:ops-indicator-snapshot:query')")
    public CommonResult<OpsIndicatorSnapshotRespVO> get(@RequestParam("id") Long id) {
        OpsIndicatorSnapshotDO snapshot = opsIndicatorSnapshotMapper.selectById(id);
        return success(BeanUtils.toBean(snapshot, OpsIndicatorSnapshotRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得运营指标快照分页")
    @PreAuthorize("@ss.hasPermission('childhealth:ops-indicator-snapshot:query')")
    public CommonResult<PageResult<OpsIndicatorSnapshotRespVO>> page(@Valid OpsIndicatorSnapshotPageReqVO pageReqVO) {
        PageResult<OpsIndicatorSnapshotDO> pageResult = opsIndicatorSnapshotMapper.selectPage(pageReqVO,
                new LambdaQueryWrapperX<OpsIndicatorSnapshotDO>()
                        .geIfPresent(OpsIndicatorSnapshotDO::getSnapshotDate, pageReqVO.getSnapshotDateStart())
                        .leIfPresent(OpsIndicatorSnapshotDO::getSnapshotDate, pageReqVO.getSnapshotDateEnd())
                        .eqIfPresent(OpsIndicatorSnapshotDO::getBatchId, pageReqVO.getBatchId())
                        .eqIfPresent(OpsIndicatorSnapshotDO::getSchoolId, pageReqVO.getSchoolId())
                        .eqIfPresent(OpsIndicatorSnapshotDO::getGradeId, pageReqVO.getGradeId())
                        .eqIfPresent(OpsIndicatorSnapshotDO::getRegionCode, pageReqVO.getRegionCode())
                        .orderByDesc(OpsIndicatorSnapshotDO::getSnapshotDate));
        return success(BeanUtils.toBean(pageResult, OpsIndicatorSnapshotRespVO.class));
    }

    @GetMapping("/trend")
    @Operation(summary = "查询趋势数据（看板趋势图使用）")
    @PreAuthorize("@ss.hasPermission('childhealth:ops-indicator-snapshot:query')")
    public CommonResult<List<OpsIndicatorSnapshotRespVO>> trend(
            @RequestParam(value = "startDate") LocalDate startDate,
            @RequestParam(value = "endDate") LocalDate endDate,
            @RequestParam(value = "batchId", required = false) Long batchId) {
        List<OpsIndicatorSnapshotDO> list = opsIndicatorSnapshotMapper.selectListByDateRange(startDate, endDate, batchId);
        return success(BeanUtils.toBean(list, OpsIndicatorSnapshotRespVO.class));
    }

    @GetMapping("/latest-by-school")
    @Operation(summary = "按学校查询最新快照")
    @PreAuthorize("@ss.hasPermission('childhealth:ops-indicator-snapshot:query')")
    public CommonResult<OpsIndicatorSnapshotRespVO> latestBySchool(
            @RequestParam("schoolId") Long schoolId,
            @RequestParam(value = "batchId", required = false) Long batchId) {
        OpsIndicatorSnapshotDO snapshot = opsIndicatorSnapshotMapper.selectLatestBySchool(schoolId, batchId);
        return success(BeanUtils.toBean(snapshot, OpsIndicatorSnapshotRespVO.class));
    }

    private void validateExists(Long id) {
        if (id == null || opsIndicatorSnapshotMapper.selectById(id) == null) {
            throw exception(OPS_INDICATOR_SNAPSHOT_NOT_EXISTS);
        }
    }

}

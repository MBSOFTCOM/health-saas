package cn.iocoder.yudao.module.childhealth.controller.admin.ops;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.controller.admin.ops.vo.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.ops.StaffWorkloadStatisticsDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.ops.StaffWorkloadStatisticsMapper;
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
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.STAFF_WORKLOAD_STATISTICS_NOT_EXISTS;

/**
 * 管理后台 - 工作量统计
 *
 * 创建日期: 2026-07-20
 * 模块: 20. 全维度数据统计
 */
@Tag(name = "管理后台 - 工作量统计")
@RestController
@RequestMapping("/childhealth/staff-workload-statistics")
@Validated
public class StaffWorkloadStatisticsController {

    @Resource
    private StaffWorkloadStatisticsMapper staffWorkloadStatisticsMapper;

    @PostMapping("/create")
    @Operation(summary = "创建工作量统计记录")
    @PreAuthorize("@ss.hasPermission('childhealth:staff-workload-statistics:create')")
    public CommonResult<Long> create(@Valid @RequestBody StaffWorkloadStatisticsSaveReqVO createReqVO) {
        StaffWorkloadStatisticsDO record = BeanUtils.toBean(createReqVO, StaffWorkloadStatisticsDO.class);
        staffWorkloadStatisticsMapper.insert(record);
        return success(record.getId());
    }

    @PutMapping("/update")
    @Operation(summary = "更新工作量统计记录")
    @PreAuthorize("@ss.hasPermission('childhealth:staff-workload-statistics:update')")
    public CommonResult<Boolean> update(@Valid @RequestBody StaffWorkloadStatisticsSaveReqVO updateReqVO) {
        validateExists(updateReqVO.getId());
        StaffWorkloadStatisticsDO updateObj = BeanUtils.toBean(updateReqVO, StaffWorkloadStatisticsDO.class);
        staffWorkloadStatisticsMapper.updateById(updateObj);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除工作量统计记录")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:staff-workload-statistics:delete')")
    public CommonResult<Boolean> delete(@RequestParam("id") Long id) {
        validateExists(id);
        staffWorkloadStatisticsMapper.deleteById(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得工作量统计详情")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:staff-workload-statistics:query')")
    public CommonResult<StaffWorkloadStatisticsRespVO> get(@RequestParam("id") Long id) {
        StaffWorkloadStatisticsDO record = staffWorkloadStatisticsMapper.selectById(id);
        return success(BeanUtils.toBean(record, StaffWorkloadStatisticsRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得工作量统计分页")
    @PreAuthorize("@ss.hasPermission('childhealth:staff-workload-statistics:query')")
    public CommonResult<PageResult<StaffWorkloadStatisticsRespVO>> page(@Valid StaffWorkloadStatisticsPageReqVO pageReqVO) {
        PageResult<StaffWorkloadStatisticsDO> pageResult = staffWorkloadStatisticsMapper.selectPage(pageReqVO,
                new LambdaQueryWrapperX<StaffWorkloadStatisticsDO>()
                        .geIfPresent(StaffWorkloadStatisticsDO::getStatDate, pageReqVO.getStatDateStart())
                        .leIfPresent(StaffWorkloadStatisticsDO::getStatDate, pageReqVO.getStatDateEnd())
                        .eqIfPresent(StaffWorkloadStatisticsDO::getStaffId, pageReqVO.getStaffId())
                        .eqIfPresent(StaffWorkloadStatisticsDO::getDeptId, pageReqVO.getDeptId())
                        .eqIfPresent(StaffWorkloadStatisticsDO::getOrgId, pageReqVO.getOrgId())
                        .eqIfPresent(StaffWorkloadStatisticsDO::getSchoolId, pageReqVO.getSchoolId())
                        .orderByDesc(StaffWorkloadStatisticsDO::getStatDate));
        return success(BeanUtils.toBean(pageResult, StaffWorkloadStatisticsRespVO.class));
    }

    @GetMapping("/trend-by-staff")
    @Operation(summary = "按医护查询工作量趋势")
    @PreAuthorize("@ss.hasPermission('childhealth:staff-workload-statistics:query')")
    public CommonResult<List<StaffWorkloadStatisticsRespVO>> trendByStaff(
            @RequestParam("staffId") Long staffId,
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate) {
        List<StaffWorkloadStatisticsDO> list = staffWorkloadStatisticsMapper.selectListByStaffAndDateRange(staffId, startDate, endDate);
        return success(BeanUtils.toBean(list, StaffWorkloadStatisticsRespVO.class));
    }

    @GetMapping("/rank-by-org")
    @Operation(summary = "按机构查询当日工作量排名")
    @PreAuthorize("@ss.hasPermission('childhealth:staff-workload-statistics:query')")
    public CommonResult<List<StaffWorkloadStatisticsRespVO>> rankByOrg(
            @RequestParam("orgId") Long orgId,
            @RequestParam("statDate") LocalDate statDate) {
        List<StaffWorkloadStatisticsDO> list = staffWorkloadStatisticsMapper.selectListByOrgAndDate(orgId, statDate);
        return success(BeanUtils.toBean(list, StaffWorkloadStatisticsRespVO.class));
    }

    private void validateExists(Long id) {
        if (id == null || staffWorkloadStatisticsMapper.selectById(id) == null) {
            throw exception(STAFF_WORKLOAD_STATISTICS_NOT_EXISTS);
        }
    }

}

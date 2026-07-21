package cn.iocoder.yudao.module.childhealth.controller.admin.screening;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.recheck.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.RecheckFollowTimelineDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.screening.RecheckFollowTimelineMapper;
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
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.RECHECK_FOLLOW_TIMELINE_NOT_EXISTS;

/**
 * 管理后台 - 复筛随访时间轴
 *
 * 创建日期: 2026-07-20
 * 模块: 13. 复筛专项管理 + 20. 全维度数据统计
 */
@Tag(name = "管理后台 - 复筛随访时间轴")
@RestController
@RequestMapping("/childhealth/recheck-follow-timeline")
@Validated
public class RecheckFollowTimelineController {

    @Resource
    private RecheckFollowTimelineMapper recheckFollowTimelineMapper;

    @PostMapping("/create")
    @Operation(summary = "创建时间轴事件")
    @PreAuthorize("@ss.hasPermission('childhealth:recheck-follow-timeline:create')")
    public CommonResult<Long> create(@Valid @RequestBody RecheckFollowTimelineSaveReqVO createReqVO) {
        RecheckFollowTimelineDO timeline = BeanUtils.toBean(createReqVO, RecheckFollowTimelineDO.class);
        recheckFollowTimelineMapper.insert(timeline);
        return success(timeline.getId());
    }

    @PutMapping("/update")
    @Operation(summary = "更新时间轴事件")
    @PreAuthorize("@ss.hasPermission('childhealth:recheck-follow-timeline:update')")
    public CommonResult<Boolean> update(@Valid @RequestBody RecheckFollowTimelineSaveReqVO updateReqVO) {
        validateExists(updateReqVO.getId());
        RecheckFollowTimelineDO updateObj = BeanUtils.toBean(updateReqVO, RecheckFollowTimelineDO.class);
        recheckFollowTimelineMapper.updateById(updateObj);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除时间轴事件")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:recheck-follow-timeline:delete')")
    public CommonResult<Boolean> delete(@RequestParam("id") Long id) {
        validateExists(id);
        recheckFollowTimelineMapper.deleteById(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得时间轴事件")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:recheck-follow-timeline:query')")
    public CommonResult<RecheckFollowTimelineRespVO> get(@RequestParam("id") Long id) {
        RecheckFollowTimelineDO timeline = recheckFollowTimelineMapper.selectById(id);
        return success(BeanUtils.toBean(timeline, RecheckFollowTimelineRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得时间轴分页")
    @PreAuthorize("@ss.hasPermission('childhealth:recheck-follow-timeline:query')")
    public CommonResult<PageResult<RecheckFollowTimelineRespVO>> page(@Valid RecheckFollowTimelinePageReqVO pageReqVO) {
        PageResult<RecheckFollowTimelineDO> pageResult = recheckFollowTimelineMapper.selectPage(pageReqVO,
                new LambdaQueryWrapperX<RecheckFollowTimelineDO>()
                        .eqIfPresent(RecheckFollowTimelineDO::getPositiveId, pageReqVO.getPositiveId())
                        .eqIfPresent(RecheckFollowTimelineDO::getStudentId, pageReqVO.getStudentId())
                        .eqIfPresent(RecheckFollowTimelineDO::getEventType, pageReqVO.getEventType())
                        .geIfPresent(RecheckFollowTimelineDO::getEventTime, pageReqVO.getEventTimeStart())
                        .leIfPresent(RecheckFollowTimelineDO::getEventTime, pageReqVO.getEventTimeEnd())
                        .orderByDesc(RecheckFollowTimelineDO::getEventTime));
        return success(BeanUtils.toBean(pageResult, RecheckFollowTimelineRespVO.class));
    }

    @GetMapping("/timeline-by-positive")
    @Operation(summary = "按阳性记录ID查询完整时间轴（复筛工作台时间轴展示）")
    @Parameter(name = "positiveId", description = "阳性记录ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:recheck-follow-timeline:query')")
    public CommonResult<List<RecheckFollowTimelineRespVO>> timelineByPositive(@RequestParam("positiveId") Long positiveId) {
        List<RecheckFollowTimelineDO> list = recheckFollowTimelineMapper.selectListByPositive(positiveId);
        return success(BeanUtils.toBean(list, RecheckFollowTimelineRespVO.class));
    }

    @GetMapping("/timeline-by-student")
    @Operation(summary = "按学生ID查询历史时间轴")
    @Parameter(name = "studentId", description = "学生ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:recheck-follow-timeline:query')")
    public CommonResult<List<RecheckFollowTimelineRespVO>> timelineByStudent(@RequestParam("studentId") Long studentId) {
        List<RecheckFollowTimelineDO> list = recheckFollowTimelineMapper.selectListByStudent(studentId);
        return success(BeanUtils.toBean(list, RecheckFollowTimelineRespVO.class));
    }

    private void validateExists(Long id) {
        if (id == null || recheckFollowTimelineMapper.selectById(id) == null) {
            throw exception(RECHECK_FOLLOW_TIMELINE_NOT_EXISTS);
        }
    }

}

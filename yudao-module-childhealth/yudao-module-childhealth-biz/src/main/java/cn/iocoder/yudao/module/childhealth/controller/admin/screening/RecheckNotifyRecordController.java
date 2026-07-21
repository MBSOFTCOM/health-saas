package cn.iocoder.yudao.module.childhealth.controller.admin.screening;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.recheck.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.RecheckNotifyRecordDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.screening.RecheckNotifyRecordMapper;
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
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.RECHECK_NOTIFY_RECORD_NOT_EXISTS;

/**
 * 管理后台 - 复筛通知记录
 *
 * 创建日期: 2026-07-20
 * 模块: 13. 复筛专项管理
 */
@Tag(name = "管理后台 - 复筛通知记录")
@RestController
@RequestMapping("/childhealth/recheck-notify-record")
@Validated
public class RecheckNotifyRecordController {

    @Resource
    private RecheckNotifyRecordMapper recheckNotifyRecordMapper;

    @PostMapping("/create")
    @Operation(summary = "创建复筛通知记录")
    @PreAuthorize("@ss.hasPermission('childhealth:recheck-notify-record:create')")
    public CommonResult<Long> create(@Valid @RequestBody RecheckNotifyRecordSaveReqVO createReqVO) {
        RecheckNotifyRecordDO record = BeanUtils.toBean(createReqVO, RecheckNotifyRecordDO.class);
        recheckNotifyRecordMapper.insert(record);
        return success(record.getId());
    }

    @PutMapping("/update")
    @Operation(summary = "更新复筛通知记录")
    @PreAuthorize("@ss.hasPermission('childhealth:recheck-notify-record:update')")
    public CommonResult<Boolean> update(@Valid @RequestBody RecheckNotifyRecordSaveReqVO updateReqVO) {
        validateExists(updateReqVO.getId());
        RecheckNotifyRecordDO updateObj = BeanUtils.toBean(updateReqVO, RecheckNotifyRecordDO.class);
        recheckNotifyRecordMapper.updateById(updateObj);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除复筛通知记录")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:recheck-notify-record:delete')")
    public CommonResult<Boolean> delete(@RequestParam("id") Long id) {
        validateExists(id);
        recheckNotifyRecordMapper.deleteById(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得复筛通知记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:recheck-notify-record:query')")
    public CommonResult<RecheckNotifyRecordRespVO> get(@RequestParam("id") Long id) {
        RecheckNotifyRecordDO record = recheckNotifyRecordMapper.selectById(id);
        return success(BeanUtils.toBean(record, RecheckNotifyRecordRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得复筛通知记录分页")
    @PreAuthorize("@ss.hasPermission('childhealth:recheck-notify-record:query')")
    public CommonResult<PageResult<RecheckNotifyRecordRespVO>> page(@Valid RecheckNotifyRecordPageReqVO pageReqVO) {
        PageResult<RecheckNotifyRecordDO> pageResult = recheckNotifyRecordMapper.selectPage(pageReqVO,
                new LambdaQueryWrapperX<RecheckNotifyRecordDO>()
                        .eqIfPresent(RecheckNotifyRecordDO::getPositiveId, pageReqVO.getPositiveId())
                        .eqIfPresent(RecheckNotifyRecordDO::getStudentId, pageReqVO.getStudentId())
                        .eqIfPresent(RecheckNotifyRecordDO::getSchoolId, pageReqVO.getSchoolId())
                        .eqIfPresent(RecheckNotifyRecordDO::getClassId, pageReqVO.getClassId())
                        .eqIfPresent(RecheckNotifyRecordDO::getNotifyChannel, pageReqVO.getNotifyChannel())
                        .eqIfPresent(RecheckNotifyRecordDO::getNotifyType, pageReqVO.getNotifyType())
                        .eqIfPresent(RecheckNotifyRecordDO::getNotifyStatus, pageReqVO.getNotifyStatus())
                        .geIfPresent(RecheckNotifyRecordDO::getNotifyTime, pageReqVO.getNotifyTimeStart())
                        .leIfPresent(RecheckNotifyRecordDO::getNotifyTime, pageReqVO.getNotifyTimeEnd())
                        .orderByDesc(RecheckNotifyRecordDO::getNotifyTime));
        return success(BeanUtils.toBean(pageResult, RecheckNotifyRecordRespVO.class));
    }

    @GetMapping("/list-by-positive")
    @Operation(summary = "按阳性记录ID查询所有通知记录")
    @Parameter(name = "positiveId", description = "阳性记录ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:recheck-notify-record:query')")
    public CommonResult<List<RecheckNotifyRecordRespVO>> listByPositive(@RequestParam("positiveId") Long positiveId) {
        List<RecheckNotifyRecordDO> list = recheckNotifyRecordMapper.selectListByPositive(positiveId);
        return success(BeanUtils.toBean(list, RecheckNotifyRecordRespVO.class));
    }

    @GetMapping("/list-by-school")
    @Operation(summary = "按学校ID查询通知记录（复筛工作台）")
    @Parameter(name = "schoolId", description = "学校ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:recheck-notify-record:query')")
    public CommonResult<List<RecheckNotifyRecordRespVO>> listBySchool(@RequestParam("schoolId") Long schoolId) {
        List<RecheckNotifyRecordDO> list = recheckNotifyRecordMapper.selectListBySchool(schoolId);
        return success(BeanUtils.toBean(list, RecheckNotifyRecordRespVO.class));
    }

    private void validateExists(Long id) {
        if (id == null || recheckNotifyRecordMapper.selectById(id) == null) {
            throw exception(RECHECK_NOTIFY_RECORD_NOT_EXISTS);
        }
    }

}

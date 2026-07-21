package cn.iocoder.yudao.module.childhealth.controller.admin.screening;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.rule.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.PositiveRuleExecLogDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.screening.PositiveRuleExecLogMapper;
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
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.POSITIVE_RULE_EXEC_LOG_NOT_EXISTS;

/**
 * 管理后台 - 阳性规则执行日志
 *
 * 创建日期: 2026-07-20
 * 模块: 7. 阳性自动识别规则引擎
 */
@Tag(name = "管理后台 - 阳性规则执行日志")
@RestController
@RequestMapping("/childhealth/positive-rule-exec-log")
@Validated
public class PositiveRuleExecLogController {

    @Resource
    private PositiveRuleExecLogMapper positiveRuleExecLogMapper;

    @PostMapping("/create")
    @Operation(summary = "创建阳性规则执行日志")
    @PreAuthorize("@ss.hasPermission('childhealth:positive-rule-exec-log:create')")
    public CommonResult<Long> create(@Valid @RequestBody PositiveRuleExecLogSaveReqVO createReqVO) {
        PositiveRuleExecLogDO log = BeanUtils.toBean(createReqVO, PositiveRuleExecLogDO.class);
        positiveRuleExecLogMapper.insert(log);
        return success(log.getId());
    }

    @PutMapping("/update")
    @Operation(summary = "更新阳性规则执行日志")
    @PreAuthorize("@ss.hasPermission('childhealth:positive-rule-exec-log:update')")
    public CommonResult<Boolean> update(@Valid @RequestBody PositiveRuleExecLogSaveReqVO updateReqVO) {
        validateExists(updateReqVO.getId());
        PositiveRuleExecLogDO updateObj = BeanUtils.toBean(updateReqVO, PositiveRuleExecLogDO.class);
        positiveRuleExecLogMapper.updateById(updateObj);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除阳性规则执行日志")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:positive-rule-exec-log:delete')")
    public CommonResult<Boolean> delete(@RequestParam("id") Long id) {
        validateExists(id);
        positiveRuleExecLogMapper.deleteById(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得阳性规则执行日志")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:positive-rule-exec-log:query')")
    public CommonResult<PositiveRuleExecLogRespVO> get(@RequestParam("id") Long id) {
        PositiveRuleExecLogDO log = positiveRuleExecLogMapper.selectById(id);
        return success(BeanUtils.toBean(log, PositiveRuleExecLogRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得阳性规则执行日志分页")
    @PreAuthorize("@ss.hasPermission('childhealth:positive-rule-exec-log:query')")
    public CommonResult<PageResult<PositiveRuleExecLogRespVO>> page(@Valid PositiveRuleExecLogPageReqVO pageReqVO) {
        PageResult<PositiveRuleExecLogDO> pageResult = positiveRuleExecLogMapper.selectPage(pageReqVO,
                buildWrapper(pageReqVO));
        return success(BeanUtils.toBean(pageResult, PositiveRuleExecLogRespVO.class));
    }

    @GetMapping("/list-by-record")
    @Operation(summary = "按筛查记录ID查询所有命中日志")
    @Parameter(name = "recordId", description = "筛查记录ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:positive-rule-exec-log:query')")
    public CommonResult<List<PositiveRuleExecLogRespVO>> listByRecord(@RequestParam("recordId") Long recordId) {
        List<PositiveRuleExecLogDO> list = positiveRuleExecLogMapper.selectListByRecord(recordId);
        return success(BeanUtils.toBean(list, PositiveRuleExecLogRespVO.class));
    }

    @GetMapping("/list-by-rule")
    @Operation(summary = "按规则ID分页查询命中日志（用于规则调优分析）")
    @PreAuthorize("@ss.hasPermission('childhealth:positive-rule-exec-log:query')")
    public CommonResult<PageResult<PositiveRuleExecLogRespVO>> listByRule(
            @RequestParam("ruleId") Long ruleId,
            @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {
        PageResult<PositiveRuleExecLogDO> pageResult = positiveRuleExecLogMapper.selectPageByRule(ruleId, pageNo, pageSize);
        return success(BeanUtils.toBean(pageResult, PositiveRuleExecLogRespVO.class));
    }

    @GetMapping("/count-by-rule")
    @Operation(summary = "统计规则命中次数")
    @Parameter(name = "ruleId", description = "规则ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:positive-rule-exec-log:query')")
    public CommonResult<Long> countByRule(@RequestParam("ruleId") Long ruleId) {
        return success(positiveRuleExecLogMapper.countByRule(ruleId));
    }

    private void validateExists(Long id) {
        if (id == null || positiveRuleExecLogMapper.selectById(id) == null) {
            throw exception(POSITIVE_RULE_EXEC_LOG_NOT_EXISTS);
        }
    }

    private cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX<PositiveRuleExecLogDO> buildWrapper(PositiveRuleExecLogPageReqVO reqVO) {
        return new cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX<PositiveRuleExecLogDO>()
                .eqIfPresent(PositiveRuleExecLogDO::getRuleId, reqVO.getRuleId())
                .eqIfPresent(PositiveRuleExecLogDO::getRecordId, reqVO.getRecordId())
                .eqIfPresent(PositiveRuleExecLogDO::getStudentId, reqVO.getStudentId())
                .eqIfPresent(PositiveRuleExecLogDO::getPositiveLevel, reqVO.getPositiveLevel())
                .eqIfPresent(PositiveRuleExecLogDO::getIsPositive, reqVO.getIsPositive())
                .geIfPresent(PositiveRuleExecLogDO::getMatchedAt, reqVO.getMatchedTimeStart())
                .leIfPresent(PositiveRuleExecLogDO::getMatchedAt, reqVO.getMatchedTimeEnd())
                .orderByDesc(PositiveRuleExecLogDO::getMatchedAt);
    }

}

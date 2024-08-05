package cn.iocoder.yudao.module.ppd.controller.admin.screenrepeatperson;


import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.ppd.controller.admin.screenrepeatperson.vo.ScreenRepeatPersonPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenrepeatperson.vo.ScreenRepeatPersonRespVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenrepeatperson.vo.ScreenRepeatPersonSaveReqVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenrepeatperson.ScreenRepeatPersonDO;
import cn.iocoder.yudao.module.ppd.service.screenpersonrealsituation.ScreenPersonService;
import cn.iocoder.yudao.module.ppd.service.screenrepeatperson.ScreenRepeatPersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 重复筛查人员管理")
@RestController
@RequestMapping("/tb/screen-repeat-person")
@Validated
public class ScreenRepeatPersonController {

    @Resource
    private ScreenRepeatPersonService screenRepeatPersonService;
    @Resource
    private ScreenPersonService screenPersonService;

    @PostMapping("/create")
    @Operation(summary = "创建重复筛查人员管理")
    @PreAuthorize("@ss.hasPermission('tb:screen-repeat-person:create')")
    public CommonResult<Long> createScreenRepeatPerson(@Valid @RequestBody ScreenRepeatPersonSaveReqVO createReqVO) {
        return success(screenRepeatPersonService.createScreenRepeatPerson(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新重复筛查人员管理")
    @PreAuthorize("@ss.hasPermission('tb:screen-repeat-person:update')")
    public CommonResult<Boolean> updateScreenRepeatPerson(@Valid @RequestBody ScreenRepeatPersonSaveReqVO updateReqVO) {
        screenRepeatPersonService.updateScreenRepeatPerson(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除重复筛查人员管理")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('tb:screen-repeat-person:delete')")
    public CommonResult<Boolean> deleteScreenRepeatPerson(@RequestParam("id") Long id) {
        screenRepeatPersonService.deleteScreenRepeatPerson(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得重复筛查人员管理")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('tb:screen-repeat-person:query')")
    public CommonResult<ScreenRepeatPersonRespVO> getScreenRepeatPerson(@RequestParam("id") Long id) {
        ScreenRepeatPersonDO screenRepeatPerson = screenRepeatPersonService.getScreenRepeatPerson(id);
        return success(BeanUtils.toBean(screenRepeatPerson, ScreenRepeatPersonRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得重复筛查人员管理分页")
    @PreAuthorize("@ss.hasPermission('tb:screen-repeat-person:query')")
    public CommonResult<PageResult<ScreenRepeatPersonRespVO>> getScreenRepeatPersonPage(@Valid ScreenRepeatPersonPageReqVO pageReqVO) {
        PageResult<ScreenRepeatPersonDO> pageResult = screenRepeatPersonService.getScreenRepeatPersonPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ScreenRepeatPersonRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出重复筛查人员管理 Excel")
    @PreAuthorize("@ss.hasPermission('tb:screen-repeat-person:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportScreenRepeatPersonExcel(@Valid ScreenRepeatPersonPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ScreenRepeatPersonDO> list = screenRepeatPersonService.getScreenRepeatPersonPage(pageReqVO).getList();

        for (ScreenRepeatPersonDO obj : list) {
            String strMoreType = screenPersonService.resolveMoreTypeToString(obj.getMoreType());
            obj.setMoreTypeStr(strMoreType);
            screenPersonService.resolveDistrict2(obj);
        }

        // 导出 Excel
        ExcelUtils.write2(response, "重复筛查人员管理.xls", "数据", ScreenRepeatPersonRespVO.class,
                        BeanUtils.toBean(list, ScreenRepeatPersonRespVO.class));
    }


    @GetMapping("/get-remain-repeat-person")
    @Operation(summary = "查询是否有重复人员名单未处理")
    public CommonResult<Boolean> getIsRemainRepeatPerson() {
        return success(screenRepeatPersonService.getIsRemainRepeatPerson());
    }


    @GetMapping("/get-is-exist-person")
    @Operation(summary = "重复人员恢复至摸底库，查询摸底库中是否存在 与恢复人员 的身份证号、工作年度、筛查类型一样的记录")
    public CommonResult<Boolean> isExist(@RequestParam("id") Long id) {
        return success(screenRepeatPersonService.isExist(id));
    }

    @GetMapping("/recover-data")
    @Operation(summary = "重复人员恢复至摸底库")
    public CommonResult<Boolean> recoverData(@RequestParam("id") Long id) {
        return success(screenRepeatPersonService.recoverData(id));
    }

}
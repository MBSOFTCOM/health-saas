package cn.iocoder.yudao.module.childhealth.controller.admin.vaccine;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.vaccine.VaccineRecordDO;
import cn.iocoder.yudao.module.childhealth.service.vaccine.VaccineRecordService;
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
 * 疫苗接种记录 Controller
 */
@Tag(name = "管理后台 - 疫苗接种记录")
@RestController
@RequestMapping("/childhealth/vaccineRecord")
@Validated
public class VaccineRecordController {

    @Resource
    private VaccineRecordService vaccineRecordService;

    @PostMapping("/create")
    @Operation(summary = "新增疫苗接种记录")
    @PreAuthorize("@ss.hasPermission('childhealth:vaccineRecord:create')")
    public CommonResult<Long> createVaccineRecord(@Valid @RequestBody VaccineRecordDO record) {
        return success(vaccineRecordService.createVaccineRecord(record));
    }

    @PutMapping("/update")
    @Operation(summary = "修改疫苗接种记录")
    @PreAuthorize("@ss.hasPermission('childhealth:vaccineRecord:update')")
    public CommonResult<Boolean> updateVaccineRecord(@Valid @RequestBody VaccineRecordDO record) {
        vaccineRecordService.updateVaccineRecord(record);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除疫苗接种记录")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:vaccineRecord:delete')")
    public CommonResult<Boolean> deleteVaccineRecord(@RequestParam("id") Long id) {
        vaccineRecordService.deleteVaccineRecord(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "查询疫苗接种记录详情")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:vaccineRecord:query')")
    public CommonResult<VaccineRecordDO> getVaccineRecord(@RequestParam("id") Long id) {
        return success(vaccineRecordService.getVaccineRecord(id));
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询疫苗接种记录")
    @PreAuthorize("@ss.hasPermission('childhealth:vaccineRecord:query')")
    public CommonResult<PageResult<VaccineRecordDO>> getVaccineRecordPage(@Valid PageParam pageParam,
                                                                          @RequestParam(value = "childId", required = false) Long childId,
                                                                          @RequestParam(value = "vaccineName", required = false) String vaccineName,
                                                                          @RequestParam(value = "status", required = false) String status) {
        return success(vaccineRecordService.getVaccineRecordPage(pageParam, childId, vaccineName, status));
    }

    @PostMapping("/confirm")
    @Operation(summary = "接种执行确认（关联计划，同步更新计划状态为已接种）")
    @PreAuthorize("@ss.hasPermission('childhealth:vaccineRecord:create')")
    public CommonResult<Long> confirmInoculation(@Valid @RequestBody VaccineRecordDO record) {
        return success(vaccineRecordService.confirmInoculation(record));
    }
}

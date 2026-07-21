package cn.iocoder.yudao.module.childhealth.controller.admin.vaccine;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.vaccine.VaccinePlanDO;
import cn.iocoder.yudao.module.childhealth.service.vaccine.VaccinePlanService;
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

/**
 * 疫苗接种计划 Controller
 */
@Tag(name = "管理后台 - 疫苗接种计划")
@RestController
@RequestMapping("/childhealth/vaccinePlan")
@Validated
public class VaccinePlanController {

    @Resource
    private VaccinePlanService vaccinePlanService;

    @PostMapping("/create")
    @Operation(summary = "新增疫苗接种计划")
    @PreAuthorize("@ss.hasPermission('childhealth:vaccinePlan:create')")
    public CommonResult<Long> createVaccinePlan(@Valid @RequestBody VaccinePlanDO plan) {
        return success(vaccinePlanService.createVaccinePlan(plan));
    }

    @PutMapping("/update")
    @Operation(summary = "修改疫苗接种计划")
    @PreAuthorize("@ss.hasPermission('childhealth:vaccinePlan:update')")
    public CommonResult<Boolean> updateVaccinePlan(@Valid @RequestBody VaccinePlanDO plan) {
        vaccinePlanService.updateVaccinePlan(plan);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除疫苗接种计划")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:vaccinePlan:delete')")
    public CommonResult<Boolean> deleteVaccinePlan(@RequestParam("id") Long id) {
        vaccinePlanService.deleteVaccinePlan(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "查询疫苗接种计划详情")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:vaccinePlan:query')")
    public CommonResult<VaccinePlanDO> getVaccinePlan(@RequestParam("id") Long id) {
        return success(vaccinePlanService.getVaccinePlan(id));
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询疫苗接种计划")
    @PreAuthorize("@ss.hasPermission('childhealth:vaccinePlan:query')")
    public CommonResult<PageResult<VaccinePlanDO>> getVaccinePlanPage(@Valid PageParam pageParam,
                                                                       @RequestParam(value = "childId", required = false) Long childId,
                                                                       @RequestParam(value = "vaccineName", required = false) String vaccineName,
                                                                       @RequestParam(value = "status", required = false) String status) {
        return success(vaccinePlanService.getVaccinePlanPage(pageParam, childId, vaccineName, status));
    }

    @PostMapping("/generate")
    @Operation(summary = "根据儿童ID自动生成国家免疫规划疫苗接种计划")
    @Parameter(name = "childId", description = "儿童ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:vaccinePlan:create')")
    public CommonResult<Integer> generatePlanByChild(@RequestParam("childId") Long childId) {
        return success(vaccinePlanService.generatePlanByChild(childId));
    }

    @GetMapping("/upcoming")
    @Operation(summary = "查询未来指定天数内到期的待接种计划")
    @Parameter(name = "days", description = "天数（默认7天）", example = "7")
    @PreAuthorize("@ss.hasPermission('childhealth:vaccinePlan:query')")
    public CommonResult<List<VaccinePlanDO>> getUpcomingPlans(@RequestParam(value = "days", defaultValue = "7") int days) {
        return success(vaccinePlanService.getUpcomingPlans(days));
    }

    @PostMapping("/send-reminder")
    @Operation(summary = "发送接种提醒")
    @Parameter(name = "id", description = "计划ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:vaccinePlan:update')")
    public CommonResult<Boolean> sendReminder(@RequestParam("id") Long id) {
        return success(vaccinePlanService.sendReminder(id));
    }
}

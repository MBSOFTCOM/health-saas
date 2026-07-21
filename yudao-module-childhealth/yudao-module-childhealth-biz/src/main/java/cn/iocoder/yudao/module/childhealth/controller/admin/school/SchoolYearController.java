package cn.iocoder.yudao.module.childhealth.controller.admin.school;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.childhealth.service.school.SchoolYearService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@RestController
@RequestMapping("/childhealth/schoolYear")
public class SchoolYearController {

    @Resource
    private SchoolYearService schoolYearService;

    @GetMapping("/bindings")
    @PreAuthorize("@ss.hasPermission('childhealth:school-year:query')")
    public CommonResult<Map<String, Object>> bindings(@RequestParam Long id) {
        return success(schoolYearService.bindings(id));
    }

    @PostMapping("/archive")
    @PreAuthorize("@ss.hasPermission('childhealth:school-year:update')")
    public CommonResult<Boolean> archive(@RequestParam Long id) {
        schoolYearService.archive(id);
        return success(true);
    }
}

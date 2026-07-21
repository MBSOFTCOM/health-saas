package cn.iocoder.yudao.module.childhealth.controller.admin.school;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.childhealth.service.school.SchoolInfoService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@RestController
@RequestMapping("/childhealth/schoolInfo")
public class SchoolInfoController {

    @Resource
    private SchoolInfoService schoolInfoService;

    @GetMapping("/hierarchy")
    @PreAuthorize("@ss.hasPermission('childhealth:school-info:query')")
    public CommonResult<Map<String, Object>> hierarchy(@RequestParam Long id) {
        return success(schoolInfoService.hierarchy(id));
    }

    @PostMapping("/import-hierarchy")
    @PreAuthorize("@ss.hasPermission('childhealth:school-info:create')")
    public CommonResult<String> importHierarchy(@RequestParam Long schoolId, @RequestParam("file") MultipartFile file) throws IOException {
        schoolInfoService.importHierarchy(schoolId, file);
        return success("导入成功");
    }

    @GetMapping("/hierarchy-template")
    @PreAuthorize("@ss.hasPermission('childhealth:school-info:query')")
    public void hierarchyTemplate(HttpServletResponse response) throws IOException {
        schoolInfoService.hierarchyTemplate(response);
    }
}

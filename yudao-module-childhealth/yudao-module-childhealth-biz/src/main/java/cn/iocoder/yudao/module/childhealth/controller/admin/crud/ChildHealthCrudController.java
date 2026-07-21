package cn.iocoder.yudao.module.childhealth.controller.admin.crud;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.security.core.service.SecurityFrameworkService;
import cn.iocoder.yudao.module.childhealth.service.crud.ChildHealthCrudService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@RestController
@RequestMapping("/childhealth")
public class ChildHealthCrudController {

    @Resource
    private ChildHealthCrudService service;

    @Resource
    private SecurityFrameworkService securityFrameworkService;

    private static final Map<String, String> PERMISSION_PREFIX = Map.of(
            "schoolInfo", "childhealth:school-info",
            "schoolYear", "childhealth:school-year",
            "gradeInfo", "childhealth:grade-info",
            "classInfo", "childhealth:class-info",
            "studentInfo", "childhealth:student-info"
    );

    private void checkPermission(String resource, String action, jakarta.servlet.http.HttpServletRequest request) {
        String actualResource = resource(resource, request);
        String prefix = PERMISSION_PREFIX.get(actualResource);
        // 通用资源使用各自菜单权限；未映射资源保持旧逻辑使用 childhealth:crud 权限
        String permission = prefix != null ? prefix + ":" + action : "childhealth:crud:" + action;
        if (!securityFrameworkService.hasPermission(permission)) {
            throw new ServiceException(GlobalErrorCodeConstants.FORBIDDEN);
        }
    }

    @GetMapping({"/{resource}/page", "/screening/batch/page", "/case/high-risk/page"})
    public CommonResult<PageResult<Map<String, Object>>> page(
            @PathVariable(required = false) String resource, @RequestParam Map<String, String> params,
            jakarta.servlet.http.HttpServletRequest request) {
        checkPermission(resource, "query", request);
        return success(service.page(resource(resource, request), params));
    }

    @GetMapping({"/{resource}/get", "/screening/batch/get", "/case/high-risk/get"})
    public CommonResult<Map<String, Object>> get(
            @PathVariable(required = false) String resource, @RequestParam Long id,
            jakarta.servlet.http.HttpServletRequest request) {
        checkPermission(resource, "query", request);
        return success(service.get(resource(resource, request), id));
    }

    @PostMapping({"/{resource}/create", "/screening/batch/create", "/case/high-risk/create"})
    public CommonResult<Long> create(
            @PathVariable(required = false) String resource, @RequestBody Map<String, Object> body,
            jakarta.servlet.http.HttpServletRequest request) {
        checkPermission(resource, "create", request);
        return success(service.create(resource(resource, request), body));
    }

    @PutMapping({"/{resource}/update", "/screening/batch/update", "/case/high-risk/update"})
    public CommonResult<Boolean> update(
            @PathVariable(required = false) String resource, @RequestBody Map<String, Object> body,
            jakarta.servlet.http.HttpServletRequest request) {
        checkPermission(resource, "update", request);
        service.update(resource(resource, request), body);
        return success(true);
    }

    @DeleteMapping({"/{resource}/delete", "/screening/batch/delete", "/case/high-risk/delete"})
    public CommonResult<Boolean> delete(
            @PathVariable(required = false) String resource, @RequestParam Long id,
            jakarta.servlet.http.HttpServletRequest request) {
        checkPermission(resource, "delete", request);
        service.delete(resource(resource, request), id);
        return success(true);
    }

    @GetMapping("/{resource}/export")
    public void export(
            @PathVariable String resource, @RequestParam Map<String, String> params,
            HttpServletResponse response,
            jakarta.servlet.http.HttpServletRequest request) throws IOException {
        checkPermission(resource, "export", request);
        service.export(resource(resource, request), params, response);
    }

    @PostMapping("/{resource}/import")
    public CommonResult<String> importExcel(
            @PathVariable String resource, @RequestParam("file") MultipartFile file,
            jakarta.servlet.http.HttpServletRequest request) throws IOException {
        checkPermission(resource, "create", request);
        service.importExcel(resource(resource, request), file);
        return success("导入成功");
    }

    @GetMapping("/{resource}/template")
    public void template(
            @PathVariable String resource,
            HttpServletResponse response,
            jakarta.servlet.http.HttpServletRequest request) throws IOException {
        checkPermission(resource, "query", request);
        service.template(resource(resource, request), response);
    }

    private String resource(String resource, jakarta.servlet.http.HttpServletRequest request) {
        if (resource != null) return resource;
        String uri = request.getRequestURI();
        if (uri.contains("/screening/batch/")) return "screening/batch";
        if (uri.contains("/case/high-risk/")) return "case/high-risk";
        throw new IllegalArgumentException("无法识别儿童健康资源");
    }
}

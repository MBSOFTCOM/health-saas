package cn.iocoder.yudao.module.childhealth.service.school;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface SchoolInfoService {

    Map<String, Object> hierarchy(Long schoolId);

    void importHierarchy(Long schoolId, MultipartFile file) throws IOException;

    void hierarchyTemplate(HttpServletResponse response) throws IOException;
}

package cn.iocoder.yudao.module.childhealth.service.school;

import java.util.Map;

public interface SchoolYearService {

    Map<String, Object> bindings(Long yearId);

    void archive(Long yearId);
}

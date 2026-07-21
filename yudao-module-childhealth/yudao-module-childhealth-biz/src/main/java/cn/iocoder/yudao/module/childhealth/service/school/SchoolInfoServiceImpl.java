package cn.iocoder.yudao.module.childhealth.service.school;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.crud.vo.GradeClassImportVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class SchoolInfoServiceImpl implements SchoolInfoService {

    @Resource
    private JdbcTemplate jdbc;

    @Override
    public Map<String, Object> hierarchy(Long schoolId) {
        Map<String, Object> result = new LinkedHashMap<>();
        List<Map<String, Object>> years = jdbc.queryForList(
                "SELECT DISTINCT y.* FROM `school_year` y " +
                        "WHERE y.`id` IN (SELECT `year_id` FROM `grade_info` WHERE `school_id` = ?) " +
                        "OR y.`id` IN (SELECT `year_id` FROM `screening_batch` WHERE `school_id` = ?) ORDER BY y.`start_date` DESC",
                schoolId, schoolId);
        List<Map<String, Object>> grades = jdbc.queryForList(
                "SELECT g.*, y.`year_name` FROM `grade_info` g " +
                        "LEFT JOIN `school_year` y ON y.`id` = g.`year_id` " +
                        "WHERE g.`school_id` = ? ORDER BY g.`year_id` DESC, g.`grade_level`",
                schoolId);
        List<Map<String, Object>> classes = jdbc.queryForList(
                "SELECT c.*, g.`grade_name`, y.`year_name` FROM `class_info` c " +
                        "LEFT JOIN `grade_info` g ON g.`id` = c.`grade_id` " +
                        "LEFT JOIN `school_year` y ON y.`id` = g.`year_id` " +
                        "WHERE g.`school_id` = ? ORDER BY y.`start_date` DESC, g.`grade_level`, c.`class_name`",
                schoolId);
        result.put("years", years);
        result.put("grades", grades);
        result.put("classes", classes);
        return result;
    }

    @Override
    @Transactional
    public void importHierarchy(Long schoolId, MultipartFile file) throws IOException {
        List<GradeClassImportVO> list = ExcelUtils.read(file, GradeClassImportVO.class);
        if (list.isEmpty()) throw new ServiceException(1_010_008_001, "导入数据不能为空");
        for (GradeClassImportVO vo : list) {
            Long yearId = findOrCreateYear(vo.getYearName());
            Long gradeId = findOrCreateGrade(schoolId, yearId, vo.getGradeName());
            findOrCreateClass(gradeId, vo.getClassName(), vo.getHeadTeacher());
        }
    }

    @Override
    public void hierarchyTemplate(HttpServletResponse response) throws IOException {
        ExcelUtils.write(response, "年级班级导入模板.xlsx", "年级班级", GradeClassImportVO.class, Collections.emptyList());
    }

    private Long findOrCreateYear(String yearName) {
        if (yearName == null || yearName.isBlank()) throw new ServiceException(1_010_008_002, "学年名称不能为空");
        List<Map<String, Object>> list = jdbc.queryForList("SELECT `id` FROM `school_year` WHERE `year_name` = ?", yearName);
        if (!list.isEmpty()) return ((Number) list.get(0).get("id")).longValue();
        jdbc.update("INSERT INTO `school_year` (`year_name`, `start_date`, `end_date`, `is_current`) VALUES (?, ?, ?, ?)",
                yearName, null, null, 0);
        return jdbc.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
    }

    private Long findOrCreateGrade(Long schoolId, Long yearId, String gradeName) {
        if (gradeName == null || gradeName.isBlank()) throw new ServiceException(1_010_008_003, "年级名称不能为空");
        List<Map<String, Object>> list = jdbc.queryForList(
                "SELECT `id` FROM `grade_info` WHERE `school_id` = ? AND `year_id` = ? AND `grade_name` = ?",
                schoolId, yearId, gradeName);
        if (!list.isEmpty()) return ((Number) list.get(0).get("id")).longValue();
        jdbc.update("INSERT INTO `grade_info` (`school_id`, `year_id`, `grade_name`, `grade_level`) VALUES (?, ?, ?, ?)",
                schoolId, yearId, gradeName, 0);
        return jdbc.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
    }

    private void findOrCreateClass(Long gradeId, String className, String headTeacher) {
        if (className == null || className.isBlank()) return;
        List<Map<String, Object>> list = jdbc.queryForList(
                "SELECT `id` FROM `class_info` WHERE `grade_id` = ? AND `class_name` = ?",
                gradeId, className);
        if (!list.isEmpty()) {
            jdbc.update("UPDATE `class_info` SET `head_teacher` = ? WHERE `id` = ?", headTeacher, list.get(0).get("id"));
            return;
        }
        jdbc.update("INSERT INTO `class_info` (`grade_id`, `class_name`, `head_teacher`, `student_count`) VALUES (?, ?, ?, ?)",
                gradeId, className, headTeacher, 0);
    }
}

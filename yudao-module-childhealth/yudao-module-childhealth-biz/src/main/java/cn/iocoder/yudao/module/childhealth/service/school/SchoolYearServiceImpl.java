package cn.iocoder.yudao.module.childhealth.service.school;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.util.date.DateUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class SchoolYearServiceImpl implements SchoolYearService {

    @Resource
    private JdbcTemplate jdbc;

    @Override
    public Map<String, Object> bindings(Long yearId) {
        Map<String, Object> result = new LinkedHashMap<>();
        // 学校：通过年级或体检批次关联
        List<Map<String, Object>> schools = jdbc.queryForList(
                "SELECT DISTINCT s.* FROM `school_info` s WHERE s.`id` IN " +
                        "(SELECT `school_id` FROM `grade_info` WHERE `year_id` = ?) " +
                        "OR s.`id` IN (SELECT `school_id` FROM `screening_batch` WHERE `year_id` = ?)",
                yearId, yearId);
        // 年级
        List<Map<String, Object>> grades = jdbc.queryForList(
                "SELECT g.*, s.`school_name` FROM `grade_info` g LEFT JOIN `school_info` s ON s.`id` = g.`school_id` WHERE g.`year_id` = ?",
                yearId);
        // 班级
        List<Map<String, Object>> classes = jdbc.queryForList(
                "SELECT c.*, g.`grade_name`, s.`school_name` FROM `class_info` c " +
                        "LEFT JOIN `grade_info` g ON g.`id` = c.`grade_id` " +
                        "LEFT JOIN `school_info` s ON s.`id` = g.`school_id` " +
                        "WHERE g.`year_id` = ?",
                yearId);
        // 体检批次
        List<Map<String, Object>> batches = jdbc.queryForList(
                "SELECT b.*, s.`school_name` FROM `screening_batch` b LEFT JOIN `school_info` s ON s.`id` = b.`school_id` WHERE b.`year_id` = ?",
                yearId);
        result.put("schools", schools);
        result.put("grades", grades);
        result.put("classes", classes);
        result.put("batches", batches);
        return result;
    }

    @Override
    @Transactional
    public void archive(Long yearId) {
        Map<String, Object> year = jdbc.queryForList("SELECT * FROM `school_year` WHERE `id` = ?", yearId).stream().findFirst().orElse(null);
        if (year == null) throw new ServiceException(1_010_007_001, "学年不存在");
        // 统计该学年下的筛查记录
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM `screening_record` r " +
                        "JOIN `screening_batch` b ON b.`id` = r.`batch_id` " +
                        "WHERE b.`year_id` = ?", Integer.class, yearId);
        // 将筛查记录标记为已归档
        jdbc.update("UPDATE `screening_record` r " +
                        "JOIN `screening_batch` b ON b.`id` = r.`batch_id` " +
                        "SET r.`archive_year_id` = ?, r.`archive_time` = ? " +
                        "WHERE b.`year_id` = ?",
                yearId, DateUtils.of(LocalDateTime.now()), yearId);
        // 写入归档日志
        jdbc.update("INSERT INTO `school_year_archive_log` (`year_id`, `year_name`, `record_count`, `archive_time`) VALUES (?, ?, ?, ?)",
                yearId, year.get("year_name"), count == null ? 0 : count, DateUtils.of(LocalDateTime.now()));
    }
}

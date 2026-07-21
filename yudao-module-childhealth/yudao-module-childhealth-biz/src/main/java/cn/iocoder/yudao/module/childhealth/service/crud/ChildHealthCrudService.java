package cn.iocoder.yudao.module.childhealth.service.crud;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.crud.vo.StudentInfoExcelVO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChildHealthCrudService {

    private static final Set<String> TABLES = Set.of(
            "child_base_info", "guardian_info", "school_info", "school_year", "grade_info", "class_info",
            "student_info", "department_info", "staff_info", "sys_dict", "exam_appointment", "exam_record",
            "physical_exam_record", "eye_exam_record", "hearing_exam_record", "oral_exam_record", "lab_report",
            "medical_record_template", "medical_record", "growth_curve_data", "kindergarten_exam", "external_report",
            "abnormal_rule", "abnormal_detect_log", "device_integration_log", "case_type_config", "case_registration",
            "case_card", "high_risk_newborn", "follow_plan", "follow_record", "case_recovery_assessment",
            "case_alert_log", "screening_batch", "screening_plan", "screening_item_config", "positive_rule",
            "disease_knowledge", "screening_record", "screening_result_detail", "screening_positive", "recheck_record",
            "qrcode_management", "referral_record", "screening_statistics", "scale_config", "scale_question",
            "scale_assessment_record", "assessment_report", "development_milestone", "child_development_assessment",
            "message_push_config", "message_push_log", "reminder_rule_config", "exam_reminder_log",
            "questionnaire_config", "questionnaire_answer", "health_article", "article_read_log", "transfer_archive",
            "follow_task", "sys_user", "sys_role", "sys_user_role", "sys_menu", "sys_role_menu",
            "sys_operation_log", "sys_data_change_log", "sys_api_log", "sys_config", "sys_backup_log", "sys_notice",
            "sys_integration_config");

    private final JdbcTemplate jdbc;
    private final PasswordEncoder passwordEncoder;
    private final Map<String, Set<String>> columnCache = new ConcurrentHashMap<>();

    public ChildHealthCrudService(JdbcTemplate jdbc, PasswordEncoder passwordEncoder) {
        this.jdbc = jdbc;
        this.passwordEncoder = passwordEncoder;
    }

    public PageResult<Map<String, Object>> page(String resource, Map<String, String> params) {
        String table = table(resource);
        Set<String> columns = columns(table);
        int pageNo = positiveInt(params.get("pageNo"), 1);
        int pageSize = Math.min(positiveInt(params.get("pageSize"), 10), 100);
        List<Object> values = new ArrayList<>();
        String where = where(resource, columns, params, values);
        if ("child".equals(resource)) where += where.isEmpty() ? " WHERE `is_deleted` = 0" : " AND `is_deleted` = 0";
        Long total = jdbc.queryForObject("SELECT COUNT(*) FROM `" + table + "`" + where, Long.class, values.toArray());
        values.add((pageNo - 1) * pageSize);
        values.add(pageSize);
        List<Map<String, Object>> rows = jdbc.queryForList("SELECT * FROM `" + table + "`" + where
                + " ORDER BY `id` DESC LIMIT ?, ?", values.toArray());
        return new PageResult<>(rows.stream().map(row -> frontendRow(resource, row)).toList(), total == null ? 0L : total);
    }

    public Map<String, Object> get(String resource, Long id) {
        String active = "child".equals(resource) ? " AND `is_deleted` = 0" : "";
        List<Map<String, Object>> rows = jdbc.queryForList("SELECT * FROM `" + table(resource) + "` WHERE `id` = ?" + active, id);
        if (rows.isEmpty()) throw error("记录不存在");
        return frontendRow(resource, rows.get(0));
    }

    @Transactional
    public Long create(String resource, Map<String, Object> body) {
        String table = table(resource);
        Map<String, Object> data = databaseData(resource, table, body, false);
        applyCreateDefaults(resource, data);
        if (data.isEmpty()) throw error("新增数据不能为空");
        List<String> names = new ArrayList<>(data.keySet());
        String sql = "INSERT INTO `" + table + "` (`" + String.join("`,`", names) + "`) VALUES ("
                + String.join(",", Collections.nCopies(names.size(), "?")) + ")";
        KeyHolder keys = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < names.size(); i++) statement.setObject(i + 1, value(data.get(names.get(i))));
            return statement;
        }, keys);
        Number key = keys.getKey();
        if (key == null) throw error("新增记录未返回主键");
        afterSave(resource, key.longValue(), body);
        return key.longValue();
    }

    @Transactional
    public void update(String resource, Map<String, Object> body) {
        Long id = longValue(body.get("id"));
        if (id == null) throw error("修改记录必须传id");
        String table = table(resource);
        if (jdbc.queryForObject("SELECT COUNT(*) FROM `" + table + "` WHERE `id` = ?", Long.class, id) == 0) {
            throw error("记录不存在");
        }
        Map<String, Object> data = databaseData(resource, table, body, true);
        data.remove("id");
        if (!data.isEmpty()) {
            List<String> names = new ArrayList<>(data.keySet());
            List<Object> values = names.stream().map(name -> value(data.get(name))).collect(java.util.stream.Collectors.toList());
            values.add(id);
            jdbc.update("UPDATE `" + table + "` SET `" + String.join("` = ?, `", names) + "` = ? WHERE `id` = ?",
                    values.toArray());
        }
        afterSave(resource, id, body);
    }

    @Transactional
    public void delete(String resource, Long id) {
        if ("child".equals(resource)) {
            if (jdbc.update("UPDATE `child_base_info` SET `is_deleted` = 1 WHERE `id` = ? AND `is_deleted` = 0", id) == 0) throw error("记录不存在");
            return;
        }
        if (jdbc.update("DELETE FROM `" + table(resource) + "` WHERE `id` = ?", id) == 0) throw error("记录不存在");
    }

    public void export(String resource, Map<String, String> params, HttpServletResponse response) throws IOException {
        if (!"studentInfo".equals(resource)) throw error("当前资源暂不支持导出");
        String table = table(resource);
        Set<String> columns = columns(table);
        List<Object> values = new ArrayList<>();
        String where = where(resource, columns, params, values);
        List<Map<String, Object>> rows = jdbc.queryForList("SELECT * FROM `" + table + "`" + where
                + " ORDER BY `id` DESC", values.toArray());
        List<StudentInfoExcelVO> data = rows.stream().map(row -> toStudentExcel(frontendRow(resource, row))).toList();
        ExcelUtils.write(response, "学生档案.xlsx", "学生档案", StudentInfoExcelVO.class, data);
    }

    @Transactional
    public void importExcel(String resource, MultipartFile file) throws IOException {
        if (!"studentInfo".equals(resource)) throw error("当前资源暂不支持导入");
        List<StudentInfoExcelVO> list = ExcelUtils.read(file, StudentInfoExcelVO.class);
        if (list.isEmpty()) throw error("导入数据不能为空");
        int success = 0;
        for (StudentInfoExcelVO vo : list) {
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("childId", vo.getChildId());
            body.put("studentNo", vo.getStudentNo());
            body.put("classId", vo.getClassId());
            body.put("name", vo.getName());
            body.put("gender", vo.getGender());
            body.put("birthDate", vo.getBirthDate());
            body.put("guardianName", vo.getGuardianName());
            body.put("guardianMobile", vo.getGuardianMobile());
            body.put("status", vo.getStatus());
            create(resource, body);
            success++;
        }
    }

    public void template(String resource, HttpServletResponse response) throws IOException {
        if (!"studentInfo".equals(resource)) throw error("当前资源暂不支持模板下载");
        ExcelUtils.write(response, "学生档案导入模板.xlsx", "学生档案", StudentInfoExcelVO.class, Collections.emptyList());
    }

    private StudentInfoExcelVO toStudentExcel(Map<String, Object> row) {
        StudentInfoExcelVO vo = new StudentInfoExcelVO();
        vo.setChildId(longOrNull(row.get("childId")));
        vo.setStudentNo(strOrNull(row.get("studentNo")));
        vo.setClassId(longOrNull(row.get("classId")));
        vo.setName(strOrNull(row.get("name")));
        vo.setGender(intOrNull(row.get("gender")));
        vo.setBirthDate(strOrNull(row.get("birthDate")));
        vo.setGuardianName(strOrNull(row.get("guardianName")));
        vo.setGuardianMobile(strOrNull(row.get("guardianMobile")));
        vo.setStatus(intOrNull(row.get("status")));
        return vo;
    }

    private Long longOrNull(Object value) {
        if (value == null) return null;
        try { return Long.valueOf(value.toString()); } catch (NumberFormatException e) { return null; }
    }

    private Integer intOrNull(Object value) {
        if (value == null) return null;
        try { return Integer.valueOf(value.toString()); } catch (NumberFormatException e) { return null; }
    }

    private String strOrNull(Object value) {
        return value == null ? null : value.toString();
    }

    private String where(String resource, Set<String> columns, Map<String, String> params, List<Object> values) {
        List<String> conditions = new ArrayList<>();
        params.forEach((key, raw) -> {
            if (Set.of("pageNo", "pageSize").contains(key) || raw == null || raw.isBlank()) return;
            if ("child".equals(resource) && Set.of("parentName", "parentPhone").contains(key)) {
                String guardianColumn = "parentName".equals(key) ? "name" : "mobile";
                conditions.add("EXISTS (SELECT 1 FROM `guardian_info` g WHERE g.`child_id` = `child_base_info`.`id` "
                        + "AND g.`is_primary` = 1 AND g.`" + guardianColumn + "` LIKE ?)");
                values.add("%" + raw + "%");
                return;
            }
            String column = frontendColumn(resource, key);
            if (!columns.contains(column)) return;
            conditions.add("`" + column + "` LIKE ?");
            values.add("%" + raw + "%");
        });
        return conditions.isEmpty() ? "" : " WHERE " + String.join(" AND ", conditions);
    }

    private Map<String, Object> databaseData(String resource, String table, Map<String, Object> body, boolean update) {
        Set<String> columns = columns(table);
        Map<String, Object> data = new LinkedHashMap<>();
        body.forEach((key, raw) -> {
            String column = frontendColumn(resource, key);
            if ("sysUser".equals(resource) && "password".equals(column)
                    && (raw == null || raw.toString().isBlank())) return;
            if (columns.contains(column) && !Set.of("create_time", "update_time").contains(column)
                    && (update || raw != null)) data.put(column, normalized(resource, column, raw));
        });
        if ("screening/batch".equals(resource) && body.get("academicYear") != null) {
            resolveSchoolYear(data, body.get("academicYear"));
        }
        if ("case/high-risk".equals(resource) && body.get("caseType") != null) {
            List<Long> ids = jdbc.query("SELECT `id` FROM `case_type_config` WHERE `type_code` = ? LIMIT 1",
                    (rs, rowNum) -> rs.getLong(1), body.get("caseType"));
            if (ids.isEmpty()) throw error("专案类型不存在");
            data.put("case_type_id", ids.get(0));
        }
        return data;
    }

    private void applyCreateDefaults(String resource, Map<String, Object> data) {
        long suffix = System.currentTimeMillis();
        if ("child".equals(resource)) data.putIfAbsent("child_code", "CHILD-" + suffix);
        if ("checkup".equals(resource) || "examRecord".equals(resource)) {
            data.putIfAbsent("exam_no", "EXAM-" + suffix);
            data.putIfAbsent("check_status", 1);
        }
        if ("case/high-risk".equals(resource) || "caseRegistration".equals(resource)) {
            data.putIfAbsent("case_no", "CASE-" + suffix);
            data.putIfAbsent("registration_date", LocalDate.now());
            data.putIfAbsent("case_status", 1);
        }
        if ("screening/batch".equals(resource) || "screeningBatch".equals(resource)) {
            data.putIfAbsent("batch_no", "SCREEN-" + suffix);
            data.putIfAbsent("batch_status", 1);
        }
    }

    private void resolveSchoolYear(Map<String, Object> data, Object yearName) {
        List<Long> ids = jdbc.query("SELECT `id` FROM `school_year` WHERE `year_name` = ? LIMIT 1",
                (rs, rowNum) -> rs.getLong(1), yearName);
        if (ids.isEmpty()) throw error("学年不存在，请先维护学年资料");
        data.put("year_id", ids.get(0));
    }

    private void afterSave(String resource, Long id, Map<String, Object> body) {
        if ("child".equals(resource)) saveGuardian(id, body);
        if ("checkup".equals(resource)) savePhysical(id, body);
    }

    private void saveGuardian(Long childId, Map<String, Object> body) {
        Object name = body.get("parentName");
        Object mobile = body.get("parentPhone");
        if (name == null || mobile == null) return;
        Long id = jdbc.query("SELECT `id` FROM `guardian_info` WHERE `child_id` = ? AND `is_primary` = 1 LIMIT 1",
                rs -> rs.next() ? rs.getLong(1) : null, childId);
        if (id == null) jdbc.update("INSERT INTO `guardian_info` (`child_id`,`relation`,`name`,`mobile`,`is_primary`) VALUES (?,3,?,?,1)", childId, name, mobile);
        else jdbc.update("UPDATE `guardian_info` SET `name` = ?, `mobile` = ? WHERE `id` = ?", name, mobile, id);
    }

    private void savePhysical(Long examId, Map<String, Object> body) {
        if (!body.containsKey("height") && !body.containsKey("weight") && !body.containsKey("nutritionStatus")) return;
        jdbc.update("INSERT INTO `physical_exam_record` (`exam_id`,`height`,`weight`,`growth_assessment`) VALUES (?,?,?,?) "
                        + "ON DUPLICATE KEY UPDATE `height`=VALUES(`height`),`weight`=VALUES(`weight`),`growth_assessment`=VALUES(`growth_assessment`)",
                examId, body.get("height"), body.get("weight"), body.get("nutritionStatus"));
    }

    private Map<String, Object> frontendRow(String resource, Map<String, Object> row) {
        // ponytail: 分页上限100，复合字段按行查询；数据量成为瓶颈时改成批量联表。
        Map<String, Object> result = new LinkedHashMap<>();
        row.forEach((key, value) -> { if (!("sysUser".equals(resource) && "password".equals(key))) result.put(frontendField(resource, key), value); });
        if ("child".equals(resource)) {
            List<Map<String, Object>> guardian = jdbc.queryForList("SELECT `name`,`mobile` FROM `guardian_info` WHERE `child_id` = ? AND `is_primary` = 1 LIMIT 1", row.get("id"));
            if (!guardian.isEmpty()) { result.put("parentName", guardian.get(0).get("name")); result.put("parentPhone", guardian.get(0).get("mobile")); }
            List<String> schools = jdbc.query("SELECT s.`school_name` FROM `student_info` st JOIN `class_info` c ON c.`id`=st.`class_id` JOIN `grade_info` g ON g.`id`=c.`grade_id` JOIN `school_info` s ON s.`id`=g.`school_id` WHERE st.`child_id`=? LIMIT 1", (rs, rowNum) -> rs.getString(1), row.get("id"));
            if (!schools.isEmpty()) result.put("schoolName", schools.get(0));
        }
        if ("checkup".equals(resource)) {
            List<Map<String, Object>> physical = jdbc.queryForList("SELECT `height`,`weight`,`growth_assessment` FROM `physical_exam_record` WHERE `exam_id` = ? LIMIT 1", row.get("id"));
            if (!physical.isEmpty()) { result.put("height", physical.get(0).get("height")); result.put("weight", physical.get(0).get("weight")); result.put("nutritionStatus", physical.get(0).get("growth_assessment")); }
        }
        if ("screening/batch".equals(resource) && row.get("year_id") != null) {
            List<String> years = jdbc.query("SELECT `year_name` FROM `school_year` WHERE `id` = ?",
                    (rs, rowNum) -> rs.getString(1), row.get("year_id"));
            if (!years.isEmpty()) result.put("academicYear", years.get(0));
        }
        if ("case/high-risk".equals(resource)) {
            List<Map<String, Object>> types = jdbc.queryForList("SELECT `type_code`,`type_name` FROM `case_type_config` WHERE `id` = ?", row.get("case_type_id"));
            if (!types.isEmpty()) { result.put("caseType", types.get(0).get("type_code")); result.put("caseName", types.get(0).get("type_name")); }
            List<String> children = jdbc.query("SELECT `name` FROM `child_base_info` WHERE `id` = ?", (rs, rowNum) -> rs.getString(1), row.get("child_id"));
            if (!children.isEmpty()) result.put("childName", children.get(0));
        }
        if ("caseRegistration".equals(resource)) {
            List<Map<String, Object>> types = jdbc.queryForList("SELECT `type_code`,`type_name` FROM `case_type_config` WHERE `id` = ?", row.get("case_type_id"));
            if (!types.isEmpty()) { result.put("caseType", types.get(0).get("type_code")); result.put("caseName", types.get(0).get("type_name")); }
            List<String> children = jdbc.query("SELECT `name` FROM `child_base_info` WHERE `id` = ?", (rs, rowNum) -> rs.getString(1), row.get("child_id"));
            if (!children.isEmpty()) result.put("childName", children.get(0));
        }
        return result;
    }

    private Set<String> columns(String table) {
        return columnCache.computeIfAbsent(table, name -> jdbc.query("SELECT * FROM `" + name + "` WHERE 1 = 0", rs -> {
            Set<String> result = new HashSet<>();
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) result.add(rs.getMetaData().getColumnName(i));
            return result;
        }));
    }

    static String table(String resource) {
        String table = switch (resource) {
            case "child" -> "child_base_info";
            case "checkup" -> "exam_record";
            case "screening/batch" -> "screening_batch";
            case "case/high-risk" -> "case_registration";
            default -> snake(resource);
        };
        if (!TABLES.contains(table)) throw new ServiceException(1_010_007_001, "不支持的儿童健康资源: " + resource);
        return table;
    }

    private static String frontendColumn(String resource, String field) {
        if ("child".equals(resource)) return switch (field) { case "childName" -> "name"; case "highRiskType" -> "high_risk_tags"; default -> snake(field); };
        if ("checkup".equals(resource)) return switch (field) { case "checkupDate" -> "exam_date"; case "ageMonths" -> "month_age"; case "checkupType" -> "exam_type"; case "isAbnormal" -> "has_abnormal"; case "abnormalItems" -> "abnormal_tags"; case "reviewStatus" -> "check_status"; default -> snake(field); };
        if ("screening/batch".equals(resource)) return switch (field) { case "batchCode" -> "batch_no"; case "academicYear" -> "academic_year"; case "screeningStartDate" -> "start_date"; case "screeningEndDate" -> "end_date"; case "totalStudents" -> "target_count"; case "screenedCount" -> "actual_count"; default -> snake(field); };
        if ("case/high-risk".equals(resource)) return switch (field) { case "createdDate", "diagnosedDate" -> "registration_date"; case "severityLevel" -> "case_level"; default -> snake(field); };
        if ("caseRegistration".equals(resource)) return switch (field) { case "caseType" -> "case_type_id"; case "registrationDate" -> "registration_date"; default -> snake(field); };
        if ("screeningRecord".equals(resource) && "reviewStatus".equals(field)) return "check_status";
        return snake(field);
    }

    private static String frontendField(String resource, String column) {
        if ("child".equals(resource)) return switch (column) { case "name" -> "childName"; case "high_risk_tags" -> "highRiskType"; default -> camel(column); };
        if ("checkup".equals(resource)) return switch (column) { case "exam_date" -> "checkupDate"; case "month_age" -> "ageMonths"; case "exam_type" -> "checkupType"; case "has_abnormal" -> "isAbnormal"; case "abnormal_tags" -> "abnormalItems"; case "check_status" -> "reviewStatus"; default -> camel(column); };
        if ("screening/batch".equals(resource)) return switch (column) { case "batch_no" -> "batchCode"; case "start_date" -> "screeningStartDate"; case "end_date" -> "screeningEndDate"; case "target_count" -> "totalStudents"; case "actual_count" -> "screenedCount"; default -> camel(column); };
        if ("case/high-risk".equals(resource)) return switch (column) { case "registration_date" -> "createdDate"; case "case_level" -> "severityLevel"; default -> camel(column); };
        if ("caseRegistration".equals(resource)) return switch (column) { case "registration_date" -> "registrationDate"; case "case_type_id" -> "caseTypeId"; case "case_level" -> "caseLevel"; case "case_status" -> "caseStatus"; case "case_no" -> "caseNo"; case "child_id" -> "childId"; case "case_source" -> "caseSource"; case "initial_diagnosis" -> "initialDiagnosis"; case "responsible_doctor" -> "responsibleDoctor"; case "discharge_date" -> "dischargeDate"; case "discharge_reason" -> "dischargeReason"; default -> camel(column); };
        return camel(column);
    }

    private Object normalized(String resource, String column, Object raw) {
        if (raw == null) return null;
        String value = raw.toString();
        if ("sysUser".equals(resource) && "password".equals(column)) {
            return value.startsWith("$2") ? value : passwordEncoder.encode(value);
        }
        if ("screening/batch".equals(resource) && "batch_status".equals(column)) {
            return switch (value.toUpperCase(Locale.ROOT)) { case "PLANNING" -> 1; case "SCREENING" -> 2; case "COMPLETED" -> 3; default -> raw; };
        }
        if ("case/high-risk".equals(resource) && "case_status".equals(column)) {
            return switch (value.toUpperCase(Locale.ROOT)) { case "ACTIVE", "OPEN" -> 1; case "CLOSED" -> 2; case "TRANSFERRED" -> 3; default -> raw; };
        }
        if ("case/high-risk".equals(resource) && "case_level".equals(column)) {
            return switch (value.toUpperCase(Locale.ROOT)) { case "LOW", "MILD" -> 1; case "MEDIUM", "MODERATE" -> 2; case "HIGH", "SEVERE" -> 3; default -> raw; };
        }
        if ("caseRegistration".equals(resource) && "case_status".equals(column)) {
            return switch (value.toUpperCase(Locale.ROOT)) { case "ACTIVE", "OPEN" -> 1; case "CLOSED" -> 2; case "REFERRED", "TRANSFERRED" -> 3; default -> raw; };
        }
        if ("caseRegistration".equals(resource) && "case_level".equals(column)) {
            return switch (value.toUpperCase(Locale.ROOT)) { case "LEVEL_1", "LOW", "MILD" -> 1; case "LEVEL_2", "MEDIUM", "MODERATE" -> 2; case "LEVEL_3", "HIGH", "SEVERE" -> 3; default -> raw; };
        }
        if ("caseRegistration".equals(resource) && "case_source".equals(column)) {
            return switch (value.toUpperCase(Locale.ROOT)) { case "EXAM" -> 1; case "PARENT" -> 2; case "REFERRAL" -> 3; case "OTHER" -> 4; default -> raw; };
        }
        return raw;
    }

    static String snake(String value) {
        return value.replace('-', '_').replace('/', '_').replaceAll("([a-z0-9])([A-Z])", "$1_$2").toLowerCase(Locale.ROOT);
    }

    static String camel(String value) {
        StringBuilder result = new StringBuilder(); boolean upper = false;
        for (char c : value.toCharArray()) { if (c == '_') upper = true; else { result.append(upper ? Character.toUpperCase(c) : c); upper = false; } }
        return result.toString();
    }

    private Object value(Object value) {
        return value instanceof Map<?, ?> || value instanceof Collection<?> ? JsonUtils.toJsonString(value) : value;
    }

    private int positiveInt(String value, int fallback) {
        try { int parsed = Integer.parseInt(value); return parsed > 0 ? parsed : fallback; } catch (Exception ignored) { return fallback; }
    }

    private Long longValue(Object value) {
        if (value == null) return null;
        try { return Long.valueOf(value.toString()); } catch (NumberFormatException ex) { throw error("id必须是数字"); }
    }

    private ServiceException error(String message) {
        return new ServiceException(1_010_007_001, message);
    }
}

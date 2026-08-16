package cn.iocoder.yudao.module.childhealth.service.report;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.module.childhealth.api.report.dto.ChildHealthReportDTO.StatisticsRequest;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.ScreeningBatchDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.ScreeningRecordDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.ScreeningResultDetailDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.ScreeningPositiveDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.workflow.HealthCheckupDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.screening.ScreeningBatchMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.screening.ScreeningRecordMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.screening.ScreeningResultDetailMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.screening.ScreeningPositiveMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.screening.RecheckRecordMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.workflow.ReferralRecordMapper;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.exam.PhysicalExamRecordDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.workflow.HealthCheckupMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.exam.PhysicalExamRecordMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.annotation.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.*;

@Service("childHealthReportService")
public class ChildHealthReportServiceImpl implements ChildHealthReportService {

    @Resource
    private ScreeningRecordMapper screeningRecordMapper;
    @Resource
    private ScreeningResultDetailMapper screeningResultDetailMapper;
    @Resource
    private ScreeningPositiveMapper screeningPositiveMapper;
    @Resource
    private RecheckRecordMapper recheckRecordMapper;
    @Resource
    private ReferralRecordMapper referralRecordMapper;
    @Resource
    private ScreeningBatchMapper screeningBatchMapper;
    @Resource
    private HealthCheckupMapper healthCheckupMapper;
    @Resource
    private PhysicalExamRecordMapper physicalExamRecordMapper;
    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public Map<String, Object> exam(Long id) {
        // 体检详情：主记录 + 体格检查明细 + 关联筛查记录 + 阳性项
        HealthCheckupDO exam = healthCheckupMapper.selectById(id);
        if (exam == null) {
            throw error("体检记录不存在");
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("exam", exam);
        // 体格检查明细
        PhysicalExamRecordDO physical = physicalExamRecordMapper.selectOne(
                Wrappers.<PhysicalExamRecordDO>lambdaQuery()
                        .eq(PhysicalExamRecordDO::getExamId, id)
                        .last("LIMIT 1"));
        result.put("physical", physical);
        // 关联筛查记录（按同儿童同日筛查关联）
        List<ScreeningRecordDO> screeningRecords = screeningRecordMapper.selectList(
                Wrappers.<ScreeningRecordDO>lambdaQuery()
                        .eq(ScreeningRecordDO::getStudentId, exam.getChildId())
                        .eq(ScreeningRecordDO::getScreeningDate, exam.getCheckupDate()));
        result.put("screeningRecords", screeningRecords);
        // 阳性项汇总
        if (!screeningRecords.isEmpty()) {
            List<Long> recordIds = new ArrayList<>(screeningRecords.size());
            for (ScreeningRecordDO r : screeningRecords) {
                recordIds.add(r.getId());
            }
            result.put("positives", screeningPositiveMapper.selectListByRecordIds(recordIds));
        } else {
            result.put("positives", Collections.emptyList());
        }
        return result;
    }

    @Override
    public Map<String, Object> personalScreen(Long recordId) {
        ScreeningRecordDO record = screeningRecordMapper.selectById(recordId);
        if (record == null)
            throw error("筛查记录不存在");
        List<ScreeningResultDetailDO> details = screeningResultDetailMapper.selectList(
                Wrappers.<ScreeningResultDetailDO>lambdaQuery()
                        .eq(ScreeningResultDetailDO::getRecordId, recordId)
                        .orderByAsc(ScreeningResultDetailDO::getItemCode));
        List<ScreeningPositiveDO> positives = screeningPositiveMapper.selectList(
                Wrappers.<ScreeningPositiveDO>lambdaQuery()
                        .eq(ScreeningPositiveDO::getRecordId, recordId));
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("record", record);
        result.put("details", details);
        result.put("positives", positives);
        return result;
    }

    @Override
    public Map<String, Object> schoolScreen(Long batchId) {
        ScreeningBatchDO batch = screeningBatchMapper.selectById(batchId);
        if (batch == null)
            throw error("筛查批次不存在");
        long total = screeningRecordMapper.selectCount(Wrappers.<ScreeningRecordDO>lambdaQuery()
                .eq(ScreeningRecordDO::getBatchId, batchId));
        long positive = screeningRecordMapper.selectCount(Wrappers.<ScreeningRecordDO>lambdaQuery()
                .eq(ScreeningRecordDO::getBatchId, batchId).eq(ScreeningRecordDO::getHasPositive, 1));
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("batch", batch);
        result.put("screenedCount", total);
        result.put("positiveCount", positive);
        result.put("positiveRate", total == 0 ? 0D : (double) positive / total);
        return result;
    }

    @Override
    public Map<String, Object> gradeScreen(Long batchId, Long gradeId) {
        if (screeningBatchMapper.selectById(batchId) == null)
            throw error("筛查批次不存在");
        return jdbcTemplate.queryForMap(
                "SELECT COUNT(*) screenedCount, COALESCE(SUM(r.has_positive = 1),0) positiveCount, " +
                        "COALESCE(SUM(r.check_status = 3),0) auditedCount FROM screening_record r " +
                        "JOIN student_info s ON s.id=r.student_id JOIN class_info c ON c.id=s.class_id " +
                        "WHERE r.batch_id=? AND c.grade_id=?",
                batchId, gradeId);
    }

    @Override
    public List<Map<String, Object>> regionScreen(String regionCode) {
        String sql = "SELECT s.id schoolId,s.school_name schoolName,s.region_code regionCode," +
                "COUNT(r.id) screenedCount,COALESCE(SUM(r.has_positive = 1),0) positiveCount," +
                "COALESCE(SUM(r.check_status = 3),0) auditedCount FROM school_info s " +
                "LEFT JOIN screening_batch b ON b.school_id=s.id LEFT JOIN screening_record r ON r.batch_id=b.id";
        if (regionCode != null && !regionCode.isBlank()) {
            return jdbcTemplate.queryForList(
                    sql + " WHERE s.region_code=? GROUP BY s.id,s.school_name,s.region_code ORDER BY s.school_name",
                    regionCode);
        }
        return jdbcTemplate.queryForList(sql + " GROUP BY s.id,s.school_name,s.region_code ORDER BY s.school_name");
    }

    @Override
    public Map<String, Object> workload(StatisticsRequest request) {
        if (request.getStartDate().isAfter(request.getEndDate()))
            throw error("开始日期不能晚于结束日期");
        Date start = Date.valueOf(request.getStartDate());
        Date end = Date.valueOf(request.getEndDate());
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("screening", jdbcTemplate.queryForList(
                "SELECT d.checker_id staffId,COUNT(*) itemCount FROM screening_result_detail d " +
                        "JOIN screening_record r ON r.id=d.record_id WHERE d.checker_id IS NOT NULL AND r.screening_date BETWEEN ? AND ? GROUP BY d.checker_id",
                start, end));
        result.put("audit", jdbcTemplate.queryForList(
                "SELECT audit_doctor staffId,COUNT(*) auditCount FROM screening_record WHERE audit_doctor IS NOT NULL AND DATE(audit_time) BETWEEN ? AND ? GROUP BY audit_doctor",
                start, end));
        result.put("follow", jdbcTemplate.queryForList(
                "SELECT follow_doctor staffId,COUNT(*) followCount FROM follow_record WHERE follow_doctor IS NOT NULL AND follow_date BETWEEN ? AND ? GROUP BY follow_doctor",
                start, end));
        return result;
    }

    @Override
    public Map<String, Object> statistics(StatisticsRequest request) {
        if (request.getStartDate().isAfter(request.getEndDate()))
            throw error("开始日期不能晚于结束日期");
        long exams = healthCheckupMapper.selectCount(Wrappers.<HealthCheckupDO>lambdaQuery()
                .between(HealthCheckupDO::getCheckupDate, request.getStartDate(), request.getEndDate()));
        var query = Wrappers.<ScreeningRecordDO>lambdaQuery()
                .between(ScreeningRecordDO::getScreeningDate, request.getStartDate(), request.getEndDate())
                .eq(request.getBatchId() != null, ScreeningRecordDO::getBatchId, request.getBatchId());
        long screens = screeningRecordMapper.selectCount(query);
        long positives = screeningRecordMapper.selectCount(Wrappers.<ScreeningRecordDO>lambdaQuery()
                .between(ScreeningRecordDO::getScreeningDate, request.getStartDate(), request.getEndDate())
                .eq(request.getBatchId() != null, ScreeningRecordDO::getBatchId, request.getBatchId())
                .eq(ScreeningRecordDO::getHasPositive, 1));
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("startDate", request.getStartDate());
        result.put("endDate", request.getEndDate());
        result.put("examCount", exams);
        result.put("screeningCount", screens);
        result.put("positiveCount", positives);
        result.put("positiveRate", screens == 0 ? 0D : (double) positives / screens);
        return result;
    }

    @Override
    public byte[] export(StatisticsRequest request) {
        return exportWithFormat(request, "csv");
    }

    /**
     * 按指定格式导出统计报表。
     *
     * @param request 统计请求（含起止日期、批次）
     * @param format  导出格式：csv / excel / pdf（pdf 仅返回提示文本，实际 PDF 由积木报表引擎渲染）
     * @return 文件字节内容
     */
    public byte[] exportWithFormat(StatisticsRequest request, String format) {
        Map<String, Object> data = statistics(request);
        if (format == null || format.isBlank() || "csv".equalsIgnoreCase(format)) {
            return writeCsv(data);
        }
        if ("excel".equalsIgnoreCase(format) || "xlsx".equalsIgnoreCase(format)) {
            return writeExcel(data);
        }
        if ("pdf".equalsIgnoreCase(format)) {
            // PDF 由积木报表（Jeecg JimuReport）渲染，此处返回提示文本以便前端跳转预览
            String tip = "PDF 报表请通过积木报表引擎预览/导出，参数：startDate="
                    + data.get("startDate") + ", endDate=" + data.get("endDate")
                    + ", batchId=" + data.get("batchId");
            return tip.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        }
        // 默认走 CSV
        return writeCsv(data);
    }

    private byte[] writeCsv(Map<String, Object> data) {
        StringBuilder csv = new StringBuilder(
                "start_date,end_date,exam_count,screening_count,positive_count,positive_rate\n");
        csv.append(data.get("startDate")).append(",").append(data.get("endDate")).append(",")
                .append(data.get("examCount")).append(",").append(data.get("screeningCount")).append(",")
                .append(data.get("positiveCount")).append(",").append(data.get("positiveRate")).append("\n");
        return csv.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8);
    }

    private byte[] writeExcel(Map<String, Object> data) {
        // 使用 EasyExcel 写入 xlsx：以 List<List<Object>> 表示行列，避免新建 VO
        java.util.List<java.util.List<Object>> rows = new java.util.ArrayList<>();
        // 表头
        rows.add(java.util.Arrays.asList("开始日期", "结束日期", "体检数", "筛查数", "阳性数", "阳性率"));
        // 数据行
        rows.add(java.util.Arrays.asList(
                String.valueOf(data.get("startDate")),
                String.valueOf(data.get("endDate")),
                data.get("examCount"),
                data.get("screeningCount"),
                data.get("positiveCount"),
                data.get("positiveRate")));
        java.io.ByteArrayOutputStream os = new java.io.ByteArrayOutputStream();
        com.alibaba.excel.EasyExcel.write(os)
                .registerWriteHandler(new com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy())
                .sheet("儿童健康统计")
                .doWrite(rows);
        return os.toByteArray();
    }

    @Override
    public List<Map<String, Object>> growthTrend(Long childId) {
        // 生长发育趋势：按 child_id 关联 exam_record + physical_exam_record，按月龄/日期升序输出
        return jdbcTemplate.queryForList(
                "SELECT e.id examId, e.exam_date examDate, e.month_age monthAge, " +
                        "p.height, p.weight, p.head_circumference headCircumference, " +
                        "p.chest_circumference chestCircumference, p.bmi, " +
                        "p.height_sd heightSd, p.weight_sd weightSd, p.growth_assessment growthAssessment " +
                        "FROM exam_record e LEFT JOIN physical_exam_record p ON p.exam_id = e.id " +
                        "WHERE e.child_id = ? ORDER BY e.exam_date ASC",
                childId);
    }

    @Override
    public Map<String, Object> abnormalityDistribution() {
        // 异常分布：按五健分类（itemCode 前缀）分组统计异常明细数
        // MySQL 用 SUBSTRING_INDEX 取下划线前的部分作为 category
        List<Map<String, Object>> byCategory = jdbcTemplate.queryForList(
                "SELECT SUBSTRING_INDEX(UPPER(d.item_code), '_', 1) category, " +
                        "COUNT(*) abnormalCount " +
                        "FROM screening_result_detail d " +
                        "WHERE d.is_abnormal = 1 AND d.item_code IS NOT NULL " +
                        "GROUP BY SUBSTRING_INDEX(UPPER(d.item_code), '_', 1) " +
                        "ORDER BY abnormalCount DESC");
        // 按性别分布
        List<Map<String, Object>> byGender = jdbcTemplate.queryForList(
                "SELECT CASE WHEN c.gender = 1 THEN '男' WHEN c.gender = 2 THEN '女' ELSE '未知' END gender, " +
                        "COUNT(DISTINCT d.record_id) abnormalRecordCount " +
                        "FROM screening_result_detail d " +
                        "JOIN screening_record r ON r.id = d.record_id " +
                        "LEFT JOIN child_base_info c ON c.id = r.student_id " +
                        "WHERE d.is_abnormal = 1 " +
                        "GROUP BY c.gender");
        // 按年龄组分布（按出生日期推算）
        List<Map<String, Object>> byAgeGroup = jdbcTemplate.queryForList(
                "SELECT CASE " +
                        "  WHEN TIMESTAMPDIFF(YEAR, c.birth_date, CURDATE()) <= 6 THEN '0-6岁' " +
                        "  WHEN TIMESTAMPDIFF(YEAR, c.birth_date, CURDATE()) <= 10 THEN '7-10岁' " +
                        "  WHEN TIMESTAMPDIFF(YEAR, c.birth_date, CURDATE()) <= 14 THEN '11-14岁' " +
                        "  ELSE '15-18岁' END ageGroup, " +
                        "  COUNT(DISTINCT d.record_id) abnormalRecordCount " +
                        "FROM screening_result_detail d " +
                        "JOIN screening_record r ON r.id = d.record_id " +
                        "LEFT JOIN child_base_info c ON c.id = r.student_id " +
                        "WHERE d.is_abnormal = 1 AND c.birth_date IS NOT NULL " +
                        "GROUP BY ageGroup ORDER BY ageGroup");
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("byCategory", byCategory);
        result.put("byGender", byGender);
        result.put("byAgeGroup", byAgeGroup);
        return result;
    }

    @Override
    public List<Map<String, Object>> screeningCoverage(Long batchId) {
        // 筛查覆盖率：按学校维度统计 batchId 的目标 vs 实际筛查人数
        // 注：school_info.target_count 为该学年学校总人数（演示用）；actual_count 为本批次实际筛查人数
        return jdbcTemplate.queryForList(
                "SELECT b.school_id schoolId, s.school_name schoolName, " +
                        "b.target_count targetCount, b.actual_count actualCount, " +
                        "CASE WHEN b.target_count > 0 " +
                        "  THEN ROUND(b.actual_count * 100.0 / b.target_count, 2) " +
                        "  ELSE 0 END coverageRate " +
                        "FROM screening_batch b " +
                        "LEFT JOIN school_info s ON s.id = b.school_id " +
                        "WHERE b.id = ?",
                batchId);
    }

    private ServiceException error(String message) {
        return new ServiceException(1_010_006_001, message);
    }
}

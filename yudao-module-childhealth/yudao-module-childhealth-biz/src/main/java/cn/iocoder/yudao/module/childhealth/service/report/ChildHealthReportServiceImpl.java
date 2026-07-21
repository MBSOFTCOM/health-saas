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
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("examId", id);
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
        Map<String, Object> data = statistics(request);
        StringBuilder csv = new StringBuilder(
                "start_date,end_date,exam_count,screening_count,positive_count,positive_rate\n");
        csv.append(data.get("startDate")).append(",").append(data.get("endDate")).append(",")
                .append(data.get("examCount")).append(",").append(data.get("screeningCount")).append(",")
                .append(data.get("positiveCount")).append(",").append(data.get("positiveRate")).append("\n");
        return csv.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8);
    }

    @Override
    public List<Map<String, Object>> growthTrend(Long childId) {
        return List.of();
    }

    @Override
    public Map<String, Object> abnormalityDistribution() {
        return Map.of();
    }

    @Override
    public List<Map<String, Object>> screeningCoverage(Long batchId) {
        return List.of();
    }

    private ServiceException error(String message) {
        return new ServiceException(1_010_006_001, message);
    }
}

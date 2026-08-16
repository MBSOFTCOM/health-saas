package cn.iocoder.yudao.module.childhealth.controller.admin.screening;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.batch.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.ScreeningBatchDO;
import cn.iocoder.yudao.module.childhealth.service.screening.ScreeningBatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 筛查批次")
@RestController
@RequestMapping("/childhealth/screening-batch")
@Validated
public class ScreeningBatchController {

    @Resource
    private ScreeningBatchService screeningBatchService;

    @Resource
    private JdbcTemplate jdbc;

    // ==================== 批次维度扩展接口（对应前端 api/screen/batch.js） ====================

    @GetMapping("/schools")
    @Operation(summary = "查询批次内学校列表", description = "当前批次模型为单学校，返回该批次的学校信息")
    @Parameter(name = "batchId", description = "批次ID", required = true)
    public CommonResult<List<Map<String, Object>>> getBatchSchools(@RequestParam("batchId") Long batchId) {
        ScreeningBatchDO batch = screeningBatchService.getScreeningBatch(batchId);
        if (batch == null || batch.getSchoolId() == null) {
            return success(new ArrayList<>());
        }
        List<Map<String, Object>> schools = jdbc.queryForList(
                "SELECT id, school_name AS schoolName, school_code AS schoolCode FROM school_info WHERE id = ?",
                batch.getSchoolId());
        return success(schools);
    }

    @GetMapping("/classes")
    @Operation(summary = "查询批次内班级列表", description = "从该批次有筛查记录的学生中聚合班级")
    @Parameter(name = "batchId", description = "批次ID", required = true)
    @Parameter(name = "schoolId", description = "学校ID（可选，当前批次已绑定单学校，传入仅作过滤预留）")
    public CommonResult<List<Map<String, Object>>> getBatchClasses(
            @RequestParam("batchId") Long batchId,
            @RequestParam(value = "schoolId", required = false) Long schoolId) {
        // 通过 student_info -> class_info -> grade_info -> school_info 关联，按批次下的筛查记录聚合
        String sql =
                "SELECT DISTINCT c.id AS classId, c.class_name AS className, " +
                        "g.id AS gradeId, g.grade_name AS gradeName " +
                        "FROM screening_record r " +
                        "LEFT JOIN student_info s ON s.id = r.student_id " +
                        "LEFT JOIN class_info c ON c.id = s.class_id " +
                        "LEFT JOIN grade_info g ON g.id = c.grade_id " +
                        "WHERE r.batch_id = ? AND c.id IS NOT NULL " +
                        "ORDER BY g.grade_level, c.class_name";
        List<Map<String, Object>> list = jdbc.queryForList(sql, batchId);
        return success(list);
    }

    @GetMapping("/screen-items")
    @Operation(summary = "查询批次筛查项目", description = "返回五健筛查的5个固定项目（体形/视力/骨骼/口腔/心理）")
    @Parameter(name = "batchId", description = "批次ID", required = true)
    public CommonResult<List<Map<String, Object>>> getBatchScreenItems(@RequestParam("batchId") Long batchId) {
        // 五健筛查固定5项，项目编码与 ScreeningItemConfig 保持一致
        List<Map<String, Object>> items = new ArrayList<>();
        String[][] defaults = {
                {"BODY_SHAPE", "体形筛查", "体重/身高/BMI"},
                {"VISION", "视力筛查", "裸眼视力/矫正视力"},
                {"SPINE", "骨骼筛查", "脊柱侧弯/扁平足"},
                {"ORAL", "口腔筛查", "龋齿/牙龈"},
                {"PSYCHOLOGY", "心理筛查", "心理量表评估"}
        };
        for (String[] d : defaults) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("itemCode", d[0]);
            m.put("itemName", d[1]);
            m.put("itemDesc", d[2]);
            items.add(m);
        }
        return success(items);
    }

    @PostMapping("/create")
    @Operation(summary = "创建筛查批次")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-batch:create')")
    public CommonResult<Long> createScreeningBatch(@Valid @RequestBody ScreeningBatchSaveReqVO createReqVO) {
        return success(screeningBatchService.createScreeningBatch(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新筛查批次")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-batch:update')")
    public CommonResult<Boolean> updateScreeningBatch(@Valid @RequestBody ScreeningBatchSaveReqVO updateReqVO) {
        screeningBatchService.updateScreeningBatch(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除筛查批次")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:screening-batch:delete')")
    public CommonResult<Boolean> deleteScreeningBatch(@RequestParam("id") Long id) {
        screeningBatchService.deleteScreeningBatch(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得筛查批次")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-batch:query')")
    public CommonResult<ScreeningBatchRespVO> getScreeningBatch(@RequestParam("id") Long id) {
        ScreeningBatchDO screeningBatch = screeningBatchService.getScreeningBatch(id);
        return success(BeanUtils.toBean(screeningBatch, ScreeningBatchRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得筛查批次分页")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-batch:query')")
    public CommonResult<PageResult<ScreeningBatchRespVO>> getScreeningBatchPage(@Valid ScreeningBatchPageReqVO pageReqVO) {
        PageResult<ScreeningBatchDO> pageResult = screeningBatchService.getScreeningBatchPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ScreeningBatchRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得筛查批次列表")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-batch:query')")
    public CommonResult<List<ScreeningBatchRespVO>> getScreeningBatchList(@Valid ScreeningBatchListReqVO listReqVO) {
        List<ScreeningBatchDO> list = screeningBatchService.getScreeningBatchList(listReqVO);
        return success(BeanUtils.toBean(list, ScreeningBatchRespVO.class));
    }

    @PutMapping("/update-status")
    @Operation(summary = "更新筛查批次状态")
    @Parameter(name = "id", description = "编号", required = true)
    @Parameter(name = "status", description = "状态", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:screening-batch:update')")
    public CommonResult<Boolean> updateBatchStatus(@RequestParam("id") Long id,
                                                    @RequestParam("status") Integer status) {
        screeningBatchService.updateBatchStatus(id, status);
        return success(true);
    }

    @PutMapping("/batch-update-status")
    @Operation(summary = "批量更新筛查批次状态（统一管理用）")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-batch:update')")
    public CommonResult<Boolean> batchUpdateStatus(@RequestBody Map<String, Object> body) {
        @SuppressWarnings("unchecked")
        List<Long> ids = (List<Long>) body.get("ids");
        Integer status = (Integer) body.get("status");
        screeningBatchService.batchUpdateStatus(ids, status);
        return success(true);
    }

    @GetMapping("/status-statistics")
    @Operation(summary = "按状态统计批次数量（统一管理用）")
    @Parameter(name = "schoolId", description = "学校ID（可选）")
    @Parameter(name = "yearId", description = "学年ID（可选）")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-batch:query')")
    public CommonResult<Map<Integer, Long>> statusStatistics(
            @RequestParam(value = "schoolId", required = false) Long schoolId,
            @RequestParam(value = "yearId", required = false) Long yearId) {
        return success(screeningBatchService.statusStatistics(schoolId, yearId));
    }

}
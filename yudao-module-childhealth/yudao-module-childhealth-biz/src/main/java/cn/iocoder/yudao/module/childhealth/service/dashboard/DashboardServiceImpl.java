package cn.iocoder.yudao.module.childhealth.service.dashboard;

import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.dashboard.vo.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.ops.OpsIndicatorSnapshotDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.RecheckRecordDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.ScreeningPositiveDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.ScreeningRecordDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.ScreeningResultDetailDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.ops.OpsIndicatorSnapshotMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.screening.RecheckRecordMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.screening.ScreeningPositiveMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.screening.ScreeningRecordMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.screening.ScreeningResultDetailMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 数据看板 Service 实现
 *
 * 创建日期: 2026-07-20
 * 模块: 1. 数据看板 + 12. 运营工作台
 */
@Service
public class DashboardServiceImpl implements DashboardService {

    /** 五健专项编码 */
    private static final String CATEGORY_VISION = "VISION";
    private static final String CATEGORY_ORAL   = "ORAL";
    private static final String CATEGORY_BONE  = "BONE";
    private static final String CATEGORY_PSY   = "PSYCHOLOGICAL";
    private static final String CATEGORY_SHAPE  = "SHAPE";

    @Resource
    private OpsIndicatorSnapshotMapper opsIndicatorSnapshotMapper;
    @Resource
    private ScreeningRecordMapper screeningRecordMapper;
    @Resource
    private ScreeningResultDetailMapper screeningResultDetailMapper;
    @Resource
    private ScreeningPositiveMapper screeningPositiveMapper;
    @Resource
    private RecheckRecordMapper recheckRecordMapper;

    @Override
    public DashboardOverviewRespVO getOverview(DashboardQueryReqVO reqVO) {
        // 1. 查最新快照
        OpsIndicatorSnapshotDO snapshot;
        if (reqVO.getSchoolId() != null) {
            snapshot = opsIndicatorSnapshotMapper.selectLatestBySchool(reqVO.getSchoolId(), reqVO.getBatchId());
        } else {
            // 全局总览：按批次查最新日期
            LocalDate today = LocalDate.now();
            snapshot = opsIndicatorSnapshotMapper.selectByDate(today, reqVO.getBatchId(), null, null);
            // 如果当日快照不存在，回退查最新一条（按时间倒序）
            if (snapshot == null) {
                List<OpsIndicatorSnapshotDO> list = opsIndicatorSnapshotMapper.selectListByDateRange(
                        today.minusDays(30), today, reqVO.getBatchId());
                if (!CollectionUtils.isEmpty(list)) {
                    snapshot = list.get(list.size() - 1);
                }
            }
        }

        // 2. 转换并返回
        if (snapshot == null) {
            return new DashboardOverviewRespVO();
        }
        DashboardOverviewRespVO respVO = new DashboardOverviewRespVO();
        respVO.setTargetCount(snapshot.getTargetCount());
        respVO.setActualCount(snapshot.getActualCount());
        respVO.setParticipationRate(snapshot.getParticipationRate());
        respVO.setPositiveCount(snapshot.getPositiveCount());
        respVO.setPositiveRate(snapshot.getPositiveRate());
        respVO.setNeedRecheckCount(snapshot.getPositiveCount());
        respVO.setRecheckCount(snapshot.getRecheckCount());
        respVO.setRecheckRate(snapshot.getRecheckRate());
        respVO.setNeedFollowCount(snapshot.getPositiveCount());
        respVO.setFollowCount(snapshot.getFollowCount());
        respVO.setFollowRate(snapshot.getFollowRate());
        respVO.setPendingAuditCount(snapshot.getPendingAuditCount());
        respVO.setPendingRecheckCount(snapshot.getPendingRecheckCount());
        respVO.setPendingFollowCount(snapshot.getPendingFollowCount());
        return respVO;
    }

    @Override
    public List<DashboardTrendRespVO> getTrend(DashboardQueryReqVO reqVO) {
        LocalDate startDate = reqVO.getStartDate() != null ? reqVO.getStartDate() : LocalDate.now().minusDays(30);
        LocalDate endDate = reqVO.getEndDate() != null ? reqVO.getEndDate() : LocalDate.now();
        List<OpsIndicatorSnapshotDO> list = opsIndicatorSnapshotMapper.selectListByDateRange(startDate, endDate, reqVO.getBatchId());
        return BeanUtils.toBean(list, DashboardTrendRespVO.class);
    }

    @Override
    public List<DashboardCategoryRespVO> getByRegion(DashboardQueryReqVO reqVO) {
        // 按区域查询快照列表
        LocalDate queryDate = reqVO.getEndDate() != null ? reqVO.getEndDate() : LocalDate.now();
        List<OpsIndicatorSnapshotDO> list;
        if (reqVO.getRegionCode() != null) {
            list = opsIndicatorSnapshotMapper.selectListByRegion(reqVO.getRegionCode(), queryDate);
        } else {
            list = opsIndicatorSnapshotMapper.selectListByBatch(reqVO.getBatchId(), queryDate);
        }
        return convertToCategoryList(list, "区域");
    }

    @Override
    public List<DashboardCategoryRespVO> getBySchool(DashboardQueryReqVO reqVO) {
        LocalDate queryDate = reqVO.getEndDate() != null ? reqVO.getEndDate() : LocalDate.now();
        List<OpsIndicatorSnapshotDO> list = opsIndicatorSnapshotMapper.selectListByBatch(reqVO.getBatchId(), queryDate);
        return convertToCategoryList(list, "学校");
    }

    @Override
    public List<DashboardCategoryRespVO> getByCategory(DashboardQueryReqVO reqVO) {
        // 五健专项分类统计：从 screening_result_detail 按项目编码前缀聚合
        // 1. 查询当前批次下所有筛查记录ID
        List<ScreeningRecordDO> records;
        if (reqVO.getBatchId() != null) {
            // 通过 BaseMapperX 内置方法按 batchId 查询
            records = screeningRecordMapper.selectList(ScreeningRecordDO::getBatchId, reqVO.getBatchId());
        } else {
            // 无批次过滤时默认查最近 1000 条（避免全表扫描）
            records = screeningRecordMapper.selectList(new cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX<ScreeningRecordDO>()
                    .orderByDesc(ScreeningRecordDO::getId)
                    .last("LIMIT 1000"));
        }
        if (CollectionUtils.isEmpty(records)) {
            return buildEmptyCategoryList();
        }
        // 2. 提取 recordId 列表
        List<Long> recordIds = new ArrayList<>(records.size());
        for (ScreeningRecordDO r : records) {
            recordIds.add(r.getId());
        }
        // 3. 查询全部明细（分母）和异常明细（分子）
        List<ScreeningResultDetailDO> allDetails = screeningResultDetailMapper.selectListByRecordIds(recordIds);
        List<ScreeningResultDetailDO> abnormalDetails = screeningResultDetailMapper.selectAbnormalListByRecordIds(recordIds);
        // 4. 查询阳性记录（用于复筛统计；阳性记录的 diseaseCode 同样按五健前缀归类）
        List<ScreeningPositiveDO> positiveList = screeningPositiveMapper.selectListByRecordIds(recordIds);
        // 5. 提取 positiveIds 并查询复筛记录
        List<Long> positiveIds = new ArrayList<>(positiveList.size());
        for (ScreeningPositiveDO p : positiveList) {
            positiveIds.add(p.getId());
        }
        List<RecheckRecordDO> recheckList = recheckRecordMapper.selectListByPositiveIds(positiveIds);
        // 6. 按 itemCode/diseaseCode 前缀分组聚合 stats: [筛查数, 阳性数, 复筛数]
        Map<String, int[]> stats = new HashMap<>();
        stats.put(CATEGORY_VISION, new int[]{0, 0, 0});
        stats.put(CATEGORY_ORAL, new int[]{0, 0, 0});
        stats.put(CATEGORY_BONE, new int[]{0, 0, 0});
        stats.put(CATEGORY_PSY, new int[]{0, 0, 0});
        stats.put(CATEGORY_SHAPE, new int[]{0, 0, 0});
        for (ScreeningResultDetailDO d : allDetails) {
            String cat = resolveCategory(d.getItemCode());
            if (cat != null) {
                stats.get(cat)[0] += 1;
            }
        }
        for (ScreeningResultDetailDO d : abnormalDetails) {
            String cat = resolveCategory(d.getItemCode());
            if (cat != null) {
                stats.get(cat)[1] += 1;
            }
        }
        // 7. 以 positiveId -> ScreeningPositiveDO 映射，将复筛记录归属到对应分类
        Map<Long, ScreeningPositiveDO> positiveMap = positiveList.stream()
                .collect(Collectors.toMap(ScreeningPositiveDO::getId, Function.identity()));
        for (RecheckRecordDO r : recheckList) {
            ScreeningPositiveDO p = positiveMap.get(r.getPositiveId());
            if (p == null) {
                continue;
            }
            String cat = resolveCategory(p.getDiseaseCode());
            if (cat != null) {
                stats.get(cat)[2] += 1;
            }
        }
        // 8. 构造 VO 列表（按固定顺序）
        List<DashboardCategoryRespVO> result = new ArrayList<>(5);
        result.add(buildCategoryWithData(CATEGORY_VISION, "视力", stats.get(CATEGORY_VISION)));
        result.add(buildCategoryWithData(CATEGORY_ORAL, "口腔", stats.get(CATEGORY_ORAL)));
        result.add(buildCategoryWithData(CATEGORY_BONE, "骨骼", stats.get(CATEGORY_BONE)));
        result.add(buildCategoryWithData(CATEGORY_PSY, "心理", stats.get(CATEGORY_PSY)));
        result.add(buildCategoryWithData(CATEGORY_SHAPE, "体形", stats.get(CATEGORY_SHAPE)));
        return result;
    }

    /**
     * 将快照列表转为分类统计列表
     */
    private List<DashboardCategoryRespVO> convertToCategoryList(List<OpsIndicatorSnapshotDO> list, String defaultNamePrefix) {
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        List<DashboardCategoryRespVO> result = new ArrayList<>();
        for (OpsIndicatorSnapshotDO snapshot : list) {
            DashboardCategoryRespVO category = new DashboardCategoryRespVO();
            category.setCategoryCode(Objects.toString(snapshot.getSchoolId(), snapshot.getRegionCode()));
            category.setCategoryName(defaultNamePrefix + "-" + snapshot.getSchoolId());
            category.setScreeningCount(snapshot.getActualCount());
            category.setPositiveCount(snapshot.getPositiveCount());
            category.setPositiveRate(snapshot.getPositiveRate());
            category.setRecheckCount(snapshot.getRecheckCount());
            category.setRecheckRate(snapshot.getRecheckRate());
            result.add(category);
        }
        return result;
    }

    /**
     * 按 itemCode 解析五健分类编码
     * 约定: 项目编码以 VISION_/ORAL_/BONE_/PSYCHOLOGICAL_/SHAPE_ 为前缀
     */
    private String resolveCategory(String itemCode) {
        if (itemCode == null) {
            return null;
        }
        String upper = itemCode.toUpperCase();
        if (upper.startsWith(CATEGORY_VISION + "_") || upper.startsWith("V_")) {
            return CATEGORY_VISION;
        }
        if (upper.startsWith(CATEGORY_ORAL + "_") || upper.startsWith("O_")) {
            return CATEGORY_ORAL;
        }
        if (upper.startsWith(CATEGORY_BONE + "_") || upper.startsWith("B_")) {
            return CATEGORY_BONE;
        }
        if (upper.startsWith("PSY_") || upper.startsWith(CATEGORY_PSY + "_")) {
            return CATEGORY_PSY;
        }
        if (upper.startsWith(CATEGORY_SHAPE + "_") || upper.startsWith("S_")) {
            return CATEGORY_SHAPE;
        }
        return null;
    }

    /**
     * 构造五健分类空数据（前端展示骨架）
     */
    private DashboardCategoryRespVO buildCategory(String code, String name) {
        DashboardCategoryRespVO category = new DashboardCategoryRespVO();
        category.setCategoryCode(code);
        category.setCategoryName(name);
        category.setScreeningCount(0);
        category.setPositiveCount(0);
        category.setPositiveRate(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        category.setRecheckCount(0);
        category.setRecheckRate(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        return category;
    }

    /**
     * 构造五健分类空数据列表
     */
    private List<DashboardCategoryRespVO> buildEmptyCategoryList() {
        List<DashboardCategoryRespVO> result = new ArrayList<>(5);
        result.add(buildCategory(CATEGORY_VISION, "视力"));
        result.add(buildCategory(CATEGORY_ORAL, "口腔"));
        result.add(buildCategory(CATEGORY_BONE, "骨骼"));
        result.add(buildCategory(CATEGORY_PSY, "心理"));
        result.add(buildCategory(CATEGORY_SHAPE, "体形"));
        return result;
    }

    /**
     * 根据统计数组构造分类 VO
     * @param stats int[3] = [筛查数, 阳性数, 复筛数]
     */
    private DashboardCategoryRespVO buildCategoryWithData(String code, String name, int[] stats) {
        DashboardCategoryRespVO category = new DashboardCategoryRespVO();
        category.setCategoryCode(code);
        category.setCategoryName(name);
        category.setScreeningCount(stats[0]);
        category.setPositiveCount(stats[1]);
        if (stats[0] > 0) {
            // 阳性率 = 阳性数 / 筛查数 * 100，保留两位小数
            category.setPositiveRate(BigDecimal.valueOf(stats[1])
                    .divide(BigDecimal.valueOf(stats[0]), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP));
        } else {
            category.setPositiveRate(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        }
        // 复筛率 = 复筛数 / 阳性数 * 100，保留两位小数（阳性数为 0 时置零）
        category.setRecheckCount(stats[2]);
        if (stats[1] > 0) {
            category.setRecheckRate(BigDecimal.valueOf(stats[2])
                    .divide(BigDecimal.valueOf(stats[1]), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP));
        } else {
            category.setRecheckRate(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        }
        return category;
    }

}

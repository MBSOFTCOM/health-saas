package cn.iocoder.yudao.module.childhealth.service.spine;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.spine.SpineExamRecordDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.spine.SpineExamRecordMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 脊柱骨骼筛查 Service 实现类
 */
@Service
@Validated
public class SpineExamRecordServiceImpl implements SpineExamRecordService {

    /**
     * 脊柱骨骼筛查记录不存在的错误码
     */
    private static final ErrorCode SPINE_EXAM_RECORD_NOT_EXISTS = new ErrorCode(100117800, "脊柱骨骼筛查记录不存在");

    @Resource
    private SpineExamRecordMapper spineExamRecordMapper;

    @Override
    public PageResult<SpineExamRecordDO> getSpineExamRecordPage(PageParam pageParam, Long examId, Long childId, Integer riskLevel) {
        return spineExamRecordMapper.selectPage(pageParam, examId, childId, riskLevel);
    }

    @Override
    public SpineExamRecordDO getSpineExamRecord(Long id) {
        return spineExamRecordMapper.selectById(id);
    }

    @Override
    public Long createSpineExamRecord(SpineExamRecordDO record) {
        spineExamRecordMapper.insert(record);
        return record.getId();
    }

    @Override
    public void updateSpineExamRecord(SpineExamRecordDO record) {
        validateSpineExamRecordExists(record.getId());
        spineExamRecordMapper.updateById(record);
    }

    @Override
    public void deleteSpineExamRecord(Long id) {
        validateSpineExamRecordExists(id);
        spineExamRecordMapper.deleteById(id);
    }

    @Override
    public SpineExamRecordDO autoAssessSpineRisk(SpineExamRecordDO record) {
        BigDecimal atrAngle = record.getAtrAngle();
        BigDecimal cobbAngle = record.getSpineCurveAngle();
        BigDecimal leftArchIndex = record.getLeftArchIndex();
        BigDecimal rightArchIndex = record.getRightArchIndex();

        // 默认评估结果
        int riskLevel = 1; // 1正常 / 2预警 / 3异常
        String spineAssessment = "NORMAL";
        StringBuilder diagnosis = new StringBuilder();
        StringBuilder suggestion = new StringBuilder();
        StringBuilder correctionPlan = new StringBuilder();
        boolean abnormal = false;

        // ==================== ATR 角度评估 ====================
        if (atrAngle != null) {
            double atr = atrAngle.doubleValue();
            if (atr >= 10) {
                // ATR 角度 ≥ 10° → SEVERE
                riskLevel = 3;
                spineAssessment = "SEVERE";
                abnormal = true;
                diagnosis.append("重度脊柱侧弯风险（ATR≥10°）；");
                suggestion.append("建议立即脊柱专科就诊，行全脊柱X光检查，评估支具或手术治疗；");
                correctionPlan.append("禁止剧烈扭转运动，建议规范佩戴支具并配合施罗斯矫形体操；");
            } else if (atr >= 7) {
                // ATR 角度 ≥ 7° → MODERATE，riskLevel=3 异常
                riskLevel = 3;
                spineAssessment = "MODERATE";
                abnormal = true;
                diagnosis.append("中度脊柱侧弯风险（ATR 7°-10°）；");
                suggestion.append("建议脊柱专科就诊，行全脊柱X光检查评估Cobb角，考虑支具治疗；");
                correctionPlan.append("建议施罗斯矫形体操训练，每天40分钟，配合核心肌群训练；");
            } else if (atr >= 5) {
                // ATR 角度 5-7° → riskLevel=2 预警
                riskLevel = 2;
                spineAssessment = "MILD";
                diagnosis.append("轻度脊柱侧弯风险（ATR 5°-7°）；");
                suggestion.append("建议3-6个月复查，加强姿势管理与背部肌群锻炼；");
                correctionPlan.append("建议姿势矫正训练，加强核心及背部肌群锻炼，注意坐姿站姿；");
            } else {
                // ATR 角度 < 5° → riskLevel=1 正常
                diagnosis.append("ATR角度正常（<5°）；");
                suggestion.append("保持良好姿势，定期复查；");
            }
        }

        // ==================== Cobb 角评估 ====================
        if (cobbAngle != null) {
            double cobb = cobbAngle.doubleValue();
            if (cobb >= 40) {
                // Cobb 角 ≥ 40° → 重度侧弯，建议手术
                riskLevel = 3;
                abnormal = true;
                diagnosis.append("重度脊柱侧弯（Cobb≥40°）；");
                suggestion.append("建议脊柱外科会诊，评估手术指征，考虑矫形手术；");
                correctionPlan.append("术前避免负重，术后按医嘱进行康复训练；");
            } else if (cobb >= 25) {
                // Cobb 角 25-40° → 中度侧弯
                riskLevel = 3;
                abnormal = true;
                diagnosis.append("中度脊柱侧弯（Cobb 25°-40°）；");
                suggestion.append("建议支具治疗，每3-6个月复查全脊柱X光；");
                correctionPlan.append("规范佩戴支具（每天≥18小时），配合施罗斯体操训练；");
            } else if (cobb >= 10) {
                // Cobb 角 10-25° → 轻度侧弯
                if (riskLevel < 2) {
                    riskLevel = 2;
                }
                diagnosis.append("轻度脊柱侧弯（Cobb 10°-25°）；");
                suggestion.append("建议6-12个月复查全脊柱X光，密切观察进展；");
                correctionPlan.append("建议施罗斯体操训练+核心肌群锻炼，注意姿势管理；");
            }
        }

        // ==================== 足弓指数评估 ====================
        // 足弓指数 > 0.52 → 扁平足；< 0.21 → 高弓足
        String leftArchResult = evaluateArchIndex(leftArchIndex, "左");
        String rightArchResult = evaluateArchIndex(rightArchIndex, "右");
        if (!"NORMAL".equals(leftArchResult) || !"NORMAL".equals(rightArchResult)) {
            // 综合足弓评估
            if ("FLAT_FOOT".equals(leftArchResult) || "FLAT_FOOT".equals(rightArchResult)) {
                record.setArchAssessment("FLAT_FOOT");
            } else {
                record.setArchAssessment("HIGH_ARCH");
            }
            if (riskLevel < 2) {
                riskLevel = 2;
            }
            if (!"NORMAL".equals(leftArchResult)) {
                diagnosis.append(leftArchResult.equals("FLAT_FOOT") ? "左侧扁平足；" : "左侧高弓足；");
                suggestion.append(leftArchResult.equals("FLAT_FOOT")
                        ? "左侧建议穿矫形鞋垫，加强足底内在肌训练；"
                        : "左侧建议穿减震鞋垫，避免高冲击运动；");
            }
            if (!"NORMAL".equals(rightArchResult)) {
                diagnosis.append(rightArchResult.equals("FLAT_FOOT") ? "右侧扁平足；" : "右侧高弓足；");
                suggestion.append(rightArchResult.equals("FLAT_FOOT")
                        ? "右侧建议穿矫形鞋垫，加强足底内在肌训练；"
                        : "右侧建议穿减震鞋垫，避免高冲击运动；");
            }
            correctionPlan.append("建议足弓矫正训练，选择合适鞋垫/鞋具；");
        } else {
            record.setArchAssessment("NORMAL");
        }

        // ==================== 设置最终评估结果 ====================
        record.setRiskLevel(riskLevel);
        record.setSpineAssessment(spineAssessment);
        // 是否异常：风险分级为3 或 存在异常项
        record.setIsAbnormal(abnormal || riskLevel == 3);

        String diagStr = diagnosis.toString();
        String suggStr = suggestion.toString();
        String corrStr = correctionPlan.toString();

        record.setDiagnosis(diagStr.isEmpty() ? "脊柱骨骼检查未见明显异常" : diagStr);
        record.setSuggestion(suggStr.isEmpty() ? "保持良好姿势，定期复查" : suggStr);
        record.setPostureCorrectionPlan(corrStr.isEmpty() ? "建议保持正确坐姿站姿，适量运动，加强核心肌群锻炼"
                : corrStr);

        return record;
    }

    /**
     * 评估足弓指数
     * - > 0.52 → FLAT_FOOT（扁平足）
     * - < 0.21 → HIGH_ARCH（高弓足）
     * - 其他 → NORMAL（正常）
     *
     * @param archIndex 足弓指数
     * @param side      侧别（仅用于日志，不影响结果）
     * @return 评估结果
     */
    private String evaluateArchIndex(BigDecimal archIndex, String side) {
        if (archIndex == null) {
            return "NORMAL";
        }
        double value = archIndex.doubleValue();
        if (value > 0.52) {
            return "FLAT_FOOT";
        } else if (value < 0.21) {
            return "HIGH_ARCH";
        }
        return "NORMAL";
    }

    /**
     * 校验脊柱骨骼筛查记录是否存在
     *
     * @param id 主键ID
     */
    private void validateSpineExamRecordExists(Long id) {
        if (id == null || spineExamRecordMapper.selectById(id) == null) {
            throw exception(SPINE_EXAM_RECORD_NOT_EXISTS);
        }
    }
}

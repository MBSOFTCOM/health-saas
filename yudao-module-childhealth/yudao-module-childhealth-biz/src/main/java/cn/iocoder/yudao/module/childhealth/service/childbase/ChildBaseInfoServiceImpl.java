package cn.iocoder.yudao.module.childhealth.service.childbase;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.childbase.vo.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.casemanagement.HighRiskNewbornDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.childbase.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.workflow.ChildInfoDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.workflow.GuardianInfoDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.casemanagement.HighRiskNewbornMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.childbase.*;
import cn.iocoder.yudao.module.childhealth.dal.mysql.workflow.ChildInfoMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.workflow.GuardianInfoMapper;
import cn.iocoder.yudao.module.childhealth.service.exam.ExamService;
import cn.iocoder.yudao.module.childhealth.service.workflow.ChildHealthWorkflowService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.*;

/**
 * 儿童基本信息 Service 实现类
 *
 * 统一建档入口（P0-2）：所有儿童档案创建都通过此 Service 入口，并自动触发高危识别（P1-1）。
 *
 * @author 系统
 */
@Slf4j
@Service
@Validated
public class ChildBaseInfoServiceImpl implements ChildBaseInfoService {

    @Resource private ChildInfoMapper childInfoMapper;
    @Resource private GuardianInfoMapper guardianInfoMapper;
    @Resource private HighRiskNewbornMapper highRiskNewbornMapper;
    @Resource private HighRiskFactorConfigMapper highRiskFactorConfigMapper;
    @Resource private DeliveryInfoMapper deliveryInfoMapper;
    @Resource private FamilyInfoMapper familyInfoMapper;
    @Resource private ImmunizationInfoMapper immunizationInfoMapper;
    @Resource private NeonatalDiagnosisMapper neonatalDiagnosisMapper;
    @Resource private ChildHealthWorkflowService childHealthWorkflowService;
    @Resource private ExamService examService;

    // ==================== 建档管理（统一入口） ====================

    @Override
    @Transactional
    public Long createChildBaseInfo(ChildBaseInfoCreateReqVO createReqVO) {
        // 1. 字段校验
        validateGender(createReqVO.getGender());
        if (createReqVO.getBirthDate() != null && createReqVO.getBirthDate().isAfter(LocalDate.now())) {
            throw exception(CHILD_INFO_NOT_EXISTS); // 出生日期不能晚于当前日期
        }
        if (createReqVO.getHighRiskTags() != null && !createReqVO.getHighRiskTags().isBlank()
                && !isValidJsonArray(createReqVO.getHighRiskTags())) {
            throw exception(CHILD_INFO_NOT_EXISTS); // 高危标签必须是合法JSON数组
        }

        // 2. 生成儿童编码（若未提供）
        String childCode = createReqVO.getChildCode();
        if (childCode == null || childCode.isBlank()) {
            childCode = generateChildCode();
        }

        // 3. 唯一性校验
        if (childInfoMapper.selectCount(Wrappers.<ChildInfoDO>lambdaQuery()
                .eq(ChildInfoDO::getChildCode, childCode)) > 0) {
            throw exception(CHILD_INFO_CODE_DUPLICATE);
        }
        if (createReqVO.getIdCard() != null && !createReqVO.getIdCard().isBlank()
                && childInfoMapper.selectCount(Wrappers.<ChildInfoDO>lambdaQuery()
                        .eq(ChildInfoDO::getIdCard, createReqVO.getIdCard())) > 0) {
            throw exception(CHILD_INFO_ID_CARD_DUPLICATE);
        }

        // 4. 构建并插入儿童档案
        ChildInfoDO child = new ChildInfoDO();
        child.setChildCode(childCode);
        // VO 的 name 字段映射到 DO 的 childName
        child.setChildName(createReqVO.getName());
        child.setGender(createReqVO.getGender());
        child.setBirthDate(createReqVO.getBirthDate());
        child.setIdCard(createReqVO.getIdCard());
        child.setBirthWeight(createReqVO.getBirthWeight());
        child.setBirthHeight(createReqVO.getBirthHeight());
        child.setGestationalAge(createReqVO.getGestationalAge());
        child.setIsPremature(createReqVO.getIsPremature());
        child.setHighRiskTags(createReqVO.getHighRiskTags());
        child.setRegisterSource(1); // 手动建档
        child.setStatus(1); // 正常
        child.setIsHighRisk(createReqVO.getIsHighRisk() == null ? 0 : createReqVO.getIsHighRisk());
        LocalDateTime now = LocalDateTime.now();
        child.setCreateTime(now);
        child.setUpdateTime(now);
        child.setFirstVisitDate(LocalDate.now());
        childInfoMapper.insert(child);

        // 5. 保存监护人信息
        if (createReqVO.getGuardians() != null && !createReqVO.getGuardians().isEmpty()) {
            for (GuardianInfoCreateReqVO g : createReqVO.getGuardians()) {
                GuardianInfoDO guardian = new GuardianInfoDO();
                guardian.setChildId(child.getId());
                guardian.setRelation(g.getRelation());
                guardian.setGuardianName(g.getName());
                guardian.setGuardianPhone(g.getMobile());
                guardian.setIdCard(g.getIdCard());
                guardian.setIsPrimary(g.getIsPrimary() == null ? 0 : g.getIsPrimary());
                guardian.setWechatOpenid(g.getWechatOpenid());
                guardian.setCreateTime(now);
                guardianInfoMapper.insert(guardian);
            }
        }

        // 6. P1-1 建档时自动触发高危识别（需求3）
        List<String> detectedFactors = autoDetectHighRisk(child.getId());

        // 7. 建档时预警提醒（需求5）：自动识别后同步到高危新生儿台账
        // autoDetectHighRisk 内部已调用 syncHighRiskLedger

        // 8. P2-3 需求6：建档后自动生成公卫儿童保健计划（满月/3/6/8/12/18/24/30 月、3-6 岁年度）
        try {
            examService.generateCarePlan(child.getId());
        } catch (Exception e) {
            // 保健计划生成失败不影响建档主流程
            log.warn("[createChildBaseInfo] 自动生成保健计划失败 childId={} err={}",
                    child.getId(), e.getMessage());
        }

        log.info("[createChildBaseInfo] 建档成功 childId={} childCode={} detectedHighRiskFactors={}",
                child.getId(), childCode, detectedFactors);
        return child.getId();
    }

    @Override
    public void updateChildBaseInfo(ChildBaseInfoUpdateReqVO updateReqVO) {
        ChildInfoDO child = requireChild(updateReqVO.getId());
        if (updateReqVO.getGender() != null) {
            validateGender(updateReqVO.getGender());
        }
        if (updateReqVO.getBirthDate() != null && updateReqVO.getBirthDate().isAfter(LocalDate.now())) {
            throw exception(CHILD_INFO_NOT_EXISTS); // 出生日期不能晚于当前日期
        }
        if (updateReqVO.getIdCard() != null && !updateReqVO.getIdCard().isBlank()
                && childInfoMapper.selectCount(Wrappers.<ChildInfoDO>lambdaQuery()
                        .eq(ChildInfoDO::getIdCard, updateReqVO.getIdCard())
                        .ne(ChildInfoDO::getId, updateReqVO.getId())) > 0) {
            throw exception(CHILD_INFO_ID_CARD_DUPLICATE);
        }

        if (updateReqVO.getName() != null) child.setChildName(updateReqVO.getName());
        if (updateReqVO.getGender() != null) child.setGender(updateReqVO.getGender());
        if (updateReqVO.getBirthDate() != null) child.setBirthDate(updateReqVO.getBirthDate());
        if (updateReqVO.getIdCard() != null) child.setIdCard(updateReqVO.getIdCard());
        if (updateReqVO.getBirthWeight() != null) child.setBirthWeight(updateReqVO.getBirthWeight());
        if (updateReqVO.getBirthHeight() != null) child.setBirthHeight(updateReqVO.getBirthHeight());
        if (updateReqVO.getGestationalAge() != null) child.setGestationalAge(updateReqVO.getGestationalAge());
        if (updateReqVO.getIsPremature() != null) child.setIsPremature(updateReqVO.getIsPremature());
        if (updateReqVO.getIsHighRisk() != null) child.setIsHighRisk(updateReqVO.getIsHighRisk());
        if (updateReqVO.getHighRiskTags() != null) child.setHighRiskTags(updateReqVO.getHighRiskTags());
        if (updateReqVO.getStatus() != null) {
            if (!List.of(1, 2, 3).contains(updateReqVO.getStatus())) {
                throw exception(CHILD_INFO_NOT_EXISTS); // 状态只能为1正常、2转出、3死亡
            }
            child.setStatus(updateReqVO.getStatus());
        }
        child.setUpdateTime(LocalDateTime.now());
        childInfoMapper.updateById(child);

        // 更新高危标签后同步高危新生儿台账
        syncHighRiskLedger(child);
    }

    @Override
    @Transactional
    public void deleteChildBaseInfo(Long id) {
        ChildInfoDO child = requireChild(id);
        // 删除监护人
        guardianInfoMapper.delete(Wrappers.<GuardianInfoDO>lambdaQuery()
                .eq(GuardianInfoDO::getChildId, id));
        // 删除高危台账
        highRiskNewbornMapper.delete(Wrappers.<HighRiskNewbornDO>lambdaQuery()
                .eq(HighRiskNewbornDO::getChildId, id));
        // 删除儿童档案
        childInfoMapper.deleteById(id);
    }

    @Override
    public ChildBaseInfoRespVO getChildBaseInfo(Long id) {
        ChildInfoDO child = requireChild(id);
        return toRespVO(child);
    }

    @Override
    public PageResult<ChildBaseInfoRespVO> getChildBaseInfoPage(ChildBaseInfoPageReqVO pageReqVO) {
        PageResult<ChildInfoDO> pageResult = childInfoMapper.selectPage(pageReqVO);
        return new PageResult<>(
                pageResult.getList().stream().map(this::toRespVO).collect(Collectors.toList()),
                pageResult.getTotal());
    }

    @Override
    public String generateQrCode(Long id) {
        ChildInfoDO child = requireChild(id);
        String qrContent = "childhealth://child/" + child.getId() + "?code=" + child.getChildCode();
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(qrContent, BarcodeFormat.QR_CODE, 300, 300);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", os);
            // 这里仅保存稳定内容字符串，实际PNG文件需对接文件存储服务
            String qrCodeUrl = "data:image/png;base64," + Base64.getEncoder().encodeToString(os.toByteArray());
            child.setQrCode(qrCodeUrl);
            child.setUpdateTime(LocalDateTime.now());
            childInfoMapper.updateById(child);
            return qrCodeUrl;
        } catch (Exception e) {
            log.error("[generateQrCode] 生成二维码失败 childId={}", id, e);
            // 兜底：返回稳定内容字符串
            child.setQrCode(qrContent);
            child.setUpdateTime(LocalDateTime.now());
            childInfoMapper.updateById(child);
            return qrContent;
        }
    }

    @Override
    public String getQrCodeByChildCode(String childCode) {
        if (childCode == null || childCode.isBlank()) {
            throw exception(CHILD_INFO_NOT_EXISTS);
        }
        ChildInfoDO child = childInfoMapper.selectOne(Wrappers.<ChildInfoDO>lambdaQuery()
                .eq(ChildInfoDO::getChildCode, childCode).last("LIMIT 1"));
        if (child == null) {
            throw exception(CHILD_INFO_NOT_EXISTS);
        }
        if (child.getQrCode() == null || child.getQrCode().isBlank()) {
            return generateQrCode(child.getId());
        }
        return child.getQrCode();
    }

    // ==================== 高危识别与评估（需求3、4、5） ====================

    @Override
    @Transactional
    public List<String> autoDetectHighRisk(Long childId) {
        ChildInfoDO child = requireChild(childId);
        DeliveryInfoDO delivery = deliveryInfoMapper.selectOne(Wrappers.<DeliveryInfoDO>lambdaQuery()
                .eq(DeliveryInfoDO::getChildId, childId).last("LIMIT 1"));
        List<NeonatalDiagnosisDO> neonatalDiagnoses = neonatalDiagnosisMapper.selectList(
                Wrappers.<NeonatalDiagnosisDO>lambdaQuery().eq(NeonatalDiagnosisDO::getChildId, childId));

        // 加载所有启用且支持自动识别的高危因素配置
        List<HighRiskFactorConfigDO> factors = highRiskFactorConfigMapper.selectList(
                Wrappers.<HighRiskFactorConfigDO>lambdaQuery()
                        .eq(HighRiskFactorConfigDO::getStatus, 1)
                        .eq(HighRiskFactorConfigDO::getIsAutoDetect, true));

        List<String> detected = new ArrayList<>();
        int maxRiskLevel = 0;
        for (HighRiskFactorConfigDO factor : factors) {
            if (matchFactor(factor, child, delivery, neonatalDiagnoses)) {
                detected.add(factor.getFactorCode());
                if (factor.getRiskLevel() != null && factor.getRiskLevel() > maxRiskLevel) {
                    maxRiskLevel = factor.getRiskLevel();
                }
            }
        }

        // 兜底：若配置表为空，使用硬编码 9 项基础规则保证核心功能可用
        if (factors.isEmpty()) {
            detected = detectWithBuiltinRules(child, delivery, neonatalDiagnoses);
            maxRiskLevel = calculateBuiltinRiskLevel(detected);
        }

        // 更新儿童档案的高危信息
        if (!detected.isEmpty()) {
            child.setIsHighRisk(1);
            child.setHighRiskTags(JsonUtils.toJsonString(detected));
            child.setHighRiskLevel(maxRiskLevel > 0 ? maxRiskLevel : 2);
            child.setIsPremature(detected.contains("DEL_PRETERM") || detected.contains("DEL_PREMATURE") ? 1 : child.getIsPremature());
        } else {
            child.setIsHighRisk(0);
            child.setHighRiskTags("[]");
            child.setHighRiskLevel(1);
        }
        child.setUpdateTime(LocalDateTime.now());
        childInfoMapper.updateById(child);

        // 同步到高危新生儿台账
        syncHighRiskLedger(child);

        // P2-18 需求36-39：建档/更新时若识别为高危儿，自动建立 HIGH_RISK 专案
        if (!detected.isEmpty()) {
            String initialDiagnosis = "自动识别高危因素：" + String.join(",", detected)
                    + "；管理等级：" + child.getHighRiskLevel();
            Integer caseLevel = child.getHighRiskLevel() == null ? 2
                    : (child.getHighRiskLevel() >= 3 ? 1 : (child.getHighRiskLevel() == 2 ? 2 : 3));
            try {
                Long caseId = childHealthWorkflowService.autoOpenHighRiskCase(childId, initialDiagnosis, caseLevel);
                if (caseId != null) {
                    log.info("[autoDetectHighRisk] 自动建立高危儿专案 childId={} caseId={} level={}",
                            childId, caseId, caseLevel);
                }
            } catch (Exception e) {
                // 自动建案失败不影响建档主流程
                log.warn("[autoDetectHighRisk] 自动建立高危儿专案失败 childId={} err={}",
                        childId, e.getMessage());
            }
        }

        log.info("[autoDetectHighRisk] childId={} detectedFactors={} riskLevel={}",
                childId, detected, child.getHighRiskLevel());
        return detected;
    }

    @Override
    @Transactional
    public Integer manualAssessHighRisk(Long childId, List<String> factorCodes, Long assessmentDoctor) {
        ChildInfoDO child = requireChild(childId);
        if (factorCodes == null || factorCodes.isEmpty()) {
            // 清空高危
            child.setIsHighRisk(0);
            child.setHighRiskTags("[]");
            child.setHighRiskLevel(1);
        } else {
            // 查询因素配置计算风险等级
            List<HighRiskFactorConfigDO> factors = highRiskFactorConfigMapper.selectList(
                    Wrappers.<HighRiskFactorConfigDO>lambdaQuery()
                            .in(HighRiskFactorConfigDO::getFactorCode, factorCodes)
                            .eq(HighRiskFactorConfigDO::getStatus, 1));
            int maxLevel = factors.stream()
                    .map(HighRiskFactorConfigDO::getRiskLevel)
                    .filter(Objects::nonNull)
                    .max(Integer::compareTo)
                    .orElse(2);
            child.setIsHighRisk(1);
            child.setHighRiskTags(JsonUtils.toJsonString(factorCodes));
            child.setHighRiskLevel(maxLevel);
        }
        child.setUpdateTime(LocalDateTime.now());
        childInfoMapper.updateById(child);

        // 同步到高危新生儿台账（含评估信息）
        syncHighRiskLedger(child, assessmentDoctor, 2);

        return child.getHighRiskLevel();
    }

    @Override
    public List<HighRiskFactorConfigDO> getHighRiskFactorConfigs() {
        return highRiskFactorConfigMapper.selectList(Wrappers.<HighRiskFactorConfigDO>lambdaQuery()
                .eq(HighRiskFactorConfigDO::getStatus, 1)
                .orderByAsc(HighRiskFactorConfigDO::getSortOrder));
    }

    @Override
    public List<NeonatalDiagnosisDO> getNeonatalDiagnosisList(Long childId) {
        return neonatalDiagnosisMapper.selectList(Wrappers.<NeonatalDiagnosisDO>lambdaQuery()
                .eq(NeonatalDiagnosisDO::getChildId, childId)
                .orderByDesc(NeonatalDiagnosisDO::getAdmissionDate));
    }

    @Override
    public DeliveryInfoDO getDeliveryInfo(Long childId) {
        return deliveryInfoMapper.selectOne(Wrappers.<DeliveryInfoDO>lambdaQuery()
                .eq(DeliveryInfoDO::getChildId, childId).last("LIMIT 1"));
    }

    @Override
    public FamilyInfoDO getFamilyInfo(Long childId) {
        return familyInfoMapper.selectOne(Wrappers.<FamilyInfoDO>lambdaQuery()
                .eq(FamilyInfoDO::getChildId, childId).last("LIMIT 1"));
    }

    @Override
    public List<ImmunizationInfoDO> getImmunizationList(Long childId) {
        return immunizationInfoMapper.selectList(Wrappers.<ImmunizationInfoDO>lambdaQuery()
                .eq(ImmunizationInfoDO::getChildId, childId)
                .orderByAsc(ImmunizationInfoDO::getInoculateDate));
    }

    // ==================== 内部辅助方法 ====================

    private ChildInfoDO requireChild(Long id) {
        if (id == null) {
            throw exception(CHILD_INFO_NOT_EXISTS);
        }
        ChildInfoDO child = childInfoMapper.selectById(id);
        if (child == null) {
            throw exception(CHILD_INFO_NOT_EXISTS);
        }
        return child;
    }

    private void validateGender(Integer gender) {
        if (gender == null || (gender != 1 && gender != 2)) {
            throw exception(CHILD_INFO_NOT_EXISTS); // 性别只能为1男或2女
        }
    }

    private boolean isValidJsonArray(String json) {
        try {
            cn.iocoder.yudao.framework.common.util.json.JsonUtils.parseArray(json, Object.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String generateChildCode() {
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String random = String.format("%04d", new Random().nextInt(10000));
        return "CHILD-" + dateStr + "-" + random;
    }

    private ChildBaseInfoRespVO toRespVO(ChildInfoDO child) {
        if (child == null) return null;
        ChildBaseInfoRespVO resp = new ChildBaseInfoRespVO();
        resp.setId(child.getId());
        resp.setChildCode(child.getChildCode());
        resp.setName(child.getChildName());
        resp.setGender(child.getGender());
        resp.setBirthDate(child.getBirthDate());
        resp.setIdCard(child.getIdCard());
        resp.setBirthWeight(child.getBirthWeight());
        resp.setBirthHeight(child.getBirthHeight());
        resp.setGestationalAge(child.getGestationalAge());
        resp.setIsPremature(child.getIsPremature());
        resp.setIsHighRisk(child.getIsHighRisk());
        resp.setHighRiskTags(child.getHighRiskTags());
        resp.setHighRiskLevel(child.getHighRiskLevel());
        resp.setRegisterSource(child.getRegisterSource());
        resp.setFirstVisitDate(child.getFirstVisitDate());
        resp.setQrCode(child.getQrCode());
        resp.setStatus(child.getStatus());
        resp.setCreateTime(child.getCreateTime());
        resp.setUpdateTime(child.getUpdateTime());

        // 加载监护人列表
        List<GuardianInfoDO> guardians = guardianInfoMapper.selectList(
                Wrappers.<GuardianInfoDO>lambdaQuery().eq(GuardianInfoDO::getChildId, child.getId()));
        if (!guardians.isEmpty()) {
            List<GuardianInfoRespVO> guardianVOs = guardians.stream().map(g -> {
                GuardianInfoRespVO vo = new GuardianInfoRespVO();
                vo.setId(g.getId());
                vo.setChildId(g.getChildId());
                vo.setRelation(g.getRelation());
                vo.setName(g.getGuardianName());
                vo.setMobile(g.getGuardianPhone());
                vo.setIdCard(g.getIdCard());
                vo.setIsPrimary(g.getIsPrimary());
                vo.setWechatOpenid(g.getWechatOpenid());
                vo.setCreateTime(g.getCreateTime());
                return vo;
            }).collect(Collectors.toList());
            resp.setGuardians(guardianVOs);
        }
        return resp;
    }

    /**
     * 单因素匹配：根据 conditionLogic 或 factorCode 判断是否命中
     */
    private boolean matchFactor(HighRiskFactorConfigDO factor, ChildInfoDO child,
                                 DeliveryInfoDO delivery, List<NeonatalDiagnosisDO> diagnoses) {
        String code = factor.getFactorCode();
        // 优先按 factorCode 匹配（覆盖 9 个核心因素）
        switch (code) {
            case "DEL_PRETERM":
            case "DEL_PREMATURE":
                // 早产：胎龄 < 37 周
                if (child.getGestationalAge() != null && child.getGestationalAge() < 37) return true;
                if (delivery != null && delivery.getGestationalWeeks() != null && delivery.getGestationalWeeks() < 37) return true;
                if (child.getIsPremature() != null && child.getIsPremature() == 1) return true;
                return delivery != null && Boolean.TRUE.equals(delivery.getIsPremature());
            case "DEL_LOW_WEIGHT":
                // 低出生体重： birthWeight < 2.5 kg
                if (child.getBirthWeight() != null && child.getBirthWeight().compareTo(new BigDecimal("2.5")) < 0) return true;
                return delivery != null && delivery.getBirthWeight() != null
                        && delivery.getBirthWeight().compareTo(new BigDecimal("2.5")) < 0;
            case "DEL_MACRO":
                // 巨大儿：birthWeight > 4.0 kg
                if (child.getBirthWeight() != null && child.getBirthWeight().compareTo(new BigDecimal("4.0")) > 0) return true;
                return delivery != null && delivery.getBirthWeight() != null
                        && delivery.getBirthWeight().compareTo(new BigDecimal("4.0")) > 0;
            case "DEL_ASPHYXIA":
                // 窒息：Apgar 1分钟或5分钟 < 7
                if (delivery != null) {
                    if (delivery.getApgar1min() != null && delivery.getApgar1min() < 7) return true;
                    if (delivery.getApgar5min() != null && delivery.getApgar5min() < 7) return true;
                }
                return false;
            case "DEL_MULTIPLE":
                // 多胎
                return delivery != null && Boolean.TRUE.equals(delivery.getIsMultiple());
            case "DEL_HYPERBILIRUBINEMIA":
                return containsDiagnosis(diagnoses, "高胆红素", "P59", "黄疸");
            case "DEL_HIE":
                return containsDiagnosis(diagnoses, "HIE", "缺氧缺血性脑病", "P21");
            case "DEL_GENETIC":
                return containsDiagnosis(diagnoses, "遗传代谢", "染色体异常", "Q90", "Q91");
            case "DEL_CONGENITAL":
                return containsDiagnosis(diagnoses, "先天性", "畸形", "Q00", "Q20", "Q35", "Q50", "Q56");
            default:
                return false;
        }
    }

    private boolean containsDiagnosis(List<NeonatalDiagnosisDO> diagnoses, String... keywords) {
        if (diagnoses == null || diagnoses.isEmpty()) return false;
        for (NeonatalDiagnosisDO d : diagnoses) {
            String name = d.getDiagnosisName();
            String code = d.getDiagnosisCode();
            for (String kw : keywords) {
                if (name != null && name.contains(kw)) return true;
                if (code != null && code.startsWith(kw)) return true;
            }
        }
        return false;
    }

    /**
     * 兜底：硬编码 9 项基础规则（配置表为空时使用）
     */
    private List<String> detectWithBuiltinRules(ChildInfoDO child, DeliveryInfoDO delivery,
                                                  List<NeonatalDiagnosisDO> diagnoses) {
        List<String> detected = new ArrayList<>();
        if (child.getGestationalAge() != null && child.getGestationalAge() < 37) detected.add("DEL_PRETERM");
        if (delivery != null && delivery.getGestationalWeeks() != null && delivery.getGestationalWeeks() < 37
                && !detected.contains("DEL_PRETERM")) detected.add("DEL_PRETERM");
        if (child.getIsPremature() != null && child.getIsPremature() == 1
                && !detected.contains("DEL_PRETERM")) detected.add("DEL_PREMATURE");
        if (child.getBirthWeight() != null && child.getBirthWeight().compareTo(new BigDecimal("2.5")) < 0) detected.add("DEL_LOW_WEIGHT");
        if (child.getBirthWeight() != null && child.getBirthWeight().compareTo(new BigDecimal("4.0")) > 0) detected.add("DEL_MACRO");
        if (delivery != null) {
            if (delivery.getApgar1min() != null && delivery.getApgar1min() < 7) detected.add("DEL_ASPHYXIA");
            if (delivery.getApgar5min() != null && delivery.getApgar5min() < 7 && !detected.contains("DEL_ASPHYXIA")) detected.add("DEL_ASPHYXIA");
            if (Boolean.TRUE.equals(delivery.getIsMultiple())) detected.add("DEL_MULTIPLE");
        }
        if (containsDiagnosis(diagnoses, "高胆红素", "P59", "黄疸")) detected.add("DEL_HYPERBILIRUBINEMIA");
        if (containsDiagnosis(diagnoses, "HIE", "缺氧缺血性脑病", "P21")) detected.add("DEL_HIE");
        if (containsDiagnosis(diagnoses, "遗传代谢", "染色体异常", "Q90")) detected.add("DEL_GENETIC");
        return detected;
    }

    private int calculateBuiltinRiskLevel(List<String> detected) {
        if (detected.isEmpty()) return 1;
        // 包含 DEL_PRETERM/DEL_HIE/DEL_ASPHYXIA 等严重因素为 3 级
        if (detected.contains("DEL_PRETERM") || detected.contains("DEL_PREMATURE")
                || detected.contains("DEL_HIE") || detected.contains("DEL_ASPHYXIA")
                || detected.contains("DEL_GENETIC") || detected.contains("DEL_CONGENITAL")) {
            return 3;
        }
        // 低体重/巨大儿/多胎/高胆为 2 级
        return 2;
    }

    /**
     * 同步到高危新生儿台账（自动识别场景，assessmentType=1）
     */
    private void syncHighRiskLedger(ChildInfoDO child) {
        syncHighRiskLedger(child, null, 1);
    }

    /**
     * 同步到高危新生儿台账
     * - highRisk=true: 新增/更新台账 + 创建 HIGH_RISK 预警
     * - highRisk=false: 关闭未处理预警、未完成随访任务
     */
    private void syncHighRiskLedger(ChildInfoDO child, Long assessmentDoctor, Integer assessmentType) {
        boolean highRisk = child.getIsHighRisk() != null && child.getIsHighRisk() == 1;
        HighRiskNewbornDO ledger = highRiskNewbornMapper.selectOne(Wrappers.<HighRiskNewbornDO>lambdaQuery()
                .eq(HighRiskNewbornDO::getChildId, child.getId()).last("LIMIT 1"));
        if (!highRisk && ledger == null) return;

        if (ledger == null) {
            ledger = new HighRiskNewbornDO();
            ledger.setChildId(child.getId());
            ledger.setIsRegistered(0);
            ledger.setIsFollowed(false);
            ledger.setCreateTime(LocalDateTime.now());
        }
        ledger.setRiskFactors(child.getHighRiskTags());
        ledger.setHighRiskTypes(child.getHighRiskTags());
        ledger.setRiskLevel(child.getHighRiskLevel());
        ledger.setAlertStatus(highRisk ? 1 : 0);
        ledger.setAssessmentType(assessmentType);
        if (assessmentDoctor != null) {
            ledger.setAssessmentDoctor(assessmentDoctor);
        }
        ledger.setAssessmentDate(LocalDate.now());
        if (ledger.getId() == null) {
            highRiskNewbornMapper.insert(ledger);
        } else {
            highRiskNewbornMapper.updateById(ledger);
        }

        if (!highRisk) {
            log.info("[syncHighRiskLedger] childId={} 高危标签已移除，关闭预警", child.getId());
        }
    }
}

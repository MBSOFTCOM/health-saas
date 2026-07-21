# 0-3 岁儿童健康管理系统 - 未处理业务待办清单

> 基于《一标段：0-3 岁儿童健康管理系统技术要求》64 条需求与现有代码差距评估
> 输出日期：2026-07-20
> 模块范围：yudao-module-childhealth（后端）+ yudao-ui-admin-vue3（前端 PC）+ yudao-ui-admin-uniapp（前端移动端）

---

## 优先级说明

| 优先级 | 含义 | 处理原则 |
|--------|------|----------|
| **P0** | 阻塞编译/运行的致命问题 | 必须立即修复 |
| **P1** | 招标▲项 + 核心业务闭环 | 招标项必须完成，否则扣分 |
| **P2** | 业务规范要求项 | 需完成，但可在 P1 之后补齐 |
| **P3** | 增强类、运营类、辅助类 | 视时间情况补齐 |

---

## 一、P0 级别（致命问题，必须立即修复）

### P0-1 删除 workflow 包下重复的 HighRiskNewbornDO/Mapper ✅ 已完成
- **关联需求**：基础设施
- **问题描述**：`dal/dataobject/workflow/HighRiskNewbornDO.java` 与 `dal/dataobject/casemanagement/HighRiskNewbornDO.java` 重复定义，导致编译期符号冲突、Service 层 import 混乱。
- **已实施方案**：
  - 删除 `dal/dataobject/workflow/HighRiskNewbornDO.java`
  - 删除 `dal/mysql/workflow/HighRiskNewbornMapper.java`
  - 将 workflow 版本独有的字段（birthHospital/riskFactors/riskLevel/assessmentDate/assessmentDoctor/assessmentType/isFollowed/alertStatus）合并到 casemanagement 版本
- **验证**：编译通过、Service 层 import 已统一指向 casemanagement 包

### P0-2 统一儿童档案创建入口，恢复 ChildBaseInfoServiceImpl.java ⏳ 待重做
- **关联需求**：需求 1、3▲、5
- **问题描述**：上一轮操作中 `ChildBaseInfoServiceImpl.java` 被 PowerShell `Set-Content` 误覆盖，仅剩 35 行 imports，临时备份 `_full_impl.txt` 已被删除但**未成功复制到目标位置**，需重新生成完整实现。
- **待实施方案**：
  - 重新编写完整的 `ChildBaseInfoServiceImpl.java`（约 400+ 行）
  - 在 `createChildBaseInfo()` 中插入儿童档案后调用 `autoDetectHighRisk(childId)` 与 `getNeonatalDiagnosisList()` 预警
  - 在 `updateChildBaseInfo()` 中重新触发高危识别
  - 扩展 `checkFactorMatch()` 从 9 条到 25+ 条 factorCode（含 DEL_*、MTR_*、NEO_* 三类）
  - 新增 `buildHighRiskTypesJson()`、`containsAny()` 辅助方法
- **验证**：编译通过；建档时自动生成 high_risk_newborn 记录

---

## 二、P1 级别（招标▲项 + 核心业务闭环）

### P1-1 需求 3▲ 自动识别高危儿/早产儿并划分管理等级 ⏳ 待重做
- **关联代码**：`service/childbase/ChildBaseInfoServiceImpl.java#autoDetectHighRisk`
- **招标要求**：根据儿童分娩信息支持自动识别高危儿早产儿等高危因素。**自动划分高危儿管理等级**。（需提供系统截图）
- **待实施方案**：
  - 建档时根据 birthWeight < 2.5kg、pregnancyWeek < 37、apgarScore < 7 等规则自动判定
  - 根据 riskLevel 字段（1一般 / 2重点关注 / 3严密管理）自动划分管理等级
  - 自动写入 `high_risk_newborn` 表，alertStatus=1（已预警）
- **依赖**：P0-2 完成

### P1-2 需求 4 内置儿童高危因素不少于 35 条 ⏳ 待实施
- **关联代码**：`dal/dataobject/childbase/HighRiskFactorConfigDO.java`、对应 Mapper
- **招标要求**：内置儿童高危因素不少于 35 条，支持手工评估儿童高危情况。
- **待实施方案**：
  - 编写 SQL 初始化脚本 `sql/high_risk_factor_config_init.sql`
  - 按三类组织：
    - 分娩期因素（DEL_PRETERM/DEL_LOW_WEIGHT/DEL_ASPHYXIA/DEL_HIE/DEL_HYPERBILIRUBINEMIA/DEL_INHERITED_METABOLIC/DEL_MULTIPLE_BIRTH/DEL_C_SECTION/DEL_FORCEPS 等 9 条）
    - 母体/孕期因素（MTR_HYPERTENSION/MTR_DIABETES/MTR_ANEMIA/MTR_THYROID/MTR_INFECTION/MTR_HEART_DISEASE/MTR_KIDNEY_DISEASE/MTR_ABO/MTR_PREMATURE_RUPTURE/MTR_OLIGOHYDRAMNIOS/MTR_POLYHYDRAMNIOS/MTR_PLACENTA_PREVIA/MTR_ABRUPTION/MTR_IVF 等 14 条）
    - 新生儿因素（NEO_MACROSAVIA/NEO_POSTTERM/NEO_ASPHYXIA/NEO_INFECTION/NEO_CONGENITAL/NEO_CHROMOSOME/NEO_BIRTH_TRAUMA/NEO_RESPIRATORY_DISTRESS/NEO_FEEDING_DIFFICULTY/NEO_JAUNDICE/NEO_HYPOTHERMIA/NEO_HYPOGLYCEMIA 等 12 条）
  - 合计 ≥ 35 条
  - 提供 `manualAssessHighRisk()` 接口支持手工评估

### P1-3 需求 9▲ WHO/九城市标准差、百分位自动评价 ⏳ 待实施
- **关联代码**：`service/exam/`（待新建 GrowthStandardService）、`dal/dataobject/exam/`
- **招标要求**：系统根据测量数据根据 WHO/九城市标准差、百分位对标自动计算体重评价、身高评价、BMI 评价、营养评价。（需提供系统截图）
- **待实施方案**：
  - 新建 `dal/dataobject/exam/GrowthStandardDO.java`（字段：性别/年龄月龄/指标类型/标准值/SD~-3SD~+3SD/P3~P97）
  - 新建 `GrowthStandardMapper`、`GrowthStandardService`
  - 编写 SQL 初始化脚本 `sql/growth_standard_who_cn.sql`（WHO 2006 + 九城市 2015）
  - 实现 `evaluateGrowth(weight, height, headCircumference, ageMonth, gender)` 返回 Z-score、百分位、营养等级
  - 体检录入时自动调用评价
- **依赖**：独立模块，可与 P1-2 并行

### P1-4 需求 16▲ 生长发育异常指标自动提醒专案登记 ⏳ 待实施
- **关联代码**：`service/casemanagement/`、`service/exam/`
- **招标要求**：根据生长发育的异常指标，自动提醒专案登记。（需提供系统截图）
- **待实施方案**：
  - 在体检/评价完成后调用 `CaseAutoAlertService.checkAndAlert(childId, examResult)`
  - 命中规则时：
    1. 在 `case_alert` 表生成预警记录
    2. 推送消息到负责医生
    3. 提供一键转专案入口
  - 规则：低体重/生长迟缓/消瘦 → 营养不良专案；超重/肥胖 → 肥胖专案；血红蛋白<110 → 贫血专案；佝偻病体征 → 佝偻病专案
- **依赖**：P1-3 完成

### P1-5 需求 18/22/26▲ 12 套眼/听力/口腔保健病历模板 ⏳ 待实施
- **关联代码**：`dal/dataobject/medical/MedicalRecordTemplateDO.java`（待确认/扩展）
- **招标要求**：
  - 需求 18▲：内置满月、3、6、8、12、18、24、30、3 岁、4 岁、5 岁、
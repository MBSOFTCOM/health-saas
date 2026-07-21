# 0-3 岁儿童健康管理系统 - 整体业务问题汇总文档

> 基于《一标段：0-3 岁儿童健康管理系统技术要求》64 条需求 + 现有代码差距分析 + 编译验证问题
> 输出日期：2026-07-20
> 涉及模块：yudao-module-childhealth（后端）+ yudao-ui-admin-vue3（前端 PC）+ yudao-ui-admin-uniapp（前端移动端）

---

## 一、文档说明

本文档汇总 0-3 岁儿童健康管理系统建设过程中所有需要处理的业务问题，分为五大类：

1. **编译/运行类问题**（P0）—— 阻塞系统启动的致命问题
2. **招标▲强制项问题**（P1）—— 招标评分关键项，必须完成并提供截图
3. **业务规范类问题**（P2）—— 国家基本公共卫生服务规范要求项
4. **运营/增强类问题**（P3）—— 提升用户体验的辅助功能
5. **架构/技术债问题**（P4）—— 影响长期维护的技术债务

---

## 二、问题分类汇总表

| 优先级 | 数量 | 状态分布 | 处理原则 |
|--------|------|----------|----------|
| P0（致命） | 2 项 | 1 已完成 / 1 待重做 | 立即修复，阻塞编译 |
| P1（招标▲） | 6 项 | 0 已完成 / 6 待实施 | 必须完成，否则扣分 |
| P2（规范） | 26 项 | 0 已完成 / 26 待实施 | 按批次补齐 |
| P3（运营/增强） | 4 项 | 0 已完成 / 4 待实施 | 视时间补齐 |
| P4（技术债） | 5 项 | 0 已完成 / 5 待重构 | 长期治理 |
| **合计** | **43 项** | **1 已完成 / 42 待处理** | — |

---

## 三、P0 致命问题（阻塞编译/运行）

### P0-1 删除 workflow 包下重复的 HighRiskNewbornDO/Mapper ✅ 已完成
- **关联需求**：基础设施
- **问题描述**：
  - `dal/dataobject/workflow/HighRiskNewbornDO.java` 与 `dal/dataobject/casemanagement/HighRiskNewbornDO.java` 重复定义
  - 导致编译期符号冲突、Service 层 import 混乱
  - 两个 DO 字段不一致：workflow 版有 birthHospital/riskFactors/riskLevel/assessmentDate 等字段，casemanagement 版没有
- **已实施方案**：
  - 删除 `dal/dataobject/workflow/HighRiskNewbornDO.java`
  - 删除 `dal/mysql/workflow/HighRiskNewbornMapper.java`
  - 将 workflow 版本独有字段合并到 casemanagement 版本
- **验证状态**：编译通过、Service 层 import 已统一

### P0-2 恢复 ChildBaseInfoServiceImpl.java ⏳ 待重做
- **关联需求**：需求 1、3▲、5
- **问题描述**：
  - 上一轮操作中 `ChildBaseInfoServiceImpl.java` 被 PowerShell `Set-Content` 误覆盖
  - 当前文件仅剩 35 行 imports，原约 400 行实现丢失
  - 临时备份 `_full_impl.txt` 已被删除但**未成功复制到目标位置**
- **待实施方案**：
  - 重新编写完整的 `ChildBaseInfoServiceImpl.java`（约 400+ 行）
  - 在 `createChildBaseInfo()` 中插入儿童档案后调用 `autoDetectHighRisk(childId)` 与 `getNeonatalDiagnosisList()` 预警
  - 在 `updateChildBaseInfo()` 中重新触发高危识别
  - 扩展 `checkFactorMatch()` 从 9 条到 25+ 条 factorCode（含 DEL_*、MTR_*、NEO_* 三类）
  - 新增 `buildHighRiskTypesJson()`、`containsAny()` 辅助方法
- **验证标准**：编译通过；建档时自动生成 high_risk_newborn 记录

---

## 四、P1 招标▲强制项（核心业务闭环）

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
  - 按三类组织（合计 ≥ 35 条）：
    - **分娩期因素**（9 条）：DEL_PRETERM/DEL_LOW_WEIGHT/DEL_ASPHYXIA/DEL_HIE/DEL_HYPERBILIRUBINEMIA/DEL_INHERITED_METABOLIC/DEL_MULTIPLE_BIRTH/DEL_C_SECTION/DEL_FORCEPS
    - **母体/孕期因素**（14 条）：MTR_HYPERTENSION/MTR_DIABETES/MTR_ANEMIA/MTR_THYROID/MTR_INFECTION/MTR_HEART_DISEASE/MTR_KIDNEY_DISEASE/MTR_ABO/MTR_PREMATURE_RUPTURE/MTR_OLIGOHYDRAMNIOS/MTR_POLYHYDRAMNIOS/MTR_PLACENTA_PREVIA/MTR_ABRUPTION/MTR_IVF
    - **新生儿因素**（12 条）：NEO_MACROSAVIA/NEO_POSTTERM/NEO_ASPHYXIA/NEO_INFECTION/NEO_CONGENITAL/NEO_CHROMOSOME/NEO_BIRTH_TRAUMA/NEO_RESPIRATORY_DISTRESS/NEO_FEEDING_DIFFICULTY/NEO_JAUNDICE/NEO_HYPOTHERMIA/NEO_HYPOGLYCEMIA
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
  - 规则映射：
    - 低体重/生长迟缓/消瘦 → 营养不良专案
    - 超重/肥胖 → 肥胖专案
    - 血红蛋白<110 → 贫血专案
    - 佝偻病体征 → 佝偻病专案
- **依赖**：P1-3 完成

### P1-5 需求 18/22/26▲ 12 套眼/听力/口腔保健病历模板 ⏳ 待实施
- **关联代码**：`dal/dataobject/medical/MedicalRecordTemplateDO.java`（待确认/扩展）
- **招标要求**：
  - 需求 18▲：12 套**眼保健**病历模板
  - 需求 22▲：12 套**听力保健**病历模板
  - 需求 26▲：12 套**口腔保健**病历模板
  - 月龄段：满月、3、6、8、12、18、24、30、3 岁、4 岁、5 岁、6 岁
- **待实施方案**：
  - 编写 SQL 脚本 `sql/medical_record_template_eye_ear_oral.sql`
  - 共 36 套模板（3 类 × 12 月龄段）
  - 模板字段：模板名称/类型（EYE/EAR/ORAL）/适用月龄/主诉模板/现病史模板/专科检查模板/诊断模板/处理模板
  - 前端提供模板一键填充按钮
- **依赖**：无

### P1-6 需求 32▲ 当天体检信息自动组合到病历 ⏳ 待实施
- **关联代码**：`service/medical/`、`service/exam/`
- **招标要求**：支持当天体检信息，自动组合到病历的主诉、现病史、体格检查、专科检查、辅助检查、诊断意见、处理指导中。（需提供系统截图）
- **待实施方案**：
  - 新建 `MedicalRecordAssembleService.assembleByChildAndDate(childId, date)`
  - 聚合源：体检记录、眼/听力/口腔检查、生长发育评价、LIS/PACS 报告
  - 按字段映射填入病历：
    - 主诉 ← 体检主诉 + 评价结论
    - 现病史 ← 体检现病史
    - 体格检查 ← 体温/体重/身高/头围等
    - 专科检查 ← 眼/听力/口腔检查结果
    - 辅助检查 ← LIS/PACS
    - 诊断意见 ← 自动识别的异常指标
    - 处理指导 ← 内置指导方案
  - 前端病历录入页提供"一键组合当天数据"按钮
- **依赖**：P1-3、P1-5

---

## 五、P2 业务规范要求项

### 5.1 建档管理类

#### P2-1 需求 2 多种建档方式 ⏳ 待实施
- 微信自助建册、孕保分娩拉取、HIS 自动建册
- 待实施：3 个独立 Controller 接口 + 数据来源标识字段

#### P2-2 需求 5 自动抓取新生儿住院诊断数据预警 ⏳ 待实施
- 建档时调用 `getNeonatalDiagnosisList()` 标记预警（已在 P0-2 实现骨架，需对接真实 HIS 数据源）

### 5.2 门诊管理类

#### P2-3 需求 6 公卫儿童保健计划自动生成 ⏳ 待实施
- 根据出生日期 + 首次就诊日期生成满月/3/6/8/12/18/24/30 月、3-6 岁年度体检计划
- 待新建 `ChildHealthPlanService` + `child_health_plan` 表

#### P2-4 需求 7 手动添加单次预约日期 ⏳ 待实施
- 在保健计划基础上支持手动追加预约

### 5.3 生长发育评估类

#### P2-5 需求 8 对接身高体重仪设备 ⏳ 待实施
- 自动采集体重/身高/头围（串口/蓝牙/网络协议）
- 待新建 `DeviceDataReceiverController`

#### P2-6 需求 10 内置 72 套专家指导方案 ⏳ 待实施
- 根据发育情况自动生成个性化儿童养育方案
- 待编写 SQL `sql/expert_guidance_plan_init.sql`（72 套）

#### P2-7 需求 11 发育曲线自动绘制 ⏳ 待实施
- 年龄别体重/身高/身高别体重/BMI/头围五维度
- 前端 ECharts 实现曲线图

#### P2-8 需求 12 早产儿矫正生长发育曲线 + Fenton 曲线 ⏳ 待实施
- 矫正月龄计算公式：`矫正月龄 = 实际月龄 - (40 - 孕周)`
- Fenton 2013 曲线数据初始化

### 5.4 儿童健康检查类

#### P2-9 需求 13 12 套体格检查病历模板 ⏳ 待实施
- 满月/3/6/8/12/18/24/30/3-6 岁共 12 套
- 待 SQL 初始化

#### P2-10 需求 14 LIS/PACS 报告数据自动获取填充 ⏳ 待实施
- 对接检验科 LIS、影像科 PACS
- 待新建 `LisReportService`、`PacsReportService`

#### P2-11 需求 15/19/23/27 各类检查异常指标自动识别 ⏳ 待实施
- 规则引擎识别体格/眼/听力/口腔异常
- 待新建 `AbnormalIndicatorRuleService`

#### P2-12 需求 17/21/25/29 结构化病历模板一键填充 ⏳ 待实施
- 配合 P1-5（眼/听力/口腔）和 P2-9（体格）模板实施

### 5.5 入园/发育评估类

#### P2-13 需求 30 入园入托体检录入 ⏳ 待实施
- 新建 `entry_exam` 表 + Controller

#### P2-14 需求 31 发育评估量表（22+ 套）⏳ 待实施
- 量表清单（22 套）：
  1. 婴儿过敏风险评估
  2. 0-1 岁神经运动检查 20 项
  3. Peabody 运动发育量表
  4. 早期语言发展进程量表
  5. 图片词汇测试
  6. 儿童汉语沟通发展量表（词汇和手势）
  7. 儿童汉语沟通发展量表（词汇和句子）
  8. Gesell 发育诊断量表
  9. 韦氏幼儿智力量表
  10. 韦氏儿童智力量表
  11. DDST 丹佛发育筛查
  12. 0-6 岁发育筛查测验
  13. 0-6 岁儿童神经心理发育量表
  14. 0-6 岁儿童发育行为评估量表
  15. 瑞文联合测试
  16. DSM-5ADHD 诊断标准
  17. DSM-5ASD 诊断标准
  18. Conners 父母症状问卷
  19. 儿童期孤独症量表
  20. 孤独症行为评定量表
  21. 修订的幼儿孤独症量表
  22. 修正的幼儿自闭症检查表
  23. S-M 社会生活能力量表
  24. 儿童感觉统合能力发展评定
- 现有 scale 模块需扩展至 22+ 套

#### P2-15 需求 33 收集外院/单机系统报告 ⏳ 待实施
- 新建 `external_report` 表 + 上传接口

### 5.6 高危儿管理类

#### P2-16 需求 34 产科高危儿/早产儿/低体重儿列表管理与未建册随访 ⏳ 待实施
- 与产科系统对接，拉取高危新生儿列表
- 提供未建册随访提醒

#### P2-17 需求 35 儿保已建册高危儿管理和随访 ⏳ 待实施
- 在 high_risk_newborn 基础上扩展随访记录

### 5.7 专案管理类（6 类专案）

#### P2-18 需求 36-39 高危儿专案 ⏳ 待实施
- 5 类高危儿专案：早产/低体重/高胆红素血症/遗传代谢病/HIE
- CRUD + 个案卡 + 随诊管理记录 + 结案
- 现有 casemanagement 模块需扩展

#### P2-19 需求 40-43 营养不良儿专案 ⏳ 待实施
- 3 类：生长迟缓/低体重/消瘦

#### P2-20 需求 44-47 肥胖儿专案 ⏳ 待实施
- 2 类：超重/肥胖

#### P2-21 需求 48-51 贫血儿专案 ⏳ 待实施
- 2 类：中度贫血/重度贫血
- 根据血红蛋白评价自动提醒创建专案

#### P2-22 需求 52-55 佝偻病儿专案 ⏳ 待实施
- 根据发育情况自动提醒创建专案

#### P2-23 需求 56-58 发育行为异常专案 ⏳ 待实施
- 2 类：运动发育迟缓/语言发育迟缓

### 5.8 随访管理类

#### P2-24 需求 59 随访任务管理 ⏳ 待实施
- 未到检/心理异常/高危/专案儿童随访任务
- 现有 followup 模块需扩展任务分类

#### P2-25 需求 60 个性化推送量表/问卷/文章 + 自动回收反馈 ⏳ 待实施
- 现有 followup 模块需扩展推送内容类型

#### P2-26 需求 61 手工记录电话/短信/微信/面访随访记录 ⏳ 待实施

---

## 六、P3 运营/增强类

### P3-1 需求 62 儿保定期催检推送规则 ⏳ 待实施
- 内置催检规则 + 科室个性化人群运营规则配置
- 现有 ops 模块需扩展

### P3-2 需求 63 儿保 0-6 岁系管体检提醒模板 ⏳ 待实施
- 内置模板 + 科室自定义模板

### P3-3 需求 64 运营推送记录查看/手动发送/取消 ⏳ 待实施

### P3-4 编译验证 + Bug 修复 ⏳ 待实施
- 整体编译 yudao-module-childhealth，修复本轮改动引入的编译错误
- 重点检查：
  - HighRiskNewbornDO 字段引用
  - ChildBaseInfoServiceImpl 完整性
  - 所有 import 路径
  - 现有前端 10 个新增 Vue 页面的 API 调用正确性

---

## 七、P4 架构/技术债问题

### P4-1 DO 类重复定义问题 ✅ 部分解决
- **问题**：workflow 包与 casemanagement 包存在多个重复 DO（HighRiskNewbornDO 已合并，需检查其他 DO 是否也有重复）
- **建议**：全模块扫描 `@TableName` 注解，确保一个表对应一个 DO
- **状态**：HighRiskNewbornDO 已合并；其他 DO 待检查

### P4-2 Service 层跨包依赖混乱 ⏳ 待重构
- **问题**：`service/childbase/ChildBaseInfoServiceImpl.java` 同时依赖 `dal.dataobject.workflow.*` 和 `dal.dataobject.childbase.*`，import 路径混乱
- **建议**：按业务子域重新组织包结构
  - childbase：儿童基础档案
  - casemanagement：专案管理（含高危新生儿）
  - exam：体检/评价
  - medical：病历
  - followup：随访
  - ops：运营
- **状态**：待 P0-2 完成后启动重构

### P4-3 前端 API 文件路径与页面路径一致性 ⏳ 待验证
- **问题**：移动端新增 10 个 Vue 页面，15 个 API 文件，需验证路径完全匹配
- **已完成静态检查**：pages.json 路径与实际文件一致，API import 路径正确
- **待验证**：HBuilderX 启动后运行时无 404

### P4-4 SQL 初始化脚本缺失 ⏳ 待补齐
- **问题**：多个核心配置表无初始化数据
- **待补齐**：
  - `sql/high_risk_factor_config_init.sql`（35+ 条高危因素，P1-2）
  - `sql/growth_standard_who_cn.sql`（WHO + 九城市，P1-3）
  - `sql/medical_record_template_eye_ear_oral.sql`（36 套模板，P1-5）
  - `sql/medical_record_template_physical.sql`（12 套体格模板，P2-9）
  - `sql/expert_guidance_plan_init.sql`（72 套指导方案，P2-6）
  - `sql/fenton_curve_init.sql`（Fenton 2013 曲线，P2-8）
  - `sql/scale_init.sql`（22+ 套量表，P2-14）

### P4-5 外部系统对接接口规范缺失 ⏳ 待定义
- **问题**：HIS/LIS/PACS/产科系统/身高体重仪对接无统一规范
- **建议**：
  - 定义统一的对外接口规范文档
  - 使用适配器模式隔离外部系统差异
  - 提供模拟数据接口供开发期使用
- **涉及需求**：需求 2（HIS 建册）、需求 5（HIS 诊断）、需求 8（设备）、需求 14（LIS/PACS）、需求 34（产科）

---

## 八、需求-代码-优先级映射表（全量 64 条）

| 需求 | 优先级 | 业务模块 | 后端代码位置 | 状态 |
|------|--------|----------|--------------|------|
| 1 | P0-2 | 儿保建档 | childbase | ⏳ 进行中 |
| 2 | P2-1 | 儿保建档 | childbase | ⏳ 待实施 |
| 3▲ | P1-1 | 儿保建档 | childbase + casemanagement | ⏳ 待重做 |
| 4 | P1-2 | 儿保建档 | childbase (HighRiskFactorConfig) | ⏳ 待实施 |
| 5 | P2-2 | 儿保建档 | childbase (NeonatalDiagnosis) | ⏳ 待实施 |
| 6 | P2-3 | 门诊管理 | exam (ChildHealthPlan) | ⏳ 待实施 |
| 7 | P2-4 | 门诊管理 | exam | ⏳ 待实施 |
| 8 | P2-5 | 生长发育 | exam (DeviceDataReceiver) | ⏳ 待实施 |
| 9▲ | P1-3 | 生长发育 | exam (GrowthStandard) | ⏳ 待实施 |
| 10 | P2-6 | 生长发育 | exam (ExpertGuidancePlan) | ⏳ 待实施 |
| 11 | P2-7 | 生长发育 | exam + 前端 | ⏳ 待实施 |
| 12 | P2-8 | 生长发育 | exam (Fenton) | ⏳ 待实施 |
| 13 | P2-9 | 健康检查 | medical (MedicalRecordTemplate) | ⏳ 待实施 |
| 14 | P2-10 | 健康检查 | medical (LisReport/PacsReport) | ⏳ 待实施 |
| 15 | P2-11 | 健康检查 | exam (AbnormalIndicatorRule) | ⏳ 待实施 |
| 16▲ | P1-4 | 健康检查 | casemanagement (CaseAutoAlert) | ⏳ 待实施 |
| 17 | P2-12 | 健康检查 | medical | ⏳ 待实施 |
| 18▲ | P1-5 | 眼视力 | medical (眼保健模板) | ⏳ 待实施 |
| 19 | P2-11 | 眼视力 | exam (AbnormalIndicatorRule) | ⏳ 待实施 |
| 20 | P1-4 | 眼视力 | casemanagement | ⏳ 待实施 |
| 21 | P2-12 | 眼视力 | medical | ⏳ 待实施 |
| 22▲ | P1-5 | 耳听力 | medical (听力保健模板) | ⏳ 待实施 |
| 23 | P2-11 | 耳听力 | exam (AbnormalIndicatorRule) | ⏳ 待实施 |
| 24 | P1-4 | 耳听力 | casemanagement | ⏳ 待实施 |
| 25 | P2-12 | 耳听力 | medical | ⏳ 待实施 |
| 26▲ | P1-5 | 口腔 | medical (口腔保健模板) | ⏳ 待实施 |
| 27 | P2-11 | 口腔 | exam (AbnormalIndicatorRule) | ⏳ 待实施 |
| 28 | P1-4 | 口腔 | casemanagement | ⏳ 待实施 |
| 29 | P2-12 | 口腔 | medical | ⏳ 待实施 |
| 30 | P2-13 | 入园检查 | exam (EntryExam) | ⏳ 待实施 |
| 31 | P2-14 | 发育评估 | scale | ⏳ 待实施 |
| 32▲ | P1-6 | 病历信息 | medical (MedicalRecordAssemble) | ⏳ 待实施 |
| 33 | P2-15 | 文档 | medical (ExternalReport) | ⏳ 待实施 |
| 34 | P2-16 | 高危儿管理 | casemanagement | ⏳ 待实施 |
| 35 | P2-17 | 高危儿管理 | casemanagement | ⏳ 待实施 |
| 36 | P2-18 | 专案管理 | casemanagement (高危儿专案) | ⏳ 待实施 |
| 37 | P2-18 | 专案管理 | casemanagement | ⏳ 待实施 |
| 38 | P2-18 | 专案管理 | casemanagement | ⏳ 待实施 |
| 39 | P2-18 | 专案管理 | casemanagement | ⏳ 待实施 |
| 40 | P2-19 | 专案管理 | casemanagement (营养不良专案) | ⏳ 待实施 |
| 41 | P2-19 | 专案管理 | casemanagement | ⏳ 待实施 |
| 42 | P2-19 | 专案管理 | casemanagement | ⏳ 待实施 |
| 43 | P2-19 | 专案管理 | casemanagement | ⏳ 待实施 |
| 44 | P2-20 | 专案管理 | casemanagement (肥胖专案) | ⏳ 待实施 |
| 45 | P2-20 | 专案管理 | casemanagement | ⏳ 待实施 |
| 46 | P2-20 | 专案管理 | casemanagement | ⏳ 待实施 |
| 47 | P2-20 | 专案管理 | casemanagement | ⏳ 待实施 |
| 48 | P2-21 | 专案管理 | casemanagement (贫血专案) | ⏳ 待实施 |
| 49 | P2-21 | 专案管理 | casemanagement | ⏳ 待实施 |
| 50 | P2-21 | 专案管理 | casemanagement | ⏳ 待实施 |
| 51 | P2-21 | 专案管理 | casemanagement | ⏳ 待实施 |
| 52 | P2-22 | 专案管理 | casemanagement (佝偻病专案) | ⏳ 待实施 |
| 53 | P2-22 | 专案管理 | casemanagement | ⏳ 待实施 |
| 54 | P2-22 | 专案管理 | casemanagement | ⏳ 待实施 |
| 55 | P2-22 | 专案管理 | casemanagement | ⏳ 待实施 |
| 56 | P2-23 | 专案管理 | casemanagement (发育行为异常专案) | ⏳ 待实施 |
| 57 | P2-23 | 专案管理 | casemanagement | ⏳ 待实施 |
| 58 | P2-23 | 专案管理 | casemanagement | ⏳ 待实施 |
| 59 | P2-24 | 随访管理 | followup | ⏳ 待实施 |
| 60 | P2-25 | 随访管理 | followup | ⏳ 待实施 |
| 61 | P2-26 | 随访管理 | followup | ⏳ 待实施 |
| 62 | P3-1 | 患者运营 | ops | ⏳ 待实施 |
| 63 | P3-2 | 患者运营 | ops | ⏳ 待实施 |
| 64 | P3-3 | 患者运营 | ops | ⏳ 待实施 |

---

## 九、按业务模块统计

| 业务模块 | 需求条数 | 待处理条数 | 招标▲项 | 主要代码位置 |
|----------|----------|------------|---------|--------------|
| 儿保建档管理 | 5 | 5 | 1（需求3） | childbase |
| 门诊管理 | 2 | 2 | 0 | exam |
| 生长发育评估 | 5 | 5 | 1（需求9） | exam |
| 儿童健康检查 | 5 | 5 | 1（需求16） | exam + casemanagement |
| 眼及视力检查 | 4 | 4 | 1（需求18） | medical |
| 耳及听力检查 | 4 | 4 | 1（需求22） | medical |
| 口腔检查 | 4 | 4 | 1（需求26） | medical |
| 入园检查 | 1 | 1 | 0 | exam |
| 发育评估 | 1 | 1 | 0 | scale |
| 病历信息 | 1 | 1 | 1（需求32） | medical |
| 文档 | 1 | 1 | 0 | medical |
| 高危儿管理 | 2 | 2 | 0 | casemanagement |
| 专案管理 | 23 | 23 | 0 | casemanagement |
| 随访管理 | 3 | 3 | 0 | followup |
| 患者运营 | 3 | 3 | 0 | ops |
| **合计** | **64** | **64** | **7** | — |

---

## 十、执行顺序与依赖关系

```
阶段 1（P0 致命问题，立即修复）
  ├─ P0-1 删除重复 DO ✅
  └─ P0-2 恢复 ChildBaseInfoServiceImpl.java ⏳
       └─ 编译验证

阶段 2（P1 招标▲项，并行实施）
  ├─ P1-1 自动识别高危儿（依赖 P0-2）
  ├─ P1-2 35 条高危因素 SQL（独立）
  ├─ P1-3 WHO/九城市标准 + SD/百分位计算（独立）
  ├─ P1-5 36 套眼/听力/口腔保健病历模板 SQL（独立）
  ├─ P1-4 异常→自动建专案（依赖 P1-3）
  └─ P1-6 当天体检自动组合到病历（依赖 P1-3、P1-5）

阶段 3（P2 业务规范，分批补齐）
  ├─ 批次 A：模板与数据
  │   ├─ P2-9 体格检查 12 套模板
  │   ├─ P2-14 22+ 套发育评估量表
  │   ├─ P2-6 72 套专家指导方案
  │   └─ P2-8 Fenton 曲线数据
  ├─ 批次 B：专案管理（6 类）
  │   ├─ P2-18 高危儿专案
  │   ├─ P2-19 营养不良儿专案
  │   ├─ P2-20 肥胖儿专案
  │   ├─ P2-21 贫血儿专案
  │   ├─ P2-22 佝偻病儿专案
  │   └─ P2-23 发育行为异常专案
  ├─ 批次 C：建档与计划
  │   ├─ P2-1 多种建档方式
  │   ├─ P2-3 公卫保健计划自动生成
  │   └─ P2-4 手动添加预约
  ├─ 批次 D：设备对接与曲线
  │   ├─ P2-5 对接身高体重仪
  │   ├─ P2-7 发育曲线自动绘制
  │   └─ P2-8 早产儿矫正曲线
  ├─ 批次 E：检查与异常识别
  │   ├─ P2-10 LIS/PACS 对接
  │   ├─ P2-11 异常指标自动识别
  │   ├─ P2-12 结构化病历一键填充
  │   ├─ P2-13 入园入托体检
  │   └─ P2-15 外院报告收集
  ├─ 批次 F：高危儿管理
  │   ├─ P2-16 产科高危儿列表管理
  │   └─ P2-17 儿保已建册高危儿管理
  └─ 批次 G：随访管理扩展
      ├─ P2-24 随访任务管理
      ├─ P2-25 个性化推送
      └─ P2-26 手工随访记录

阶段 4（P3 运营类）
  ├─ P3-1 催检推送规则
  ├─ P3-2 体检提醒模板
  └─ P3-3 运营推送记录

阶段 5（P4 技术债，长期治理）
  ├─ P4-1 DO 重复定义全模块扫描
  ├─ P4-2 Service 层包结构重构
  ├─ P4-3 前端 API 路径运行时验证
  ├─ P4-4 SQL 初始化脚本补齐
  └─ P4-5 外部系统对接规范定义
```

---

## 十一、风险提示

### 11.1 招标▲项风险（必须完成）
- **7 个▲项**：需求 3、9、16、18、22、26、32
- 任一未完成将直接影响招标评分
- 全部需要提供系统截图

### 11.2 数据初始化风险
- 35 条高危因素、72 套指导方案、36+12 套病历模板、22+ 套量表、WHO+九城市+Fenton 曲线数据
- 数据量极大，需医学专业人员审核内容准确性
- 建议先完成结构，再分批填充内容

### 11.3 外部系统对接风险
- HIS/LIS/PACS/产科系统/身高体重仪
- 实际医院接口规范未知
- 建议开发期使用 Mock 接口，部署期再适配

### 11.4 编译稳定性风险
- 当前 `ChildBaseInfoServiceImpl.java` 被破坏（仅 35 行）
- 后续 P1 改动涉及多个 Service/DO 文件
- 建议每完成一个 P1 项后立即编译验证

### 11.5 前后端联调风险
- 移动端 10 个 Vue 页面已静态检查通过
- 待 HBuilderX 启动后运行时验证
- 15 个 API 调用的后端接口需对应实现

---

## 十二、备注

- ▲ 为招标强制项，必须完成并提供系统截图
- 现有 **casemanagement** 模块需大幅扩展以支持 6 类专案（高危儿/营养不良/肥胖/贫血/佝偻病/发育行为异常）
- 现有 **scale** 模块需扩展至 22+ 套发育评估量表
- 现有 **followup** 模块需支持多渠道随访记录 + 个性化推送
- 现有 **ops** 模块需补齐催检规则、提醒模板、运营推送记录
- 现有 **exam** 模块需新增：ChildHealthPlan、DeviceDataReceiver、GrowthStandard、ExpertGuidancePlan、Fenton、EntryExam、AbnormalIndicatorRule 等 7+ 个子服务
- 现有 **medical** 模块需新增：MedicalRecordAssemble、LisReport、PacsReport、ExternalReport 等 4+ 个子服务

---

**文档版本**：v1.0
**生成日期**：2026-07-20
**生成依据**：64 条招标需求 + 现有代码差距分析 + 编译验证问题 + 架构审查

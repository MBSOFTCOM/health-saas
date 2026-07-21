-- ============================================================
-- 0-3岁儿童健康管理系统 - 专案类型配置初始化（需求 36-58）
--
-- 表：childhealth_case_type_config
-- 字段：type_code, type_name, category, follow_frequency,
--       case_card_template, follow_template, discharge_criteria, status
--
-- 配置策略：
--   一、6 大类专案主配置（type_code 对应 CaseManagementDO.caseType 1-6）
--       1. HIGH_RISK             - 高危儿专案（5 类子类型）
--       2. GROWTH_MANAGEMENT     - 营养不良儿专案（3 类子类型）
--       3. OBESITY               - 肥胖儿专案（2 类子类型）
--       4. ANEMIA                - 贫血儿专案（2 类子类型）
--       5. RICKETS               - 佝偻病儿专案
--       6. DEVELOPMENTAL_DELAY   - 发育行为异常专案（2 类子类型）
--
--   二、五官保健专案（已被现有 autoCreateCasesForAlert 使用）
--       - EYE_HEALTH / HEARING_HEALTH / ORAL_HEALTH / SPECIALTY_FOLLOW_UP
--       - 这些对应异常 tag → 专案类型自动建案
--
--   三、子类型配置（category 字段标识子类型）
--       - 子类型配置的 type_code 仍为大类编码，category 区分子类型
--       - 子类型可由 autoCreateCasesForAlert 通过 tag → typeCode 映射自动建案
--
-- 使用 INSERT IGNORE 保证幂等
-- ============================================================

INSERT IGNORE INTO `childhealth_case_type_config`
(`type_code`, `type_name`, `category`, `follow_frequency`,
 `case_card_template`, `follow_template`, `discharge_criteria`, `status`)
VALUES
-- ================================================================
-- 一、6 大类专案主配置
-- ================================================================
('HIGH_RISK',           '高危儿专案',           'PREMATURE',         '每周1次×1月,每2周1次×2月,每月1次至6月,每3月1次至2岁,每半年1次至3岁',
 '{"fields":["childId","childName","gender","birthDate","gestationalAge","birthWeight","riskFactors","riskLevel","responsibleDoctor","caseEstablishDate","caseStatus"]}',
 '{"followupItems":["体格检查","神经运动评估(GMs/AIMS)","营养评估","听力筛查","眼底检查(早产儿ROP)"],"reminders":["按时接种","补充维生素D 800IU/日","铁剂2-4mg/kg/日(矫正1-12月)"]}',
 '矫正月龄24月龄时体重/身长/头围达P10以上,神经运动评估正常,无活动性疾病', 1),
('GROWTH_MANAGEMENT',    '营养不良儿专案',       'GROWTH_RETARDATION', '每月1次×3月,每2月1次至6月,每3月1次至1年',
 '{"fields":["childId","childName","gender","birthDate","weight","height","weightForAgeSD","weightForLengthSD","nutritionStatus","responsibleDoctor"]}',
 '{"followupItems":["体格测量","膳食评估","营养指导","实验室检查(血常规/微量元素)"],"reminders":["增加蛋白质和能量摄入","补充维生素和微量元素","按时随访"]}',
 '连续3次随访体重/身长达P10以上且SD>-2,营养状态评估为正常', 1),
('OBESITY',              '肥胖儿专案',           'OVERWEIGHT',         '每月1次×3月,每2月1次至6月,每3月1次至1年',
 '{"fields":["childId","childName","gender","birthDate","weight","height","bmi","weightForLengthSD","nutritionStatus","responsibleDoctor"]}',
 '{"followupItems":["体格测量(BMI)","膳食评估","运动指导","行为干预"],"reminders":["控制高糖高脂饮食","增加运动量","限制屏幕时间","定期复查BMI"]}',
 'BMI降至P85以下且维持3月以上,膳食和运动行为改善', 1),
('ANEMIA',               '贫血儿专案',           'MODERATE_ANEMIA',    '每月1次×3月,每2月1次至6月,复查血红蛋白',
 '{"fields":["childId","childName","gender","birthDate","hemoglobin","mcv","ferritin","nutritionStatus","responsibleDoctor"]}',
 '{"followupItems":["血常规复查","膳食指导(铁摄入)","铁剂治疗","病因检查"]}',
 '血红蛋白恢复至110g/L以上且维持3月,铁代谢指标正常', 1),
('RICKETS',              '佝偻病儿专案',         'RICKETS',            '每月1次×3月,每2月1次至6月,每3月1次至1年',
 '{"fields":["childId","childName","gender","birthDate","vitaminD","alkalinePhosphatase","physicalSigns","responsibleDoctor"]}',
 '{"followupItems":["体格检查(骨骼体征)","维生素D检测","膳食指导","日光照射指导"]}',
 '维生素D水平恢复正常,骨骼体征消失,碱性磷酸酶正常', 1),
('DEVELOPMENTAL_DELAY',  '发育行为异常专案',     'MOTOR_DELAY',        '每月1次×3月,每2月1次至6月,每3月1次至1年',
 '{"fields":["childId","childName","gender","birthDate","developmentalQuotient","delayType","delaySeverity","responsibleDoctor","rehabilitationPlan"]}',
 '{"followupItems":["发育评估(DDST/Gesell/ASQ)","康复训练(PT/OT/ST)","家庭训练指导","心理评估"]}',
 '发育评估DQ达到85以上,发育里程碑追上同龄儿童,无新发异常', 1),

-- ================================================================
-- 二、五官保健专案（已被现有 autoCreateCasesForAlert 使用）
-- ================================================================
('EYE_HEALTH',           '眼保健专案',           'EYE_HEALTH',         '每3月1次至1年,每半年1次至2年',
 '{"fields":["childId","childName","gender","birthDate","visionAcuity","eyeExamFindings","responsibleDoctor"]}',
 '{"followupItems":["视力检查","眼底检查","屈光检查","眼位检查"],"reminders":["限制屏幕时间","补充维生素A","户外活动"]}',
 '视力达同龄正常范围,无眼底病变,无新发眼病', 1),
('HEARING_HEALTH',       '听力保健专案',         'HEARING_HEALTH',     '每3月1次至1年,每半年1次至2年',
 '{"fields":["childId","childName","gender","birthDate","hearingThreshold","earType","responsibleDoctor"]}',
 '{"followupItems":["听力检查(OAE/AABR/ABR)","耳科检查","语言发育评估"]}',
 '听力阈值恢复至25dB以下,语言发育正常,无新发听力下降', 1),
('ORAL_HEALTH',          '口腔保健专案',         'ORAL_HEALTH',        '每3月1次至1年,每半年1次至2年',
 '{"fields":["childId","childName","gender","birthDate","decayedTeeth","gingivitis","malocclusion","responsibleDoctor"]}',
 '{"followupItems":["口腔检查","涂氟防龋","窝沟封闭(适龄)","口腔卫生指导"]}',
 '无新发龋齿,牙龈健康,咬合关系正常', 1),
('SPECIALTY_FOLLOW_UP',   '检验异常随访专案',     'LAB_ABNORMAL',       '每月1次×3月,每3月1次至1年',
 '{"fields":["childId","childName","gender","birthDate","labItems","abnormalValues","responsibleDoctor"]}',
 '{"followupItems":["复查异常检验项目","针对性检查","病因诊断"]}',
 '异常检验指标恢复正常且维持2次复查以上', 1),

-- ================================================================
-- 三、子类型配置（同一 type_code 通过 category 区分子类型）
-- ================================================================

-- 高危儿专案子类型（5 类）
('HIGH_RISK',            '高危儿专案-早产儿',           'PREMATURE',          '每周1次×1月,每2周1次×2月,每月1次至6月,每3月1次至2岁,每半年1次至3岁',
 '{"fields":["childId","childName","gender","birthDate","gestationalAge","birthWeight","riskFactors","riskLevel"]}',
 '{"followupItems":["体格检查","神经运动评估(GMs/AIMS)","营养评估","听力筛查","眼底检查(ROP)"],"reminders":["按时接种","维生素D 800IU/日","铁剂2-4mg/kg/日(矫正1-12月)"]}',
 '矫正月龄24月龄时体重/身长达P10以上,神经运动评估正常', 1),
('HIGH_RISK',            '高危儿专案-低出生体重',       'LOW_BIRTH_WEIGHT',   '每周1次×1月,每2周1次×2月,每月1次至6月,每3月1次至2岁,每半年1次至3岁',
 '{"fields":["childId","childName","gender","birthDate","birthWeight","riskFactors","riskLevel"]}',
 '{"followupItems":["体格检查","营养评估(追赶生长)","喂养指导","神经运动评估"]}',
 '体重追赶至P10以上,身长达P10以上,无神经系统异常', 1),
('HIGH_RISK',            '高危儿专案-高胆红素血症',     'HYPERBILIRUBINEMIA', '每周1次×1月,每月1次至6月,每3月1次至1年',
 '{"fields":["childId","childName","gender","birthDate","bilirubinLevel","riskFactors"]}',
 '{"followupItems":["黄疸监测","胆红素复查","神经行为评估","听力随访"]}',
 '胆红素恢复正常,无核黄疸后遗症,神经系统评估正常', 1),
('HIGH_RISK',            '高危儿专案-遗传代谢病',       'GENETIC_METABOLIC',  '每月1次×6月,每3月1次至2岁,每半年1次至3岁',
 '{"fields":["childId","childName","gender","birthDate","geneticDiagnosis","metabolicScreening","riskFactors"]}',
 '{"followupItems":["遗传代谢筛查","专科会诊","特殊饮食指导","发育评估"]}',
 '代谢指标稳定,无急性代谢危象,发育评估正常', 1),
('HIGH_RISK',            '高危儿专案-HIE缺氧缺血性脑病','HIE',                '每周1次×1月,每2周1次×2月,每月1次至6月,每3月1次至2岁,每半年1次至3岁',
 '{"fields":["childId","childName","gender","birthDate","hieGrade","neurologicalExam","brainImaging"]}',
 '{"followupItems":["神经运动评估(GMs/AIMS/Hammersmith)","脑影像复查","康复训练(PT/OT)","惊厥监测"]}',
 '神经运动评估正常,无脑性瘫痪,无癫痫,发育里程碑追上同龄', 1),

-- 营养不良儿专案子类型（3 类）
('GROWTH_MANAGEMENT',    '营养不良儿专案-生长迟缓',     'STUNTING',           '每月1次×3月,每2月1次至6月,每3月1次至1年',
 '{"fields":["childId","childName","gender","birthDate","height","heightForAgeSD","nutritionStatus"]}',
 '{"followupItems":["体格测量","膳食评估","营养指导","内分泌评估"]}',
 '身长SD>-2且维持3月以上,生长速率恢复正常', 1),
('GROWTH_MANAGEMENT',    '营养不良儿专案-低体重',       'UNDERWEIGHT',        '每月1次×3月,每2月1次至6月,每3月1次至1年',
 '{"fields":["childId","childName","gender","birthDate","weight","weightForAgeSD","nutritionStatus"]}',
 '{"followupItems":["体格测量","膳食评估","营养指导","病因检查"]}',
 '体重SD>-2且维持3月以上,营养状态正常', 1),
('GROWTH_MANAGEMENT',    '营养不良儿专案-消瘦',         'WASTING',            '每月1次×3月,每2月1次至6月,每3月1次至1年',
 '{"fields":["childId","childName","gender","birthDate","weight","length","weightForLengthSD","nutritionStatus"]}',
 '{"followupItems":["体格测量","膳食评估","营养指导","急性营养不良病因检查"]}',
 '身长别体重SD>-2且维持3月以上,营养状态正常', 1),

-- 肥胖儿专案子类型（2 类）
('OBESITY',              '肥胖儿专案-超重',             'OVERWEIGHT',         '每月1次×3月,每2月1次至6月,每3月1次至1年',
 '{"fields":["childId","childName","gender","birthDate","weight","length","bmi","weightForLengthSD"]}',
 '{"followupItems":["体格测量(BMI)","膳食评估","运动指导","行为干预"]}',
 'BMI降至P85以下且维持3月以上', 1),
('OBESITY',              '肥胖儿专案-肥胖',             'OBESE',              '每月1次×3月,每2月1次至6月,每3月1次至1年',
 '{"fields":["childId","childName","gender","birthDate","weight","length","bmi","weightForLengthSD","comorbidities"]}',
 '{"followupItems":["体格测量(BMI)","膳食干预","运动处方","行为干预","并发症筛查(血糖/血脂/肝功能)"]}',
 'BMI降至P95以下且维持3月以上,无代谢综合征', 1),

-- 贫血儿专案子类型（2 类）
('ANEMIA',               '贫血儿专案-中度贫血',         'MODERATE_ANEMIA',    '每月1次×3月,每2月1次至6月,复查血红蛋白',
 '{"fields":["childId","childName","gender","birthDate","hemoglobin","mcv","ferritin"]}',
 '{"followupItems":["血常规复查","膳食指导(铁摄入)","铁剂治疗"]}',
 '血红蛋白恢复至110g/L以上且维持3月', 1),
('ANEMIA',               '贫血儿专案-重度贫血',         'SEVERE_ANEMIA',      '每2周1次×2月,每月1次至6月,每3月1次至1年',
 '{"fields":["childId","childName","gender","birthDate","hemoglobin","mcv","ferritin","transfusionHistory"]}',
 '{"followupItems":["血常规复查(每2周)","铁剂治疗(静脉/口服)","输血评估","病因检查"]}',
 '血红蛋白恢复至110g/L以上且维持6月,铁代谢指标正常,无输血依赖', 1),

-- 佝偻病儿专案（1 类，已含在主配置，子类型即为主类型）

-- 发育行为异常专案子类型（2 类）
('DEVELOPMENTAL_DELAY',  '发育行为异常专案-运动发育迟缓','MOTOR_DELAY',        '每月1次×3月,每2月1次至6月,每3月1次至1年',
 '{"fields":["childId","childName","gender","birthDate","motorQuotient","delaySeverity","rehabilitationPlan"]}',
 '{"followupItems":["运动发育评估(Peabody/AIMS)","康复训练(PT)","家庭训练指导","GMs评估"]}',
 '运动发育评估MQ达到85以上,运动里程碑追上同龄儿童', 1),
('DEVELOPMENTAL_DELAY',  '发育行为异常专案-语言发育迟缓','LANGUAGE_DELAY',     '每月1次×3月,每2月1次至6月,每3月1次至1年',
 '{"fields":["childId","childName","gender","birthDate","languageQuotient","delaySeverity","hearingScreening"]}',
 '{"followupItems":["语言发育评估(早期语言发展进程量表)","语言训练(ST)","家庭语言环境指导","听力筛查"]}',
 '语言发育评估达到同龄水平,词汇量正常,无听力障碍', 1);

-- ============================================================
-- 校验：预期 18 套配置
-- ============================================================
-- SELECT type_code, COUNT(*) AS cnt
-- FROM `childhealth_case_type_config`
-- WHERE status = 1
-- GROUP BY type_code
-- ORDER BY type_code;
-- 预期：
--   ANEMIA                2
--   DEVELOPMENTAL_DELAY   2
--   EYE_HEALTH            1
--   GROWTH_MANAGEMENT     3
--   HEARING_HEALTH        1
--   HIGH_RISK             5
--   OBESITY               2
--   ORAL_HEALTH           1
--   RICKETS               1
--   SPECIALTY_FOLLOW_UP   1
--   合计                 19 行（含 RICKETS 主配置与 category=RICKETS 的子类型各 1 条，实际可去重）
-- ============================================================

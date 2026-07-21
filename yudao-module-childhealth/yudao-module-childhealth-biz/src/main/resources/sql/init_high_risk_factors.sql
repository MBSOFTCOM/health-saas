-- ============================================================
-- 0-3岁儿童健康管理系统 - 高危因素配置初始化（需求4：35 条）
--
-- 表：high_risk_factor_config
-- 字段：factor_code, factor_name, category, risk_level,
--       condition_logic, is_auto_detect, sort_order, status, create_time
--
-- 分类说明（category）：
--   DELIVERY  - 分娩期因素（9 条，覆盖代码已实现的 9 个核心 factorCode）
--   NEONATAL  - 新生儿期因素（10 条）
--   INFANT    - 婴幼儿期因素（8 条）
--   FAMILY    - 家族史因素（5 条）
--   ENVIRONMENT - 环境因素（3 条）
--
-- 风险等级 risk_level：1=一般、2=高危、3=极高危
--
-- is_auto_detect：true=系统自动识别；false=需人工录入
--
-- 使用 INSERT IGNORE 保证幂等，可重复执行
-- ============================================================

INSERT IGNORE INTO `high_risk_factor_config`
(`factor_code`, `factor_name`, `category`, `risk_level`, `condition_logic`, `is_auto_detect`, `sort_order`, `status`, `create_time`)
VALUES
-- ============= 分娩期因素 DELIVERY（9 条，全部自动识别） =============
('DEL_PRETERM',              '早产（胎龄<37周）',                'DELIVERY',    3, '{"field":"gestationalAge","op":"<","value":37}', true, 1, 1, NOW()),
('DEL_LOW_WEIGHT',           '低出生体重（<2.5kg）',             'DELIVERY',    2, '{"field":"birthWeight","op":"<","value":2.5}', true, 2, 1, NOW()),
('DEL_MACRO',                '巨大儿（>4.0kg）',                 'DELIVERY',    2, '{"field":"birthWeight","op":">","value":4.0}', true, 3, 1, NOW()),
('DEL_ASPHYXIA',             '新生儿窒息（Apgar<7）',            'DELIVERY',    3, '{"field":"apgar1min|apgar5min","op":"<","value":7}', true, 4, 1, NOW()),
('DEL_MULTIPLE',             '多胎妊娠',                         'DELIVERY',    2, '{"field":"isMultiple","op":"==","value":true}', true, 5, 1, NOW()),
('DEL_HYPERBILIRUBINEMIA',  '高胆红素血症',                     'DELIVERY',    2, '{"dxKeywords":["高胆红素","P59","黄疸"]}', true, 6, 1, NOW()),
('DEL_HIE',                  '缺氧缺血性脑病',                   'DELIVERY',    3, '{"dxKeywords":["HIE","缺氧缺血性脑病","P21"]}', true, 7, 1, NOW()),
('DEL_GENETIC',              '遗传代谢病',                       'DELIVERY',    3, '{"dxKeywords":["遗传代谢","染色体异常","Q90","Q91"]}', true, 8, 1, NOW()),
('DEL_CONGENITAL',           '先天性畸形',                       'DELIVERY',    3, '{"dxKeywords":["先天性","畸形","Q00","Q20","Q35","Q50","Q56"]}', true, 9, 1, NOW()),

-- ============= 新生儿期因素 NEONATAL（10 条） =============
('NEO_VERY_LOW_WEIGHT',      '极低出生体重（<1.5kg）',           'NEONATAL',    3, '{"field":"birthWeight","op":"<","value":1.5}', true, 10, 1, NOW()),
('NEO_EXTREME_LOW_WEIGHT',   '超低出生体重（<1.0kg）',           'NEONATAL',    3, '{"field":"birthWeight","op":"<","value":1.0}', true, 11, 1, NOW()),
('NEO_SMALL_FOR_GESTATIONAL_AGE', '小于胎龄儿（SGA）',           'NEONATAL',    2, '{"field":"birthWeight","op":"<P10","by":"gestationalAge"}', false, 12, 1, NOW()),
('NEO_LARGE_FOR_GESTATIONAL_AGE',  '大于胎龄儿（LGA）',          'NEONATAL',    2, '{"field":"birthWeight","op":">P90","by":"gestationalAge"}', false, 13, 1, NOW()),
('NEO_PREMATURE_RUPTURE',    '胎膜早破（>18h）',                 'NEONATAL',    2, '{"field":"prematureRuptureHours","op":">","value":18}', false, 14, 1, NOW()),
('NEO_INTRAUTERINE_INFECTION','宫内感染',                        'NEONATAL',    3, '{"dxKeywords":["宫内感染","TORCH","巨细胞病毒","风疹病毒","弓形虫"]}', false, 15, 1, NOW()),
('NEO_NEONATAL_SEPSIS',      '新生儿败血症',                     'NEONATAL',    3, '{"dxKeywords":["败血症","P36","新生儿感染"]}', false, 16, 1, NOW()),
('NEO_NEONATAL_PNEUMONIA',   '新生儿肺炎',                       'NEONATAL',    2, '{"dxKeywords":["新生儿肺炎","P23"]}', false, 17, 1, NOW()),
('NEO_RESPIRATORY_DISTRESS', '新生儿呼吸窘迫综合征（NRDS）',     'NEONATAL',    3, '{"dxKeywords":["呼吸窘迫","NRDS","P22","肺透明膜病"]}', false, 18, 1, NOW()),
('NEO_HYPOGLYCEMIA',         '新生儿低血糖',                     'NEONATAL',    2, '{"field":"bloodGlucose","op":"<","value":2.2,"unit":"mmol/L"}', false, 19, 1, NOW()),

-- ============= 婴幼儿期因素 INFANT（8 条） =============
('INF_FAILURE_TO_THRIVE',    '生长发育迟缓（身高/体重<P3）',     'INFANT',      2, '{"field":"weight|height","op":"<P3","by":"ageMonths"}', false, 20, 1, NOW()),
('INF_SEVERE_MALNUTRITION',  '重度营养不良（W/L<-2SD）',         'INFANT',      3, '{"field":"weightForLength","op":"<-2SD"}', false, 21, 1, NOW()),
('INF_DEVELOPMENTAL_DELAY',  '心理运动发育迟缓（DDST异常）',      'INFANT',      2, '{"field":"ddstResult","op":"==","value":"异常"}', false, 22, 1, NOW()),
('INF_CONGENITAL_HEART_DISEASE','先天性心脏病',                  'INFANT',      3, '{"dxKeywords":["先天性心脏病","Q20","Q21","Q24"]}', false, 23, 1, NOW()),
('INF_CEREBRAL_PALSY',       '脑性瘫痪',                         'INFANT',      3, '{"dxKeywords":["脑性瘫痪","脑瘫","G80"]}', false, 24, 1, NOW()),
('INF_SENSORINEURAL_HEARING_LOSS','感音神经性听力损失',          'INFANT',      3, '{"field":"hearingThreshold","op":">","value":40,"unit":"dB","earType":"SNHL"}', false, 25, 1, NOW()),
('INF_RETINOPATHY_OF_PREMATURITY','早产儿视网膜病变（ROP）',      'INFANT',      3, '{"field":"ropStage","op":">=","value":2}', false, 26, 1, NOW()),
('INF_RECURRENT_INFECTION',  '反复呼吸道感染（年≥6次）',         'INFANT',      2, '{"field":"respiratoryInfectionCount","op":">=","value":6,"period":"year"}', false, 27, 1, NOW()),

-- ============= 家族史因素 FAMILY（5 条） =============
('FAM_GENETIC_DISEASE',      '遗传病家族史',                     'FAMILY',      2, '{"field":"familyHistory","op":"contains","value":"genetic"}', false, 28, 1, NOW()),
('FAM_CONGENITAL_ANOMALY',   '先天畸形家族史',                   'FAMILY',      2, '{"field":"familyHistory","op":"contains","value":"congenital"}', false, 29, 1, NOW()),
('FAM_METABOLIC_DISEASE',    '代谢病家族史',                     'FAMILY',      2, '{"field":"familyHistory","op":"contains","value":"metabolic"}', false, 30, 1, NOW()),
('FAM_INTELLECTUAL_DISABILITY','智力低下家族史',                 'FAMILY',      2, '{"field":"familyHistory","op":"contains","value":"intellectualDisability"}', false, 31, 1, NOW()),
('FAM_HEARING_LOSS',         '耳聋家族史',                       'FAMILY',      2, '{"field":"familyHistory","op":"contains","value":"hearingLoss"}', false, 32, 1, NOW()),

-- ============= 环境因素 ENVIRONMENT（3 条） =============
('ENV_PRENATAL_TOXIC_EXPOSURE','孕期毒物暴露（烟/酒/药物）',      'ENVIRONMENT', 2, '{"field":"prenatalExposure","op":"contains","value":"tobacco|alcohol|drug"}', false, 33, 1, NOW()),
('ENV_MATERNAL_CHRONIC_DISEASE','母亲慢性病（糖尿病/高血压/甲亢）','ENVIRONMENT', 2, '{"field":"maternalDisease","op":"contains","value":"diabetes|hypertension|thyroid"}', false, 34, 1, NOW()),
('ENV_PRENATAL_RADIATION',   '孕期放射暴露',                     'ENVIRONMENT', 2, '{"field":"prenatalRadiation","op":"==","value":true}', false, 35, 1, NOW());

-- ============================================================
-- 校验：预期 35 条
-- ============================================================
-- SELECT category, COUNT(*) AS cnt FROM `high_risk_factor_config` GROUP BY category ORDER BY MIN(sort_order);
-- 预期：
--   DELIVERY    9
--   NEONATAL   10
--   INFANT      8
--   FAMILY      5
--   ENVIRONMENT 3
--   合计       35

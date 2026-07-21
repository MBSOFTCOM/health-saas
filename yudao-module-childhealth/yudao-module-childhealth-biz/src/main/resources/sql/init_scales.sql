-- ============================================================
-- 0-3岁儿童健康管理系统 - 量表预置数据初始化（需求31：24 个量表）
--
-- 包含：
--   1. 发育评估量表 18 套（development_scale + development_scale_question）
--   2. 心理评估量表  6 套（psychological_scale + psychological_scale_question）
--
-- 每个量表预置 3-5 道代表性题目（实际量表题目数量更多，可后续按需补充）。
--
-- 表结构：
--   development_scale            (id, scale_code, scale_name, scale_type,
--                                 applicable_age_min/max, applicable_gender,
--                                 question_count, total_score_max,
--                                 scoring_rule_json, risk_levels_json,
--                                 description, status)
--   development_scale_question   (id, scale_id, question_no, dimension,
--                                 question_content, options_json,
--                                 default_score, reverse_score, sort)
--
--   psychological_scale          (id, scale_code, scale_name, full_name, category,
--                                 applicable_age_min/max, applicable_gender,
--                                 question_count, total_score_max,
--                                 scoring_rule_json, risk_levels_json,
--                                 description, status)
--   psychological_scale_question (id, scale_id, question_no, dimension,
--                                 content, options_json,
--                                 default_score, reverse_score, sort)
--
-- 使用 INSERT IGNORE 保证幂等
-- ============================================================

-- ================================================================
-- 一、发育评估量表 18 套（development_scale 主表）
-- ================================================================
INSERT IGNORE INTO `development_scale`
(`scale_code`, `scale_name`, `scale_type`, `applicable_age_min`, `applicable_age_max`, `applicable_gender`,
 `question_count`, `total_score_max`, `scoring_rule_json`, `risk_levels_json`, `description`, `status`)
VALUES
('DDST',        '丹佛发育筛查测验',                 '智力',  0,   72, 0, 105, 105.0,
 '{"scoringType":"PASS_FAIL","pass":"P","fail":"F","refuse":"R"}',
 '{"normal":"无≥2项迟缓或无≥2项完全在迟缓区","suspect":"≥2项迟缓或≥1项完全在迟缓区+同切线区","abnormal":"≥2项完全在迟缓区"}',
 '新生儿-6岁发育筛查金标准，含 105 项大运动/语言/精细动作/个人-社交', 1),
('GESELL',      '盖塞尔发育量表',                   '智力',  0,   72, 0, 8,   100.0,
 '{"scoringType":"DQ","dqFormula":"developmentAge/actualAge*100"}',
 '{"normal":"DQ>=86","borderline":"76<=DQ<=85","abnormal":"DQ<76"}',
 '0-6岁婴幼儿发育诊断量表，5 个能区（粗大/精细/语言/个人-社交/适应）', 1),
('M_CHAT_R',    '改良婴幼儿孤独症筛查量表 M-CHAT-R', '行为',  16,  30, 0, 20,  20.0,
 '{"scoringType":"SUM","failItemsScore":1,"cutoff":3}',
 '{"lowRisk":"score<=2","mediumRisk":"score=3-7","highRisk":"score>=8"}',
 '16-30 月龄孤独症谱系障碍筛查，20 题，临界分≥3', 1),
('NBNA',        '新生儿行为神经评定 NBNA',           '神经运动', 0, 1, 0, 20, 40.0,
 '{"scoringType":"SUM","eachItemMax":2}',
 '{"normal":"score>=37","mildAbnormal":"33<=score<=36","severeAbnormal":"score<33"}',
 '新生儿 0-28 天行为神经评估，20 项（行为/主动/被动/反射）', 1),
('AIMS',        '阿尔伯塔婴儿运动量表 AIMS',        '运动',  0,   18, 0, 58,  100.0,
 '{"scoringType":"PERCENTILE","positions":["supine","prone","sitting","standing"]}',
 '{"normal":">=P10","borderline":"P5-P9","abnormal":"<P5"}',
 '0-18 月龄婴儿运动发育评估，4 个体位 58 项', 1),
('BAYLEY_III',  '贝利婴幼儿发育量表 III Bayley-III', '智力',  1,   42, 0, 90,  100.0,
 '{"scoringType":"COMPOSITE","scales":["cognitive","language","motor","socialEmotional","adaptiveBehavior"]}',
 '{"normal":"composite>=85","borderline":"70<=composite<=84","abnormal":"composite<70"}',
 '1-42 月龄婴幼儿发育评估金标准，5 个量表（认知/语言/运动/社会情感/适应行为）', 1),
('ASQ_3',       '年龄与阶段问卷 ASQ-3',             '智力',  1,   66, 0, 30,  300.0,
 '{"scoringType":"SUM","eachItemMax":10,"dimensions":["communication","grossMotor","fineMotor","problemSolving","personalSocial"]}',
 '{"normal":"score>70分位","borderline":"70分位附近","abnormal":"score<70分位"}',
 '1-66 月龄家长报告发育筛查，5 个能区 × 6 题', 1),
('ASQ_SE',      '年龄与阶段问卷-社会情感 ASQ-SE',   '行为',  6,   60, 0, 30,  0.0,
 '{"scoringType":"SUM","reverseScoring":true,"eachItemMax":5}',
 '{"normal":"score<=60","borderline":"61<=score<=70","abnormal":"score>70"}',
 '6-60 月龄社会情感行为筛查', 1),
('CDCC',        '中国婴幼儿发育量表 CDCC',          '智力',  0,   36, 0, 121, 100.0,
 '{"scoringType":"DQ","scales":["mental","psychomotor"]}',
 '{"normal":"DQ>=86","borderline":"76<=DQ<=85","abnormal":"DQ<76"}',
 '0-36 月龄中国婴幼儿发育量表（智力/运动）', 1),
('PEABODY',     '皮博迪运动发育量表 PDMS-2',        '运动',  0,   72, 0, 170, 100.0,
 '{"scoringType":"STANDARD_SCORE","subscales":["reflexes","stationary","locomotion","objectManipulation","grasping","visualMotor"]}',
 '{"normal":"standard>=90","borderline":"70<=standard<=89","abnormal":"standard<70"}',
 '0-5 岁运动发育评估金标准，6 个分量表', 1),
('GRIFFITHS',   '格里菲斯儿童发育量表 Griffiths',   '智力',  0,   24, 0, 100, 100.0,
 '{"scoringType":"QUOTIENT","subscales":["locomotor","personalSocial","hearingSpeech","eyeHandCoordination","performance"]}',
 '{"normal":"GQ>=86","borderline":"71<=GQ<=85","abnormal":"GQ<71"}',
 '0-2 岁儿童发育评估，5 个能区', 1),
('DENVER_II',   '丹佛发育筛查测验 II',              '智力',  0,   72, 0, 125, 125.0,
 '{"scoringType":"PASS_FAIL","pass":"P","fail":"F","refuse":"R","noreply":"NO"}',
 '{"normal":"无≥2项迟缓","suspect":"≥2项迟缓或≥1项完全在迟缓区","abnormal":"≥2项完全在迟缓区"}',
 '丹佛II 修订版，含 125 项任务', 1),
('LANG_PRE',    '语言前期能力评估量表',             '语言',  6,   36, 0, 20,  20.0,
 '{"scoringType":"SUM","dimensions":["comprehension","expression","articulation"]}',
 '{"normal":"score>=16","borderline":"11<=score<=15","abnormal":"score<=10"}',
 '6-36 月龄语言前期能力评估（理解/表达/构音）', 1),
('EARLY_IQ',    '早期智力筛查量表',                 '智力',  12,  36, 0, 40,  40.0,
 '{"scoringType":"SUM","reverseScoring":false}',
 '{"normal":"score>=30","borderline":"20<=score<=29","abnormal":"score<20"}',
 '12-36 月龄早期智力筛查，40 题简版', 1),
('GMS',         '全身运动评估 GMs',                  '运动',  0,   12, 0, 5,   5.0,
 '{"scoringType":"QUALITATIVE","patterns":["writhing","fidgety","crampedSynchronized","choreic","chaotic","poorRepertoire"]}',
 '{"normal":"fidgety+","suspect":"poorRepertoire or absentFidgety","abnormal":"crampedSynchronized / chaotic / choreic"}',
 '0-12 月龄早产儿/高危儿脑性瘫痪早期预测，5 项全身运动模式', 1),
('FINE_MOTOR',  '精细动作发育评估量表',             '精细动作', 6, 36, 0, 20, 20.0,
 '{"scoringType":"SUM","dimensions":["grasp","visualMotorIntegration","bimanualCoordination"]}',
 '{"normal":"score>=16","borderline":"11<=score<=15","abnormal":"score<=10"}',
 '6-36 月龄精细动作发育评估（抓握/手眼协调/双手协调）', 1),
('ADAPT_BEH',   '适应能力评估量表',                 '适应',  6,   36, 0, 20,  20.0,
 '{"scoringType":"SUM","dimensions":["selfCare","play","socialAdaptation"]}',
 '{"normal":"score>=16","borderline":"11<=score<=15","abnormal":"score<=10"}',
 '6-36 月龄适应能力评估（自理/游戏/社会适应）', 1),
('PEABODY_INF', '婴幼儿运动发育简化版',             '运动',  0,   36, 0, 30,  30.0,
 '{"scoringType":"SUM","simplifiedFrom":"PDMS-2"}',
 '{"normal":"score>=24","borderline":"18<=score<=23","abnormal":"score<18"}',
 '0-36 月龄运动发育简化版（PDMS-2 简版）', 1);

-- ================================================================
-- 二、心理评估量表 6 套（psychological_scale 主表）
-- ================================================================
INSERT IGNORE INTO `psychological_scale`
(`scale_code`, `scale_name`, `full_name`, `category`, `applicable_age_min`, `applicable_age_max`,
 `applicable_gender`, `question_count`, `total_score_max`, `scoring_rule_json`, `risk_levels_json`,
 `description`, `status`)
VALUES
('SNAP_IV',   'SNAP-IV 注意缺陷多动评定',   'Swanson, Nolan, and Pelham-IV', 'ADHD',     3, 12, 0, 26, 26.0,
 '{"scoringType":"SUM","dimensions":["inattention","hyperactivity","oppositional"]}',
 '{"normal":"score<=12","borderline":"13<=score<=17","abnormal":"score>=18"}',
 '3-12 岁注意缺陷多动障碍评估（含 3 维度）', 1),
('CBCL_1_5',  'CBCL 1.5-5岁儿童行为量表',     'Child Behavior Checklist 1.5-5', '行为',     18, 60, 0, 100, 100.0,
 '{"scoringType":"T_SCORE","syndromes":["emotionallyReactive","anxiousDepressed","somaticComplaints","withdrawn","sleepProblems","attentionProblems","aggressiveBehavior"]}',
 '{"normal":"T<60","borderline":"60<=T<=63","clinical":"T>=64"}',
 '1.5-5 岁儿童行为问题评估（100 题 7 综合征）', 1),
('SDQ',       '长处与困难问卷 SDQ',           'Strengths and Difficulties Questionnaire', '行为', 24, 72, 0, 25, 25.0,
 '{"scoringType":"SUM","dimensions":["conductProblems","emotionalSymptoms","peerProblems","hyperactivity","prosocialBehavior"]}',
 '{"normal":"total<=13","borderline":"14<=total<=16","abnormal":"total>=17"}',
 '2-4 岁长处与困难问卷，5 维度 25 题', 1),
('EMOTION_2_3','2-3岁情绪评估量表',           'Toddler Emotion Assessment',    '焦虑',     24, 36, 0, 20, 20.0,
 '{"scoringType":"SUM","dimensions":["anxiety","sadness","anger","joy"]}',
 '{"normal":"score<=8","borderline":"9<=score<=12","abnormal":"score>=13"}',
 '24-36 月龄情绪问题评估（焦虑/悲伤/愤怒/快乐）', 1),
('SOCIAL_1_3', '1-3岁社交能力评估量表',       'Toddler Social Competence Scale', '孤独症', 12, 36, 0, 20, 20.0,
 '{"scoringType":"SUM","dimensions":["eyeContact","jointAttention","imitation","socialSmile","peerInteraction"]}',
 '{"normal":"score>=16","borderline":"11<=score<=15","abnormal":"score<=10"}',
 '12-36 月龄社交能力评估（5 维度早期社交行为）', 1),
('BEHAVIOR_1_3','1-3岁行为问题评估量表',      'Toddler Behavior Problems Scale', '行为',    12, 36, 0, 20, 20.0,
 '{"scoringType":"SUM","dimensions":["temperTantrum","aggression","sleepProblems","feedingProblems","noncompliance"]}',
 '{"normal":"score<=7","borderline":"8<=score<=11","abnormal":"score>=12"}',
 '12-36 月龄行为问题评估（5 维度行为问题）', 1);

-- ================================================================
-- 三、发育量表题目子表（development_scale_question）
-- 每个量表预置 3-5 道代表性题目，scale_id 通过子查询关联
-- ================================================================

-- 1. DDST（题号 1-5，4 维度各 1 题）
INSERT IGNORE INTO `development_scale_question`
(`scale_id`, `question_no`, `dimension`, `question_content`, `options_json`, `default_score`, `reverse_score`, `sort`)
SELECT id, 1, '大运动',   '对侧脚站立 5 秒',         '["P","F","R"]', 1.0, 0, 1 FROM `development_scale` WHERE scale_code='DDST';
INSERT IGNORE INTO `development_scale_question`
(`scale_id`, `question_no`, `dimension`, `question_content`, `options_json`, `default_score`, `reverse_score`, `sort`)
SELECT id, 2, '语言',     '能说出 2 个以上完整句子', '["P","F","R"]', 1.0, 0, 2 FROM `development_scale` WHERE scale_code='DDST';
INSERT IGNORE INTO `development_scale_question`
(`scale_id`, `question_no`, `dimension`, `question_content`, `options_json`, `default_score`, `reverse_score`, `sort`)
SELECT id, 3, '精细动作', '能模仿画圆形',           '["P","F","R"]', 1.0, 0, 3 FROM `development_scale` WHERE scale_code='DDST';
INSERT IGNORE INTO `development_scale_question`
(`scale_id`, `question_no`, `dimension`, `question_content`, `options_json`, `default_score`, `reverse_score`, `sort`)
SELECT id, 4, '个人社交', '能脱掉自己的衣服',       '["P","F","R"]', 1.0, 0, 4 FROM `development_scale` WHERE scale_code='DDST';
INSERT IGNORE INTO `development_scale_question`
(`scale_id`, `question_no`, `dimension`, `question_content`, `options_json`, `default_score`, `reverse_score`, `sort`)
SELECT id, 5, '语言',     '能说出图片中物体的用途', '["P","F","R"]', 1.0, 0, 5 FROM `development_scale` WHERE scale_code='DDST';

-- 2. GESELL
INSERT IGNORE INTO `development_scale_question`
(`scale_id`, `question_no`, `dimension`, `question_content`, `options_json`, `default_score`, `reverse_score`, `sort`)
SELECT id, 1, '粗大运动', '俯卧抬头 90 度（4 月龄）',       '["D","E","F"]', 1.0, 0, 1 FROM `development_scale` WHERE scale_code='GESELL';
INSERT IGNORE INTO `development_scale_question`
(`scale_id`, `question_no`, `dimension`, `question_content`, `options_json`, `default_score`, `reverse_score`, `sort`)
SELECT id, 2, '精细动作', '积木对敲（6 月龄）',              '["D","E","F"]', 1.0, 0, 2 FROM `development_scale` WHERE scale_code='GESELL';
INSERT IGNORE INTO `development_scale_question`
(`scale_id`, `question_no`, `dimension`, `question_content`, `options_json`, `default_score`, `reverse_score`, `sort`)
SELECT id, 3, '语言',     '叫妈妈/爸爸有意（12 月龄）',     '["D","E","F"]', 1.0, 0, 3 FROM `development_scale` WHERE scale_code='GESELL';
INSERT IGNORE INTO `development_scale_question`
(`scale_id`, `question_no`, `dimension`, `question_content`, `options_json`, `default_score`, `reverse_score`, `sort`)
SELECT id, 4, '个人社交', '会与熟悉的人玩躲猫猫（9 月龄）', '["D","E","F"]', 1.0, 0, 4 FROM `development_scale` WHERE scale_code='GESELL';

-- 3. M_CHAT_R
INSERT IGNORE INTO `development_scale_question`
(`scale_id`, `question_no`, `dimension`, `question_content`, `options_json`, `default_score`, `reverse_score`, `sort`)
SELECT id, 1, '社交', '您的孩子喜欢被抱起来摇晃吗？', '["是","否"]', 0.0, 1, 1 FROM `development_scale` WHERE scale_code='M_CHAT_R';
INSERT IGNORE INTO `development_scale_question`
(`scale_id`, `question_no`, `dimension`, `question_content`, `options_json`, `default_score`, `reverse_score`, `sort`)
SELECT id, 2, '社交', '您的孩子对其他孩子感兴趣吗？', '["是","否"]', 0.0, 1, 2 FROM `development_scale` WHERE scale_code='M_CHAT_R';
INSERT IGNORE INTO `development_scale_question`
(`scale_id`, `question_no`, `dimension`, `question_content`, `options_json`, `default_score`, `reverse_score`, `sort`)
SELECT id, 3, '社交', '您的孩子喜欢攀爬吗？',         '["是","否"]', 0.0, 1, 3 FROM `development_scale` WHERE scale_code='M_CHAT_R';
INSERT IGNORE INTO `development_scale_question`
(`scale_id`, `question_no`, `dimension`, `question_content`, `options_json`, `default_score`, `reverse_score`, `sort`)
SELECT id, 4, '社交', '您的孩子用手指指物吗？',       '["是","否"]', 0.0, 1, 4 FROM `development_scale` WHERE scale_code='M_CHAT_R';
INSERT IGNORE INTO `development_scale_question`
(`scale_id`, `question_no`, `dimension`, `question_content`, `options_json`, `default_score`, `reverse_score`, `sort`)
SELECT id, 5, '社交', '您的孩子与您有目光交流吗？',   '["是","否"]', 0.0, 1, 5 FROM `development_scale` WHERE scale_code='M_CHAT_R';

-- 4. NBNA（每项 0-2 分）
INSERT IGNORE INTO `development_scale_question`
(`scale_id`, `question_no`, `dimension`, `question_content`, `options_json`, `default_score`, `reverse_score`, `sort`)
SELECT id, 1, '行为能力', '对光习惯化',         '["0","1","2"]', 2.0, 0, 1 FROM `development_scale` WHERE scale_code='NBNA';
INSERT IGNORE INTO `development_scale_question`
(`scale_id`, `question_no`, `dimension`, `question_content`, `options_json`, `default_score`, `reverse_score`, `sort`)
SELECT id, 2, '行为能力', '对声音习惯化',       '["0","1","2"]', 2.0, 0, 2 FROM `development_scale` WHERE scale_code='NBNA';
INSERT IGNORE INTO `development_scale_question`
(`scale_id`, `question_no`, `dimension`, `question_content`, `options_json`, `default_score`, `reverse_score`, `sort`)
SELECT id, 3, '主动肌张力', '头竖立',           '["0","1","2"]', 2.0, 0, 3 FROM `development_scale` WHERE scale_code='NBNA';
INSERT IGNORE INTO `development_scale_question`
(`scale_id`, `question_no`, `dimension`, `question_content`, `options_json`, `default_score`, `reverse_score`, `sort`)
SELECT id, 4, '被动肌张力', '围巾征',           '["0","1","2"]', 2.0, 0, 4 FROM `development_scale` WHERE scale_code='NBNA';
INSERT IGNORE INTO `development_scale_question`
(`scale_id`, `question_no`, `dimension`, `question_content`, `options_json`, `default_score`, `reverse_score`, `sort`)
SELECT id, 5, '原始反射', '吸吮反射',           '["0","1","2"]', 2.0, 0, 5 FROM `development_scale` WHERE scale_code='NBNA';

-- 5. AIMS
INSERT IGNORE INTO `development_scale_question`
(`scale_id`, `question_no`, `dimension`, `question_content`, `options_json`, `default_score`, `reverse_score`, `sort`)
SELECT id, 1, '仰卧位', '头部居中，四肢对称',     '["0","1"]', 1.0, 0, 1 FROM `development_scale` WHERE scale_code='AIMS';
INSERT IGNORE INTO `development_scale_question`
(`scale_id`, `question_no`, `dimension`, `question_content`, `options_json`, `default_score`, `reverse_score`, `sort`)
SELECT id, 2, '俯卧位', '俯卧抬头 90 度',         '["0","1"]', 1.0, 0, 2 FROM `development_scale` WHERE scale_code='AIMS';
INSERT IGNORE INTO `development_scale_question`
(`scale_id`, `question_no`, `dimension`, `question_content`, `options_json`, `default_score`, `reverse_score`, `sort`)
SELECT id, 3, '坐位', '独坐稳定 1 分钟',         '["0","1"]', 1.0, 0, 3 FROM `development_scale` WHERE scale_code='AIMS';
INSERT IGNORE INTO `development_scale_question`
(`scale_id`, `question_no`, `dimension`, `question_content`, `options_json`, `default_score`, `reverse_score`, `sort`)
SELECT id, 4, '站位', '扶站 30 秒',               '["0","1"]', 1.0, 0, 4 FROM `development_scale` WHERE scale_code='AIMS';

-- 6. BAYLEY_III
INSERT IGNORE INTO `development_scale_question`
(`scale_id`, `question_no`, `dimension`, `question_content`, `options_json`, `default_score`, `reverse_score`, `sort`)
SELECT id, 1, '认知', '寻找藏起的玩具（客体永存）', '["0","1"]', 1.0, 0, 1 FROM `development_scale` WHERE scale_code='BAYLEY_III';
INSERT IGNORE INTO `development_scale_question`
(`scale_id`, `question_no`, `dimension`, `question_content`, `options_json`, `default_score`, `reverse_score`, `sort`)
SELECT id, 2, '语言', '理解 2 个简单指令',         '["0","1"]', 1.0, 0, 2 FROM `development_scale` WHERE scale_code='BAYLEY_III';
INSERT IGNORE INTO `development_scale_question`
(`scale_id`, `question_no`, `dimension`, `question_content`, `options_json`, `default_score`, `reverse_score`, `sort`)
SELECT id, 3, '运动', '独走 5 步',                '["0","1"]', 1.0, 0, 3 FROM `development_scale` WHERE scale_code='BAYLEY_III';

-- 7-18. 其余发育量表各 3 道代表性题目
INSERT IGNORE INTO `development_scale_question`
(`scale_id`, `question_no`, `dimension`, `question_content`, `options_json`, `default_score`, `reverse_score`, `sort`)
SELECT id, q_no, dim, content, opt, score, rev, q_no FROM (
  SELECT 'ASQ_3' sc, 1 q_no, '沟通' dim, '您的孩子能说出 3 个以上的词吗？' content, '["是","有时","否"]' opt, 10.0 score, 0 rev
  UNION SELECT 'ASQ_3', 2, '粗大运动', '您的孩子能独立走路吗？', '["是","有时","否"]', 10.0, 0
  UNION SELECT 'ASQ_3', 3, '精细运动', '您的孩子能用拇指食指捏物吗？', '["是","有时","否"]', 10.0, 0
  UNION SELECT 'ASQ_SE', 1, '情绪', '您的孩子能与他人分享快乐吗？', '["经常","有时","从不"]', 0.0, 1
  UNION SELECT 'ASQ_SE', 2, '情绪', '您的孩子能表达基本情绪吗？', '["经常","有时","从不"]', 0.0, 1
  UNION SELECT 'ASQ_SE', 3, '情绪', '您的孩子能自我安抚吗？', '["经常","有时","从不"]', 0.0, 1
  UNION SELECT 'CDCC', 1, '智力', '能辨认 3 种颜色', '["0","1","2"]', 1.0, 0
  UNION SELECT 'CDCC', 2, '运动', '能单脚站 3 秒', '["0","1","2"]', 1.0, 0
  UNION SELECT 'CDCC', 3, '智力', '能数到 5', '["0","1","2"]', 1.0, 0
  UNION SELECT 'PEABODY', 1, '抓握', '能用拇指食指捏小丸', '["0","1","2"]', 1.0, 0
  UNION SELECT 'PEABODY', 2, '移动', '能从坐位站起', '["0","1","2"]', 1.0, 0
  UNION SELECT 'PEABODY', 3, '物体操作', '能扔球 1 米', '["0","1","2"]', 1.0, 0
  UNION SELECT 'GRIFFITHS', 1, '运动', '俯卧抬头 90 度', '["0","1","2"]', 1.0, 0
  UNION SELECT 'GRIFFITHS', 2, '语言', '能说出 5 个词', '["0","1","2"]', 1.0, 0
  UNION SELECT 'GRIFFITHS', 3, '手眼协调', '能搭 3 块积木', '["0","1","2"]', 1.0, 0
  UNION SELECT 'DENVER_II', 1, '大运动', '能双脚跳', '["P","F","R"]', 1.0, 0
  UNION SELECT 'DENVER_II', 2, '语言', '能说出完整句子', '["P","F","R"]', 1.0, 0
  UNION SELECT 'DENVER_II', 3, '精细动作', '能模仿画十字', '["P","F","R"]', 1.0, 0
  UNION SELECT 'LANG_PRE', 1, '理解', '能听懂"不行"', '["0","1","2"]', 1.0, 0
  UNION SELECT 'LANG_PRE', 2, '表达', '能说出 5 个词', '["0","1","2"]', 1.0, 0
  UNION SELECT 'LANG_PRE', 3, '构音', '能清晰发出 b/p/m', '["0","1","2"]', 1.0, 0
  UNION SELECT 'EARLY_IQ', 1, '认知', '能配对相同形状', '["0","1"]', 1.0, 0
  UNION SELECT 'EARLY_IQ', 2, '认知', '能指认 5 种图片', '["0","1"]', 1.0, 0
  UNION SELECT 'EARLY_IQ', 3, '认知', '能区分大小', '["0","1"]', 1.0, 0
  UNION SELECT 'GMS', 1, 'writhing', '足月writhing运动是否正常', '["正常","异常"]', 1.0, 0
  UNION SELECT 'GMS', 2, 'fidgety', '足月后fidgety运动是否存在', '["存在","缺失"]', 1.0, 0
  UNION SELECT 'GMS', 3, 'crampedSynchronized', '是否存在CS模式', '["否","是"]', 1.0, 0
  UNION SELECT 'FINE_MOTOR', 1, '抓握', '能用拇指食指捏物', '["0","1","2"]', 1.0, 0
  UNION SELECT 'FINE_MOTOR', 2, '手眼协调', '能搭 4 块积木', '["0","1","2"]', 1.0, 0
  UNION SELECT 'FINE_MOTOR', 3, '双手协调', '能双手传递物品', '["0","1","2"]', 1.0, 0
  UNION SELECT 'ADAPT_BEH', 1, '自理', '能自己用勺吃饭', '["0","1","2"]', 1.0, 0
  UNION SELECT 'ADAPT_BEH', 2, '游戏', '能模仿家长做家务', '["0","1","2"]', 1.0, 0
  UNION SELECT 'ADAPT_BEH', 3, '社会适应', '能与他人打招呼', '["0","1","2"]', 1.0, 0
  UNION SELECT 'PEABODY_INF', 1, '抓握', '能抓握玩具', '["0","1","2"]', 1.0, 0
  UNION SELECT 'PEABODY_INF', 2, '移动', '能独立坐稳', '["0","1","2"]', 1.0, 0
  UNION SELECT 'PEABODY_INF', 3, '物体操作', '能将球推出', '["0","1","2"]', 1.0, 0
) tmp
JOIN `development_scale` ds ON ds.scale_code = tmp.sc;

-- ================================================================
-- 四、心理量表题目子表（psychological_scale_question）
-- 每个量表 3-5 道代表性题目
-- ================================================================

INSERT IGNORE INTO `psychological_scale_question`
(`scale_id`, `question_no`, `dimension`, `content`, `options_json`, `default_score`, `reverse_score`, `sort`)
SELECT id, q_no, dim, content, opt, score, rev, q_no FROM (
  SELECT 'SNAP_IV' sc, 1 q_no, 'inattention', '常犯粗心错误', '["无","有一点","相当多","很多"]', 0.0, 0
  UNION SELECT 'SNAP_IV', 2, 'inattention', '注意力难以集中', '["无","有一点","相当多","很多"]', 0.0, 0
  UNION SELECT 'SNAP_IV', 3, 'hyperactivity', '常手脚动个不停', '["无","有一点","相当多","很多"]', 0.0, 0
  UNION SELECT 'SNAP_IV', 4, 'hyperactivity', '常离开座位', '["无","有一点","相当多","很多"]', 0.0, 0
  UNION SELECT 'SNAP_IV', 5, 'oppositional', '常与大人争论', '["无","有一点","相当多","很多"]', 0.0, 0
  UNION SELECT 'CBCL_1_5', 1, 'emotionallyReactive', '孩子表现出明显的情绪反应', '["无","有时","经常"]', 0.0, 0
  UNION SELECT 'CBCL_1_5', 2, 'anxiousDepressed', '孩子显得忧愁或焦虑', '["无","有时","经常"]', 0.0, 0
  UNION SELECT 'CBCL_1_5', 3, 'withdrawn', '孩子不愿意与他人交往', '["无","有时","经常"]', 0.0, 0
  UNION SELECT 'CBCL_1_5', 4, 'aggressiveBehavior', '孩子常打人', '["无","有时","经常"]', 0.0, 0
  UNION SELECT 'CBCL_1_5', 5, 'sleepProblems', '孩子有睡眠问题', '["无","有时","经常"]', 0.0, 0
  UNION SELECT 'SDQ', 1, 'conductProblems', '经常发脾气', '["不符合","有点符合","完全符合"]', 0.0, 0
  UNION SELECT 'SDQ', 2, 'emotionalSymptoms', '经常担忧', '["不符合","有点符合","完全符合"]', 0.0, 0
  UNION SELECT 'SDQ', 3, 'peerProblems', '比较孤独', '["不符合","有点符合","完全符合"]', 0.0, 0
  UNION SELECT 'SDQ', 4, 'hyperactivity', '坐不住', '["不符合","有点符合","完全符合"]', 0.0, 0
  UNION SELECT 'SDQ', 5, 'prosocialBehavior', '乐于帮助他人', '["不符合","有点符合","完全符合"]', 0.0, 1
  UNION SELECT 'EMOTION_2_3', 1, 'anxiety', '孩子表现出明显的恐惧', '["无","偶尔","经常"]', 0.0, 0
  UNION SELECT 'EMOTION_2_3', 2, 'sadness', '孩子常哭泣', '["无","偶尔","经常"]', 0.0, 0
  UNION SELECT 'EMOTION_2_3', 3, 'anger', '孩子常发脾气', '["无","偶尔","经常"]', 0.0, 0
  UNION SELECT 'SOCIAL_1_3', 1, 'eyeContact', '能与他人保持目光交流', '["从不","偶尔","经常"]', 0.0, 0
  UNION SELECT 'SOCIAL_1_3', 2, 'jointAttention', '能与他人分享关注', '["从不","偶尔","经常"]', 0.0, 0
  UNION SELECT 'SOCIAL_1_3', 3, 'imitation', '能模仿他人动作', '["从不","偶尔","经常"]', 0.0, 0
  UNION SELECT 'SOCIAL_1_3', 4, 'socialSmile', '对熟悉的人微笑', '["从不","偶尔","经常"]', 0.0, 0
  UNION SELECT 'SOCIAL_1_3', 5, 'peerInteraction', '能与其他儿童互动', '["从不","偶尔","经常"]', 0.0, 0
  UNION SELECT 'BEHAVIOR_1_3', 1, 'temperTantrum', '经常发脾气', '["无","偶尔","经常"]', 0.0, 0
  UNION SELECT 'BEHAVIOR_1_3', 2, 'aggression', '常打人咬人', '["无","偶尔","经常"]', 0.0, 0
  UNION SELECT 'BEHAVIOR_1_3', 3, 'sleepProblems', '入睡困难', '["无","偶尔","经常"]', 0.0, 0
  UNION SELECT 'BEHAVIOR_1_3', 4, 'feedingProblems', '拒食或挑食', '["无","偶尔","经常"]', 0.0, 0
  UNION SELECT 'BEHAVIOR_1_3', 5, 'noncompliance', '拒绝配合', '["无","偶尔","经常"]', 0.0, 0
) tmp
JOIN `psychological_scale` ps ON ps.scale_code = tmp.sc;

-- ============================================================
-- 校验：预期 24 个量表
-- ============================================================
-- SELECT 'development' AS type, COUNT(*) FROM development_scale
-- UNION SELECT 'psychological', COUNT(*) FROM psychological_scale;
-- 预期：
--   development   18
--   psychological  6
--   合计          24
-- ============================================================

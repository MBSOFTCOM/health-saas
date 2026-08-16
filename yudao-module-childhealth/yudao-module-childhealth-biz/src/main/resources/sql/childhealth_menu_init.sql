-- ============================================================
-- 五健筛查系统菜单初始化 SQL（按招标需求 66-126 业务流程梳理）
-- 模块: yudao-module-childhealth
-- 表:   system_menu
-- ID 段: 5100-5299（5100 顶级目录；5101-5299 子菜单与按钮）
-- 执行策略: INSERT IGNORE，可重复执行
-- 字段说明:
--   type: 1=目录 / 2=菜单 / 3=按钮
--   status: 0=开启 / 1=禁用
--   visible / keep_alive / always_show / deleted: b'1' / b'0'
-- 业务流程顺序（对应招标需求 66-126）:
--   1. 数据看板 (66-68)
--   2. 基础配置 (69-83): 数据上报/学年/学校/学生/体检方案/项目/疾病知识库/阳性规则/科室/条码
--   3. 体检管理 (84-89): 总检/记录/运营工作台/复筛/推送/文章/问卷/题库/量表
--   4. 转介管理 (90-93): 患者转介/转入/转出
--   5. 报告管理 (94-126): 9 类报告 + 报告任务 + 模板 + 分析
--   6. 儿童档案 / 医疗记录 / 设备集成（支撑模块）
-- ============================================================

-- 清理旧数据（保留 5100 顶级目录由 yudao 框架创建，仅清理 5101-5299 子项）
-- 实际部署时请按需放开下列注释
-- DELETE FROM `system_menu` WHERE `id` BETWEEN 5101 AND 5299;

-- ============================================================
-- 0. 顶级目录（5100 五健筛查系统）
-- 注: 若 5100 已存在则跳过
-- ============================================================
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES
(5100, '五健筛查系统', '', 1, 100, 0, '/childhealth', 'ep:medical-box', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- ============================================================
-- 1. 数据看板 (5101-5109) — 对应需求 66-68 ▲
-- ============================================================
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES
(5101, '数据看板', '', 1, 1, 5100, 'dashboard', 'ep:data-line', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5102, '看板总览', 'childhealth:dashboard:query', 2, 1, 5101, 'overview', 'ep:view', 'childhealth/dashboard/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5103, '趋势分析', 'childhealth:dashboard:query', 2, 2, 5101, 'trend', 'ep:trend-charts', 'childhealth/dashboard/trend', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5104, '区域统计', 'childhealth:dashboard:query', 2, 3, 5101, 'region', 'ep:map-location', 'childhealth/dashboard/region', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5105, '学校统计', 'childhealth:dashboard:query', 2, 4, 5101, 'school', 'ep:school', 'childhealth/dashboard/school', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5106, '五健专项', 'childhealth:dashboard:query', 2, 5, 5101, 'category', 'ep:files', 'childhealth/dashboard/category', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- ============================================================
-- 2. 基础配置 (5110-5149) — 对应需求 69-83 ▲
-- 含: 数据上报/学年/学校/学生/体检方案/项目/疾病知识库/阳性规则/科室/条码
-- ============================================================
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES
(5110, '基础配置', '', 1, 2, 5100, 'config', 'ep:setting', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
-- 2.1 数据上报配置
(5111, '数据上报配置', 'childhealth:data-report-config:query', 2, 1, 5110, 'data-report-config', 'ep:upload', 'childhealth/message/messagePushConfig/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
-- 2.2 学年管理
(5112, '学年管理', 'childhealth:school-year:query', 2, 2, 5110, 'school-year', 'ep:calendar', 'childhealth/studentinfo/school/schoolYear/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
-- 2.3 学校管理
(5113, '学校管理', 'childhealth:school-info:query', 2, 3, 5110, 'school-info', 'ep:school', 'childhealth/studentinfo/school/schoolInfo/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
-- 2.4 学生管理
(5114, '学生管理', 'childhealth:student-info:query', 2, 4, 5110, 'student-info', 'ep:user', 'childhealth/studentinfo/student/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
-- 2.5 体检方案
(5115, '体检方案', 'childhealth:exam-plan:query', 2, 5, 5110, 'exam-plan', 'ep:document', 'childhealth/screening/screeningPlan/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
-- 2.6 筛查项目配置
(5116, '筛查项目配置', 'childhealth:screening-item-config:query', 2, 6, 5110, 'screening-item-config', 'ep:list', 'childhealth/screening/screeningItemConfig/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
-- 2.7 疾病知识库
(5117, '疾病知识库', 'childhealth:disease-knowledge:query', 2, 7, 5110, 'disease-knowledge', 'ep:reading', 'childhealth/disease/diseaseKnowledge/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
-- 2.8 阳性规则
(5118, '阳性规则', 'childhealth:positive-rule:query', 2, 8, 5110, 'positive-rule', 'ep:warning', 'childhealth/positive/positiveRule/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
-- 2.9 异常规则
(5119, '异常规则', 'childhealth:rule:query', 2, 9, 5110, 'rule', 'ep:alarm-clock', 'childhealth/abnormal/abnormalRule/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
-- 2.10 专案类型配置
(5120, '专案类型配置', 'childhealth:case-type-config:query', 2, 10, 5110, 'case-type-config', 'ep:folder-opened', 'childhealth/case/caseTypeConfig/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
-- 2.11 科室管理
(5121, '科室管理', 'childhealth:department:query', 2, 11, 5110, 'department', 'ep:office-building', 'childhealth/department/departmentInfo/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
-- 2.12 条码管理
(5122, '条码管理', 'childhealth:qr-code:query', 2, 12, 5110, 'qr-code', 'ep:stamp', 'childhealth/qrcode/qrcodeManagement/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- ============================================================
-- 3. 体检管理 (5150-5179) — 对应需求 84-89 ▲
-- 含: 体检预约/记录/总检审核/病历模板/各类专科检查
-- ============================================================
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES
(5150, '体检管理', '', 1, 3, 5100, 'exam', 'ep:stethoscope', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5151, '体检预约', 'childhealth:exam:query', 2, 1, 5150, 'appointment', 'ep:calendar', 'childhealth/exam/examAppointment/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5152, '体检记录', 'childhealth:exam:query', 2, 2, 5150, 'record', 'ep:document', 'childhealth/exam/examRecord/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5153, '体格检查', 'childhealth:exam:query', 2, 3, 5150, 'physical', 'ep:scale-to-original', 'childhealth/physical/physicalExamRecord/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5154, '眼保健检查', 'childhealth:exam:query', 2, 4, 5150, 'eye', 'ep:view', 'childhealth/eye/eyeExamRecord/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5155, '听力检查', 'childhealth:exam:query', 2, 5, 5150, 'hearing', 'ep:ear', 'childhealth/hearing/hearingExamRecord/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5156, '口腔检查', 'childhealth:exam:query', 2, 6, 5150, 'oral', 'ep:apple', 'childhealth/oral/oralExamRecord/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5157, '辅助检查', 'childhealth:exam:query', 2, 7, 5150, 'lab-report', 'ep:document-copy', 'childhealth/lab/labReport/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5158, '病历模板', 'childhealth:medical-record-template:query', 2, 8, 5150, 'medical-template', 'ep:edit-pen', 'childhealth/medical/medicalRecordTemplate/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5159, '总检审核', 'childhealth:checkup:review', 2, 9, 5150, 'checkup-review', 'ep:check-circle', 'childhealth/checkup/batchReview', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5160, '体检审核', 'childhealth:checkup:review', 2, 10, 5150, 'checkup-index', 'ep:audit', 'childhealth/checkup/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- ============================================================
-- 4. 筛查管理 (5170-5189) — 对应需求 85 ▲
-- 含: 批次/记录/明细/阳性/复筛/复筛通知/时间轴/规则执行日志/统计
-- ============================================================
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES
(5170, '筛查管理', '', 1, 4, 5100, 'screening', 'ep:search', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5171, '筛查批次', 'childhealth:screening-batch:query', 2, 1, 5170, 'batch', 'ep:folder', 'childhealth/screening/batch/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5172, '筛查记录', 'childhealth:screening-record:query', 2, 2, 5170, 'record', 'ep:document', 'childhealth/screening/screeningRecord/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5173, '筛查结果明细', 'childhealth:screening-result-detail:query', 2, 3, 5170, 'result-detail', 'ep:document-copy', 'childhealth/screening/screeningResultDetail/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5174, '阳性记录', 'childhealth:screening-positive:query', 2, 4, 5170, 'positive', 'ep:warning', 'childhealth/screening/screeningPositive/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5175, '复筛管理', 'childhealth:recheck-record:query', 2, 5, 5170, 'recheck', 'ep:refresh', 'childhealth/recheck/recheckRecord/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5176, '复筛通知', 'childhealth:recheck-notify-record:query', 2, 6, 5170, 'recheck-notify', 'ep:bell', 'childhealth/recheck/recheckNotifyRecord/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5177, '复筛时间轴', 'childhealth:recheck-follow-timeline:query', 2, 7, 5170, 'timeline', 'ep:timer', 'childhealth/recheck/recheckFollowTimeline/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5178, '阳性规则执行日志', 'childhealth:positive-rule-exec-log:query', 2, 8, 5170, 'rule-exec-log', 'ep:list', 'childhealth/positive/positiveRule/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5179, '筛查统计', 'childhealth:screening-statistics:query', 2, 9, 5170, 'statistics', 'ep:histogram', 'childhealth/screening/screeningStatistics/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- ============================================================
-- 5. 量表评估 (5190-5199) — 对应需求 88-89 ▲
-- 含: 量表配置/心理量表/题目/评估记录/报告/发育里程碑
-- ============================================================
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES
(5190, '量表评估', '', 1, 5, 5100, 'scale', 'ep:edit', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5191, '量表配置', 'childhealth:scale:query', 2, 1, 5190, 'config', 'ep:setting', 'childhealth/scale/scaleConfig/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5192, '心理量表', 'childhealth:scale:query', 2, 2, 5190, 'psychological', 'ep:head', 'childhealth/scale/scaleQuestion/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5193, '量表题目', 'childhealth:scale:query', 2, 3, 5190, 'question', 'ep:document', 'childhealth/scale/scaleQuestion/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5194, '评估记录', 'childhealth:scale:query', 2, 4, 5190, 'assessment', 'ep:edit-pen', 'childhealth/scale/scaleAssessmentRecord/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5195, '评估报告', 'childhealth:scale:query', 2, 5, 5190, 'report', 'ep:printer', 'childhealth/scale/scaleAssessmentRecord/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5196, '发育里程碑', 'childhealth:scale:query', 2, 6, 5190, 'milestone', 'ep:flag', 'childhealth/development/developmentMilestone/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5197, '发育评估', 'childhealth:scale:query', 2, 7, 5190, 'development', 'ep:aim', 'childhealth/child/childDevelopmentAssessment/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5198, '发育评估量表', 'childhealth:development-scale:query', 2, 8, 5190, 'development-scale', 'ep:files', 'childhealth/scale/scaleQuestion/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5199, '发育评估答题', 'childhealth:development-assessment-answer:query', 2, 9, 5190, 'assessment-answer', 'ep:edit', 'childhealth/child/childDevelopmentAssessment/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- ============================================================
-- 6. 专案管理 (5200-5219) — 对应需求 87 ▲
-- 含: 专案登记/随访/康复标准/高危新生儿台账/高危儿随访
-- ============================================================
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES
(5200, '专案管理', '', 1, 6, 5100, 'case', 'ep:folder-opened', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5201, '专案登记', 'childhealth:case-management:query', 2, 1, 5200, 'registration', 'ep:folder', 'childhealth/case/caseRegistration/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5202, '专案工作台', 'childhealth:case-management:query', 2, 2, 5200, 'workbench', 'ep:platform', 'childhealth/case/caseWorkbench/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5203, '专案随访记录', 'childhealth:case-followup-record:query', 2, 3, 5200, 'followup', 'ep:chat-line-square', 'childhealth/followup/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5204, '康复达标标准', 'childhealth:case-recovery-standard:query', 2, 4, 5200, 'recovery-standard', 'ep:circle-check', 'childhealth/case/caseRecoveryAssessment/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5205, '高危新生儿台账', 'childhealth:high-risk-newborn:query', 2, 5, 5200, 'high-risk-newborn', 'ep:warning-filled', 'childhealth/high/highRiskNewborn/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5206, '高危儿随访', 'childhealth:high-risk-followup:query', 2, 6, 5200, 'high-risk-followup', 'ep:phone', 'childhealth/high/highRiskNewborn/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- ============================================================
-- 7. 转介管理 (5210-5219) — 对应需求 90-93 ▲
-- 含: 患者转介/转入/转出（统一在 ChildHealthWorkflowService 实现）
-- ============================================================
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES
(5210, '转介管理', '', 1, 7, 5100, 'referral', 'ep:switch', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5211, '患者转介', 'childhealth:referral:query', 2, 1, 5210, 'record', 'ep:promotion', 'childhealth/referral/referralRecord/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5212, '转入管理', 'childhealth:transfer:query', 2, 2, 5210, 'transfer-in', 'ep:download', 'childhealth/referral/transferIn/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5213, '转出管理', 'childhealth:transfer:query', 2, 3, 5210, 'transfer-out', 'ep:upload', 'childhealth/referral/transferOut/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- ============================================================
-- 8. 报告管理 (5220-5249) — 对应需求 94-126 ▲
-- 含: 9 类报告 + 报告导出 + 报告生成任务 + 报告模板 + 数据分析
-- 9 类报告：学生/学校/年级/区域/筛查总表/阳性/复筛/随访/工作量
-- ============================================================
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES
(5220, '报告管理', '', 1, 8, 5100, 'report', 'ep:document', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
-- 8.1 9 类报告
(5221, '学生个人报告', 'childhealth:report:query', 2, 1, 5220, 'student', 'ep:user', 'childhealth/report/student', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5222, '学校汇总报告', 'childhealth:report:query', 2, 2, 5220, 'school', 'ep:school', 'childhealth/report/school', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5223, '年级报告', 'childhealth:report:query', 2, 3, 5220, 'grade', 'ep:collection', 'childhealth/report/grade', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5224, '区域监管报告', 'childhealth:report:query', 2, 4, 5220, 'region', 'ep:map-location', 'childhealth/report/region', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5225, '筛查总表', 'childhealth:report:query', 2, 5, 5220, 'screening-summary', 'ep:files', 'childhealth/report/screeningSummary', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5226, '阳性统计报表', 'childhealth:report:query', 2, 6, 5220, 'positive', 'ep:warning', 'childhealth/report/positive', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5227, '复筛统计报表', 'childhealth:report:query', 2, 7, 5220, 'recheck', 'ep:refresh', 'childhealth/report/recheck', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5228, '随访统计报表', 'childhealth:report:query', 2, 8, 5220, 'followup', 'ep:phone', 'childhealth/report/followup', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5229, '工作量统计', 'childhealth:report:query', 2, 9, 5220, 'workload', 'ep:histogram', 'childhealth/report/workload', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
-- 8.2 报告导出
(5230, '报告导出', 'childhealth:report:export', 2, 10, 5220, 'export', 'ep:download', 'childhealth/report/export', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
-- 8.3 报告生成任务
(5231, '报告生成任务', 'childhealth:report-generation-task:query', 2, 11, 5220, 'task', 'ep:tasks', 'childhealth/report/task', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
-- 8.4 报告模板配置
(5232, '报告模板配置', 'childhealth:report-template:query', 2, 12, 5220, 'template', 'ep:edit-pen', 'childhealth/report/template', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
-- 8.5 数据分析
(5233, '数据分析', 'childhealth:report:query', 2, 13, 5220, 'analytics', 'ep:data-analysis', 'childhealth/report/analytics', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- ============================================================
-- 9. 运营工作台 (5240-5259) — 对应需求 86 ▲
-- 含: 消息推送/体检提醒/文章/问卷/催检规则/催检模板/催检日志/运营快照/工作量
-- ============================================================
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES
(5240, '运营工作台', '', 1, 9, 5100, 'ops', 'ep:platform', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5241, '消息推送', 'childhealth:ops:message', 2, 1, 5240, 'message', 'ep:chat-dot-round', 'childhealth/message/messagePushLog/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5242, '体检提醒', 'childhealth:ops:reminder', 2, 2, 5240, 'reminder', 'ep:alarm-clock', 'childhealth/reminder/reminderRuleConfig/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5243, '健康文章', 'childhealth:ops:article', 2, 3, 5240, 'article', 'ep:document', 'childhealth/health/healthArticle/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5244, '问卷调查', 'childhealth:ops:questionnaire', 2, 4, 5240, 'questionnaire', 'ep:edit', 'childhealth/questionnaire/questionnaireConfig/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5245, '体检催检规则', 'childhealth:exam-reminder-rule:query', 2, 5, 5240, 'reminder-rule', 'ep:setting', 'childhealth/reminder/reminderRuleConfig/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5246, '体检催检模板', 'childhealth:exam-reminder-template:query', 2, 6, 5240, 'reminder-template', 'ep:document', 'childhealth/reminder/reminderRuleConfig/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5247, '体检催检日志', 'childhealth:exam-reminder-log:query', 2, 7, 5240, 'reminder-log', 'ep:list', 'childhealth/exam/examReminderLog/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5248, '运营指标快照', 'childhealth:ops-indicator-snapshot:query', 2, 8, 5240, 'snapshot', 'ep:data-line', 'childhealth/message/messagePushConfig/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5249, '工作量统计', 'childhealth:staff-workload-statistics:query', 2, 9, 5240, 'staff-workload', 'ep:histogram', 'childhealth/report/workload', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- ============================================================
-- 10. 儿童档案 (5260-5269)
-- ============================================================
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES
(5260, '儿童档案', '', 1, 10, 5100, 'child', 'ep:user-filled', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5261, '儿童基本信息', 'childhealth:child-base-info:query', 2, 1, 5260, 'base-info', 'ep:user', 'childhealth/child/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5262, '监护人信息', 'childhealth:guardian-info:query', 2, 2, 5260, 'guardian-info', 'ep:user-filled', 'childhealth/guardian/guardianInfo/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- ============================================================
-- 11. 医疗记录 (5270-5279)
-- 含: 病历/病历模板/病历字段/LIS-PACS/外部报告归档
-- ============================================================
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES
(5270, '医疗记录', '', 1, 11, 5100, 'medical', 'ep:document', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5271, '病历管理', 'childhealth:medical-record:query', 2, 1, 5270, 'record', 'ep:document', 'childhealth/medical/medicalRecord/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5272, '病历模板', 'childhealth:medical-record-template:query', 2, 2, 5270, 'template', 'ep:edit-pen', 'childhealth/medical/medicalRecordTemplate/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5273, '病历字段', 'childhealth:medical-record-item:query', 2, 3, 5270, 'record-item', 'ep:files', 'childhealth/medical/medicalRecord/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5274, 'LIS/PACS报告', 'childhealth:lis-pacs-report:query', 2, 4, 5270, 'lis-pacs', 'ep:link', 'childhealth/external/externalReport/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5275, '外部报告归档', 'childhealth:external-report-archive:query', 2, 5, 5270, 'external-report', 'ep:folder-opened', 'childhealth/external/externalReport/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- ============================================================
# ============================================================
# 13. Data Push (5290-5299)
# ============================================================
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES
(5290, '鏁版嵁涓婃姤', '', 1, 13, 5100, 'data-push', 'ep:upload', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5291, '涓婃姤浠诲姟', 'childhealth:data-push-task:query', 2, 1, 5290, 'task', 'ep:document', 'childhealth/integration/dataPushTask/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5292, '澶辫触鏃ュ織', 'childhealth:data-push-fail-log:query', 2, 2, 5290, 'fail-log', 'ep:warning', 'childhealth/integration/dataPushFailLog/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');


-- 12. 设备集成 (5280-5289)
-- ============================================================
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES
(5280, '设备集成', '', 1, 12, 5100, 'device', 'ep:cpu', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5281, '设备数据采集', 'childhealth:device:collect', 2, 1, 5280, 'collect', 'ep:download', 'childhealth/device/deviceIntegrationLog/index', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- ============================================================
-- 校验 SQL（执行后用于人工核对菜单条数）
-- ============================================================
-- 预期顶级目录数: 1 (id=5100)
-- 预期一级菜单数: 12 (5101/5110/5150/5170/5190/5200/5210/5220/5240/5260/5270/5280)
-- 预期总菜单数: 60+
-- SELECT COUNT(*) FROM `system_menu` WHERE `id` BETWEEN 5100 AND 5299;
-- SELECT id, name, permission, type, parent_id, path FROM `system_menu` WHERE `id` BETWEEN 5100 AND 5299 ORDER BY parent_id, sort;

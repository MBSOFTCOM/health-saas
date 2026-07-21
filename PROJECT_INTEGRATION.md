# 固原市妇幼保健院0-3岁儿童健康管理系统

## 1. 项目概述

### 1.1 项目名称
固原市妇幼保健院0-3岁儿童健康管理系统

### 1.2 系统定位
本系统是一套面向妇幼保健机构的综合性儿童健康管理平台，专注于0-3岁儿童的健康档案管理、健康筛查、体检业务、高危儿管理、量表评估、报表统计等核心业务。系统通过整合医疗设备数据、实现智能化筛查和随访管理，为儿童提供全周期的健康保障服务。

### 1.3 技术架构

#### 后端架构
- **框架基础**: Spring Boot 3.2.2 + MyBatis-Plus
- **Java版本**: Java 17
- **构建工具**: Maven
- **数据库**: MySQL（支持多数据源）
- **缓存**: Redis
- **消息队列**: 支持 RocketMQ、Kafka、RabbitMQ
- **工作流引擎**: Flowable 6.x
- **接口文档**: SpringDoc + Knife4j
- **报表服务**: 积木报表（Jeecg JimuReport）

#### 前端架构
- **管理后台**: Vue 3 + TypeScript + Element Plus
- **移动端**: uni-app（支持多端发布）
- **构建工具**: Vite

#### 核心模块结构
```
chinderhealth/
├── yudao-dependencies/          # 依赖管理
├── yudao-framework/             # 框架核心模块
│   ├── yudao-common/            # 公共组件
│   ├── yudao-spring-boot-starter-web/      # Web模块
│   ├── yudao-spring-boot-starter-mybatis/  # 数据库模块
│   ├── yudao-spring-boot-starter-redis/    # Redis模块
│   ├── yudao-spring-boot-starter-security/ # 安全模块
│   └── ...其他starter模块
├── yudao-module-system/         # 系统管理模块
├── yudao-module-infra/          # 基础设施模块
├── yudao-module-childhealth/    # 儿童健康业务模块（核心）
├── flow-screen-ppd/             # 结核病筛查模块（PPD）
└── yudao-server/                # 启动服务模块
```

---

## 2. 已实现的模块清单

### 2.1 儿童基础健康检查模块

**功能描述**:
- 儿童基本信息档案管理（档案建立、更新、转出、死亡登记）
- 监护人信息管理
- 儿童生长曲线追踪
- 基础健康数据维护

**核心实体**:
- `ChildInfoDO` - 儿童基本信息
- `GuardianInfoDO` - 监护人信息
- `GrowthCurveDataDO` - 生长曲线数据

### 2.2 高危儿和疾病专案管理模块

**功能描述**:
- 高危儿识别与专案建立
- 疾病知识库管理
- 阳性规则配置
- 专案卡片管理
- 转诊与转档管理
- 病例康复评估

**核心实体**:
- `HighRiskNewbornDO` - 高危新生儿信息
- `CaseCardDO` - 专案卡片
- `CaseRegistrationDO` - 病例登记
- `CaseRecoveryAssessmentDO` - 康复评估
- `ReferralRecordDO` - 转诊记录
- `TransferArchiveDO` - 转档记录
- `DiseaseKnowledgeDO` - 疾病知识库
- `PositiveRuleDO` - 阳性判定规则

### 2.3 五健筛查核心业务模块

**功能描述**:
- 筛查批次管理（批次创建、状态跟踪、统计汇总）
- 筛查记录管理
- 筛查项目配置
- 筛查阳性管理
- 复查记录管理
- 异常自动识别与预警

**核心实体**:
- `ScreeningBatchDO` - 筛查批次
- `ScreeningRecordDO` - 筛查记录
- `ScreeningItemConfigDO` - 筛查项目配置
- `ScreeningPositiveDO` - 筛查阳性记录
- `RecheckRecordDO` - 复查记录
- `ScreeningResultDetailDO` - 筛查结果明细

### 2.4 体检业务模块

**功能描述**:
- 体检预约管理
- 体检记录管理
- 体格检查（身高、体重、头围、胸围等）
- 眼保健检查
- 听力检查
- 口腔检查
- 辅助检查报告管理（LIS/PACS集成）
- 病历模板管理
- 体检审核流程

**核心实体**:
- `ExamAppointmentDO` - 体检预约
- `ExamRecordDO` - 体检记录
- `PhysicalExamRecordDO` - 体格检查记录
- `EyeExamRecordDO` - 眼保健检查记录
- `HearingExamRecordDO` - 听力检查记录
- `OralExamRecordDO` - 口腔检查记录
- `LabReportDO` - 辅助检查报告
- `MedicalRecordTemplateDO` - 病历模板

### 2.5 随访与患者运营管理模块

**功能描述**:
- 随访计划管理
- 随访任务分配
- 随访记录管理
- 消息推送管理
- 健康文章推送
- 问卷配置与调查
- 提醒规则配置

**核心实体**:
- `FollowPlanDO` - 随访计划
- `FollowTaskDO` - 随访任务
- `FollowUpRecordDO` - 随访记录
- `MessagePushLogDO` - 消息推送日志
- `HealthArticleDO` - 健康文章
- `QuestionnaireConfigDO` - 问卷配置
- `QuestionnaireAnswerDO` - 问卷答案
- `ReminderRuleDO` - 提醒规则
- `ExamReminderLogDO` - 体检提醒日志

### 2.6 量表评估模块

**功能描述**:
- 心理发育量表评估
- 量表题库管理
- 评估记录管理
- 评估报告生成

**核心实体**:
- `ScaleQuestionDO` - 量表题目
- `ScaleAssessmentRecordDO` - 量表评估记录
- `AssessmentReportDO` - 评估报告

### 2.7 报表服务模块

**功能描述**:
- 筛查汇总统计
- 阳性统计报表
- 复查统计报表
- 随访统计报表
- 工作量统计
- 区域报表
- 学生个人报告
- 学校汇总报告
- 年级报告
- 检查批次报表

**核心功能**:
- 报表导出（Excel、PDF）
- 数据可视化分析
- 自定义报表模板

### 2.8 工作流引擎模块

**功能描述**:
- 儿童健康工作流管理
- 筛查阳性规则服务
- 案例预警日志
- 外部报告管理

**核心实体**:
- `CaseAlertLogDO` - 案例预警日志
- `ExternalReportDO` - 外部报告

### 2.9 异常规则管理模块

**功能描述**:
- 异常判定规则配置
- 异常检测日志

**核心实体**:
- `AbnormalRuleDO` - 异常规则
- `AbnormalDetectLogDO` - 异常检测日志

### 2.10 设备集成模块

**功能描述**:
- 医疗设备数据采集接口
- 设备集成日志管理
- LIS/PACS系统集成

**核心实体**:
- `DeviceIntegrationLogDO` - 设备集成日志

### 2.11 结核病筛查模块（PPD）

**功能描述**:
- PPD筛查管理
- 痰液检查管理
- 胸部CT/X光检查
- 知情同意书管理
- 试剂管理
- 消耗品管理
- 区域管理
- 同步数据管理
- 统计历史数据

**核心功能**:
- 筛查人员管理
- 筛查点管理
- 筛查汇总统计
- PDF报告生成
- MinIO文件存储

---

## 3. API 接口清单

### 3.1 体检业务接口（/childhealth/exam）

| 接口路径 | 方法 | 功能说明 | 权限标识 |
|---------|------|---------|---------|
| /appointment/create | POST | 创建体检预约 | childhealth:exam:create |
| /appointment/update-status | PUT | 更新预约状态 | childhealth:exam:update |
| /appointment/page | GET | 获取预约分页列表 | childhealth:exam:query |
| /appointment/get | GET | 获取预约详情 | childhealth:exam:query |
| /appointment/cancel | PUT | 取消预约 | childhealth:exam:update |
| /record/create | POST | 创建体检记录 | childhealth:exam:create |
| /record/page | GET | 获取体检记录分页 | childhealth:exam:query |
| /record/get | GET | 获取体检记录详情 | childhealth:exam:query |
| /record/submit | PUT | 提交体检记录审核 | childhealth:exam:update |
| /record/review | PUT | 审核体检记录 | childhealth:exam:update |
| /physical/create | POST | 录入体格检查数据 | childhealth:exam:create |
| /physical/update | PUT | 更新体格检查数据 | childhealth:exam:update |
| /eye/create | POST | 录入眼保健检查数据 | childhealth:exam:create |
| /hearing/create | POST | 录入听力检查数据 | childhealth:exam:create |
| /oral/create | POST | 录入口腔检查数据 | childhealth:exam:create |
| /lab-report/create | POST | 录入辅助检查报告 | childhealth:exam:create |
| /lab-report/list | GET | 获取辅助检查报告列表 | childhealth:exam:query |
| /template/get-applicable | GET | 获取适用病历模板 | childhealth:exam:query |
| /template/page | GET | 获取病历模板分页 | childhealth:exam:query |
| /template/get | GET | 获取病历模板详情 | childhealth:exam:query |

### 3.2 筛查批次接口（/childhealth/screening-batch）

| 接口路径 | 方法 | 功能说明 | 权限标识 |
|---------|------|---------|---------|
| /create | POST | 创建筛查批次 | childhealth:screening-batch:create |
| /update | PUT | 更新筛查批次 | childhealth:screening-batch:update |
| /delete | DELETE | 删除筛查批次 | childhealth:screening-batch:delete |
| /get | GET | 获取筛查批次详情 | childhealth:screening-batch:query |
| /page | GET | 获取筛查批次分页 | childhealth:screening-batch:query |
| /list | GET | 获取筛查批次列表 | childhealth:screening-batch:query |
| /update-status | PUT | 更新批次状态 | childhealth:screening-batch:update |

### 3.3 筛查记录接口（/childhealth/screening-record）

| 接口路径 | 方法 | 功能说明 |
|---------|------|---------|
| /create | POST | 创建筛查记录 |
| /update | PUT | 更新筛查记录 |
| /delete | DELETE | 删除筛查记录 |
| /get | GET | 获取筛查记录详情 |
| /page | GET | 获取筛查记录分页 |
| /list | GET | 获取筛查记录列表 |

### 3.4 筛查阳性接口（/childhealth/screening-positive）

| 接口路径 | 方法 | 功能说明 |
|---------|------|---------|
| /create | POST | 创建阳性记录 |
| /update | PUT | 更新阳性记录 |
| /delete | DELETE | 删除阳性记录 |
| /get | GET | 获取阳性记录详情 |
| /page | GET | 获取阳性记录分页 |

### 3.5 复查记录接口（/childhealth/recheck-record）

| 接口路径 | 方法 | 功能说明 |
|---------|------|---------|
| /create | POST | 创建复查记录 |
| /update | PUT | 更新复查记录 |
| /delete | DELETE | 删除复查记录 |
| /get | GET | 获取复查记录详情 |
| /page | GET | 获取复查记录分页 |

### 3.6 阳性规则接口（/childhealth/positive-rule）

| 接口路径 | 方法 | 功能说明 |
|---------|------|---------|
| /create | POST | 创建阳性规则 |
| /update | PUT | 更新阳性规则 |
| /delete | DELETE | 删除阳性规则 |
| /get | GET | 获取阳性规则详情 |
| /page | GET | 获取阳性规则分页 |
| /list | GET | 获取阳性规则列表 |

### 3.7 筛查项目配置接口（/childhealth/screening-item-config）

| 接口路径 | 方法 | 功能说明 |
|---------|------|---------|
| /create | POST | 创建筛查项目配置 |
| /update | PUT | 更新筛查项目配置 |
| /delete | DELETE | 删除筛查项目配置 |
| /get | GET | 获取配置详情 |
| /page | GET | 获取配置分页 |
| /list | GET | 获取配置列表 |

### 3.8 疾病知识库接口（/childhealth/disease-knowledge）

| 接口路径 | 方法 | 功能说明 |
|---------|------|---------|
| /create | POST | 创建疾病知识 |
| /update | PUT | 更新疾病知识 |
| /delete | DELETE | 删除疾病知识 |
| /get | GET | 获取疾病知识详情 |
| /page | GET | 获取疾病知识分页 |
| /list | GET | 获取疾病知识列表 |

### 3.9 报表服务接口

#### 儿童健康报表接口（/childhealth/report）

| 接口路径 | 方法 | 功能说明 |
|---------|------|---------|
| /screening-summary | POST | 筛查汇总统计 |
| /positive-statistics | POST | 阳性统计报表 |
| /recheck-statistics | POST | 复查统计报表 |
| /followup-statistics | POST | 随访统计报表 |
| /workload-statistics | POST | 工作量统计报表 |
| /region-report | GET | 区域报表 |
| /student-personal-report | GET | 学生个人报告 |
| /school-summary-report | GET | 学校汇总报告 |
| /grade-report | GET | 年级报告 |
| /checkup-batch-report | GET | 检查批次报表 |
| /export | POST | 导出报表 |

#### 数据分析接口（/childhealth/analytics）

| 接口路径 | 方法 | 功能说明 |
|---------|------|---------|
| /overview | GET | 数据概览 |
| /trend | GET | 趋势分析 |
| /distribution | GET | 分布分析 |

### 3.10 随访管理接口（/childhealth/followup）

| 接口路径 | 方法 | 功能说明 |
|---------|------|---------|
| /create | POST | 创建随访记录 |
| /update | PUT | 更新随访记录 |
| /complete | PUT | 完成随访 |
| /get | GET | 获取随访详情 |
| /page | GET | 获取随访分页 |

### 3.11 量表评估接口（/childhealth/scale）

| 接口路径 | 方法 | 功能说明 |
|---------|------|---------|
| /create | POST | 创建量表评估 |
| /submit | PUT | 提交评估答案 |
| /calculate | POST | 计算评估结果 |
| /get | GET | 获取评估详情 |
| /page | GET | 获取评估分页 |

### 3.12 异常规则接口（/childhealth/rule）

| 接口路径 | 方法 | 功能说明 |
|---------|------|---------|
| /create | POST | 创建异常规则 |
| /update | PUT | 更新异常规则 |
| /delete | DELETE | 删除异常规则 |
| /get | GET | 获取异常规则详情 |
| /page | GET | 获取异常规则分页 |

### 3.13 设备集成接口（/childhealth/device）

| 接口路径 | 方法 | 功能说明 |
|---------|------|---------|
| /collect | POST | 设备数据采集 |
| /sync | POST | 数据同步 |

### 3.14 儿童健康管理接口（/childhealth/management）

| 接口路径 | 方法 | 功能说明 |
|---------|------|---------|
| /child-info/create | POST | 创建儿童信息 |
| /child-info/update | PUT | 更新儿童信息 |
| /child-info/get | GET | 获取儿童信息详情 |
| /child-info/page | GET | 获取儿童信息分页 |
| /guardian/create | POST | 创建监护人信息 |
| /guardian/update | PUT | 更新监护人信息 |

### 3.15 工作流接口（/childhealth/workflow）

| 接口路径 | 方法 | 功能说明 |
|---------|------|---------|
| /start | POST | 启动工作流 |
| /approve | PUT | 审批流程 |
| /query | GET | 查询流程状态 |

### 3.16 运营服务接口（/childhealth/ops）

| 接口路径 | 方法 | 功能说明 |
|---------|------|---------|
| /message/push | POST | 推送消息 |
| /article/publish | POST | 发布健康文章 |
| /questionnaire/create | POST | 创建问卷 |
| /questionnaire/answer | POST | 提交问卷答案 |

### 3.17 结核病筛查接口（PPD模块）

| 接口路径 | 方法 | 功能说明 |
|---------|------|---------|
| /ppd/create | POST | 创建PPD筛查 |
| /ppd/page | GET | 获取PPD筛查分页 |
| /sputum/create | POST | 创建痰液检查 |
| /ct/create | POST | 创建CT检查 |
| /chest-radiograph/create | POST | 创建胸透检查 |
| /consent-form/create | POST | 创建知情同意书 |
| /reagent/page | GET | 获取试剂分页 |
| /consume/page | GET | 获取消耗品分页 |
| /district/list | GET | 获取区域列表 |
| /sync/data | POST | 同步数据 |
| /report/generate | POST | 生成报告 |
| /pdf/export | GET | 导出PDF |
| /minio/upload | POST | 上传文件 |

---

## 4. 数据库结构

### 4.1 核心数据表说明

#### 儿童基础信息表

| 表名 | 说明 | 主要字段 |
|------|------|---------|
| child_base_info | 儿童基本信息表 | id, child_code, name, gender, birth_date, id_card, birth_weight, birth_height, gestational_age, is_premature, is_high_risk, high_risk_tags, qr_code, status |
| guardian_info | 监护人信息表 | id, child_id, guardian_name, relationship, phone, address |
| growth_curve_data | 生长曲线数据表 | id, child_id, measure_date, height, weight, head_circumference |

#### 筛查业务表

| 表名 | 说明 | 主要字段 |
|------|------|---------|
| screening_batch | 筛查批次表 | id, batch_no, batch_name, year_id, school_id, start_date, end_date, target_count, actual_count, batch_status |
| screening_record | 筛查记录表 | id, batch_id, child_id, screening_date, screening_type, result, status |
| screening_item_config | 筛查项目配置表 | id, item_code, item_name, screening_type, age_range, is_enabled |
| screening_positive | 筛查阳性表 | id, record_id, child_id, positive_type, positive_level, status |
| recheck_record | 复查记录表 | id, positive_id, recheck_date, recheck_result, status |
| screening_result_detail | 筛查结果明细表 | id, record_id, item_id, result_value, is_abnormal |

#### 体检业务表

| 表名 | 说明 | 主要字段 |
|------|------|---------|
| exam_appointment | 体检预约表 | id, child_id, appointment_date, appointment_time, status |
| exam_record | 体检记录表 | id, child_id, exam_date, exam_type, status, reviewer_id, review_time |
| physical_exam_record | 体格检查记录表 | id, exam_id, height, weight, head_circumference, chest_circumference, bmi |
| eye_exam_record | 眼保健检查记录表 | id, exam_id, left_vision, right_vision, is_abnormal |
| hearing_exam_record | 听力检查记录表 | id, exam_id, left_hearing, right_hearing, is_abnormal |
| oral_exam_record | 口腔检查记录表 | id, exam_id, teeth_number, dental_caries, is_abnormal |
| lab_report | 辅助检查报告表 | id, exam_id, report_type, report_data, source |
| medical_record_template | 病历模板表 | id, template_name, template_type, month_age_range, content |

#### 专案管理表

| 表名 | 说明 | 主要字段 |
|------|------|---------|
| high_risk_newborn | 高危新生儿表 | id, child_id, risk_factors, intervention_plan, status |
| case_card | 专案卡片表 | id, child_id, case_type, create_date, status |
| case_registration | 病例登记表 | id, card_id, diagnosis, treatment_plan |
| case_recovery_assessment | 康复评估表 | id, case_id, assessment_date, assessment_result |
| referral_record | 转诊记录表 | id, case_id, referral_hospital, referral_date, reason |
| transfer_archive | 转档记录表 | id, child_id, from_org, to_org, transfer_date |

#### 随访运营表

| 表名 | 说明 | 主要字段 |
|------|------|---------|
| follow_plan | 随访计划表 | id, plan_name, target_group, start_date, end_date |
| follow_task | 随访任务表 | id, plan_id, executor_id, task_date, status |
| follow_up_record | 随访记录表 | id, task_id, child_id, follow_date, follow_result |
| message_push_log | 消息推送日志表 | id, child_id, message_type, push_time, status |
| health_article | 健康文章表 | id, title, content, publish_date, author |
| questionnaire_config | 问卷配置表 | id, questionnaire_name, questions, is_enabled |
| questionnaire_answer | 问卷答案表 | id, questionnaire_id, child_id, answers, submit_time |
| reminder_rule | 提醒规则表 | id, rule_name, rule_type, trigger_condition |

#### 量表评估表

| 表名 | 说明 | 主要字段 |
|------|------|---------|
| scale_question | 量表题目表 | id, scale_type, question_content, options, score |
| scale_assessment_record | 量表评估记录表 | id, child_id, scale_type, assessment_date, total_score |
| assessment_report | 评估报告表 | id, assessment_id, report_content, create_time |

#### 配置规则表

| 表名 | 说明 | 主要字段 |
|------|------|---------|
| positive_rule | 阳性规则表 | id, rule_name, screening_type, conditions, priority |
| disease_knowledge | 疾病知识库表 | id, disease_name, description, symptoms, treatment |
| abnormal_rule | 异常规则表 | id, rule_name, exam_type, conditions, alert_level |
| case_type_config | 案例类型配置表 | id, case_type, case_name, description |

#### 系统管理表

| 表名 | 说明 | 主要字段 |
|------|------|---------|
| system_user | 系统用户表 | id, username, password, nickname, status |
| system_role | 系统角色表 | id, role_name, role_code, status |
| system_menu | 系统菜单表 | id, menu_name, parent_id, path, component |
| system_dept | 部门表 | id, dept_name, parent_id, leader |
| system_dict_type | 字典类型表 | id, dict_type, dict_name |
| system_dict_data | 字典数据表 | id, dict_type, dict_label, dict_value |

---

## 5. 部署说明

### 5.1 环境要求

#### 硬件要求
- CPU: 4核及以上
- 内存: 8GB及以上
- 磁盘: 50GB及以上

#### 软件要求
- 操作系统: Linux（CentOS 7+）/Windows Server
- JDK: 17+
- MySQL: 8.0+
- Redis: 6.0+
- Maven: 3.6+
- Node.js: 16+（前端构建）
- Nginx: 1.18+（可选，用于前端部署）

### 5.2 数据库初始化

```bash
# 1. 创建数据库
CREATE DATABASE childhealth DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 2. 创建用户并授权
CREATE USER 'childhealth'@'%' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON childhealth.* TO 'childhealth'@'%';
FLUSH PRIVILEGES;

# 3. 导入基础表结构（通过项目SQL文件或Flowable自动创建）
# 项目使用Flowable自动创建表，首次启动会自动初始化
```

### 5.3 项目构建

#### 后端构建
```bash
# 进入项目根目录
cd chinderhealth

# Maven构建（跳过测试）
mvn clean package -DskipTests

# 构建产物位于 yudao-server/target/yudao-server.jar
```

#### 前端构建
```bash
# 管理后台
cd yudao-ui/yudao-ui-admin-vue3
npm install
npm run build

# 移动端
cd yudao-ui/yudao-ui-admin-uniapp
npm install
# 使用HBuilderX或uni-app CLI构建
```

### 5.4 配置文件说明

#### application.yaml（主配置）
```yaml
spring:
  application:
    name: yudao-server
  profiles:
    active: local  # 环境标识
  servlet:
    multipart:
      max-file-size: 16MB
      max-request-size: 32MB

# MyBatis-Plus配置
mybatis-plus:
  global-config:
    db-config:
      id-type: AUTO  # 主键策略
      logic-delete-value: 1
      logic-not-delete-value: 0

# Flowable工作流配置
flowable:
  database-schema-update: true
  history-level: audit
```

#### application-local.yaml（本地环境）
```yaml
spring:
  datasource:
    dynamic:
      datasource:
        master:
          url: jdbc:mysql://localhost:3306/childhealth
          username: childhealth
          password: your_password
  data:
    redis:
      host: localhost
      port: 6379
      database: 0
```

### 5.5 启动步骤

#### 后端启动
```bash
# 开发环境启动
java -jar yudao-server/target/yudao-server.jar --spring.profiles.active=local

# 生产环境启动
java -jar yudao-server.jar \
  --spring.profiles.active=prod \
  --server.port=8080 \
  -Xms512m -Xmx1024m
```

#### 访问地址
- 后端接口: http://localhost:8080
- 接口文档: http://localhost:8080/swagger-ui.html
- Knife4j文档: http://localhost:8080/doc.html
- 管理后台: http://localhost:80 (需部署前端)

---

## 6. 开发指南

### 6.1 项目结构说明

#### 后端项目结构
```
yudao-module-childhealth/
├── yudao-module-childhealth-api/       # API接口定义层
│   ├── device/                         # 设备集成接口
│   ├── followup/                       # 随访接口
│   ├── management/                     # 管理接口
│   ├── message/                        # 消息接口
│   ├── ops/                            # 运营接口
│   ├── report/                         # 报表接口
│   ├── rule/                           # 规则接口
│   ├── scale/                          # 量表接口
│   └── workflow/                       # 工作流接口
└── yudao-module-childhealth-biz/       # 业务实现层
    └── src/main/java/cn/iocoder/yudao/module/childhealth/
        ├── controller/                 # 控制器层
        │   └── admin/                  # 管理后台接口
        │       ├── childbase/          # 儿童基础信息
        │       ├── crud/               # 通用CRUD
        │       ├── device/             # 设备集成
        │       ├── exam/               # 体检业务
        │       ├── followup/           # 随访管理
        │       ├── management/         # 儿童健康管理
        │       ├── ops/                # 运营服务
        │       ├── report/             # 报表服务
        │       ├── rule/               # 异常规则
        │       ├── scale/              # 量表评估
        │       ├── screening/          # 筛查业务
        │       ├── template/           # 模板管理
        │       └── workflow/           # 工作流
        ├── service/                    # 服务层
        ├── dal/                        # 数据访问层
        │   ├── dataobject/             # 数据对象（DO）
        │   └── mysql/                  # Mapper接口
        └── convert/                    # 对象转换器
```

#### 前端项目结构（Vue3）
```
yudao-ui-admin-vue3/
├── src/
│   ├── api/                           # API接口
│   │   ├── tb/                        # 儿童健康相关API
│   │   │   ├── report/                # 报表API
│   │   │   └── minio/                 # 文件存储API
│   │   └── system/                    # 系统API
│   ├── views/                         # 页面组件
│   ├── components/                    # 公共组件
│   ├── store/                         # 状态管理
│   ├── router/                        # 路由配置
│   └── utils/                         # 工具函数
└── vite.config.ts                     # Vite配置
```

### 6.2 代码规范

#### 命名规范
- **类名**: 大驼峰命名法（UpperCamelCase），如 `ScreeningBatchService`
- **方法名**: 小驼峰命名法（lowerCamelCase），如 `createScreeningBatch`
- **常量名**: 全大写下划线分隔，如 `MAX_PAGE_SIZE`
- **包名**: 全小写，如 `cn.iocoder.yudao.module.childhealth`

#### 注释规范
- **类注释**: 使用JavaDoc格式，说明类的功能和作者
- **方法注释**: 说明方法功能、参数含义、返回值说明
- **字段注释**: 使用JavaDoc格式注释字段含义

#### 代码结构规范
```java
/**
 * 筛查批次 Controller
 *
 * @author 芋道源码
 */
@Tag(name = "管理后台 - 筛查批次")
@RestController
@RequestMapping("/childhealth/screening-batch")
@Validated
public class ScreeningBatchController {

    @Resource
    private ScreeningBatchService screeningBatchService;

    @PostMapping("/create")
    @Operation(summary = "创建筛查批次")
    @PreAuthorize("@ss.hasPermission('childhealth:screening-batch:create')")
    public CommonResult<Long> createScreeningBatch(@Valid @RequestBody ScreeningBatchSaveReqVO createReqVO) {
        return success(screeningBatchService.createScreeningBatch(createReqVO));
    }
}
```

#### 异常处理规范
- 使用统一异常处理器处理业务异常
- 业务异常使用 `ServiceException` 抛出
- 参数校验使用 `@Valid` 注解 + `BindingResult`

### 6.3 扩展开发指引

#### 新增业务模块步骤

1. **创建数据对象（DO）**
```java
@Data
@TableName("new_business_table")
public class NewBusinessDO extends BaseDO {
    @TableId
    private Long id;
    private String name;
    // ... 其他字段
}
```

2. **创建Mapper接口**
```java
@Mapper
public interface NewBusinessMapper extends BaseMapperX<NewBusinessDO> {
    // 自定义查询方法
}
```

3. **创建Service接口和实现**
```java
public interface NewBusinessService {
    Long createNewBusiness(NewBusinessSaveReqVO createReqVO);
    // ... 其他方法
}

@Service
public class NewBusinessServiceImpl implements NewBusinessService {
    @Resource
    private NewBusinessMapper newBusinessMapper;

    @Override
    public Long createNewBusiness(NewBusinessSaveReqVO createReqVO) {
        // 业务逻辑实现
    }
}
```

4. **创建Controller**
```java
@Tag(name = "管理后台 - 新业务")
@RestController
@RequestMapping("/childhealth/new-business")
@Validated
public class NewBusinessController {
    @Resource
    private NewBusinessService newBusinessService;

    @PostMapping("/create")
    @Operation(summary = "创建新业务")
    @PreAuthorize("@ss.hasPermission('childhealth:new-business:create')")
    public CommonResult<Long> createNewBusiness(@Valid @RequestBody NewBusinessSaveReqVO createReqVO) {
        return success(newBusinessService.createNewBusiness(createReqVO));
    }
}
```

#### 新增筛查类型步骤

1. 在 `screening_item_config` 表中添加新的筛查项目配置
2. 在 `positive_rule` 表中配置阳性判定规则
3. 在 `disease_knowledge` 表中添加疾病知识库
4. 在前端添加对应的筛查录入页面和查询页面

#### 集成新设备步骤

1. 实现 `DeviceIntegrationService` 接口
2. 创建设备数据采集接口 `DeviceCollectRequest`
3. 在 `DeviceIntegrationController` 中添加采集接口
4. 配置设备数据解析规则和存储规则

---

## 7. 注意事项

### 7.1 关键技术要点

#### 多租户支持
- 系统内置多租户支持（通过 `@TenantIgnore` 注解可忽略）
- 租户隔离主要通过 `tenant_id` 字段实现
- 需在租户忽略表配置中添加系统表

#### 数据权限
- 使用 `@DataPermission` 注解实现数据权限控制
- 支持部门数据权限、仅本人数据权限等

#### 工作流集成
- 使用 Flowable 6.x 作为工作流引擎
- 流程定义文件位于 `resources/processes/`
- 支持流程自动部署（可配置关闭）

#### 异常自动识别
- 系统内置异常规则引擎
- 支持基于阈值的异常判定
- 异常日志自动记录到 `abnormal_detect_log` 表

#### 报表服务
- 使用积木报表（Jeecg JimuReport）作为报表引擎
- 支持自定义报表模板
- 支持多数据源报表

### 7.2 性能优化建议

#### 数据库优化
- 合理使用索引（筛查批次、筛查记录、儿童信息等高频查询表）
- 分页查询使用 `PageResult` 避免全表扫描
- 大表考虑分表或归档（如筛查记录表）
- 定期清理历史日志表数据

#### 缓存优化
- 使用 Redis 缓存热点数据（如筛查项目配置、阳性规则）
- 合理设置缓存过期时间
- 使用 `@Cacheable` 注解简化缓存操作

#### 接口优化
- 批量操作使用批量接口减少网络开销
- 合理使用异步处理（如报表生成、数据导出）
- 使用消息队列处理耗时操作（如随访提醒）

#### 前端优化
- 列表页使用虚拟滚动处理大数据量
- 图片懒加载
- 组件按需加载

### 7.3 安全考虑

#### 接口安全
- 所有接口需通过 `@PreAuthorize` 进行权限校验
- 敏感操作需记录操作日志（`@OperateLog`）
- 使用 HTTPS 加密传输

#### 数据安全
- 敏感字段加密存储（如身份证号、手机号）
- 使用 MyBatis-Plus 的加密功能
- 数据导出需进行脱敏处理

#### SQL注入防护
- 使用 MyBatis-Plus 的参数化查询
- 避免拼接 SQL 语句
- 对用户输入进行过滤和校验

#### 权限控制
- 基于角色的权限控制（RBAC）
- 数据权限隔离（部门数据、仅本人数据）
- 接口权限精细化控制

---

## 附录

### A. 技术栈清单

| 类别 | 技术 | 版本 |
|------|------|------|
| 核心框架 | Spring Boot | 3.2.2 |
| 持久层框架 | MyBatis-Plus | 3.5.x |
| 数据库 | MySQL | 8.0+ |
| 缓存 | Redis | 6.0+ |
| 工作流引擎 | Flowable | 6.x |
| 前端框架 | Vue | 3.x |
| 前端UI库 | Element Plus | 2.x |
| 移动端框架 | uni-app | 3.x |
| 接口文档 | Knife4j | 4.x |
| 报表引擎 | JimuReport | 1.6.x |

### B. 参考文档

- [芋道源码文档](https://doc.iocoder.cn/)
- [MyBatis-Plus官方文档](https://baomidou.com/)
- [Flowable官方文档](https://www.flowable.com/)
- [uni-app官方文档](https://uniapp.dcloud.io/)
- [Element Plus官方文档](https://element-plus.org/)

### C. 更新日志

| 版本 | 日期 | 更新内容 |
|------|------|---------|
| 1.0.0 | 2024-01-01 | 初始版本，完成核心业务模块开发 |
| 1.1.0 | 2024-03-01 | 新增量表评估模块、报表服务优化 |
| 1.2.0 | 2024-06-01 | 新增结核病筛查模块（PPD）、设备集成接口 |

---

**文档维护人员**: 系统开发团队  
**最后更新时间**: 2024年6月  
**文档版本**: v1.0
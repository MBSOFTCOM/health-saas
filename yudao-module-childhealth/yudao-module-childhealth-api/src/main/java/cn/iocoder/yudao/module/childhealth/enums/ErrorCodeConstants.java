package cn.iocoder.yudao.module.childhealth.enums;


import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * 儿童健康模块错误码枚举类
 */
public interface ErrorCodeConstants {

    // ========== 模块基础错误码 100100000-100199999 ==========
    ErrorCode CHILDHEALTH_CONFIG_NOT_EXISTS = new ErrorCode(100100000, "儿童健康配置不存在");
    
    // ========== 儿童基本信息 100101000-100101999 ==========
    ErrorCode CHILD_INFO_NOT_EXISTS = new ErrorCode(100101000, "儿童基本信息不存在");
    ErrorCode CHILD_INFO_ID_CARD_DUPLICATE = new ErrorCode(100101001, "儿童身份证号已存在");
    ErrorCode CHILD_INFO_CODE_DUPLICATE = new ErrorCode(100101002, "儿童编码已存在");

    // ========== 监护人信息 100101500-100101999 ==========
    ErrorCode GUARDIAN_INFO_NOT_EXISTS = new ErrorCode(100101500, "监护人信息不存在");
    ErrorCode GUARDIAN_MOBILE_DUPLICATE = new ErrorCode(100101501, "监护人电话已被使用");
    
    // ========== 生长发育记录 100102000-100102999 ==========
    ErrorCode GROWTH_RECORD_NOT_EXISTS = new ErrorCode(100102000, "生长发育记录不存在");
    
    // ========== 体检记录 100103000-100103999 ==========
    ErrorCode HEALTH_CHECK_NOT_EXISTS = new ErrorCode(100103000, "体检记录不存在");
    
    // ========== 疫苗接种记录 100104000-100104999 ==========
    ErrorCode VACCINATION_RECORD_NOT_EXISTS = new ErrorCode(100104000, "疫苗接种记录不存在");
    
    // ========== 疾病管理 100105000-100105999 ==========
    ErrorCode DISEASE_MANAGEMENT_NOT_EXISTS = new ErrorCode(100105000, "疾病管理记录不存在");
    
    // ========== 营养指导 100106000-100106999 ==========
    ErrorCode NUTRITION_GUIDE_NOT_EXISTS = new ErrorCode(100106000, "营养指导记录不存在");

    // ========== 筛查批次 100110000-100110999 ==========
    ErrorCode SCREENING_BATCH_NOT_EXISTS = new ErrorCode(100110000, "筛查批次不存在");
    ErrorCode SCREENING_BATCH_NO_DUPLICATE = new ErrorCode(100110001, "筛查批次编号已存在");
    ErrorCode SCREENING_BATCH_STATUS_ERROR = new ErrorCode(100110002, "筛查批次状态不正确");

    // ========== 筛查记录 100111000-100111999 ==========
    ErrorCode SCREENING_RECORD_NOT_EXISTS = new ErrorCode(100111000, "筛查记录不存在");
    ErrorCode SCREENING_RECORD_NO_DUPLICATE = new ErrorCode(100111001, "筛查流水号已存在");
    ErrorCode SCREENING_RECORD_ALREADY_EXISTS = new ErrorCode(100111002, "该学生已存在筛查记录");
    ErrorCode SCREENING_RECORD_NOT_AUDIT = new ErrorCode(100111003, "筛查记录未审核");

    // ========== 筛查明细 100112000-100112999 ==========
    ErrorCode SCREENING_DETAIL_NOT_EXISTS = new ErrorCode(100112000, "筛查明细不存在");
    ErrorCode SCREENING_DETAIL_DUPLICATE = new ErrorCode(100112001, "筛查项目重复录入");

    // ========== 阳性记录 100113000-100113999 ==========
    ErrorCode POSITIVE_RECORD_NOT_EXISTS = new ErrorCode(100113000, "阳性记录不存在");

    // ========== 复筛记录 100114000-100114999 ==========
    ErrorCode RECHECK_RECORD_NOT_EXISTS = new ErrorCode(100114000, "复筛记录不存在");
    ErrorCode RECHECK_RECORD_ALREADY_EXISTS = new ErrorCode(100114001, "该阳性记录已有复筛记录");

    // ========== 疾病知识库 100115000-100115999 ==========
    ErrorCode DISEASE_KNOWLEDGE_NOT_EXISTS = new ErrorCode(100115000, "疾病知识库不存在");
    ErrorCode DISEASE_CODE_DUPLICATE = new ErrorCode(100115001, "疾病编码已存在");

    // ========== 阳性判定规则 100116000-100116999 ==========
    ErrorCode POSITIVE_RULE_NOT_EXISTS = new ErrorCode(100116000, "阳性判定规则不存在");
    ErrorCode POSITIVE_RULE_CODE_DUPLICATE = new ErrorCode(100116001, "阳性判定规则编码已存在");

    // ========== 筛查项目配置 100117000-100117999 ==========
    ErrorCode SCREENING_ITEM_NOT_EXISTS = new ErrorCode(100117000, "筛查项目不存在");
    ErrorCode SCREENING_ITEM_CODE_DUPLICATE = new ErrorCode(100117001, "筛查项目编码已存在");

    // ========== 二维码管理 100118000-100118999 ==========
    ErrorCode QRCODE_NOT_EXISTS = new ErrorCode(100118000, "二维码不存在");
    ErrorCode QRCODE_ALREADY_USED = new ErrorCode(100118001, "二维码已使用");
    ErrorCode QRCODE_ALREADY_PRINTED = new ErrorCode(100118002, "二维码已打印");

    // ========== 转介管理 100119000-100119999 ==========
    ErrorCode REFERRAL_NOT_EXISTS = new ErrorCode(100119000, "转介记录不存在");
    ErrorCode REFERRAL_NO_DUPLICATE = new ErrorCode(100119001, "转介编号已存在");

    // ========== 档案转递 100134000-100134999 ==========
    ErrorCode TRANSFER_ARCHIVE_NOT_EXISTS = new ErrorCode(100134000, "档案转递记录不存在");
    ErrorCode TRANSFER_ARCHIVE_NO_DUPLICATE = new ErrorCode(100134001, "转递编号已存在");

    // ========== 学生档案 100120000-100120999 ==========
    ErrorCode STUDENT_NOT_EXISTS = new ErrorCode(100120000, "学生档案不存在");
    ErrorCode STUDENT_NO_DUPLICATE = new ErrorCode(100120001, "学号已存在");

    // ========== 学校信息 100121000-100121999 ==========
    ErrorCode SCHOOL_NOT_EXISTS = new ErrorCode(100121000, "学校不存在");
    ErrorCode SCHOOL_CODE_DUPLICATE = new ErrorCode(100121001, "学校编码已存在");

    // ========== 班级信息 100122000-100122999 ==========
    ErrorCode CLASS_NOT_EXISTS = new ErrorCode(100122000, "班级不存在");

    // ========== 年级信息 100123000-100123999 ==========
    ErrorCode GRADE_NOT_EXISTS = new ErrorCode(100123000, "年级不存在");

    // ========== 体检预约 100124000-100124999 ==========
    ErrorCode EXAM_APPOINTMENT_NOT_EXISTS = new ErrorCode(100124000, "体检预约不存在");
    ErrorCode EXAM_APPOINTMENT_CHILD_ID_REQUIRED = new ErrorCode(100124001, "儿童ID不能为空");
    ErrorCode EXAM_APPOINTMENT_TYPE_INVALID = new ErrorCode(100124002, "体检类型无效");
    ErrorCode EXAM_APPOINTMENT_DATE_REQUIRED = new ErrorCode(100124003, "预约日期不能为空");
    ErrorCode EXAM_APPOINTMENT_ALREADY_COMPLETED = new ErrorCode(100124004, "预约已完成，无法取消");

    // ========== 体检记录 100125000-100125999 ==========
    ErrorCode EXAM_RECORD_NOT_EXISTS = new ErrorCode(100125000, "体检记录不存在");
    ErrorCode EXAM_RECORD_CHILD_ID_REQUIRED = new ErrorCode(100125001, "儿童ID不能为空");
    ErrorCode EXAM_RECORD_DATE_REQUIRED = new ErrorCode(100125002, "体检日期不能为空");
    ErrorCode EXAM_RECORD_TYPE_INVALID = new ErrorCode(100125003, "体检类型无效");
    ErrorCode EXAM_RECORD_STATUS_INVALID = new ErrorCode(100125004, "体检记录状态无效");
    ErrorCode EXAM_RECORD_ID_REQUIRED = new ErrorCode(100125005, "体检记录ID不能为空");
    ErrorCode EXAM_RECORD_NOT_PENDING_REVIEW = new ErrorCode(100125006, "体检记录未处于待审核状态");

    // ========== 体格检查 100126000-100126999 ==========
    ErrorCode EXAM_PHYSICAL_NOT_EXISTS = new ErrorCode(100126000, "体格检查记录不存在");

    // ========== 眼保健检查 100127000-100127999 ==========
    ErrorCode EXAM_EYE_ALREADY_EXISTS = new ErrorCode(100127000, "该体检记录已有眼保健检查");

    // ========== 听力检查 100128000-100128999 ==========
    ErrorCode EXAM_HEARING_ALREADY_EXISTS = new ErrorCode(100128000, "该体检记录已有听力检查");

    // ========== 口腔检查 100129000-100129999 ==========
    ErrorCode EXAM_ORAL_ALREADY_EXISTS = new ErrorCode(100129000, "该体检记录已有口腔检查");

    // ========== 病历模板 100130000-100130999 ==========
    ErrorCode EXAM_TEMPLATE_NOT_EXISTS = new ErrorCode(100130000, "病历模板不存在");

    // ========== 筛查方案 100131000-100131999 ==========
    ErrorCode SCREENING_PLAN_NOT_EXISTS = new ErrorCode(100131000, "筛查方案不存在");
    ErrorCode SCREENING_PLAN_CODE_DUPLICATE = new ErrorCode(100131001, "筛查方案编码已存在");

    // ========== 筛查结果明细 100132000-100132999 ==========
    ErrorCode SCREENING_RESULT_DETAIL_NOT_EXISTS = new ErrorCode(100132000, "筛查结果明细不存在");
    ErrorCode SCREENING_RESULT_DETAIL_DUPLICATE = new ErrorCode(100132001, "筛查项目重复录入");

    // ========== 筛查统计 100133000-100133999 ==========
    ErrorCode SCREENING_STATISTICS_NOT_EXISTS = new ErrorCode(100133000, "筛查统计不存在");

    // ========== 专案类型配置 100135000-100135999 ==========
    ErrorCode CASE_TYPE_CONFIG_NOT_EXISTS = new ErrorCode(100135000, "专案类型配置不存在");

    // ========== 阳性规则执行日志 100140000-100140999 ==========  (新增 2026-07-20)
    ErrorCode POSITIVE_RULE_EXEC_LOG_NOT_EXISTS = new ErrorCode(100140000, "阳性规则执行日志不存在");

    // ========== 数据上报任务 100141000-100141999 ==========  (新增 2026-07-20)
    ErrorCode DATA_PUSH_TASK_NOT_EXISTS = new ErrorCode(100141000, "数据上报任务不存在");
    ErrorCode DATA_PUSH_TASK_NO_DUPLICATE = new ErrorCode(100141001, "数据上报任务编号已存在");
    ErrorCode DATA_PUSH_TASK_STATUS_ERROR = new ErrorCode(100141002, "数据上报任务状态不允许此操作");
    ErrorCode DATA_PUSH_FAIL_LOG_NOT_EXISTS = new ErrorCode(100141100, "数据上报失败日志不存在");

    // ========== 运营指标快照 100142000-100142999 ==========  (新增 2026-07-20)
    ErrorCode OPS_INDICATOR_SNAPSHOT_NOT_EXISTS = new ErrorCode(100142000, "运营指标快照不存在");

    // ========== 复筛通知记录 100143000-100143999 ==========  (新增 2026-07-20)
    ErrorCode RECHECK_NOTIFY_RECORD_NOT_EXISTS = new ErrorCode(100143000, "复筛通知记录不存在");

    // ========== 复筛随访时间轴 100144000-100144999 ==========  (新增 2026-07-20)
    ErrorCode RECHECK_FOLLOW_TIMELINE_NOT_EXISTS = new ErrorCode(100144000, "复筛随访时间轴记录不存在");

    // ========== 报告模板配置 100145000-100145999 ==========  (新增 2026-07-20)
    ErrorCode REPORT_TEMPLATE_NOT_EXISTS = new ErrorCode(100145000, "报告模板不存在");
    ErrorCode REPORT_TEMPLATE_CODE_DUPLICATE = new ErrorCode(100145001, "报告模板编码已存在");

    // ========== 报告生成任务 100146000-100146999 ==========  (新增 2026-07-20)
    ErrorCode REPORT_GENERATION_TASK_NOT_EXISTS = new ErrorCode(100146000, "报告生成任务不存在");
    ErrorCode REPORT_GENERATION_TASK_NO_DUPLICATE = new ErrorCode(100146001, "报告生成任务编号已存在");
    ErrorCode REPORT_GENERATION_TASK_STATUS_ERROR = new ErrorCode(100146002, "报告生成任务状态不允许此操作");

    // ========== 工作量统计 100147000-100147999 ==========  (新增 2026-07-20)
    ErrorCode STAFF_WORKLOAD_STATISTICS_NOT_EXISTS = new ErrorCode(100147000, "工作量统计记录不存在");

    // ========== A. 病历模板 100148000-100148999 ==========  (新增 2026-07-20 模块扩展)
    ErrorCode MEDICAL_RECORD_TEMPLATE_NOT_EXISTS = new ErrorCode(100148000, "病历模板不存在");
    ErrorCode MEDICAL_RECORD_TEMPLATE_CODE_DUPLICATE = new ErrorCode(100148001, "病历模板编码已存在");
    ErrorCode MEDICAL_RECORD_NOT_EXISTS = new ErrorCode(100148100, "病历不存在");
    ErrorCode MEDICAL_RECORD_NO_DUPLICATE = new ErrorCode(100148101, "病历号已存在");
    ErrorCode MEDICAL_RECORD_ITEM_NOT_EXISTS = new ErrorCode(100148200, "病历字段值不存在");
    ErrorCode LIS_PACS_REPORT_NOT_EXISTS = new ErrorCode(100148300, "LIS/PACS报告不存在");
    ErrorCode EXTERNAL_REPORT_ARCHIVE_NOT_EXISTS = new ErrorCode(100148400, "外部报告归档不存在");
    ErrorCode DEVELOPMENT_SCALE_NOT_EXISTS = new ErrorCode(100148500, "发育评估量表不存在");
    ErrorCode DEVELOPMENT_SCALE_CODE_DUPLICATE = new ErrorCode(100148501, "发育评估量表编码已存在");
    ErrorCode DEVELOPMENT_SCALE_QUESTION_NOT_EXISTS = new ErrorCode(100148600, "发育评估量表题目不存在");
    ErrorCode DEVELOPMENT_ASSESSMENT_RECORD_NOT_EXISTS = new ErrorCode(100148700, "发育评估记录不存在");
    ErrorCode DEVELOPMENT_ASSESSMENT_ANSWER_NOT_EXISTS = new ErrorCode(100148800, "发育评估答题不存在");

    // ========== B. 高危儿&专案管理 100149000-100149999 ==========  (新增 2026-07-20 模块扩展)
    ErrorCode HIGH_RISK_NEWBORN_NOT_EXISTS = new ErrorCode(100149000, "高危新生儿记录不存在");
    ErrorCode HIGH_RISK_FOLLOWUP_NOT_EXISTS = new ErrorCode(100149100, "高危儿随访记录不存在");
    ErrorCode CASE_MANAGEMENT_NOT_EXISTS = new ErrorCode(100149200, "专案记录不存在");
    ErrorCode CASE_MANAGEMENT_NO_DUPLICATE = new ErrorCode(100149201, "专案编号已存在");
    ErrorCode CASE_MANAGEMENT_STATUS_ERROR = new ErrorCode(100149202, "专案状态不允许此操作");
    ErrorCode CASE_FOLLOWUP_RECORD_NOT_EXISTS = new ErrorCode(100149300, "专案随访记录不存在");
    ErrorCode CASE_RECOVERY_STANDARD_NOT_EXISTS = new ErrorCode(100149400, "专案康复达标标准不存在");
    ErrorCode EXAM_REMINDER_RULE_NOT_EXISTS = new ErrorCode(100149500, "体检催检规则不存在");
    ErrorCode EXAM_REMINDER_RULE_CODE_DUPLICATE = new ErrorCode(100149501, "体检催检规则编码已存在");
    ErrorCode EXAM_REMINDER_LOG_NOT_EXISTS = new ErrorCode(100149600, "体检催检日志不存在");
    ErrorCode EXAM_REMINDER_TEMPLATE_NOT_EXISTS = new ErrorCode(100149700, "体检提醒模板不存在");
    ErrorCode EXAM_REMINDER_TEMPLATE_CODE_DUPLICATE = new ErrorCode(100149701, "体检提醒模板编码已存在");

    // ========== C. 五健筛查配置 100150000-100150999 ==========  (新增 2026-07-20 模块扩展)
    ErrorCode HEALTH_SCREENING_PLAN_NOT_EXISTS = new ErrorCode(100150000, "体检方案不存在");
    ErrorCode HEALTH_SCREENING_PLAN_CODE_DUPLICATE = new ErrorCode(100150001, "体检方案编码已存在");
    ErrorCode SCREENING_PLAN_BATCH_NOT_EXISTS = new ErrorCode(100150100, "体检方案批次关联不存在");
    ErrorCode SCREENING_BATCH_STUDENT_NOT_EXISTS = new ErrorCode(100150150, "批次内学生名单不存在");
    ErrorCode SCREENING_BATCH_STUDENT_DUPLICATE = new ErrorCode(100150151, "该学生已在批次名单中");
    ErrorCode SCREENING_BATCH_STUDENT_NO_MATCH = new ErrorCode(100150152, "学号无法匹配学生档案");
    ErrorCode SCREENING_DEPARTMENT_NOT_EXISTS = new ErrorCode(100150200, "筛查科室不存在");
    ErrorCode SCREENING_DEPARTMENT_CODE_DUPLICATE = new ErrorCode(100150201, "筛查科室编码已存在");
    ErrorCode HEALTH_ARTICLE_NOT_EXISTS = new ErrorCode(100150300, "健康文章不存在");
    ErrorCode HEALTH_ARTICLE_CODE_DUPLICATE = new ErrorCode(100150301, "健康文章编码已存在");
    ErrorCode QUESTIONNAIRE_DEFINITION_NOT_EXISTS = new ErrorCode(100150400, "问卷定义不存在");
    ErrorCode QUESTIONNAIRE_DEFINITION_CODE_DUPLICATE = new ErrorCode(100150401, "问卷编码已存在");
    ErrorCode QUESTIONNAIRE_RECORD_NOT_EXISTS = new ErrorCode(100150500, "问卷答卷不存在");
    ErrorCode PSYCHOLOGICAL_SCALE_NOT_EXISTS = new ErrorCode(100150600, "心理量表不存在");
    ErrorCode PSYCHOLOGICAL_SCALE_CODE_DUPLICATE = new ErrorCode(100150601, "心理量表编码已存在");
    ErrorCode PSYCHOLOGICAL_SCALE_QUESTION_NOT_EXISTS = new ErrorCode(100150700, "心理量表题目不存在");
    ErrorCode PSYCHOLOGICAL_ASSESSMENT_NOT_EXISTS = new ErrorCode(100150800, "心理量表评估记录不存在");
    ErrorCode TRANSFER_IN_RECORD_NOT_EXISTS = new ErrorCode(100150900, "转入档案不存在");
    ErrorCode TRANSFER_IN_RECORD_NO_DUPLICATE = new ErrorCode(100150901, "转入编号已存在");
    ErrorCode TRANSFER_OUT_RECORD_NOT_EXISTS = new ErrorCode(100151000, "转出档案不存在");
    ErrorCode TRANSFER_OUT_RECORD_NO_DUPLICATE = new ErrorCode(100151001, "转出编号已存在");

    // ========== D. 移动端功能补全 100151100-100151999 ==========  (新增 2026-07-20 模块扩展)
    ErrorCode DEVICE_BINDING_NOT_EXISTS = new ErrorCode(100151100, "设备绑定不存在");
    ErrorCode DEVICE_BINDING_CODE_DUPLICATE = new ErrorCode(100151101, "设备编码已存在");
    ErrorCode DEVICE_DATA_RECORD_NOT_EXISTS = new ErrorCode(100151200, "设备数据记录不存在");
    ErrorCode RECHECK_CHECKIN_NOT_EXISTS = new ErrorCode(100151300, "复筛报到记录不存在");
    ErrorCode RECHECK_CHECKIN_NO_DUPLICATE = new ErrorCode(100151301, "复筛报到编号已存在");

    // ========== E. 家长端绑定/解绑 100152000-100152999 ==========  (新增 2026-08-15)
    ErrorCode PARENT_MOBILE_REQUIRED = new ErrorCode(100152000, "家长手机号不能为空");
    ErrorCode STUDENT_ALREADY_BOUND = new ErrorCode(100152001, "该学生已被其他家长绑定");
    ErrorCode STUDENT_NOT_BOUND_BY_YOU = new ErrorCode(100152002, "该学生未由当前家长绑定，无法解绑");

}
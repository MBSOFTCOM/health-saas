import request from '@/utils/request'

/**
 * 家长移动端接口（需求 139-151）
 * 涵盖：就诊人绑定、自主报名、体检报告查询、复筛通知单、问卷在线填写、
 *       量表在线填写、宣教知识查看
 * 所有家长端业务均围绕"就诊人(受检者)"展开，由后端按身份证号关联筛查数据
 */

// ========== 通用：学校/批次/字典 ==========

// 查询可选学校列表（用于报名时下拉选择）
export function getSchoolOptionList(params) {
  return request({
    url: '/parent/school/list',
    method: 'GET',
    params
  })
}

// 查询可选年级列表（按学校）
export function getGradeOptionList(schoolId) {
  return request({
    url: '/parent/school/grades',
    method: 'GET',
    params: { schoolId }
  })
}

// 查询可选班级列表（按学校+年级）
export function getClassOptionList(schoolId, grade) {
  return request({
    url: '/parent/school/classes',
    method: 'GET',
    params: { schoolId, grade }
  })
}

// 查询可选筛查批次列表（用于报名关联）
export function getBatchOptionList(params) {
  return request({
    url: '/parent/batch/list',
    method: 'GET',
    params
  })
}

// ========== 150-151 就诊人绑定 ==========

// 查询当前家长已绑定的就诊人列表
export function getBoundPatientList() {
  return request({
    url: '/parent/patient/list',
    method: 'GET'
  })
}

// 通过身份证号校验受检者是否可被绑定（确保账号唯一绑定）
export function checkPatientBindable(idCard) {
  return request({
    url: '/parent/patient/check-bindable',
    method: 'GET',
    params: { idCard }
  })
}

// 绑定就诊人（受检者信息与移动端业务自动关联）
export function bindPatient(data) {
  return request({
    url: '/parent/patient/bind',
    method: 'POST',
    data
  })
}

// 解绑就诊人
export function unbindPatient(id) {
  return request({
    url: '/parent/patient/unbind?id=' + id,
    method: 'DELETE'
  })
}

// 查询就诊人详情（含已绑定报告/复筛/问卷等业务概览）
export function getBoundPatientDetail(id) {
  return request({
    url: '/parent/patient/get?id=' + id,
    method: 'GET'
  })
}

// ========== 139-140 自主报名 ==========

// 在线提交报名申请（信息自动同步至筛查系统，并与学校及筛查批次自动关联）
export function submitRegistration(data) {
  return request({
    url: '/parent/register/submit',
    method: 'POST',
    data
  })
}

// 查询报名状态列表（家长可在线查询报名申请）
export function getRegistrationList(params) {
  return request({
    url: '/parent/register/list',
    method: 'GET',
    params
  })
}

// 查询报名详情
export function getRegistrationDetail(id) {
  return request({
    url: '/parent/register/get?id=' + id,
    method: 'GET'
  })
}

// 取消报名申请
export function cancelRegistration(id) {
  return request({
    url: '/parent/register/cancel?id=' + id,
    method: 'PUT'
  })
}

// ========== 141 体检报告查询 ==========

// 查询就诊人体检报告列表（含历史报告）
export function getReportList(patientId, params) {
  return request({
    url: '/parent/report/list',
    method: 'GET',
    params: { patientId, ...params }
  })
}

// 查询体检报告详情（筛查结果、异常项目、阳性解读、护理建议、复筛建议）
export function getReportDetail(id) {
  return request({
    url: '/parent/report/get?id=' + id,
    method: 'GET'
  })
}

// 查询历史体检报告趋势变化（按项目维度返回趋势数据）
export function getReportTrend(patientId, params) {
  return request({
    url: '/parent/report/trend',
    method: 'GET',
    params: { patientId, ...params }
  })
}

// ========== 142-143 复筛通知单查询 ==========

// 查询复筛通知单列表
export function getRescreenNoticeList(patientId, params) {
  return request({
    url: '/parent/rescreen-notice/list',
    method: 'GET',
    params: { patientId, ...params }
  })
}

// 查询复筛通知单详情（阳性结果、复筛项目、复筛时间、复筛地点、状态、注意事项）
export function getRescreenNoticeDetail(id) {
  return request({
    url: '/parent/rescreen-notice/get?id=' + id,
    method: 'GET'
  })
}

// 查询复筛状态（与复筛业务自动关联，状态同步）
export function getRescreenNoticeStatus(patientId, batchId) {
  return request({
    url: '/parent/rescreen-notice/status',
    method: 'GET',
    params: { patientId, batchId }
  })
}

// ========== 144-145 问卷在线填写 ==========

// 查询问卷列表（根据不同筛查场景动态展示）
export function getQuestionnaireList(patientId, params) {
  return request({
    url: '/parent/questionnaire/list',
    method: 'GET',
    params: { patientId, ...params }
  })
}

// 查询问卷详情（题目结构）
export function getQuestionnaireDetail(id, patientId) {
  return request({
    url: '/parent/questionnaire/get',
    method: 'GET',
    params: { id, patientId }
  })
}

// 保存问卷填写进度（支持进度保存）
export function saveQuestionnaireProgress(data) {
  return request({
    url: '/parent/questionnaire/save-progress',
    method: 'POST',
    data
  })
}

// 提交问卷（问卷结果与筛查数据进行关联分析，辅助健康评估与风险识别）
export function submitQuestionnaire(data) {
  return request({
    url: '/parent/questionnaire/submit',
    method: 'POST',
    data
  })
}

// 查询问卷填写结果
export function getQuestionnaireResult(id, patientId) {
  return request({
    url: '/parent/questionnaire/result',
    method: 'GET',
    params: { id, patientId }
  })
}

// ========== 146-147 量表在线填写 ==========

// 查询量表列表（心理筛查量表及健康评估量表）
export function getScaleList(patientId, params) {
  return request({
    url: '/parent/scale/list',
    method: 'GET',
    params: { patientId, ...params }
  })
}

// 查询量表详情（题目结构）
export function getScaleDetail(id, patientId) {
  return request({
    url: '/parent/scale/get',
    method: 'GET',
    params: { id, patientId }
  })
}

// 保存量表填写进度
export function saveScaleProgress(data) {
  return request({
    url: '/parent/scale/save-progress',
    method: 'POST',
    data
  })
}

// 提交量表（自动计算量表结果及风险等级；与心理筛查、疾病规则及阳性结果进行关联分析）
export function submitScale(data) {
  return request({
    url: '/parent/scale/submit',
    method: 'POST',
    data
  })
}

// 查询量表结果与风险等级
export function getScaleResult(id, patientId) {
  return request({
    url: '/parent/scale/result',
    method: 'GET',
    params: { id, patientId }
  })
}

// ========== 148-149 宣教知识查看 ==========

// 查询宣教知识分类列表
export function getEducationCategoryList() {
  return request({
    url: '/parent/education/category/list',
    method: 'GET'
  })
}

// 查询宣教知识列表（按分类、专题筛选；可结合受检者阳性结果推荐）
export function getEducationList(params) {
  return request({
    url: '/parent/education/list',
    method: 'GET',
    params
  })
}

// 查询宣教知识详情（科普知识、疾病预防知识、健康指导内容）
export function getEducationDetail(id) {
  return request({
    url: '/parent/education/get?id=' + id,
    method: 'GET'
  })
}

// 根据受检者阳性结果推荐相关宣教知识
export function getRecommendedEducation(patientId) {
  return request({
    url: '/parent/education/recommend?patientId=' + patientId,
    method: 'GET'
  })
}

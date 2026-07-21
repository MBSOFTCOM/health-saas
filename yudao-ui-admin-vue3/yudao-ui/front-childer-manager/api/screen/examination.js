import request from '@/utils/request'

/**
 * 体检总检管理接口（需求 131-132）
 * 扫描受检者二维码查看筛查完成情况；展示已完成/未完成项目及异常结果；
 * 实时查看筛查进度与项目状态；支持快速补检
 */

// 扫码获取受检者当前筛查完成情况（含已完成/未完成/异常项）
export function getExaminationSummary(qrcode, batchId) {
  return request({
    url: '/screening/examination/summary',
    method: 'GET',
    params: { qrcode, batchId }
  })
}

// 获取受检者筛查进度及项目状态
export function getExaminationProgress(studentId, batchId) {
  return request({
    url: '/screening/examination/progress',
    method: 'GET',
    params: { studentId, batchId }
  })
}

// 获取异常结果列表
export function getAbnormalResults(studentId, batchId) {
  return request({
    url: '/screening/examination/abnormal',
    method: 'GET',
    params: { studentId, batchId }
  })
}

// 创建补检任务
export function createRecheckTask(data) {
  return request({
    url: '/screening/examination/recheck-task',
    method: 'POST',
    data
  })
}

// 获取补检任务列表
export function getRecheckTaskList(params) {
  return request({
    url: '/screening/examination/recheck-task/list',
    method: 'GET',
    params
  })
}

// 完成补检
export function completeRecheckTask(taskId, data) {
  return request({
    url: '/screening/examination/recheck-task/complete?id=' + taskId,
    method: 'PUT',
    data
  })
}

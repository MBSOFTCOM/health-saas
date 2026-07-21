import request from '@/utils/request'

/**
 * 筛查进度查询接口（需求 133-134）
 * 支持按批次、学校、年级、班级等维度实时查询筛查完成情况；
 * 统计已筛查人数、未筛查人数及筛查完成率；
 * 动态查看现场筛查进度与任务完成情况
 */

// 多维度查询筛查进度汇总
export function getProgressSummary(params) {
  return request({
    url: '/screening/progress/summary',
    method: 'GET',
    params
  })
}

// 按维度分组统计（dimension: batch/school/grade/class）
export function getProgressByDimension(dimension, params) {
  return request({
    url: '/screening/progress/by-dimension',
    method: 'GET',
    params: { dimension, ...params }
  })
}

// 现场实时进度（任务级别）
export function getLiveProgress(batchId) {
  return request({
    url: '/screening/progress/live?batchId=' + batchId,
    method: 'GET'
  })
}

// 任务完成情况
export function getTaskCompletion(batchId, params) {
  return request({
    url: '/screening/progress/task-completion',
    method: 'GET',
    params: { batchId, ...params }
  })
}

// 进度明细列表
export function getProgressDetail(params) {
  return request({
    url: '/screening/progress/detail',
    method: 'GET',
    params
  })
}

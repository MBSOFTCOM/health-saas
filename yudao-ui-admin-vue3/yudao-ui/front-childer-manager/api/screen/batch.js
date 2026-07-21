import request from '@/utils/request'

/**
 * 体检批次相关接口（需求 127）
 * 支持医护人员通过移动端查询系统内已创建的体检批次
 */

// 分页查询体检批次列表（支持按体检时间、学校、区域筛选）
export function getBatchPage(params) {
  return request({
    url: '/screening/batch/page',
    method: 'GET',
    params
  })
}

// 查询全部批次（下拉选择用）
export function getBatchList(params) {
  return request({
    url: '/screening/batch/list',
    method: 'GET',
    params
  })
}

// 获取批次详情
export function getBatchDetail(id) {
  return request({
    url: '/screening/batch/get?id=' + id,
    method: 'GET'
  })
}

// 新增体检批次
export function createBatch(data) {
  return request({
    url: '/screening/batch/create',
    method: 'POST',
    data
  })
}

// 修改体检批次
export function updateBatch(data) {
  return request({
    url: '/screening/batch/update',
    method: 'PUT',
    data
  })
}

// 删除体检批次
export function deleteBatch(id) {
  return request({
    url: '/screening/batch/delete?id=' + id,
    method: 'DELETE'
  })
}

// 查询批次内学校列表
export function getBatchSchools(batchId) {
  return request({
    url: '/screening/batch/schools?batchId=' + batchId,
    method: 'GET'
  })
}

// 查询批次内班级列表
export function getBatchClasses(batchId, schoolId) {
  return request({
    url: '/screening/batch/classes',
    method: 'GET',
    params: { batchId, schoolId }
  })
}

// 查询批次内受检人数
export function getBatchPersonCount(batchId) {
  return request({
    url: '/screening/batch/person-count?batchId=' + batchId,
    method: 'GET'
  })
}

// 查询批次内筛查项目
export function getBatchScreenItems(batchId) {
  return request({
    url: '/screening/batch/screen-items?batchId=' + batchId,
    method: 'GET'
  })
}

// 查询批次筛查进度
export function getBatchProgress(batchId) {
  return request({
    url: '/screening/batch/progress?batchId=' + batchId,
    method: 'GET'
  })
}

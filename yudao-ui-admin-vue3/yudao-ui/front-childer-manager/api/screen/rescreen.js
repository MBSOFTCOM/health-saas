import request from '@/utils/request'

/**
 * 复筛相关接口（需求 136-138）
 * 136. 扫码复筛报到登记 + 身份核验
 * 137. 查看阳性结果、复筛项目及复筛状态；记录复筛到检情况
 * 138. 扫码快速录入复筛结果；自动关联初筛阳性结果与复筛项目；
 *      复筛数据实时同步上传；复筛结果自动归档至健康档案
 */

// ========== 复筛报到（136-137） ==========

// 扫码报到登记（同时核验身份）
export function rescreenCheckin(qrcode, batchId) {
  return request({
    url: '/screening/rescreen/checkin',
    method: 'POST',
    data: { qrcode, batchId }
  })
}

// 查询受检者阳性结果与复筛项目
export function getRescreenItems(qrcode, batchId) {
  return request({
    url: '/screening/rescreen/items',
    method: 'GET',
    params: { qrcode, batchId }
  })
}

// 查询复筛状态
export function getRescreenStatus(studentId, batchId) {
  return request({
    url: '/screening/rescreen/status',
    method: 'GET',
    params: { studentId, batchId }
  })
}

// 记录复筛到检情况
export function recordRescreenArrival(data) {
  return request({
    url: '/screening/rescreen/arrival',
    method: 'POST',
    data
  })
}

// 复筛报到列表（用于现场查询）
export function getRescreenCheckinList(params) {
  return request({
    url: '/screening/rescreen/checkin-list',
    method: 'GET',
    params
  })
}

// ========== 复筛信息登记（138） ==========

// 扫码进入复筛登记（返回受检者信息、初筛阳性项、关联复筛项目）
export function getRescreenRegisterByQrcode(qrcode, batchId) {
  return request({
    url: '/screening/rescreen/register-by-qrcode',
    method: 'GET',
    params: { qrcode, batchId }
  })
}

// 保存复筛结果（自动关联初筛阳性结果与复筛项目）
export function saveRescreenResult(data) {
  return request({
    url: '/screening/rescreen/save',
    method: 'POST',
    data
  })
}

// 批量保存复筛结果
export function batchSaveRescreenResult(data) {
  return request({
    url: '/screening/rescreen/batch-save',
    method: 'POST',
    data
  })
}

// 复筛数据实时同步上传
export function syncRescreenData(data) {
  return request({
    url: '/screening/rescreen/sync',
    method: 'POST',
    data
  })
}

// 复筛结果自动归档至健康档案
export function archiveRescreenToHealth(studentId, batchId) {
  return request({
    url: '/screening/rescreen/archive',
    method: 'POST',
    data: { studentId, batchId }
  })
}

// 查询复筛历史记录
export function getRescreenHistory(studentId) {
  return request({
    url: '/screening/rescreen/history?studentId=' + studentId,
    method: 'GET'
  })
}

import request from '@/utils/request'

/**
 * 筛查数据登记接口（需求 128）
 * 支持扫描二维码进入登记界面；录入体形、视力、骨骼、口腔、心理等五健筛查结果；
 * 支持设备对接自动采集数据；支持异常结果识别与数据校验
 */

// 通过二维码获取受检者及登记任务信息
export function getScreeningTaskByQrcode(qrcode, batchId) {
  return request({
    url: '/screening/register/task-by-qrcode',
    method: 'GET',
    params: { qrcode, batchId }
  })
}

// 保存单项筛查结果（体形/视力/骨骼/口腔/心理）
// category: body-shape / vision / bone / oral / mental
export function saveScreeningResult(data) {
  return request({
    url: '/screening/register/save',
    method: 'POST',
    data
  })
}

// 批量保存筛查结果
export function batchSaveScreeningResult(data) {
  return request({
    url: '/screening/register/batch-save',
    method: 'POST',
    data
  })
}

// 获取受检者各项目筛查结果
export function getScreeningResult(studentId, batchId) {
  return request({
    url: '/screening/register/result',
    method: 'GET',
    params: { studentId, batchId }
  })
}

// 异常结果识别（前端提交数据，后端返回异常项标识）
export function checkAbnormal(data) {
  return request({
    url: '/screening/register/check-abnormal',
    method: 'POST',
    data
  })
}

// 数据校验（提交前的完整性、合理性校验）
export function validateScreeningData(data) {
  return request({
    url: '/screening/register/validate',
    method: 'POST',
    data
  })
}

// 设备采集数据上传（统一入口，按 deviceType 分流）
export function uploadDeviceData(data) {
  return request({
    url: '/screening/register/device-data',
    method: 'POST',
    data
  })
}

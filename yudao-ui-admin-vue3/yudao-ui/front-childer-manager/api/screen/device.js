import request from '@/utils/request'

/**
 * 设备管理与数据同步接口（需求 135）
 * 支持按品牌、型号快速配对与自动识别；
 * 支持筛查数据实时同步上传、设备状态检测、数据同步校验及异常提醒
 */

// 查询设备列表
export function getDeviceList(params) {
  return request({
    url: '/screening/device/list',
    method: 'GET',
    params
  })
}

// 设备分页查询
export function getDevicePage(params) {
  return request({
    url: '/screening/device/page',
    method: 'GET',
    params
  })
}

// 设备详情
export function getDeviceDetail(id) {
  return request({
    url: '/screening/device/get?id=' + id,
    method: 'GET'
  })
}

// 新增设备
export function createDevice(data) {
  return request({
    url: '/screening/device/create',
    method: 'POST',
    data
  })
}

// 修改设备
export function updateDevice(data) {
  return request({
    url: '/screening/device/update',
    method: 'PUT',
    data
  })
}

// 删除设备
export function deleteDevice(id) {
  return request({
    url: '/screening/device/delete?id=' + id,
    method: 'DELETE'
  })
}

// 按品牌、型号配对设备
export function pairDevice(brand, model, data) {
  return request({
    url: '/screening/device/pair',
    method: 'POST',
    data: { brand, model, ...data }
  })
}

// 设备状态检测
export function checkDeviceStatus(deviceId) {
  return request({
    url: '/screening/device/status?deviceId=' + deviceId,
    method: 'GET'
  })
}

// 数据实时同步上传
export function syncDeviceData(data) {
  return request({
    url: '/screening/device/sync',
    method: 'POST',
    data
  })
}

// 数据同步校验
export function verifySyncData(data) {
  return request({
    url: '/screening/device/sync-verify',
    method: 'POST',
    data
  })
}

// 异常提醒列表
export function getDeviceAlerts(deviceId) {
  return request({
    url: '/screening/device/alerts?deviceId=' + deviceId,
    method: 'GET'
  })
}

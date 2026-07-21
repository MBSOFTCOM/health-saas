import request from '@/utils/request'

/**
 * 学生信息管理接口（需求 129-130）
 * 支持移动端新增受检者、按条件查询、二维码生成与蓝牙打印
 */

// 分页查询学生信息（支持按学校、年级、班级、姓名筛选）
export function getStudentPage(params) {
  return request({
    url: '/screening/student/page',
    method: 'GET',
    params
  })
}

// 学生列表（不分页）
export function getStudentList(params) {
  return request({
    url: '/screening/student/list',
    method: 'GET',
    params
  })
}

// 学生详情
export function getStudentDetail(id) {
  return request({
    url: '/screening/student/get?id=' + id,
    method: 'GET'
  })
}

// 通过二维码编码获取学生信息
export function getStudentByQrcode(qrcode) {
  return request({
    url: '/screening/student/qrcode?code=' + encodeURIComponent(qrcode),
    method: 'GET'
  })
}

// 新增学生
export function createStudent(data) {
  return request({
    url: '/screening/student/create',
    method: 'POST',
    data
  })
}

// 修改学生
export function updateStudent(data) {
  return request({
    url: '/screening/student/update',
    method: 'PUT',
    data
  })
}

// 删除学生
export function deleteStudent(id) {
  return request({
    url: '/screening/student/delete?id=' + id,
    method: 'DELETE'
  })
}

// 获取学生二维码内容（含二维码图片 base64）
export function getStudentQrcode(id) {
  return request({
    url: '/screening/student/qrcode-image?id=' + id,
    method: 'GET'
  })
}

// 批量生成学生二维码
export function batchGenerateQrcode(ids) {
  return request({
    url: '/screening/student/qrcode-batch',
    method: 'POST',
    data: { ids }
  })
}

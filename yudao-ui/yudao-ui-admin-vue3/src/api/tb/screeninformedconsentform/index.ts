import request from '@/config/axios'

// 知情同意书 VO
export interface ScreenInformedConsentFormVO {
  id: number // 自增id
  studentId: number // 受筛查学生的id（待筛查人员id）
  schoolName: string // 学校
  classroom: string // 班级
  isSign: boolean // 是否签署1：是  2：否
  reason: string // 拒绝签署原因
  signature: string // 家长签名图片地址
}

// 知情同意书 API
export const ScreenInformedConsentFormApi = {
  // 查询知情同意书分页
  getScreenInformedConsentFormPage: async (params: any) => {
    return await request.get({ url: `/tb/screen-informed-consent-form/page`, params })
  },

  // 查询知情同意书详情
  getScreenInformedConsentForm: async (id: number) => {
    return await request.get({ url: `/tb/screen-informed-consent-form/get?id=` + id })
  },
  // 查询知情同意书详情
  getLastInformedConsentForm: async (studentId: number) => {
    return await request.get({ url: `/tb/screen-informed-consent-form/get/last?studentId=` + studentId })
  },

  // 新增知情同意书
  createScreenInformedConsentForm: async (data: ScreenInformedConsentFormVO) => {
    return await request.post({ url: `/tb/screen-informed-consent-form/create`, data })
  },

  // 修改知情同意书
  updateScreenInformedConsentForm: async (data: ScreenInformedConsentFormVO) => {
    return await request.put({ url: `/tb/screen-informed-consent-form/update`, data })
  },

  // 删除知情同意书
  deleteScreenInformedConsentForm: async (id: number) => {
    return await request.delete({ url: `/tb/screen-informed-consent-form/delete?id=` + id })
  },

  // 导出知情同意书 Excel
  exportScreenInformedConsentForm: async (params) => {
    return await request.download({ url: `/tb/screen-informed-consent-form/export-excel`, params })
  },
}

import request from '@/config/axios'

// 消耗管理记录 VO
export interface ScreenConsumeRecordVO {
  id: number // 主键id
  changeNumber: number // 变化量
  type: number // 变化类型（1：筛查自动扣减，2：手动增加库存，3：手动减少库存）
  consumeId: number // 消耗管理表id
}

// 消耗管理记录 API
export const ScreenConsumeRecordApi = {
  // 查询消耗管理记录分页
  getScreenConsumeRecordPage: async (params: any) => {
    return await request.get({ url: `/tb/screen-consume-record/page`, params })
  },

  // 查询消耗管理记录详情
  getScreenConsumeRecord: async (id: number) => {
    return await request.get({ url: `/tb/screen-consume-record/get?id=` + id })
  },

  // 新增消耗管理记录
  createScreenConsumeRecord: async (data: ScreenConsumeRecordVO) => {
    return await request.post({ url: `/tb/screen-consume-record/create`, data })
  },

  // 修改消耗管理记录
  updateScreenConsumeRecord: async (data: ScreenConsumeRecordVO) => {
    return await request.put({ url: `/tb/screen-consume-record/update`, data })
  },

  // 删除消耗管理记录
  deleteScreenConsumeRecord: async (id: number) => {
    return await request.delete({ url: `/tb/screen-consume-record/delete?id=` + id })
  },

  // 导出消耗管理记录 Excel
  exportScreenConsumeRecord: async (params) => {
    return await request.download({ url: `/tb/screen-consume-record/export-excel`, params })
  },
}
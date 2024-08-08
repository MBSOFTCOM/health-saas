import request from '@/config/axios'

// 消耗管理 VO
export interface ScreenConsumeVO {
  id: number // 主键id
  reagentId: number // 试剂id
  reagentName: string // 试剂名称
  reagentType: number // 试剂类型
  consumeOrder: number // 消耗序位
  bathNumber: string // 批次号
  inboundNumber: number // 入库量（按试剂）
  manufactureDate: Date // 生产日期
  reagentSpecsNum: number // 转换系数（人次）
  threshold: number // 库存预警值（按试剂）
  indate: string // 有效期
  usable: number // 是否启用

}

// 消耗管理 API
export const ScreenConsumeApi = {
  // 查询消耗管理分页
  getScreenConsumePage: async (params: any) => {
    return await request.get({ url: `/tb/screen-consume/page`, params })
  },

  // 查询消耗管理详情
  getScreenConsume: async (id: number) => {
    return await request.get({ url: `/tb/screen-consume/get?id=` + id })
  },

  // 新增消耗管理
  createScreenConsume: async (data: ScreenConsumeVO) => {
    return await request.post({ url: `/tb/screen-consume/create`, data })
  },

  // 修改消耗管理
  updateScreenConsume: async (data: ScreenConsumeVO) => {
    return await request.put({ url: `/tb/screen-consume/update`, data })
  },

  // 删除消耗管理
  deleteScreenConsume: async (id: number) => {
    return await request.delete({ url: `/tb/screen-consume/delete?id=` + id })
  },

  // 导出消耗管理 Excel
  exportScreenConsume: async (params) => {
    return await request.download({ url: `/tb/screen-consume/export-excel`, params })
  },
}

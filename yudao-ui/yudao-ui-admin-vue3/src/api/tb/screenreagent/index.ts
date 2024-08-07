import request from '@/config/axios'

// 试剂 VO
export interface ScreenReagentVO {
  id: number // 自增主键
  name: string // 试剂名称
  type: boolean // 试剂类型
  reagentSpecsNum: number // 规格型号（人份）
  unit: string // 单位
  threshold: number // 库存预警值
  manufacturer: string // 供应商
  usable: number // 是否启用
  batchNumber: string // 批号
  lifespan: number // 有效期
  manufactureTime: string // 生产日期,时间戳
  num: number // 现有库存，实际可消耗次数的是	库存数x规格型号
  consumeOrder: number // 消耗序位，1最先
}

// 试剂 API
export const ScreenReagentApi = {
  // 查询试剂分页
  getScreenReagentPage: async (params: any) => {
    return await request.get({ url: `/tb/screen-reagent/page`, params })
  },

  // 查询试剂详情
  getScreenReagent: async (id: number) => {
    return await request.get({ url: `/tb/screen-reagent/get?id=` + id })
  },

  // 新增试剂
  createScreenReagent: async (data: ScreenReagentVO) => {
    return await request.post({ url: `/tb/screen-reagent/create`, data })
  },

  // 修改试剂
  updateScreenReagent: async (data: ScreenReagentVO) => {
    return await request.put({ url: `/tb/screen-reagent/update`, data })
  },

  // 删除试剂
  deleteScreenReagent: async (id: number) => {
    return await request.delete({ url: `/tb/screen-reagent/delete?id=` + id })
  },

  // 导出试剂 Excel
  exportScreenReagent: async (params) => {
    return await request.download({ url: `/tb/screen-reagent/export-excel`, params })
  },
}
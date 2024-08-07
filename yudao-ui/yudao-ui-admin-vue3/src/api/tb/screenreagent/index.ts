import request from '@/config/axios'

// 试剂 VO
export interface ScreenReagentVO {
  name: string // 试剂名称
  type: number // 试剂类型
  reagentSpecsNum: number // 转换系数（人次）
  usable: number // 是否启用
  titer: number // 效价
  potencyUnit: string // 效价单位
  specification: number // 规格
  specificationUnit: string // 规格单位
  packageUnit: string // 包装单位
  manufacturer: string // 供应商
  threshold: number // 库存预警值（按试剂）
  id: number // 自增主键id
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
import request from '@/config/axios'

// 中英文对照 VO
export interface EngCnReflectVO {
  id: number // 主键
  ruleCode: string // 规则编号
  englishName: string // 英文名称
  chineseName: string // 中文名称
}

// 中英文对照 API
export const EngCnReflectApi = {
  // 查询中英文对照分页
  getEngCnReflectPage: async (params: any) => {
    return await request.get({ url: `/cd/eng-cn-reflect/page`, params })
  },

  // 查询中英文对照详情
  getEngCnReflect: async (id: number) => {
    return await request.get({ url: `/cd/eng-cn-reflect/get?id=` + id })
  },

  // 新增中英文对照
  createEngCnReflect: async (data: EngCnReflectVO) => {
    return await request.post({ url: `/cd/eng-cn-reflect/create`, data })
  },

  // 修改中英文对照
  updateEngCnReflect: async (data: EngCnReflectVO) => {
    return await request.put({ url: `/cd/eng-cn-reflect/update`, data })
  },

  // 删除中英文对照
  deleteEngCnReflect: async (id: number) => {
    return await request.delete({ url: `/cd/eng-cn-reflect/delete?id=` + id })
  },

  // 导出中英文对照 Excel
  exportEngCnReflect: async (params) => {
    return await request.download({ url: `/cd/eng-cn-reflect/export-excel`, params })
  },
}
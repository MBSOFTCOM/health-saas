import request from '@/config/axios'

// 工作进展报告-统计表-导出的历史选项 VO
export interface ScreenStaticsHistoryVO {
  id: number // 主键id
  deptId: number // 机构id
  tableTittle: string // 表格标题
  school: string // 学校名称
  hospital: string // 医院名称
  district: string // 行政区划名称
  contact: string // 联系人姓名
  contactPhone: string // 联系电话
  injectionPeople: string // 注射人姓名
  checkPeople: string // 查验人姓名
  infoList: [] // 基本信息勾选，如'[1,2,3,4]'
}

// 工作进展报告-统计表-导出的历史选项 API
export const ScreenStaticsHistoryApi = {
  // 查询工作进展报告-统计表-导出的历史选项分页
  getScreenStaticsHistoryPage: async (params: any) => {
    return await request.get({ url: `/tb/screen-statics-history/page`, params })
  },

  // 查询工作进展报告-统计表-导出的历史选项详情
/*  getScreenStaticsHistory: async (id: number) => {
    return await request.get({ url: `/tb/screen-statics-history/get?id=` + id })
  },*/
  // 查询工作进展报告-统计表-导出的历史选项详情
  getScreenStaticsHistory: async () => {
    return await request.get({ url: `/tb/screen-statics-history/get`})
  },

  // 新增工作进展报告-统计表-导出的历史选项
  createScreenStaticsHistory: async (data: ScreenStaticsHistoryVO) => {
    return await request.post({ url: `/tb/screen-statics-history/create`, data })
  },

  // 修改工作进展报告-统计表-导出的历史选项
  updateScreenStaticsHistory: async (data: ScreenStaticsHistoryVO) => {
    return await request.put({ url: `/tb/screen-statics-history/update`, data })
  },

  // 删除工作进展报告-统计表-导出的历史选项
  deleteScreenStaticsHistory: async (id: number) => {
    return await request.delete({ url: `/tb/screen-statics-history/delete?id=` + id })
  },

  // 导出工作进展报告-统计表-导出的历史选项 Excel
  exportScreenStaticsHistory: async (params) => {
    return await request.download({ url: `/tb/screen-statics-history/export-excel`, params })
  },
}

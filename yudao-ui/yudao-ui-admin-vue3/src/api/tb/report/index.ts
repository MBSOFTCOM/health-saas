import request from '@/config/axios'

export const ReportApi = {
  // 查询报表数据
  getReportData: async (params: any) => {
    return await request.get({ url: `/tb/screen-report/getReportData`, params })
  },

  // 查询汇总表--学校肺结核筛查结果统计表
  getSchoolSummary: async (params: any) => {
    return await request.get({ url: `/tb/screen-report/getSchoolSummary`, params })
  },

  // 查询汇总表--医疗结构结核菌素皮肤试验开展情况统计表
  getAgencySummary: async (params: any) => {
    return await request.get({ url: `/tb/screen-report/exportSchoolSummary`, params })
  },
}

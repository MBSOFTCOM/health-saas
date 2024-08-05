import request from '@/config/axios'

export const ReportApi = {
  // 查询报表数据
  getReportData: async (params: any) => {
    return await request.get({ url: `/tb/screen-report/getReportData`, params })
  }
}
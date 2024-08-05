import request from '@/config/axios'

// 诊断组 VO
export interface ScreenDiagnosisVO {
  id: number // 主键
  screenId: string // 筛查编号
  syncId: number // 同步时唯一编码
  doctorSignature: string // 医生签名
  screenTime: Date // 筛查时间
  outcome: boolean // 结果。1-利福平耐药 2-病原学阳性 3-病原学阴性 4-无病原学结果
  treatmentProgram: string // 治疗方案
  report: number // 是否网报 0-否 1-是
  preventiveTreatment: string // 符合潜伏治疗条件者是否进行预防性治疗 0-否 1-是
  screenOrder: number // 筛查次序
  personId: number // 对应摸底表中id
  screenPoint: string // 筛查点

  idNum: string,
  name: string
}

// 诊断组 API
export const ScreenDiagnosisApi = {
  // 查询诊断组分页
  getScreenDiagnosisPage: async (params: any) => {
    return await request.get({ url: `/tb/screen-diagnosis/page`, params })
  },

  // 查询诊断组详情
  getScreenDiagnosis: async (id: number) => {
    return await request.get({ url: `/tb/screen-diagnosis/get?id=` + id })
  },

  // 新增诊断组
  createScreenDiagnosis: async (data: ScreenDiagnosisVO) => {
    return await request.post({ url: `/tb/screen-diagnosis/create`, data })
  },

  // 修改诊断组
  updateScreenDiagnosis: async (data: ScreenDiagnosisVO) => {
    return await request.put({ url: `/tb/screen-diagnosis/update`, data })
  },

  // 删除诊断组
  deleteScreenDiagnosis: async (id: number) => {
    return await request.delete({ url: `/tb/screen-diagnosis/delete?id=` + id })
  },

  // 导出诊断组 Excel
  exportScreenDiagnosis: async (params) => {
    return await request.download({ url: `/tb/screen-diagnosis/export-excel`, params })
  },

  // 根据personId获取提交的最近一次诊断结果
  getLastTimeDiagnosisResult: async (personId: number) =>  {
    return await request.get({url: '/tb/screen-diagnosis/last-time?personId=' + personId})
  },

  // 获取检查项 选择器筛查列表
  getCheckSelectList: async (type: number, personId: number) => {
    return await request.get({ url: `/tb/screen-diagnosis/getCheckSelectList?type=` + type + '&personId=' + personId })
  },
}

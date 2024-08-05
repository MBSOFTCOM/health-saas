import request from '@/config/axios'

// 实验室组 VO
export interface ScreenExperimentVO {
  id: number // 主键
  syncId: number // 同步时唯一编码
  personId: number // 对应摸底表中id
  sputumExaminationId: number // 对应痰检组表中id
  smearResult: number // 涂片结果（1=阳性、2=阴性、3=未查）
  cultureResult: number // 培养结果 (1=阳性、2=阴性、3=污染、4=未查）
  molecularBiology: number // 分子生物学（1=结核分枝杆菌核酸阳性、2=未检出结核分枝杆菌、3=不确定、4=未查）
  tissueSpecimenResult: number // 组织标本检测结果（1=组织学阳性、2=仅病理学阳性、3=阴性、4=未查）
  strainIdentificationResult: number // 菌种鉴定检测结果（1=结核分枝杆菌复合群、2=非结核分枝杆菌、3=未查）
  tbDrugSensitivityMethod: number // 结核分支杆菌药敏检测方法（1=分子生物学、2=传统药敏试验）
  drugResistanceResult: number // 耐药综合判定（1=单耐利福平、2=耐多药、3=广泛耐药、4=单耐异烟肼、5=利福平与异烟肼均敏感）
  hivResult: number // HIV抗体检测结果（1=已知阳性、2=新检测初筛阳性、3=新检测确认阳性、4=阴性、5=拒查、6=未提供）
  remark: string // 备注

  screenTime: Date,
  screenOrder: string,
  screenId: string
}

// 实验室组 API
export const ScreenExperimentApi = {
  // 查询实验室组分页
  getScreenExperimentPage: async (params: any) => {
    return await request.get({ url: `/tb/screen-experiment/page`, params })
  },

  // 查询实验室组详情
  getScreenExperiment: async (id: number) => {
    return await request.get({ url: `/tb/screen-experiment/get?id=` + id })
  },

  // 新增实验室组
  createScreenExperiment: async (data: ScreenExperimentVO) => {
    return await request.post({ url: `/tb/screen-experiment/create`, data })
  },

  // 修改实验室组
  updateScreenExperiment: async (data: ScreenExperimentVO) => {
    return await request.put({ url: `/tb/screen-experiment/update`, data })
  },

  // 删除实验室组
  deleteScreenExperiment: async (id: number) => {
    return await request.delete({ url: `/tb/screen-experiment/delete?id=` + id })
  },

  // 导出实验室组 Excel
  exportScreenExperiment: async (params) => {
    return await request.download({ url: `/tb/screen-experiment/export-excel`, params })
  },
}

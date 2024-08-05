import request from '@/config/axios'

// 重复筛查人员管理 VO
export interface ScreenRepeatPersonVO {
  id: number // 主键
  height: number // 身高
  weight: number // 体重
  permanentAddress: string // 户籍地址
  permanentAddressProvince: string // 户籍地址-省
  permanentAddressCity: string // 户籍地址-市
  permanentAddressCounty: string // 户籍地址-县
  permanentAddressTown: string // 户籍地址-乡镇
  address: string // 现住址
  province: string // 现住址-省
  city: string // 现住址-市
  county: string // 现住址-县
  town: string // 现住址-乡镇
  nation: number // 民族
  firstType: number // 第一人群分类（1-重点人群 2-非重点人群 4-教职工）
  moreType: number // 多人群分类（1-学生、2-老年人、4-教职工、8-密接者、16-糖尿病、32-僧尼、64-既往患者）
  schoolOrTemple: string // 单位
  classroom: string // 班级
  contactHistory: number // 既往有无和肺结核患者密切接触。0-否 1-是
  isNew: number // 是否需筛查(0-否，1-是)
  isScreened: number // 是否已筛查(0-未筛查，1-已筛查，2-正在筛查中)
  isNewStudent: number // 是否为新生(0-否，1-是)
  screenPoint: string // 筛查点
  screenTime: Date // 计划筛查时间
  remark: string // 备注
  year: number // 所属工作年份
  screenType: number // 筛查类型  1--常规、2--新生、3--应急
  screenId: string // 筛查编号（生成）
  syncId: number // 同步时唯一编码
  idNum: string // 身份证号
  name: string // 姓名
  age: number // 年龄
  tel: string // 联系电话
  sex: boolean // 性别(1-女，0-男)
}

// 重复筛查人员管理 API
export const ScreenRepeatPersonApi = {
  // 查询重复筛查人员管理分页
  getScreenRepeatPersonPage: async (params: any) => {
    return await request.get({ url: `/tb/screen-repeat-person/page`, params })
  },

  // 查询重复筛查人员管理详情
  getScreenRepeatPerson: async (id: number) => {
    return await request.get({ url: `/tb/screen-repeat-person/get?id=` + id })
  },

  // 新增重复筛查人员管理
  createScreenRepeatPerson: async (data: ScreenRepeatPersonVO) => {
    return await request.post({ url: `/tb/screen-repeat-person/create`, data })
  },

  // 修改重复筛查人员管理
  updateScreenRepeatPerson: async (data: ScreenRepeatPersonVO) => {
    return await request.put({ url: `/tb/screen-repeat-person/update`, data })
  },

  // 删除重复筛查人员管理
  deleteScreenRepeatPerson: async (id: number) => {
    return await request.delete({ url: `/tb/screen-repeat-person/delete?id=` + id })
  },

  // 导出重复筛查人员管理 Excel
  exportScreenRepeatPerson: async (params) => {
    return await request.download({ url: `/tb/screen-repeat-person/export-excel`, params })
  },

  // 查询是否有重复人员名单未处理
  getIsRemainRepeatPerson: async () => {
    return await request.get({ url: `/tb/screen-repeat-person/get-remain-repeat-person` })
  },
  // 重复人员恢复至摸底库，查询摸底库中是否存在 与恢复人员 的身份证号、工作年度、筛查类型一样的记录
  isExist: async (id: number) => {
    return await request.get({url: `/tb/screen-repeat-person/get-is-exist-person?id=` + id})
  },
  // 重复人员恢复至摸底库
  recoverData: async (id: number) => {
    return await request.get({url:  `/tb/screen-repeat-person/recover-data?id=` + id})
  }
}

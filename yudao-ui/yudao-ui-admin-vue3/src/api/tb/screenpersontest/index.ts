import request from '@/config/axios'

// 摸底库测试 VO
export interface ScreenPersonTestVO {
  id: number // 主键
  year: number // 年份
  screenType: number // 筛查类型  1--常规、2--新生、3--应急
  screenId: string // 筛查编号（生成）
  syncId: number // 同步时唯一编码
  idNum: string // 身份证号
  name: string // 姓名
  age: number // 年龄
  tel: string // 联系电话
  sex: number // 性别(1-女，0-男)
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
  schoolOrTemple: string // 学校或寺庙
  classroom: string // 班级
  contactHistory: number // 既往有无和肺结核患者密切接触。0-否 1-是
  isNew: number // 是否需筛查(0-否，1-是)
  isScreened: number // 是否已筛查(0-否，1-是)
  isNewStudent: number // 是否为新生(0-否，1-是)
  screenPoint: string // 筛查点
  screenTime: Date // 计划筛查时间
  remark: string // 备注
}

// 摸底库测试 API
export const ScreenPersonTestApi = {


  // 生成测试数据
  generateData: async () => {
    return await request.post({ url: `/tb/synchronize/generate-person-data` })
  },
}

import request from '@/config/axios'

// 摸底 VO
export interface ScreenPersonVO {
  id: number // 主键
  idNum: string // 身份证号
  name: string // 姓名
  age: number //年龄
  tel: string // 联系电话
  sex: number // 性别(2-女，1-男)
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
  firstType: number // 第一人群分类（1-重点人群 2-非重点人群 3-教职工）
  moreType: string // 多人群分类（1-学生、2-老年人、3-教职工、4-密接者、5-糖尿病、6-僧尼、7-既往患者）
  schoolOrTemple: string // 学校或寺庙
  classroom: string // 班级
  isNew: number // 是否新增
  isNewStudent: number //是否是新生
  isScreened: number // 是否筛查
  belongingRegion: string // 归属地区
  screenTime: Date // 筛查时间
  year: number
  screenType: number,
  studentType: number,
  deptId: number
}

// 摸底 API
export const ScreenPersonApi = {
  // 查询摸底分页
  getScreenPersonPage: async (params: any) => {
    return await request.get({url: `/tb/screen-person/page`, params})
  },

  // 查询待筛查人员分页
  getScreenedPage: async (params: any) => {
    return await request.get({url: `/tb/screen-person/screened-page`, params})
  },

  // 查询摸底详情
  getScreenPerson: async (id: number) => {
    return await request.get({url: `/tb/screen-person/get?id=` + id})
  },

  // 新增摸底
  createScreenPerson: async (data: ScreenPersonVO) => {
    return await request.post({url: `/tb/screen-person/create`, data})
  },

  // 修改摸底
  updateScreenPerson: async (data: ScreenPersonVO) => {
    return await request.put({url: `/tb/screen-person/update`, data})
  },

  // 删除摸底
  deleteScreenPerson: async (id: number) => {
    return await request.delete({url: `/tb/screen-person/delete?id=` + id})
  },

  // 下载摸底人员导入模板 Excel
  importScreenPersonTemplate: async () => {
    return await request.download({url: `/tb/screen-person/get-import-template`})
  },

  // 下载待筛查人员导入模板 Excel
  importScreenPersonTemplate2: async () => {
    return await request.download({url: `/tb/screen-person/get-import-template2`})
  },

  // 导出摸底 Excel
  exportScreenPerson: async (params) => {
    return await request.download({url: `/tb/screen-person/export-excel`, params})
  },

  // 导出待筛查人员
  exportScreenedPerson: async (params) => {
    return await request.download({url: `/tb/screen-person/export`, params})
  },

  // 获取某个患者的各组数据
  getPatientInfoList: async (id: number, year: number, screenType:number) => {
    return await request.get({url: `/tb/screen-person/get-patientInfoList?id=`+ id + `&year=` + year + `&screenType=` + screenType})
  },

  // 获取图片链接
  getScreenPersonImageUrl: async (type: number, personId: number, screenOrder: number, screenId: string, year: number, screenType: number) => {
    return await request.get({url: `/tb/screen-person/get-imageUrl?personId=`
        + personId + `&type=` + type + `&screenOrder=` + screenOrder + `&screenId=` + screenId + `&year=` + year + `&screenType=` + screenType})
  },

  // 上传图片
  uploadImage: async (data) => {
    return await request.upload({
      url: `/tb/screen-person/update-image`,
      data: data,
    })
  },

  // 上传图片
  uploadImage2: async (data) => {
    return await request.upload({
      url: `/tb/screen-person/update-image2`,
      data: data,
    })
  },

  // 导入
  importFile: async (data) => {
    return await request.upload({
      url: import.meta.env.VITE_BASE_URL + import.meta.env.VITE_API_URL + `/tb/screen-person/import-template`,
      data: data,
    })
  },


}

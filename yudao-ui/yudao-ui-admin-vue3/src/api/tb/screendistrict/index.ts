import request from '@/config/axios'

// 甘孜州区划 VO
export interface ScreenDistrictVO {
  id: number // 自增类型（主键）
  code: string // 区划代码（唯一）
  level: string // 区划级别
  name: string // 区划名称
  parentCode: string // 上级地区code
}

// 甘孜州区划 API
export const ScreenDistrictApi = {

  getProvince: async () => {
    return await request.get({url: `/tb/screen-district/get-province`})
  },

  getCity: async (provinceCode: string) => {
    return await request.get({url: `/tb/screen-district/get-city?provinceCode=` + provinceCode})
  },

  getCounty: async (cityCode: string) => {
    return await request.get({url: `/tb/screen-district/get-county?cityCode=` + cityCode})
  },

  getTown: async (countyCode: string) => {
    return await request.get({url: `/tb/screen-district/get-town?countyCode=` + countyCode})
  },

  getDistrictList: async (level: number, parentCode: string) => {
    return await request.get({url: `/tb/screen-district/get-district-list?level=` + level + '&parentCode=' + parentCode})
  },

  getDistrictList2: async () => {
    return await request.get({url: `/tb/screen-district/get-district-list2`})
  },

  getDistrictCode: async (deptId: number) => {
    return await request.get({url: `/tb/screen-district/get-district-code?deptId=` + deptId})
  },

}

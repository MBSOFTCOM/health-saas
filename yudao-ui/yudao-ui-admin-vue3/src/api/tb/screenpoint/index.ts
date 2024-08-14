import request from '@/config/axios'
import {PermissionAssignUserRoleReqVO} from "@/api/system/permission";
import {DeptVO} from "@/api/system/dept";

// 筛查点 VO
export interface ScreenPointVO {
  worker: string //队长
  name: string // 筛查点名称
  remark: string // 备注
  collectWorker: any //采集组
  ppdWorker: any // PPD组
  drctWorker: any // DR/CT组
  sputum_worker: any
  experiment_worker: any
  electrocardiogram_worker: any
  diagnosis_worker: any
}

// 筛查点 API
export const ScreenPointApi = {
  // 查询筛查点分页
  getScreenPointPage: async (params: any) => {
    return await request.get({url: `/tb/screen-point/page`, params})
  },

  // 查询筛查点详情
  getScreenPoint: async (id: number) => {
    return await request.get({url: `/tb/screen-point/get?id=` + id})
  },

  // 新增筛查点
  createScreenPoint: async (data: ScreenPointVO) => {
    return await request.post({url: `/tb/screen-point/create`, data})
  },

  // 修改筛查点
  updateScreenPoint: async (data: ScreenPointVO) => {
    return await request.put({url: `/tb/screen-point/update`, data})
  },

  // 删除筛查点
  deleteScreenPoint: async (id: number) => {
    return await request.delete({url: `/tb/screen-point/delete?id=` + id})
  },

  // 导出筛查点 Excel
  exportScreenPoint: async (params) => {
    return await request.download({url: `/tb/screen-point/export-excel`, params})
  },

  // 查询筛查点列表
  getScreenPointList: async () => {
    return await request.get({url: `/tb/screen-point/get-list`})
  },

  updatePoint: async (data: any) => {
    return await request.post({url: `/tb/screen-point/update-point`, data: data})
  },

  // 下载筛查点导入模板 Excel
  importScreenPersonTemplate: async () => {
    return await request.download({url: `/tb/screen-point/get-import-template`})
  },

  getUserList: async () => {
    return await request.get({url: '/tb/screen-point/get-userList'})
  },

  getDeptList: async () => {
    return await request.get({url: `/tb/screen-point/get-deptList`});
  },

  setWorker: async (userId: number, screenPointId: number) => {
    return await request.get({url: `/tb/screen-point/set-worker?userId=` + userId + `&screenPointId=` + screenPointId});
  },
  // 分配新队长，删除原来的队长角色
  giveCapRole: async (newCapId: number, oldCapId: number, screenPointId: number) => {
    return await request.get({url: `/tb/screen-point/set-cap?newCapId=` + newCapId + `&oldCapId=` + oldCapId + `&screenPointId=` + screenPointId})
  },
  getIsDistribute: async (userId: number) => {
    return await request.get({url: `/tb/screen-point/get-isDistribute?userId=` + userId})
  },
  getIsDistributeNowYear: async (userId: number, year: number) => {
    return await request.get({url: `/tb/screen-point/get-distributeyear?userId=` + userId + `&year=` + year})
  },

  // 各组可共用
  resolveCollectList: async (collectList: any) => {
    return await request.get({url: `/tb/screen-point/resolve-collect-list?collectList=` + collectList})
  },

  deleteScreenPointCollect: async (collectWorker: string, screenPointId: number, deletedData: number, type: number) => {
    return await request.get({url: `/tb/screen-point/delete-collect?collectWorker=`
        + collectWorker + `&screenPointId=` + screenPointId + `&deletedData=` + deletedData + `&type=` + type})
  },

  setUserScreenPoint: async (userId: number, screenPointId: number, roleId: number) => {
    return await request.get({url: `/tb/screen-point/set-user-screenpoint?userId=` + userId + `&screenPointId=` + screenPointId + `&roleId=` + roleId })
  },

  // 更新筛查表中该筛查点的各组人员
  setCollect: async (collectUserIdStr: string, screenPointId: number, type:number) => {
    return await request.get({url: `/tb/screen-point/set-screenpoint-collect?collectUserIdStr=` + collectUserIdStr + `&screenPointId=` + screenPointId + `&type=` + type})
  },

  // 查询用户管理列表
  getUserPage: async (params: PageParam) => {
    return request.get({ url: '/tb/screen-point/user-page', params })
  },

  // 赋予用户角色
  assignUserRole: async (data: PermissionAssignUserRoleReqVO) => {
    return await request.post({ url: '/tb/screen-point/assign-user-role', data })
  },

  // 查询用户拥有的角色数组
  getUserRoleList: async (userId: number) => {
    return await request.get({ url: '/tb/screen-point/list-user-roles?userId=' + userId })
  },

  // 修改部门
  updateDept:  async (params: DeptVO) => {
    return await request.put({ url: '/tb/screen-point/dept-update', data: params })
  }


}

import request from '@/config/axios'

export interface DeptVO {
  id?: number
  name: string
  parentId: number
  status: number
  sort: number
  leaderUserId: number
  phone: string
  email: string
  createTime: Date
  regionLevel: number,
  districtCode: string
}

// 查询部门（精简)列表
export const getSimpleDeptList = async (): Promise<DeptVO[]> => {
  return await request.get({ url: '/system/dept/simple-list' })
}

// 查询部门列表
export const getDeptPage = async (params: PageParam) => {
  return await request.get({ url: '/system/dept/list', params })
}

// 查询部门详情
export const getDept = async (id: number) => {
  return await request.get({ url: '/system/dept/get?id=' + id })
}

// 新增部门
export const createDept = async (data: DeptVO) => {
  return await request.post({ url: '/system/dept/create', data: data })
}

// 修改部门
export const updateDept = async (params: DeptVO) => {
  return await request.put({ url: '/system/dept/update', data: params })
}

// 删除部门
export const deleteDept = async (id: number) => {
  return await request.delete({ url: '/system/dept/delete?id=' + id })
}

// 获得登录账号部门id
export async function getMyDeptId() {
  return await request.get({url: `/system/dept/get-my-dept`})
}

// 获得本部门及以下的部门列表
export async function getMyDeptList() {
  return await request.get({url: `/system/dept/get-my-deptList`})
}

export async function getAll() {
  return await request.get({url: `/tb/screen-district/all`})
}


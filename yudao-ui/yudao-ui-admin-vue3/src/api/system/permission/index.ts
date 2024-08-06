import request from '@/config/axios'

export interface PermissionAssignUserRoleReqVO {
  userId: number
  roleIds: number[]
}

export interface PermissionAssignRoleMenuReqVO {
  roleId: number
  menuIds: number[]
}

export interface PermissionAssignRoleDataScopeReqVO {
  roleId: number
  dataScope: number
  dataScopeDeptIds: number[]
}

// 查询角色拥有的菜单权限
export const getRoleMenuList = async (roleId: number) => {
  return await request.get({ url: '/system/permission/list-role-menus?roleId=' + roleId })
}

// 赋予角色菜单权限
export const assignRoleMenu = async (data: PermissionAssignRoleMenuReqVO) => {
  return await request.post({ url: '/system/permission/assign-role-menu', data })
}

// 赋予角色数据权限
export const assignRoleDataScope = async (data: PermissionAssignRoleDataScopeReqVO) => {
  return await request.post({ url: '/system/permission/assign-role-data-scope', data })
}

// 查询用户拥有的角色数组
export const getUserRoleList = async (userId: number) => {
  return await request.get({ url: '/system/permission/list-user-roles?userId=' + userId })
}

// 赋予用户角色
export const assignUserRole = async (data: PermissionAssignUserRoleReqVO) => {
  return await request.post({ url: '/system/permission/assign-user-role', data })
}

// 得到队长的角色id
export const getCapitalRoleId = async () => {
  return await request.get({url: `/system/permission/get-capital-roleid`})
}

// 得到采集组的角色id
export const getCollectRoleId = async () => {
  return await request.get({url: `/system/permission/get-collect-roleid`})
}

// 得到PPD组的角色id
export const getPPDRoleId = async () => {
  return await request.get({url: `/system/permission/get-ppd-roleid`})
}

// 得到DR/CT组的角色id
export const getDRCTRoleId = async () => {
  return await request.get({url: `/system/permission/get-drct-roleid`})
}

// 得到痰检组的角色id
export const getSputumRoleId = async () => {
  return await request.get({url: `/system/permission/get-sputum-roleid`})
}

// 得到实验组的角色id
export const getExperimentRoleId = async () => {
  return await request.get({url: `/system/permission/get-experiment-roleid`})
}

// 得到心电图组的角色id
export const getElectrocardiogramRoleId = async () => {
  return await request.get({url: `/system/permission/get-electrocardiogram-roleid`})
}

// 得到诊断组的角色id
export const getDiagnosisRoleId = async () => {
  return await request.get({url: `/system/permission/get-diagnosis-roleid`})
}


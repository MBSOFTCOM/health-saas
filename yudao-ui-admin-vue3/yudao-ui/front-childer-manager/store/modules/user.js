import config from '@/config'
import storage from '@/utils/storage'
import constant from '@/utils/constant'
import {
	login,
	logout,
	getInfo
} from '@/api/login'
import {
	setToken,
	removeToken
} from '@/utils/auth'
import {
	listSimpleDictData
} from '@/api/common'

const baseUrl = config.baseUrl

const user = {
	state: {
		id: 0, // 用户编号
		name: storage.get(constant.name),
		avatar: storage.get(constant.avatar),
		roles: storage.get(constant.roles),
		permissions: storage.get(constant.permissions),
	},

	mutations: {
		SET_ID: (state, id) => {
			state.id = id
		},
		SET_NAME: (state, name) => {
			state.name = name
			storage.set(constant.name, name)
		},
		SET_AVATAR: (state, avatar) => {
			state.avatar = avatar
			storage.set(constant.avatar, avatar)
		},
		SET_ROLES: (state, roles) => {
			state.roles = roles
			storage.set(constant.roles, roles)
		},
		SET_PERMISSIONS: (state, permissions) => {
			state.permissions = permissions
			storage.set(constant.permissions, permissions)
		},
	},

	actions: {
		// 登录
		Login({
			commit
		}, userInfo) {
			const username = userInfo.username.trim()
			const password = userInfo.password
			const captchaVerification = userInfo.captchaVerification
			return new Promise((resolve, reject) => {
				login(username, password, captchaVerification).then(res => {
					res = res.data;
					// 设置 token
					setToken(res)
					resolve()
				}).catch(error => {
					reject(error)
				})
			})
		},

		// 获取用户信息
		GetInfo({
			commit,
			state
		}) {
			return new Promise((resolve, reject) => {
				getInfo().then(res => {
					res = res.data; // 读取 data 数据
					const user = res.user
					const avatar = user?.avatar || '@/static/images/profile.jpg';
					const nickname = (user == null || user.nickname === "" || user.nickname ==
						null) ? "" : user.nickname
					if (res.roles && res.roles.length > 0) {
						commit('SET_ROLES', res.roles)
						commit('SET_PERMISSIONS', res.permissions)
					} else {
						commit('SET_ROLES', ['ROLE_DEFAULT'])
					}
					commit('SET_NAME', nickname)
					commit('SET_AVATAR', avatar)
					resolve(res)
				}).catch(error => {
					reject(error)
				})
			})
		},

		// 退出系统
		LogOut({
			commit,
			state
		}) {
			return new Promise((resolve, reject) => {
				logout(state.token).then(() => {
					commit('SET_ROLES', [])
					commit('SET_PERMISSIONS', [])
					removeToken()
					storage.clean()
					resolve()
				}).catch(error => {
					reject(error)
				})
			})
		},

		// 获取字典数据
		DictData({
			commit,
			state
		}, dictInfo) {
			return new Promise((resolve, reject) => {
				let resultObj = null;
				if (dictInfo.dictTypeStr) {
					// 1. 先从缓存中取下所有字典数据，如果没有则设置缓存并获取对应字典类型对应的数据
					let dictObject_storage = uni.getStorageSync('storage_' + dictInfo.dictTypeStr);
					if (dictObject_storage) {

						if (dictObject_storage) {
							dictObject_storage.forEach(item => {
								if (item.value === dictInfo.dictValue) {
									resultObj = item;
								}
							});
						}
						resolve(resultObj)
					} else {
						// 2.请求后端接口获取字典数据，并存入缓存
						try {
							listSimpleDictData(dictInfo.dictTypeStr).then(res => {
								if (res.data) {
									res.data.list.forEach(item => {
										if (item.value === dictInfo.dictValue) {
											resultObj = item;
										}
									});

									// 将字典数据加入缓存
									uni.setStorageSync('storage_' + dictInfo.dictTypeStr, res.data
										.list);

									resolve(resultObj)
								}
							});
						} catch (e) {
							reject(error)
						}
					}
				}
			});
		}
	}
}

export default user
import request from '@/utils/request'

export function getAgencyList() {
	return request({
		url: '/tb/agency/list',
		'method': 'GET'
	})
}

// 查询指定字典类型的字典数据列表
export function listSimpleDictData(dictTypeStr) {
	return request({
		url: '/system/dict-data/page?dictType=' + dictTypeStr,
		'method': 'GET'
	})
}

// 获取下级机构名称及自己当前机构名称 
export function getLoginUserAgencyList(){
	return request({
		url: '/tb/agency/loginUser/agencylist',
		'method': 'GET'
	})
}
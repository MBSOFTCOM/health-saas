import request from '@/utils/request'

// 查询诊断组分页
export function getScreenDiagnosisPage(params) {
	return request({
		url: '/tb/screen-diagnosis/page',
		'method': 'GET',
		data: params
	})
}

// 根据对应类型的体检项查询
export function getCheckSelectList(params) {
	return request({
		url: '/tb/screen-diagnosis/getCheckSelectList',
		'method': 'GET',
		data: params
	})
}

// 根据患者id获取最后一次诊断结果数据
export function getLastTime(params) {
	return request({
		url: '/tb/screen-diagnosis/last-time',
		'method': 'GET',
		data: params
	})
}

// 提交诊断结果
export function createScreenDiagnosis(data) {
	return request({
		url: '/tb/screen-diagnosis/create',
		'method': 'POST',
		data: data
	})
}

// 获取诊断组显示统计数据
export function getScreenDiagnosisData(data){
	return request({
		url: '/tb/screen-diagnosis/getScreenDiagnosisData',
		'method': 'POST',
		data: data
	})
}


// key、value互转
export function reverseObject(originalObj, transformFunction) {
	return Object.entries(originalObj).reduce((acc, [key, value]) => {
		acc[transformFunction(value)] = key; // 使用transformFunction转换值，并设置为新对象的键
		return acc;
	}, {});
}

// 诊断结果
export const outcomeMappings = {
	1: '病原学阳性',
	2: '病原学阴性',
	3: '无病原学结果',
	4: '耐药',
	5: '陈旧性肺结核',
	6: '暂时排除结核病',
	7: '潜伏感染者',
	8: '肺结核',
	9: '肺外结核'
};

// 治疗方案
export const treatmentProgramMappings = {
	1: '14天治疗管理',
	2: '住院治疗',
	3: '隔离治疗',
	4: '服药管理（复诊）',
	5: '服药管理（服药）',
	6: '服药管理（随访）',
	7: '停止治疗',
};

// 是否网报
export const reportMappings = {
	1: '是',
	0: '否'
};

// 是否进行预防性治疗
export const preventiveTreatmentMappings = {
	1: '是',
	0: '否'
};
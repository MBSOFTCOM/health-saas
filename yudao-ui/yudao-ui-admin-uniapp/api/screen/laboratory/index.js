import request from '@/utils/request'

// 查询实验组分页
export function getScreenExperimentPage(params) {
	return request({
		url: '/tb/screen-experiment/page',
		'method': 'GET',
		data: params
	})
}

// 查询实验室详情页面数据
export function getScreenExperimentDetail(params){
	return request({
		url: '/tb/screen-experiment/get',
		'method': 'GET',
		data: params
	})
}
export function getSputumOrder(params){
    // console.log(params)
	return request({
		url: '/tb/screen-experiment/getOrder',
		'method': 'GET',
		data: params
	})
}

// 提交实验结果
export function createScreenExperiment(data){
	return request({
		url: '/tb/screen-experiment/create',
		'method': 'POST',
		data: data
	})
}

// 获取实验组显示统计数据
export function getExperimentData(data){
	return request({
		url: '/tb/screen-experiment/getExperimentData',
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



// HIV抗体检测结果
export const hivTestResults = {
  1: '已知阳性',
  2: '新检测初筛阳性',
  3: '新检测确认阳性',
  4: '阴性',
  5: '拒查',
  6: '未提供'
};

// 耐药综合判定
export const drugResistanceMap = {
  1: '单耐利福平',
  2: '耐多药',
  3: '广泛耐药',
  4: '单耐异烟肼',
  5: '利福平与异烟肼均敏感'
};

// 结核分支杆菌药敏检测方法
export const tbDrugSensitivityTestMethods = {
  1: '分子生物学',
  2: '传统药敏试验'
};

// 菌种鉴定检测结果
export const speciesIdentificationResults = {
  1: '结核分枝杆菌复合群',
  2: '非结核分枝杆菌',
  3: '未查'
};

// 组织标本检测结果
export const tissueTestResults = {
  1: '组织学阳性',
  2: '仅病理学阳性',
  3: '阴性',
  4: '未查'
};

// 分子生物学
export const molecularBiologyResults = {
  1: '结核分枝杆菌核酸阳性',
  2: '未检出结核分枝杆菌',
  3: '不确定',
  4: '未查'
};

// 培养结果
export const cultureResults = {
  1: '阳性',
  2: '阴性',
  3: '污染',
  4: '未查'
}; 

// 涂片结果
export const smearResults = {
  1: '阳性',
  2: '阴性',
  3: '未查'
};

// 页面跳转状态
export const state=[{label:"新增",value:0},{label:"修改",value:1},{label:"详情",value:2},{label: '审核',value: 3}]
export const sex=[{label:"男",value:0},{label:"女",value:1}]
export const firstType=setDictByList(['重点人群','非重点人群','教职工'],[1,2,4])
export const moreType=setDictByList(['学生','老年人','教职工','密接者','糖尿病','僧尼','既往患者','HIV/AIDS'],[1,2,4,8,16,32,64,128])
export const ctOutcome=setDictByList(['正常','疑似结核','其他异常'],[0,1,2])
export const testDict=setDictByLabelList(['a','b','c'],2,0)
export const  sputumType=setDictByLabelList(['无痰','即时痰','晨痰','夜痰'],0,1)
export const  sputumCheck=setDictByLabelList(['即时痰','晨痰','夜痰'],1,1)
export const screenType=setDictByList(['常规筛查','新生入学筛查','应急筛查'],[1,2,3])
export const ppdOutcome=setDictByList(['水泡','双圈','坏死','淋巴管炎'],[1,2,3,4])

/** 筛查人群分类*/
export const personTypeForProcess=
	[
		{
			name:"学生"
		},
		{
			name:"教职工"
		},
		{
			name:"僧尼"
		},
		{
			name:"密切接触者"
		},
		{
			name:"老年人、糖尿病、HIV、AIDS、既往患结核病患者"
		},
		{
			name:"非重点人群"
		}
	]



/** 筛查类型*/
export const screenTypeMap={
	1:"常规筛查",
	2:"新生入学筛查",
	3:"应急筛查"
}

/**
 * 字典添加元素
 * @param dict 字典
 * @param label 标签
 * @param value 数值
 * @returns {*}
 */
export function setDict(dict,label,value){
	dict.push({label:label,value:value})
	return dict
}
export function setDictByList(labelList,valueList){
	if (labelList.length!=valueList.length){
		return null
	}else {
		let dict=[]
		for (let i = 0; i < labelList.length; i++) {
			setDict(dict,labelList[i],valueList[i])
		}
		// console.log("dict=",dict)
		return dict
	}
}

/**
 * 根据标签数组生成指定步长和指定起始下标位置的字典
 * @param labelList 标签数组
 * @param startIndex 开始索引
 * @param step 步长
 * @returns {null|*[]}
 */
export function setDictByLabelList(labelList,startIndex,step){
	if (!step){
		step=1
	}
	let length=labelList.length
	if (!labelList){
		return null
	}
	let value=[]
	let index=startIndex
	for (let i = 0; i < labelList.length; i++) {
		// index+(step*i)
		value.push(index+(step*i))
	}
	// console.log("value=",value)
	return setDictByList(labelList,value)
}
/**
 * 根据字典的标签获取值
 * @param dictName 字典
 * @param label 标签
 * @returns {string|null} 对应数值，无则返回null
 */
export function getValueByLabel(dictName,label){
   for (let item of dictName) {
      if (item.label===label){
         return item.value
      }
   }
   return null
}

/**
 * 跟据字典获取所有标签
 * @param dict
 * @returns {[]|null}
 */
export function getLabels(dict){
let labels=[]
   for (let item of dict) {
      labels.push(item.label)
   }
   if (labels.length>0){
      return labels
   }
   return null
}
/**
 * 跟据字典标签获取所有该item
 * @returns {{}|null}
 * @param dict 字典
 * @param label 标签
 */
export function getItemByLabel(dict,label){
   for (let item of dict) {
      if (item.label==label){
         return item
      }
   }
   return null
}

/**
 * 跟据字典值获取所有该item
 * @returns {{}|null}
 * @param dict 字典
 * @param value 值
 */
export function getItemByValue(dict,value){
	for (let item of dict) {
		if (item.value===value){
			return item
		}
	}
	return null
}

/**
 * 根据值获取标签
 * @param dict 字典
 * @param value
 */
export function getLabelByValue(dict,value){
   for (let item of dict) {
	   if (item.value===value){
		   // console.log(item.label)
		   return item.label
      }
   }
   return ''
}
export function getValueByKey(dict,key){
	return dict[key]
}
// 筛查状态
export const screenStatusMap={
	0:'未筛查',
	1:'已筛查',
	2:'筛查中'
}
// 性别
export const genderMap={
	1:'女',
	0:'男'
}
//是否新增、新生、已筛查、已注射
export const commonMap={
	1:'是',
	0:'否'
}
// 第一人群分类
export const firstTypeMap={
	1:'重点人群',
	2:'非重点人群',
	4:'教职工'
}
// 多人群分类
export const moreTypeMap={
	1: '学生',
	2: '老年人',
	4: '教职工',
	8: '密接者',
	16: '糖尿病',
	32: '僧尼',
	64: '既往患者',
	128:'HIV/AIDS'
}
// 注射方式
export const injectionWayMap={
	1:'PPD注射',
	2:'EC注射',
	3:'IGRA注射',
}
// 民族
export const nationMap={
	0: "汉族",
	  1: "蒙古族",
	  2: "回族",
	  3: "藏族",
	  4: "维吾尔族",
	  5: "苗族",
	  6: "彝族",
	  7: "壮族",
	  8: "布依族",
	  9: "朝鲜族",
	  10: "满族",
	  11: "侗族",
	  12: "瑶族",
	  13: "白族",
	  14: "土家族",
	  15: "哈尼族",
	  16: "哈萨克族",
	  17: "傣族",
	  18: "黎族",
	  19: "傈僳族",
	  20: "佤族",
	  21: "畲族",
	  22: "高山族",
	  23: "拉祜族",
	  24: "水族",
	  25: "东乡族",
	  26: "纳西族",
	  27: "景颇族",
	  28: "柯尔克孜族",
	  29: "土族",
	  30: "达斡尔族",
	  31: "仫佬族",
	  32: "羌族",
	  33: "布朗族",
	  34: "撒拉族",
	  35: "毛隆族",
	  36: "仡佬族",
	  37: "锡伯族",
	  38: "阿昌族",
	  39: "普米族",
	  40: "塔吉克族",
	  41: "怒族",
	  42: "乌孜别克族",
	  43: "俄罗斯族",
	  44: "鄂温克族",
	  45: "德昂族",
	  46: "保安族",
	  47: "裕固族",
	  48: "京族",
	  49: "塔塔尔族",
	  50: "独龙族",
	  51: "鄂伦春族",
	  52: "赫哲族",
	  53: "门巴族",
	  54: "珞巴族",
	  55: "基诺族"
}

export const items1= [
				{
					text: '重点人群',
					value: '1'
				},
				{
					text: '非重点人群',
					value: '2'
				},
				{
					text: '教职工',
					value: '4'
				}
			]

export const items2= [
				{
					text: '学生',
					value: '1'
				},
				{
					text: '老年人',
					value: '2'
				},
				{
					text: '教职工',
					value: '4'
				},
				{
					text: '密接者',
					value: '8'
				},
				{
					text: '糖尿病',
					value: '16'
				},
				{
					text: '僧尼',
					value: '32'
				},
				{
					text: '既往患者',
					value: '64'
				},
				{
					text: 'HIV/AIDS',
					value: '128'
				}
			]
export const items3=[
				{
					text: '密接者',
					value: '8'
				},
				{
					text: '糖尿病',
					value: '16'
				},
				{
					text: '既往患者',
					value: '64'
				},
				{
					text: 'HIV/AIDS',
					value: '128'
				}
			]
			
export const ethnic=[
  { value: 0, text: "汉族" },
  { value: 1, text: "蒙古族" },
  { value: 2, text: "回族" },
  { value: 3, text: "藏族" },
  { value: 4, text: "维吾尔族" },
  { value: 5, text: "苗族" },
  { value: 6, text: "彝族" },
  { value: 7, text: "壮族" },
  { value: 8, text: "布依族" },
  { value: 9, text: "朝鲜族" },
  { value: 10, text: "满族" },
  { value: 11, text: "侗族" },
  { value: 12, text: "瑶族" },
  { value: 13, text: "白族" },
  { value: 14, text: "土家族" },
  { value: 15, text: "哈尼族" },
  { value: 16, text: "哈萨克族" },
  { value: 17, text: "傣族" },
  { value: 18, text: "黎族" },
  { value: 19, text: "傈僳族" },
  { value: 20, text: "佤族" },
  { value: 21, text: "畲族" },
  { value: 22, text: "高山族" },
  { value: 23, text: "拉祜族" },
  { value: 24, text: "水族" },
  { value: 25, text: "东乡族" },
  { value: 26, text: "纳西族" },
  { value: 27, text: "景颇族" },
  { value: 28, text: "柯尔克孜族" },
  { value: 29, text: "土族" },
  { value: 30, text: "达斡尔族" },
  { value: 31, text: "仫佬族" },
  { value: 32, text: "羌族" },
  { value: 33, text: "布朗族" },
  { value: 34, text: "撒拉族" },
  { value: 35, text: "毛隆族" },
  { value: 36, text: "仡佬族" },
  { value: 37, text: "锡伯族" },
  { value: 38, text: "阿昌族" },
  { value: 39, text: "普米族" },
  { value: 40, text: "塔吉克族" },
  { value: 41, text: "怒族" },
  { value: 42, text: "乌孜别克族" },
  { value: 43, text: "俄罗斯族" },
  { value: 44, text: "鄂温克族" },
  { value: 45, text: "德昂族" },
  { value: 46, text: "保安族" },
  { value: 47, text: "裕固族" },
  { value: 48, text: "京族" },
  { value: 49, text: "塔塔尔族" },
  { value: 50, text: "独龙族" },
  { value: 51, text: "鄂伦春族" },
  { value: 52, text: "赫哲族" },
  { value: 53, text: "门巴族" },
  { value: 54, text: "珞巴族" },
  { value: 55, text: "基诺族" }
]


export const collectSymptoms = [
	{
		text: '咳嗽、咳痰(超过一周)',
		value: 1
	},
	{
		text: '血痰或咯血',
		value: 2
	},
	{
		text: '发热',
		value: 3
	},
	{
		text: '胸痛',
		value: 4
	},
	{
		text: '夜间盗汗',
		value: 5
	},
	{
		text: '食欲不振',
		value: 6
	},
	{
		text: '乏力',
		value: 7
	},
	{
		text: '体重减轻(超过6斤)',
		value: 8
	},
	{
		text: '有无卡痕',
		value: 9
	}
]
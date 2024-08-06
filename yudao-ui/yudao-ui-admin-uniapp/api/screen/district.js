import dbUtils from "@/uni_modules/zjy-sqlite-manage/components/zjy-sqlite-manage/dbUtils.js" //sqlite-manage插件
import request from '@/utils/request'
import {dbName,promise,tbScreenDistrict,emptyData} from "@/utils/sqlite";
/**
 * 获取所有区划数据
 */
export const getDistrictData=()=>{
	return request({
		url: '/tb/screen-district/all',
		method: 'GET'
	})
}
/**
 * 添加区划数据
 * @param data {[]}
 */
export const  insData=(data)=>{
	for (let i = 0; i < data.length; i++) {
		dbUtils.addTabItem(dbName,tbScreenDistrict,data[i])
	}
}
/**
 * 覆盖区划数据
 */
export const coverData=()=>{
	console.log(2);
	emptyData(tbScreenDistrict).then(()=>{
		console.log(3);
		getDistrictData().then((res)=>{
			console.log(4);
			insData(res.data)
		})
	})
}
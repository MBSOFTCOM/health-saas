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
 * 获取所有区划数据
 */
export const getDistrictDataFromLocal=async()=>{
	let sql=`select * from tb_screen_district`
	return promise(dbName,sql)
}
/**
 * 添加区划数据
 * @param data {[]}
 */
export const  insData=async(data)=>{
	// console.log("添加数据");
	// console.log(data);
	for (let i = 0; i < data.length; i++) {
		dbUtils.addTabItem(dbName,tbScreenDistrict,data[i])
	}
	console.log("数据写入完成");
}
/**
 * 覆盖区划数据
 */
export const coverData=()=>{
	emptyData(tbScreenDistrict).then(()=>{
		getDistrictData().then((res)=>{
			insData(res.data)
		})
	})
}
import dbUtils from "@/uni_modules/zjy-sqlite-manage/components/zjy-sqlite-manage/dbUtils.js" //sqlite-manage插件
import request from '@/utils/request'
import {dbName,promise,tbScreenConsume,emptyData} from "@/utils/sqlite";
/**
 * 根据类型获取可用的试剂批号数据
 */
export const getConsumeData = async () => {
    return request({
        url: `/tb/screen-consume/getUsable`,
        method: 'GET',
    })
}

/**
 * 将试剂列表写入数据库
 * @param {[Object]} data
 */
export const insData = async (data) => {
    if (data.length<=0){
        return
    }
    for (let i = 0; i < data.length; i++) {
        dbUtils.addTabItem(dbName,tbScreenConsume,data[i])
    }
}
/**
 * 覆盖试剂数据
 * @param {[Object]} data
 */
export const coverData = async (data) => {
		console.log(222);
    if (!data || data.length==0){
        return
    }
    emptyData(tbScreenConsume).then(async ()=>{
        await insData(data)
        let local=await getDataFromLocal()
        console.log(local)
    })
}
/**
 * 获取可用的试剂批号数据，并覆盖试剂数据
 */
export const coverDataAuto = async () => {
    let list=await getConsumeData()
    await coverData(list.data)
	let local=await getDataFromLocal()
	console.log(local)
}
export const getDataFromLocal = async () => {
    let sql=`select * from ${tbScreenConsume}`
    return promise(dbName,sql)
}
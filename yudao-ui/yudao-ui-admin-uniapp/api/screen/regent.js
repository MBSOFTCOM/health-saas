import dbUtils from "@/uni_modules/zjy-sqlite-manage/components/zjy-sqlite-manage/dbUtils.js" //sqlite-manage插件
import request from '@/utils/request'
import {dbName,promise,tbScreenReagent,emptyData} from "@/utils/sqlite";
/**
 * 根据类型获取试剂数据,type:number
 * @param {Object} data
 */
export const getRegentData = async (data) => {
    return request({
        url: `/tb/screen-reagent/getRegent`,
        method: 'GET',
        params: data,
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
        dbUtils.addTabItem(dbName,tbScreenReagent,data[i])
    }
}
/**
 * 覆盖试剂数据
 * @param {[Object]} data
 */
export const coverData = async (data) => {
	console.log(111);
   if (!data || data.length==0){
       return
   }
  emptyData(tbScreenReagent).then(async ()=>{
      await insData(data)
      let local=await getDataFromLocal()
      console.log(local)
  })
}
/**
 * 根据类型获取试剂数据,type:number，并覆盖试剂数据
 * @param {[Object]} data
 */
export const coverDataAuto = async (data) => {
    let list=await getRegentData(data)
    await coverData(list.data)
	let local=await getDataFromLocal()
	console.log(list)
}
export const getDataFromLocal = async () => {
  let sql=`select * from ${tbScreenReagent}`
    return promise(dbName,sql)
}
import dbUtils from "@/uni_modules/zjy-sqlite-manage/components/zjy-sqlite-manage/dbUtils.js" //sqlite-manage插件
import request from '@/utils/request'
import {dbName,promise,tbScreenConsume,emptyData, tbScreenConsumeRecord} from "@/utils/sqlite";
import {tbScreenPpd} from "../../utils/sqlite";
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
/**
 * 根据试剂id获取消耗序位最小的试剂批号
 * @param {number} regentId
 */
export const  getFirstConsume=async(regentId)=>{
	let sql=`select c.*,r.changeNumber from ${tbScreenConsume} c left join ${tbScreenConsumeRecord} r on r.consumeId=c.id where reagentId=${regentId} order by consumeOrder DESC limit 1`
	// console.log(sql);
	return promise(dbName,sql)
}

/**
 * 根据试剂批次明细id分别统计已使用人次
 * @return {*}
 */
export const staticsConsumeGroupByReagentId = () => {
  let sql=`select p.reagentId,ifnull(count(*)) num,c.currentNumber from ${tbScreenPpd} p left join ${tbScreenConsume} c on p.reagentId = c.id group by p.reagentId`
    return promise(dbName,sql)
}
/**
 * 根据试剂批次明细id分别统计已使用人次
 * @param {number} reagentId
 * @return {*}
 */
export const staticsConsumeById = async(reagentId) => {
	// console.log(111);
	let sql=`select reagentId,ifnull(count(*),0) num from ${tbScreenPpd} where reagentId=${reagentId}`
    return promise(dbName,sql)
}
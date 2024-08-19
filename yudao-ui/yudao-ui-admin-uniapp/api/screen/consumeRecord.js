import dbUtils from "@/uni_modules/zjy-sqlite-manage/components/zjy-sqlite-manage/dbUtils.js" //sqlite-manage插件
import request from '@/utils/request'
import {dbName,promise,tbScreenConsume,tbScreenConsumeRecord,emptyData} from "@/utils/sqlite";

/**
 * 批量向本地数据插入试剂消耗明细数据
 * @param {[]}data
 */
export const insRecord = (data) => {
    if (!data || data.length==0){
        return
    }
    for (let i = 0; i < data.length; i++) {
        data.type=4
        dbUtils.addTabItem(dbName,tbScreenConsumeRecord,data[i])
    }
}
/**
 * 获取本地所有试剂的消耗记录数据
 * @return {Promise<*>}
 */
export const selectLocalData=async ()=>{
    let sql=`select * from ${tbScreenConsumeRecord}`
    return promise(dbName,sql)
}
/**
 * 获取本地试剂批号的消耗记录数量
 * @return {Promise<*>}
 */
export const selectLocalRecordByRegentId=async (reagentId)=>{
    let sql=`select ifnull(changeNumber,0) num from ${tbScreenConsumeRecord} where consumeId=${reagentId}`
    return promise(dbName,sql)
}
/**
 * 根据试剂批号的消耗明细更细消耗数量
 * @param {number} num
 * @param {number} reagentId
 * @return {Promise}
 */
export const updateRecordNum=async(num,reagentId,updater,updateTime)=>{
	let sql=`update ${tbScreenConsumeRecord} set changeNumber=${num} ,updater=${updater} ,updateTime='${updateTime}' where consumeId=${reagentId}`
	// console.log(sql);
	return promise(dbName,sql)
}
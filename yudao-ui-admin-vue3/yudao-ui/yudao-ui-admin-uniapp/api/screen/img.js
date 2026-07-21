import {dbName, getMaxScreenOrder, tbScreenChestRadiograph,tbScreenImages, promise, tbScreenSum} from "@/utils/sqlite";

/**
 * 添加记录到图片表
 * @param util 全局dbUtils，this.$dbUtils
 * @param data 记录对象
 * @returns {Promise<void>}
 */
export async function insertImg(util,data){
 await util.addTabItem(dbName,tbScreenImages,data)
}
/**
 * 更新图片表
 * @param data 记录对象
 * @returns {Promise<void>}
 */
export async function updateImg(data){
 let sql=`update ${tbScreenImages} set path='${data.path}' where screenOrder=${data.screenOrder} and idNum=${data.idNum} and screenType=${uni.$screenType} and type=${data.type}`
 // console.log(sql);
 await promise(dbName,sql)
}

/**
 *
 * @returns {Promise<void>}
 */
export async function getAll(){
 let sql=`select * from ${tbScreenImages}`
 return await promise(dbName,sql)
}
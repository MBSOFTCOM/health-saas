import dbUtils from "@/uni_modules/zjy-sqlite-manage/components/zjy-sqlite-manage/dbUtils.js" //sqlite-manage插件
import request from '@/utils/request'
import {dbName,promise,tbScreenReagent,emptyData} from "@/utils/sqlite";

/**
 * 获取所有表名
 * @return {*}
 */
export const getTableName = () => {
    let sql=`SELECT name FROM sqlite_master WHERE type='table' ORDER BY name`
    // console.log(sql)
    return promise(dbName,sql)
}
/**
 * 获取所有字段信息
 * @param {string}tableName 表名
 * @return {[]} 
 */
export const getFields = (tableName) => {
    let sql=`PRAGMA table_info('${tableName}')`
    // console.log(sql)
    return promise(dbName,sql)
}
/**
 * 获取所有数据
 * @param {string} table 表名
 * @return {[]}
 */
export const getList = (table) => {
    let sql=`select * from ${table}`
    return promise(dbName,sql)
}
/**
 * 获取分页
 * @param {string}table
 * @param {{pageNo:number,pageSize:number}} param
 * @return {[]}
 */
export const getPage = (table,param) => {
    let pageParam=setPageParam(param)
    let sql=`select * from ${table} limit ${pageParam.pageNo},${pageParam.pageSize}`
    // console.log(sql)
    return promise(dbName,sql)
}
/**
 * 更新分页参数，用于分页sql
 * @param {{pageNo:number,pageSize:number}} pageParam
 * @return {{pageNo:number,pageSize:number}}
 */
export const setPageParam = (pageParam) => {
    let param=Object.assign({},pageParam)
    param.pageNo=pageParam.pageSize * (pageParam.pageNo-1)
    return param
}
/**
 * 获取分页的数据数量
 * @param {string}table
 * @param {{pageNo:number,pageSize:number}} param
 * @return {[]}
 */
export const countPage = (table,param) => {
    let pageParam=setPageParam(param)
    let sql=`select ifnull(count(*),0) num from ${table} limit ${pageParam.pageNo},${pageParam.pageSize}`
    return promise(dbName,sql)
}
/**
 * 获取所有数据的数量
 * @param {string} table 表名
 * @return {[]}
 */
export const countList = (table) => {
    let sql=`select ifnull(count(*),0) num from ${table}`
    return promise(dbName,sql)
}
/**
 * @param {string} 表名
 * @param {string} 身份证
 * @param {string} 筛查次序（传null则不判断次序）
 * @param {string} 次序的字段名
 * @param {number} 数据状态 （1-新增 2-修改）
 */
export const setStatusFlag=(table,idNum,order,orderField,statusFlagSet)=>{
	let sql=`update ${table} set statusFlag=${statusFlagSet} where idNum=${idNum}`
	if(order){
		sql+=` ${orderField}=${order}`
	}
	console.log(sql);
	return  promise(dbName,sql)
}
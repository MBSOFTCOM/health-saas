import {promise, tbScreenElectrocardiogram} from "./sqlite";
import dbUtils from "../uni_modules/zjy-sqlite-manage/components/zjy-sqlite-manage/dbUtils";

const dbName = 'tb_screen'
const tbScreenSum = "tb_screen_sum"

//根据人员id,筛查类型，筛查年份修改数据
export function updateOne(setData,id,year,screenType) {
		let sql = '';
		let dataKeys = Object.keys(setData)
		let setStr = ''
		dataKeys.forEach((item, index) => {
			// console.log(setData[item])
			setStr += (
				`${item} = ${JSON.stringify(setData[item])}${dataKeys.length - 1 !== index ? "," : ""}`)
		})
		
	sql = `UPDATE ${tbScreenSum} SET ${setStr} WHERE personId = ${id} AND year= ${year} AND screenType= ${screenType}`
  // console.log("SQL:" + sql);
	if(sql!=''){
		// console.log(111);
		return new Promise((resolve, reject) => {
		  plus.sqlite.selectSql({
		    name: dbName,
		    sql: sql,
		    success(e) {
		      resolve(e);
		    },
		    fail(e) {
				console.log(e);
		      reject(e);
		    }
		  });
		});
	}
}

/**
 * 查询汇总表中是否存在记录
 * @param params {year:number,personId:number,screenId:number}
 * @returns {Promise<*>}
 */
export function selectOne(params) {
	let sql=`select id from ${tbScreenSum} where personId=${params.personId} and screenType=${params.screenType} and year=${params.year}`
	// console.log(sql)
	return promise(dbName,sql)
}

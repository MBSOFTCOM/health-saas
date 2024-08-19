import {promise} from "@/utils/sqlite";

export const dbName = 'tb_screen'
export const tbScreenImages = "tb_screen_images"

////根据人员id,筛查类型，筛查年份修改数据
const  updateOne = (setData,id,year,screenType,order,type)=> {
		let sql = '';
		let dataKeys = Object.keys(setData)
		let setStr = ''
		dataKeys.forEach((item, index) => {
			// console.log(setData[item])
			setStr += (
				`${item} = ${JSON.stringify(setData[item])}${dataKeys.length - 1 !== index ? "," : ""}`)
		})
		
	sql = `UPDATE ${tbScreenImages} SET ${setStr} WHERE personId = ${id} AND year= ${year} AND screenType= ${screenType} AND screenOrder = ${order} AND type=${type}`
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
 * 删除图片表数据
 * @param idNum 身份证
 * @param year 年度
 * @param screenType 筛查类型
 * @param order 次序
 * @param type 图片类型
 */
const deleteOne=(idNum,year,screenType,order,type)=>{
	let sql=`delete from ${tbScreenImages} WHERE idNum = ${idNum} AND year= ${year} AND screenType= ${screenType} AND screenOrder = ${order} AND type=${type}`
	return promise(dbName,sql)
}

//查看数据
const  selectOne = (id,year,screenType,order,type)=> {
	
	let sql = `SELECT * FROM ${tbScreenImages} WHERE personId = ${id} AND year= ${year} AND screenType= ${screenType} AND screenOrder = ${order} AND type=${type}`
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

export default {
	updateOne, ////根据人员id,筛查类型，筛查年份修改数据
	selectOne,
	deleteOne
}
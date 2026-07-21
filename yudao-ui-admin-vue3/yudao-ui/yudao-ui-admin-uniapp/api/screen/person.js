import {dbName,promise, tbScreenPerson} from "@/utils/sqlite";
import dbUtils from "@/uni_modules/zjy-sqlite-manage/components/zjy-sqlite-manage/dbUtils.js" //sqlite-manage插件
/**
 * 根据身份证号查询待筛查人员信息
 * @param idNum
 * @returns {*}
 */
export const getPersonByIdNum = (idNum) => {
	let sql=`select * from ${tbScreenPerson} where idNum=${idNum}`
	return promise(dbName,sql)
}
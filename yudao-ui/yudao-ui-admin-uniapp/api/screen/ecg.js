import {tbScreenElectrocardiogram,dbName} from "@/utils/sqlite";
import {promise} from "../../utils/sqlite";

/**
 * 获取某患者某筛查类型最新的心电图记录id
 * @param personId 患者id
 * @param screenType 筛查类型
 * @returns {Promise<*>}
 */
export const getLastOne = async (personId,screenType) => {
    if (screenType!=null){
        screenType=uni.$screenType
    }
	let data= await getMaxId(personId,screenType)
    let sql=`select id,screenOrder,screenTime from ${tbScreenElectrocardiogram} where id=${data[0].id}`
    // console.log(sql)
    return promise(dbName,sql)
}
const getMaxId = async (personId,screenType) => {
  let sql=`select max(id) id from ${tbScreenElectrocardiogram} where personId=${personId} and screenType=${screenType}`
    return promise(dbName,sql)
}
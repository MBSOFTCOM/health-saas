import {
    dbName,
    getMaxScreenOrder,
    tbScreenChestRadiograph,
    promise,
    tbScreenSum,
    tbScreenPerson
} from "../../utils/sqlite";
import {ctOutcome, getValueByLabel} from "../../utils/dict";

/**
 * 根据parentId获取ct记录,不传入screenNum则默认查找最后一次的
 * @param parentId 患者主键
 * @param screenNum 筛查次数
 * @returns {Promise<*>}
 */
export async function getCTById(parentId,screenNum){
    let sql=''
    if (!screenNum){
        let data=await getMaxScreenOrder(tbScreenChestRadiograph,parentId,null)
        screenNum=data
    }
    sql=`select * from ${tbScreenChestRadiograph}  where personId=${parentId} and screenOrder=${screenNum} and screenType=${uni.$screenType}`
    // console.log("sql="+sql)
    let result= await promise(dbName,sql)
    return result[0]
}

/**
 * 更新dr、ct记录信息
 * @param id 记录id
 * @param data 要修改的信息对象
 * @param util dbUtils
 * @returns {Promise<void>}
 */
export async function updateDr(id,data,util){
    await util.updateSQL(dbName, tbScreenChestRadiograph, data, 'id', id)
}

/**
 *插入汇总表
 * @param data
 * @param util
 * @returns {Promise<void>}
 */
export async function insertToSum(data,util){
    util.addTabItem(dbName,tbScreenSum,data)
}
/**
 * 修改已有记录
 * @param personId 群众id
 * @param data
 * @param util 全局dbutils
 * @returns {Promise<void>}
 */
export async function updateSum(personId,data,util){
    util.updateSQL(dbName,tbScreenSum,data,'personId',personId)
}

/**
 * 获取所有排序号和筛查时间
 * @param personId 患者主键
 * @returns {Promise<*>}
 */
export async function getOrderAndTime(personId){
    let sql=`select screenOrder,screenTime,outcome from ${tbScreenChestRadiograph} where personId=${personId} and screenType=${uni.$screenType} order by screenOrder`
    // console.log("sql="+sql)
    let data=await promise(dbName,sql)
    return data
}
/**
 * 获取患者列表分页
 * @param pageNo 分页，页码
 * @param pageSize 分页，页条数
 * @param name 患者姓名
 * @param idNum 身份证号
 * @param firstType 第一人群分类
 * @param moreType 多人群分类
 * @param startTime 筛查开始时间
 * @param endTime 筛查结束时间
 * @param screenId 筛查编号
 * @param timeCol 筛查时间字段名
 */
export async function getPatientPage(pageNo, pageSize, name, idNum, firstType,moreType, startTime, endTime,screenId,timeCol) {
    // console.log("开始分页")
    let result = {
        total: 0,
        data: []
    }
    let sizeIndex = 0
    let querySql = `select p.* from ${tbScreenPerson} p left join ${tbScreenSum} c on p.id=c.personId `
    let countSql = `select count(DISTINCT p.id) num from ${tbScreenPerson} p left join  ${tbScreenSum} c on c.personId =p.id `
    if (pageNo < 1 || pageSize < 1) {
        return new Promise((resolve, reject) => {
            reject("分页参数错误")
        });
    }
    sizeIndex = (pageNo - 1) * pageSize
    let queryWhere = ' '
    if (name || idNum || (firstType && firstType.length>0) || (moreType && moreType.length>0) || screenId || (startTime && endTime)) {
        queryWhere = ` where`
        if (name) {
            queryWhere += ` name like '%${name}%' and `
        }
        if (idNum) {
            queryWhere += ` idNum like '%${idNum}%' and `
        }
        if ((firstType && firstType.length>0) || (moreType && moreType.length>0)){
            queryWhere+='('
            if (firstType && firstType.length>0) {
                queryWhere += ` firstType in (${firstType}) and `
            }
            if (moreType && moreType.length>0) {
                queryWhere += ` moreType in (${moreType}) and `
            }
            if (queryWhere.includes('and  moreType')){
                queryWhere=queryWhere.replace('and  moreType','or  moreType')
            }
            if (queryWhere.trim().endsWith('and')){
                console.log("进入判断")
                queryWhere=queryWhere.trim()
                queryWhere=queryWhere.replace(/\band\b$/,') and ')
                console.log("querySql=",querySql+queryWhere)
            }
        }
        if (screenId) {
            queryWhere += ` p.screenId = '${screenId}' and `
        }
        if ((startTime && endTime)) {
            queryWhere += ` date(c.${timeCol}) between date('${startTime}') and  date('${endTime}') and `
        }
        queryWhere = queryWhere.trim()
        if (queryWhere.endsWith('and')) {
            let lastIndexOf = queryWhere.lastIndexOf('and');
            queryWhere = queryWhere.replace(/\band\b$/, "")

        }
    }

    // 统计total数量
    countSql += queryWhere
    // console.log("count===="+countSql)
    let total = await promise(dbName, countSql)
    result.total = total[0].num
    // console.log("total="+total)

    // 数据查询
    queryWhere += ` order by id desc`
    queryWhere += ` limit ${sizeIndex},${pageSize}`
    querySql += queryWhere

    console.log("query="+querySql)
    let data = await promise(dbName, querySql)
    result.data = data

    return result
}

export async function getPersonWithOutCome(){

}
export async function getTypeStatistics(){
    let normal=getValueByLabel(ctOutcome,'正常')
    let suspected=getValueByLabel(ctOutcome,'疑似结核')
    let sql=`select count(case when c.outcome=${normal} then 1 end) normal,count(case when c.outcome=${suspected} then 1 end) suspected from ${tbScreenChestRadiograph} c  join ${tbScreenSum} s on s.personId=c.personId and s.chestRadiographNum=c.screenOrder where c.screenType=${uni.$screenType}`
    let data=await promise(dbName,sql)
    // console.log("count=",data)
    return data[0]
}
export async function getTimeStatistics(timeType){
    let sql=``
    switch (timeType) {
        case 'year':sql=`SELECT count(DISTINCT personId) num FROM ${tbScreenChestRadiograph} WHERE screenType=${uni.$screenType} and  strftime('%Y', date(screenTime)) = strftime('%Y', date('now'))` ;break
        case 'month':sql=`SELECT count(DISTINCT personId) num FROM ${tbScreenChestRadiograph} WHERE screenType=${uni.$screenType} and strftime('%Y-%m', date(screenTime)) = strftime('%Y-%m', date('now'))` ;break
        case 'day':sql=`SELECT count(DISTINCT personId) num FROM ${tbScreenChestRadiograph} WHERE screenType=${uni.$screenType} and date(screenTime) = date('now')` ;break
    }
    let data=await promise(dbName,sql)
    return data[0].num
}

export async function currentDayMonthYearStatistics(){
    let result={}
    let yearNum= await getTimeStatistics('year')
    let monthNum= await getTimeStatistics('month')
    let dayNum= await getTimeStatistics('day')
    result.yearNum=yearNum
    result.monthNum=monthNum
    result.dayNum=dayNum
    // console.log("result=",result)
    return result
}
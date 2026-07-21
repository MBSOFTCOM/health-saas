/**
* 显示消息提示框
* @param content 提示的标题
*/
import {
  chronic,
  closer,
  monkFifteen,
  monkFive,
  monkFourteen, normalFifteen, normalFive, normalFourteen,
  studentFifteen,
  studentFive,
  studentFourteen,
  teacher
} from "./dictData";
import {promise,dbName} from './sqlite'
export const TYPE_MS='ms'
export const TYPE_S='s'

/**
 * 获取当前时间时间戳
 * @param type string
 * @return number
 */
export function getTimeStamp(type){
  if (!type){
    type=TYPE_MS
  }
  let time=new Date().getTime()
  if (type=='s'){
    time=Math.floor(time/1000)
  }
  return time
}
export function toast(content) {
  uni.showToast({
    icon: 'none',
    title: content
  })
}

/**
* 显示模态弹窗
* @param content 提示的标题
*/
export function showConfirm(content) {
  return new Promise((resolve, reject) => {
    uni.showModal({
      title: '提示',
      content: content,
      cancelText: '取消',
      confirmText: '确定',
      success: function(res) {
        resolve(res)
      }
    })
  })
}

/**
 * 获取某个表中小于 10 0000的id，如果没有就返回0
 * @param table string
 */
export async function selectMaxIdleOneStandard(table) {
  let sql=`select ifnull(max(id),0) num from ${table} where id < 100000`
  return await promise(dbName,sql)
}

/**
 *
 * @param table
 * @return {number}
 */
export async function getNextId(table){
  let idObj=await selectMaxIdleOneStandard(table)
  return idObj[0].num+1
}
/**
* 参数处理
* @param params 参数
*/
export function tansParams(params) {
  let result = ''
  for (const propName of Object.keys(params)) {
    const value = params[propName]
    var part = encodeURIComponent(propName) + "="
    if (value !== null && value !== "" && typeof (value) !== "undefined") {
      if (typeof value === 'object') {
        for (const key of Object.keys(value)) {
          if (value[key] !== null && value[key] !== "" && typeof (value[key]) !== 'undefined') {
            let params = propName + '[' + key + ']'
            var subPart = encodeURIComponent(params) + "="
            result += subPart + encodeURIComponent(value[key]) + "&"
          }
        }
      } else {
        result += part + encodeURIComponent(value) + "&"
      }
    }
  }
  return result
}

/**
 * 取出包装字符串中的系数
 * @param str 包装字符串
 */
export function extractFirstIntegerFromString(str) {
  if(str){
    const regex = /\d+/; // 匹配第一次出现的数字
    const match = str.match(regex);

    if (!match) {
      return 1; // 如果没有找到整数，则返回默认值
    }

    // 提取第一次匹配到的数字并转换为整数
    return parseInt(match[0], 10);
  }

  return 1;
}

/**
 * 判断不同人群的筛查流程
 * @param age 年龄
 * @param type 多人群分类
 * @param firstType 第一人群分类
 * @param name 患者名称
 * @param key 当前做完分组
 * @param isBad 根据当前记录是否异常，异常传入false，返回下一组,正常返回空字符
 * @returns {Map<string, string>|*}
 */
export function parsePatientType(age,type,firstType,name,key,isBad){
  // console.log(`${name}-key=${key}-isBad=${isBad}`)

  let mapList=[]
  let map=new Map([])
  let str='请医生核实患者情况判断'

  // 学生
  if (type.includes(1)){
    // console.log("学生")
    if(age<=5 && age>=0){
      if (isBad){
        return ''
      }
      mapList.push(0)
      map= studentFive
    }else if (age>=6 && age<=14){
      map= studentFourteen
      mapList.push(1)
    }else {
      map= studentFifteen
      mapList.push(2)
    }
  }
  // 教职工
  if (type.includes(4)){
    // console.log("教职工")
    map= teacher
    mapList.push(3)
  }
  // 僧尼
  if (type.includes(32)){
    // console.log("僧尼")
    if(age<=5 && age>=0){
      map= monkFive
      mapList.push(4)
    }else if (age>=6 && age<=14){
      map= monkFourteen
      mapList.push(5)
    }else {
      map= monkFifteen
      mapList.push(6)
    }
  }
  // 密接者
  if (type.includes(8)){
    // console.log("密接者")
    map= closer
    mapList.push(7)
  }
  // 糖尿病、既往患者
  if ([2,16,64,128].some(item=> type.includes(item))){
    // console.log(`${name}--${key}---${chronic.get(key)}=====糖尿病、老年人、既往患者`)
    // console.log("糖尿病、老年人、既往患者")
    map= chronic
    mapList.push(8)
  }
  // 非重点人群
  if (firstType && firstType==2){
    // console.log("非重点")
    if(age<=5 && age>=0){
      map= normalFive
      mapList.push(9)
    }else if (age>=6 && age<=14){
      map= normalFourteen
      mapList.push(10)
    }else {
      map= normalFifteen
      mapList.push(11)
    }
  }
  return mapList
}

/**
 * 根据已完成分组、当前所在分组检查结果异常状态判断下一步流程
 * @param curFinish 已完成分组
 * @param dictData 流程obj
 * @param isError true为异常，false 为正常
 * @returns {string|*}
 */
export function parseNext(curFinish,dictData,isError){
  // console.log(`${curFinish}-${isError}`)
  if(curFinish==null){
    return `【${dictData.type}】: 还没有检测记录或已诊断结束，是否开始新的筛查`
  }
  let map=dictData.value
  let keys=map.keys()
  let keyList=[]
  for (let i = 0; i < keys.length; i++) {
    // console.log(keys[i])
    keyList.push(keys[i])
  }
  let value=map.get(curFinish)
  let initVal=map.get(null)
  if (!keyList.includes(curFinish)){
    let list=Object.assign([],initVal)
    let str=list.join(",")
    return `【${dictData.type}】: 确认患者做完【${str}】`
  }
  // 当前完成的分组是并行流程部分
  if(Array.isArray(initVal) && initVal.includes(curFinish)){
    let list=Object.assign([],initVal)
    // 将list中值等于curFinish的元素去掉
    list=list.filter(item=> item!=curFinish)
    let str=list.join(",")

    return `【${dictData.type}】: 确认患者做完【${str}】后进入【${value}】,如果这些分组检测结果都正常则进入【诊断组】`
  }else if (["采集组","ppd组","胸片组"].includes(curFinish)){
    // 判断已做分组的结果是否异常
    if (isError){
      return map.get(curFinish)
    }else {
      return `【${dictData.type}】: 诊断组`
    }
  }else {
    return `【${dictData.type}】: ${map.get(curFinish)}`
  }
}

export function parsePatientTypeAfterCT(age,type,firstType,name,key){
  let map=new Map([])
  if (type.includes(1)){
    // console.log("学生")
    if(age<=5 && age>=0){
      map= studentFive
    }else if (age>=6 && age<=14){
      map= studentFourteen
    }else {
      map= studentFifteen
    }
  }
  // 教职工
  else if (type.includes(4)){
    // console.log("教职工")
    map= teacher
  }
  // 僧尼
  else if (type.includes(32)){
    // console.log("僧尼")
    if(age<=5 && age>=0){

      map= monkFive
    }else if (age>=6 && age<=14){
      map= monkFourteen
    }else {
      map= monkFifteen
    }
  }
  // 密接者
  else if (type.includes(8)){
    // console.log("密接者")
    map= closer
  }
  // 糖尿病、既往患者
  else if ([2,16,64].some(item=> type.includes(item))){
    // console.log(`${name}--${key}---${chronic.get(key)}=====糖尿病、老年人、既往患者`)
    // console.log("糖尿病、老年人、既往患者")
    map= chronic
  }
  // 非重点人群
  else if (firstType && firstType==2){
    // console.log("非重点")
    if(age<=5 && age>=0){
      map= normalFive
    }else if (age>=6 && age<=14){
      map= normalFourteen
    }else {
      map= normalFifteen
    }
  }
  return map
}

// // 注册应用启动时的全局事件监听器
// document.addEventListener("plusready", function() {
//     // 监听全局变量的变化
//     setInterval(function() {
//         if (uni.$person === null) {
//             // 当全局变量为空时触发相应的操作
//             console.log("全局变量为空，触发相应操作");
//         }
//     }, 1000); // 每秒检查一次全局变量是否为空
// });
import upload from '@/utils/upload'
import request from '@/utils/request'
import dbUtils from '/uni_modules/zjy-sqlite-manage/components/zjy-sqlite-manage/dbUtils';
import { count,promise } from '@/utils/sqlite';
import { login } from '../login';
import { func } from '../../uni_modules/uview-plus/libs/function/test';
import tab from '../../plugins/tab';
import {errorKey} from "../../utils/sqlite";

const dbName = 'tb_screen'
// ppd表
const tbScreenPpd ="tb_screen_ppd"
// 摸底表
const tbScreenPerson ="tb_screen_person"
//采集表
const tbScreenCollect="tb_screen_collect"
// ct/dr表名
const tbScreenChestRadiograph ="tb_screen_chest_radiograph"
// 痰检表名
const tbScreenSputumExamination = "tb_screen_sputum_examination"
// 实验室表
const tbScreenExperiment = "tb_screen_experiment"
// 心电表
const tbScreenElectrocardiogram = "tb_screen_electrocardiogram"
// 诊断组
const tbScreenDiagnosis = "tb_screen_diagnosis"
// 工作队伍
const tbScreenUser = "tb_screen_user"
// 汇总
const tbScreenSum = "tb_screen_sum"
// 移动端各组离线图片信息表
const tbScreenImages = "tb_screen_images"


// 获取所有业务表
export function getTableList(params) {
	return request({
		url: '/tb/synchronize/get-tableLists',
		'method': 'GET',
		data: params
	})
}

// ========================== 摸底表 ==========================
// 获取平板端待筛查分页数据
/**
 * @param {Object} screenId
 * @param {Object} screenPoint
 * @param {Object} statusFlag 数据的状态 （1- 新增，2-修改）；数据类型 （'not null'-新增或修改的，'null' 不是新增或修改的，null 所有数据 ）
 * @param {Object} pageNo
 * @param {Object} pageSize
 */
export function getPersonData(screenId,screenPoint,statusFlag,pageNo,pageSize){
	let sql = `select *
			   from ${tbScreenPerson} 
			   where screenPoint = '${screenPoint}'
			   `
	if(screenId){
		sql+=`and screenId like '%${screenId}%' `
	}
	if(statusFlag){
		if(statusFlag=='null'){
			sql+=` and statusFlag is null `
		}else  if(statusFlag== 'not null'){
			sql+=` and statusFlag is not null `
		}else{
			sql+=` and statusFlag = ${statusFlag} `
		}
	}
	sql+=` order by id desc `
	if(pageNo !=-1){
		let offset = (pageNo-1)*pageSize
		sql+=` limit ${offset},${pageSize} `
	}
	// console.log("SQL:" + sql);

	return new Promise((resolve, reject) => {
	  plus.sqlite.selectSql({
	    name: dbName,
	    sql: sql,
	    success(e) {
	      resolve(e);
	    },
	    fail(e) {
	      // console.log(e);
	      reject(e);
	    }
	  });
	});
}
// 获取平板端待筛查数据总条数
/**
 * @param {Object} screenId
 * @param {Object} screenPoint
 * @param {Object} statusFlag 数据的状态 （1- 新增，2-修改）；数据类型 （'not null'-新增或修改的，'null' 不是新增或修改的，null 所有数据 ）
 */
export function getPersonCount(screenId,screenPoint,statusFlag){
	let sql = `select ifnull(count(*),0) num
			   from ${tbScreenPerson} 
			   where screenPoint = '${screenPoint}'
			   `
	if(screenId){
		sql+=`and screenId like '%${screenId}%'`
	}
	if(statusFlag){
		if(statusFlag=='null'){
			sql+=` and statusFlag is null `
		}else  if(statusFlag== 'not null'){
			sql+=` and statusFlag is not null `
		}else{
			sql+=` and statusFlag = ${statusFlag} `
		}
	}
	// console.log("SQL:" + sql);

	return new Promise((resolve, reject) => {
	  plus.sqlite.selectSql({
	    name: dbName,
	    sql: sql,
	    success(e) {
	      resolve(e);
	    },
	    fail(e) {
	      // console.log(e);
	      reject(e);
	    }
	  });
	});
}

// 获取摸底表 待筛查数据
export function getTableData1(params) {
	return request({
		url: '/tb/synchronize/get-tableData1',
		'method': 'GET',
		data: params
	})
}
// 更新摸底表数据
export function updateTableData1(params) {
	return request({
		url: '/tb/synchronize/update-tableData1',
		'method': 'PUT',
		data: params
	})
}


//同步之前  根据身份证，筛查年份查看是否有重复数据
export function selectPersonIdByIndex(screenType,idNum,year) {
  let sql = '';

  sql = `SELECT id FROM ${tbScreenPerson} WHERE screenType=${screenType} AND idNum= '${idNum}' AND year =${year}`

  // console.log("SQL:" + sql);

  return new Promise((resolve, reject) => {
    plus.sqlite.selectSql({
      name: dbName,
      sql: sql,
      success(e) {
        resolve(e);
      },
      fail(e) {
        // console.log(e);
        reject(e);
      }
    });
  });
}
// 根据表名  查询该表的最大id值(可共用)
export function selectMaxId(tableName) {
  let sql = `SELECT MAX(id) maxId FROM ${tableName} `

  // console.log("SQL:" + sql);

  return new Promise((resolve, reject) => {
    plus.sqlite.selectSql({
      name: 'tb_screen',
      sql: sql,
      success(e) {
        resolve(e);
      },
      fail(e) {
        // console.log(e);
        reject(e);
      }
    });
  });
}


// ========================== 采集表 ==========================
// 获取平板端采集组分页数据
/**
 * @param {Object} screenId
 * @param {Object} screenPoint
 * @param {Object} statusFlag 数据的状态 （1- 新增，2-修改）；数据类型 （'not null'-新增或修改的，'null' 不是新增或修改的，null 所有数据 ）
 * @param {Object} pageNo
 * @param {Object} pageSize
 */
export function getCollectData(screenId,screenPoint,statusFlag,pageNo,pageSize){
	let sql = `select sc.*,sp.name,sp.screenPoint
			   from ${tbScreenCollect} sc
			   left join ${tbScreenPerson} sp on sc.personId=sp.id
			   where sp.screenPoint = '${screenPoint}'
			   `
	if(screenId){
		sql+=` and sc.screenId like '%${screenId}%'`
	}
	if(statusFlag){
		if(statusFlag=='null'){
			sql+=` and sc.statusFlag is null `
		}else  if(statusFlag== 'not null'){
			sql+=` and sc.statusFlag is not null `
		}else{
			sql+=` and sc.statusFlag = ${statusFlag} `
		}
	}
	sql+=`order by sc.id desc `
	if(pageNo!=-1){
		let offset = (pageNo-1)*pageSize
		sql+=` limit ${offset},${pageSize}`
	// console.log(`limit ${offset},${pageSize}`);
	}
	// console.log("SQL:" + sql);

	return new Promise((resolve, reject) => {
	  plus.sqlite.selectSql({
	    name: dbName,
	    sql: sql,
	    success(e) {
	      resolve(e);
	    },
	    fail(e) {
	      // console.log(e);
	      reject(e);
	    }
	  });
	});
}
// 获取平板端采集组数据总条数
/**
 * @param {Object} screenId
 * @param {Object} screenPoint
 * @param {Object} statusFlag 数据的状态 （1- 新增，2-修改）；数据类型 （'not null'-新增或修改的，'null' 不是新增或修改的，null 所有数据 ）
 */
export function getCollectCount(screenId,screenPoint,statusFlag){
	let sql = `select ifnull(count(*),0) num 
			   from ${tbScreenCollect} sc
			   left join ${tbScreenPerson} sp on sc.personId=sp.id
			   where sp.screenPoint = '${screenPoint}'
			   `
	if(screenId){
		sql+=` and sc.screenId like '%${screenId}%'`
	}
	if(statusFlag){
		if(statusFlag=='null'){
			sql+=` and sc.statusFlag is null `
		}else  if(statusFlag== 'not null'){
			sql+=` and sc.statusFlag is not null `
		}else{
			sql+=` and sc.statusFlag = ${statusFlag} `
		}
	}
	// console.log("SQL:" + sql);

	return new Promise((resolve, reject) => {
	  plus.sqlite.selectSql({
	    name: dbName,
	    sql: sql,
	    success(e) {
	      resolve(e);
	    },
	    fail(e) {
	      // console.log(e);
	      reject(e);
	    }
	  });
	});
}

// 获取采集组数据
export function getTableData2(params) {
	return request({
		url: '/tb/synchronize/get-tableData2',
		'method': 'GET',
		data: params
	})
}
// 更新采集表数据
export function updateTableData2(params) {
	return request({
		url: '/tb/synchronize/update-tableData2',
		'method': 'PUT',
		data: params
	})
}

// 根据筛查编号，筛查次序,摸底表id 查询是否有重复记录(可共用)
export function selectTableIdByIndex(table,screenId,screenOrder,personId) {
  let sql = '';

  sql = `SELECT id FROM ${table} WHERE screenId= ${screenId} AND personId =${personId}`
  
  if(screenOrder!=null){
	  sql+=` AND screenOrder =${screenOrder} `
  }

  // console.log("SQL:" + sql);

  return new Promise((resolve, reject) => {
    plus.sqlite.selectSql({
      name: 'tb_screen',
      sql: sql,
      success(e) {
        resolve(e);
      },
      fail(e) {
        // console.log(e);
        reject(e);
      }
    });
  });
}

//更新表数据（可共用）
export function updateTable(table,setData, id) {
  let setStr = '';
  let sql = '';
  if (JSON.stringify(setData) !== '{}') {
    let dataKeys = Object.keys(setData);
    dataKeys.forEach((item, index) => {
      // console.log(setData[item]);
      setStr += `${item}=${JSON.stringify(setData[item])}${dataKeys.length - 1 !== index ? "," : ""}`;
    });
    sql = `UPDATE ${table} SET ${setStr} WHERE id = ${id}`;
  } else {
    return Promise.reject("没有修改值！");
  }
  // console.log("SQL:" + sql);

  return new Promise((resolve, reject) => {
    plus.sqlite.executeSql({
      name: 'tb_screen',
      sql: sql,
      success(e) {
        resolve(e);
      },
      fail(e) {
        // console.log(e);
        reject(e);
      }
    });
  });
}

// ========================== ppd表 ==========================
// 获取平板端ppd分页数据
export function getPpdData(screenId,injection,screenPoint,statusFlag,pageNo,pageSize){
	let sql = `select sp1.*,sp2.name
			   from ${tbScreenPpd} sp1
			   left join ${tbScreenPerson} sp2 on sp1.personId=sp2.id
			   where sp2.screenPoint = '${screenPoint}'`
	if(screenId){
		sql+=` and sp1.screenId like '%${screenId}%' `
	}
	if(injection!=null){
		sql+=` and sp1.injection=${injection} `
	}
	if(statusFlag){
		if(statusFlag=='null'){
			sql+=` and sp1.statusFlag is null `
		}else  if(statusFlag== 'not null'){
			sql+=` and sp1.statusFlag is not null `
		}else{
			sql+=` and sp1.statusFlag = ${statusFlag} `
		}
	}
	sql+=` order by sp1.id desc `
	if(pageNo!=-1){
		let offset = (pageNo-1)*pageSize
		sql+=` limit ${offset},${pageSize}`
	}
	// console.log("SQL:" + sql);

	return new Promise((resolve, reject) => {
	  plus.sqlite.selectSql({
	    name: dbName,
	    sql: sql,
	    success(e) {
	      resolve(e);
	    },
	    fail(e) {
	      // console.log(e);
	      reject(e);
	    }
	  });
	});
}
// 获取平板端ppd数据总条数
export function getPpdCount(screenId,injection,screenPoint,statusFlag){
	let sql = `select ifnull(count(*),0) num
			   from ${tbScreenPpd} sp1
			   left join ${tbScreenPerson} sp2 on sp1.personId=sp2.id
			   where sp2.screenPoint = '${screenPoint}'
			   `
	if(screenId){
		sql+=` and sp1.screenId like '%${screenId}%' `
	}
	if(injection!=null){
		sql+=` and sp1.injection=${injection} `
	}
	if(statusFlag){
		if(statusFlag=='null'){
			sql+=` and sp1.statusFlag is null `
		}else  if(statusFlag== 'not null'){
			sql+=` and sp1.statusFlag is not null `
		}else{
			sql+=` and sp1.statusFlag = ${statusFlag} `
		}
	}
	// console.log("SQL:" + sql);

	return new Promise((resolve, reject) => {
	  plus.sqlite.selectSql({
	    name: dbName,
	    sql: sql,
	    success(e) {
	      resolve(e);
	    },
	    fail(e) {
	      // console.log(e);
	      reject(e);
	    }
	  });
	});
}
// 获取ppd组数据
export function getTableData3(params) {
	return request({
		url: '/tb/synchronize/get-tableData3',
		'method': 'GET',
		data: params
	})
}
// 更新ppd表数据
export function updateTableData3(params) {
	return request({
		url: '/tb/synchronize/update-tableData3',
		'method': 'PUT',
		data: params
	})
}


// ========================== dr/ct表 ==========================
// 获取平板端dr/ct分页数据
export function getDrCtData(screenId,outcome,screenPoint,statusFlag,pageNo,pageSize){
	let sql = `select sc.*,sp.name
			   from ${tbScreenChestRadiograph} sc
			   left join ${tbScreenPerson} sp on sc.personId=sp.id
			   where sp.screenPoint = '${screenPoint}'
			   `
	if(screenId){
		sql+=` and sc.screenId like '%${screenId}%' `
	}
	if(outcome!=null){
		sql+=` and sc.outcome=${outcome} `
	}
	if(statusFlag){
		if(statusFlag=='null'){
			sql+=` and sc.statusFlag is null `
		}else  if(statusFlag== 'not null'){
			sql+=` and sc.statusFlag is not null `
		}else{
			sql+=` and sc.statusFlag = ${statusFlag} `
		}
	}
	sql+=` order by sc.id desc `
	if(pageNo!=-1){
		let offset = (pageNo-1)*pageSize
		sql+=` limit ${offset},${pageSize}`
	}
	// console.log("SQL:" + sql);

	return new Promise((resolve, reject) => {
	  plus.sqlite.selectSql({
	    name: dbName,
	    sql: sql,
	    success(e) {
	      resolve(e);
	    },
	    fail(e) {
	      // console.log(e);
	      reject(e);
	    }
	  });
	});
}
// 获取平板端dr/ct数据总条数
export function getDrCtCount(screenId,outcome,screenPoint,statusFlag){
	let sql = `select ifnull(count(*),0) num
			   from ${tbScreenChestRadiograph} sc
			   left join ${tbScreenPerson} sp on sc.personId=sp.id
			   where sp.screenPoint = '${screenPoint}'
			   `
	if(screenId){
		sql+=` and sc.screenId like '%${screenId}%' `
	}
	if(outcome!=null){
		sql+=` and sc.outcome=${outcome} `
	}
	if(statusFlag){
		if(statusFlag=='null'){
			sql+=` and sc.statusFlag is null `
		}else  if(statusFlag== 'not null'){
			sql+=` and sc.statusFlag is not null `
		}else{
			sql+=` and sc.statusFlag = ${statusFlag} `
		}
	}
	// console.log("SQL:" + sql);

	return new Promise((resolve, reject) => {
	  plus.sqlite.selectSql({
	    name: dbName,
	    sql: sql,
	    success(e) {
	      resolve(e);
	    },
	    fail(e) {
	      // console.log(e);
	      reject(e);
	    }
	  });
	});
}
// 获取dr/ct组数据
export function getTableData4(params) {
	return request({
		url: '/tb/synchronize/get-tableData4',
		'method': 'GET',
		data: params
	})
}
// 更新dr/ct表数据
export function updateTableData4(params) {
	return request({
		url: '/tb/synchronize/update-tableData4',
		'method': 'PUT',
		data: params
	})
}

// ========================== 痰检表 ==========================
// 获取平板端痰检分页数据
export function getSputumExaminationData(screenId,atomization,screenPoint,pageNo,pageSize){
	let sql = `select sse.*,sp.name
			   from ${tbScreenSputumExamination} sse
			   left join ${tbScreenPerson} sp on sse.personId=sp.id
			   where sp.screenPoint = '${screenPoint}'
			   `
	if(screenId){
		sql+=` and sse.screenId like '%${screenId}%' `
	}
	if(atomization!=null){
		sql+=` and sse.atomization=${atomization} `
	}
	sql+=` order by sse.id desc `
	if(pageNo!=-1){
		let offset = (pageNo-1)*pageSize
		sql+=` limit ${offset},${pageSize}`
	}
	// console.log("SQL:" + sql);

	return new Promise((resolve, reject) => {
	  plus.sqlite.selectSql({
	    name: dbName,
	    sql: sql,
	    success(e) {
	      resolve(e);
	    },
	    fail(e) {
	      // console.log(e);
	      reject(e);
	    }
	  });
	});
}
// 获取平板端痰检数据总条数
export function getSputumExaminationCount(screenId,atomization,screenPoint){
	let sql = `select ifnull(count(*),0) num
			   from ${tbScreenSputumExamination} sse
			   left join ${tbScreenPerson} sp on sse.personId=sp.id
			   where sp.screenPoint = '${screenPoint}'
			   `
	if(screenId){
		sql+=` and sse.screenId like '%${screenId}%' `
	}
	if(atomization!=null){
		sql+=` and sse.atomization=${atomization}`
	}
	// console.log("SQL:" + sql);

	return new Promise((resolve, reject) => {
	  plus.sqlite.selectSql({
	    name: dbName,
	    sql: sql,
	    success(e) {
	      resolve(e);
	    },
	    fail(e) {
	      // console.log(e);
	      reject(e);
	    }
	  });
	});
}
// 获取痰检组数据
export function getTableData5(params) {
	return request({
		url: '/tb/synchronize/get-tableData5',
		'method': 'GET',
		data: params
	})
}
// 更新痰检表数据
export function updateTableData5(params) {
	return request({
		url: '/tb/synchronize/update-tableData5',
		'method': 'PUT',
		data: params
	})
}

// ========================== 心电表 ==========================
// 获取平板端心电图分页数据
export function getElectrocardiogramData(screenId,screenPoint,pageNo,pageSize){
	let sql = `select se.*,sp.name
			   from ${tbScreenElectrocardiogram} se
			   left join ${tbScreenPerson} sp on se.personId=sp.id
			   where sp.screenPoint = '${screenPoint}'
			   `
	if(screenId){
		sql+=` and screenId like '%${screenId}%' `
	}
	sql+=` order by se.id desc `
	if(pageNo!=-1){
		let offset = (pageNo-1)*pageSize
		sql+=` limit ${offset},${pageSize}`
	}
	// console.log("SQL:" + sql);

	return new Promise((resolve, reject) => {
	  plus.sqlite.selectSql({
	    name: dbName,
	    sql: sql,
	    success(e) {
	      resolve(e);
	    },
	    fail(e) {
	      // console.log(e);
	      reject(e);
	    }
	  });
	});
}
// 获取平板端心电图数据总条数
export function getElectrocardiogramCount(screenId,screenPoint){
	let sql = `select ifnull(count(*),0) num
			   from ${tbScreenElectrocardiogram} se
			   left join ${tbScreenPerson} sp on se.personId=sp.id
			   where sp.screenPoint = '${screenPoint}'
			   `
	if(screenId){
		sql+=` and screenId like '%${screenId}%`
	}
	// console.log("SQL:" + sql);

	return new Promise((resolve, reject) => {
	  plus.sqlite.selectSql({
	    name: dbName,
	    sql: sql,
	    success(e) {
	      resolve(e);
	    },
	    fail(e) {
	      // console.log(e);
	      reject(e);
	    }
	  });
	});
}
// 获取心电图组数据
export function getTableData6(params) {
	return request({
		url: '/tb/synchronize/get-tableData6',
		'method': 'GET',
		data: params
	})
}
// 更新心电表数据
export function updateTableData6(params) {
	return request({
		url: '/tb/synchronize/update-tableData6',
		'method': 'PUT',
		data: params
	})
}

// 心电表分页数据

// ========================== 汇总表-统计 ==========================
// 上传数据到服务器
export function uploadSumData(params) {
	return request({
		url: '/tb/synchronize/update-sum-data',
		'method': 'PUT',
		timeout:60 * 1000,
		data: params
	})
}
// 下拉数据到本地
export function getSumData(params) {
	return request({
		url: '/tb/synchronize/get-sum-data',
		'method': 'GET',
		data: params
	})
}

// 根据筛查编号和摸底表id查询汇总表id和当前已完成的分组
export function selectSumIdByIndex(year,screenType,screenId,personId) {
  let sql = `
				SELECT id,curFinish 
				FROM ${tbScreenSum} 
				WHERE screenId= ${screenId} 
				AND personId =${personId}
				AND year =${year}
				AND screenType =${screenType}
			`
  // console.log("SQL:" + sql);

  return new Promise((resolve, reject) => {
    plus.sqlite.selectSql({
      name: 'tb_screen',
      sql: sql,
      success(e) {
        resolve(e);
      },
      fail(e) {
        // console.log(e);
        reject(e);
      }
    });
  });
}

// 更新汇总表数据(上面有可共用的方法)
// 统计采集组数据
export function getCollectStatistic(){
	let sql = `
				SELECT  t.year, t.screenType , t.screenId , t.personId ,t.num collectNum, s.screenTime lastCollectTime,t.collectId
				FROM (
				    SELECT max(screenOrder) AS num, year, screenType, screenId, personId,max(id) as collectId
				    FROM ${tbScreenCollect}
				    GROUP BY year, screenType, screenId, personId
				) AS t
				JOIN (
				    SELECT year, screenType, screenId, personId, MAX(screenTime) AS screenTime
				    FROM ${tbScreenCollect}
				    GROUP BY year, screenType, screenId, personId
				) AS s ON t.year = s.year 
				        AND t.screenType = s.screenType 
				        AND t.screenId = s.screenId 
				        AND t.personId = s.personId
			   `
	console.log("SQL:" + sql);

	return new Promise((resolve, reject) => {
	  plus.sqlite.selectSql({
	    name: dbName,
	    sql: sql,
	    success(e) {
	      resolve(e);
	    },
	    fail(e) {
	      // console.log(e);
	      reject(e);
	    }
	  });
	});
}
// 统计ppd组数据
export function getPpdStatistic(){
	let sql = `
				SELECT  t.year, t.screenType, t.screenId, t.personId,t.num ppdNum, s.screenTime lastPpdTime,t.ppdId
				FROM (
				    SELECT max(screenOrder) AS num, year, screenType, screenId, personId,max(id) ppdId
				    FROM ${tbScreenPpd}
				    GROUP BY year, screenType, screenId, personId
				) AS t
				JOIN (
				    SELECT year, screenType, screenId, personId, MAX(screenTime) AS screenTime
				    FROM ${tbScreenPpd}
				    GROUP BY year, screenType, screenId, personId
				) AS s ON t.year = s.year 
				        AND t.screenType = s.screenType 
				        AND t.screenId = s.screenId 
				        AND t.personId = s.personId
			   `
	console.log("SQL:" + sql);

	return new Promise((resolve, reject) => {
	  plus.sqlite.selectSql({
	    name: dbName,
	    sql: sql,
	    success(e) {
	      resolve(e);
	    },
	    fail(e) {
	      // console.log(e);
	      reject(e);
	    }
	  });
	});
}
// 统计dr组数据
export function getDrStatistic(){
	let sql = `
				SELECT  t.year, t.screenType, t.screenId, t.personId,t.num chestRadiographNum, s.photoTime lastChestRadiographTime,t.chestRadiographId
				FROM (
				    SELECT max(screenOrder) AS num, year, screenType, screenId, personId,max(id) chestRadiographId
				    FROM ${tbScreenChestRadiograph}
				    GROUP BY year, screenType, screenId, personId
				) AS t
				JOIN (
				    SELECT year, screenType, screenId, personId, MAX(photoTime) AS photoTime
				    FROM ${tbScreenChestRadiograph}
				    GROUP BY year, screenType, screenId, personId
				) AS s ON t.year = s.year 
				        AND t.screenType = s.screenType 
				        AND t.screenId = s.screenId 
				        AND t.personId = s.personId
			   `
	console.log("SQL:" + sql);

	return new Promise((resolve, reject) => {
	  plus.sqlite.selectSql({
	    name: dbName,
	    sql: sql,
	    success(e) {
	      resolve(e);
	    },
	    fail(e) {
	      // console.log(e);
	      reject(e);
	    }
	  });
	});
}
// 统计痰检组数据
export async function getSputumStatistic(){
	let sql = `
				SELECT max(screenOrder) AS num, year, screenType, screenId, personId,max(id) sputumExaminationId,updateTime lastSputumExaminationTime
				FROM ${tbScreenSputumExamination}
				GROUP BY year, screenType, screenId, personId,updateTime
			   `
			   sql=`select * from ${tbScreenSputumExamination}`
	console.log("SQL:" + sql);
	let data=await promise(dbName,sql)
	console.log(data);
	console.log(1111);
	// return new Promise((resolve, reject) => {
	//   plus.sqlite.selectSql({
	//     name: dbName,
	//     sql: sql,
	//     success(e) {
	//       resolve(e);
	//     },
	//     fail(e) {
	//       // console.log(e);
	//       reject(e);
	//     }
	//   });
	// });
}

// 统计心电组数据
export function getElectrocardiogramStatistic(){
	let sql = `
				SELECT  t.year, t.screenType, t.screenId, t.personId,t.num electrocardiogramNum, s.screenTime lastElectrocardiogramTime,t.electrocardiogramId
				FROM (
				    SELECT max(screenOrder) AS num, year, screenType, screenId, personId,max(id) electrocardiogramId
				    FROM ${tbScreenElectrocardiogram}
				    GROUP BY year, screenType, screenId, personId
				) AS t
				JOIN (
				    SELECT year, screenType, screenId, personId, MAX(screenTime) AS screenTime
				    FROM ${tbScreenElectrocardiogram}
				    GROUP BY year, screenType, screenId, personId
				) AS s ON t.year = s.year 
				        AND t.screenType = s.screenType 
				        AND t.screenId = s.screenId 
				        AND t.personId = s.personId
			   `
	console.log("SQL:" + sql);

	return new Promise((resolve, reject) => {
	  plus.sqlite.selectSql({
	    name: dbName,
	    sql: sql,
	    success(e) {
	      resolve(e);
	    },
	    fail(e) {
	      // console.log(e);
	      reject(e);
	    }
	  });
	});
}



// 统计痰检组数据
export function getLocalSumData(screenId,screenType,year,personId,idNum){
	let sql = `
				SELECT *
				from ${tbScreenSum}
				where 
				idNum=${idNum}
				and screenType=${screenType}
				and year=${year}
			   `
	// console.log("SQL:" + sql);
	 return promise(dbName,sql)
	 // console.log(data);
}
export function getLocalSumDataTypeAndYear(screenType,year){
	let sql = `
				SELECT *
				from ${tbScreenSum}
				where screenType=${screenType}
				and year=${year}
				and statusFlag is not null
			   `
	console.log("SQL:" + sql);
	 return promise(dbName,sql)
	 // console.log(data);
}

// ========================== 工作队 ==========================
// 获取筛查点和筛查单位
export function getScreenPoint(name){
	let sql = `select screenPoint,agency
			   from	${tbScreenUser}
			   where name='${name}'
			   `
	// console.log("SQL:" + sql);

	return new Promise((resolve, reject) => {
	  plus.sqlite.selectSql({
	    name: dbName,
	    sql: sql,
	    success(e) {
	      resolve(e);
	    },
	    fail(e) {
	      // console.log(e);
	      reject(e);
	    }
	  });
	});
}
// 获取角色分组
export function getGroups(name){
	let sql = `select groupName
			   from	${tbScreenUser}
			   where name='${name}'
			   `
	// console.log("SQL:" + sql);

	return new Promise((resolve, reject) => {
	  plus.sqlite.selectSql({
	    name: dbName,
	    sql: sql,
	    success(e) {
	      resolve(e);
	    },
	    fail(e) {
	      // console.log(e);
	      reject(e);
	    }
	  });
	});
}
// 插入年份
export function insertYear(year){
	let sql = `update ${tbScreenUser}
			   set year=${year}
			   `
	// console.log("SQL:" + sql);

	return new Promise((resolve, reject) => {
	  plus.sqlite.executeSql({
	    name: dbName,
	    sql: sql,
	    success(e) {
	      resolve(e);
	    },
	    fail(e) {
	      // console.log(e);
	      reject(e);
	    }
	  });
	});
}
export function getYear(name){
	let sql = `select distinct year
			   from ${tbScreenUser}
			   where name='${name}'
			   `
	// console.log("SQL:" + sql);

	return new Promise((resolve, reject) => {
	  plus.sqlite.selectSql({
	    name: dbName,
	    sql: sql,
	    success(e) {
	      resolve(e);
	    },
	    fail(e) {
	      // console.log(e);
	      reject(e);
	    }
	  });
	});
}
// 获取平板端工作队伍分页数据
export function getWorkTeamData(groupName,pageNo,pageSize){
	let str=''
	if(groupName){
		str+=` where groupName like '%${groupName}%'`
	}
	let sql = `select *
			   from ${tbScreenUser} 
			   `
	sql+=str
	if(pageNo!=-1){
		let offset = (pageNo-1)*pageSize
		sql+=`limit ${offset},${pageSize}`
	}
	// console.log("SQL:" + sql);

	return new Promise((resolve, reject) => {
	  plus.sqlite.selectSql({
	    name: dbName,
	    sql: sql,
	    success(e) {
	      resolve(e);
	    },
	    fail(e) {
	      // console.log(e);
	      reject(e);
	    }
	  });
	});
}
// 获取平板端工作队伍数据总条数
export function getWorkTeamCount(groupName){
	let str=''
	if(groupName){
		str+=` where groupName like '%${groupName}%' `
	}
	let sql = `select ifnull(count(*),0) num 
			   from ${tbScreenUser} 
			   `
	sql+=str
	// console.log("SQL:" + sql);

	return new Promise((resolve, reject) => {
	  plus.sqlite.selectSql({
	    name: dbName,
	    sql: sql,
	    success(e) {
	      resolve(e);
	    },
	    fail(e) {
	      // console.log(e);
	      reject(e);
	    }
	  });
	});
}

// 清除表数据
export function truncateTable(tableName){
	let sql = `delete from ${tableName} `

	// console.log("SQL:" + sql);

	return new Promise((resolve, reject) => {
	  plus.sqlite.selectSql({
	    name: dbName,
	    sql: sql,
	    success(e) {
	      resolve(e);
	    },
	    fail(e) {
	      // console.log(e);
	      reject(e);
	    }
	  });
	});
}
// 获取工作队伍信息
export function getWorkTeamInfo(screenPointId) {
	return request({
		url: '/tb/synchronize/get-workTeam?screenPointId='+screenPointId,
		'method': 'GET'
	})
}
// 根据当前用户id获取密码
export function getUserInfo(id) {
	return request({
		url: '/tb/synchronize/get-userInfo?id='+id,
		'method': 'GET'
	})
}

/**
 * 单条记录上传
 * @param {number} type：图片来源 需要上传的图片分组（1：采集，2：PPD，3：DR/CT，4：痰检组， 5：心电图）
 * @param {Object} screenId 筛查编号
 * @param {Object} personId 摸底表id
 * @param {Object} screenOrder 筛查次序
 * @param {Object} year 年份
 * @param {Object} screenType 筛查类型
 */
export function uploadOfflineImageOne(type, screenId, personId, screenOrder, year, screenType) {
	const now = new Date();
	const currentYear = now.getFullYear();

	let inType = '';

	// 查询对应范围类型的数据
	switch (type) {
		case 1:
			inType = ' IN (8) '
			break;
		case 2:
			inType = ' IN (9,16,17,18)'
			break;
		case 3:
			inType = ' IN (1, 2, 10, 11) '
			break;
		case 4:
			inType = ' IN (3, 5, 6, 7, 12) '
			break;
		case 5:
			inType = ' IN (4, 15) '
			break;
	}

	plus.sqlite.selectSql({
		name: dbName,
		sql: `SELECT
				*
			FROM
				"tb_screen_images" 
			WHERE
				"type" ${inType} and screenId = ${screenId} and personId = ${personId} and screenOrder = ${screenOrder} and year = ${year} and screenType = ${screenType} and statusFlag is not null`,
		success(res) {
			if (res) {
				for (let i = 0; i < res.length; i++) {
					if (res[i].path) {
						let item = res[i];
						// 单张离线图片上传
						upload({
							url: '/admin-api/tb/screen-images/updateImage',
							method: 'PUT',
							name: 'imageFile',
							filePath: item.path
						}).then(async(res) => {
							if (res) {
								let url = res.data;

								let data = {
									screenId: item.screenId,
									personId: item.personId,
									type: item.type,
									path: item.path,
									idNum: item.idNum,
									url: url,
									screenTime: item.screenTime,
									screenOrder: item.screenOrder,
									screenPoint: item.screenPoint,
									createTime: item.createTime,
									// 筛查年份、类型
									year: currentYear,
									screenType: screenType
								};

								console.log(data);
								// 创建移动端各组离线图片信息
								await request({
									url: '/tb/screen-images/create',
									'method': 'POST',
									data: data
								});
								await updateFlagTableById(tbScreenImages,item.id,null)
							}
						})
					}
				}
			}
		},
		fail(e) {
			console.log(e);
		}
	});
}


/**
 * 批量上传离线图片
 * @param {number} type：图片来源 需要上传的图片分组（1：采集，2：PPD，3：DR/CT，4：痰检组， 5：心电图）
 */
export async function uploadOfflineImage(type) {
	const now = new Date();
	const currentYear = now.getFullYear();

	let inType = '';

	// 查询对应范围类型的数据
	switch (type) {
		case 1:
			inType = ' IN (8) '
			break;
		case 2:
			inType = ' IN (9,16,17,18)'
			break;
		case 3:
			inType = ' IN (1, 2, 10, 11) '
			break;
		case 4:
			inType = ' IN (3, 5, 6, 7, 12) '
			break;
		case 5:
			inType = ' IN (4, 15) '
			break;
	}
	let sql=`SELECT * FROM "tb_screen_images" WHERE statusFlag is not null and "type" ${inType} `
	let data=await promise(dbName,sql)
	console.log(data)
	uni.showLoading({
		title: '上传图片中...',
		mask: true
	});
	if (data) {
		console.log(data.length)
		if(data.length==0){
			uni.hideLoading();
			uni.showToast({
				title: '暂无需要上传的图片',
				mask: true,
				icon: 'none',
				duration: 1500
			});
		}
		for (let i = 0; i < data.length; i++) {
			try {
				if (data[i].path) {
					let item = data[i];
					// 单张离线图片上传
					let uploadResp = await upload({
						url: '/admin-api/tb/screen-images/updateImage',
						method: 'PUT',
						name: 'imageFile',
						filePath: item.path
					})
					if (uploadResp) {
						let url = uploadResp.data;
						let data = {
							padId: "" + item.id + item.idNum,
							screenId: item.screenId,
							personId: item.personId,
							type: item.type,
							idNum: item.idNum,
							path: item.path,
							url: url,
							screenTime: item.screenTime,
							screenOrder: item.screenOrder,
							screenPoint: item.screenPoint,
							createTime: item.createTime,
							// 筛查年份、类型
							year: currentYear,
							screenType: item.screenType
						};
						// 创建移动端各组离线图片信息
						await request({
							url: '/tb/screen-images/create',
							'method': 'POST',
							data: data
						});
						await updateFlagTableById(tbScreenImages, item.id, null)
					}
				}
			}catch (e) {
				throw new Error('图片上传失败')
				return
			}
		}
}

}
/**
 * 上传图片
 * @param {{}}  
 */
export const uploadPic=async(item)=> {
	let uploadResp = await upload({
		url: '/admin-api/tb/screen-images/updateImage',
		method: 'PUT',
		name: 'imageFile',
		filePath: item.path
	})
	console.log(uploadResp);
	if (uploadResp) {
		let url = uploadResp.data;
		let data = {
			padId: "" + item.id + item.idNum,
			screenId: item.screenId,
			personId: item.personId,
			type: item.type,
			idNum: item.idNum,
			path: item.path,
			url: url,
			screenTime: item.screenTime,
			screenOrder: item.screenOrder,
			screenPoint: item.screenPoint,
			createTime: item.createTime,
			// 筛查年份、类型
			year: currentYear,
			screenType: item.screenType
		};
		// 创建移动端各组离线图片信息
		await request({
			url: '/tb/screen-images/create',
			'method': 'POST',
			data: data
		});
	}
}
/**
 * @param {string} idNum
 * @param {number} personId
 * @param {string} table
 */
export async function listDataByIdNumAndPersonId(idNum,personId,table){
	let sql=`select * from ${table} where idNum='${idNum}' and personId=${personId}`
	if(table==tbScreenPerson){
		sql=`select * from ${table} where idNum='${idNum}' and id=${personId}`
	}
	console.log(sql);
	return promise(dbName,sql)
}

/**
 * 根据id更新
 * @param table
 * @param id
 * @return {Promise<void>}
 */
export async function updateFlagTableById(table,id,flag){
	let sql=`update ${table} set statusFlag=${flag} where id=${id}`
	return promise(dbName,sql)
}
/**
 * 更新筛查编号、患者id、数据状态
 * @param {string} table 表名
 * @param {number} id 旧的患者id
 * @param {number} newId 新的患者id
 * @param {string} idNum 患者身份证
 * @param {string} screenId 筛查编号
 */
export async function updatePersonId(table,id,newId,idNum,screenId){
	let field='personId'
	if(table==tbScreenPerson){
		field='id'
	}
	let statusFlagSet=''
	if(table!=tbScreenSum){
		statusFlagSet=', statusFlag=null'
	}
	let screenIdSet=''
	if(screenId){
		screenIdSet=`,screenId=${screenId} `
	}
	let sql=`update ${table} set ${field}=${newId}${screenIdSet}${statusFlagSet} where ${field}=${id} and idNum=${idNum} `
	console.log(sql);
	return promise(dbName,sql)
}
/**
 * 更新筛查编号、患者id
 * @param {string} table 表名
 * @param {number} id 旧的患者id
 * @param {number} newId 新的患者id
 * @param {string} idNum 患者身份证
 * @param {string} screenId 筛查编号
 */
export async function updatePersonIdOnly(table,id,newId,idNum,screenId){
	let field='personId'
	if(table==tbScreenPerson){
		field='id'
	}
	let screenIdSet=''
	if(screenId){
		screenIdSet=`,screenId=${screenId} `
	}
	let sql=`update ${table} set ${field}=${newId}${screenIdSet} where ${field}=${id} and idNum=${idNum} `
	console.log(sql);
	return promise(dbName,sql)
}
/**
 * 更新id和数据状态statusFlag
 * @param {string} table 表名
 * @param {number} id 旧的id
 * @param {number} newId 新的id
 * @param {string} idNum 患者身份证
 */
export async function updateIdAndStatusFlag(table,id,newId,idNum){
	let sql=`update ${table} set id=${newId},statusFlag=null where id=${id} and idNum=${idNum} `
	// console.log(sql);
	return promise(dbName,sql)
}
/**
 * 更新数据状态statusFlag
 * @param {string} table 表名
 * @param {number} id 旧的id
 * @param {string} idNum 患者身份证
 */
export async function updateStatusFlagOnly(table,id,idNum){
	let sql=`update ${table} set statusFlag=null where id=${id} and idNum=${idNum} `
	// console.log(sql);
	return promise(dbName,sql)
}
/**
 * 更新汇总表
 * @param {number} id 旧的分组id
 * @param {number} newId 新的患者id
 * @param {string} idNum 患者身份证
 * @param {string} field 分组表字段
 */
export async function updateSumFieldId(id,newId,idNum,field){
	let sql=`update ${tbScreenSum} set ${field}=${newId} where ${field}=${id} and idNum=${idNum} `
	// console.log(sql);
	return promise(dbName,sql)
}
export async function updateErrorFlag(type,data){
	try{
		let start=await uni.getStorage({key:errorKey})
		let copList=Object.assign([],start.data)
		let copObj=new Set(copList)
		// 缓存中有值
		if (start && start.data && start.data.length >0){
			if (type){
				// 判断data的类型是不是数组
				if (Array.isArray(data)) {
					data.forEach(element => copObj.add(element));
				}else{
					copObj.add(data);
				}
			}else{ // 删除元素
				if (Array.isArray(data)) {
					data.forEach(element => copObj.delete(element));
				}else{
					copObj.delete(data);
				}
			}
		}else {
			if (type){  // 添加
				if (!Array.isArray(data)) {
					let list=[]
					list.push(data)
					copObj=new Set(list)
				}else {
					copObj=new Set(data)
				}
			}else {  // 删除

			}
		}
		// console.log([...copObj]);
		uni.setStorage({
			key:errorKey,
			data: Array.from(copObj)
		})
	}catch (e) {
		console.log(e)
		uni.setStorage({
			key:errorKey,
			data:[]
		})
	}
}

/**
 * 删除平板的本地图片
 * @param dirPath 图片所在目录
 * @return {Promise<void>}
 */
export async function delImgFromDir(dirPath){
	if (!dirPath){
		return
	}// 获取文件系统管理器
	const fs = plus.io;
// 解析路径
	fs.resolveLocalFileSystemURL(dirPath, function(entry) {
		// 检查是否是目录
		if (entry.isDirectory) {
			const directoryReader = entry.createReader();

			// 读取目录中的所有条目
			directoryReader.readEntries(function(entries) {
				for (let i = 0; i < entries.length; i++) {
					const fileEntry = entries[i];

					// 删除文件
					fileEntry.remove(function() {
						console.log(`${fileEntry.name} 删除成功`);
					}, function(error) {
						if (error.message.includes("不支持当前路径")) {
							uni.hideLoading();
							console.error(`删除 ${fileEntry.name} 失败`, error);
							uni.showToast({
								title: `删除部分图片，保存在相册中的图片请自行选择删除`,
								mask: true,
								icon: 'none',
								duration: 3000
							});
							return; // 退出循环
						}
					});

					if (error) break; // 退出循环
				}

			}, function(error) {
				console.error('读取目录失败', error);
			});
			uni.hideLoading();
			uni.showToast({
				title: `删除部分图片，保存在相册中的图片请自行选择删除`,
				mask: true,
				icon: 'none',
				duration: 3000
			});
		} else {
			console.log('该路径不是一个文件夹');
		}
	}, function(error) {
		console.log(error.message)
		console.log(error.message.includes("不支持当前路径"))
		if (error.message.includes("不支持当前路径")){
			uni.hideLoading();
			uni.showToast({
				title: `删除部分图片，保存在相册中的图片请自行选择删除`,
				mask: true,
				icon: 'none',
				duration: 2000
			});
			return
		}
		console.error('解析路径失败', error);
	});
}

/**
 * 删除对象中的空属性
 * @param obj
 */
export function removeNullProperties(obj) {
	for (let key in obj) {
		if (obj[key] == null || obj[key] == 'null') {
			delete obj[key];
		}
	}
}
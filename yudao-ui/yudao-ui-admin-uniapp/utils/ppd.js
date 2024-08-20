const dbName = 'tb_screen'
const tbScreenPpd ="tb_screen_ppd"

export function getPpdList() {
  let setStr = '';
  let sql = '';

	sql = `SELECT * FROM ${tbScreenPpd} WHERE screenType = ${uni.$screenType} AND year =${uni.$person.year}`
  // console.log("SQL:" + sql);
	if(sql!=''){
		return new Promise((resolve, reject) => {
		  plus.sqlite.selectSql({
		    name: 'tb_screen',
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

//根据人员id查询其对应ppd组数据
export function getPpdBypersonIdToOrder(personId) {
  let setStr = '';
  let sql = '';
	// console.log(uni.$person);
	sql = `SELECT MAX(screenOrder) AS screenOrder  FROM ${tbScreenPpd} WHERE personId = ${personId} AND year =${uni.$person.year} AND screenType=${uni.$screenType}`
  // console.log("SQL:" + sql);
	if(sql!=''){
		return new Promise((resolve, reject) => {
		  plus.sqlite.selectSql({
		    name: 'tb_screen',
		    sql: sql,
		    success(e) {
		      resolve(e);
		    },
		    fail(e) {
				console.log(e);
		      reject(e);
			  throw new Error(e)
		    }
		  });
		});
	}
}


//根据注射次序和人员id修改其对应ppd组数据
export function updateBypersonIdAndScreenOrder(setData,screenOrder,personId) {
  let setStr = '';
  let sql = '';
// console.log("setData",setData);

	if (JSON.stringify(setData) !== '{}') {
		let dataKeys = Object.keys(setData)
		dataKeys.forEach((item, index) => {
			// console.log(setData[item])
				setStr += (
					`${item}=${JSON.stringify(setData[item])}${dataKeys.length - 1 !== index ? "," : ""}`
				)	
		})
		sql = `UPDATE ${tbScreenPpd} SET ${setStr} WHERE personId = ${personId} AND screenOrder= ${screenOrder} AND year =${uni.$person.year} AND screenType=${uni.$screenType}`
		// console.log(sql);
	}else{
		return 0
	}
	
  // console.log("SQL:" + sql);
	if(sql!=''){
		return new Promise((resolve, reject) => {
		  plus.sqlite.selectSql({
		    name: 'tb_screen',
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


//根据注射次序和人员id查询其对应ppd组数据
export function getBypersonIdAndScreenOrder(screenOrder,personId) {
  let setStr = '';
  let sql = '';


  sql = `SELECT * FROM ${tbScreenPpd} WHERE personId = ${personId} AND screenOrder= ${screenOrder} AND year =${uni.$person.year} AND screenType=${uni.$screenType}`
	
	
  // console.log("SQL:" + sql);
	if(sql!=''){
		return new Promise((resolve, reject) => {
		  plus.sqlite.selectSql({
		    name: 'tb_screen',
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


//根据注射次序和人员id查询其对应ppd组数据
export function getBypersonIdAndScreenOrderOne(screenOrder,personId) {
  let setStr = '';
  let sql = '';


  sql = `SELECT * FROM ${tbScreenPpd} WHERE personId = ${personId} AND screenOrder= ${screenOrder} AND year =${uni.$person.year} AND screenType=${uni.$screenType}`
	
	
  // console.log("SQL:" + sql);
	if(sql!=''){
		return new Promise((resolve, reject) => {
		  plus.sqlite.selectSql({
		    name: 'tb_screen',
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


//根据患者id次人的采集组最近的记录id
export function getPddToId(id) {
  let setStr = '';
  let sql = '';

  sql = `SELECT id FROM ${tbScreenPpd} WHERE personId= ${id} AND year=${uni.$person.year} AND screenType=${uni.$screenType} ORDER BY screenTime DESC LIMIT 1`;
  // console.log("SQL:" + sql);

  return new Promise((resolve, reject) => {
    plus.sqlite.selectSql({
      name: 'tb_screen',
      sql: sql,
      success(e) {
        resolve(e);
      },
      fail(e) {
        reject(e);
      }
    });
  });
}

//统计ppd组
export function statisticsNum(time) {
  let setStr = '';
  let sql = '';

 sql = `SELECT COUNT(CASE WHEN strftime('%Y', screenTime) = strftime('%Y', '${time}') THEN 1 END) AS yearNum,
  					COUNT(CASE WHEN strftime('%Y', screenTime) = strftime('%Y', '${time}')  AND a.outcome = 1 THEN 1 END) AS yearYangNum,
					COUNT(CASE WHEN strftime('%Y', screenTime) = strftime('%Y', '${time}')  AND a.outcome = 0 THEN 1 END) AS yearYinNum,
  					COUNT(CASE WHEN strftime('%Y-%m', screenTime) = strftime('%Y-%m', '${time}')  THEN 1 END) AS monthlyNum,
  					COUNT(CASE WHEN strftime('%Y-%m', screenTime) = strftime('%Y-%m', '${time}') AND a.outcome = 1 THEN 1 END) AS monthlyYangNum,
					COUNT(CASE WHEN strftime('%Y-%m', screenTime) = strftime('%Y-%m', '${time}') AND a.outcome = 0 THEN 1 END) AS monthlyYinNum,
  					COUNT(CASE WHEN strftime('%Y-%m-%d', screenTime) = strftime('%Y-%m-%d', '${time}') THEN 1 END) AS dayNum,
  					COUNT(CASE WHEN strftime('%Y-%m-%d', screenTime) = strftime('%Y-%m-%d', '${time}') AND a.outcome = 1 THEN 1 END ) AS dayYangNum,
					COUNT(CASE WHEN strftime('%Y-%m-%d', screenTime) = strftime('%Y-%m-%d', '${time}') AND a.outcome = 1 THEN 1 END ) AS dayYinNum,
					COUNT(CASE WHEN a.injectionWay = 1 THEN 1 END ) AS ppdNum,
					COUNT(CASE WHEN a.injectionWay = 2 THEN 1 END ) AS ecNum,
					COUNT(CASE WHEN a.injectionWay = 3 THEN 1 END ) AS igraNum,
					COUNT(CASE WHEN a.transverseDiameter >= 10 THEN 1 END ) AS xLengthGtTenNum,
					COUNT(CASE WHEN a.longitudinalDiameter <= 10 THEN 1 END ) AS yLengthLtTenNum,
					COUNT(CASE WHEN a.bleb LIKE '%4%' THEN 1 END ) AS lymphangitisNum,
					COUNT(CASE WHEN a.bleb LIKE '%1%' THEN 1 END ) AS doubleCircleNum,
					COUNT(CASE WHEN a.bleb LIKE '%2%' THEN 1 END ) AS blisterNum,
					COUNT(CASE WHEN a.bleb LIKE '%3%' THEN 1 END ) AS necrosisNum
  					FROM (SELECT * FROM (SELECT screenId,screenTime,outcome,transverseDiameter,longitudinalDiameter,bleb,injectionWay FROM ${tbScreenPpd} WHERE screenType = ${uni.$screenType} AND year=${uni.$person.year}  ORDER BY id DESC) AS b GROUP BY screenId) AS a;`;
					

  return new Promise((resolve, reject) => {
    plus.sqlite.selectSql({
      name: 'tb_screen',
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
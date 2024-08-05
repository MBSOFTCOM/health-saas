import { user } from "./sqlite";

// 数据库名
export const dbName = 'tb_screen'
//用户表
export const tbScreenUser = "tb_screen_user"


//根据账号查看用户信息
export function getUser(userName) {
  let sql = '';

  sql = `SELECT * FROM ${tbScreenUser} WHERE name = '${userName}'`;
  // console.log("SQL:" + sql);

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


//根据账号查看用户信息
export function getUserList() {
  let sql = '';

  sql = `SELECT * FROM ${tbScreenUser}`;
  console.log("SQL:" + sql);

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
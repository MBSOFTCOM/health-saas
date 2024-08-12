import dbUtils from "@/uni_modules/zjy-sqlite-manage/components/zjy-sqlite-manage/dbUtils.js" //sqlite-manage插件
import request from '@/utils/request'
import {dbName,promise,tbScreenConsume,emptyData} from "@/utils/sqlite";

export const getConsumeData = async (data) => {
  return request({
      url: ``,
      method: 'GET',
      data: data,
  })
}
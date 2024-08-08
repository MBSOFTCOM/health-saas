import request from "../../utils/request";

export function getCTOutcomeByOrder(data){
    return request({
        url:'/tb/screen-computed-tomography/outcome',
        method:'GET',
        params:data
    })
}

/**
 * 获取ct数据分页
 * @param data
 * @returns {Promise<unknown>}
 */
export function getCtList(data){
    return request({
        url:'/tb/screen-computed-tomography/page',
        method:'GET',
        params:data
    })
}

/**
 * 获取页面统计卡片
 * @returns {Promise<unknown>}
 */
export function getSta(data){
    return request({
        url:'/tb/screen-computed-tomography/statiscs',
        method:'GET',
        params:data
    })
}
/**
 * 获取ct数据分页
 * @param data
 * @returns {Promise<unknown>}
 */
export function getOneCt(data){
    // 使用promise的形式返回
    return request({
        url:'/tb/screen-computed-tomography/getOne',
        method:'GET',
        params:data
    })
}
/**
 * 获取ct数据分页
 * @param data
 * @returns {Promise<unknown>}
 */
export function getOrderList(data){
    return request({
        url:'/tb/screen-computed-tomography/getOrders',
        method:'GET',
        params:data
    })
}
/**
 *根据
 * @param personId 患者id
 */
export function getMaxScreenOrder(data){
    // console.log(data)
    return request({
        url:'/tb/screen-computed-tomography/maxOrder',
        method:'GET',
        params:data
    })
}
/**
 * 新增记录
 * @returns {Promise | Promise<unknown>}
 */
export function create(data){
    return request({
        url:'/tb/screen-computed-tomography/create',
        method:'POST',
        data:data
    })
}

/**
 * 新增ct的事务，后端处理
 * @returns {Promise | Promise<unknown>}
 */
export function createTransaction(data){
    return request({
        url:'/tb/screen-computed-tomography/createTrans',
        method:'POST',
        data:data
    })
}

/**
 * 新增ct的事务，后端处理
 * @returns {Promise | Promise<unknown>}
 */
export function getCreateOrder(data){
    return request({
        url:'/tb/screen-computed-tomography/create-order',
        method:'POST',
        data:data
    })
}
/**
 * 新增记录
 * @returns {Promise | Promise<unknown>}
 */
export function update(data){
    return request({
        url:'/tb/screen-computed-tomography/update',
        method:'POST',
        data:data
    })
}

/**
 * 插入图片表
 * @param util
 * @param data
 * @returns {Promise<unknown>}
 */
export async function insertImg(util,data){
    return request({
        url:'/tb/screen-chest-radiograph/update',
        method:'POST',
        data:data
    })
}
import request from "../../utils/request";

/**
 * 汇总表创建记录
 * @param data
 * @returns {Promise | Promise<unknown>}
 */
export function create(data){
    return request({
        url:'/tb/screen-sum/create',
        method:"POST",
        data:data
    })
}
/**
 * 汇总表创建记录
 * @param data
 * @returns {Promise | Promise<unknown>}
 */
export function selectCount(data){
    return request({
        url:'/tb/screen-sum/count',
        method:"GET",
        params:data
    })
}

/**
 * 汇总表修改记录
 * @param data
 * @returns {Promise | Promise<unknown>}
 */
export function update(data){
    return request({
        url:'/tb/screen-sum/update',
        method:"POST",
        data:data
    })
}

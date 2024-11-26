import request from '@/utils/request'

// 查询工作项列表
export function listItem(query) {
    return request({
        url: '/dev-api/project/item/list',
        method: 'get',
        params: query
    })
}

// 查询工作项详细
export function getItem(workItemId) {
    return request({
        url: '/dev-api/project/item/' + workItemId,
        method: 'get'
    })
}

// 新增工作项
export function addItem(data) {
    return request({
        url: '/dev-api/project/item',
        method: 'post',
        data: data
    })
}

// 修改工作项
export function updateItem(data) {
    return request({
        url: '/dev-api/project/item',
        method: 'put',
        data: data
    })
}

// 删除工作项
export function delItem(workItemIds) {
    return request({
        url: '/dev-api/project/item/' + workItemIds,
        method: 'delete'
    })
}

// 导出工作项
export function exportItem(query) {
    return request({
        url: '/dev-api/project/item/export',
        method: 'post',
        data: query,
        responseType: 'blob'
    })
}
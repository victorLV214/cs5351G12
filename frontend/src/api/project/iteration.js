import request from '@/utils/request'

// 查询迭代列表
export function listIteration(query) {
    return request({
        url: '/dev-api/project/iteration/list',
        method: 'get',
        params: query
    })
}

// 查询迭代详细信息
export function getIteration(iterationId) {
    return request({
        url: '/dev-api/project/iteration/' + iterationId,
        method: 'get'
    })
}

// 新增迭代
export function addIteration(data) {
    return request({
        url: '/dev-api/project/iteration',
        method: 'post',
        data: data
    })
}

// 修改迭代
export function updateIteration(data) {
    return request({
        url: '/dev-api/project/iteration',
        method: 'put',
        data: data
    })
}

// 删除迭代
export function deleteIteration(iterationId) {
    return request({
        url: '/dev-api/project/iteration/' + iterationId,
        method: 'delete'
    })
}
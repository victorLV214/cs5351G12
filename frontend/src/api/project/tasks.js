import request from '@/utils/request'

// 查询任务列表
export function listTask(query) {
    return request({
        url: '/dev-api/project/task/list',
        method: 'get',
        params: query
    })
}

// 新增任务
export function addTask(data) {
    return request({
        url: '/dev-api/project/task',
        method: 'post',
        data: data
    })
}

// 修改任务
export function updateTask(data) {
    return request({
        url: '/dev-api/project/task',
        method: 'put',
        data: data
    })
}

// 获取任务详细信息
export function getTask(taskId) {
    return request({
        url: '/dev-api/project/task/' + taskId,
        method: 'get'
    })
}

// 删除任务
export function delTask(taskId) {
    return request({
        url: '/dev-api/project/tas k/' + taskId,
        method: 'delete'
    })
}
import request from '@/utils/request'

// 查询项目列表
export function listProject(query) {
    return request({
        url: '/dev-api/project/list',
        method: 'get',
        params: query
    })
}

// 查询单个项目详情
export function getProject(projectId) {
    return request({
        url: '/dev-api/project/' + projectId,
        method: 'get'
    })
}

// 新增项目
export function addProject(data) {
    return request({
        url: '/dev-api/project',
        method: 'post',
        data: data
    })
}

// 修改项目
export function updateProject(data) {
    return request({
        url: '/dev-api/project',
        method: 'put',
        data: data
    })
}

// 删除项目
export function delProject(projectIds) {
    return request({
        url: '/dev-api/project/' + projectIds,
        method: 'delete'
    })
}

// 导出项目
export function exportProject(query) {
    return request({
        url: '/dev-api/project/export',
        method: 'get',
        params: query
    })
}
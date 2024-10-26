import request from '@/utils/request'

// 查询需求列表
export function listRequirement(projectId, query) {
    return request({
        url: `/dev-api/project/${projectId}/requirement/list`,
        method: 'get',
        params: query
    })
}

// 获取需求详细信息
export function getRequirement(projectId, reqId) {
    return request({
        url: `/dev-api/project/${projectId}/requirement/${reqId}`,
        method: 'get'
    })
}

// 新增需求
export function addRequirement(projectId, data) {
    return request({
        url: `/dev-api/project/${projectId}/requirement`,
        method: 'post',
        data: data
    })
}

// 修改需求
export function updateRequirement(projectId, data) {
    return request({
        url: `/dev-api/project/${projectId}/requirement`,
        method: 'put',
        data: data
    })
}

// 删除需求
export function delRequirement(projectId, requirementIds) {
    return request({
        url: `/dev-api/project/${projectId}/requirement/${requirementIds}`,
        method: 'delete'
    })
}

// 导出需求列表
export function exportRequirement(projectId, query) {
    return request({
        url: `/dev-api/project/${projectId}/requirement/export`,
        method: 'get',
        params: query
    })
}
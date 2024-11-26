import request from '@/utils/request'

// 查询需求列表
export function listRequirement(query) {
    return request({
        url: '/dev-api/project/requirement/list',
        method: 'get',
        params: query
    })
}


export function getRequirementDetail(requirementId) {
    return request({
        url: `/dev-api/project/requirement/${requirementId}`,
        method: 'get'
    })
}

// 新增需求
export function addRequirement(data) {
    return request({
        url: `/dev-api/project/requirement`,
        method: 'post',
        data: data
    })
}

// 修改需求
export function updateRequirement(data) {
    return request({
        url: `/dev-api/project/requirement`,
        method: 'put',
        data: data
    })
}

// 删除需求
export function delRequirement(requirementId) {
    return request({
        url: `/dev-api/project/requirement/${requirementId}`,
        method: 'delete'
    })
}

// 导出需求列表
export function exportRequirement(query) {
    return request({
        url: `/dev-api/project/requirement/export`,
        method: 'post',
        data: query,
        responseType: 'blob' ,
    })
}
import request from '@/utils/request'

// 查询项目成员列表
export function listProjectMember(query) {
    return request({
        url: '/dev-api/project/member/list',
        method: 'get',
        params: query
    })
}

// 查询项目成员详细
export function getProjectMember(projectMemberId) {
    return request({
        url: '/dev-api/project/member/' + projectMemberId,
        method: 'get'
    })
}

// 新增项目成员
export function addProjectMember(data) {
    return request({
        url: '/dev-api/project/member',
        method: 'post',
        data: data
    })
}

// 修改项目成员
export function updateProjectMember(data) {
    return request({
        url: '/dev-api/project/member',
        method: 'put',
        data: data
    })
}

// 删除项目成员
export function delProjectMember(projectMemberIds) {
    return request({
        url: '/dev-api/project/member/' + projectMemberIds,
        method: 'delete'
    })
}

// 导出项目成员
export function exportProjectMember(query) {
    return request({
        url: '/dev-api/project/member/export',
        method: 'post',
        data: query,
        responseType: "blob"
    })
}
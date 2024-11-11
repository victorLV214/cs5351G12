import request from '@/utils/request';

// 查询冲刺列表
export function listSprints(query) {
    return request({
        url: '/dev-api/sprint/list',
        method: 'get',
        params: query
    });
}

// 查询单个冲刺详细
export function getSprint(sprintId) {
    return request({
        url: `/dev-api/sprint/${sprintId}`,
        method: 'get'
    });
}

// 新增冲刺
export function addSprint(data) {
    return request({
        url: '/dev-api/sprint',
        method: 'post',
        data: data
    });
}

// 修改冲刺
export function updateSprint(data) {
    return request({
        url: '/dev-api/sprint',
        method: 'put',
        data: data
    });
}

// 删除冲刺
export function delSprint(sprintId) {
    return request({
        url: `/dev-api/sprint/${sprintId}`,
        method: 'delete'
    });
}
// Fetch burndown data for a specific sprint
export function getBurndownData(sprintId) {
    return request({
        url: `/dev-api/sprint/${sprintId}/burndown`, // Endpoint to get burndown data
        method: 'get'
    });
}
// 导出冲刺
export function exportSprints(query) {
    return request({
        url: '/dev-api/sprint/export',
        method: 'get',
        params: query
    });
}
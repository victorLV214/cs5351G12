import request from '@/utils/request'

// 查询缺陷列表
export function listDefect(query) {
    return request({
        url: '/dev-api/project/defect/list',
        method: 'get',
        params: query
    })
}

// 查询缺陷详细
export function getDefect(defectId) {
    return request({
        url: '/dev-api/project/defect/' + defectId,
        method: 'get'
    })
}

// 新增缺陷
export function addDefect(data) {
    return request({
        url: '/dev-api/project/defect',
        method: 'post',
        data: data
    })
}

// 修改缺陷
export function updateDefect(data) {
    return request({
        url: '/dev-api/project/defect',
        method: 'put',
        data: data
    })
}

// 删除缺陷
export function delDefect(defectIds) {
    return request({
        url: `/dev-api/project/defect/${defectIds}`,
        method: 'delete'
    })
}

// 导出缺陷
export function exportDefect(query) {
    return request({
        url: '/dev-api/project/defect/export',
        method: 'post',
        data: query,
        responseType: "blob"
    })
}
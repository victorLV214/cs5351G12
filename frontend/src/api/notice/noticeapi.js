import request from '@/utils/request'


export function getUserNoticeList(userId) {
    return request({
        url: '/system/user/notice/list',
        method: 'get',
        params: { userId }
    })
}
export function updateNoticeStatus(data) {
    return request({
        url: '/system/user/notice/status',
        method: 'put',
        data: data
    })
}
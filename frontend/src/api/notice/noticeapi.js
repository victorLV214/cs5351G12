import request from '@/utils/request'


export function getUserNoticeList(userId) {
    return request({
        url: '/system/user/notice/list',
        method: 'get',
        params: { userId }
    })
}
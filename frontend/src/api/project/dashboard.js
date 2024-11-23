import request from '@/utils/request'


export function getDashboardData() {
    return request({
        url: '/dev-api/dashboard',
        method: 'get'
    })
}
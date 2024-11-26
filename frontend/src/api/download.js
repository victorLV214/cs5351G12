import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

export function exportProjectList(query) {
    return request({
        url: '/dev-api/project/export',
        method: 'post',
        data: query,
        responseType: 'blob' ,
    })
}

// 导出项目列表
export function exportProject(query){
    return request({
        url: '/dev-api/project/export',
        method: 'post',
        data: query,
        responseType: 'blob' ,
    })
}

// 导出项目成员列表
export function exportProjectMember(query){
    return request({
        url: '/dev-api/project/member/export',
        method: 'post',
        data: query,
        responseType: 'blob' ,
    })
}

// 导出用户参与的项目列表
export function exportMyProjectList (query) {
    return request({
        url: '/dev-api/project/my/export',
        method: 'post',
        data: query,
        responseType: 'blob' ,
    })
}




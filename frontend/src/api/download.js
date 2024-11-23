import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";



export function exportProject(query) {
    return request({
        url: '/dev-api/project/export',
        method: 'post',
        data: query,
        responseType: 'blob' ,
    })
}
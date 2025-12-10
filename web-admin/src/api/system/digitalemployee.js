import request from '@/utils/request'

// 查询digitalemployee列表
export function listDigitalemployee(query) {
  return request({
    url: '/system/digitalemployee/list',
    method: 'get',
    params: query
  })
}

// 查询digitalemployee详细
export function getDigitalemployee(id) {
  return request({
    url: '/system/digitalemployee/' + id,
    method: 'get'
  })
}

// 新增digitalemployee
export function addDigitalemployee(data) {
  return request({
    url: '/system/digitalemployee',
    method: 'post',
    data: data
  })
}

// 修改digitalemployee
export function updateDigitalemployee(data) {
  return request({
    url: '/system/digitalemployee',
    method: 'put',
    data: data
  })
}

// 删除digitalemployee
export function delDigitalemployee(id) {
  return request({
    url: '/system/digitalemployee/' + id,
    method: 'delete'
  })
}

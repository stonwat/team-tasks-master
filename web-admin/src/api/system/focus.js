import request from '@/utils/request'

// 查询focus列表
export function listFocus(query) {
  return request({
    url: '/system/focus/list',
    method: 'get',
    params: query
  })
}

// 查询focus详细
export function getFocus(id) {
  return request({
    url: '/system/focus/' + id,
    method: 'get'
  })
}

// 新增focus
export function addFocus(data) {
  return request({
    url: '/system/focus',
    method: 'post',
    data: data
  })
}

// 修改focus
export function updateFocus(data) {
  return request({
    url: '/system/focus',
    method: 'put',
    data: data
  })
}

// 删除focus
export function delFocus(id) {
  return request({
    url: '/system/focus/' + id,
    method: 'delete'
  })
}

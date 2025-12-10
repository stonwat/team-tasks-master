import request from '@/utils/request'

// 查询应用目标清单列表
export function listList(query) {
  return request({
    url: '/system/list/list',
    method: 'get',
    params: query
  })
}

// 查询应用目标清单详细
export function getList(id) {
  return request({
    url: '/system/list/' + id,
    method: 'get'
  })
}

// 新增应用目标清单
export function addList(data) {
  return request({
    url: '/system/list',
    method: 'post',
    data: data
  })
}

// 修改应用目标清单
export function updateList(data) {
  return request({
    url: '/system/list',
    method: 'put',
    data: data
  })
}

// 删除应用目标清单
export function delList(id) {
  return request({
    url: '/system/list/' + id,
    method: 'delete'
  })
}

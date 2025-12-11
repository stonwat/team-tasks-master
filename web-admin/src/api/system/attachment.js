import request from '@/utils/request'

// 查询文件类型列表
export function listAttachment(query) {
  return request({
    url: '/system/attachment/list',
    method: 'get',
    params: query
  })
}

// 查询文件类型详细
export function getAttachment(id) {
  return request({
    url: '/system/attachment/' + id,
    method: 'get'
  })
}

// 新增文件类型
export function addAttachment(data) {
  return request({
    url: '/system/attachment',
    method: 'post',
    data: data
  })
}

// 修改文件类型
export function updateAttachment(data) {
  return request({
    url: '/system/attachment',
    method: 'put',
    data: data
  })
}

// 删除文件类型
export function delAttachment(id) {
  return request({
    url: '/system/attachment/' + id,
    method: 'delete'
  })
}

import request from '@/utils/request'

// 查询团队风采列表
export function listShowcase(query) {
  return request({
    url: '/admin/showcase/list',
    method: 'get',
    params: query
  })
}

// 查询团队风采详细
export function getShowcase(id) {
  return request({
    url: '/admin/showcase/' + id,
    method: 'get'
  })
}

// 新增团队风采
export function addShowcase(data) {
  return request({
    url: '/admin/showcase',
    method: 'post',
    data: data
  })
}

// 修改团队风采
export function updateShowcase(data) {
  return request({
    url: '/admin/showcase',
    method: 'put',
    data: data
  })
}

// 删除团队风采
export function delShowcase(id) {
  return request({
    url: '/admin/showcase/' + id,
    method: 'delete'
  })
}

import request from '@/utils/request'

// 查询管理-团队成员列表
export function listTeam(query) {
  return request({
    url: '/admin/team/list',
    method: 'get',
    params: query
  })
}

// 查询管理-团队成员详细
export function getTeam(id) {
  return request({
    url: '/admin/team/' + id,
    method: 'get'
  })
}

// 新增管理-团队成员
export function addTeam(data) {
  return request({
    url: '/admin/team',
    method: 'post',
    data: data
  })
}

// 修改管理-团队成员
export function updateTeam(data) {
  return request({
    url: '/admin/team',
    method: 'put',
    data: data
  })
}

// 删除管理-团队成员
export function delTeam(id) {
  return request({
    url: '/admin/team/' + id,
    method: 'delete'
  })
}

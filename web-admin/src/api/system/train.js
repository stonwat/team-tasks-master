import request from '@/utils/request'

// 查询train列表
export function listTrain(query) {
  return request({
    url: '/system/train/list',
    method: 'get',
    params: query
  })
}

// 查询train详细
export function getTrain(id) {
  return request({
    url: '/system/train/' + id,
    method: 'get'
  })
}

// 新增train
export function addTrain(data) {
  return request({
    url: '/system/train',
    method: 'post',
    data: data
  })
}

// 修改train
export function updateTrain(data) {
  return request({
    url: '/system/train',
    method: 'put',
    data: data
  })
}

// 删除train
export function delTrain(id) {
  return request({
    url: '/system/train/' + id,
    method: 'delete'
  })
}

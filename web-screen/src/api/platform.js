import request from '@/utils/request';

/**
 * 平台相关API接口
 */

// 获取平台列表
export function getPlatforms() {
  return request.get('/platforms');
}

// 获取平台详情
export function getPlatformDetail(id) {
  return request.get(`/platforms/${id}`);
}

// 创建平台
export function createPlatform(data) {
  return request.post('/platforms', data);
}

// 更新平台信息
export function updatePlatform(id, data) {
  return request.put(`/platforms/${id}`, data);
}

// 删除平台
export function deletePlatform(id) {
  return request.delete(`/platforms/${id}`);
}

// 更新平台状态
export function updatePlatformStatus(id, status) {
  return request.put(`/platforms/${id}/status`, { status });
}

// 获取平台统计数据
export function getPlatformStats() {
  return request.get('/platforms/stats');
}

// 搜索平台
export function searchPlatforms(keyword) {
  return request.get('/platforms/search', { keyword });
}

export default {
  getPlatforms,
  getPlatformDetail,
  createPlatform,
  updatePlatform,
  deletePlatform,
  updatePlatformStatus,
  getPlatformStats,
  searchPlatforms
};
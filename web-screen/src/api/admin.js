import request from '@/utils/request';

/**
 * 用户相关API接口
 */

// 获取项目列表
export function getProjectList(params) {
  return request.get('/system/list/list',params
  );
}

// 获取重点任务列表
export function getFocusList() {
  return request.get('/system/focus/list');
}

// 获取培训会议等代办列表
export function getTrainList() {
  return request.get('/system/train/list');
}
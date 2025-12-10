import request from '@/utils/request';

/**
 * 模型相关API接口
 */

// 获取模型列表
export function getModelList() {
  return request.get('/system/datascreen/mode/view');
}

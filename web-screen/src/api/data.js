import request from '@/utils/request';

/**
 * 数据相关API接口
 */

// 获取数据标注列表
export function getDatasetList() {
  return request.get('/system/datascreen/data/view');
}

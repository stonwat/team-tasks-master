import request from '@/utils/request';

//获取监控页面今日实时数据接口
export function getPowerDataRealTiming(){
  return request.get('/system/datascreen/resource/view');
}

//获取使用情况
export function getPowerUsage(params){
  return request.get('/system/datascreen/resource/monitor', params);
}

export default{
  getPowerDataRealTiming,
  getPowerUsage
}
/**
 * 封装fetch请求
 */

// 基础配置
const BASE_URL = 'http://136.192.230.68:9010';

// 默认请求配置
const DEFAULT_CONFIG = {
  timeout: 10000, // 10秒超时
  headers: {
    'Content-Type': 'application/json'
  }
};

/**
 * 请求拦截器
 * @param {Object} config - 请求配置
 * @returns {Object} 处理后的配置
 */
function requestInterceptor(config) {
  // 可以在这里添加token等通用请求头
  // const token = localStorage.getItem('token');
  // if (token) {
  //   config.headers.Authorization = `Bearer ${token}`;
  // }
  
  return config;
}

/**
 * 响应拦截器
 * @param {Response} response - 原始响应
 * @returns {Promise} 处理后的响应数据
 */
async function responseInterceptor(response) {
  // 检查响应状态
  if (!response.ok) {
    const errorData = await response.json().catch(() => ({}));
    throw new Error(errorData.msg || `请求失败: ${response.status} ${response.statusText}`);
  }
  
  // 解析JSON数据
  const data = await response.json();
  
  // 检查业务状态码
  if (data.code && data.code !== 200) {
    throw new Error(data.msg|| '请求失败');
  }
  
  return data;
}

/**
 * 超时控制
 * @param {number} timeout - 超时时间(ms)
 * @returns {Promise} 超时Promise
 */
function timeoutPromise(timeout) {
  return new Promise((_, reject) => {
    setTimeout(() => {
      reject(new Error('请求超时'));
    }, timeout);
  });
}

/**
 * 发送请求
 * @param {string} url - 请求地址
 * @param {Object} options - 请求选项
 * @returns {Promise} 响应数据
 */
async function request(url, options = {}) {
  // 合并配置
  const config = {
    ...DEFAULT_CONFIG,
    ...options,
    headers: {
      ...DEFAULT_CONFIG.headers,
      ...options.headers
    }
  };
  
  // 请求拦截
  const interceptedConfig = requestInterceptor(config);
  
  // 构造完整URL
  const fullUrl = url.startsWith('http') ? url : BASE_URL + url;
  
  try {
    // 发送请求（带超时控制）
    const response = await Promise.race([
      fetch(fullUrl, interceptedConfig),
      timeoutPromise(interceptedConfig.timeout)
    ]);
    
    // 响应拦截
    return await responseInterceptor(response);
  } catch (error) {
    console.error('请求错误:', error);
    throw error;
  }
}

/**
 * GET请求
 * @param {string} url - 请求地址
 * @param {Object} params - 查询参数
 * @param {Object} options - 请求选项
 * @returns {Promise} 响应数据
 */
export function get(url, params = {}, options = {}) {
  // 构造查询字符串
  const queryString = new URLSearchParams(params).toString();
  const fullUrl = queryString ? `${url}?${queryString}` : url;
  
  return request(fullUrl, {
    method: 'GET',
    ...options
  });
}

/**
 * POST请求
 * @param {string} url - 请求地址
 * @param {Object} data - 请求数据
 * @param {Object} options - 请求选项
 * @returns {Promise} 响应数据
 */
export function post(url, data = {}, options = {}) {
  return request(url, {
    method: 'POST',
    body: JSON.stringify(data),
    ...options
  });
}

/**
 * PUT请求
 * @param {string} url - 请求地址
 * @param {Object} data - 请求数据
 * @param {Object} options - 请求选项
 * @returns {Promise} 响应数据
 */
export function put(url, data = {}, options = {}) {
  return request(url, {
    method: 'PUT',
    body: JSON.stringify(data),
    ...options
  });
}

/**
 * DELETE请求
 * @param {string} url - 请求地址
 * @param {Object} options - 请求选项
 * @returns {Promise} 响应数据
 */
export function del(url, options = {}) {
  return request(url, {
    method: 'DELETE',
    ...options
  });
}

// 导出默认对象
export default {
  get,
  post,
  put,
  delete: del,
  request
};
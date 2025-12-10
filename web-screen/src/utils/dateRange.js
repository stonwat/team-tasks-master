/**
 * 日期格式化工具（抽离复用，避免重复代码）
 * @param {Date} date - 需格式化的Date对象
 * @returns {string} 格式化后的日期字符串（YYYY-MM-DD HH:mm:ss）
 */
const formatDate = (date) => {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0'); // 月份0-11，需+1
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  const seconds = String(date.getSeconds()).padStart(2, '0');

  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
};

/**
 * 生成日期范围：从6天前00:00:00到今天23:59:59（近7天）
 * @returns {Object} { start: 开始时间(YYYY-MM-DD HH:mm:ss), end: 结束时间(YYYY-MM-DD HH:mm:ss) }
 */
export function getLast7DaysRange() {
  const today = new Date();

  // 结束时间：今天23:59:59
  const endDate = new Date(today);
  endDate.setHours(23, 59, 59, 0);

  // 开始时间：6天前00:00:00（6天前+今天=7天）
  const startDate = new Date(today);
  startDate.setDate(today.getDate() - 6); // 日期减6天
  startDate.setHours(0, 0, 0, 0);

  return {
    start: formatDate(startDate),
    end: formatDate(endDate)
  };
}

/**
 * 生成日期范围：从29天前00:00:00到今天23:59:59（近30天）
 * @returns {Object} { start: 开始时间(YYYY-MM-DD HH:mm:ss), end: 结束时间(YYYY-MM-DD HH:mm:ss) }
 */
export function getLast30DaysRange() {
  const today = new Date();

  // 结束时间：与近7天逻辑一致（今天23:59:59）
  const endDate = new Date(today);
  endDate.setHours(23, 59, 59, 0);

  // 开始时间：29天前00:00:00（29天前+今天=30天）
  const startDate = new Date(today);
  startDate.setDate(today.getDate() - 29); // 日期减29天
  startDate.setHours(0, 0, 0, 0);

  return {
    start: formatDate(startDate),
    end: formatDate(endDate)
  };
}
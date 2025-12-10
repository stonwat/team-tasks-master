


/**
 * 计算任务精确结束时间（精确到分钟）
 * @param {string} startDateStr - 开始时间，格式 "YYYY-MM-DD HH:mm"
 * @param {number} days - 工期天数（每天按8小时计算）
 * @param {string[][]} exceptionPeriods - 例外时间段数组
 * @param {string[][]} overTimePeriods - 加班时间段数组
 * @param {number[]} regularWorkdays - 常规工作日数组（0=周日）
 * @param {string[][]} workTimeConfig - 工作时间配置数组
 * @returns {string} 结束时间字符串
 */
export function calculateEndTime(startDateStr, days, exceptionPeriods, overTimePeriods, regularWorkdays, workTimeConfig) {

  // console.info(startDateStr)
  // console.info(days)
  // console.info(exceptionPeriods)
  // console.info(overTimePeriods)
  // console.info(regularWorkdays)
  // console.info(workTimeConfig)

  // 日期解析工具函数（处理24:00特殊格式）
  const parseDate = (str) => {
    const [datePart, timePart] = str.split(' ');
    const [year, month, day] = datePart.split('-').map(Number);
    let hours = 0, minutes = 0;

    // 处理 "24:00" 格式（视为次日0点）
    if (timePart === '24:00') {
      return new Date(year, month - 1, day + 1, 0, 0);
    } else {
      // 分解时间部分
      const [time, modifier] = timePart.includes('PM') ? [timePart.replace('PM', ''), 'PM'] : [timePart, 'AM'];
      [hours, minutes] = time.split(':').map(Number);
      // 处理12小时制转换
      if (modifier === 'PM' && hours < 12) hours += 12;
      if (modifier === 'AM' && hours === 12) hours = 0;
    }
    return new Date(year, month - 1, day, hours, minutes);
  };

  // 判断时间是否在指定时间段内
  const isInPeriod = (date, periods) =>
    periods.some(([startStr, endStr]) => {
      const start = parseDate(startStr);
      const end = parseDate(endStr);
      return date >= start && date < end;
    });

  // 核心计算逻辑
  let currentTime = parseDate(startDateStr);
  let remainingMinutes = days * 8 * 60; // 总需工作分钟数

  // 逐分钟推进时间（优化后改为按时间段跳跃）
  while (remainingMinutes > 0) {
    // 判断当前时间有效性 --------------------------------
    const isOvertime = isInPeriod(currentTime, overTimePeriods);
    const isException = !isOvertime && isInPeriod(currentTime, exceptionPeriods);
    const isRegularDay = regularWorkdays.includes(currentTime.getDay());
    const isValidTime = isOvertime || (!isException && isRegularDay);

    // 非有效时间的处理：直接跳到下一个可能的有效时间点
    if (!isValidTime) {
      const nextDay = new Date(currentTime);
      nextDay.setDate(nextDay.getDate() + 1);
      nextDay.setHours(0, 0, 0, 0); // 跳到下一天的00:00
      currentTime = nextDay;
      continue;
    }

    // 有效时间的处理 --------------------------------
    // 获取当天工作时间段配置（转换为分钟数）
    const dayStart = new Date(currentTime);
    dayStart.setHours(0, 0, 0, 0);
    const workBlocks = workTimeConfig
      .map(([start, end]) => ({
        start: start.split(':').map(Number),
        end: end.split(':').map(Number)
      }))
      .map(({ start: [sh, sm], end: [eh, em] }) => ({
        start: sh * 60 + sm,
        end: eh * 60 + em
      }))
      .sort((a, b) => a.start - b.end); // 按开始时间排序

    // 遍历工作时间段
    let workedToday = false;
    for (const block of workBlocks) {
      // 转换时间段为具体日期对象
      const blockStart = new Date(dayStart.getTime() + block.start * 60 * 1000);
      const blockEnd = new Date(dayStart.getTime() + block.end * 60 * 1000);

      // 跳过已过的时间段
      if (currentTime >= blockEnd) continue;

      // 确定实际开始时间
      const effectiveStart = currentTime < blockStart ? blockStart : currentTime;
      if (effectiveStart >= blockEnd) continue;

      // 计算可用时间（单位：分钟）
      const availableMs = Math.min(blockEnd - effectiveStart, remainingMinutes * 60 * 1000);
      const availableMinutes = Math.floor(availableMs / 1000 / 60);

      // 更新时间状态
      if (availableMinutes > 0) {
        remainingMinutes -= availableMinutes;
        currentTime = new Date(effectiveStart.getTime() + availableMinutes * 60 * 1000);
        workedToday = true;
        if (remainingMinutes <= 0) break;
      }
    }

    // 当天无可用时间段的处理：跳到下一天
    if (!workedToday) {
      currentTime = new Date(dayStart.getTime() + 24 * 60 * 60 * 1000);
    }
  }

  // 格式化输出结果 --------------------------------
  const pad = n => String(n).padStart(2, '0');
  return `${currentTime.getFullYear()}-${pad(currentTime.getMonth() + 1)}-${pad(currentTime.getDate())} ` +
    `${pad(currentTime.getHours())}:${pad(currentTime.getMinutes())}`;
}



/**
 * 主计算函数：计算工作持续时间和实际工作天数
 * @returns {Object} 包含总分钟数(durationMinutes)和天数(workDays)
 */
export function calculateWorkingDuration(_taskStart,_taskEnd,_workWeekDays,_workDates,_workHours,_exceptionPeriods) {

  console.info(_taskStart)
  console.info(_taskEnd)
  console.info(_workWeekDays)
  console.info(_workDates)
  console.info(_workHours)
  console.info(_exceptionPeriods)


  // ==================== 参数配置 ====================
  // 例外时间段（需要排除的非工作时间）
  // const exceptionPeriods = [
  //     ["2015-03-11 00:00", "2015-03-12 24:00"],
  //     ["2015-03-13 00:00", "2015-03-14 24:00"]
  // ].map(p => ({ start: parseTime(p[0]), end: parseTime(p[1])}));


  const exceptionPeriods = _exceptionPeriods.map(p => ({ start: parseTime(p[0]), end: parseTime(p[1])}));

  // console.info(exceptionPeriods)

  // 加班时间段（覆盖例外日期和工作日限制）
  // const workDates = [
  //     ["2015-03-11 00:00", "2015-03-11 24:00"],
  //     ["2015-03-12 00:00", "2015-03-12 24:00"]
  // ].map(p => ({ start: parseTime(p[0]), end: parseTime(p[1])}));

  const workDates = _workDates.map(p => ({ start: parseTime(p[0]), end: parseTime(p[1])}));


  // console.info(workDates)


  // 常规工作日定义（0=周日，1=周一，...6=周六）
  // 周一到周五
  //const workWeekDays = [1, 2, 3, 4, 5];
  const workWeekDays =_workWeekDays



  // 工作时间配置（适用于常规和加班日期）
  // const workHours = [
  //     { startH: 9, startM: 0, endH: 12, endM: 0 },  // 上午工作时间
  //     { startH: 13, startM: 0, endH: 18, endM: 0 }  // 下午工作时间
  // ];
  const workHours =_workHours.map(p=>{
    return {
      startH: parseInt(p[0].split(":")[0]),
      startM: parseInt(p[0].split(":")[1]),
      endH: parseInt(p[1].split(":")[0]),
      endM: parseInt(p[1].split(":")[1])
    }
  })

  // 任务时间范围
  // const taskStart = parseTime("2015-03-01 09:00");
  // const taskEnd = parseTime("2015-03-31 18:00");
  const taskStart = parseTime(_taskStart);
  const taskEnd = parseTime(_taskEnd);

  // ==================== 核心逻辑 ====================
  // 步骤1：生成常规工作时间段（仅限工作日）
  const normalPeriods = generateNormalPeriods(taskStart, taskEnd, workWeekDays, workHours);

  // 步骤2：生成加班时间段（覆盖指定日期）
  const overtimePeriods = generateOvertimePeriods(workDates, workHours);

  // 步骤3：合并所有时间段
  const allPeriods = [...normalPeriods, ...overtimePeriods];

  // 步骤4：计算剩余有效例外时间段（排除被加班覆盖的部分）
  const remainingExceptions = getEffectiveExceptions(exceptionPeriods, overtimePeriods);

  // 步骤5：排除剩余例外时间段的影响
  const filteredPeriods = subtractExceptions(allPeriods, remainingExceptions);

  // 步骤6：合并重叠时间段
  const mergedPeriods = mergePeriods(filteredPeriods);

  // 步骤7：计算结果
  return {
    durationMinutes: calculateMinutes(mergedPeriods),
    workDays: calculateWorkDays(mergedPeriods)
  };
}

// ==================== 工具函数 ====================

/**
 * 时间字符串解析器（支持24:00格式）
 * @param {string} timeStr - 时间字符串，格式：YYYY-MM-DD HH:mm
 * @returns {Date} 解析后的日期对象
 */
function parseTime(timeStr) {
  const [datePart, timePart] = timeStr.split(' ');
  const [year, month, day] = datePart.split('-').map(Number);
  let [hours = 0, minutes = 0] = (timePart || '').split(':').map(Number);

  // 处理24:00表示次日0点
  if (hours === 24) {
    return new Date(year, month - 1, day + 1, 0, minutes);
  }
  return new Date(year, month - 1, day, hours, minutes);
}

/**
 * 生成常规工作时间段（仅限工作日）
 * @param {Date} startDate - 任务开始时间
 * @param {Date} endDate - 任务结束时间
 * @param {number[]} workDays - 有效工作日数组（0-6）
 * @param {Object[]} workHours - 工作时间配置
 * @returns {Object[]} 时间段数组 {start: Date, end: Date}
 */
function generateNormalPeriods(startDate, endDate, workDays, workHours) {
  const periods = [];
  let currentDay = new Date(startDate);
  currentDay.setHours(0, 0, 0, 0); // 标准化到当日零点

  while (currentDay <= endDate) {
    const dayOfWeek = currentDay.getDay();
    if (workDays.includes(dayOfWeek)) {
      workHours.forEach(({ startH, startM, endH, endM }) => {
        // 创建时间段
        const periodStart = new Date(currentDay);
        periodStart.setHours(startH, startM);
        const periodEnd = new Date(currentDay);
        periodEnd.setHours(endH, endM);

        // 适配任务时间边界
        const adjustedStart = periodStart < startDate ? startDate : periodStart;
        const adjustedEnd = periodEnd > endDate ? endDate : periodEnd;

        if (adjustedStart < adjustedEnd) {
          periods.push({ start: adjustedStart, end: adjustedEnd });
        }
      });
    }
    currentDay.setDate(currentDay.getDate() + 1); // 移至下一天
    currentDay.setHours(0, 0, 0, 0);
  }
  return periods;
}

/**
 * 生成加班时间段（覆盖指定日期）
 * @param {Object[]} workDates - 加班日期范围数组
 * @param {Object[]} workHours - 工作时间配置
 * @returns {Object[]} 时间段数组
 */
function generateOvertimePeriods(workDates, workHours) {
  const periods = [];
  workDates.forEach(({ start, end }) => {
    let currentDay = new Date(start);
    currentDay.setHours(0, 0, 0, 0);
    const endDay = new Date(end);

    // 遍历加班日期范围内的每一天
    while (currentDay <= endDay) {
      workHours.forEach(({ startH, startM, endH, endM }) => {
        const periodStart = new Date(currentDay);
        periodStart.setHours(startH, startM);
        const periodEnd = new Date(currentDay);
        periodEnd.setHours(endH, endM);

        // 适配加班日期边界
        const adjustedStart = periodStart < start ? start : periodStart;
        const adjustedEnd = periodEnd > end ? end : periodEnd;

        if (adjustedStart < adjustedEnd) {
          periods.push({ start: adjustedStart, end: adjustedEnd });
        }
      });
      currentDay.setDate(currentDay.getDate() + 1);
      currentDay.setHours(0, 0, 0, 0);
    }
  });
  return periods;
}

/**
 * 获取有效例外时间段（排除被加班覆盖的部分）
 * @param {Object[]} exceptions - 原始例外时间段
 * @param {Object[]} overtimePeriods - 加班时间段
 * @returns {Object[]} 剩余有效例外时间段
 */
function getEffectiveExceptions(exceptions, overtimePeriods) {
  let remaining = [...exceptions];
  overtimePeriods.forEach(ovt => {
    remaining = remaining.flatMap(excl => splitException(excl, ovt));
  });
  return remaining.filter(p => p.start < p.end);
}

/**
 * 分割例外时间段（排除与加班重叠部分）
 * @param {Object} excl - 例外时间段
 * @param {Object} ovt - 加班时间段
 * @returns {Object[]} 切割后的时间段数组
 */
function splitException(excl, ovt) {
  const exclStart = excl.start.getTime();
  const exclEnd = excl.end.getTime();
  const ovtStart = ovt.start.getTime();
  const ovtEnd = ovt.end.getTime();

  // 无交集时保留原例外
  if (exclEnd <= ovtStart || exclStart >= ovtEnd) return [excl];

  // 切割为前后两部分
  const parts = [];
  if (exclStart < ovtStart) parts.push({ start: excl.start, end: ovt.start });
  if (exclEnd > ovtEnd) parts.push({ start: ovt.end, end: excl.end });
  return parts;
}

/**
 * 排除例外时间段的影响
 * @param {Object[]} periods - 原始时间段数组
 * @param {Object[]} exceptions - 例外时间段数组
 * @returns {Object[]} 排除后的有效时间段
 */
function subtractExceptions(periods, exceptions) {
  let result = [];
  periods.forEach(period => {
    let remaining = [period];
    exceptions.forEach(excl => {
      const temp = [];
      remaining.forEach(seg => {
        temp.push(...splitSegment(seg, excl));
      });
      remaining = temp;
    });
    result.push(...remaining);
  });
  return result.filter(p => p.start < p.end);
}

/**
 * 切割时间段（处理与例外的重叠）
 * @param {Object} seg - 原始时间段
 * @param {Object} excl - 例外时间段
 * @returns {Object[]} 切割后的时间段数组
 */
function splitSegment(seg, excl) {
  const segStart = seg.start.getTime();
  const segEnd = seg.end.getTime();
  const exclStart = excl.start.getTime();
  const exclEnd = excl.end.getTime();

  // 无交集时保留原时段
  if (segEnd <= exclStart || segStart >= exclEnd) return [seg];

  // 切割为前后两部分
  const parts = [];
  if (segStart < exclStart) parts.push({ start: seg.start, end: excl.start });
  if (segEnd > exclEnd) parts.push({ start: excl.end, end: seg.end });
  return parts;
}

/**
 * 合并重叠时间段
 * @param {Object[]} periods - 时间段数组
 * @returns {Object[]} 合并后的时间段数组
 */
function mergePeriods(periods) {
  if (periods.length === 0) return [];

  // 按开始时间排序
  const sorted = [...periods].sort((a, b) => a.start - b.start);
  const merged = [sorted[0]];

  for (let i = 1; i < sorted.length; i++) {
    const last = merged[merged.length - 1];
    const current = sorted[i];

    // 检测重叠并合并
    if (current.start <= last.end) {
      last.end = new Date(Math.max(last.end.getTime(), current.end.getTime()));
    } else {
      merged.push(current);
    }
  }
  return merged;
}

/**
 * 计算总工作时间（分钟）
 * @param {Object[]} periods - 时间段数组
 * @returns {number} 总分钟数
 */
function calculateMinutes(periods) {
  return periods.reduce((total, p) => {
    return total + Math.round((p.end - p.start) / (1000 * 60));
  }, 0);
}

/**
 * 计算实际工作天数
 * @param {Object[]} periods - 时间段数组
 * @returns {number} 去重后的日期数量
 */
function calculateWorkDays(periods) {
  const daySet = new Set();
  periods.forEach(p => {
    daySet.add(p.start.toISOString().slice(0, 10)); // 按日期去重
  });
  return daySet.size;
}

// ==================== 执行与测试 ====================
// const result = calculateWorkingDuration();
// console.log('总工作时间:', result.durationMinutes + '分钟');
// console.log('实际工作天数:', result.workDays + '天');

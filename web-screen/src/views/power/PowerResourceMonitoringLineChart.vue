<template>
    <div id="child-page" class="power-resource-monitoring-line-chart">
    <div class="header-box">
      <span class="header-title">算力资源监控</span>
    </div>
    <div class="filter-btn-group">
      <!-- 左侧筛选下拉 -->
      <div class="filter-btn-left">
        <div class="custom-select">
          <div class="select-trigger" @click="isDropdownOpen = !isDropdownOpen">
            {{ selectedComputeItem || '请选择指标' }}
            <i class="arrow" :class="{ 'rotate': isDropdownOpen }"></i>
          </div>
          <div class="select-options" v-if="isDropdownOpen">
            <div v-for="(item, index) in computeFilterItems" :key="index" class="select-option"
              :class="{ 'selected': selectedComputeItem === item.name }" @click="selectComputeItem(item)">
              {{ item.name }}
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧时间筛选 -->
      <div class="filter-btn-right">
        <div class="time-filter-item" :class="{ 'selected': timeRangeSelected === '7days' }"
          @click="handleTimeFilter('7days')">
          近7天
        </div>
        <div class="time-filter-item" :class="{ 'selected': timeRangeSelected === '30days' }"
          @click="handleTimeFilter('30days')">
          近30天
        </div>
      </div>
    </div>

    <!-- 图表组件 -->
    <PowerLineChart :UsageDataFirst="usageDataFirst" :UsageDataSecond="legendData.length > 1 ? usageDataSecond : null" :xAxisData="timeData"
      :chartTitle="chartTitle" :legendData="legendData" style="width: 100%;" />
  </div>
</template>

<script>
import PowerLineChart from '@/components/echart-ai-office/line-chart/PowerLineChart.vue';

import { getLast7DaysRange, getLast30DaysRange } from '@/utils/dateRange';

import { getPowerUsage } from '@/api/power.js';

export default {
  components: { PowerLineChart },
  data() {
    return {
      // 左侧下拉选项
      computeFilterItems: [
        {
          name: 'CPU',
          resourceType: 'CPU' //根据资源类型访问接口 
        },
        {
          name: '内存',
          resourceType: 'MEMORY'
        },
        {
          name: 'AI加速卡',
          resourceType: 'GPU'
        },
        {
          name: 'NPU',
          resourceType: 'NPU'
        },
        {
          name: 'DCU',
          resourceType: 'DCU'
        },
        {
          name: '磁盘',
          resourceType: 'DISK'
        },
        {
          name: '网络接口流量',
          resourceType: 'NETWORK_RECEIVE'
        }
      ],
      // 动态图表标题和图例
      chartTitle: 'CPU使用情况(核)',
      legendData: ['CPU使用量', 'CPU请求量'],
      selectedComputeItem: '', // 当前选中项
      selectedResourceType: 'CPU',
      isDropdownOpen: false,   // 下拉框展开状态
      dateRange: {},// 默认时间范围

      // 图表数据
      usageDataFirst: [],
      usageDataSecond: [],
      timeData: [],
      timeRangeSelected: '7days',

    };

  },
  mounted() {
    this.dateRange = getLast7DaysRange();
    this.loadPowerUsage();
  },
  methods: {
    // 选择计算指标
    selectComputeItem(item) {
      this.selectedComputeItem = item.name;
      this.selectedResourceType = item.resourceType;
      this.isDropdownOpen = false;
      this.legendData = [];
      this.usageDataSecond = [];
      // 根据选择的指标设置不同的标题和图例
      const configMap = {
        'CPU': {
          title: 'CPU使用情况(核)',
          legend: ['CPU使用量', 'CPU请求量']
        },
        '内存': {
          title: '内存使用情况(GB)',
          legend: ['内存使用量', '内存请求量']
        },
        'AI加速卡': {
          title: 'AI加速卡使用情况(卡)',
          legend: ['AI加速卡使用量', 'AI加速卡请求量']
        },
        'NPU': {
          title: 'NPU使用情况(卡)',
          legend: ['NPU使用量', 'NPU请求量']
        },
        'DCU': {
          title: 'DCU使用情况(卡)',
          legend: ['DCU使用量', 'DCU请求量']
        },
        '磁盘': {
          title: '磁盘使用情况(GB)',
          legend: ['磁盘使用量']
        },
        '网络接口流量': {
          title: '网络接口流量情况(kb/s)',
          legend: ['网络接收速率', '网络发送速率']
        }
      };

      const config = configMap[item.name];
      if (config) {
        this.chartTitle = config.title;
        this.legendData = config.legend;

        this.loadPowerUsage(item.name);
      }


    },

    // 时间范围筛选逻辑
    handleTimeFilter(range) {
      this.timeRangeSelected = range;
      if (range === '7days') {
        this.timeData = [];
        this.dateRange = getLast7DaysRange();
        this.loadPowerUsage();
      } else if (range === '30days') {
        this.timeData = [];
        this.dateRange = getLast30DaysRange();
        this.loadPowerUsage();
      }
    },
    // 算力使用情况接口调用
    async loadPowerUsage(itemName) {
      try {
        const params = {
          gmtCreateStart: this.dateRange.start,
          gmtCreateEnd: this.dateRange.end,
          resourceType: this.selectedResourceType
        };

        // 使用await等待接口返回
        const response = await getPowerUsage(params);
        const res = response.data.data;

        // 根据itemName处理数据
        if (itemName === '磁盘使用情况') {
          this.usageDataFirst = res.map(item => item.usedResource);
          this.usageDataSecond = [];
        } else {
          this.usageDataFirst = res.map(item => item.usedResource);
          this.usageDataSecond = res.map(item => item.requestResource);
        }

        // 处理时间数据
        this.timeData = res.map(item => {
          const datePrefix = item.date.substring(5, 10);
          return `${datePrefix}\n${item.weekDay}`;
        });
      } catch (error) {
        // 捕获接口请求异常
        console.error('获取算力使用数据失败:', error);
        // 可在此处添加错误提示，如this.$message.error('数据加载失败')
      }
    }

  }
};
</script>

<style lang="scss" scoped>
@import '@/assets/scss/components/child-page.scss';

.power-resource-monitoring-line-chart {
  width: 100%;
}

.filter-btn-group {
  display: flex;
  justify-content: space-between;
  gap: 15px;
  margin-bottom: 10px;
  align-items: center;
}

/* 左侧筛选下拉样式 */
.filter-btn-left {
  display: flex;
  position: relative;
}

.custom-select {
  position: relative;
  margin-left: 5px;
  width: 140px;
}

.select-trigger {
  padding: 8px 14px;
  background-color: #171c33;
  color: #d3d6dd;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s;
  font-size: 14px;
  display: flex;
  justify-content: space-between;
  align-items: center;

  &:hover {
    background-color: #1a5cd7;
    color: #fff;
  }
}

.arrow {
  display: inline-block;
  width: 0;
  height: 0;
  margin-left: 8px;
  border-left: 4px solid transparent;
  border-right: 4px solid transparent;
  border-top: 6px solid #d3d6dd;
  transition: transform 0.3s;

  &.rotate {
    transform: rotate(180deg);
  }
}

.select-options {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  margin-top: 5px;
  font-size: 14px;
  background-color: #171c33;
  border-radius: 6px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
  z-index: 10;
}

.select-option {
  padding: 10px 24px;
  color: #d3d6dd;
  cursor: pointer;
  transition: all 0.2s;

  &:hover {
    background-color: #1a5cd7;
    color: #fff;
  }

  &.selected {
    background-color: #1890ff;
    color: #fff;
  }
}

/* 右侧时间筛选样式（不变） */
.filter-btn-right {
  display: flex;
  gap: 10px;
  margin-right: 20px;
}

.time-filter-item {
  padding: 4px 16px;
  background-color: #171c33;
  color: #d3d6dd;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s;
  font-size: 14px;
  white-space: nowrap;

  &.selected {
    background-color: #fff;
    color: #000;
    box-shadow: 0 0 10px rgba(24, 144, 255, 0.3);
  }

  &:hover {
    background-color: #fff;
    color: #000;
  }
}
</style>
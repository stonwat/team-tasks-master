<template>
  <div class="performance-chart-container">
    <!-- 标题栏：模型选择 + 指标切换 -->
    <div class="chart-header">
      <h3 class="panel-title">模型性能趋势分析</h3>
      <div class="chart-controls">
        <!-- 模型选择下拉 -->
        <select 
          v-model="selectedModel" 
          @change="updateChart"
          class="model-select"
        >
          <option v-for="model in modelOptions" :key="model.id" :value="model.id">
            {{ model.name }} (v{{ model.version }})
          </option>
        </select>

        <!-- 指标切换 -->
        <div class="metric-tabs">
          <span 
            class="metric-item" 
            :class="{ active: activeMetric === 'accuracy' }"
            @click="activeMetric = 'accuracy'; updateChart()"
          >
            准确率
          </span>
          <span 
            class="metric-item" 
            :class="{ active: activeMetric === 'loss' }"
            @click="activeMetric = 'loss'; updateChart()"
          >
            损失值
          </span>
          <span 
            class="metric-item" 
            :class="{ active: activeMetric === 'f1' }"
            @click="activeMetric = 'f1'; updateChart()"
          >
            F1分数
          </span>
        </div>
      </div>
    </div>

    <!-- ECharts图表容器 -->
    <div class="chart-container" ref="chartRef"></div>
  </div>
</template>

<script>
// 引入ECharts（需项目中安装：npm install echarts）
import * as echarts from 'echarts';

export default {
  data() {
    return {
      // 模型选择列表
      modelOptions: [
        { id: 'model1', name: 'ResNet-50', version: '3.2' },
        { id: 'model2', name: 'BERT-Base', version: '2.1' },
        { id: 'model3', name: 'YOLOv8-S', version: '1.5' }
      ],
      selectedModel: 'model1', // 默认选中模型
      activeMetric: 'accuracy', // 默认显示指标：准确率
      chartInstance: null, // ECharts实例
      // 模拟性能数据（按模型+指标分类）
      performanceData: {
        model1: { // ResNet-50
          accuracy: [68, 75, 82, 86, 89, 92, 94], // 近7天准确率
          loss: [2.1, 1.8, 1.2, 0.9, 0.7, 0.5, 0.3], // 近7天损失值
          f1: [65, 72, 79, 83, 87, 90, 93] // 近7天F1分数
        },
        model2: { // BERT-Base
          accuracy: [72, 78, 83, 85, 88, 90, 91],
          loss: [1.9, 1.6, 1.1, 0.8, 0.6, 0.4, 0.3],
          f1: [69, 75, 81, 84, 86, 89, 90]
        },
        model3: { // YOLOv8-S
          accuracy: [65, 70, 76, 80, 83, 85, 87],
          loss: [2.3, 2.0, 1.5, 1.1, 0.9, 0.7, 0.6],
          f1: [62, 68, 74, 78, 81, 83, 85]
        }
      },
      // X轴：近7天日期
      dateAxis: this.getRecent7Days()
    };
  },
  mounted() {
    // 初始化图表
    this.initChart();
    // 监听窗口resize
    window.addEventListener('resize', this.resizeChart);
  },
  beforeDestroy() {
    // 销毁图表实例
    if (this.chartInstance) {
      this.chartInstance.dispose();
      this.chartInstance = null;
    }
    window.removeEventListener('resize', this.resizeChart);
  },
  methods: {
    // 获取近7天日期（格式：MM-DD）
    getRecent7Days() {
      const days = [];
      for (let i = 6; i >= 0; i--) {
        const date = new Date();
        date.setDate(date.getDate() - i);
        const month = date.getMonth() + 1;
        const day = date.getDate();
        days.push(`${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}`);
      }
      return days;
    },
    // 初始化ECharts图表
    initChart() {
      this.chartInstance = echarts.init(this.$refs.chartRef);
      // 设置图表配置
      this.updateChart();
    },
    // 更新图表数据（切换模型/指标时调用）
    updateChart() {
      const modelData = this.performanceData[this.selectedModel];
      if (!modelData) return;

      // 图表配置项
      const option = {
        // 背景色
        backgroundColor: 'transparent',
        // 图例
        legend: {
          data: [this.getMetricName()],
          top: 0,
          textStyle: { color: '#d3d6dd' }
        },
        // 网格（边距调整）
        grid: {
          left: '3%',
          right: '4%',
          bottom: '15%',
          top: '20%',
          containLabel: true
        },
        // X轴（日期）
        xAxis: {
          type: 'category',
          data: this.dateAxis,
          axisLine: { lineStyle: { color: '#333' } },
          axisLabel: { 
            color: '#d3d6dd',
            rotate: 30 // 日期倾斜，避免重叠
          },
          splitLine: { lineStyle: { color: '#2c3147' } }
        },
        // Y轴（指标值）
        yAxis: {
          type: 'value',
          min: this.getMetricMin(), // 按指标设置最小值
          max: this.getMetricMax(), // 按指标设置最大值
          axisLine: { lineStyle: { color: '#333' } },
          axisLabel: { 
            color: '#d3d6dd',
            formatter: this.getMetricFormatter() // 指标值格式（%或小数）
          },
          splitLine: { lineStyle: { color: '#2c3147' } },
          nameTextStyle: { color: '#d3d6dd' },
          name: this.getMetricName() // Y轴名称
        },
        // 系列（趋势线）
        series: [
          {
            name: this.getMetricName(),
            type: 'line',
            data: modelData[this.activeMetric],
            smooth: true, // 平滑曲线
            symbol: 'circle', // 数据点样式
            symbolSize: 6,
            lineStyle: {
              width: 3,
              color: this.getMetricColor() // 按指标设置线条颜色
            },
            itemStyle: {
              color: this.getMetricColor(),
              borderColor: '#171c33',
              borderWidth: 2
            },
            // 鼠标悬浮提示
            emphasis: {
              itemStyle: { symbolSize: 8 }
            },
            // 填充区域
            areaStyle: {
              color: {
                type: 'linear',
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [
                  { offset: 0, color: this.getMetricColor() + '80' }, // 半透明
                  { offset: 1, color: this.getMetricColor() + '00' }  // 透明
                ]
              }
            },
            // 提示框
            tooltip: {
              trigger: 'item',
              formatter: (params) => {
                return `${this.getMetricName()}: ${params.value}${this.activeMetric !== 'loss' ? '%' : ''}<br/>日期: ${params.name}`;
              },
              backgroundColor: '#0f1325',
              borderColor: '#1890ff',
              borderWidth: 1,
              textStyle: { color: '#d3d6dd' }
            }
          }
        ]
      };

      // 应用配置项
      this.chartInstance.setOption(option);
    },
    // 图表 resize
    resizeChart() {
      if (this.chartInstance) {
        this.chartInstance.resize();
      }
    },
    // 获取指标名称（中文）
    getMetricName() {
      const names = {
        accuracy: '准确率',
        loss: '损失值',
        f1: 'F1分数'
      };
      return names[this.activeMetric];
    },
    // 获取指标最小值
    getMetricMin() {
      if (this.activeMetric === 'loss') return 0;
      return 50; // 准确率/F1分数最小50%
    },
    // 获取指标最大值
    getMetricMax() {
      if (this.activeMetric === 'loss') return 2.5;
      return 100; // 准确率/F1分数最大100%
    },
    // 获取指标值格式器
    getMetricFormatter() {
      if (this.activeMetric === 'loss') return '{value:.2f}';
      return '{value}%';
    },
    // 获取指标颜色
    getMetricColor() {
      const colors = {
        accuracy: '#1890ff', // 蓝色-准确率
        loss: '#ff4d4f',    // 红色-损失值
        f1: '#4CAF50'       // 绿色-F1分数
      };
      return colors[this.activeMetric];
    }
  }
};
</script>

<style lang="scss" scoped>
.performance-chart-container {
  padding: 15px;
  height: calc(100% - 30px);
  display: flex;
  flex-direction: column;
  gap: 15px;
}

// 标题栏 + 控制项
.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 15px;

  .chart-controls {
    display: flex;
    align-items: center;
    gap: 20px;

    // 模型选择下拉
    .model-select {
      padding: 6px 12px;
      background-color: #171c33;
      color: #d3d6dd;
      border: 1px solid #333;
      border-radius: 4px;
      outline: none;
      cursor: pointer;

      &:hover {
        border-color: #1890ff;
      }
    }

    // 指标切换标签
    .metric-tabs {
      display: flex;
      gap: 10px;

      .metric-item {
        padding: 6px 12px;
        color: #d3d6dd;
        background-color: #171c33;
        border-radius: 4px;
        cursor: pointer;
        font-size: 13px;

        &.active {
          background-color: #1890ff;
          color: #fff;
        }

        &:hover:not(.active) {
          background-color: #2c3147;
        }
      }
    }
  }
}

// 图表容器（固定高度，适配父组件）
.chart-container {
  width: 100%;
  height: 220px;
  flex: 1;
  min-height: 200px;
}

// 响应式适配
@media (max-width: 768px) {
  .chart-header {
    flex-direction: column;
    align-items: flex-start !important;
  }

  .chart-controls {
    width: 100%;
    justify-content: space-between;
  }
}
</style>
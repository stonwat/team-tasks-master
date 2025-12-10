<template>
  <!-- ECharts 渲染容器，通过 ref 获取 DOM -->
  <div class="power-usage-line-chart" ref="chartContainer"></div>
</template>

<script>
import * as echarts from 'echarts'; // 导入 ECharts

export default {
  name: 'CpuUsageChart',
  props: {
    // CPU 使用量数据（数组）
    UsageDataFirst: {
      type: Array,
      default: () => []
    },
    // 请求量数据（数组）
    UsageDataSecond: {
      type: Array,
      default: () => []
    },
    // X 轴时间数据（数组）
    xAxisData: {
      type: Array,
      default: () => []
    },
    // 图表标题（字符串）
    chartTitle: {
      type: String,
      default: 'CPU使用情况(核)'
    },
    // 图例数据（数组）
    legendData: {
      type: Array,
      default: () => ['CPU使用量', 'CPU请求量']
    }
  },
  data() {
    return {
      chartInstance: null // 存储 ECharts 实例
    };
  },
  mounted() {
    this.initChart();
    window.addEventListener('resize', this.handleResize);
  },
  beforeDestroy() {
    if (this.chartInstance) {
      this.chartInstance.dispose();
    }
    window.removeEventListener('resize', this.handleResize);
  },
  watch: {
    UsageDataFirst: {
      handler() {
        this.updateChart();
      },
      deep: true
    },
    UsageDataSecond: {
      handler() {
        this.updateChart();
      },
      deep: true
    },
    xAxisData: {
      handler() {
        this.updateChart();
      },
      deep: true
    },
    chartTitle() {
      this.updateChart();
    },
    legendData: {
      handler() {
        this.updateChart();
      },
      deep: true
    }
  },
  methods: {
    initChart() {
      const chartDom = this.$refs.chartContainer;
      console.log('chartDom是否存在:', !!chartDom); // 若为false，说明DOM未
      if (!chartDom) return;
      this.chartInstance = echarts.init(chartDom);
      this.updateChart();
    },
    updateChart() {
      if (!this.chartInstance) return;

      const option = {
        title: {
          text: this.chartTitle,
          textStyle: {
            color: '#cfe2ff',
            fontSize: 14
          }
        },
        legend: {
          data: this.legendData,
          textStyle: {
            color: "#B4B4B4"
          },
          itemWidth: 25,  
          itemHeight: 10, 
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: { type: 'cross' }
        },
        xAxis: {
          type: 'category',
          data: this.xAxisData,
          axisLabel: {
            rotate: 0,
            interval: 0,
            fontSize: 14,
            color: '#fff'
          },
          boundaryGap: false
        },
        yAxis: {
          type: 'value',
          name: '',
          axisLabel: {
            fontSize: 14,
            color: '#fff'
          }
        },
        series: [
          {
            name: this.legendData[0] || 'CPU使用量',
            type: 'line',
            smooth: true,
            lineStyle: { color: '#4895ef' },
            symbol: 'emptyCircle',
            symbolSize: 8,
            data: this.UsageDataFirst
          },
          {
            name: this.legendData[1] || 'CPU请求量',
            type: 'line',
            smooth: true,
            data: this.UsageDataSecond,
            lineStyle: { color: '#6fcf97' },
            symbol: 'emptyCircle',
            symbolSize: 8
          }
        ],
        grid: {
          left: '3%',
          right: '5%',
          bottom: '5%',
          top: '12%',
          containLabel: true
        }
      };

      this.chartInstance.setOption(option);
    },
    handleResize() {
      if (this.chartInstance) {
        this.chartInstance.resize();
      }
    }
  }
};
</script>

<style scoped>
.power-usage-line-chart {
  min-width: 400px;
  min-height: 330px;
}
</style>
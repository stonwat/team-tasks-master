<template>
    <div id="compute-trend-chart">
        <div class="compute-trend-chart">
            <div ref="chartRef" class="chart-container"></div>
        </div>
    </div>
</template>

<script>
import * as echarts from 'echarts';

export default {
    data() {
        return {
            chartInstance: null
        };
    },
    mounted() {
        this.initChart();
        window.addEventListener('resize', this.handleResize);
    },
    beforeDestroy() {
        window.removeEventListener('resize', this.handleResize);
        if (this.chartInstance) {
            this.chartInstance.dispose();
        }
    },
    methods: {
        initChart() {
            if (!this.$refs.chartRef) return;

            // 初始化图表实例
            this.chartInstance = echarts.init(this.$refs.chartRef);

            // 设置图表数据和配置
            const trendData = {
                xData: ["06-12", "06-13", "06-14", "06-15", "06-16", "06-17", "06-18"],
                yData: [7200, 7800, 8100, 7900, 8300, 8400, 8500]
            };

            this.chartInstance.setOption({
                tooltip: { trigger: 'axis' },
                xAxis: {
                    type: 'category',
                    data: trendData.xData,
                    axisLabel: { color: '#d3d6dd' },
                    axisLine: { lineStyle: { color: '#333' } }
                },
                yAxis: {
                    type: 'value',
                    name: '算力(GFLOPS)',
                    nameTextStyle: { color: '#d3d6dd' },
                    axisLabel: { color: '#d3d6dd' },
                    axisLine: { lineStyle: { color: '#333' } }
                },
                series: [{
                    name: '算力使用',
                    type: 'bar',
                    data: trendData.yData,
                    itemStyle: { color: '#33cea0' },
                    barWidth: '60%'
                }]
            });
        },
        handleResize() {
            if (this.chartInstance) {
                this.chartInstance.resize();
            }
        }
    }
};
</script>

<style lang="scss" scoped>
.compute-trend-chart {
    width: 100%;
    height: 100%;

    .chart-container {
        width: 100%;
        height: 380px;
        margin: 10px 0;
    }
}

// 响应式调整
@media (max-width: 768px) {
    .chart-container {
        height: 180px !important;
    }
}
</style>
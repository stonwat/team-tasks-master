<template>
    <div id="real-time-monitoring">
        <h3 class="panel-title">实时算力监控</h3>
        <div ref="gpuChart" class="chart-container"></div>
        <div class="compute-overview">
            <div class="overview-item">
                <span class="label">GPU总数</span>
                <span class="value">{{ gpuTotal }} 张</span>
            </div>
            <div class="overview-item">
                <span class="label">GPU使用率</span>
                <span class="value">{{ gpuUsage }}%</span>
            </div>
            <div class="overview-item">
                <span class="label">CPU使用率</span>
                <span class="value">{{ cpuUsage }}%</span>
            </div>
            <div class="overview-item">
                <span class="label">内存使用率</span>
                <span class="value">{{ memoryUsage }}%</span>
            </div>
        </div>
    </div>
</template>

<script>
import * as echarts from 'echarts';

export default {
    data() {
        return {
            gpuChartInstance: null,
            gpuTotal: 0,
            gpuUsage: 10,
            cpuUsage: 40,
            memoryUsage: 50
        };
    },
    mounted() {
        this.$nextTick(() => {
            this.initGpuChart();
        });
        window.addEventListener('resize', this.handleResize);
    },
    beforeDestroy() {
        window.removeEventListener('resize', this.handleResize);
        if (this.gpuChartInstance) this.gpuChartInstance.dispose();
    },
    methods: {
        initGpuChart() {
            if (this.$refs.gpuChart) {
                this.gpuChartInstance = echarts.init(this.$refs.gpuChart);
                const gpuData = {
                    xData: ["100分钟前", "90分钟前", "80分钟前", "70分钟前", "60分钟前", "50分钟前", "40分钟前", "30分钟前", "20分钟前", "10分钟前"],
                    yData: [58, 62, 65, 63, 66, 69, 67, 68, 69, 68]
                };
                this.gpuChartInstance.setOption({
                    tooltip: { trigger: 'axis' },
                    xAxis: {
                        type: 'category',
                        data: gpuData.xData,
                        axisLabel: { color: '#d3d6dd' },
                        axisLine: { lineStyle: { color: '#333' } }
                    },
                    yAxis: {
                        type: 'value',
                        max: 100,
                        name: '使用率(%)',
                        nameTextStyle: { color: '#d3d6dd' },
                        axisLabel: { color: '#d3d6dd' },
                        axisLine: { lineStyle: { color: '#333' } }
                    },
                    series: [{
                        name: 'GPU使用率',
                        type: 'line',
                        data: gpuData.yData,
                        itemStyle: { color: '#1890ff' },
                        lineStyle: { color: '#1890ff', width: 2 },
                        areaStyle: {
                            color: {
                                type: 'linear',
                                x: 0, y: 0, x2: 0, y2: 1,
                                colorStops: [
                                    { offset: 0, color: 'rgba(24, 144, 255, 0.3)' },
                                    { offset: 1, color: 'rgba(24, 144, 255, 0)' }
                                ]
                            }
                        }
                    }]
                });
            }
        },
        handleResize() {
            if (this.gpuChartInstance) this.gpuChartInstance.resize();
        }
    }
};
</script>

<style lang="scss" scoped>
.panel-title {
    color: #fff;
    font-size: 16px;
    margin: 10px 0;
    padding-left: 10px;
    border-left: 3px solid #1890ff;
}

.chart-container {
    width: 100%;
    height: 220px;
    margin: 10px 0;
}

.compute-overview {
    display: flex;
    justify-content: space-between;
    flex-wrap: wrap;
    margin-top: 15px;
    padding: 0 10px;

    .overview-item {
        display: flex;
        flex-direction: column;
        align-items: center;
        width: 22%;
        min-width: 80px;
        margin-bottom: 10px;

        .label {
            color: #d3d6dd;
            font-size: 12px;
            margin-bottom: 3px;
        }

        .value {
            color: #fff;
            font-size: 16px;
            font-weight: bold;
        }
    }
}
</style>
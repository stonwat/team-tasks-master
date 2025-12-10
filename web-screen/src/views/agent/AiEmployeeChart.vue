<template>
    <div id="child-page">
        <div class="header-box">
            <span class="header-title">内部应用统计</span>
            <dv-decoration-3 class="dv-dec-3" />
        </div>
        <div class="ai-employee-chart">
            <!-- 原生按钮切换 -->
            <!-- <div class="tool">
                <button v-for="t in types" :key="t.value" :class="['switch-btn', { active: chartType === t.value }]"
                    @click="chartType = t.value">
                    {{ t.label }}
                </button>
            </div> -->

            <!-- ECharts 容器 -->
            <div ref="chartContainer" class="chart-container"></div>
        </div>
    </div>
</template>

<script>
import * as echarts from 'echarts'

// 合并后的完整数据（含图片1、图片2）
const rawList = [
    // ======= 原图片1数据 =======
    { scene: '装维调度与自服务数字员工', target: 90, actual: 54.98 },
    { scene: '综维数字员工', target: 80, actual: 73.48 },
    { scene: '隐患管控与故障处置数字员工', target: 50, actual: 16.65 },
    { scene: '网优数字员工', target: 85, actual: 62.38 },
    { scene: '智能客服助理', target: 80, actual: 0 },
    { scene: '多模态客服机器人', target: 90, actual: 83.34 },
    { scene: '门店宝', target: 50, actual: 1.24 },
    { scene: 'AI客户助理', target: 30, actual: 20.9 },
    { scene: '小微ICT智能体', target: 67, actual: 0 },
    { scene: '精准营销智能体', target: 50, actual: 31 },

    // ======= 图片2新增数据 =======
    { scene: 'AI质检专家', target: 85, actual: 85 },
    { scene: '网安研判智能体', target: 99, actual: 99 },
    { scene: '研发智能体', target: 40, actual: 93.29 },
    { scene: '规划评审智能体', target: 80, actual: 80 },
    { scene: '产数支撑智能体', target: 80, actual: 49.91 },
    { scene: '采供管理智能体', target: 50, actual: 16.07 },
    { scene: '穿透式监管智能体', target: 85, actual: 85 },
    { scene: '舆情管理智能体', target: 100, actual: 100 },
    { scene: '改革信息编写智能体', target: 50, actual: 50 },
    { scene: '智能监考', target: 70, actual: 70 },

    // ======= File2 管控型补充 =======
    { scene: '司库风险助手', target: 50, actual: 50 },
    { scene: '审计慧查风险洞察智能体', target: 50, actual: 50 },
    { scene: '合同管理智能体', target: 50, actual: 50 },
    { scene: '巡视成果审核智能体', target: 50, actual: 50 },
    { scene: '党建知识助手', target: 50, actual: 50 },
    { scene: '“连心桥”平台', target: 95, actual: 95 },
    { scene: '投关管理智能体', target: 70, actual: 70 }
]

export default {
    name: 'AiEmployeeChart',
    data() {
        return {
            chartType: 'bar',
            chartInstance: null,
            types: [
                { value: 'bar', label: '柱状图' },
                { value: 'line', label: '折线图' }
            ]
        }
    },
    computed: {
        xAxisData() { return rawList.map(i => i.scene) },
        targetData() { return rawList.map(i => i.target) },
        actualData() { return rawList.map(i => i.actual) }
    },
    watch: { chartType() { this.updateChart() } },
    mounted() {
        this.initChart()
        window.addEventListener('resize', this.handleResize)
    },
    beforeDestroy() {
        if (this.chartInstance) this.chartInstance.dispose()
        window.removeEventListener('resize', this.handleResize)
    },
    methods: {
        initChart() {
            const dom = this.$refs.chartContainer
            if (!dom) return
            this.chartInstance = echarts.init(dom)
            this.updateChart()
        },

        /* ========  官方同款高级 option  ========= */
        updateChart() {
            if (!this.chartInstance) return

            const chartType = this.chartType

            // ① 玫瑰渐变（搬自官方）
            function roseGrad(color) {
                return new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                    { offset: 0, color: echarts.color.lift(color, 0.5) },
                    { offset: 1, color: color }
                ])
            }

            // ② 系列配置
            const series = [
                {
                    name: '目标分数',
                    type: chartType,
                    smooth: chartType === 'line',
                    barWidth: chartType === 'bar' ? 16 : undefined,
                    lineStyle: { width: 3, color: '#5470c6' },
                    itemStyle: { color: roseGrad('#5470c6'), borderRadius: chartType === 'bar' ? [3, 3, 0, 0] : 0 },
                    emphasis: { itemStyle: { shadowBlur: 10, shadowColor: 'rgba(84,112,198,0.8)' } },
                    data: this.targetData
                },
                {
                    name: '当前分数',
                    type: chartType,
                    smooth: chartType === 'line',
                    barWidth: chartType === 'bar' ? 16 : undefined,
                    lineStyle: { width: 3, color: '#91cc75' },
                    itemStyle: { color: roseGrad('#91cc75'), borderRadius: chartType === 'bar' ? [3, 3, 0, 0] : 0 },
                    emphasis: { itemStyle: { shadowBlur: 10, shadowColor: 'rgba(145,204,117,0.8)' } },
                    data: this.actualData
                }
            ]

            // ③ 官方同款 option
            const option = {
                title: {
                    text: 'AI 数字员工指标达成情况',
                    left: 'center',
                    textStyle: { color: '#cfe2ff', fontSize: 16, fontWeight: 'bold' }
                },
                tooltip: {
                    trigger: 'axis',
                    axisPointer: { type: 'shadow', label: { show: true } },
                    backgroundColor: 'rgba(0,0,0,0.7)',
                    borderColor: '#007bff',
                    borderWidth: 1,
                    textStyle: { color: '#fff' },
                    // ④ 千元格式化（同官方）
                    formatter: function (params) {
                        let tpl = []
                        params.forEach(i => {
                            const val = (+i.value).toLocaleString()
                            tpl.push(`${i.marker} ${i.seriesName}: ${val}`)
                        })
                        return params[0].name + '<br/>' + tpl.join('<br/>')
                    }
                },
                legend: {
                    data: ['目标值', '当前值'],
                    top: 32,
                    textStyle: { color: '#B4B4B4' },
                    itemWidth: 25,
                    itemHeight: 10
                },
                grid: {
                    left: '3%',
                    right: '5%',
                    bottom: '15%',
                    top: '18%',
                    containLabel: true,
                    backgroundColor: 'rgba(255,255,255,0.02)',
                    borderWidth: 0,
                    shadowColor: 'rgba(0,0,0,0.2)',
                    shadowBlur: 10
                },
                xAxis: {
                    type: 'category',
                    data: this.xAxisData,
                    axisLabel: { color: '#fff', fontSize: 10, interval: 'auto', rotate: 0 },
                    axisLine: { lineStyle: { color: 'rgba(255,255,255,0.2)' } },
                    axisTick: { show: false }
                },
                yAxis: {
                    type: 'value',
                    name: '单位：%',
                    nameTextStyle: { color: '#a0b3c6', fontSize: 12 },
                    axisLabel: { color: '#fff', formatter: '{value}' },
                    axisLine: { show: false },
                    axisTick: { show: false },
                    splitLine: { lineStyle: { color: 'rgba(255,255,255,0.08)' } }
                },
                // ⑤ 官方 dataZoom 三件套
                dataZoom: [
                    { type: 'inside', start: 0, end: 100 },
                    { start: 0, end: 100, height: 20, bottom: 0 }
                ],
                // ⑥ 工具箱
                toolbox: {
                    show: true,
                    right: 20,
                    top: 0,
                    feature: {
                        dataView: { readOnly: false },
                        magicType: { type: ['line', 'bar'] },
                        restore: {},
                        saveAsImage: {}
                    },
                    iconStyle: { borderColor: '#a0b3c6' },
                    emphasis: { iconStyle: { borderColor: '#007bff' } }
                },
                series
            }

            this.chartInstance.setOption(option, true)
        },

        handleResize() {
            this.chartInstance && this.chartInstance.resize()
        }
    }
}
</script>

<style lang="scss" scoped>
@import '@/assets/scss/components/child-page.scss';

.dv-dec-3 {
    position: relative;
    width: 100px;
    height: 25px;
    top: 8px;
}

.ai-employee-chart {
    padding: 16px;
    /* background: #0b1f3b; */
}

.tool {
    position: absolute;
    top: 10px;
    right: 40px;
    margin-bottom: 12px;
    text-align: right;
}

.switch-btn {
    margin-left: 8px;
    padding: 4px 12px;
    border: 1px solid #5470c6;
    background: transparent;
    color: #cfe2ff;
    cursor: pointer;
    border-radius: 3px;
    font-size: 12px;
    transition: all 0.2s;
}

.switch-btn.active {
    background: #5470c6;
    color: #fff;
}

.chart-container {
    width: 100%;
    height: 360px;
}
</style>
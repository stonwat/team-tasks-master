<template>
    <div id="power" ref="appRef">
        <div class="bg">
            <dv-loading v-if="loading">Loading...</dv-loading>
            <div v-else class="host-body">
                <!-- 标题区域 -->
                <div class="d-flex jc-center">
                    <dv-decoration-10 class="dv-dec-10" />
                    <div class="d-flex jc-center">
                        <dv-decoration-8 class="dv-dec-8" :color="decorationColor" />
                        <div class="title">
                            <span class="title-text" @click="gotoIndex">扶摇·数智管理监控平台</span>
                            <dv-decoration-6 class="dv-dec-6" :reverse="true" :color="['#50e3c2', '#67a1e5']" />
                        </div>
                        <dv-decoration-8 class="dv-dec-8" :reverse="true" :color="decorationColor" />
                    </div>
                    <dv-decoration-10 class="dv-dec-10-s" />
                </div>

                <!-- 主Tab栏 -->
                <div class="d-flex jc-between px-2">
                    <div class="d-flex aside-width">
                        <div class="react-left ml-4 react-l-s">
                            <span class="react-left-inner"></span>
                            <span class="text">{{ dateYear }} {{ dateWeek }} {{ dateDay }}</span>
                        </div>
                        <div class="react-left ml-3">
                            <span class="text">实时算力： GFLOPS</span>
                        </div>
                    </div>
                    <div class="d-flex aside-width">
                        <div class="react-right mr-3 react-l-s">
                            <span class="text" style="width: 100%; text-align: center;">算力资源</span>
                        </div>
                        <div class="react-right bg-color-black mr-3">
                            <div class="text fw-b tab-header main-tab" style="display: flex;">
                                <div v-for="(tab, index) in tabs" :key="index" class="tab-item react-right"
                                    :class="{ active: activeTab === index }" @click="changeTab(index)">
                                    {{ tab }}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="body-box">
                    <div class="top-box">
                        <PowerResourceOverviewPanel />
                    </div>
                    <div class="bottom-box">
                        <dv-border-box-12>
                            <PowerResourceMonitoringLineChart />
                        </dv-border-box-12>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import PowerResourceMonitoringLineChart from "./PowerResourceMonitoringLineChart.vue";
import PowerResourceOverviewPanel from "./PowerResourceOverviewPanel.vue";

import drawMixin from "../../utils/drawMixin.js";
import { formatTime } from '../../utils/index.js';

export default {
    mixins: [drawMixin],
    data() {
        return {
            loading: true,
            dateDay: "14: 30: 25",
            dateYear: "2025-06-18",
            dateWeek: "星期二",
            decorationColor: ['#568aea', '#000000'],
            // 主Tab配置
            tabs: ['算力', '平台', '模型', '数据', '应用', '管理'],
            activeTab: 0
        }
    },
    components: {
        PowerResourceMonitoringLineChart,
        PowerResourceOverviewPanel
    },
    mounted() {
        this.initTime();
        // this.$nextTick(() => {
        //     this.initCharts();
        // });
        this.cancelLoading();
    },
    beforeDestroy() {
        window.removeEventListener('resize', this.handleResize);
        if (this.gpuChartInstance) this.gpuChartInstance.dispose();
        if (this.trendChartInstance) this.trendChartInstance.dispose();
    },
    methods: {
        initTime() {
            const now = new Date();
            this.dateDay = formatTime(now, 'HH: mm: ss');
            this.dateYear = formatTime(now, 'yyyy-MM-dd');
            this.dateWeek = ["周日", "周一", "周二", "周三", "周四", "周五", "周六"][now.getDay()];
        },
        handleResize() {
            if (this.gpuChartInstance) this.gpuChartInstance.resize();
            if (this.trendChartInstance) this.trendChartInstance.resize();
        },
        cancelLoading() {
            setTimeout(() => { this.loading = false; }, 500);
        },
        gotoIndex() {
            this.$router.push('/');
        },
        // 主Tab切换
        changeTab(index) {
            this.activeTab = index;
            switch (index) {
                case 0:
                    alert('当前为算力管理页面'); break;
                case 1: this.$router.push('/platform'); break;
                case 2: this.$router.push('/model'); break;
                case 3: this.$router.push('/data'); break;
                case 4: this.$router.push('/agent'); break;
                case 5: this.$router.push('/admin'); break;
                default: alert('该功能暂未开放');
            }
        },

    }
}
</script>

<style lang="scss" scoped>
@import '@/assets/scss/power.scss';

$tab-active-bg: #00ffc8;
$tab-text: #a0b3c6;
$tab-active-text: #000;

.tab-header {
  display: flex;
  gap: 10px;
  padding: 0 4px;
  border-radius: 6px;
  backdrop-filter: blur(4px);
}

.tab-item {
  cursor: pointer;
  border-radius: 4px;
  font-size: 14px;
  color: $tab-text;
  background: transparent;
  transition: all 0.3s ease;

  &:hover {
    color: $tab-active-bg;
  }

  &.active {
    background: $tab-active-bg;
    color: $tab-active-text;
    font-weight: 600;
    box-shadow: 0 0 12px $tab-active-bg;
  }
}

@media (max-width: 768px) {

    .top-box {
        flex-direction: column;
        height: auto;
    }

    .bottom-box {
        flex-direction: column;
        height: auto;
    }
}
</style>
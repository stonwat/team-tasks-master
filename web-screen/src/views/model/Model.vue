<template>
    <div id="model" ref="appRef">
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
                            <span class="text"></span>
                        </div>
                    </div>
                    <div class="d-flex aside-width">
                        <div class="react-right mr-3 react-l-s">
                            <span class="text" style="width: 100%; text-align: center;">模型管理</span>
                        </div>
                        <div class="react-right bg-color-black mr-3">
                            <div class="text fw-b tab-header main-tab" style="display: flex;">
                                <div v-for="(tab, index) in tabs" :key="index" class="tab-item react-right"
                                    :class="{ active: activeTab === index }" @click="changeTab(tab, index)">
                                    {{ tab }}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="body-box">
                    <div class="top-box">
                        <ModelOverviewPanel></ModelOverviewPanel>
                    </div>
                    <div class="bottom-box">
                        <dv-border-box-12 style="height: 600px;">
                            <ModelMarketplace :columns="2" :carouselLimit="4" gap="16px" padding="15px" cardHeight="250px" />
                        </dv-border-box-12>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import drawMixin from "../../utils/drawMixin.js";
import { formatTime } from '../../utils/index.js';

import ModelMarketplace from "./ModelMarketplace.vue";
import ModelOverviewPanel from "./ModeOverviewPanel.vue"

export default {
    mixins: [drawMixin],
    data() {
        return {
            loading: true,
            dateDay: "14: 30: 25",
            dateYear: "2025-06-18",
            dateWeek: "星期二",
            decorationColor: ['#568aea', '#000000'],
            tabs: ['算力', '平台', '模型', '数据', '应用', '管理'],
            activeTab: 2,
            // 筛选项配置：默认全选（checked: true）
            computeFilterItems: [
                { name: '实时监控', checked: true },
                { name: '算力分配', checked: true },
                { name: '任务管理', checked: true }
            ],
            alarmList: [
                { level: "紧急", content: "GPU-03使用率超95%（持续10分钟）", time: "2025-06-18 13:20" },
                { level: "警告", content: "内存使用率接近阈值（85%）", time: "2025-06-18 14:05" }
            ]
        }
    },
    components: {
        ModelMarketplace,
        ModelOverviewPanel
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
        changeTab(tab, index) {
            this.activeTab = index;
            switch (index) {
                case 0: this.$router.push('/power'); break;
                case 1: this.$router.push('/platform'); break;
                case 2:
                    alert('当前为模型管理页面'); break;
                case 3: this.$router.push('/data'); break;
                case 4: this.$router.push('/agent'); break;
                case 5: this.$router.push('/admin'); break;
                default: alert('该功能暂未开放');
            }
        },
        // 筛选按钮切换（选中/取消）
        toggleFilter(index) {
            this.computeFilterItems[index].checked = !this.computeFilterItems[index].checked;
        }
    }
}
</script>

<style lang="scss" scoped>
@import '../../assets/scss/model.scss';

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

    .bottom-box {
        flex-direction: column;
        height: auto;
    }

    .bottom-panel {
        height: 420px;
        margin-bottom: 20px;
    }
}
</style>
<template>
  <div id="index" ref="appRef">
    <div class="bg">
      <dv-loading v-if="loading">Loading...</dv-loading>
      <div v-else class="host-body">
        <div class="d-flex jc-center">
          <dv-decoration-10 class="dv-dec-10" />
          <div class="d-flex jc-center">
            <dv-decoration-8 class="dv-dec-8" :color="decorationColor" />
            <div class="title">
              <span class="title-text">扶摇·数智管理监控平台</span>
              <dv-decoration-6 class="dv-dec-6" :reverse="true" :color="['#50e3c2', '#67a1e5']" />
            </div>
            <dv-decoration-8 class="dv-dec-8" :reverse="true" :color="decorationColor" />
          </div>
          <dv-decoration-10 class="dv-dec-10-s" />
        </div>
        <div class="d-flex jc-between px-2">
          <div class="d-flex aside-width">
            <div class="react-left ml-4 react-l-s">
              <span class="react-left"></span>
              <span class="text">{{ dateYear }} {{ dateWeek }} {{ dateDay }}</span>
            </div>
            <div class="react-left-inner ml-3">
            </div>
          </div>
          <div class="d-flex aside-width">
            <div class="react-right mr-3 react-l-s">
              <span class="text" style="width: 100%; text-align: center;">首页</span>
            </div>
            <div class="react-right bg-color-black mr-3">
              <div class="text fw-b react-right tab-header" style="display: flex;">
                <div v-for="(tab, index) in tabs" :key="index" class="tab-item react-right"
                  :class="{ active: activeTab === index }" @click="changeTab(tab, index)">
                  {{ tab }}
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="body-box">
          <div class="top-box" >
            <dv-border-box-12 style="height: 460px;">
              <AdminKeyTaskProgress containerWidth="100%" containerHeight="450px" listHeight="400px"
                listPadding="0 10px" taskInfoWidth="280px" :scrollThreshold="6" :scrollSpeed="2" />
            </dv-border-box-12>
            <div>
              <AdminOverviewPanel />
              <AdminTodoList containerWidth="90%" listHeight="250px" listPadding="10px 20px" :scrollThreshold="3"
                :scrollSpeed="2" />
            </div>
            <dv-border-box-12 style="height: 460px;">
              <NewsCarousel :interval="3000" />
            </dv-border-box-12>
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
import PowerResourceMonitoringLineChart from "@/views/power/PowerResourceMonitoringLineChart.vue";

import AdminOverviewPanel from '@/views/admin/AdminOverviewPanel.vue';
import AdminKeyTaskProgress from "@/views/admin/AdminKeyTaskProgress.vue";
import AdminTodoList from '@/views/admin/AdminTodoList.vue';
import NewsCarousel from '@/views/NewsCarousel.vue';
import drawMixin from "../utils/drawMixin.js";
import { formatTime } from '../utils/index.js';

export default {
  mixins: [drawMixin],
  data() {
    return {
      timing: null,
      loading: true,
      dateDay: null,
      dateYear: null,
      dateWeek: null,
      weekday: ['周日', '周一', '周二', '周三', '周四', '周五', '周六'],
      decorationColor: ['#568aea', '#000000'],
      tabs: ['算力', '平台', '模型', '数据', '应用', '管理'],
      activeTab: 6
    }
  },
  components: {
    AdminKeyTaskProgress,
    AdminOverviewPanel,
    AdminTodoList,
    PowerResourceMonitoringLineChart,
    NewsCarousel
  },
  mounted() {
    this.timeFn();
    this.cancelLoading();
  },
  beforeDestroy() {
    clearInterval(this.timing)
  },
  methods: {
    timeFn() {
      this.timing = setInterval(() => {
        this.dateDay = formatTime(new Date(), 'HH: mm: ss')
        this.dateYear = formatTime(new Date(), 'yyyy-MM-dd')
        this.dateWeek = this.weekday[new Date().getDay()]
      }, 1000)
    },
    cancelLoading() {
      setTimeout(() => {
        this.loading = false
      }, 500)
    },
    changeTab(tab, index) {
      this.activeTab = index;
      console.log(tab, index);
      switch (index) {
        case 0: this.$router.push('/power'); break;
        case 1: this.$router.push('/platform'); break;
        case 2: this.$router.push('/model'); break;
        case 3: this.$router.push('/data'); break;
        case 4: this.$router.push('/agent'); break;
        case 5: this.$router.push('/admin'); break;
        default: alert('该功能暂未开放');
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/assets/scss/index.scss';

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
</style>

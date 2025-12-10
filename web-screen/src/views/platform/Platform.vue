<template>
  <div id="platform" ref="appRef">
    <div class="bg">
      <dv-loading v-if="loading">Loading...</dv-loading>
      <div v-else class="host-body">
        <!-- 标题区域装饰 -->
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

        <!-- 日期与平台统计区域 -->
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
              <span class="text" style="width: 100%; text-align: center;">平台集成</span>
            </div>
            <div class="react-right bg-color-black mr-3">
              <div class="text fw-b tab-header" style="display: flex;">
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
            <dv-border-box-12>
              <!-- <PlatformCardItem @cardClick="goToPlatform" /> -->
              <PlatformCardItem @cardClick="goToPlatform" containerWidth="1870px" :cardColumns="2" cardGap="10px"
                cardHeight="255px" bodyPadding="10px 20px" :carouselThreshold="6" :showCardCount="6"
                :carouselInterval="6000" />
            </dv-border-box-12>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
// 1. 新增导入平台卡片子组件
import PlatformCardItem from './PlatformCardItem.vue';
// 原版依赖保留
import drawMixin from "../../utils/drawMixin.js";
import { formatTime } from '../../utils/index.js';

export default {
  mixins: [drawMixin],
  // 2. 注册子组件
  components: { PlatformCardItem },
  data() {
    // 原版数据完全保留
    return {
      timing: null,
      loading: true,
      dateDay: null,
      dateYear: null,
      dateWeek: null,
      weekday: ['周日', '周一', '周二', '周三', '周四', '周五', '周六'],
      tabs: ['算力', '平台', '模型', '数据', '应用', '管理'],
      activeTab: 1,
      decorationColor: ['#568aea', '#000000']
    }
  },
  mounted() {
    // 原版逻辑完全保留
    this.timeFn();
    this.cancelLoading();
  },
  beforeDestroy() {
    // 原版逻辑完全保留
    clearInterval(this.timing);
  },
  methods: {
    // 原版时间相关方法完全保留
    timeFn() {
      this.updateTime();
      this.timing = setInterval(() => this.updateTime(), 1000);
    },
    updateTime() {
      const now = new Date();
      this.dateDay = formatTime(now, 'HH: mm: ss');
      this.dateYear = formatTime(now, 'yyyy-MM-dd');
      this.dateWeek = this.weekday[now.getDay()];
    },
    cancelLoading() {
      setTimeout(() => {
        this.loading = false;
      }, 500);
    },
    gotoIndex() {
      this.$router.push('/');
    },
    // 原版卡片点击逻辑完全保留（子组件通过事件触发）
    goToPlatform(platform) {
      if (platform.status === 'active') {
        alert(`即将跳转到 ${platform.name} 平台: ${platform.url || '暂无链接'}`);
      } else {
        alert(`${platform.name} 平台当前不可用（${platform.status === 'maintenance' ? '维护中' : '未知状态'}）`);
      }
    },
    // 原版Tab切换逻辑完全保留
    changeTab(tab, index) {
      this.activeTab = index;
      console.log(tab, index);
      switch (index) {
        case 0: this.$router.push('/power'); break;
        case 1:
          alert('当前为平台管理页面'); break;
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
@import '../../assets/scss/platform.scss';

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
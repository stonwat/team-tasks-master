<template>
  <div id="child-page" class="agent-statistic-map">
    <div class="header-box">
      <span class="header-title">黑龙江省内情况</span>
      <dv-decoration-1 class="dv-dec-1" />
    </div>
    <div>
      
    </div>
    <div class="d-flex jc-center">
      <AgentMapChart />
    </div>
  </div>
</template>

<script>
import AgentMapChart from "@/components/echart-ai-office/map-chart/Index.vue";
export default {
  components: {
    AgentMapChart
  },
};
</script>

<style lang="scss" scoped>
@import '@/assets/scss/components/child-page.scss';

.agent-statistic-map {
  min-width: 500px;
  min-height: 400px;
  border-radius: 5px;

  .dv-dec-1 {
    position: relative;
    width: 100px;
    height: 20px;
    margin-left: 10px;
    top: 10px;
  }
}
</style>
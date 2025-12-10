<template>
  <div>
    <Chart :cdata="cdata" />
  </div>
</template>

<script>
import Chart from './chart.vue';
export default {
  data () {
    return {
      cdata: {
        xData: ["通用", "金融", "公安", "政务", "医疗","企业办公"],
        seriesData: [
          { value: 10, name: "通用" },
          { value: 5, name: "企业办公" },
          { value: 15, name: "公安" },
          { value: 25, name: "政务" },
          { value: 20, name: "医疗" },
          { value: 35, name: "金融" }
        ]
      }
    }
  },
  components: {
    Chart,
  },
  mounted () {
  },
  methods: {
  }
}
</script>

<style lang="scss" scoped>
</style>
<template>
  <div>
    <Chart :cdata="cdata" />
  </div>
</template>

<script>
import Chart from './Chart.vue';
export default {
  data () {
    return {
      cdata: [
        {
          // 名字需要与地图数据文件里面定义的一一对应
          name: '哈尔滨市',
          value: 10,
          elseData:{
            // 这里放置地图 tooltip 里想显示的数据
          }
        },
        {
          name: '齐齐哈尔市',
          value: 9,
        },
        {
          name: '鸡西市',
          value: 8,
        },
        {
          name: '鹤岗市',
          value: 7,
        },
        {
          name: '双鸭山市',
          value: 6,
        },
        {
          name: '大庆市',
          value: 5,
        },
        {
          name: '伊春市',
          value: 4,
        },
        {
          name: '佳木斯市',
          value: 3,
        },
        {
          name: '七台河市',
          value: 2,
        },
        {
          name: '牡丹江市',
          value: 8,
        },
        {
          name: '黑河市',
          value: 6,
        },
        {
          name: '绥化市',
          value: 7,
        },
        {
          name: '大兴安岭地区',
          value: 5,
        }
      ]
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
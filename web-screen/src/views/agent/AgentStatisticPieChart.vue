<template>
    <div id="child-page" class="agent-statistic-pie-chart">
        <div class="header-box">
            <span class="header-title">智能体统计</span>
            <dv-decoration-3 class="dv-dec-3" />
        </div>

        <!-- 饼图 -->
        <div class="d-flex jc-center">
            <Echart :options="options" id="centreLeft1Chart" height="280px" width="260px" />
        </div>

        <!-- 底部数字 -->
        <div class="bottom-data">
            <div class="item-box mt-2" v-for="(item, index) in numberData" :key="index">
                <p class="text-bottom">
                    {{ item.text }}：
                    <span class="colorYellow">{{ item.number.number }}(个)</span>
                </p>
            </div>
        </div>
    </div>
</template>

<script>
import Echart from '@/common/echart'

export default {
    name: 'AgentStatisticPie',
    components: { Echart },
    data() {
        return {
            /* 饼图数据 */
            options: {},
            cdata: {
                xData: ['管控型', '战略型', '价值型'],
                seriesData: [
                    { value: 10, name: '管控型' },
                    { value: 9, name: '战略型' },
                    { value: 8, name: '价值型' }
                ]
            },

            /* 底部数字 */
            numberData: [
                {
                    number: { number: 27, toFixed: 0, textAlign: 'center', content: '{nt}', style: { fontSize: 24 } },
                    text: '内部应用总量'
                }
                // {
                //     number: { number: 1144, toFixed: 0, textAlign: 'left', content: '{nt}', style: { fontSize: 24 } },
                //     text: '总共完成数量'
                // }
            ]
        }
    },
    watch: {
        cdata: {
            handler(newData) {
                this.options = {
                    color: ['#37a2da', '#9fe6b8', '#ff9f7f'],
                    tooltip: { trigger: 'item', formatter: '{a} <br/>{b} : {c} ({d}%)' },
                    legend: {
                        orient: 'horizontal',
                        icon: 'circle',
                        bottom: 0,
                        x: 'center',
                        data: newData.xData,
                        textStyle: { color: '#fff', fontSize: 14 },
                        itemWidth: 14,
                        itemHeight: 14,
                        itemGap: 28
                    },
                    series: [
                        {
                            name: '内部应用数量统计',
                            type: 'pie',
                            radius: [20, 50],
                            roseType: 'area',
                            center: ['50%', '40%'],
                            data: newData.seriesData
                        }
                    ]
                }
            },
            immediate: true,
            deep: true
        }
    },
    mounted() {
        // this.changeTiming()
    },
    methods: {
        changeTiming() {
            setInterval(() => this.changeNumber(), 3000)
        },
        changeNumber() {
            this.numberData.forEach((item, idx) => {
                item.number.number[0] += ++idx
                item.number = { ...item.number }
            })
        }
    }
}
</script>

<style lang="scss" scoped>
@import '@/assets/scss/components/child-page.scss';

$box-width: 335px;
$box-height: 400px;

.agent-statistic-pie-chart {
    min-width: $box-width;
    min-height: $box-height;
    border-radius: 10px;

    .text {
        color: #c3cbde;
    }

    .text-bottom {
        padding-top: 30px;
        color: #c3cbde;
    }

    .dv-dec-3 {
        position: relative;
        width: 100px;
        height: 25px;
        top: 8px;
    }

    .bottom-data {
        display: flex;
        justify-content: center;
        padding-top: 10px;

        .item-box {
            // float: right;       
            // width: 50%;         
            font-size: 16px;
            color: #d3d6dd;
            text-align: center;

            .colorYellow {
                color: yellowgreen;
                font-size: 20px;
            }
        }
    }
}
</style>
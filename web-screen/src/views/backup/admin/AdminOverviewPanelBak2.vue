<template>
    <div id="admin-overview-panel">
        <div class="up">
            <div class="bg-color-black item" v-for="item in titleItem" :key="item.title">
                <p class="colorBlue">{{ item.title }}</p>
                <div>
                    <dv-digital-flop class="dv-dig-flop ml-1 mt-2 pl-3" :config="item.number" />
                </div>
            </div>
        </div>
        <div class="down">
            <!-- <div class="notice">
        <span>
          <icon name="chart-pie" class="text-icon"></icon>
        </span>
        <span style="font-size: 14px">待办事项</span>
        <dv-scroll-board :config="notice" />
      </div> -->
            <div class="percent">
                <div class="item bg-color-black">
                    <span>智算使用率</span>
                    <AdminOverviewPanel style="display: flex;justify-content: center;" :id="rate[0].id"
                        :tips="rate[0].tips" :colorObj="rate[0].colorData" />
                </div>
                <div class="item bg-color-black">
                    <span>通算使用率</span>
                    <AdminOverviewPanel style="display: flex;justify-content: center;" :id="rate[1].id"
                        :tips="rate[1].tips" :colorObj="rate[1].colorData" />
                </div>
                <div class="water">
                    <dv-water-level-pond class="dv-wa-le-po" :config="water" />
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import AdminOverviewPanel from '@/components/echart-ai-office/percent-chart/AdminPercentChart.vue'

export default {
    data() {
        return {
            titleItem: [
                {
                    title: '团队成员(人)',
                    number: {
                        number: [10],
                        toFixed: 0,
                        textAlign: 'left',
                        content: '{nt}',
                        style: {
                            fontSize: 26
                        }
                    }
                },
                {
                    title: '内部项目(个)',
                    number: {
                        number: [18],
                        toFixed: 0,
                        textAlign: 'left',
                        content: '{nt}',
                        style: {
                            fontSize: 26
                        }
                    }
                },
                {
                    title: '外部项目(个)',
                    number: {
                        number: [2],
                        toFixed: 0,
                        textAlign: 'left',
                        content: '{nt}',
                        style: {
                            fontSize: 26
                        }
                    }
                },
                {
                    title: '智算服务器(台)',
                    number: {
                        number: [4],
                        toFixed: 0,
                        textAlign: 'left',
                        content: '{nt}',
                        style: {
                            fontSize: 26
                        }
                    }
                },
                {
                    title: '通算服务器(台)',
                    number: {
                        number: [4],
                        toFixed: 0,
                        textAlign: 'left',
                        content: '{nt}',
                        style: {
                            fontSize: 26
                        }
                    }
                },
                {
                    title: '智能体(个)',
                    number: {
                        number: [100],
                        toFixed: 0,
                        textAlign: 'left',
                        content: '{nt}',
                        style: {
                            fontSize: 26
                        }
                    }
                }
            ],
            notice: {
                data: [
                    ['产品需求评审会'],
                    ['每周例会'],
                    ['技术方案讨论会'],
                    ['客户项目启动会'],
                    ['UI设计评审'],
                    ['市场推广会议'],
                    ['季度总结大会'],
                    ['研发进度同步会'],
                    ['合作伙伴洽谈会'],
                    ['年度战略规划会']
                ],
                rowNum: 5,
                headerBGC: '#0f1325',
                oddRowBGC: '#0f1325',
                evenRowBGC: '#171c33'
            },
            water: {
                data: [40],
                shape: 'roundRect',
                formatter: '',
                waveNum: 3
            },
            // 通过率和达标率的组件复用数据
            rate: [
                {
                    id: 'centerRate1',
                    tips: 60,
                    colorData: {
                        textStyle: '#3fc0fb',
                        series: {
                            color: ['#00bcd44a', 'transparent'],
                            dataColor: {
                                normal: '#03a9f4',
                                shadowColor: '#97e2f5'
                            }
                        }
                    }
                },
                {
                    id: 'centerRate2',
                    tips: 40,
                    colorData: {
                        textStyle: '#67e0e3',
                        series: {
                            color: ['#faf3a378', 'transparent'],
                            dataColor: {
                                normal: '#ff9800',
                                shadowColor: '#fcebad'
                            }
                        }
                    }
                }
            ]
        }
    },
    components: {
        AdminOverviewPanel
    }
}
</script>

<style lang="scss" scoped>
#admin-overview-panel {
    display: flex;
    flex-direction: column;
    margin: 10px;
    // min-width: 600px;

    .up {
        width: 100%;
        display: flex;
        flex-wrap: wrap;
        justify-content: space-around;

        .item {
            border-radius: 6px;
            padding-top: 8px;
            margin-top: 8px;
            width: 32%;
            height: 70px;

            .dv-dig-flop {
                width: 150px;
                height: 30px;
            }
        }
    }

    .down {
        padding: 6px 4px;
        padding-bottom: 0;
        width: 100%;
        display: flex;
        height: 260px;
        justify-content: space-between;

        .bg-color-black {
            border-radius: 5px;
        }

        .notice {
            padding: 8px;
            width: 66%;
            height: 90%;

            .dv-scr-rank-board {
                height: 220px;
            }
        }

        .percent {
            width: 100%;
            display: flex;
            flex-wrap: wrap;

            .item {
                width: 50%;
                height: 120px;

                span {
                    margin-top: 8px;
                    font-size: 14px;
                    display: flex;
                    justify-content: center;
                }
            }

            .water {
                width: 100%;

                .dv-wa-le-po {
                    height: 120px;
                }
            }
        }
    }
}
</style>

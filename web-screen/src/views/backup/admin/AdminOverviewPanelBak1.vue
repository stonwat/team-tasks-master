<template>
  <div id="admin-overview-panel">
    <div class="up">
      <div class="bg-color-black item" v-for="item in titleItem" :key="item.title"
        @click="openDetailModal('dataCard', item)" style="cursor: pointer;">
        <p class="colorBlue">{{ item.title }}</p>
        <div>
          <dv-digital-flop class="dv-dig-flop ml-1 mt-2 pl-3" :config="item.number" />
        </div>
      </div>
    </div>
    <div class="down">
      <div class="percent">
        <div class="item bg-color-black">
          <span>智算使用率</span>
          <AdminOverviewPanel style="display: flex;justify-content: center;" :id="rate[0].id" :tips="rate[0].tips"
            :colorObj="rate[0].colorData" />
        </div>
        <div class="item bg-color-black">
          <span>通算使用率</span>
          <AdminOverviewPanel style="display: flex;justify-content: center;" :id="rate[1].id" :tips="rate[1].tips"
            :colorObj="rate[1].colorData" />
        </div>
        <div class="water">
          <dv-water-level-pond class="dv-wa-le-po" :config="water" />
        </div>
      </div>
    </div>

    <div id="child-modal" v-if="showModal">
      <div class="modal-mask" @click="closeModal()"></div>
      <div class="modal" style="width: 600px;">
        <div class="modal-header">
          <h3 class="modal-title">{{ modalTitle }}</h3>
          <button class="modal-close" @click="closeModal()">×</button>
        </div>
        <div class="modal-body">
          <div v-if="modalType === 'dataCard'">
            <div class="all-platform-item">
              <div class="item-icon" style="background-color: rgba(100, 149, 237, 0.3)">
                <i class="el-icon-user" v-if="modalData.title.includes('团队成员')"></i>
                <i class="el-icon-folder" v-if="modalData.title.includes('内部项目')"></i>
                <i class="el-icon-folder-opened" v-if="modalData.title.includes('外部项目')"></i>
                <i class="el-icon-server" v-if="modalData.title.includes('智算服务器')"></i>
                <i class="el-icon-cloud" v-if="modalData.title.includes('通算服务器')"></i>
                <i class="el-icon-robot" v-if="modalData.title.includes('智能体')"></i>
              </div>
              <div class="item-info">
                <h4 class="item-name">{{ modalData.title }}</h4>
                <p class="item-desc">当前数量：{{ modalData.number.number[0] }} {{ modalData.title.match(/\((.*?)\)/)[1] }}
                </p>
                <div class="item-meta">
                  <span>数据更新时间：{{ updateTime }}</span>
                  <span class="item-status active">正常</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import AdminOverviewPanel from '@/components/echart-ai-office/percent-chart/AdminPercentChart.vue'

export default {
  components: {
    AdminOverviewPanel
  },
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
            style: { fontSize: 26 }
          }
        },
        {
          title: '内部项目(个)',
          number: {
            number: [18],
            toFixed: 0,
            textAlign: 'left',
            content: '{nt}',
            style: { fontSize: 26 }
          }
        },
        {
          title: '外部项目(个)',
          number: {
            number: [2],
            toFixed: 0,
            textAlign: 'left',
            content: '{nt}',
            style: { fontSize: 26 }
          }
        },
        {
          title: '智算服务器(台)',
          number: {
            number: [4],
            toFixed: 0,
            textAlign: 'left',
            content: '{nt}',
            style: { fontSize: 26 }
          }
        },
        {
          title: '通算服务器(台)',
          number: {
            number: [4],
            toFixed: 0,
            textAlign: 'left',
            content: '{nt}',
            style: { fontSize: 26 }
          }
        },
        {
          title: '智能体(个)',
          number: {
            number: [100],
            toFixed: 0,
            textAlign: 'left',
            content: '{nt}',
            style: { fontSize: 26 }
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
      rate: [
        {
          id: 'centerRate1',
          tips: 60,
          colorData: {
            textStyle: '#3fc0fb',
            series: {
              color: ['#00bcd44a', 'transparent'],
              dataColor: { normal: '#03a9f4', shadowColor: '#97e2f5' }
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
              dataColor: { normal: '#ff9800', shadowColor: '#fcebad' }
            }
          }
        }
      ],
      showModal: false,
      modalType: '',
      modalTitle: '',
      modalData: {},
      updateTime: '2025-10-31 10:00:00'
    }
  },
  methods: {
    openDetailModal(type, data) {
      this.modalType = type
      this.modalData = data
      this.modalTitle = data.title
      this.showModal = true
    },
    closeModal() {
      this.showModal = false
      this.modalType = ''
      this.modalData = {}
    }
  }
}
</script>

<style lang="scss" scoped>
@import "@/assets/scss/components/child-modal.scss";

#admin-overview-panel {
  display: flex;
  flex-direction: column;
  margin: 10px;

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
      background-color: #0f172a;
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
          color: #cfe2ff;
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

.colorBlue {
  color: #3fc0fb;
  font-size: 14px;
  margin: 0 0 4px 12px;
}
</style>
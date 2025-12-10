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
        <!-- <div class="item bg-color-black" @click="openDetailModal('rate', rate[0], '智算使用率')" style="cursor: pointer;">
          <span>智算使用率</span>
          <AdminPercentChart style="display: flex;justify-content: center;" :id="rate[0].id" :tips="rate[0].tips"
            :colorObj="rate[0].colorData" />
        </div>
        <div class="item bg-color-black" @click="openDetailModal('rate', rate[1], '通算使用率')" style="cursor: pointer;">
          <span>通算使用率</span>
          <AdminPercentChart style="display: flex;justify-content: center;" :id="rate[1].id" :tips="rate[1].tips"
            :colorObj="rate[1].colorData" />
        </div> -->

        <div class="water" @click="openDetailModal('water', water)" style="cursor: pointer;">
          <dv-water-level-pond class="dv-wa-le-po" :config="water" />
        </div>
      </div>
    </div>

    <div id="child-modal" v-if="showModal">
      <div class="modal-mask" @click="closeModal()"></div>
      <div class="modal" style="width: 800px;">
        <div class="modal-header">
          <h3 class="modal-title">{{ modalTitle }}</h3>
          <button class="modal-close" @click="closeModal()">×</button>
        </div>
        <div class="modal-body">
          <div v-if="modalType === 'dataCard'">
            <div class="all-platform-item">
              <div class="item-icon" :style="getIconStyle()">
                <span>{{ getIconSymbol() }}</span>
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

            <!-- 数据列表展示 -->
            <div class="all-platforms-list mt-4">
              <div class="all-platform-item" v-for="(item, index) in getDetailList()" :key="index">
                <div class="item-info"
                  style="flex-direction: row; align-items: center; justify-content: space-between;">
                  <div>
                    <h4 class="item-name">{{ item.name || item.title }}</h4>
                    <div class="item-meta">
                      <span v-if="item.role">{{ item.role }}</span>
                      <span v-if="item.status">{{ item.status }}</span>
                      <span v-if="item.model">{{ item.model }}</span>
                      <span v-if="item.type">{{ item.type }}</span>
                    </div>
                  </div>
                  <div class="item-meta">
                    <span v-if="item.joinDate">加入: {{ item.joinDate }}</span>
                    <span v-if="item.startDate">开始: {{ item.startDate }}</span>
                    <span v-if="item.purchaseDate">购置: {{ item.purchaseDate }}</span>
                    <span v-if="item.deploymentDate">部署: {{ item.deploymentDate }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 使用率详情 -->
          <div v-if="modalType === 'rate'">
            <div class="all-platform-item">
              <div class="item-icon" style="background-color: rgba(51, 206, 160, 0.3)">
                <span>📊</span>
              </div>
              <div class="item-info">
                <h4 class="item-name">{{ modalTitle }}</h4>
                <p class="item-desc">当前使用率：{{ modalData.tips }}%</p>
                <div class="item-meta">
                  <span>数据更新时间：{{ updateTime }}</span>
                  <span class="item-status active">监控中</span>
                </div>
              </div>
            </div>

            <div class="all-platforms-list mt-4">
              <div class="all-platform-item">
                <div class="item-info">
                  <h4 class="item-name">使用率趋势</h4>
                  <div class="item-meta">
                    <span>今日峰值: {{ modalData.tips + 10 }}%</span>
                    <span>昨日平均: {{ modalData.tips - 5 }}%</span>
                    <span>本周趋势: 上升</span>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 水位详情 -->
          <div v-if="modalType === 'water'">
            <div class="all-platform-item">
              <div class="item-icon" style="background-color: rgba(255, 152, 0, 0.3)">
                <span>🌊</span>
              </div>
              <div class="item-info">
                <h4 class="item-name">水位状态监控</h4>
                <p class="item-desc">当前水位：{{ modalData.data[0] }}%</p>
                <div class="item-meta">
                  <span>数据更新时间：{{ updateTime }}</span>
                  <span class="item-status maintenance">待优化</span>
                </div>
              </div>
            </div>

            <div class="all-platforms-list mt-4">
              <div class="all-platform-item">
                <div class="item-info">
                  <h4 class="item-name">水位变化记录</h4>
                  <div class="item-meta">
                    <span>最高水位: 75%</span>
                    <span>最低水位: 20%</span>
                    <span>平均水位: 45%</span>
                  </div>
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
// import AdminPercentChart from '@/components/echart-ai-office/percent-chart/AdminPercentChart.vue'

export default {
  components: {
    // AdminPercentChart
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
        // {
        //   title: '智算服务器(台)',
        //   number: {
        //     number: [4],
        //     toFixed: 0,
        //     textAlign: 'left',
        //     content: '{nt}',
        //     style: { fontSize: 26 }
        //   }
        // },
        // {
        //   title: '通算服务器(台)',
        //   number: {
        //     number: [4],
        //     toFixed: 0,
        //     textAlign: 'left',
        //     content: '{nt}',
        //     style: { fontSize: 26 }
        //   }
        // },
        // {
        //   title: '智能体(个)',
        //   number: {
        //     number: [27],
        //     toFixed: 0,
        //     textAlign: 'left',
        //     content: '{nt}',
        //     style: { fontSize: 26 }
        //   }
        // }
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
      // 各类型数据详情列表
      memberList: [
        { id: 1, name: '张明', role: '前端开发工程师', joinDate: '2023-01-15' },
        { id: 2, name: '李华', role: '后端开发工程师', joinDate: '2023-02-20' },
        { id: 3, name: '王芳', role: 'UI设计师', joinDate: '2023-03-10' },
        { id: 4, name: '赵伟', role: '算法工程师', joinDate: '2023-04-05' },
        { id: 5, name: '刘强', role: '项目经理', joinDate: '2023-01-05' }
      ],
      innerProjectList: [
        { id: 1, name: '智能分析平台', status: '进行中', startDate: '2023-03-01' },
        { id: 2, name: '数据管理系统', status: '已完成', startDate: '2022-11-15' },
        { id: 3, name: '自动化测试工具', status: '进行中', startDate: '2023-02-20' },
        { id: 4, name: '用户画像系统', status: '规划中', startDate: '2023-06-01' }
      ],
      outerProjectList: [
        { id: 1, name: '企业资源管理系统', status: '进行中', startDate: '2023-01-10' },
        { id: 2, name: '智能客服平台', status: '进行中', startDate: '2023-02-15' }
      ],
      aiServerList: [
        { id: 1, name: 'AI-01', model: 'NVIDIA A100', status: '运行中', purchaseDate: '2022-10-05' },
        { id: 2, name: 'AI-02', model: 'NVIDIA A100', status: '运行中', purchaseDate: '2022-10-05' },
        { id: 3, name: 'AI-03', model: 'AMD MI250', status: '维护中', purchaseDate: '2022-12-15' },
        { id: 4, name: 'AI-04', model: 'AMD MI250', status: '运行中', purchaseDate: '2022-12-15' }
      ],
      generalServerList: [
        { id: 1, name: 'Server-01', model: 'Dell PowerEdge R750', status: '运行中', purchaseDate: '2022-09-20' },
        { id: 2, name: 'Server-02', model: 'Dell PowerEdge R750', status: '运行中', purchaseDate: '2022-09-20' },
        { id: 3, name: 'Server-03', model: 'HPE ProLiant DL380', status: '运行中', purchaseDate: '2022-11-10' },
        { id: 4, name: 'Server-04', model: 'HPE ProLiant DL380', status: '运行中', purchaseDate: '2022-11-10' }
      ],
      agentList: [
        { id: 1, name: '客服智能体', type: '对话型', status: '已部署', deploymentDate: '2023-01-20' },
        { id: 2, name: '数据分析智能体', type: '分析型', status: '测试中', deploymentDate: '2023-03-15' },
        { id: 3, name: '文档处理智能体', type: '工具型', status: '已部署', deploymentDate: '2023-02-10' }
      ],
      showModal: false,
      modalType: '',
      modalTitle: '',
      modalData: {},
      updateTime: '2025-10-31 10:00:00'
    }
  },
  methods: {
    openDetailModal(type, data, title) {
      this.modalType = type
      this.modalData = data
      this.modalTitle = title || data.title
      this.showModal = true
    },
    closeModal() {
      this.showModal = false
      this.modalType = ''
      this.modalData = {}
    },
    // 根据不同类型返回对应的详情列表
    getDetailList() {
      switch (this.modalData.title) {
        case '团队成员(人)':
          return this.memberList;
        case '内部项目(个)':
          return this.innerProjectList;
        case '外部项目(个)':
          return this.outerProjectList;
        case '智算服务器(台)':
          return this.aiServerList;
        case '通算服务器(台)':
          return this.generalServerList;
        case '智能体(个)':
          return this.agentList;
        default:
          return [];
      }
    },
    // 获取图标样式
    getIconStyle() {
      const baseStyle = "width: 48px; height: 48px; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 24px; color: white;";

      switch (this.modalData.title) {
        case '团队成员(人)':
          return baseStyle + "background-color: rgba(100, 149, 237, 0.3);";
        case '内部项目(个)':
        case '外部项目(个)':
          return baseStyle + "background-color: rgba(75, 192, 192, 0.3);";
        case '智算服务器(台)':
        case '通算服务器(台)':
          return baseStyle + "background-color: rgba(255, 159, 64, 0.3);";
        case '智能体(个)':
          return baseStyle + "background-color: rgba(153, 102, 255, 0.3);";
        default:
          return baseStyle + "background-color: rgba(100, 149, 237, 0.3);";
      }
    },
    // 获取图标符号
    getIconSymbol() {
      switch (this.modalData.title) {
        case '团队成员(人)':
          return "👥";
        case '内部项目(个)':
        case '外部项目(个)':
          return "📁";
        case '智算服务器(台)':
        case '通算服务器(台)':
          return "💻";
        case '智能体(个)':
          return "🤖";
        default:
          return "📊";
      }
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
    height: 80px;
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
          height: 60px;
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

.mt-4 {
  margin-top: 16px;
}
</style>

<template>
  <div id="child-page" class="key-task" :style="{ width: containerWidth, height: containerHeight }">
    <div class="header-box">
      <span class="header-title">重点任务进度</span>
    </div>
    <div class="ranking">
      <!-- 滚动列表容器：绑定父组件传递的高度和内边距 -->
      <div class="scroll-list mt-1" :style="{
        padding: listPadding,
        height: listHeight,
        overflow: 'hidden',
        position: 'relative'
      }">
        <div class="list-content" :style="animationStyle" :class="{ 'no-animation': ranking.data.length < 5 }">
          <!-- 原任务列表：横向布局项目名和时间 -->
          <div class="task-item" v-for="(item, index) in ranking.data" :key="index">
            <div class="task-info">
              <!-- 横向容器包裹项目名和时间 -->
              <div class="task-name-person">
                <div class="task-name">{{ item.focusName }}</div>
              </div>
              <div class="task-time-group">
                <div class="task-leader">负责人：{{ item.focusPerson }}</div>
                <!-- <div class="task-time">开始：{{ formatTime(item.focusStartTime) }}</div>
                <div class="task-time split-line">|</div> 
                <div class="task-time">截止：{{ formatTime(item.focusEndTime) }}</div> -->
              </div>
            </div>
            <div class="custom-steps">
              <div class="custom-step" v-for="step in 4" :key="step"
                :class="{ 'step-finished': item.focusPersent > step, 'step-processing': item.focusPersent === step, 'step-pending': item.focusPersent < step }">
                <div class="step-head">
                  <span v-if="item.focusPersent > step" class="head-icon finished">✓</span>
                  <span v-else-if="item.focusPersent === step" class="head-icon processing">{{ step }}</span>
                  <span v-else class="head-icon pending">{{ step }}</span>
                </div>
                <div class="step-title">{{ stepTitles[step - 1] }}</div>
                <div class="step-description">{{ getStepDesc(item.focusPersent, step) }}</div>
              </div>
            </div>
          </div>
          <!-- 复制列表：同步横向布局 -->
          <div v-if="ranking.data.length >= 5">
            <div class="task-item" v-for="(item, index) in ranking.data" :key="index">
              <div class="task-info">
                <!-- 横向容器包裹项目名和时间 -->
                <div class="task-name-person">
                  <div class="task-name">{{ item.focusName }}</div>
                </div>
                <div class="task-time-group">
                  <div class="task-leader">负责人：{{ item.focusPerson }}</div>
                  <!-- <div class="task-time">开始：{{ formatTime(item.focusStartTime) }}</div>
                  <div class="task-time split-line">|</div> 
                  <div class="task-time">截止：{{ formatTime(item.focusEndTime) }}</div> -->
                </div>
              </div>
              <div class="custom-steps">
                <div class="custom-step" v-for="step in 4" :key="step"
                  :class="{ 'step-finished': item.focusPersent > step, 'step-processing': item.focusPersent === step, 'step-pending': item.focusPersent < step }">
                  <div class="step-head">
                    <span v-if="item.focusPersent > step" class="head-icon finished">✓</span>
                    <span v-else-if="item.focusPersent === step" class="head-icon processing">{{ step }}</span>
                    <span v-else class="head-icon pending">{{ step }}</span>
                  </div>
                  <div class="step-title">{{ stepTitles[step - 1] }}</div>
                  <div class="step-description">{{ getStepDesc(item.focusPersent, step) }}</div>
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
import { getFocusList } from '@/api/admin.js';

export default {
  // 新增：父组件可控制的props参数
  props: {
    // 组件整体宽高
    containerWidth: {
      type: String,
      default: '99%' // 原代码默认宽度99%
    },
    containerHeight: {
      type: String,
      default: 'auto' // 原代码默认自适应高度
    },
    // 滚动列表相关配置
    listHeight: {
      type: String,
      default: '410px' // 原代码默认列表高度410px
    },
    listPadding: {
      type: String,
      default: '0 15px' // 原代码默认列表内边距
    },
    // 任务信息区宽度（控制左侧信息区大小）
    taskInfoWidth: {
      type: String,
      default: '240px' // 原代码默认240px
    },
    // 轮播相关配置
    scrollThreshold: {
      type: Number,
      default: 5 // 原代码默认超5条启动轮播
    },
    scrollSpeed: {
      type: Number,
      default: 3 // 原代码默认每条滚动时长3秒
    }
  },
  data() {
    return {
      ranking: {
        data: [
          { focusName: '项目A', focusPerson: '张三', focusPersent: 1, focusStartTime: '2024-01-01', focusEndTime: '2024-03-31' },
          { focusName: '项目B', focusPerson: '李四', focusPersent: 2, focusStartTime: '2024-02-10', focusEndTime: '2024-04-20' },
          { focusName: '项目C', focusPerson: '王五', focusPersent: 3, focusStartTime: '2024-01-15', focusEndTime: '2024-05-10' },
          { focusName: '项目D', focusPerson: '张三', focusPersent: 4, focusStartTime: '2023-12-20', focusEndTime: '2024-02-28' },
          { focusName: '项目A', focusPerson: '张三', focusPersent: 1, focusStartTime: '2024-01-01', focusEndTime: '2024-03-31' },
          { focusName: '项目B', focusPerson: '李四', focusPersent: 2, focusStartTime: '2024-02-10', focusEndTime: '2024-04-20' },
          { focusName: '项目C', focusPerson: '王五', focusPersent: 3, focusStartTime: '2024-01-15', focusEndTime: '2024-05-10' },
          { focusName: '项目D', focusPerson: '张三', focusPersent: 4, focusStartTime: '2023-12-20', focusEndTime: '2024-02-28' }
        ]
      },
      stepTitles: ['需求分析', '技术方案', '开发交付', '测试上线']
    };
  },
  computed: {
    animationStyle() {
      // 使用props传递的阈值和速度计算动画时长
      if (this.ranking.data.length >= this.scrollThreshold) {
        return { animationDuration: `${this.ranking.data.length * this.scrollSpeed}s` };
      }
      return {};
    }
  },
  mounted() {
    this.loadKeyTaskProgressData();
  },
  methods: {
    async loadKeyTaskProgressData() {
      try {
        const focusResponse = await getFocusList();
        if (focusResponse?.rows?.length) {
          this.ranking.data = focusResponse.rows.map(item => ({
            focusName: item.focusName,
            focusPerson: item.focusPerson,
            focusPersent: Number(item.focusPersent),
            focusStartTime: item.focusStarttime,
            focusEndTime: item.focusEndtime
          }));
        }
        console.log('重点关注:', focusResponse, this.ranking.data);
      } catch (error) {
        // console.error('加载数据失败:', error);
      }
    },
    getStepDesc(currentValue, step) {
      return currentValue > step ? '已完成' : currentValue === step ? '进行中' : '未开始';
    },
    formatTime(timeStr) {
      if (!timeStr || timeStr === 'null' || timeStr === 'undefined') {
        return '未设置';
      }
      if (typeof timeStr === 'number') {
        timeStr = new Date(timeStr).toISOString().slice(0, 10);
      }
      return timeStr.slice(0, 10) || '未设置';
    }
  }
};
</script>

<style lang="scss" scoped>
@import '@/assets/scss/components/child-page.scss';

.key-task {
  box-sizing: border-box; // 新增：确保宽高包含内边距和边框
}

.bg-color-black {
  border-radius: 5px;
  min-height: 200px;
}

.ranking {
  display: flex;
  flex-direction: column;
  width: 100%;
  padding-bottom: 0;
  justify-content: space-between;

  .scroll-list {

    // 高度由props控制，此处不写固定值
    .list-content {
      width: 100%;
      animation: scrollUp linear infinite;
    }

    .list-content.no-animation {
      animation: none !important;
      transform: translateY(0) !important;
    }

    .task-item {
      display: flex;
      align-items: flex-start;
      padding: 8px 0;
      border-bottom: 1px dashed #333;
      color: #fff;

      .task-info {
        /* 2. 核心：横向布局项目名和时间 */
        .task-name-person {
          display: flex;
          align-items: center;
          gap: 15px;
          /* 项目名和时间之间的间距 */
          margin-bottom: 8px;
          /* 与负责人之间的间距 */
        }

        .task-name {
          font-size: 14px;
          white-space: nowrap;
          /* 项目名不换行 */
          overflow: hidden;
          text-overflow: ellipsis;
          /* 超长时显示省略号 */
          flex: 0 0 200px;
          /* 固定项目名宽度，避免挤压时间 */
        }

        /* 3. 时间组横向排列 */
        .task-time-group {
          display: flex;
          align-items: center;
          gap: 10px;
          /* 时间项之间的间距 */
          flex: 1;
          /* 时间组占剩余宽度 */
        }

        .task-time {
          font-size: 12px;
          color: #909399;
          white-space: nowrap;
          /* 时间不换行 */
        }

        /* 4. 时间分隔符样式 */
        .split-line {
          color: #666;
          /* 分隔符灰色，不刺眼 */
          font-size: 14px;
        }

        .task-leader {
          font-size: 12px;
          color: #999;
        }
      }
    }
  }
}

/* 自定义步骤条样式（保留不变） */
.custom-steps {
  display: flex;
  width: 100%;
}

.custom-step {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;

  &:not(:last-child)::after {
    content: '';
    position: absolute;
    top: 10px;
    right: -50%;
    width: 100%;
    height: 2px;
    background-color: #eaeefb;
    z-index: 1;
  }
}

.step-head {
  width: 20px;
  height: 20px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 4px;
  z-index: 2;
  border: 1px solid #c0c4cc;
}

.head-icon {
  font-size: 12px;
  font-weight: bold;
  color: #c0c4cc;
}

.step-finished .step-head {
  background-color: #67c23a;
  border-color: #67c23a;
}

.step-finished .head-icon.finished {
  color: white;
}

.step-finished:not(:last-child)::after {
  background-color: #67c23a;
}

.step-processing .step-head {
  background-color: #409eff;
  border-color: #409eff;
}

.step-processing .head-icon.processing {
  color: white;
}

.step-processing:not(:last-child)::after {
  background-color: #409eff;
}

.step-pending .step-head {
  background-color: #909399;
}

.step-pending .head-icon.pending {
  color: white;
}

.step-pending:not(:last-child)::after {
  background-color: #909399;
}

.step-title {
  font-size: 12px;
  margin-bottom: 2px;
}

.step-description {
  font-size: 11px;
  color: #909399;
}

@keyframes scrollUp {
  0% {
    transform: translateY(0);
  }

  100% {
    transform: translateY(-50%);
  }
}
</style>
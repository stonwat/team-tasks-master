<template>
  <div id="child-page" class="todo-list" :style="{ width: containerWidth, height: containerHeight }">
    <div class="ranking">
      <div class="header-box">
        <span class="header-title">培训/会议</span>
      </div>
      <!-- 滚动列表容器：绑定核心props -->
      <div class="scroll-list mt-1" :style="{
        padding: listPadding,
        height: listHeight,
        overflow: 'hidden',
        position: 'relative'
      }">
        <div class="list-content" :style="animationStyle"
          :class="{ 'no-animation': todoList.length < scrollThreshold }">
          <!-- 主任务列表 -->
          <div class="todo-item" v-for="(item, index) in todoList" :key="index">
            <div class="todo-info">
              <div class="todo-name-person">
                <div class="todo-name">{{ item.name }}</div>
                <div class="todo-location">地点：{{ item.location }}</div>
              </div>
              <div class="todo-time">时间：{{ formatDate(item.date) }}</div>
            </div>
          </div>
          <!-- 复制列表（保持滚动连续性） -->
          <div v-if="todoList.length >= scrollThreshold">
            <div class="todo-item" v-for="(item, index) in todoList" :key="index">
              <div class="todo-info">
                <div class="todo-name-person">
                  <div class="todo-name">{{ item.name }}</div>
                  <div class="todo-location">地点：{{ item.location }}</div>
                </div>
                <div class="todo-time">时间：{{ formatDate(item.date) }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getTrainList } from '@/api/admin.js';

export default {
  props: {
    // 核心尺寸控制
    containerWidth: {
      type: String,
      default: '99%'
    },
    containerHeight: {
      type: String,
      default: 'auto'
    },
    // 滚动列表核心配置
    listHeight: {
      type: String,
      default: '360px' // 原默认高度
    },
    listPadding: {
      type: String,
      default: '0 15px' // 原默认内边距
    },
    // 轮播核心配置
    scrollThreshold: {
      type: Number,
      default: 2 // 原默认阈值
    },
    scrollSpeed: {
      type: Number,
      default: 3 // 原默认滚动速度（秒/条）
    }
  },
  data() {
    return {
      todoList: [
        { name: '产品功能培训', location: '一楼会议室A', date: '2025-10-22' },
        { name: '技术架构研讨会', location: '二楼研讨室', date: '2025-10-25' },
        { name: '客户需求沟通会', location: '线上腾讯会议', date: '2025-10-28' },
        { name: '新员工入职培训', location: '培训室101', date: '2025-11-01' },
        { name: '项目进度同步会', location: '三楼会议室B', date: '2025-11-05' }
      ]
    };
  },
  computed: {
    animationStyle() {
      if (this.todoList.length >= this.scrollThreshold) {
        return { animationDuration: `${this.todoList.length * this.scrollSpeed}s` };
      }
      return {};
    }
  },
  mounted() {
    this.loadTodoList();
  },
  methods: {
    async loadTodoList() {
      try {
        const res = await getTrainList();
        if (res?.rows?.length) {
          this.todoList = res.rows.map(item => ({
            name: item.trainName,
            location: item.trainPlace,
            date: item.trainTime
          }));
        }
        console.log('代办清单数据加载成功：', this.todoList);
      } catch (err) {
        // console.error('加载代办清单失败：', err);
      }
    },
    formatDate(dateStr) {
      if (!dateStr || dateStr === 'null' || dateStr === 'undefined') {
        return '未设置';
      }
      return dateStr.slice(0, 10) || '未设置';
    }
  }
};
</script>

<style lang="scss" scoped>
@import '@/assets/scss/components/child-page.scss';

.todo-list {
  box-sizing: border-box;
}

.ranking {
  display: flex;
  flex-direction: column;
  width: 100%;
  padding-bottom: 0;
  justify-content: space-between;

  .scroll-list {
    .list-content {
      width: 100%;
      animation: scrollUp linear infinite;
    }

    .list-content.no-animation {
      animation: none !important;
      transform: translateY(0) !important;
    }

    .todo-item {
      display: flex;
      align-items: flex-start;
      padding: 8px 0;
      /* 保留原默认内边距，无需通过props控制 */
      border-bottom: 1px dashed #333;
      color: #fff;

      .todo-info {
        display: flex;
        justify-content: space-between;
        width: 100%;

        .todo-name-person {
          display: flex;
          flex-direction: column;
          gap: 4px;
        }

        .todo-name {
          font-size: 14px;
          /* 保留原默认字号 */
          font-weight: 500;
        }

        .todo-location,
        .todo-time {
          font-size: 12px;
          color: #909399;
        }

        .todo-time {
          white-space: nowrap;
        }
      }
    }
  }
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